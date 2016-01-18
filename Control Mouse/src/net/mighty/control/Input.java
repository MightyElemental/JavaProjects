package net.mighty.control;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import net.mightyelemental.network.TCPServer;
import net.mightyelemental.network.client.TCPClient;
import net.mightyelemental.network.listener.MessageListenerClient;
import net.mightyelemental.network.listener.MessageListenerServer;

public class Input implements MessageListenerServer, MessageListenerClient {

	TCPServer	host;
	TCPClient	client;

	public int maxBytes = 2 ^ 20;

	public synchronized void createHost(int port) {
		host = new TCPServer(port, false, maxBytes);
		host.setupServer();
		host.addListener(this);
	}

	public static Rectangle clientScreenRect;

	public synchronized void createClient(String ip, int port) {
		clientScreenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		client = new TCPClient(ip, port, false, maxBytes);
		client.setup();
		client.addListener(this);
		clientThread.start();
	}

	public Thread clientThread = new Thread("client") {

		public void run() {
			while (Control.connected) {
				try {
					Control.capture = new Robot().createScreenCapture(clientScreenRect);
					Control.capture = Control.resize(Control.capture, (int) (Control.capture.getWidth() * 0.8),
							(int) (Control.capture.getHeight() * 0.8));
					sleep((int) (1000));
					// System.out.println(Control.imgToBytes(Control.capture).length);
					// client.out.write(Control.imgToBytes(Control.capture));
					client.out.println("hello there");
				} catch (InterruptedException | AWTException e) {
					e.printStackTrace();
				}
			}
		}

	};

	public int unsignedToBytes(byte b) {
		return b & 0xFF;
	}

	@Override
	public void onMessageRecievedFromClient(String message, InetAddress ip, int port) {
		Control.frame.entiresList.add(message);
	}

	@Override
	public void onNewClientAdded(InetAddress ip, int port, String UID) {
		System.out.println("hello " + ip.getHostAddress() + ":" + port + " (" + UID + ")");
		Control.frame.entiresList.add("Added " + ip.getHostAddress() + ":" + port + " (" + UID + ")");
	}

	@Override
	public void onMessageRecievedFromServer(String message) {
		Control.frame.entiresList.add(message);
	}

	@Override
	public void onBytesRecievedFromServer(byte[] bytes) {
		// Control.frame.entiresList.add(bytes.toString());
	}

	public List<Integer> temp = new ArrayList<Integer>();

	@Override
	public void onBytesRecievedFromClient(byte[] bytes, InetAddress ip, int port) {
		String tempStr = "";
		for (byte b : bytes) {
			tempStr += (char) b;
		}
		System.out.println("*" + tempStr + "*");
		for (int i = 0; i < bytes.length; i++) {
			// if (bytes[i] < 0) {
			// continue;
			// }
			temp.add(bytes[i] & 0xFF);
		}
		byte[] b2 = new byte[temp.size()];
		for (int i = 0; i < temp.size(); i++) {
			Object e = temp.toArray()[i];
			int b = (int) e;
			b2[i] = (byte) b;
		}
		try {
			BufferedImage tmp = Control.bytesToImg(b2);
			if (tmp != null) {
				Control.capture = tmp;
				System.out.println("new");
				temp.clear();
			}
			if (Control.capture != null) {
				String temp = Control.capture.getHeight() + " x " + Control.capture.getWidth();
				System.out.println(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Control.frame.remoteView.repaint();
	}

}
