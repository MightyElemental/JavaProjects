package net.mightyelemental.neuralnet;

import java.awt.Dimension;

import javax.swing.JFrame;

import net.mightyelemental.neuralnet.pong.Pong;

public class Main {

	private static JFrame frame;

	public static void main(String[] args) {
		Dimension d = new Dimension(800, 600);
		frame = new JFrame();
		frame.setTitle("pong");
		frame.setSize(d);
		frame.setLocationRelativeTo(null);
		frame.add(new Pong(d));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
