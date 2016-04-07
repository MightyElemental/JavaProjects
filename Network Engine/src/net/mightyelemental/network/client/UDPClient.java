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
import java.util.HashMap;
import java.util.Map;

public class UDPClient extends Client {
	
	
	public long timeStarted = System.currentTimeMillis();
	public long timeRunning = 0l;
	
	private DatagramSocket clientSocket;
	
	private InetAddress IPAddress;
	
	private byte[] receiveData;
	private byte[] sendData;
	
	private boolean usesEncryption = false;
	
	private Thread clientTick = new Thread("ClientReceiveThread") {
		
		
		@SuppressWarnings( "unchecked" )
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
					
					Object obj = ois.readObject();
					lastObject = obj;
					initiater.onObjectRecieved(obj);
					
					if (((Map<String, Object>) obj).containsKey("UID")) { // Sets
																			// client
																			// uid
						clientUID = (String) ((Map<String, Object>) obj).get("UID");
					}
					
					// String receiveData = new
					// String(receivePacket.getData()).trim();
					// if (usesEncryption) {
					// receiveData =
					// BasicCommands.decryptMessageBase64(receiveData);
					// }
					// System.out.println(receiveData.toString());
					// if (receiveData.toString().contains("JLB1F0_CLIENT_UID"))
					// {
					// clientUID =
					// receiveData.toString().replace("JLB1F0_CLIENT_UID ", "");
					// } else if
					// (receiveData.toString().contains("JLB1F0_RETURN_PING")) {
					// timeOfPingResponse = System.currentTimeMillis();
					// pingTime = timeOfPingResponse - timeOfPingRequest;
					// } else {
					// lastMessage = receiveData.toString();
					// recievedMessages.add(lastMessage);
					// initiater.onMessageRecieved(lastMessage);
					// }
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
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
	
	/** Used to connect the client to server as well as setting up the data pipes. */
	public synchronized void setup() throws SocketException, UnknownHostException {
		
		clientSocket = new DatagramSocket();
		IPAddress = InetAddress.getByName(getAddress());
		
		sendData = new byte[maxBytes];
		receiveData = new byte[maxBytes];
		
		clientTick.start();
		hasBeenSetup = true;
	}
	
	/** Used to stop the client thread */
	public synchronized void stopClient() {
		this.running = false;
		sendData = null;
		receiveData = null;
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
	public void sendObject(String varName, Object obj) throws IOException {
		if (!hasBeenSetup) {
			System.err.println("FATAL ERROR: Client has not been setup yet!");
			return;
		}
		sendData = null;
		objectToSend.clear();
		objectToSend.put(varName, obj);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(objectToSend);
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
		clientSocket.send(new DatagramPacket(sendData, sendData.length, this.IPAddress, this.port));
		objectToSend.clear();
		objectToSend = null;
		objectToSend = new HashMap<String, Object>();
	}
	
	@Override
	public void sendObjectMap(Map<String, Object> objects) throws IOException {
		if (!hasBeenSetup) {
			System.err.println("FATAL ERROR: Client has not been setup yet!");
			return;
		}
		sendData = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(objects);
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
		
		clientSocket.send(new DatagramPacket(sendData, sendData.length, this.IPAddress, this.port));
		objectToSend.clear();
		objectToSend = null;
		objectToSend = new HashMap<String, Object>();
	}
	
}
