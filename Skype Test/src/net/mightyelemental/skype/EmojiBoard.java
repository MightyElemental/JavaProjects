package net.mightyelemental.skype;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import com.skype.Skype;
import com.skype.SkypeException;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings( "serial" )
public class EmojiBoard extends JFrame {

	private JPanel		contentPane;
	private JTextField	textField;

	/** Launch the application. */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					EmojiBoard frame = new EmojiBoard();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/** Finds the amount of hours overlapping in a 24 hour period. */
	public static int findOverlappingInterval(int startTime1, int endTime1, int startTime2, int endTime2) {
		int overlappingTime = 0;

		// First time
		int time1Length = 0;
		if (endTime1 < startTime1) {
			time1Length = 24 - startTime1;
			time1Length += endTime1;
		}
		int[] time1;
		if (time1Length == 0) {
			time1 = new int[Math.abs(endTime1 - startTime1)];
			for (int i1 = startTime1; i1 < endTime1; i1++) {
				time1[i1 - startTime1] = i1;
			}
		} else {
			time1 = new int[time1Length];
			int count = 0;
			for (int i1 = 0; i1 < endTime1; i1++) {
				time1[count] = i1;
				count++;
			}
			for (int i1 = startTime1; i1 < 24; i1++) {
				time1[count] = i1;
				count++;
			}
		}

		// Second time
		int time2Length = 0;
		if (endTime2 < startTime2) {
			time2Length = 24 - startTime2;
			time2Length += endTime2;
		}
		int[] time2;
		if (time2Length == 0) {
			time2 = new int[Math.abs(endTime2 - startTime2)];
			for (int i2 = startTime2; i2 < endTime2; i2++) {
				time2[i2 - startTime2] = i2;
			}
		} else {
			time2 = new int[time2Length];
			int count = 0;
			for (int i2 = 0; i2 < endTime2; i2++) {
				time2[count] = i2;
				count++;
			}
			for (int i2 = startTime2; i2 < 24; i2++) {
				time2[count] = i2;
				count++;
			}
		}

		// Overlap calculator
		for (int i = 0; i < time1.length; i++) {
			for (int j = 0; j < time2.length; j++) {
				if (time1[i] == time2[j]) {
					overlappingTime++;
				}
			}
		}
		return overlappingTime;
	}

	/** Create the frame. */
	public EmojiBoard() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 615, 514);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 42, 579, 421);
		contentPane.add(tabbedPane);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Misc", null, panel, null);
		panel.setLayout(new GridLayout(2, 0, 0, 0));

		JButton btnTableFlip = new JButton("Table Flip");
		btnTableFlip.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				send("\u0028\u251b\u25c9\u0414\u25c9\u0029\u251b\u5f61\u253b\u2501\u253b");
			}
		});
		panel.add(btnTableFlip);

		JButton btnAngryFace = new JButton("Angry Face");
		btnAngryFace.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				send("\u0028\u256c \u0ca0\u76ca\u0ca0\u0029");
			}
		});
		panel.add(btnAngryFace);

		JButton btnFlex = new JButton("Flex");
		btnFlex.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				send("\u1559\u0028\u21c0\u2038\u21bc\u2036\u0029\u1557");
			}
		});
		panel.add(btnFlex);

		JButton btnAngryTroll = new JButton("Angry Troll");
		btnAngryTroll.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				send("\u30fd\u0f3c \u0ca0\u76ca\u0ca0 \u0f3d\uff89");
			}
		});
		panel.add(btnAngryTroll);

		JButton btnShrug = new JButton("Shrug");
		btnShrug.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				send("\u00af\\\u005f\u0028\u30c4\u0029\u005f\u002f\u00af");
			}
		});
		panel.add(btnShrug);

		JButton btnPutTableBack = new JButton("Put Table Back");
		btnPutTableBack.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				send("\u252c\u2500\u252c\ufeff \u30ce\u0028 \u309c\u002d\u309c\u30ce\u0029");
			}
		});
		panel.add(btnPutTableBack);

		textField = new JTextField();
		textField.setBounds(321, 11, 268, 20);
		contentPane.add(textField);
		textField.setColumns(25);

		JLabel lblReciever = new JLabel("Reciever");
		lblReciever.setHorizontalAlignment(SwingConstants.RIGHT);
		lblReciever.setBounds(225, 14, 84, 14);
		contentPane.add(lblReciever);
	}

	public void send(String message) {
		try {
			Skype.chat(textField.getText()).send(message);
		} catch (SkypeException e) {
			e.printStackTrace();
		}
	}
}
