package net.mightyelemental.network.listener;

import java.net.InetAddress;
import java.util.Map;

public interface MessageListenerServer {
	
	// /** @param message
	// * the message from the client
	// * @param ip
	// * the IP address of the client
	// * @param port
	// * the client's port */
	// public void onMessageRecievedFromClient(String message, InetAddress ip, int port);
	//
	// /** @param bytes
	// * the bytes array from the client
	// * @param ip
	// * the IP address of the client
	// * @param port
	// * the client's port */
	// public void onBytesRecievedFromClient(byte[] bytes, InetAddress ip, int port);
	
	/** @param obj
	 *            the object the client received from the server */
	public void onObjectRecievedFromClient(InetAddress ip, int port, Map<String, Object> obj);
	
	/** @param ip
	 *            the client's IP address
	 * @param port
	 *            the client's port */
	public void onNewClientAdded(InetAddress ip, int port, String uid);
	
	/** @param ip
	 *            the client's IP address
	 * @param port
	 *            the client's port */
	public void onClientDisconnect(InetAddress ip, int port, String uid);
	
}
