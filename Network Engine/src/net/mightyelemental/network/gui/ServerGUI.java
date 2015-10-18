package net.mightyelemental.network.gui;

import java.awt.List;
import java.net.InetAddress;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.mightyelemental.network.Server;
import javax.swing.border.EtchedBorder;

@SuppressWarnings( "serial" )
public class ServerGUI extends JFrame {

	private JPanel contentPane;

	private List	list	= new List();
	private Server	server;

	/** Create the frame. */
	public ServerGUI( String title, Server server ) {
		this.server = server;
		setResizable(false);
		setVisible(true);
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 90, 233, 171);
		contentPane.add(panel);
		panel.setLayout(null);

		list.setBounds(10, 24, 213, 137);
		panel.add(list);

		JLabel lblClients = new JLabel("Clients");
		lblClients.setHorizontalAlignment(SwingConstants.CENTER);
		lblClients.setBounds(0, 4, 233, 14);
		panel.add(lblClients);
	}

	public void updateClients(Map<String, java.util.List<Object>> map) {
		list.removeAll();
		Object[] keys = server.getAttachedClients().keySet().toArray();
		for (Object key : keys) {
			InetAddress ip = (InetAddress) server.getAttachedClients().get(key).toArray()[0];
			int port = Integer.parseInt(server.getAttachedClients().get(key).toArray()[1] + "");
			String temp = "IP:" + ip.toString().replace('/', '\0') + ":" + port + " (" + key.toString() + ")";
			list.add(temp);
		}
	}
}
