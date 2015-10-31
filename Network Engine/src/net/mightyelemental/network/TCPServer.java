package net.mightyelemental.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.mightyelemental.network.listener.MessageListenerServer;
import net.mightyelemental.network.listener.ServerInitiater;

public class TCPServer {

	private int		port;
	private boolean	running;

	private static Random random = new Random();

	private ServerInitiater initiater = new ServerInitiater();

	private Map<String, TCPConnection> tcpConnections = new HashMap<String, TCPConnection>();

	ServerSocket welcomeSocket;

	// This instance of the server
	private TCPServer thisServer = this;

	private Thread serverTick = new Thread("ServerThread") {

		public void run() {
			while (running) {
				try {
					Socket newClientSocket = welcomeSocket.accept();
					TCPConnection newTcpClient = new TCPConnection(newClientSocket, initiater, thisServer);
					String UID = generateClientInfo(newTcpClient, random);
					newTcpClient.setUID(UID);
					tcpConnections.put(UID, newTcpClient);

					initiater.onNewClientAdded(newClientSocket.getInetAddress(), newClientSocket.getPort(), UID);

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
	public TCPServer( int port ) {
		this.port = port;
	}

	/** Adds listener to initiater
	 * 
	 * @param mls
	 *            the MessageListenerServer instance */
	public void addListener(MessageListenerServer mls) {
		initiater.addListener(mls);
	}

	public synchronized void setupServer() {

		try {
			welcomeSocket = new ServerSocket(port);
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
	public synchronized void sendMessage(String message, InetAddress ip, int port) throws InterruptedException {
		try {
			getTCPConnectionFromIP(ip, port).sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Adds client UID to array and makes sure its unique
	 * 
	 * @return uid the client's UID */
	private String generateClientInfo(TCPConnection tcpCon, Random rand) {
		String chars = BasicCommands.generateClientUID(rand);
		while (tcpConnections.containsKey(chars)) {
			chars = BasicCommands.generateClientUID(rand);
		}
		// System.out.println("New client! " + chars + " | IP: " + ip.getHostAddress() + ":" + port);
		tcpConnections.put(chars, tcpCon);
		try {
			this.sendMessage(chars, tcpCon.getIp(), tcpCon.getPort());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return chars;
	}

	/** @return the tcpConnections */
	public Map<String, TCPConnection> getTcpConnections() {
		return tcpConnections;
	}

}
