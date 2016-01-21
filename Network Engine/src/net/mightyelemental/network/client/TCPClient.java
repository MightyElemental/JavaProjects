package net.mightyelemental.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import net.mightyelemental.network.BasicCommands;

public class TCPClient extends Client {

	private Socket clientSocket;

	private boolean running;

	private boolean usesEncryption = false;

	private int maxBytes;

	private Thread clientTick = new Thread("ClientReceiveThread") {

		public void run() {
			running = true;
			while (running) {

				try {
					Object obj = ois.readObject();
					initiater.onObjectRecieved(obj);
				} catch (ClassNotFoundException | IOException e) {
					initiater.onServerClosed();
				}

				// String tempMessage = null;
				// try {
				// tempMessage = in.readLine();
				// } catch (IOException e) {
				// System.err.println("Server has been closed");
				// stopClient();
				// }
				// System.out.println("[TCPClient] message: " + tempMessage);
				// if (usesEncryption) {
				// tempMessage = BasicCommands.decryptMessageBase64(tempMessage);
				// }
				//
				// if (tempMessage.contains("JLB1F0_CLIENT_UID")) {
				// clientUID = tempMessage.replace("JLB1F0_CLIENT_UID ", "");
				// } else if (tempMessage.contains("JLB1F0_RETURN_PING")) {
				// timeOfPingResponse = System.currentTimeMillis();
				// pingTime = timeOfPingResponse - timeOfPingRequest;
				// } else {
				// lastMessage = tempMessage;
				// recievedMessages.add(lastMessage);
				// }
			}
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	public TCPClient( String address, int port, boolean usesEncryption, int maxBytes ) {
		this.address = address;
		this.port = port;
		this.usesEncryption = usesEncryption;
		this.maxBytes = maxBytes;
	}

	public synchronized void setup() {
		try {
			clientSocket = new Socket(address, port);
		} catch (IOException e) {
			System.err.println("Could not connect to server!");
			stopClient();
		}
		try {
			clientSocket.setReceiveBufferSize(maxBytes);
			clientSocket.setSendBufferSize(maxBytes);
			clientSocket.setKeepAlive(true);
			// in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			// out = new PrintStream(clientSocket.getOutputStream());
			// byteOut = new DataOutputStream(clientSocket.getOutputStream());
			ois = new ObjectInputStream(clientSocket.getInputStream());
			ous = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientTick.start();
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

	/** @return the usesEncryption */
	public boolean doesUseEncryption() {
		return usesEncryption;
	}

	/** @param usesEncryption
	 *            the usesEncryption to set */
	public void setUseEncryption(boolean usesEncryption) {
		this.usesEncryption = usesEncryption;
	}

	@Deprecated
	public void sendMessage(String message) {
		message = message + '\n';
		if (usesEncryption) {
			message = BasicCommands.encryptMessageBase64(message);
		}
		// out.println(message);
	}

	@Override
	public void sendObject(Object obj) throws IOException {
		ous.writeObject(obj);
	}

	@Override
	@Deprecated
	public void sendBytes(byte[] bytes) {
	}

}
