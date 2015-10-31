package net.mightyelemental.network.gui;

import java.awt.Font;
import java.awt.List;
import java.net.InetAddress;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import net.mightyelemental.network.UDPServer;

@SuppressWarnings( "serial" )
public class ServerGUI extends JFrame {

	private JPanel contentPane;

	private List		list		= new List();
	private UDPServer		server;
	private JTextField	textField;
	List				commands	= new List();

	/** Create the frame. */
	public ServerGUI( String title, UDPServer server, String IPAddress ) {
		this.server = server;
		setResizable(false);
		setVisible(true);
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 475, 387);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 177, 233, 171);
		contentPane.add(panel);
		panel.setLayout(null);

		list.setBounds(10, 24, 213, 137);
		panel.add(list);

		JLabel lblClients = new JLabel("Clients");
		lblClients.setHorizontalAlignment(SwingConstants.CENTER);
		lblClients.setBounds(0, 4, 233, 14);
		panel.add(lblClients);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(253, 177, 206, 171);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblServerDetails = new JLabel("Server Details");
		lblServerDetails.setHorizontalAlignment(SwingConstants.CENTER);
		lblServerDetails.setBounds(0, 0, 206, 21);
		panel_1.add(lblServerDetails);

		JLabel lblServerIp = new JLabel("Server IP: ");
		lblServerIp.setBounds(10, 32, 62, 21);
		panel_1.add(lblServerIp);

		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 10));
		textField.setBounds(72, 32, 124, 21);
		panel_1.add(textField);
		textField.setEditable(false);
		textField.setText(IPAddress);
		textField.setColumns(10);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBounds(10, 11, 449, 155);
		contentPane.add(panel_2);
		panel_2.setLayout(null);

		commands.setBounds(10, 23, 429, 121);
		panel_2.add(commands);

		JLabel lblClientCommands = new JLabel("Client Commands");
		lblClientCommands.setHorizontalAlignment(SwingConstants.CENTER);
		lblClientCommands.setBounds(10, 2, 429, 14);
		panel_2.add(lblClientCommands);
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
		this.repaint();
	}

	public void addCommand(String command) {
		commands.add(command);
		this.repaint();
	}
}
