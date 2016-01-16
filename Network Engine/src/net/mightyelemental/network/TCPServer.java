package net.mightyelemental.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TCPServer extends Server {

	private Map<String, TCPConnection> tcpConnections = new HashMap<String, TCPConnection>();

	private ServerSocket serverSocket;

	private boolean usesEncryption = false;

	private Thread serverTick = new Thread("ServerThread") {

		public void run() {
			while (running) {
				try {
					Socket newClientSocket = serverSocket.accept();
					TCPConnection newTcpClient = new TCPConnection(newClientSocket, initiater, serverGUI, usesEncryption);
					String UID = generateClientInfo(newTcpClient, random);
					newTcpClient.setUID(UID);
					newTcpClient.startThread();
					tcpConnections.put(UID, newTcpClient);

					initiater.onNewClientAdded(newClientSocket.getInetAddress(), newClientSocket.getPort(), UID);

					if (hasGUI) {
						if (serverGUI != null) {
							serverGUI.updateClients();
						}
					}

					// lastMessage = inFromClient.readLine();
					// lastMessage = BasicCommands.decryptMessageBase64(lastMessage);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	/** TCP Server */
	public TCPServer( int port, boolean usesEncryption ) {
		this.port = port;
		this.usesEncryption = usesEncryption;
	}

	public synchronized void setupServer() {

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.running = true;

		serverTick.start();
	}

	/** Get the TCP Connection for the specified UID */
	public TCPConnection getTCPConnectionFromUID(String UID) {
		return tcpConnections.get(UID);
	}

	/** Get the TCP Connection for the specified IP */
	public TCPConnection getTCPConnectionFromIP(InetAddress ip, int port) {
		for (TCPConnection tcp : tcpConnections.values()) {
			if (ip.equals(tcp.getIp()) && port == tcp.getPort()) { return tcp; }
		}
		return null;
	}

	/** Sends a message to a client
	 * 
	 * @param message
	 *            the message to send
	 * @param ip
	 *            the IP address of the client
	 * @param port
	 *            the port of the client */
	public synchronized void sendMessage(String message, InetAddress ip, int port) {
		try {
			getTCPConnectionFromIP(ip, port).sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	@Override
	public void broadcastmessage(String message) {
		for (String key : getTcpConnections().keySet()) {
			try {
				getTcpConnections().get(key).sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stopServer() throws InterruptedException, IOException {
		this.serverTick.join(1000);
		serverSocket.close();
	}

	@Override
	public void sendBytes(byte[] bytes, InetAddress ip, int port) {
		try {
			getTCPConnectionFromIP(ip, port).sendBytes(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
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

}
