package net.mightyelemental.network.listener;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerInitiater {

	private List<MessageListenerServer> listeners = new ArrayList<MessageListenerServer>();

	public void addListener(MessageListenerServer toAdd) {
		listeners.add(toAdd);
	}

	public void onMessageRecieved(String message, InetAddress ip, int port) {
		// Notify everybody that may be interested.
		for (MessageListenerServer mls : listeners)
			mls.onMessageRecievedFromClient(message, ip, port);
	}

	public void onNewClientAdded(InetAddress ip, int port, String uid) {
		// Notify everybody that may be interested.
		for (MessageListenerServer mls : listeners)
			mls.onNewClientAdded(ip, port, uid);
	}

}
