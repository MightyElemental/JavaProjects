package net.mightyelemental.network.listener;

public interface MessageListenerClient {

	/** @param message
	 *            the message received from server */
	public void onMessageRecievedFromServer(String message);

	/** @param bytes
	 *            the byte array received from server */
	public void onBytesRecievedFromServer(byte[] bytes);

}
