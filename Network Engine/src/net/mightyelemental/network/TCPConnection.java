package net.mightyelemental.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import net.mightyelemental.network.gui.ServerGUI;
import net.mightyelemental.network.listener.ServerInitiater;

public class TCPConnection {
	
	
	private Socket client;
	private InetAddress ip;
	private int port;
	private String UID;
	
	public long timeOfVerifyRequest;
	
	public ObjectOutputStream objectOut;
	public ObjectInputStream objectIn;
	
	private boolean verified;
	
	@SuppressWarnings( "unused" )
	private ServerGUI serverGUI;
	
	private String verifyCode = "NONE";
	
	private ServerInitiater SI;
	
	private TCPServer server;
	
	private boolean running = false;
	public int maxBytes = 2 ^ 10;
	
	private Thread run = new Thread("TCPConnection_Undef") {
		
		
		@SuppressWarnings( "unchecked" )
		public void run() {
			try {
				while (running) {
					// is.readFully(recievedBytes);
					try {
						if (!verified && timeOfVerifyRequest + 5000 < System.currentTimeMillis()) {
							sendObject("ServerMessage", "Your Client Did Not Verify In Time!");
							stopThread();
						}
						Object obj = objectIn.readObject();
						if (((Map<String, Object>) obj).containsKey("VerifyCode")) {
							String s = (String) ((Map<String, Object>) obj).get("VerifyCode");
							if (s.equals(verifyCode)) {
								verified = true;
								server.onVerified(UID);
								
							} else {
								server.onVerifyDenied(UID);
								stopThread();
							}
						} else {
							SI.onObjectRecieved(ip, port, obj);
						}
					} catch (ClassNotFoundException | SocketException e) {
						SI.onClientDisconnect(ip, port, getUID());
						stopThread();
						break;
					} catch (EOFException e) {
						SI.onClientDisconnect(ip, port, getUID());
						stopThread();
						break;
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
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			try {
				stopThread();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	};
	
	public TCPConnection( Socket client, TCPServer server ) {
		this.client = client;
		this.ip = client.getInetAddress();
		this.port = client.getPort();
		this.SI = server.initiater;
		this.serverGUI = server.serverGUI;
		this.maxBytes = server.maxBytes;
		this.verifyCode = server.verifyCode;
		this.server = server;
		try {
			objectOut = new ObjectOutputStream(client.getOutputStream());
			objectIn = new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
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
	
	/** Start the clients thread */
	public synchronized void startThread() {
		running = true;
		run.start();
	}
	
	public synchronized void stopThread() throws IOException, InterruptedException {
		running = false;
		run.interrupt();
		server.getTcpConnections().remove(UID);
		if (client != null) {
			client.close();
		}
		if (server.hasGUI) {
			server.serverGUI.updateClients();
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
	
	public boolean isVerified() {
		return verified;
	}
	
}
