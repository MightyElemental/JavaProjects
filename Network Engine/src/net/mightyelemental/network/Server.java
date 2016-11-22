package net.mightyelemental.network;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.mightyelemental.network.gui.ServerGUI;
import net.mightyelemental.network.listener.MessageListenerServer;
import net.mightyelemental.network.listener.ServerInitiater;

public abstract class Server {
	
	
	protected int port;
	public boolean running;
	
	protected boolean hasGUI;
	protected boolean hasBeenSetup = false;
	
	protected boolean usesEncryption = false;
	protected int maxBytes = 2 ^ 10;
	
	protected String verifyCode = "NONE";
	
	protected ServerGUI serverGUI;
	
	protected static Random random = new Random();
	
	public Map<String, Object> objectToSend = new HashMap<String, Object>();
	
	protected ServerInitiater initiater = new ServerInitiater();
	
	/** @param port
	 *            - the port of which the server should run
	 * @param maxBytes
	 *            - the maximum amount of bytes the server should be able to send
	 * @param usesEncryption
	 *            - whether or not the server should use encryption
	 * @param verifyCode
	 *            - used to ensure that connecting clients are from the correct game */
	public Server( int port, boolean usesEncryption, int maxBytes, String verifyCode ) {
		this(port, verifyCode);
		this.usesEncryption = usesEncryption;
		this.maxBytes = maxBytes;
	}
	
	/** @param port
	 *            - the port of which the server should run
	 * @param verifyCode
	 *            - used to ensure that connecting clients are from the correct game */
	public Server( int port, String verifyCode ) {
		this.port = port;
		this.verifyCode = verifyCode;
	}
	
	/** Adds listener to initiater
	 * 
	 * @param mls
	 *            the MessageListenerServer instance */
	public void addListener(MessageListenerServer mls) {
		initiater.addListener(mls);
	}
	
	public ServerGUI getGUI() {
		return serverGUI;
	}
	
	/** Setup the built in GUI */
	public void initGUI(String title) {
		serverGUI = new ServerGUI(title, this, BasicCommands.getExternalIPAddress() + ":" + this.getPort());
		this.hasGUI = true;
	}
	
	/** Setup the built in GUI */
	public void initGUI(ServerGUI frame) {
		serverGUI = frame;
		this.hasGUI = true;
	}
	
	/** @return the port the server is running on */
	public int getPort() {
		return this.port;
	}
	
	/** @return the verification code that the server is using */
	public String getVerifyCode() {
		return verifyCode;
	}
	
	public abstract void setupServer() throws BindException, IOException, SocketException;
	
	public abstract void stopServer() throws InterruptedException, IOException;
	
	public abstract void sendObject(String varName, Object obj, InetAddress ip, int port) throws IOException;
	
	public abstract void sendObjectMap(Map<String, Object> objects, InetAddress ip, int port) throws IOException;
	
}
