package net.mightyelemental.winGame.guiComponents.dekstopObjects;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.state.StateBasedGame;

public class AppWindow extends RoundedRectangle {
	
	
	public AppWindow( float x, float y, float width, float height ) {
		super(x, y, width, height, 15);
	}
	
	private static final long serialVersionUID = 1L;
	public boolean fullscreen;
	
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) {
		g.setColor(Color.gray);
		g.fill(this);
		g.setColor(new Color(30, 79, 178));
		g.fillRoundRect(x, y, width, 25,15);
	}
	
}
