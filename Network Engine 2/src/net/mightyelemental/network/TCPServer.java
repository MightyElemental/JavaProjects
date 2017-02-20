package net.mightyelemental.network;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.mightyelemental.network.gui.ServerGUI;
import net.mightyelemental.network.listener.MessageListenerServer;
import net.mightyelemental.network.listener.ServerInitiater;

public class TCPServer implements Runnable {
	
	
	protected int port;
	public boolean running;
	
	protected boolean hasGUI;
	protected boolean hasBeenSetup = false;
	
	protected boolean usesEncryption = false;
	protected int maxBytes = 2 ^ 10;
	
	protected String verifyCode = "NONE";
	
	protected ServerGUI serverGUI;
	
	protected static Random random = new Random();
	public Map<String, Object> objectToSend = new HashMap<String, Object>();
	
	protected ServerInitiater initiater = new ServerInitiater();
	
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
	 * @param verifyCode
	 *            - used to ensure that connecting clients are from the correct game */
	public TCPServer( int port, String verifyCode ) {
		this.port = port;
		this.verifyCode = verifyCode;
	}
	
	/** Used to initialise the server - this is essential for the server to run properly
	 * 
	 * @throws BindException
	 *             when the server fails to bind with the specified port
	 * @throws IOException
	 *             when the server fails to load correctly */
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
	
	/** Get the TCP Connection for the specified UID
	 * 
	 * @param UID
	 *            the client UID */
	public TCPConnection getTCPConnectionFromUID(String UID) {
		return tcpConnections.get(UID);
	}
	
	/** Get the TCP Connection for the specified IP
	 * 
	 * @param ip
	 *            the client's IP address
	 * @param port
	 *            the client's port that they are using to connect */
	public TCPConnection getTCPConnectionFromIP(InetAddress ip, int port) {
		Map<String, TCPConnection> tcpConTemp = new HashMap<String, TCPConnection>();
		tcpConTemp.putAll(tcpConnections);
		for (TCPConnection tcp : tcpConTemp.values()) {
			if (ip.equals(tcp.getIp()) && port == tcp.getPort()) { return tcp; }
		}
		return null;
	}
	
	/** Ensures a unique UID for every client and adds the UID and the TCPConnection to the map
	 * 
	 * @param tcpCon
	 *            the TCPConnection that you are adding to the map
	 * @param rand
	 *            a Random instance
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
	
	/** Used to get all connections
	 * 
	 * @return the tcpConnections */
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
	
	/** Used to stop the server */
	public void stopServer() throws InterruptedException, IOException {
		this.serverTick.interrupt();
		this.serverTick.join(1000);
		serverSocket.close();
	}
	
	/** Used to send an object to a client
	 * 
	 * @param varName
	 *            the key for the message. E.G. 'ServerMessage'
	 * @param obj
	 *            the Object you are sending
	 * @param ip
	 *            the IP address of the client
	 * @param port
	 *            the port of the client */
	public void sendObject(String varName, Object obj, InetAddress ip, int port) throws IOException {
		if (!hasBeenSetup) {
			System.err.println("FATAL ERROR: Server has not been setup yet!");
			return;
		}
		try {
			getTCPConnectionFromIP(ip, port).sendObject(varName, obj);
		} catch (Exception e) {
			System.err.println("FATAL ERROR: Connection is a null value");
		}
	}
	
	public void sendObjectMap(Map<String, Object> map, InetAddress ip, int port) {
		if (!hasBeenSetup) {
			System.err.println("FATAL ERROR: Server has not been setup yet!");
			return;
		}
		try {
			getTCPConnectionFromIP(ip, port).sendMap(map);
		} catch (Exception e) {
			System.err.println("FATAL ERROR: Connection is a null value");
		}
	}
	
	/** This method will be initiated when the client fails to verify their code
	 * 
	 * @param uid
	 *            the UID of the client who failed to verify their code
	 * @throws IOException
	 *             when there is an issue with the connection */
	public void onVerifyDenied(String uid) throws IOException {
		getTCPConnectionFromUID(uid).sendObject("ServerMessage", "Your Client Verification Code Does Not Match The Server Code!");
	}
	
	/** This method will be initiated when the client verifies their code
	 * 
	 * @param uid
	 *            the UID of the client who verified their code
	 * @throws IOException
	 *             when there is an issue with the connection */
	public void onVerified(String uid) throws IOException {
		getTCPConnectionFromUID(uid).sendObject("ServerMessage", "Your Client Has Been Verified");
	}
	
	/** Adds listener to initiator
	 * 
	 * @param mls
	 *            the MessageListenerServer instance */
	public void addListener(MessageListenerServer mls) {
		initiater.addListener(mls);
	}
	
	/** Get the GUI that the server is attached to */
	public ServerGUI getGUI() {
		return serverGUI;
	}
	
	/** Setup the built in GUI */
	public void initGUI(String title) {
		serverGUI = new ServerGUI(title, this, BasicCommands.getExternalIPAddress() + ":" + this.getPort());
		this.hasGUI = true;
	}
	
	/** Setup the built in GUI */
	public void initGUI(ServerGUI frame) {
		serverGUI = frame;
		this.hasGUI = true;
	}
	
	/** @return the port the server is running on */
	public int getPort() {
		return this.port;
	}
	
	/** @return the verification code that the server is using */
	public String getVerifyCode() {
		return verifyCode;
	}
	
}
