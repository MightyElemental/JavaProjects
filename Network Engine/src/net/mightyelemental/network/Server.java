package net.mightyelemental.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.Random;

import net.mightyelemental.network.gui.ServerGUI;
import net.mightyelemental.network.listener.MessageListenerServer;
import net.mightyelemental.network.listener.ServerInitiater;

public abstract class Server {

	protected int		port;
	protected boolean	running;

	protected boolean	hasGUI;
	protected ServerGUI	serverGUI;

	protected static Random random = new Random();

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
