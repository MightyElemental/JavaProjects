package net.mightyelemental.network.listener;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Initiater {

	private Initiater() {
	}

	private static List<MessageListener> listeners = new ArrayList<MessageListener>();

	public static void addListener(MessageListener toAdd) {
		listeners.add(toAdd);
	}

	public static void onMessageRecieved(String message) {
		// Notify everybody that may be interested.
		for (MessageListener hl : listeners)
			hl.onMessageRecieved(message);
	}

	public static void onCientRecieved(InetAddress ip) {
		// Notify everybody that may be interested.
		for (MessageListener hl : listeners)
			hl.onClientIPRecieved(ip);
	}

}
