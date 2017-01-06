package net.mightyelemental.winGame.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.WindowsMain;

public class StateLogin extends BasicGameState {
	
	
	private Image loginScreen;
	
	private float pauseTime = 200f;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		loginScreen = WindowsMain.resLoader.loadImage("login.loginScreen");
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		loginScreen.draw();
		if (pauseTime > 0) {
			g.setColor(Color.black);
			g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		}
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (pauseTime > 0) pauseTime -= delta / 8f;
	}
	
	@Override
	public int getID() {
		return WindowsMain.STATE_LOGIN;
	}
	
}
