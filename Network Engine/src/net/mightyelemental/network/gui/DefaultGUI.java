package net.mightyelemental.network.gui;

import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import net.mightyelemental.network.Server;

public class DefaultGUI extends ServerGUI {
	
	
	private static final long serialVersionUID = -1494002277638312296L;
	
	private JPanel contentPane;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	
	public DefaultGUI( String title, Server server, String IPAddress ) {
		super(title, server, IPAddress);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 177, 233, 171);
		contentPane.add(panel);
		panel.setLayout(null);
		
		clientList.setBounds(10, 24, 213, 137);
		panel.add(clientList);
		
		lblClients = new JLabel("Clients");
		lblClients.setHorizontalAlignment(SwingConstants.CENTER);
		lblClients.setBounds(0, 4, 233, 14);
		panel.add(lblClients);
		
		panel_1 = new JPanel();
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
		
		panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBounds(10, 11, 449, 155);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		commands.setBounds(10, 23, 429, this.getHeight() - (387 - 121));
		panel_2.add(commands);
		
		JLabel lblClientCommands = new JLabel("Console");
		lblClientCommands.setHorizontalAlignment(SwingConstants.CENTER);
		lblClientCommands.setBounds(10, 2, 429, 14);
		panel_2.add(lblClientCommands);
	}
	
	private void repaintFrame() {
		panel.validate();
		panel.repaint();
		panel_1.validate();
		panel_1.repaint();
		panel_2.validate();
		panel_2.repaint();
	}
	
	public void paint(Graphics g) {
		try {
			commands.setBounds(10, 23, 429, this.getHeight() - (387 - 121));
			panel_2.setBounds(10, 11, 449, this.getHeight() - (387 - 155));
			panel_1.setBounds(253, 177 + (this.getHeight() - 387), 206, 171);
			panel.setBounds(10, 177 + (this.getHeight() - 387), 233, 171);
		} catch (Exception e) {
		}
	}
	
}
