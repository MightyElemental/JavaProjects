package net.mightyelemental.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class TCPClient extends Client {
	
	
	public TCPClient( String address, int port, boolean usesEncryption, int maxBytes, String verifyCode ) {
		super(address, port, usesEncryption, maxBytes, verifyCode);
	}
	
	private Socket clientSocket;
	
	private Thread clientTick = new Thread("ClientReceiveThread") {
		
		
		@SuppressWarnings( "unchecked" )
		public void run() {
			running = true;
			while (running) {
				
				try {
					if (ois == null) {
						continue;
					}
					Object obj = ois.readObject();
					lastObject = obj;
					if (((Map<String, Object>) obj).containsKey("VerifyCodeRequest")) {
						sendObject("VerifyCode", getVerifyCode());
					} else {
						initiater.onObjectRecieved(obj);
					}
					
					if (((Map<String, Object>) obj).containsKey("UID")) { // Sets
																			// client
																			// uid
						clientUID = (String) ((Map<String, Object>) obj).get("UID");
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					break;
				} catch (SocketException e) {
					initiater.onClientDropped("default");
					break;
				} catch (IOException e) {
					initiater.onClientDropped("default");
					e.printStackTrace();
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
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	};
	
	public synchronized void setup() throws IOException {
		boolean flag = false;
		try {
			clientSocket = new Socket(address, port);
		} catch (ConnectException e) {
			flag = true;
		}
		if (!flag) {
			clientSocket.setReceiveBufferSize(maxBytes);
			clientSocket.setSendBufferSize(maxBytes);
			clientSocket.setKeepAlive(true);
			ois = new ObjectInputStream(clientSocket.getInputStream());
			ous = new ObjectOutputStream(clientSocket.getOutputStream());
			clientTick.start();
			hasBeenSetup = true;
		} else {
			this.initiater.onConnectionRefused();
		}
	}
	
	/** Used to stop the client thread
	 * 
	 * @throws IOException */
	public synchronized void stopClient() throws InterruptedException, IOException {
		running = false;
		clientTick.interrupt();
		if (clientSocket != null) {
			clientSocket.close();
		}
		// clientTick.join(2000);
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
