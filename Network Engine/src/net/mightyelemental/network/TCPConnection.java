package net.mightyelemental.network;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import net.mightyelemental.network.gui.ServerGUI;
import net.mightyelemental.network.listener.ServerInitiater;

public class TCPConnection {
	
	
	private Socket client;
	private InetAddress ip;
	private int port;
	private String UID;
	
	@Deprecated
	public BufferedReader in;
	@Deprecated
	public DataInputStream is;
	@Deprecated
	public DataOutputStream byteOut;
	@Deprecated
	public PrintWriter pout;
	
	public ObjectOutputStream objectOut;
	public ObjectInputStream objectIn;
	
	private boolean usesEncryption = false;
	
	@SuppressWarnings( "unused" )
	private ServerGUI serverGUI;
	
	private ServerInitiater SI;
	
	private boolean running = false;
	public int maxBytes = 2 ^ 10;
	
	private Thread run = new Thread("TCPConnection_Undef") {
		
		
		public void run() {
			try {
				while (running) {
					// is.readFully(recievedBytes);
					try {
						Object obj = objectIn.readObject();
						SI.onObjectRecieved(ip, port, obj);
					} catch (ClassNotFoundException e) {
						SI.onClientDisconnect(ip, port, getUID());
					}
					// if (usesEncryption) {
					// System.out.println("Before decryp: " + message);// SENDS BYTE ARRAYS! DO NOT DECRYPT THEM!
					// message = BasicCommands.decryptMessageBase64(message);
					// System.out.println("After decryp: " + message);
					// } else {
					// System.out.println("Message: " + message);
					// }
					// if (message.contains("JLB1F0_TEST_CONNECTION RETURN_UID")) {
					// sendMessage("JLB1F0_CLIENT_UID " + UID);
					// } else if (message.contains("JLB1F0_PING_SERVER")) {
					// returnPingRequest();
					// } else if (serverGUI != null) {
					// serverGUI.addCommand(UID + " : " + message);
					// }
				}
			} catch (IOException e) {
				e.printStackTrace();
				stopThread();
			}
			
		}
	};
	
	/** Returns a clients ping request */
	@Deprecated
	public void returnPingRequest() {
		try {
			sendMessage("JLB1F0_RETURN_PING");
			sendMessage("JLB1F0_CLIENT_UID " + UID);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public TCPConnection( Socket client, ServerInitiater initiater, ServerGUI serverGUI, boolean usesEncryption, int maxBytes ) {
		this.client = client;
		this.ip = client.getInetAddress();
		this.port = client.getPort();
		this.SI = initiater;
		this.serverGUI = serverGUI;
		this.usesEncryption = usesEncryption;
		this.maxBytes = maxBytes;
		try {
			OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream());
			byteOut = new DataOutputStream(client.getOutputStream());
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			is = new DataInputStream(client.getInputStream());
			objectOut = new ObjectOutputStream(client.getOutputStream());
			objectIn = new ObjectInputStream(client.getInputStream());
			pout = new PrintWriter(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.client.setSoTimeout(0);
			this.client.setKeepAlive(true);
			this.client.setReceiveBufferSize(maxBytes);
			this.client.setSendBufferSize(maxBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Send a message to the client */
	@Deprecated
	public synchronized void sendMessage(String message) throws IOException {
		if (message == null) { return; }
		if (usesEncryption) {
			message = BasicCommands.encryptMessageBase64(message);
		}
		pout.println(message);// Socket closed issue
	}
	
	/** Send a byte array to the client
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if byte array contains negative values */
	@Deprecated
	public synchronized void sendBytes(byte[] bytes) throws IOException {
		if (bytes == null) { return; }
		for (byte b : bytes) {
			if (b < 0) { throw new IndexOutOfBoundsException(""); }
		}
		byteOut.write(bytes);// Socket closed issue
	}
	
	/** Start the clients thread */
	public synchronized void startThread() {
		running = true;
		run.start();
	}
	
	public synchronized void stopThread() {
		running = false;
		try {
			run.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/** @return the client */
	public Socket getClient() {
		return client;
	}
	
	/** @return the ip */
	public InetAddress getIp() {
		return ip;
	}
	
	/** @return the port */
	public int getPort() {
		return port;
	}
	
	/** @return the uID */
	public String getUID() {
		return UID;
	}
	
	/** @param uID
	 *            the uID to set */
	public void setUID(String uID) {
		UID = uID;
		run.setName("TCPConnection_" + UID);
	}
	
	private Map<String, Object> objectToSend = new HashMap<String, Object>();
	
	/** Send an object to server
	 * 
	 * @param varName
	 *            the variable name of the object so that it is easier to detect certain variables on serverside
	 * @param obj
	 *            the object to send
	 * @throws IOException */
	public void sendObject(String varName, Object obj) throws IOException {
		objectToSend = null;
		objectToSend = new HashMap<String, Object>();
		objectToSend.put(varName, obj);
		objectOut.writeObject(objectToSend);
	}
	
	/** Send a map of objects to server
	 * 
	 * @param objects
	 *            the object map to send
	 * @throws IOException */
	public void sendMap(Map<String, Object> objects) throws IOException {
		objectOut.writeObject(objects);
	}
	
}
