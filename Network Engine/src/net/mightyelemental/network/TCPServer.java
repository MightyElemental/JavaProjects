package net.mightyelemental.network;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TCPServer extends Server implements Runnable {
	
	
	private Map<String, TCPConnection> tcpConnections = new HashMap<String, TCPConnection>();
	
	private ServerSocket serverSocket;
	
	private Thread serverTick = new Thread(this);
	
	public void run() {
		while (running) {
			try {
				if (serverSocket == null) {
					continue;
				}
				Socket newClientSocket = serverSocket.accept();
				TCPConnection newTcpClient = new TCPConnection(newClientSocket, this);
				String UID = generateClientInfo(newTcpClient, random);
				newTcpClient.setUID(UID);
				newTcpClient.timeOfVerifyRequest = System.currentTimeMillis();
				newTcpClient.startThread();
				tcpConnections.put(UID, newTcpClient);
				newTcpClient.sendObject("VerifyCodeRequest", "Please Send Verify Code");
				initiater.onNewClientAdded(newClientSocket.getInetAddress(), newClientSocket.getPort(), UID);
				
				if (hasGUI) {
					if (serverGUI != null) {
						serverGUI.updateClients();
					}
				}
				
				// lastMessage = inFromClient.readLine();
				// lastMessage =
				// BasicCommands.decryptMessageBase64(lastMessage);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			serverTick.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/** @param port
	 *            - the port of which the server should run
	 * @param maxBytes
	 *            - the maximum amount of bytes the server should be able to send
	 * @param usesEncryption
	 *            - whether or not the server should use encryption
	 * @param verifyCode
	 *            - used to ensure that connecting clients are from the correct game */
	public TCPServer( int port, boolean usesEncryption, int maxBytes, String verifyCode ) {
		super(port, usesEncryption, maxBytes, verifyCode);
	}
	
	public synchronized void setupServer() throws BindException, IOException {
		
		// try {
		serverSocket = new ServerSocket(port);
		// } catch (BindException e) {
		// System.err.println("There is already a server running on that port!");
		// System.err.println("Make sure you are not using the same port as any other server on your network.");
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }
		this.running = true;
		
		serverTick.start();
		hasBeenSetup = true;
	}
	
	/** Get the TCP Connection for the specified UID */
	public TCPConnection getTCPConnectionFromUID(String UID) {
		return tcpConnections.get(UID);
	}
	
	/** Get the TCP Connection for the specified IP */
	public TCPConnection getTCPConnectionFromIP(InetAddress ip, int port) {
		Map<String, TCPConnection> tcpConTemp = tcpConnections;
		for (TCPConnection tcp : tcpConTemp.values()) {
			if (ip.equals(tcp.getIp()) && port == tcp.getPort()) { return tcp; }
		}
		return null;
	}
	
	/** Adds client UID to array and makes sure its unique
	 * 
	 * @return uid the client's UID */
	public String generateClientInfo(TCPConnection tcpCon, Random rand) {
		String chars = BasicCommands.generateClientUID(rand);
		while (tcpConnections.containsKey(chars)) {
			chars = BasicCommands.generateClientUID(rand);
		}
		// System.out.println("New client! " + chars + " | IP: " + ip.getHostAddress() + ":" + port);
		tcpConnections.put(chars, tcpCon);
		return chars;
	}
	
	/** @return the tcpConnections */
	public Map<String, TCPConnection> getTcpConnections() {
		return tcpConnections;
	}
	
	/** Used to stop a connection with the client.
	 * 
	 * @param UID
	 *            the uid of the client connection
	 * @throws InterruptedException
	 * @throws IOException */
	public void killConnection(String UID) throws IOException, InterruptedException {
		if (tcpConnections.get(UID) != null) {
			tcpConnections.get(UID).stopThread();
		}
	}
	
	@Override
	public void stopServer() throws InterruptedException, IOException {
		this.serverTick.join(1000);
		serverSocket.close();
	}
	
	/** @return the usesEncryption */
	public boolean doesUseEncryption() {
		return usesEncryption;
	}
	
	/** @param usesEncryption
	 *            the usesEncryption to set */
	public void setUseEncryption(boolean usesEncryption) {
		this.usesEncryption = usesEncryption;
	}
	
	/** @return the maxBytes */
	public int getMaxBytes() {
		return maxBytes;
	}
	
	/** @param maxBytes
	 *            the maxBytes to set */
	public void setMaxBytes(int maxBytes) {
		this.maxBytes = maxBytes;
		for (TCPConnection t : tcpConnections.values()) {
			t.maxBytes = maxBytes;
		}
	}
	
	@Override
	public void sendObject(String varName, Object obj, InetAddress ip, int port) throws IOException {
		if (!hasBeenSetup) {
			System.err.println("FATAL ERROR: Server has not been setup yet!");
			return;
		}
		if (getTCPConnectionFromIP(ip, port) != null) {
			getTCPConnectionFromIP(ip, port).sendObject(varName, obj);
		} else {
			System.err.println("FATAL ERROR: Connection is a null value");
		}
	}
	
	@Override
	public void sendObjectMap(Map<String, Object> objects, InetAddress ip, int port) throws IOException {
		if (!hasBeenSetup) {
			System.err.println("FATAL ERROR: Server has not been setup yet!");
			return;
		}
		if (getTCPConnectionFromIP(ip, port) != null) {
			getTCPConnectionFromIP(ip, port).sendMap(objects);
		} else {
			System.err.println("FATAL ERROR: Connection is a null value");
		}
	}
	
	public void onVerifyDenied(String uid) throws IOException {
		getTCPConnectionFromUID(uid).sendObject("ServerMessage", "Your Client Verification Code Does Not Match The Server Code!");
	}
	
	public void onVerified(String uid) throws IOException {
		getTCPConnectionFromUID(uid).sendObject("ServerMessage", "Your Client Has Been Verified");
	}
	
}
