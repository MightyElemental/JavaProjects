package net.mightyelemental.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Parser {

	private Server					server;
	private ArrayList<InetAddress>	addressList	= new ArrayList<InetAddress>();
	private int						port;

	public Parser( Server server, int port ) {
		this.server = server;
		this.port = port;
	}

	public void parseMessage(String message, String sender, InetAddress ip, int port) {
		if (!addressList.contains(ip)) {
			addressList.add(ip);
		}

		try {
			String[] msg = message.split(" ");

			for (int i = 0; i < msg.length; i++) {
				System.out.println(msg[i]);
			}

			switch (msg[0].toUpperCase()) {
				case "PING":
					sendMessage("PONG!", ip, port);
					break;
				case "MSG":
					Commands.messageUser(server, msg, ip, port);
					break;
				default:
					sendMessage("Invalid command.", ip, port);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			sendMessage("Internal Server Error.", ip, port);
		}
	}

	public void sendMessage(String message, InetAddress ip, int port) {
		String sendMessage = message;

		try {
			server.sendData = (sendMessage.toString()).getBytes("UTF-8");
			DatagramPacket sendPacket = new DatagramPacket(server.sendData, server.sendData.length, ip, port);
			server.serverSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void broadCastmessage(String message) {
		for (int i = 0; i < addressList.size(); i++) {
			sendMessage(message, addressList.get(i), this.port);
		}
	}
}
