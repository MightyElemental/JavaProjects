package net.mightyelemental.network;

import java.net.InetAddress;

import net.mightyelemental.network.listener.MessageListenerServer;

public interface Server {

	public void sendMessage(String message, InetAddress ip, int port);

	public void addListener(MessageListenerServer mls);

}
