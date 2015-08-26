package net.minegeek360.fvt;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FakeVTroll implements ActionListener, Runnable{
	
	public static JFrame frame = new JFrame("An Awesomely Random Game");
	
	public static JButton play = new JButton("Play");
	
	public static boolean started;
	
	Thread thread = new Thread(this);
	
	public static Panel pan = new Panel();
	
	public FakeVTroll(){
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		frame.add(pan);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosed(WindowEvent arg0) {}
			public void windowClosing(WindowEvent e) {
				if(started){
					JOptionPane.showMessageDialog(frame, "You can't close me");
				}else{
					System.exit(0);
				}
			}

			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
		});
		//frame.add(play, BorderLayout.CENTER);
		frame.revalidate();
		frame.repaint();
		started = true;
		play.setPreferredSize(new Dimension(100, 70));
		play.addActionListener(this);
	}
	
	public static final int WIDTH = 700;
	public static final int HEIGHT = 500;

	public static void main(String[] args) {
		FakeVTroll fvt = new FakeVTroll();
		fvt.thread.start();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.remove(play);
	}

	@Override
	public void run() {
		try {
			pan.update();
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true){
			if(started){
				try{
					Thread.sleep(30);
					pan.update();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			System.out.print("");
		}
	}

}
