package net.mightyelemental.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import net.mightyelemental.network.listener.ServerInitiater;

public class TCPConnection {

	private Socket		client;
	private InetAddress	ip;
	private int			port;
	private String		UID;

	public BufferedReader	in;
	public DataOutputStream	out;

	@SuppressWarnings( "unused" )
	private TCPServer tcpServer;

	private ServerInitiater SI;

	private Thread run = new Thread("TCPConnection_Undef") {

		public void run() {
			try {
				String message = in.readLine();
				message = BasicCommands.decryptMessageBase64(message);
				SI.onMessageRecieved(message, ip, port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	public TCPConnection( Socket client, ServerInitiater initiater, TCPServer tcpServer ) {
		this.client = client;
		this.ip = client.getInetAddress();
		this.port = client.getPort();
		this.SI = initiater;
		try {
			this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			this.out = new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.tcpServer = tcpServer;
	}

	/** Send a message to the client */
	public synchronized void sendMessage(String message) throws IOException {
		message = BasicCommands.encryptMessageBase64(message);
		out.writeChars(message);
	}

	/** Start the clients thread */
	public synchronized void startThread() {
		run.start();
	}

	public synchronized void stopThread() {
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

	/** @return the run */
	public Thread getRun() {
		return run;
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