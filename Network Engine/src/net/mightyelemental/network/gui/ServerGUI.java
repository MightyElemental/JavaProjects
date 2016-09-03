package net.mightyelemental.network.gui;

import java.awt.Graphics;
import java.awt.List;
import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.mightyelemental.network.Server;
import net.mightyelemental.network.TCPServer;
import net.mightyelemental.network.UDPServer;

@SuppressWarnings( "serial" )
public class ServerGUI extends JFrame {
	
	
	public List clientList = new List();
	public Server server;
	public JLabel lblClients;
	
	public JTextField textField;
	public List commands = new List();
	
	/** Create the frame. */
	public ServerGUI( String title, Server server, String IPAddress ) {
		this.server = server;
		// addComponentListener(new ComponentAdapter() {
		//
		//
		// @Override
		// public void componentResized(ComponentEvent e) {
		// int height = getHeight();
		// if (getHeight() < 328) {
		// height = 328;
		// }
		// setSize(new Dimension(475, height));// Force window to be certain width
		// super.componentResized(e);
		// }
		//
		// });
		setResizable(true);
		setVisible(true);
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 475, 387);
		this.setLocationRelativeTo(null);
	}
	
	public void updateClients() {
		clientList.removeAll();
		if (server instanceof UDPServer) {
			Object[] keys = ((UDPServer) server).getAttachedClients().keySet().toArray();
			for (Object key : keys) {
				InetAddress ip = (InetAddress) ((UDPServer) server).getAttachedClients().get(key).toArray()[0];
				int port = Integer.parseInt(((UDPServer) server).getAttachedClients().get(key).toArray()[1] + "");
				String temp = "IP:" + ip.toString().replace('/', '\0') + ":" + port + " (" + key.toString() + ")";
				clientList.add(temp);
			}
			lblClients.setText("Clients (" + ((UDPServer) server).getAttachedClients().size() + ")");
		} else {
			Object[] keys = ((TCPServer) server).getTcpConnections().keySet().toArray();
			for (Object key : keys) {
				InetAddress ip = ((TCPServer) server).getTcpConnections().get(key).getIp();
				int port = ((TCPServer) server).getTcpConnections().get(key).getPort();
				String verified = "";
				if (((TCPServer) server).getTcpConnections().get(key).isVerified()) {
					verified = " \u2713";
				}
				String temp = "IP:" + ip.toString().replace('/', '\0') + ":" + port + " (" + key.toString() + ")" + verified;
				clientList.add(temp);
			}
			lblClients.setText("Clients (" + ((TCPServer) server).getTcpConnections().size() + ")");
		}
		repaintFrame();
	}
	
	private void repaintFrame() {
		lblClients.validate();
		lblClients.repaint();
		this.validate();
		this.repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
	}
	
	public void addCommand(String command) {
		commands.add(command);
		this.repaint();
		commands.select(commands.getItemCount() - 1);
	}
}
