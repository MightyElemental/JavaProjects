package net.mightyelemental.network.listener;

import java.net.InetAddress;

public interface MessageListener {

	public void onMessageRecieved(String message);

	public void onClientIPRecieved(InetAddress ip);

}
