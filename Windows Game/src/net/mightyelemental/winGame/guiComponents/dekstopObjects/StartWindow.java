package net.mightyelemental.winGame.guiComponents.dekstopObjects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.state.StateBasedGame;

public class StartWindow extends RoundedRectangle {
	
	
	private static final long serialVersionUID = 6373909456505514103L;
	
	public StartWindow() {
		super(-5, 220, 430, 458, 15);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
	}
	
}
