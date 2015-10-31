package net.mightyelemental.network.client;

import java.util.ArrayList;

import net.mightyelemental.network.listener.ClientInitiater;
import net.mightyelemental.network.listener.MessageListenerClient;

public abstract class Client {

	protected long	timeOfPingRequest	= 0l;
	protected long	timeOfPingResponse	= 0l;
	protected long	pingTime			= 0l;

	protected String clientUID = "UNASIGNED";

	protected String	address;
	protected int		port;

	protected ClientInitiater initiater = new ClientInitiater();

	protected String			lastMessage			= "";
	protected ArrayList<String>	recievedMessages	= new ArrayList<String>();

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

	/** @return the clientUID */
	public String getClientUID() {
		return clientUID;
	}

	/** Adds listener to initiater
	 * 
	 * @param mlc
	 *            the MessageListenerClient instance */
	public void addListener(MessageListenerClient mlc) {
		initiater.addListener(mlc);
	}

	/** @return lastRecievedMessage - the message the the client last received */
	public String getLastRecievedMessage() {
		return lastMessage;
	}

	/** @return the recievedMessages */
	public ArrayList<String> getRecievedMessages() {
		return recievedMessages;
	}

	public abstract void setup();

	public abstract void sendMessage(String message);

}
