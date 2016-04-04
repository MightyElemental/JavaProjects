package net.mightyelemental.network.listener;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerInitiater {
	
	
	private List<MessageListenerServer> listeners = new ArrayList<MessageListenerServer>();
	
	public void addListener(MessageListenerServer toAdd) {
		listeners.add(toAdd);
	}
	
	// public void onMessageRecieved(String message, InetAddress ip, int port) {
	// // Notify everybody that may be interested.
	// for (MessageListenerServer mls : listeners)
	// mls.onMessageRecievedFromClient(message, ip, port);
	// }
	//
	// public void onBytesRecieved(byte[] bytes, InetAddress ip, int port) {
	// // Notify everybody that may be interested.
	// for (MessageListenerServer mls : listeners)
	// mls.onBytesRecievedFromClient(bytes, ip, port);
	// }
	
	public void onNewClientAdded(InetAddress ip, int port, String uid) {
		// Notify everybody that may be interested.
		for (MessageListenerServer mls : listeners)
			mls.onNewClientAdded(ip, port, uid);
	}
	
	public void onObjectRecieved(InetAddress ip, int port, Object obj) {
		// Notify everybody that may be interested.
		for (MessageListenerServer mls : listeners) {
			mls.onObjectRecievedFromClient(ip, port, obj);
		}
	}
	
	public void onClientDisconnect(InetAddress ip, int port, String uid) {
		// Notify everybody that may be interested.
		for (MessageListenerServer mls : listeners) {
			mls.onClientDisconnect(ip, port, uid);
		}
	}
	
}
