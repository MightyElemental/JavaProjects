package net.mightyelemental.mowergame.states;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.MowerGame;

public class LoadState extends BasicGameState {
	
	
	private final int ID;
	
	private Random rand = new Random();
	
	/* End */
	
	private Image companyLogo;
	
	private float alpha;
	
	public LoadState( int statePreLoad ) {
		this.ID = statePreLoad;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		companyLogo = MowerGame.resLoader.loadImage("IridiumGames");
	}
	
	private int ticks;
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		Image temp = companyLogo.getScaledCopy(gc.getWidth() / 1280f);
		float width = temp.getWidth();
		float height = temp.getHeight();
		if (alpha < 1f) {
			g.drawImage(temp, (gc.getWidth() / 2) - (width / 2), (gc.getHeight() / 2) - (height / 2), new Color(255, 255, 255, alpha));
		} else {
			renderImageWithGlitches(gc, sbg, g, temp, width, height);
		}
	}
	
	private float xShift = (rand.nextInt(60) - 30), yShift = (rand.nextInt(60) - 30), alphaShift = rand.nextInt(10) / 10f;
	
	public void renderImageWithGlitches(GameContainer gc, StateBasedGame sbg, Graphics g, Image temp, float width, float height) {
		float x = (gc.getWidth() / 2) - (width / 2);
		float y = (gc.getHeight() / 2) - (height / 2);
		if (ticks > 120 && ticks < 130) {
			g.drawImage(temp, x - xShift - (rand.nextInt(2) - 1), y - yShift - (rand.nextInt(2) - 1),
				new Color(255, 255, 255, alphaShift - (rand.nextInt(3) / 10f)));
		} else {
			g.drawImage(temp, x, y, new Color(255, 255, 255, alpha));
		}
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		if (gc.getInput().isKeyDown(Input.KEY_SPACE)) {
			ticks += 500;
		}
		
		ticks += delta / 17f;
		if (ticks > 150 && MowerGame.fullLoaded) {
			alpha -= 0.01f;
			if (alpha < -0.2f) {
				sbg.enterState(MowerGame.STATE_GAME);
			}
		} else {
			alpha += 0.01f;
		}
	}
	
	@Override
	public int getID() {
		return ID;
	}
	
}
