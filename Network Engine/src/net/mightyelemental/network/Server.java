package net.mightyelemental.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.mightyelemental.network.gui.ServerGUI;
import net.mightyelemental.network.listener.MessageListenerServer;
import net.mightyelemental.network.listener.ServerInitiater;

public class Server {

	private int		port;
	public boolean	running;

	public static Random random = new Random();

	public DatagramSocket serverSocket;

	public Map<String, List<Object>> attachedClients = new HashMap<String, List<Object>>();

	public byte[]	receiveData;
	public byte[]	sendData;

	private ServerInitiater initiater = new ServerInitiater();

	private String	lastMessage	= "";
	private boolean	parse		= true;

	private boolean		hasGUI;
	private ServerGUI	serverGUI;

	private Thread serverTick = new Thread("ServerThread") {

		public void run() {

			receiveData = new byte[1024];
			sendData = new byte[1024];

			while (running) {
				receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				try {

					serverSocket.receive(receivePacket);
					String data = new String(receivePacket.getData()).trim();

					InetAddress IPAddress = receivePacket.getAddress();

					int port = receivePacket.getPort();

					String[] dataArray = data.split(" : ");

					StringBuilder sb = new StringBuilder();

					for (int i = 1; i < dataArray.length; i++) {
						sb.append(dataArray[i]);
					}

					String message = sb.toString();
					// String sender = dataArray[0];
					if (lastMessage.equals(message)) {
						parse = false;
					} else {
						lastMessage = message;
						parse = true;
					}

					if (parse) {
						initiater.onMessageRecieved(message, IPAddress);
						handleMessage(IPAddress, port);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (hasGUI) {
					if (serverGUI != null) {
						serverGUI.updateClients(getAttachedClients());
					}
				}

			}

			try {
				this.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	};

	/** UDP Server */
	public Server( int port ) {
		this.port = port;
	}

	/** @return the port the server is running on */
	public int getPort() {
		return this.port;
	}

	/** This is required to start the server thread & to create a new socket */
	public synchronized void setupServer() {

		try {
			serverSocket = new DatagramSocket(port);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		this.running = true;

		serverTick.start();
	}

	/** Checks to see if the client was connected before. If not, it will add the client with a new UID */
	private void handleMessage(InetAddress ip, int port) {
		if (attachedClients.containsValue(Arrays.asList(new Object[] { ip, port }))) { return; }
		String UID = generateClientInfo(ip, port, random);
		initiater.onNewClientAdded(ip, port, UID);// notifies all listeners about new client
	}

	/** Adds client UID to array and makes sure its unique
	 * 
	 * @return uid the client's UID */
	private String generateClientInfo(InetAddress ip, int port, Random rand) {
		String chars = generateClientUID(rand);
		while (attachedClients.containsKey(chars)) {
			chars = generateClientUID(rand);
		}
		// System.out.println("New client! " + chars + " | IP: " + ip.getHostAddress() + ":" + port);
		attachedClients.put(chars, Arrays.asList(new Object[] { ip, port }));
		return chars;
	}

	/** Creates a UID for a new IP/Port */
	private String generateClientUID(Random rand) {
		String chars = "";

		for (int i = 0; i < 6; i++) {
			chars += (char) (rand.nextInt(26) + 'a');
		}
		return chars;
	}

	/** Gets the servers External IP Address. <br>
	 * It uses <a href="http://checkip.amazonaws.com">'http://checkip.amazonaws.com'</a> to do so. */
	public static String getExternalIPAddress() {
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine(); // you get the IP as a String
			return ip;
		} catch (Exception e) {
		}
		return "0.0.0.0";
	}

	/** @return the attachedClients<br>
	 *         Uses ClientUID as key. The List it gives is an 'InetAddress' and an 'int', in that order. */
	public Map<String, List<Object>> getAttachedClients() {
		return attachedClients;
	}

	/** Setup the built in GUI */
	public void initGUI(String title) {
		serverGUI = new ServerGUI(title, this);
		this.hasGUI = true;
	}

	/** Adds listener to initiater
	 * 
	 * @param mls
	 *            the MessageListenerServer instance */
	public void addListener(MessageListenerServer mls) {
		initiater.addListener(mls);
	}

	/** Sends a message to a client
	 * 
	 * @param message
	 *            the message to send
	 * @param ip
	 *            the IP address of the client
	 * @param port
	 *            the port of the client */
	public void sendMessage(String message, InetAddress ip, int port) {
		String sendMessage = message;

		try {
			sendData = (sendMessage.toString()).getBytes("UTF-8");
			DatagramPacket sendPacket = new DatagramPacket(this.sendData, this.sendData.length, ip, port);
			this.serverSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Broadcast a message to every client
	 * 
	 * @param message
	 *            the message to be sent */
	public void broadcastmessage(String message) {
		Object[] keys = this.getAttachedClients().keySet().toArray();
		for (Object key : keys) {
			InetAddress ip = (InetAddress) this.getAttachedClients().get(key).toArray()[0];
			int port = Integer.parseInt(this.getAttachedClients().get(key).toArray()[1] + "");
			sendMessage(message, ip, port);
		}
	}
}
