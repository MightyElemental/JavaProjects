package net.mightyelemental.guiComponents;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

@SuppressWarnings( "serial" )
public abstract class GUIComponent extends Rectangle {
	
	
	public Color color = Color.white;
	private String UID;
	
	public boolean transparent;
	
	public GUIComponent setColor(Color c) {
		color = c;
		return this;
	}
	
	public GUIComponent setTransparent(boolean t) {
		this.transparent = t;
		return this;
	}
	
	public GUIComponent( float x, float y, float width, float height, String uid ) {
		super(x, y, width, height);
		UID = uid;
	}
	
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) {
		if (!transparent) {
			g.setColor(color);
			g.fillRect(x, y, width, height);
		}
	}
	
	public void onMousePressed(int button, int x, int y) {
		
	}
	
	public void onMouseReleased(int button, int x, int y) {
		
	}
	
	public String getUID() {
		return UID;
	}
	
	public void onKeyPressed(int key, char c) {
		
	}
	
}
