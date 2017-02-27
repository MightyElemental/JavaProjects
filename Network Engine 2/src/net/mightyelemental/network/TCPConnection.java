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
							stopThread("has been kicked (did not verify)");
						}
						if (objectIn == null) {
							running = false;
							break;
						}
						String obj = objectIn.readUTF();
						if (server.debug) System.out.println("message=" + obj.trim());
						JSONObject j = (JSONObject) JSONValue.parse(obj);
						
						if (j.size() < 1) {
							stopThread("has been kicked (map is negative!)");
							break;
						}
						if (j.containsKey("VerifyCode")) {
							String s = (String) j.get("VerifyCode");
							if (s.equals(verifyCode)) {
								verified = true;
								server.onVerified(UID);
								
							} else {
								server.onVerifyDenied(UID);
								stopThread("has been kicked (wrong verify)");
							}
						} else {
							SI.onObjectRecieved(ip, port, j);
						}
					} catch (SocketException e) {
						SI.onClientDisconnect(ip, port, getUID());
						break;
					} catch (EOFException e) {
						SI.onClientDisconnect(ip, port, getUID());
						stopThread("has been kicked (eof)");
						break;
					} catch (StreamCorruptedException e) {
						SI.onClientDisconnect(ip, port, getUID());
						stopThread("has been kicked (stream corrupt)");
						if (server.debug) System.err.println("Wolfgang's fault (StreamCorruptedException)");
					}
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			try {
				stopThread("has been kicked (thread stopped)");
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
				stopThread("has been kicked due to error");
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			if (server.debug) System.out.println(sce.getMessage());
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
	
	public synchronized void stopThread(String reason) throws IOException, InterruptedException {
		if (running == false) return;
		running = false;
		run.interrupt();
		server.getTcpConnections().remove(UID);
		if (server.debug) System.err.println(UID + " " + reason);
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
	public void sendObject(String varName, Object obj) {
		if (verifyKick()) return;
		JSONObject j = new JSONObject();
		j.put(varName, obj);
		if (server.debug) System.out.println("send=" + j);
		if (objectOut != null && !client.isClosed()) {
			try {
				objectOut.writeUTF(j.toString());
			} catch (NullPointerException | IOException e) {
				try {
					stopThread("has been kicked (send error)");
				} catch (InterruptedException | IOException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			if (server.debug) System.err.println("Socket " + client.getRemoteSocketAddress() + " has been closed");
		}
	}
	
	private boolean checkedVerify;
	
	public boolean verifyKick() {
		boolean flag = false;
		if (checkedVerify) return false;
		if (!verified && timeOfVerifyRequest + 5000 < System.currentTimeMillis()) {
			checkedVerify = true;
			sendObject("ServerMessage", "Your Client Did Not Verify In Time!");
			flag = true;
			try {
				stopThread("has been kicked (did not verify)");
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	/** Send a map of objects to server
	 * 
	 * @param objects
	 *            the object map to send
	 * @throws IOException */
	@SuppressWarnings( "unchecked" )
	public void sendMap(Map<String, Object> objects) {
		if (verifyKick()) return;
		JSONObject j = new JSONObject();
		j.putAll(objects);
		if (server.debug) System.out.println("send=" + j);
		if (objectOut != null && !client.isClosed()) {
			try {
				objectOut.writeUTF(j.toString());
			} catch (NullPointerException | IOException e) {
				e.printStackTrace();
				try {
					stopThread("has been kicked (send error)");
				} catch (InterruptedException | IOException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			if (server.debug) System.err.println("Socket " + client.getRemoteSocketAddress() + " has been closed");
		}
	}
	
	public boolean isVerified() {
		return verified;
	}
	
}
