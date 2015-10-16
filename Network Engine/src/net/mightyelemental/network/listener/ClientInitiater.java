package net.mightyelemental.network.listener;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ClientInitiater {

	private ClientInitiater() {
	}

	private static List<MessageListenerServer> listeners = new ArrayList<MessageListenerServer>();

	public static void addListener(MessageListenerServer toAdd) {
		listeners.add(toAdd);
	}

	public static void onMessageRecieved(String message) {
		// Notify everybody that may be interested.
		for (MessageListenerServer hl : listeners)
			hl.onMessageRecieved(message);
	}

	public static void onCientRecieved(InetAddress ip) {
		// Notify everybody that may be interested.
		for (MessageListenerServer hl : listeners)
			hl.onClientIPRecieved(ip);
	}

}
