package net.mightyelemental.network.listener;

import java.util.Map;

public interface MessageListenerClient {
	
	// /** @param message
	// * the message received from server */
	// public void onMessageRecievedFromServer(String message);
	
	// /** @param bytes
	// * the byte array received from server */
	// public void onBytesRecievedFromServer(byte[] bytes);
	
	/** @param obj
	 *            the object the client received from the server */
	public void onObjectRecievedFromServer(Map<String, Object> obj);
	
	public void onClientDropped(String reason);
	
	public void onConnectionRefused();
	
}
