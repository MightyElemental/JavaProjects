package net.mightyelemental.network.gui;

import java.awt.Graphics;
import java.awt.List;
import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.mightyelemental.network.TCPServer;

@SuppressWarnings( "serial" )
public class ServerGUI extends JFrame {
	
	
	public List clientList = new List();
	public TCPServer server;
	public JLabel lblClients;
	
	public JTextField textField;
	public List commands = new List();
	
	/** Create the frame. */
	public ServerGUI( String title, TCPServer server, String IPAddress ) {
		this.server = server;
		setResizable(true);
		setVisible(true);
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 475, 387);
		this.setLocationRelativeTo(null);
	}
	
	public void updateClients() {
		clientList.removeAll();
		
		Object[] keys = server.getTcpConnections().keySet().toArray();
		for (Object key : keys) {
			if (server.getTcpConnections().get(key) != null) {
				InetAddress ip = server.getTcpConnections().get(key).getIp();
				int port = server.getTcpConnections().get(key).getPort();
				String verified = "";
				if (server.getTcpConnections().get(key).isVerified()) {
					verified = " \u2713";
				}
				String temp = "IP:" + ip.toString().replace('/', '\0') + ":" + port + " (" + key.toString() + ")" + verified;
				clientList.add(temp);
			}
		}
		lblClients.setText("Clients (" + server.getTcpConnections().size() + ")");
		
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
