package net.mightyelemental.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

public class BasicCommands {
	
	
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
		
		for (int i = 0; i < 4; i++) {
			chars += (char) (rand.nextInt(26) + 'a');
		}
		return chars;
	}
}
