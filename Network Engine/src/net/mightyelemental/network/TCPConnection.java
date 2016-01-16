package net.mightyelemental.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import net.mightyelemental.network.gui.ServerGUI;
import net.mightyelemental.network.listener.ServerInitiater;

public class TCPConnection {

	private Socket		client;
	private InetAddress	ip;
	private int			port;
	private String		UID;

	public BufferedReader	in;
	public DataOutputStream	out;

	private ServerGUI serverGUI;

	private ServerInitiater SI;

	private boolean running = false;

	private Thread run = new Thread("TCPConnection_Undef") {

		public void run() {
			try {
				while (running) {
					in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					out = new DataOutputStream(client.getOutputStream());
					String message = in.readLine();
					System.out.println("Before decryp: " + message);// SENDS BYTE ARRAYS! DO NOT DECRYPT THEM!
					if (message == null) {
						continue;
					}
					message = BasicCommands.decryptMessageBase64(message);
					System.out.println("After decryp: " + message);
					SI.onMessageRecieved(message, ip, port);
					if (message.contains("JLB1F0_TEST_CONNECTION RETURN_UID")) {
						sendMessage("JLB1F0_CLIENT_UID " + UID);
					} else if (message.contains("JLB1F0_PING_SERVER")) {
						returnPingRequest();
					} else {
						serverGUI.addCommand(UID + " : " + message);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				stopThread();
			}

		}
	};

	/** Returns a clients ping request */
	private void returnPingRequest() {
		try {
			sendMessage("JLB1F0_RETURN_PING");
			sendMessage("JLB1F0_CLIENT_UID " + UID);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public TCPConnection( Socket client, ServerInitiater initiater, ServerGUI serverGUI ) {
		this.client = client;
		this.ip = client.getInetAddress();
		this.port = client.getPort();
		this.SI = initiater;
		this.serverGUI = serverGUI;
		try {
			this.client.setSoTimeout(0);
			this.client.setKeepAlive(true);
			this.client.setReceiveBufferSize(2 ^ 9);
			this.client.setSendBufferSize(2 ^ 9);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Send a message to the client */
	public synchronized void sendMessage(String message) throws IOException {
		if (message == null) { return; }
		out = new DataOutputStream(client.getOutputStream());
		message = BasicCommands.encryptMessageBase64(message);
		out.writeChars(message);// Socket closed issue
	}

	/** Send a byte array to the client */
	public synchronized void sendBytes(byte[] bytes) throws IOException {
		if (bytes == null) { return; }
		out = new DataOutputStream(client.getOutputStream());
		out.write(bytes);// Socket closed issue
	}

	/** Start the clients thread */
	public synchronized void startThread() {
		running = true;
		run.start();
	}

	public synchronized void stopThread() {
		running = false;
		try {
			run.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** @return the client */
	public Socket getClient() {
		return client;
	}

	/** @return the ip */
	public InetAddress getIp() {
		return ip;
	}

	/** @return the port */
	public int getPort() {
		return port;
	}

	/** @return the uID */
	public String getUID() {
		return UID;
	}

	/** @param uID
	 *            the uID to set */
	public void setUID(String uID) {
		UID = uID;
		run.setName("TCPConnection_" + UID);
	}

}
