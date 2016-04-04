package net.mightyelemental.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.mightyelemental.network.listener.ClientInitiater;
import net.mightyelemental.network.listener.MessageListenerClient;

public abstract class Client {
	
	
	protected long timeOfPingRequest = 0l;
	protected long timeOfPingResponse = 0l;
	protected long pingTime = 0l;
	
	protected ObjectInputStream ois;
	protected ObjectOutputStream ous;
	
	protected boolean running;
	protected boolean debugMode = false;
	protected boolean hasBeenSetup = false;
	
	public Map<String, Object> objectToSend = new HashMap<String, Object>();
	
	protected String clientUID = "UNASIGNED";
	
	protected String address;
	protected int port;
	
	protected int maxBytes = 1024;
	
	protected ClientInitiater initiater = new ClientInitiater();
	
	protected String lastMessage = "";
	protected ArrayList<String> recievedMessages = new ArrayList<String>();
	
	public void setDebugMode(boolean state) {
		this.debugMode = state;
	}
	
	public boolean getDebugMode() {
		return this.debugMode;
	}
	
	public boolean hasBeenSetup() {
		return this.hasBeenSetup;
	}
	
	public boolean isRunning() {
		return this.running;
	}
	
	/** Pings the server */
	@Deprecated
	public void sendPingRequest() {
		timeOfPingRequest = System.currentTimeMillis();
		// sendMessage("JLB1F0_PING_SERVER");
	}
	
	/** @return the time it took to ping the server */
	public long getPingTime() {
		return timeOfPingResponse - timeOfPingRequest;
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
	
	/** @return the clientUID */
	public String getClientUID() {
		return clientUID;
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
	public String getLastRecievedMessage() {
		return lastMessage;
	}
	
	/** @return the recievedMessages */
	public ArrayList<String> getRecievedMessages() {
		return recievedMessages;
	}
	
	public abstract void setup();
	
	@Deprecated
	public abstract void sendMessage(String message);
	
	/** Sends the specified client a byte array
	 * 
	 * @param bytes
	 *            the byte array to send */
	@Deprecated
	public abstract void sendBytes(byte[] bytes);
	
	public abstract void sendObject(String varName, Object obj) throws IOException;
	
	/** Send a map of objects to server
	 * 
	 * @param objects
	 *            the object map to send @throws IOException */
	public abstract void sendObjectMap(Map<String, Object> objects) throws IOException;
	
}
