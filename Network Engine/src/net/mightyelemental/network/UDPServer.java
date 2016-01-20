package net.mightyelemental.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.mightyelemental.network.gui.ServerGUI;

public class UDPServer extends Server {

	private int		port;
	private boolean	running;

	private boolean stopServer;

	private boolean usesEncryption = false;

	public DatagramSocket serverSocket;

	public Map<String, List<Object>> attachedClients = new HashMap<String, List<Object>>();

	private byte[]	receiveData;
	private byte[]	sendData;

	protected ObjectInputStream		ois;
	protected ObjectOutputStream	ous;

	private String	lastMessage	= "";
	private int		maxBytes	= 1024;
	// private boolean parse = true;

	private Thread serverTick = new Thread("ServerThread") {

		public void run() {

			receiveData = new byte[maxBytes];
			sendData = new byte[maxBytes];

			while (running) {
				if (serverSocket == null) {
					running = false;
					break;
				} else {
					if (serverSocket.isClosed()) {
						running = false;
						break;
					}
				}
				if (stopServer) {
					running = false;
					break;
				}
				receiveData = new byte[maxBytes];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				try {

					ois = new ObjectInputStream(new ByteArrayInputStream(receivePacket.getData()));
					serverSocket.receive(receivePacket);

					InetAddress IPAddress = receivePacket.getAddress();
					int port = receivePacket.getPort();

					try {
						initiater.onObjectRecieved(IPAddress, port, ois.readObject());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					checkIfNewClient(IPAddress, port);

					// String data = new String(receivePacket.getData()).trim();
					// if (usesEncryption) {
					// data = BasicCommands.decryptMessageBase64(data);
					// }
					//
					// String[] dataArray = data.split(" : ");
					//
					// StringBuilder sb = new StringBuilder();
					//
					// for (int i = 1; i < dataArray.length; i++) {
					// sb.append(dataArray[i]);
					// }
					//
					// String message = sb.toString();
					// lastMessage = message;
					// if (message.contains("JLB1F0_TEST_CONNECTION RETURN_UID")) {
					// sendMessage("JLB1F0_CLIENT_UID " + getClientUIDFromIP(IPAddress, port), IPAddress, port);
					// } else if (message.contains("JLB1F0_PING_SERVER")) {
					// returnPingRequest(IPAddress, port);
					// } else {
					// serverGUI.addCommand(getClientUIDFromIP(IPAddress, port) + " : " + message);
					// }
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (hasGUI) {
					if (serverGUI != null) {
						serverGUI.updateClients();
					}
				}

			}

			try {
				stopServer = false;
				this.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	};

	/** UDP Server
	 * 
	 * @param port
	 *            - the port of which the server should run
	 * @param maxBytes
	 *            - the maximum amount of bytes the server should be able to send */
	public UDPServer( int port, int maxBytes, boolean usesEncryption ) {
		this.port = port;
		this.maxBytes = maxBytes;
		this.usesEncryption = usesEncryption;
	}

	/** Adds a message to the server GUI */
	public void addMessageToConsole(String message) {
		serverGUI.addCommand(message);
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
	private void checkIfNewClient(InetAddress ip, int port) {
		if (attachedClients.containsValue(Arrays.asList(new Object[] { ip, port }))) { return; }
		String UID = generateClientInfo(ip, port, random);
		try {
			sendObject("JLB1F0_CLIENT_UID " + getClientUIDFromIP(ip, port), ip, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		initiater.onNewClientAdded(ip, port, UID);// notifies all listeners about new client
	}

	/** Return the client UID associated with the given IP and port */
	public String getClientUIDFromIP(InetAddress ip, int port) {
		List<Object> tester = Arrays.asList(new Object[] { ip, port });
		if (!attachedClients.containsValue(tester)) { return null; }
		for (String key : attachedClients.keySet()) {
			if (attachedClients.get(key).equals(tester)) { return key; }
		}
		return null;
	}

	/** Adds client UID to array and makes sure its unique
	 * 
	 * @return uid the client's UID */
	private String generateClientInfo(InetAddress ip, int port, Random rand) {
		String chars = BasicCommands.generateClientUID(rand);
		while (attachedClients.containsKey(chars)) {
			chars = BasicCommands.generateClientUID(rand);
		}
		// System.out.println("New client! " + chars + " | IP: " + ip.getHostAddress() + ":" + port);
		attachedClients.put(chars, Arrays.asList(new Object[] { ip, port }));
		this.sendMessage(chars, ip, port);
		return chars;
	}

	/** @return the attachedClients<br>
	 *         Uses ClientUID as key. The List it gives is an 'InetAddress' and an 'int', in that order. */
	public Map<String, List<Object>> getAttachedClients() {
		return attachedClients;
	}

	/** Setup the built in GUI */
	public void initGUI(String title) {
		serverGUI = new ServerGUI(title, this, BasicCommands.getExternalIPAddress() + ":" + this.getPort());
		this.hasGUI = true;
	}

	/** Sends a message, instantly, to a client
	 * 
	 * @param message
	 *            the message to send
	 * @param ip
	 *            the IP address of the client
	 * @param port
	 *            the port of the client */
	@Deprecated
	public void sendInstantMessage(String message, InetAddress ip, int port) {

		String cUID = getClientUIDFromIP(ip, port);

		if (!message.contains("JLB1F0") && !message.contains("JLB1F0_CLIENT_UID") && serverGUI != null) {
			serverGUI.addCommand("Console > " + cUID + " : " + message);
		}

		if (usesEncryption) {
			message = BasicCommands.encryptMessageBase64(message);
		}

		try {
			sendData = (message.toString()).getBytes("UTF-8");
			DatagramPacket sendPacket = new DatagramPacket(this.sendData, this.sendData.length, ip, port);
			this.serverSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Waits 100ms then sends a message to a client
	 * 
	 * @param message
	 *            the message to send
	 * @param ip
	 *            the IP address of the client
	 * @param port
	 *            the port of the client */
	@Deprecated
	public synchronized void sendMessage(String message, InetAddress ip, int port) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sendInstantMessage(message, ip, port);
	}

	/** Sends the specified client a byte array
	 * 
	 * @param bytes
	 *            the byte array to send
	 * @param ip
	 *            the IP address of the client
	 * @param port
	 *            the port of the client */
	@Deprecated
	public synchronized void sendBytes(byte[] bytes, InetAddress ip, int port) {
		String cUID = getClientUIDFromIP(ip, port);

		serverGUI.addCommand("Console > " + cUID + " : Byte Array");

		try {
			sendData = bytes;
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
	@Deprecated
	public void broadcastmessage(String message) {
		Object[] keys = this.getAttachedClients().keySet().toArray();
		for (Object key : keys) {
			InetAddress ip = (InetAddress) this.getAttachedClients().get(key).toArray()[0];
			int port = Integer.parseInt(this.getAttachedClients().get(key).toArray()[1] + "");
			sendInstantMessage(message, ip, port);
		}
	}

	/** Is the server thread running? */
	public boolean isRunning() {
		return running;
	}

	/** @return the lastMessage */
	public String getLastMessage() {
		return lastMessage;
	}

	/** Returns a clients ping request */
	@SuppressWarnings( "unused" )
	private void returnPingRequest(InetAddress ip, int port) {
		sendInstantMessage("JLB1F0_RETURN_PING", ip, port);
	}

	@Override
	public void stopServer() throws InterruptedException, IOException {
		this.broadcastmessage("Server Closed");
		this.port = 0;
		this.running = false;
		Thread close = new Thread() {

			public void run() {
				try {
					stopServer = true;
					serverTick.join(1);
					serverSocket.close();
					serverSocket = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					this.join();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};
		close.start();
	}

	/** @return the maxBytes */
	public int getMaxBytes() {
		return maxBytes;
	}

	/** @param maxBytes
	 *            the maxBytes to set */
	public void setMaxBytes(int maxBytes) {
		this.maxBytes = maxBytes;
	}

	/** Sends an object over the network */
	@Override
	public void sendObject(Object obj, InetAddress ip, int port) throws IOException {
		sendData = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		oos.flush();
		// get the byte array of the object
		byte[] Buf = baos.toByteArray();

		int number = Buf.length;
		sendData = new byte[this.maxBytes];

		// int -> byte[]
		for (int i = 0; i < sendData.length; ++i) {
			int shift = i << 3; // i * 8
			sendData[sendData.length - 1 - i] = (byte) ((number & (0xff << shift)) >>> shift);
		}

		try {
			serverSocket.send(new DatagramPacket(sendData, sendData.length, ip, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
