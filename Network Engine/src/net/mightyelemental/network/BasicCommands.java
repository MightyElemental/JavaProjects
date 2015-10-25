package net.mightyelemental.network;

import java.net.InetAddress;

import javax.xml.bind.DatatypeConverter;

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
	 * @throws InterruptedException
	 *             if the sleep is interrupted */
	public static void cToSToCMessage(Server server, String message, InetAddress clientSendIP, int clientSendPort,
			InetAddress clientReceivceIP, int cliRecPort) throws InterruptedException {
		String sendMessage = server.getClientUIDFromIP(clientSendIP, clientSendPort) + "> " + message;
		server.sendMessage(sendMessage, clientReceivceIP, cliRecPort);
	}

	/** Used to encrypt messages using Base64
	 * 
	 * @param message
	 *            the message to be encrypted */
	public static String encryptMessageBase64(String message) {
		String temp = DatatypeConverter.printBase64Binary(message.getBytes());
		return temp;
	}

	/** Used to decrypt messages using Base64
	 * 
	 * @param message
	 *            the encrypted message to be decrypted */
	public static String decryptMessageBase64(String message) {
		String temp = "";
		byte[] rawData = DatatypeConverter.parseBase64Binary(message);

		for (byte b : rawData) {
			temp += (char) b;
		}

		return temp;
	}
}
