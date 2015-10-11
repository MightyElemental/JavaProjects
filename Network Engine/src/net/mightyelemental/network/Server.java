package net.mightyelemental.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Server {

	private int		port;
	public boolean	running;
	public Parser	parser;

	public static Random random = new Random();

	public DatagramSocket serverSocket;

	public Map<String, List<Object>> attachedClients = new HashMap<String, List<Object>>();

	public byte[]	receiveData;
	public byte[]	sendData;

	private String	lastMessage	= "";
	private boolean	parse		= true;

	private Thread serverTick = new Thread("ServerThread") {

		public void run() {

			receiveData = new byte[1024];
			sendData = new byte[1024];

			while (running) {
				receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				try {

					serverSocket.receive(receivePacket);
					String data = new String(receivePacket.getData()).trim();

					InetAddress IPAddress = receivePacket.getAddress();

					int port = receivePacket.getPort();

					String[] dataArray = data.split(" : ");

					StringBuilder sb = new StringBuilder();

					for (int i = 1; i < dataArray.length; i++) {
						sb.append(dataArray[i]);
					}

					String message = sb.toString();
					String sender = dataArray[0];
					if (lastMessage.equals(message)) {
						parse = false;
					} else {
						lastMessage = message;
						parse = true;
					}

					if (parse) {
						handleMessage(IPAddress, port);
						parser.parseMessage(message, sender, IPAddress, port);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				this.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	};

	public Server( int port ) {
		this.port = port;
		this.running = true;
		parser = new Parser(this, this.port);
	}

	public int getPort() {
		return this.port;
	}

	public void setupServer() {
		try {
			serverSocket = new DatagramSocket(port);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		serverTick.start();
	}

	private void handleMessage(InetAddress ip, int port) {
		if (attachedClients.containsValue(Arrays.asList(new Object[] { ip, port }))) { return; }
		generateClientInfo(ip, port, random);
	}

	private void generateClientInfo(InetAddress ip, int port, Random rand) {
		String chars = generateClientUID(rand);
		while (attachedClients.containsKey(chars)) {
			chars = generateClientUID(rand);
		}
		System.out.println("New client! " + chars + " | IP: " + ip.getHostAddress() + ":" + port);
		attachedClients.put(chars, Arrays.asList(new Object[] { ip, port }));
	}

	private String generateClientUID(Random rand) {
		String chars = "";

		for (int i = 0; i < 6; i++) {
			chars += (char) (rand.nextInt(26) + 'a');
		}
		return chars;
	}

	public static String getExternalIPAddress() {
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine(); // you get the IP as a String
			return ip;
		} catch (Exception e) {
		}
		return "0.0.0.0";
	}
}