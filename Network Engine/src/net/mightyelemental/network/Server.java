package net.mightyelemental.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
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
	
	protected ServerGUI serverGUI;
	
	protected static Random random = new Random();
	
	public Map<String, Object> objectToSend = new HashMap<String, Object>();
	
	protected ServerInitiater initiater = new ServerInitiater();
	
	/** Adds listener to initiater
	 * 
	 * @param mls
	 *            the MessageListenerServer instance */
	public void addListener(MessageListenerServer mls) {
		initiater.addListener(mls);
	}
	
	/** Setup the built in GUI */
	public void initGUI(String title) {
		serverGUI = new ServerGUI(title, this, BasicCommands.getExternalIPAddress() + ":" + this.getPort());
		this.hasGUI = true;
	}
	
	/** @return the port the server is running on */
	public int getPort() {
		return this.port;
	}
	
	/** Gets the external IP address of where the server is running */
	public static String getExternalIPAddress() {
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine(); // you get the IP as a String
			return ip;
		} catch (Exception e) {
		}
		return "0.0.0.0";
	}
	
	/** Broadcast a message to every client
	 * 
	 * @param message
	 *            the message to be sent */
	public abstract void broadcastmessage(String message);
	
	@Deprecated
	public abstract void sendMessage(String message, InetAddress ip, int port);
	
	public abstract void setupServer();
	
	public abstract void stopServer() throws InterruptedException, IOException;
	
	/** Sends the specified client a byte array
	 * 
	 * @param bytes
	 *            the byte array to send
	 * @param ip
	 *            the IP address of the client
	 * @param port
	 *            the port of the client */
	@Deprecated
	public abstract void sendBytes(byte[] bytes, InetAddress ip, int port);
	
	public abstract void sendObject(String varName, Object obj, InetAddress ip, int port) throws IOException;
	
	public abstract void sendObjectMap(Map<String, Object> objects, InetAddress ip, int port) throws IOException;
	
}
