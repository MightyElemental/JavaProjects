package net.mightyelemental.network.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import net.mightyelemental.network.listener.ClientInitiater;
import net.mightyelemental.network.listener.MessageListenerClient;
import net.mightyelemental.network.listener.MessageListenerServer;

public class Client {

	private String	userName;
	private String	address;
	private int		port;

	public boolean isRunning;

	private String				lastRecievedMessage	= "";
	private ArrayList<String>	recievedMessages	= new ArrayList<String>();

	private DatagramSocket clientSocket;

	private InetAddress IPAddress;

	private ClientInitiater initiater = new ClientInitiater();

	private byte[]	receiveData;
	private byte[]	sendData;

	private Thread receiveThread = new Thread("ClientReceiveThread") {

		public void run() {
			isRunning = true;

			while (isRunning) {
				try {
					receiveData = new byte[1024];
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					clientSocket.receive(receivePacket);
					String receiveData = new String(receivePacket.getData()).trim();

					System.out.println(receiveData.toString());
					lastRecievedMessage = receiveData.toString();
					recievedMessages.add(lastRecievedMessage);
					initiater.onMessageRecieved(lastRecievedMessage);
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
	public Client( String name, String address, int port ) {
		this.userName = name;
		this.address = address;
		this.port = port;
	}

	/** @return the clients name */
	public String getName() {
		return this.userName;
	}

	/** @return the IP address in the form of String */
	public String getAddress() {
		return this.address;
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
	}

	/** Sends a message to the connected server
	 * 
	 * @param message
	 *            the message to send to the server */
	public void sendMessage(String message) {
		sendData = null;
		String messageOut = this.userName + " : " + message;
		sendData = messageOut.getBytes();

		try {
			clientSocket.send(new DatagramPacket(sendData, sendData.length, this.IPAddress, this.port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** @return lastRecievedMessage - the message the the client last recieved */
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

}
