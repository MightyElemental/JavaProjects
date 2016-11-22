package net.mighty.control;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

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

	public int	width	= 1920;
	public int	height	= 1080;

	public synchronized void createClient(String ip, int port) {
		width = Toolkit.getDefaultToolkit().getScreenSize().width;
		height = Toolkit.getDefaultToolkit().getScreenSize().height;
		clientScreenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		clientScreenRect.setBounds(0, 0, (int) (width * 0.3), (int) (height * 0.3));
		client = new TCPClient(ip, port, false, maxBytes);
		client.setup();
		client.addListener(this);
		clientThread.start();
		try {
			client.sendObject("OriginalSize", new Dimension(width, height));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Robot robot;

	int	x1	= 0;
	int	y1	= 0;

	int	newMouseX	= 0;
	int	newMouseY	= 0;

	private Map<String, Object> objectToSend = new HashMap<String, Object>();

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
					PointerInfo a = MouseInfo.getPointerInfo();
					Point b = a.getLocation();
					x1 = (int) b.getX();
					y1 = (int) b.getY();
					int w = (int) (width * Control.frame.slider.getValue() * 0.01);
					int h = (int) (height * Control.frame.slider.getValue() * 0.01);
					clientScreenRect.setBounds(x1 - (w / 2), y1 - (h / 2), w, h);

					sleep(50);

					Control.capture = robot.createScreenCapture(clientScreenRect);
					Control.capture = Control.resize(Control.capture, (int) (Control.capture.getWidth() * 0.9),
							(int) (Control.capture.getHeight() * 0.9));
					// System.out.println(Control.imgToBytes(Control.capture).length);
					objectToSend.put("clientMouseX", x1);
					objectToSend.put("clientMouseY", y1);
					objectToSend.put("clientScreenW", clientScreenRect.getWidth());
					objectToSend.put("clientScreenH", clientScreenRect.getHeight());
					objectToSend.put("screenCapture", Control.imgToBytes(Control.capture));
					objectToSend.put("sliderValue", Control.frame.slider.getValue() * 0.01);
					client.sendObjectMap(objectToSend);
					objectToSend = null;
					objectToSend = new HashMap<String, Object>();
					// client.out.println("hello there");
					// System.out.println("Time to capture and send image: " + (System.currentTimeMillis() - time1));
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
		@SuppressWarnings( "unchecked" )
		Map<String, Object> objMap = (Map<String, Object>) obj;
		System.out.println(obj);
		if (objMap.containsKey("newMouseX")) {
			this.newMouseX = (int) objMap.get("newMouseX");
		}
		if (objMap.containsKey("newMouseY")) {
			this.newMouseY = (int) objMap.get("newMouseY");
			robot.mouseMove(newMouseX, newMouseY);
			System.out.println("MOVED");
		}
	}

	long start = System.currentTimeMillis();

	boolean hasMoved = false;

	@Override
	public void onObjectRecievedFromServer(InetAddress ip, int port, Object obj) {
		@SuppressWarnings( "unchecked" )
		Map<String, Object> objMap = (Map<String, Object>) obj;

		if (hasMoved) {
			try {
				host.sendObject("newMouseX", newMouseX, ip, port);
				host.sendObject("newMouseY", newMouseY, ip, port);
				System.out.println("MOVE");
				hasMoved = false;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		if (objMap.containsKey("OriginalSize")) {
			this.width = ((Dimension) objMap.get("OriginalSize")).width;
			this.height = ((Dimension) objMap.get("OriginalSize")).height;
		}

		if (objMap.containsKey("sliderValue")) {
			Control.frame.sliderValue = (double) objMap.get("sliderValue");
		}

		if (objMap.containsKey("clientMouseX") && objMap.containsKey("clientMouseY")) {
			Control.frame.r.x = (int) objMap.get("clientMouseX");
			Control.frame.r.y = (int) objMap.get("clientMouseY");
		}
		if (objMap.containsKey("clientScreenW") && objMap.containsKey("clientScreenH")) {
			Control.frame.r.width = (int) ((double) objMap.get("clientScreenW"));
			Control.frame.r.height = (int) ((double) objMap.get("clientScreenH"));
		}
		if (objMap.containsKey("screenCapture")) {
			// System.out.println("ms per frame: " + (System.currentTimeMillis() - start));
			Control.frame.entiresList.add("ms per frame: " + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();
			try {
				BufferedImage tmp = Control.bytesToImg((byte[]) objMap.get("screenCapture"));
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
