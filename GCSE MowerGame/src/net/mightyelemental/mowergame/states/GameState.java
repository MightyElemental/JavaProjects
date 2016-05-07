package net.mightyelemental.mowergame.states;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.MathHelper;
import net.mightyelemental.mowergame.World;

public class GameState extends BasicGameState {
	
	
	public final int ID;
	
	public World worldObj;
	
	public float timeMs = 1000 * 120;
	
	public boolean running = true;
	
	public GameState( int ID, Random rand ) {
		this.ID = ID;
		worldObj = new World(rand);
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		worldObj.init(gc, sbg);
	}
	
	public Color blackOverlay = new Color(20, 20, 20, 0);
	public Color endTextColor = new Color(255, 255, 255, 0);
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		worldObj.draw(gc, sbg, g);
		g.setColor(Color.red.darker());
		g.fillRoundRect(20, 20, 120, 20, 5);
		if (worldObj.lawnMower.health >= 0) {
			g.setColor(Color.red);
			g.fillRoundRect(20, 20, 120 / 100 * worldObj.lawnMower.health, 20, 5);
		}
		g.setColor(Color.black);
		g.drawString("Health", 22, 21);
		int temp = (int) MathHelper.round(timeMs / 1000f, 1);
		if (temp < 0) {
			temp = 0;
		}
		String str = "Time Remaining: " + temp;// game can be finished in 140 seconds
		g.drawString(str, 22, 50);
		if (!running) {
			g.setColor(blackOverlay);
			g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
			if (blackOverlay.a >= 0.8f) {
				g.setColor(endTextColor);
				int wid = g.getFont().getWidth("---GAME OVER---");
				g.drawString("---GAME OVER---", gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2);
			}
		}
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (running) {
			worldObj.update(gc, delta);
			timeMs -= delta;
		} else {
			if (blackOverlay.a <= 0.8f) {
				blackOverlay.a += 1f / delta / 6f;
			} else if (endTextColor.a < 1f) {
				endTextColor.a += 1f / delta / 2f;
			}
		}
		if (timeMs <= 0) {
			running = false;
		}
	}
	
	@Override
	public int getID() {
		return ID;
	}
	
}
