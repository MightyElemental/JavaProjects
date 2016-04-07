package net.mightyelemental.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import net.mightyelemental.network.BasicCommands;

public class TCPClient extends Client {
	
	
	private Socket clientSocket;
	
	private boolean usesEncryption = false;
	
	private int maxBytes;
	
	private Thread clientTick = new Thread("ClientReceiveThread") {
		
		
		@SuppressWarnings( "unchecked" )
		public void run() {
			running = true;
			while (running) {
				
				try {
					Object obj = ois.readObject();
					lastObject = obj;
					initiater.onObjectRecieved(obj);
					
					if (((Map<String, Object>) obj).containsKey("UID")) { // Sets
																			// client
																			// uid
						clientUID = (String) ((Map<String, Object>) obj).get("UID");
					}
				} catch (ClassNotFoundException | IOException e) {
					initiater.onServerClosed();
					break;
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
				stopClient();
			} catch (InterruptedException e) {
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
	
	public synchronized void setup() throws IOException {
		clientSocket = new Socket(address, port);
		
		clientSocket.setReceiveBufferSize(maxBytes);
		clientSocket.setSendBufferSize(maxBytes);
		clientSocket.setKeepAlive(true);
		// in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		// out = new PrintStream(clientSocket.getOutputStream());
		// byteOut = new DataOutputStream(clientSocket.getOutputStream());
		ois = new ObjectInputStream(clientSocket.getInputStream());
		ous = new ObjectOutputStream(clientSocket.getOutputStream());
		
		clientTick.start();
		hasBeenSetup = true;
	}
	
	/** Used to stop the client thread */
	public synchronized void stopClient() throws InterruptedException {// make a listener method instead of this
		running = false;
		clientTick.join(2000);
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
	public void sendObject(String varName, Object obj) throws IOException {
		if (!hasBeenSetup) {
			System.err.println("FATAL ERROR: Client has not been setup yet!");
			return;
		}
		if (!running) { return; }
		objectToSend.clear();
		objectToSend.put(varName, obj);
		ous.writeObject(objectToSend);
		objectToSend.clear();
		objectToSend = null;
		objectToSend = new HashMap<String, Object>();
	}
	
	@Override
	public void sendObjectMap(Map<String, Object> objects) throws IOException {
		if (!hasBeenSetup) {
			System.err.println("FATAL ERROR: Client has not been setup yet!");
			return;
		}
		if (!running) { return; }
		ous.writeObject(objects);
		objectToSend.clear();
		objectToSend = null;
		objectToSend = new HashMap<String, Object>();
	}
	
}
