package net.mightyelemental.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

public class BasicCommands {

	/** Send a message to a client
	 * 
	 * @param udpServer
	 *            is the UDPServer instance
	 * @param message
	 *            the message to be sent
	 * @param clientSendIP
	 *            the IP address of the client that is sending the message
	 * @param clientSendPort
	 *            the port of the client that is sending the message
	 * @param clientReceivceIP
	 *            the IP of the client that receives the message
	 * @param cliRecPort
	 *            the port of the client that receives the message
	 * @throws InterruptedException
	 *             if the sleep is interrupted */
	public static void cToSToCMessage(UDPServer udpServer, String message, InetAddress clientSendIP, int clientSendPort,
			InetAddress clientReceivceIP, int cliRecPort) throws InterruptedException {
		String sendMessage = udpServer.getClientUIDFromIP(clientSendIP, clientSendPort) + "> " + message;
		udpServer.sendMessage(sendMessage, clientReceivceIP, cliRecPort);
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
		if (message == null) { return "null"; }
		byte[] rawData = DatatypeConverter.parseBase64Binary(message);

		for (byte b : rawData) {
			temp += (char) b;
		}

		return temp;
	}

	/** Gets the servers External IP Address. <br>
	 * It uses <a href="http://checkip.amazonaws.com">'http://checkip.amazonaws.com'</a> to do so. */
	public static String getExternalIPAddress() {
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine(); // you get the IP as a String
			return ip;
		} catch (Exception e) {
		}
		return "0.0.0.0";
	}

	/** Creates a UID for a new IP/Port */
	public static String generateClientUID(Random rand) {
		String chars = "";

		for (int i = 0; i < 6; i++) {
			chars += (char) (rand.nextInt(26) + 'a');
		}
		return chars;
	}
}
