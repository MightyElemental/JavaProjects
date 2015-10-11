package net.mightyelemental.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Commands {

	public static void messageUser(Server server, String[] commands, InetAddress ip, int port) throws UnknownHostException {
		if (commands.length < 3) { return; }
		InetAddress sendIP = InetAddress.getByName(commands[1]);
		String sendMessage = ip.getHostAddress() + "> ";
		for (int i = 2; i < commands.length; i++) {
			sendMessage += commands[i] + " ";
		}
		try {
			server.sendData = (sendMessage.toString()).getBytes("UTF-8");
			DatagramPacket sendPacket = new DatagramPacket(server.sendData, server.sendData.length, sendIP, port);
			server.serverSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
