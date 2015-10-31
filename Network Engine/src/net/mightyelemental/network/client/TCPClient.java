package net.mightyelemental.network.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

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

	private long	timeOfPingRequest	= 0l;
	private long	timeOfPingResponse	= 0l;
	private long	pingTime			= 0l;

	private String				lastMessage			= "";
	private ArrayList<String>	recievedMessages	= new ArrayList<String>();

	private Thread clientTick = new Thread("ClientReceiveThread") {

		public void run() {
			while (running) {
				try {
					BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					String tempMessage = inFromServer.readLine();
					tempMessage = BasicCommands.decryptMessageBase64(tempMessage);

					if (tempMessage.contains("JLB1F0_CLIENT_UID")) {
						clientUID = tempMessage.replace("JLB1F0_CLIENT_UID ", "");
					} else if (tempMessage.contains("JLB1F0_RETURN_PING")) {
						timeOfPingResponse = System.currentTimeMillis();
						pingTime = timeOfPingResponse - timeOfPingRequest;
					} else {
						lastMessage = tempMessage;
						recievedMessages.add(lastMessage);
						initiater.onMessageRecieved(lastMessage);
					}

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

	/** Used to stop the client thread */
	public synchronized void stopClient() {
		running = false;
		try {
			clientTick.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** Pings the server */
	public void sendPingRequest() {
		timeOfPingRequest = System.currentTimeMillis();
		sendMessage("JLB1F0_PING_SERVER");
	}

	/** @return the time it took to ping the server */
	public long getPingTime() {
		return pingTime;
	}

}
