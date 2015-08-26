package net.minegeek360.fvt;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Panel extends JPanel{
	
	public double progressPercent = 0;
	public double progressPercent2 = 0;
	
	public boolean shouldUseScare = true;
	
	public String[] fakeInstalls = {"Searching for main files...", "Searching for all personal files...", "Deleting personal files...", 
			"Corrupting Programs...", "Searching for network connections to infect...", "Forcing access to Operating System Files...", 
			"Deleting 'C://Windows/System32'", "Done! have a nice day"};
	
	public int fakeInstUse = 7;
	
	public SoundPlayer sp = new SoundPlayer();
	
	public boolean paintState;
	
	public BufferedImage troll;
	
	public Panel(){
		try {
			troll = ImageIO.read(this.getClass().getClassLoader().getResource("net/minegeek360/fvt/troll.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update(){
		repaint();
		System.out.println("Updated "+progressPercent);
		
		if(shouldUseScare){
			if(fakeInstUse == 2 || fakeInstUse == 3){
				progressPercent += 0.0625;
			}else if(fakeInstUse == 4){
				progressPercent += 0.5;
				progressPercent2 += 0.5;
			}else{
				progressPercent += 0.25;
			}
		}else{
			progressPercent += 0.25;
		}
		
		
		if(progressPercent >= 100 || progressPercent2 >= 100){
			progressPercent2 = 100;
			progressPercent = 100;
		}
		
		if((""+progressPercent).endsWith(".25") || (""+progressPercent).endsWith(".75")){
			progressPercent2 += 0.5;
		}
		
		if(progressPercent >= 100){
			fakeInstUse = 7;
		}else if(progressPercent >= 90){
			fakeInstUse = 6;
		}else if(progressPercent >= 80){
			fakeInstUse = 5;
		}else if(progressPercent >= 70){
			fakeInstUse = 4;
		}else if(progressPercent >= 60){
			fakeInstUse = 3;
		}else if(progressPercent >= 40){
			fakeInstUse = 2;
		}else if(progressPercent >= 20){
			fakeInstUse = 1;
		}else if(progressPercent >= 0){
			fakeInstUse = 0;
		}
		
		if(progressPercent >= 100){
			try{
				Thread.sleep(500);
				sp.playSound2("troll_1.wav");
				Thread.sleep(1500);
			}catch(Exception e){}
			paintState = true;
			FakeVTroll.started = false;
			repaint();
		}
	}
	
	public void paint(Graphics g){
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, FakeVTroll.WIDTH, FakeVTroll.HEIGHT);
		if(!paintState){
			paintBar((Graphics2D)g);
		}else{
			g.drawImage(troll, 0, 0, getWidth(), getHeight(), null);
		}
	}
	
	public int calcBarProgress(int barLength){
		return (int) ((barLength/100)*progressPercent);
	}
	
	public int fitTextInBoxWidth(String text, Graphics2D g, int width){
		
		int total = width/2;
		int totaltemp = 0;
		
		for (int c1=0 ; c1 < text.length() ; c1++){
			char ch = text.charAt(c1); 
			totaltemp -= g.getFontMetrics().charWidth(ch);
		}
		
		
		return total + (totaltemp/2);
		
	}
	
	public void paintBar(Graphics2D g){
		g.setColor(Color.RED);
		g.setFont(new Font("", 1, 25));
		g.drawString("Infecting Computer...", fitTextInBoxWidth("Infecting Computer...", g, FakeVTroll.WIDTH), 100);
		g.setColor(Color.GRAY);
		g.fillRoundRect(50, FakeVTroll.HEIGHT/2, FakeVTroll.WIDTH-100, 20, 10, 10);
		g.setColor(Color.RED);
		g.fillRoundRect(50, FakeVTroll.HEIGHT/2, calcBarProgress(FakeVTroll.WIDTH-100), 20, 10, 10);
		g.setFont(new Font("", 1, 15));
		g.setColor(Color.BLACK);
		g.drawString(progressPercent2+"%", fitTextInBoxWidth(progressPercent2+"%", g, FakeVTroll.WIDTH), (FakeVTroll.HEIGHT/2)+15);
		if(shouldUseScare){
			g.setFont(new Font("", 1, 20));
			g.drawString(fakeInstalls[fakeInstUse], fitTextInBoxWidth(fakeInstalls[fakeInstUse], g, FakeVTroll.WIDTH), (FakeVTroll.HEIGHT/2)+90);
		}
	}

}
