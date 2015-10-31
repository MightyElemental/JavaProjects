package net.mightyelemental.network.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import net.mightyelemental.network.BasicCommands;
import net.mightyelemental.network.listener.ClientInitiater;
import net.mightyelemental.network.listener.MessageListenerClient;

public class TCPClient {

	Socket				clientSocket;
	DataOutputStream	outToServer;

	private String clientUID = "UNASIGNED";

	public boolean running;

	private String	address;
	private int		port;

	private ClientInitiater initiater = new ClientInitiater();

	private String lastMessage = "";

	private Thread clientTick = new Thread("ClientReceiveThread") {

		public void run() {
			while (running) {
				try {
					BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					lastMessage = inFromServer.readLine();
					lastMessage = BasicCommands.decryptMessageBase64(lastMessage);
				} catch (IOException e) {
					e.printStackTrace();
				}
				initiater.onMessageRecieved(lastMessage);
			}
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	public TCPClient( String address, int port ) {
		this.address = address;
		this.port = port;
	}

	public synchronized void setup() {
		try {
			clientSocket = new Socket(address, port);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientTick.start();
	}

	/** Adds listener to initiater
	 * 
	 * @param mlc
	 *            the MessageListenerClient instance */
	public void addListener(MessageListenerClient mlc) {
		initiater.addListener(mlc);
	}

	public void sendMessage(String message) {
		message = message + '\n';
		message = BasicCommands.encryptMessageBase64(message);
		try {
			outToServer.writeBytes(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** @return the clientUID */
	public String getClientUID() {
		return clientUID;
	}

}
