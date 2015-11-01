package net.mightyelemental.network.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import net.mightyelemental.network.BasicCommands;

public class TCPClient extends Client {

	private Socket				clientSocket;
	private DataOutputStream	out;
	private BufferedReader		in;

	private boolean running;

	private Thread clientTick = new Thread("ClientReceiveThread") {

		public void run() {
			running = true;
			while (running) {

				System.out.println("temp");
				String tempMessage = null;
				try {
					tempMessage = in.readLine();
				} catch (IOException e) {
					System.err.println("Server has been closed");
					stopClient();
				}
				System.out.println(tempMessage);
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
		}
		try {
			clientSocket.setReceiveBufferSize(2 ^ 9);
			clientSocket.setSendBufferSize(2 ^ 9);
			clientSocket.setKeepAlive(true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientTick.start();
	}

	public void sendMessage(String message) {
		message = message + '\n';
		message = BasicCommands.encryptMessageBase64(message);
		try {
			out.writeChars(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** @return the clientUID */
	public String getClientUID() {
		return clientUID;
	}

	/** Used to stop the client thread */
	public synchronized void stopClient() {// make a listener method instead of this
		running = false;
		try {
			clientTick.join(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
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
