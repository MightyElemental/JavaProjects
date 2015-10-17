package net.mightyelemental.network.listener;

public interface MessageListenerClient {

	/** @param message
	 *            the message received from server */
	public void onMessageRecievedFromServer(String message);

}
