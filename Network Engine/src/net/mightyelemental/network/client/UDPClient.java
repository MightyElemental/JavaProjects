package net.mightyelemental.network.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import net.mightyelemental.network.BasicCommands;

public class UDPClient extends Client {

	public boolean	running;
	public long		timeStarted	= System.currentTimeMillis();
	public long		timeRunning	= 0l;

	private DatagramSocket clientSocket;

	private InetAddress IPAddress;

	private byte[]	receiveData;
	private byte[]	sendData;

	private boolean usesEncryption = false;

	private Thread clientTick = new Thread("ClientReceiveThread") {

		public void run() {
			running = true;

			while (running) {
				timeRunning = System.currentTimeMillis() - timeStarted;
				try {
					receiveData = new byte[maxBytes];
					sendData = new byte[maxBytes];
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					ois = new ObjectInputStream(new ByteArrayInputStream(receivePacket.getData()));
					clientSocket.receive(receivePacket);

					try {
						initiater.onObjectRecieved(ois.readObject());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}

					// String receiveData = new String(receivePacket.getData()).trim();
					// if (usesEncryption) {
					// receiveData = BasicCommands.decryptMessageBase64(receiveData);
					// }
					// System.out.println(receiveData.toString());
					// if (receiveData.toString().contains("JLB1F0_CLIENT_UID")) {
					// clientUID = receiveData.toString().replace("JLB1F0_CLIENT_UID ", "");
					// } else if (receiveData.toString().contains("JLB1F0_RETURN_PING")) {
					// timeOfPingResponse = System.currentTimeMillis();
					// pingTime = timeOfPingResponse - timeOfPingRequest;
					// } else {
					// lastMessage = receiveData.toString();
					// recievedMessages.add(lastMessage);
					// initiater.onMessageRecieved(lastMessage);
					// }
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

	/** @param address
	 *            the IP address in String form
	 * @param port
	 *            the port for the client to send messages through
	 * @param maxBytes
	 *            the maximum amount of bytes the client should handle */
	public UDPClient( String address, int port, int maxBytes ) {
		this.address = address;
		this.port = port;
		this.maxBytes = maxBytes;
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

			sendData = new byte[maxBytes];
			receiveData = new byte[maxBytes];
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
		clientTick.start();
		// sendMessage("JLB1F0_TEST_CONNECTION RETURN_UID");
	}

	/** Sends a message to the connected server
	 * 
	 * @param message
	 *            the message to send to the server */
	@Deprecated
	public void sendMessage(String message) {
		sendData = null;
		String messageOut = this.clientUID + " : " + message;
		if (usesEncryption) {
			messageOut = BasicCommands.encryptMessageBase64(messageOut);
		}
		sendData = messageOut.getBytes();

		try {
			clientSocket.send(new DatagramPacket(sendData, sendData.length, this.IPAddress, this.port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Sends the specified client a byte array
	 * 
	 * @param bytes
	 *            the byte array to send */
	@Deprecated
	public synchronized void sendBytes(byte[] bytes) {
		try {
			sendData = bytes;
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, this.IPAddress, this.port);
			this.clientSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Used to stop the client thread */
	public synchronized void stopClient() {
		this.running = false;
		sendData = null;
		receiveData = null;
	}

	/** Pings the server */
	@Deprecated
	public void sendPingRequest() {
		timeOfPingRequest = System.currentTimeMillis();
		// sendMessage("JLB1F0_PING_SERVER");
	}

	/** @return the time it took to ping the server */
	public long getPingTime() {
		return pingTime;
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

	/** Sends an object over the network */
	@Override
	public void sendObject(Object obj) throws IOException {
		sendData = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
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
			clientSocket.send(new DatagramPacket(sendData, sendData.length, this.IPAddress, this.port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
