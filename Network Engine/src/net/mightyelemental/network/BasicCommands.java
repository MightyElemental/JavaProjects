package net.mightyelemental.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class BasicCommands {

	/** Send a message to a client
	 * 
	 * @param server
	 *            is the server instance
	 * @param message
	 *            the message to be sent
	 * @param clientIP
	 *            the IP address of the client to send the message to
	 * @param hostIP
	 *            the IP address of the connection sending the message
	 * @param port
	 *            the port to be sending the message through
	 * @throws UnknownHostException
	 *             if the IP address does not exist or is not valid */
	public static void cToSToCMessage(Server server, String message, InetAddress clientIP, InetAddress hostIP, int port)
			throws UnknownHostException {
		String sendMessage = hostIP.getHostAddress() + "> " + message;
		try {
			server.sendData = (sendMessage.toString()).getBytes("UTF-8");
			DatagramPacket sendPacket = new DatagramPacket(server.sendData, server.sendData.length, clientIP, port);
			server.serverSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
