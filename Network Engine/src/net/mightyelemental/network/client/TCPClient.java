package net.mightyelemental.network.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import net.mightyelemental.network.BasicCommands;

public class TCPClient extends Client {

	Socket				clientSocket;
	DataOutputStream	outToServer;

	public boolean running;

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

		} catch (IOException e) {
			System.err.println("Could not connect to server!");
			stopClient();
			System.exit(1);
		}
		try {
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientTick.start();
	}

	public void sendMessage(String message) {
		message = message + '\n';
		message = BasicCommands.encryptMessageBase64(message);
		try {
			outToServer.writeChars(message);
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

	/** @return the port the client is running on */
	public int getPort() {
		return port;
	}

	/** @return the IP address in the form of String */
	public String getAddress() {
		return this.address;
	}

	/** @return the full IP address */
	public String getFullIPAddress() {
		return getAddress() + ":" + getPort();
	}

	/** @return the clients name */
	public String getUID() {
		return this.clientUID;
	}

}
