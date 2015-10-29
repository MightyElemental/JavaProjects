package net.mightyelemental.network.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import net.mightyelemental.network.BasicCommands;
import net.mightyelemental.network.listener.ClientInitiater;
import net.mightyelemental.network.listener.MessageListenerClient;

public class Client {

	private String	clientUID	= "UNASIGNED";
	private String	address;
	private int		port;

	private long	timeOfPingRequest	= 0l;
	private long	timeOfPingResponse	= 0l;
	private long	pingTime			= 0l;

	public boolean	running;
	public long		timeStarted	= System.currentTimeMillis();
	public long		timeRunning	= 0l;

	private String				lastRecievedMessage	= "";
	private ArrayList<String>	recievedMessages	= new ArrayList<String>();

	private DatagramSocket clientSocket;

	private InetAddress IPAddress;

	private ClientInitiater initiater = new ClientInitiater();

	private byte[]	receiveData;
	private byte[]	sendData;

	private Thread receiveThread = new Thread("ClientReceiveThread") {

		public void run() {
			running = true;

			while (running) {
				timeRunning = System.currentTimeMillis() - timeStarted;
				try {
					receiveData = new byte[1024];
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					clientSocket.receive(receivePacket);
					String receiveData = new String(receivePacket.getData()).trim();
					receiveData = BasicCommands.decryptMessageBase64(receiveData);

					// System.out.println(receiveData.toString());
					if (receiveData.toString().contains("JLB1F0_CLIENT_UID")) {
						clientUID = receiveData.toString().replace("JLB1F0_CLIENT_UID ", "");
					} else if (receiveData.toString().contains("JLB1F0_RETURN_PING")) {
						timeOfPingResponse = System.currentTimeMillis();
						pingTime = timeOfPingResponse - timeOfPingRequest;
					} else {
						lastRecievedMessage = receiveData.toString();
						recievedMessages.add(lastRecievedMessage);
						initiater.onMessageRecieved(lastRecievedMessage);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				this.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	/** @param name
	 *            the name of the client
	 * @param address
	 *            the IP address in String form
	 * @param port
	 *            the port for the client to send messages through */
	public Client( String address, int port ) {
		this.address = address;
		this.port = port;
	}

	/** @return the clients name */
	public String getUID() {
		return this.clientUID;
	}

	/** @return the IP address in the form of String */
	public String getAddress() {
		return this.address;
	}

	/** @return the full IP address */
	public String getFullIPAddress() {
		return getAddress() + ":" + getPort();
	}

	/** @return the port the client is running on */
	public int getPort() {
		return port;
	}

	/** Used to connect the client to server as well as setting up the data pipes. */
	public synchronized void setup() {
		try {
			clientSocket = new DatagramSocket();
			IPAddress = InetAddress.getByName(getAddress());

			sendData = new byte[1024];
			receiveData = new byte[1024];
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
		receiveThread.start();
		// sendMessage("JLB1F0_TEST_CONNECTION RETURN_UID");
	}

	/** Sends a message to the connected server
	 * 
	 * @param message
	 *            the message to send to the server */
	public void sendMessage(String message) {
		sendData = null;
		String messageOut = this.clientUID + " : " + message;
		messageOut = BasicCommands.encryptMessageBase64(messageOut);
		sendData = messageOut.getBytes();

		try {
			clientSocket.send(new DatagramPacket(sendData, sendData.length, this.IPAddress, this.port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** @return lastRecievedMessage - the message the the client last received */
	public String getLastRecievedMessage() {
		return lastRecievedMessage;
	}

	/** @return the recievedMessages */
	public ArrayList<String> getRecievedMessages() {
		return recievedMessages;
	}

	/** Adds listener to initiater
	 * 
	 * @param mlc
	 *            the MessageListenerClient instance */
	public void addListener(MessageListenerClient mlc) {
		initiater.addListener(mlc);
	}

	/** Used to stop the client thread */
	public synchronized void stopClient() {
		this.running = false;
		sendData = null;
		receiveData = null;
	}

	/** Pings the server */
	public void sendPingRequest() {
		timeOfPingRequest = System.currentTimeMillis();
		sendMessage("JLB1F0_PING_SERVER");
	}

	/** @return the time it took to ping the server */
	public long getPingTime() {
		return pingTime;
	}

}
