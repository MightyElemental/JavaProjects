package net.mightyelemental.network.listener;

import java.util.ArrayList;
import java.util.List;

public class ClientInitiater {
	
	
	private List<MessageListenerClient> listeners = new ArrayList<MessageListenerClient>();
	
	public void addListener(MessageListenerClient toAdd) {
		listeners.add(toAdd);
	}
	
	// public void onMessageRecieved(String message) {
	// // Notify everybody that may be interested.
	// for (MessageListenerClient mlc : listeners) {
	// mlc.onMessageRecievedFromServer(message);
	// }
	// }
	//
	// public void onBytesRecieved(byte[] bytes) {
	// // Notify everybody that may be interested.
	// for (MessageListenerClient mlc : listeners) {
	// mlc.onBytesRecievedFromServer(bytes);
	// }
	// }
	
	public void onObjectRecieved(Object obj) {
		// Notify everybody that may be interested.
		for (MessageListenerClient mlc : listeners) {
			mlc.onObjectRecievedFromServer(obj);
		}
	}
	
	public void onServerClosed() {
		// Notify everybody that may be interested.
		for (MessageListenerClient mlc : listeners) {
			mlc.onServerClosed();
		}
	}
	
	public void onConnectionRefused() {
		// Notify everybody that may be interested.
		for (MessageListenerClient mlc : listeners) {
			mlc.onConnectionRefused();
		}
	}
	
}
