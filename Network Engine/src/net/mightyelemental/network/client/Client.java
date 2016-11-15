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
	
	
	protected ObjectInputStream ois;
	protected ObjectOutputStream ous;
	
	protected boolean running;
	protected boolean debugMode = false;
	protected boolean hasBeenSetup = false;
	
	private boolean usesEncryption = false;
	
	protected String verifyCode = "NONE";
	
	public Map<String, Object> objectToSend = new HashMap<String, Object>();
	
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
	 * @param veryifyCode
	 *            the code used to verify the client game */
	public Client( String address, int port, boolean usesEncryption, int maxBytes, String verifyCode ) {
		this.address = address;
		this.port = port;
		this.usesEncryption = usesEncryption;
		this.maxBytes = maxBytes;
		this.verifyCode = verifyCode;
	}
	
	public void setDebugMode(boolean state) {
		this.debugMode = state;
	}
	
	/** @return the usesEncryption */
	public boolean doesUseEncryption() {
		return usesEncryption;
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
	
	public abstract void setup() throws IOException;
	
	public abstract void sendObject(String varName, Object obj) throws IOException;
	
	/** Send a map of objects to server
	 * 
	 * @param objects
	 *            the object map to send @throws IOException */
	public abstract void sendObjectMap(Map<String, Object> objects) throws IOException;
	
	/** Used to get the initiater */
	public ClientInitiater getInitiater() {
		return initiater;
	}
	
}
