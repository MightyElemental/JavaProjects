package net.mightyelemental.network.listener;

import java.util.ArrayList;
import java.util.List;

public class ClientInitiater {

	private List<MessageListenerClient> listeners = new ArrayList<MessageListenerClient>();

	public void addListener(MessageListenerClient toAdd) {
		listeners.add(toAdd);
	}

	public void onMessageRecieved(String message) {
		// Notify everybody that may be interested.
		for (MessageListenerClient mlc : listeners) {
			mlc.onMessageRecievedFromServer(message);
		}
	}

}