package net.mightyelemental.winGame.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.WindowsMain;

public class StateLoading extends BasicGameState {
	
	
	private int ID = 0;
	
	public StateLoading( int ID ) {
		this.ID = ID;
	}
	
	private Image loadingScreen;
	
	private float xPos, counts;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		loadingScreen = WindowsMain.resLoader.loadImage("loading.loadScreen");
		float scale = (WindowsMain.WIDTH / 16.0f * 9.0f) / loadingScreen.getHeight();
		loadingScreen = loadingScreen.getScaledCopy(scale);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		loadingScreen.draw(gc.getWidth() / 2 - loadingScreen.getWidth() / 2, 0);
		renderLoadingBar(gc, sbg, g);
		g.setColor(Color.black);
		g.fillRect(731, 528, 40, 20);
		g.fillRect(463, 528, 80, 20);
	}
	
	public void renderLoadingBar(GameContainer gc, StateBasedGame sbg, Graphics g) {
		g.setColor(Color.blue);
		int startX = 500;
		g.fillRoundRect(startX + xPos, 530, 10, 15, 2);
		g.fillRoundRect(startX - 14 + xPos, 530, 10, 15, 2);
		g.fillRoundRect(startX - 28 + xPos, 530, 10, 15, 2);
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		xPos += 1 * delta / 8f;
		xPos = xPos > 260 ? 0 : xPos;
		counts = xPos == 0 ? counts + 1 : counts;
		if (counts >= 3) {
			sbg.enterState(WindowsMain.STATE_LOGIN);
		}
	}
	
	@Override
	public int getID() {
		return ID;
	}
	
}
