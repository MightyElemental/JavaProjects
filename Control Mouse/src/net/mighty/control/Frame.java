package net.mighty.control;

import java.awt.Graphics;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Frame extends JFrame {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6473700170656764028L;
	private JPanel				contentPane;
	private JPasswordField		passwordField;
	private JPasswordField		repeatPasswordField;
	private JTextField			portField;

	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

	JPanel panel = new JPanel();

	public JLabel	fpsLabel	= new JLabel("10");
	public JSlider	slider		= new JSlider();
	public List		entiresList	= new List();

	int frames = 0;

	public Rectangle r = new Rectangle(0, 0, 1920, 1080);

	@SuppressWarnings( "serial" )
	JPanel remoteView = new JPanel() {

		public void paint(Graphics g) {
			// g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
			g.drawImage(Control.capture, r.x, r.y, r.width, r.height, null);
			tabbedPane.setBounds(10, 11, contentPane.getWidth() - 20, contentPane.getHeight() - 20);
		}
	};

	JRadioButton	hostCheckButton			= new JRadioButton("Controlling");
	JRadioButton	clientCheckButton		= new JRadioButton("Controlled");
	JButton			connectButton			= new JButton("Connect");
	JTextField		ipTextPane				= new JTextField();
	JCheckBox		chckbxBroadcastScreen	= new JCheckBox("Broadcast Screen");

	/** Create the frame. */
	public Frame() {
		setTitle("Mouse Helper");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 648, 455);
		setVisible(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		contentPane.setLayout(null);

		tabbedPane.setBounds(10, 11, 612, 395);
		contentPane.add(tabbedPane);

		tabbedPane.addTab("Connection", null, panel, null);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("You are...");
		lblNewLabel.setBounds(9, 9, 70, 14);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblNewLabel);
		hostCheckButton.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				if (hostCheckButton.isSelected()) {
					ipTextPane.setEnabled(false);
					ipTextPane.setText("");
					slider.setEnabled(false);
					chckbxBroadcastScreen.setEnabled(false);
				}
			}
		});

		hostCheckButton.setBounds(9, 29, 100, 23);
		panel.add(hostCheckButton);
		hostCheckButton.setMnemonic(KeyEvent.VK_H);
		hostCheckButton.setActionCommand("host");
		group.add(hostCheckButton);
		clientCheckButton.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				if (clientCheckButton.isSelected()) {
					ipTextPane.setEnabled(true);
					slider.setEnabled(true);
					chckbxBroadcastScreen.setEnabled(true);
				}
			}
		});

		clientCheckButton.setBounds(9, 58, 100, 23);
		panel.add(clientCheckButton);
		clientCheckButton.setMnemonic(KeyEvent.VK_C);
		clientCheckButton.setActionCommand("client");
		group.add(clientCheckButton);

		connectButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (clientCheckButton.isSelected()) {
					String ip = ipTextPane.getText().replaceAll("[^0-9.]", "");
					if (ipTextPane.getText().replaceAll("[^0-9.]", "").length() <= 0) { return; }
					if (portField.getText().replaceAll("[^0-9]", "").length() <= 0) { return; }
					int port = Integer.parseInt(portField.getText().replaceAll("[^0-9]", ""));
					Control.in.createClient(ip, port);
				} else if (hostCheckButton.isSelected()) {
					if (portField.getText().replaceAll("[^0-9]", "").length() <= 0) { return; }
					int port = Integer.parseInt(portField.getText().replaceAll("[^0-9]", ""));
					Control.in.createHost(port);
				}
				connectButton.setText("Disconnect");
			}
		});
		connectButton.setBounds(9, 299, 587, 23);
		panel.add(connectButton);

		JPanel connectionBoxes = new JPanel();
		connectionBoxes.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		connectionBoxes.setBounds(338, 9, 258, 108);
		panel.add(connectionBoxes);
		connectionBoxes.setLayout(null);

		JLabel lblIpAddress = new JLabel("IP Address");
		lblIpAddress.setBounds(32, 4, 116, 14);
		connectionBoxes.add(lblIpAddress);
		lblIpAddress.setHorizontalAlignment(SwingConstants.RIGHT);
		ipTextPane.setText("127.0.0.1");

		ipTextPane.setBounds(158, 0, 100, 20);
		connectionBoxes.add(ipTextPane);

		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(32, 32, 116, 14);
		connectionBoxes.add(lblPort);
		lblPort.setHorizontalAlignment(SwingConstants.RIGHT);

		portField = new JTextField();
		portField.setText("4040");
		portField.setBounds(158, 29, 100, 20);
		connectionBoxes.add(portField);
		portField.setColumns(6);

		JLabel lblConnectionPassword = new JLabel("Connection Password");
		lblConnectionPassword.setBounds(10, 62, 138, 14);
		connectionBoxes.add(lblConnectionPassword);
		lblConnectionPassword.setHorizontalAlignment(SwingConstants.RIGHT);

		passwordField = new JPasswordField();
		passwordField.setBounds(158, 59, 100, 20);
		connectionBoxes.add(passwordField);

		JLabel lblRepeatPassword = new JLabel("Repeat Password");
		lblRepeatPassword.setBounds(32, 91, 116, 14);
		connectionBoxes.add(lblRepeatPassword);
		lblRepeatPassword.setHorizontalAlignment(SwingConstants.RIGHT);

		repeatPasswordField = new JPasswordField();
		repeatPasswordField.setBounds(158, 88, 100, 20);
		connectionBoxes.add(repeatPasswordField);

		JPanel frameControlls = new JPanel();
		frameControlls.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frameControlls.setBounds(338, 128, 258, 160);
		panel.add(frameControlls);
		frameControlls.setLayout(null);

		chckbxBroadcastScreen.setBounds(6, 7, 175, 23);
		frameControlls.add(chckbxBroadcastScreen);

		fpsLabel.setBounds(191, 58, 46, 31);
		frameControlls.add(fpsLabel);
		slider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent arg0) {
				fpsLabel.setText(slider.getValue() + "");
				Control.fps = slider.getValue();
			}
		});
		slider.setValue(10);
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(1);
		slider.setMajorTickSpacing(1);
		slider.setMinimum(1);
		slider.setMaximum(10);
		slider.setBounds(6, 58, 175, 31);
		frameControlls.add(slider);

		JLabel lblFramesPerSecond = new JLabel("Frames Per Second");
		lblFramesPerSecond.setBounds(6, 37, 175, 14);
		frameControlls.add(lblFramesPerSecond);

		entiresList.setBounds(9, 87, 323, 206);
		panel.add(entiresList);

		tabbedPane.addTab("View", null, remoteView, null);
		remoteView.setLayout(null);
	}
}
