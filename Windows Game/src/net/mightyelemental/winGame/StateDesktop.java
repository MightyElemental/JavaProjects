package net.mightyelemental.winGame;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StateDesktop extends BasicGameState {
	
	
	public StateDesktop() {
	}
	
	private Image background;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		background = ResourceLoader.loadImage("desktop.background-bliss");
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		background.draw();
		g.setColor(Color.white);
		g.drawString("<Some GUI goes here>", gc.getWidth() / 2 - (g.getFont().getWidth("<Some GUI goes here>") / 2), gc.getHeight() / 2);
	}
	
	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		
	}
	
	@Override
	public int getID() {
		return WindowsMain.STATE_DESKTOP;
	}
	
}
