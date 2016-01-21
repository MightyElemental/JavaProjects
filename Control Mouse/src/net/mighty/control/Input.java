package net.mighty.control;

import java.awt.AWTException;
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
	}

	private Robot robot;

	int	x1	= 0;
	int	y1	= 0;

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

					sleep(100);

					Control.capture = robot.createScreenCapture(clientScreenRect);
					Control.capture = Control.resize(Control.capture, (int) (Control.capture.getWidth() * 0.9),
							(int) (Control.capture.getHeight() * 0.9));
					// System.out.println(Control.imgToBytes(Control.capture).length);
					objectToSend.clear();
					objectToSend = null;
					objectToSend = new HashMap<String, Object>();
					objectToSend.put("clientMouseX", x1);
					objectToSend.put("clientMouseY", y1);
					objectToSend.put("clientScreen", clientScreenRect);
					objectToSend.put("screenCapture", Control.imgToBytes(Control.capture));
					client.sendObjectMap(objectToSend);
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

	}

	long start = System.currentTimeMillis();

	@Override
	public void onObjectRecievedFromServer(InetAddress ip, int port, Object obj) {
		@SuppressWarnings( "unchecked" )
		Map<String, Object> objMap = (Map<String, Object>) obj;

		if (objMap.containsKey("clientMouseX") && objMap.containsKey("clientMouseY")) {
			Control.frame.r.x = (int) objMap.get("clientMouseX");
			Control.frame.r.y = (int) objMap.get("clientMouseY");
			System.out.println(Control.frame.r.x);
		}
		if (objMap.containsKey("clientScreen")) {
			Control.frame.r.width = ((Rectangle) objMap.get("clientScreen")).width;
			Control.frame.r.height = ((Rectangle) objMap.get("clientScreen")).height;
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
