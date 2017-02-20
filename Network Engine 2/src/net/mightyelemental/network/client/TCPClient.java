package net.mightyelemental.network.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import net.mightyelemental.network.listener.ClientInitiater;
import net.mightyelemental.network.listener.MessageListenerClient;

public class TCPClient {
	
	
	public DataOutputStream out;
	public DataInputStream in;
	
	protected boolean running;
	protected boolean hasBeenSetup = false;
	
	protected String verifyCode = "NONE";
	
	protected String clientUID = "UNASIGNED";
	
	protected String address;
	protected int port;
	
	protected int maxBytes = 1024;
	
	protected ClientInitiater initiater = new ClientInitiater();
	
	protected Object lastObject = "";
	protected ArrayList<String> recievedMessages = new ArrayList<String>();
	
	/** @param address
	 *            the IP address
	 * @param port
	 *            the port for the client to send messages through
	 * @param maxBytes
	 *            the maximum amount of bytes the client should handle
	 * @param usesEncryption
	 *            whether or not the client should use encryption
	 * @param verifyCode
	 *            the code used to verify the client game */
	public TCPClient( String address, int port, String verifyCode ) {
		this.address = address;
		this.port = port;
		this.verifyCode = verifyCode;
	}
	
	public boolean hasBeenSetup() {
		return this.hasBeenSetup;
	}
	
	public boolean isRunning() {
		return this.running;
	}
	
	/** @return the port the client is running on */
	public int getPort() {
		return port;
	}
	
	/** @return the IP address in the form of String */
	public String getAddress() {
		return this.address;
	}
	
	/** @return the full IP address */
	public String getFullIPAddress() {
		return getAddress() + ":" + getPort();
	}
	
	/** @return the clients name */
	public String getUID() {
		return this.clientUID;
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
	
	/** Adds listener to initiater
	 * 
	 * @param mlc
	 *            the MessageListenerClient instance */
	public void addListener(MessageListenerClient mlc) {
		initiater.addListener(mlc);
	}
	
	/** @return lastRecievedMessage - the message the the client last received */
	public Object getLastRecievedObject() {
		return lastObject;
	}
	
	/** @return the recievedMessages */
	public ArrayList<String> getRecievedMessages() {
		return recievedMessages;
	}
	
	/** @return the verification code that the client is using */
	public String getVerifyCode() {
		return verifyCode;
	}
	
	/** Used to get the initiator */
	public ClientInitiater getInitiater() {
		return initiater;
	}
	
	private Socket clientSocket;
	
	private Thread clientTick = new Thread("ClientReceiveThread") {
		
		
		@SuppressWarnings( "unchecked" )
		public void run() {
			running = true;
			while (running) {
				
				try {
					if (in == null) {
						continue;
					}
					String obj = in.readUTF();
					lastObject = obj;
					JSONObject j = (JSONObject) JSONValue.parse(obj);
					if (j.containsKey("VerifyCodeRequest")) {
						sendObject("VerifyCode", getVerifyCode());
					} else {
						initiater.onObjectRecieved(j);
					}
					
					if (j.containsKey("UID")) { // Sets client UID
						clientUID = (String) j.get("UID");
					}
				} catch (SocketException e) {
					initiater.onClientDropped("default");
					break;
				} catch (IOException e) {
					initiater.onClientDropped("default");
					e.printStackTrace();
					break;
				}
				
				// String tempMessage = null;
				// try {
				// tempMessage = in.readLine();
				// } catch (IOException e) {
				// System.err.println("Server has been closed");
				// stopClient();
				// }
				// System.out.println("[TCPClient] message: " + tempMessage);
				// if (usesEncryption) {
				// tempMessage = BasicCommands.decryptMessageBase64(tempMessage);
				// }
				//
				// if (tempMessage.contains("JLB1F0_CLIENT_UID")) {
				// clientUID = tempMessage.replace("JLB1F0_CLIENT_UID ", "");
				// } else if (tempMessage.contains("JLB1F0_RETURN_PING")) {
				// timeOfPingResponse = System.currentTimeMillis();
				// pingTime = timeOfPingResponse - timeOfPingRequest;
				// } else {
				// lastMessage = tempMessage;
				// recievedMessages.add(lastMessage);
				// }
			}
			try {
				stopClient();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	};
	
	public synchronized void setup() throws IOException {
		boolean flag = false;
		try {
			clientSocket = new Socket(address, port);
		} catch (ConnectException e) {
			flag = true;
		}
		if (!flag) {
			clientSocket.setReceiveBufferSize(maxBytes);
			clientSocket.setSendBufferSize(maxBytes);
			clientSocket.setKeepAlive(true);
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			clientTick.start();
			hasBeenSetup = true;
		} else {
			this.initiater.onConnectionRefused();
		}
	}
	
	/** Used to stop the client thread
	 * 
	 * @throws IOException */
	public synchronized void stopClient() throws InterruptedException, IOException {
		running = false;
		clientTick.interrupt();
		if (clientSocket != null) {
			clientSocket.close();
		}
		// clientTick.join(2000);
	}
	
	@SuppressWarnings( "unchecked" )
	public void sendObject(String varName, Object obj) throws IOException {
		if (!hasBeenSetup) {
			System.err.println("FATAL ERROR: Client has not been setup yet!");
			return;
		}
		if (!running) { return; }
		JSONObject j = new JSONObject();
		j.put(varName, obj);
		out.writeUTF(j.toString());
	}
	
	@SuppressWarnings( "unchecked" )
	public void sendObjectMap(Map<String, Object> objects) throws IOException {
		if (!hasBeenSetup) {
			System.err.println("FATAL ERROR: Client has not been setup yet!");
			return;
		}
		if (!running) { return; }
		JSONObject j = new JSONObject();
		j.putAll(objects);
		out.writeUTF(j.toString());
	}
	
}
