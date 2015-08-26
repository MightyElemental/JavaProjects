package net.minegeek360.watcher;

import java.awt.GridLayout;

import javax.swing.JFrame;

public class Watcher implements Runnable{
	
	public Thread thread = new Thread(this);
	
	public static JFrame frame = new JFrame("Steam Market Watcher");
	
	public WatchPane[] panes = new WatchPane[3];
	
	String name = "http://steamcommunity.com/market/listings/730/AK-47%20%7C%20Redline%20%28Field-Tested%29";
	
	public Watcher(){
		frame.setVisible(true);
		frame.setSize(650, 300);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new GridLayout(1,3));
		for(int i = 0; i < panes.length; i++){
			panes[i] = new WatchPane();
			frame.add(panes[i]);
		}
		thread.start();
		name += "/render/.json?start=0&count=50&currency=3&language=english&format=json";
	}
	
	public int sleepTime = 8;
	
	public void run(){
		while(true){
			frame.repaint();
			frame.revalidate();
			for(int i = 0; i < panes.length; i++){
				if(panes[i] != null){
					panes[i].update();
					panes[i].repaint();
				}
			}
			for(int i = 0; i < sleepTime+1; i++){
				try {
					Thread.sleep(1*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				frame.setTitle("Steam Market Watcher - "+(sleepTime-i));
			}
		}
	}
	
	
	
	public static void main(String[] args){
		new Watcher();
	}
	
}
