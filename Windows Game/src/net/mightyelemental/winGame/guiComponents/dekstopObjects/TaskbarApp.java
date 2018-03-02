package net.mightyelemental.winGame.guiComponents.dekstopObjects;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.guiComponents.GUIButton;

public class TaskbarApp extends GUIButton {
	
	
	private static final long serialVersionUID = 8086670225644581843L;
	
	public AppWindow linkedWindow;
	
	public TaskbarApp( float x, String uid, AppWindow linkedWindow ) {
		super(x, 720 - 43, 86, 43, uid + "_taskbarapp");
		this.linkedWindow = linkedWindow;
		this.linkedWindow.setLinkedTaskbarApp(this);
	}
	
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(new Color(30, 79, 178));
		g.fillRoundRect(x, y, width, height, 5);
		g.setColor(new Color(30, 79, 178).darker());
		g.drawRoundRect(x, y, width, height, 5);
		g.setColor(Color.white);
		g.drawString(linkedWindow.title, x+2, y+10);
	}
	
}
