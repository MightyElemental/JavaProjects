package net.mightyelemental.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import net.mightyelemental.network.gui.ServerGUI;
import net.mightyelemental.network.listener.ServerInitiater;

public class TCPConnection {
	
	
	private Socket client;
	private InetAddress ip;
	private int port;
	private String UID;
	
	public long timeOfVerifyRequest;
	
	public DataOutputStream objectOut;
	public DataInputStream objectIn;
	
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
						if (objectIn == null) {
							running = false;
							break;
						}
						String obj = objectIn.readUTF();
						System.out.println("message = " + obj);
						JSONObject j = (JSONObject) JSONValue.parse(obj);
						
						if (j.size() < 1) {
							stopThread();
							break;
						}
						if (j.containsKey("VerifyCode")) {
							String s = (String) j.get("VerifyCode");
							if (s.equals(verifyCode)) {
								verified = true;
								server.onVerified(UID);
								
							} else {
								server.onVerifyDenied(UID);
								stopThread();
							}
						} else {
							SI.onObjectRecieved(ip, port, j);
						}
					} catch (SocketException e) {
						SI.onClientDisconnect(ip, port, getUID());
						stopThread();
						break;
					} catch (EOFException e) {
						SI.onClientDisconnect(ip, port, getUID());
						stopThread();
						break;
					} catch (StreamCorruptedException e) {
						SI.onClientDisconnect(ip, port, getUID());
						stopThread();
						System.err.println("Wolfgang's fault (StreamCorruptedException)");
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
			objectOut = new DataOutputStream(client.getOutputStream());
			objectIn = new DataInputStream(client.getInputStream());
		} catch (StreamCorruptedException sce) {
			try {
				stopThread();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(sce.getMessage());
		} catch (SocketException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!client.isClosed()) {
			try {
				this.client.setSoTimeout(0);
				this.client.setKeepAlive(true);
				this.client.setReceiveBufferSize(maxBytes);
				this.client.setSendBufferSize(maxBytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		System.err.println(UID + " has been kicked");
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
	
	/** Send an object to server
	 * 
	 * @param varName
	 *            the variable name of the object so that it is easier to detect certain variables on serverside
	 * @param obj
	 *            the object to send
	 * @throws IOException */
	@SuppressWarnings( "unchecked" )
	public void sendObject(String varName, Object obj) throws IOException {
		JSONObject j = new JSONObject();
		j.put(varName, obj);
		System.out.println(j);
		if (objectOut != null && !client.isClosed()) {
			try {
				objectOut.writeUTF(j.toString());
			} catch (NullPointerException e) {
				try {
					stopThread();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			System.err.println("Socket " + client.getRemoteSocketAddress() + " has been closed");
		}
	}
	
	/** Send a map of objects to server
	 * 
	 * @param objects
	 *            the object map to send
	 * @throws IOException */
	@SuppressWarnings( "unchecked" )
	public void sendMap(Map<String, Object> objects) throws IOException {
		JSONObject j = new JSONObject();
		j.putAll(objects);
		System.out.println(j);
		if (objectOut != null && !client.isClosed()) {
			try {
				objectOut.writeUTF(j.toString());
			} catch (NullPointerException e) {
				try {
					stopThread();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		} else {
			System.err.println("Socket " + client.getRemoteSocketAddress() + " has been closed");
		}
	}
	
	public boolean isVerified() {
		return verified;
	}
	
}
