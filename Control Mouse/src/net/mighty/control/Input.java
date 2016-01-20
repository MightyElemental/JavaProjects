package net.mighty.control;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;

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

	private Robot robot;

	public Thread clientThread = new Thread("client") {

		public void run() {
			try {
				robot = new Robot();
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
			while (Control.connected) {
				try {
					long time1 = System.currentTimeMillis();
					Control.capture = robot.createScreenCapture(clientScreenRect);
					Control.capture = Control.resize(Control.capture, (int) (Control.capture.getWidth() * 0.8),
							(int) (Control.capture.getHeight() * 0.8));
					sleep(100);
					// System.out.println(Control.imgToBytes(Control.capture).length);
					client.sendObject(Control.imgToBytes(Control.capture));
					// client.out.println("hello there");
					System.out.println("Time to capture and send image: " + (System.currentTimeMillis() - time1));
					Control.frame.entiresList.add("Time to capture and send image: " + (System.currentTimeMillis() - time1));
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("'Wow!' *wink*\nAn Error has occured!\n");
				}
			}
		}

	};

	public int unsignedToBytes(byte b) {
		return b & 0xFF;
	}

	@Override
	public void onNewClientAdded(InetAddress ip, int port, String UID) {
		System.out.println("hello " + ip.getHostAddress() + ":" + port + " (" + UID + ")");
		Control.frame.entiresList.add("Added " + ip.getHostAddress() + ":" + port + " (" + UID + ")");
	}

	@Override
	public void onObjectRecievedFromServer(Object obj) {

	}

	long start = System.currentTimeMillis();

	@Override
	public void onObjectRecievedFromServer(InetAddress ip, int port, Object obj) {
		if (obj instanceof byte[]) {
			System.out.println("ms per frame: " + (System.currentTimeMillis() - start));
			Control.frame.entiresList.add("ms per frame: " + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();
			try {
				BufferedImage tmp = Control.bytesToImg((byte[]) obj);
				if (tmp != null) {
					Control.capture = tmp;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Control.frame.remoteView.repaint();
		}
	}

	@Override
	public void onServerClosed() {
		System.err.println("Server Closed");
		System.exit(0);
	}

}
