package net.minegeek360.animalguesser;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class MainClass extends JFrame{
	
	private static MainClass m = new MainClass();
	private static Intelligence I = new Intelligence();
	private static Animals A = new Animals();
	public final String version = "0.2.5";
	public static JFrame frame = new JFrame();
	
	public static String eol = System.getProperty("line.separator");
	
	public static JTextArea jta = new JTextArea();
	
	
	public static Thread animals = new Thread(A);
	public static Thread logic = new Thread(I);
	
	//images
	public BufferedImage progIcon;
	
	
	
	public static void main(String[] args){
		m.loadImages();
		m.setUpFrame();
		m.centreWindow(frame);
		JOptionPane.showMessageDialog(null, "Welcome to Animal Guesser!!"+eol+"Hope you enjoy it!", "Animal Guesser", JOptionPane.INFORMATION_MESSAGE);
		animals.start();
		JOptionPane.showMessageDialog(null, "First pick an animal from the list...", "Animal Guesser", JOptionPane.INFORMATION_MESSAGE);
		logic.start();
	}
	
	/**Sets up the frame as its name suggests*/
	private void setUpFrame(){
		frame.setTitle("Animal Guesser "+version);
		frame.setVisible(true);
		frame.setSize(500, 700);
		frame.setFocusable(true);
		frame.setResizable(true);
		frame.setIconImage(progIcon);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jta.setEditable(false);
		frame.add(jta);
	}
	
	public void centreWindow(Window frame) {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x1 = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
	    int y1 = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
	    frame.setLocation(x1, y1);
	}
	
	/**Loads any images only icon is used right now*/
	public void loadImages(){
		try{//program Icon
			 progIcon = ImageIO.read(this.getClass().getClassLoader().getResource("net/minegeek360/sprites/pig.png"));
		}catch (Exception e){ System.err.println("Error (Main)"); }
	}
	
}
