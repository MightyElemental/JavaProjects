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
	
	public ServerGUI getGUI() {
		return serverGUI;
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
	
	public abstract void setupServer() throws BindException, IOException, SocketException;
	
	public abstract void stopServer() throws InterruptedException, IOException;
	
	public abstract void sendObject(String varName, Object obj, InetAddress ip, int port) throws IOException;
	
	public abstract void sendObjectMap(Map<String, Object> objects, InetAddress ip, int port) throws IOException;
	
}
