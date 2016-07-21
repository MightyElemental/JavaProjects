package net.mightyelemental.mowergame.states;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.MathHelper;
import net.mightyelemental.mowergame.MowerGame;
import net.mightyelemental.mowergame.World;

public class GameState extends BasicGameState {
	
	
	public final int ID;
	
	public World worldObj;
	
	public float timeTotalMs = 1000 * 120;
	
	public float timeMs = timeTotalMs;
	
	public boolean running = true;
	public boolean paused = false;
	
	public EndGameOverlay ego;
	
	public Random rand;
	
	public GameState( int ID, Random rand ) {
		this.ID = ID;
		this.rand = rand;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		timeTotalMs = 1000 * 120;
		timeMs = timeTotalMs;
		worldObj = null;
		returnToMenu = false;
		running = true;
		ego = null;
		worldObj = new World(rand, false);
		worldObj.init(gc, sbg);
	}
	
	Color barColor = new Color(200, 0, 0, 0.3f);
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		worldObj.draw(gc, sbg, g);
		int health = (int) (worldObj.lawnMower.health / worldObj.lawnMower.maxHealth * 100);
		renderProgBar(g, 20, 20, 160, 100, health, "Health " + worldObj.lawnMower.health + "%");
		renderTimeBar(gc, sbg, g);
		
		// Grass
		renderProgBar(g, 20, 80, 300, 100, (100 - worldObj.grassCon.getPercentageMowed()),
			"Mowed " + MathHelper.round(worldObj.grassCon.getPercentageMowed(), 1) + "%");
		
		// Game Over Overlay
		if (ego != null) {
			ego.render(gc, sbg, g);
		}
		if (paused) {
			g.setColor(pauseColor);
			g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
			g.setColor(Color.black);
			g.drawString("Paused", gc.getWidth() / 2 - g.getFont().getWidth("Paused") / 2, gc.getHeight() / 2);
		}
	}
	
	Color pauseColor = new Color(0.5f, 0.5f, 0.5f, 0.8f);
	
	public void renderProgBar(Graphics g, float x, float y, float width, float max, float val, String text) {
		g.setColor(barColor.darker());
		g.fillRoundRect(x, y, width, 20, 5);
		if (val >= 0) {
			g.setColor(barColor);
			g.fillRoundRect(x, y, width / max * val, 20, 5);
		}
		g.setColor(Color.black.brighter());
		g.drawString(text, x + 2, y + 1);
	}
	
	public void renderTimeBar(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// Time
		float temp = MathHelper.round(timeMs / 1000f, 1);
		if (temp < 0) {
			temp = 0;
		} // game can be finished in 90 seconds
		
		renderProgBar(g, 20, 50, 300, timeTotalMs, timeMs, "Time " + temp + "s");
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (running) {
			if (!paused) {
				worldObj.update(gc, delta);
				timeMs -= delta;
			}
		} else {
			if (ego == null) {
				ego = new EndGameOverlay(this);
			}
			ego.update(delta);
			if (returnToMenu) {
				sbg.enterState(MowerGame.STATE_MENU);
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
	
	boolean returnToMenu;
	
	@Override
	public void keyPressed(int key, char c) {
		if (ego != null) {
			if (ego.speedUpLevel < 2) {
				ego.speedUpLevel++;
			}
			
			if ((key == 57 || key == 1) && ego.canSkip) {
				returnToMenu = true;
			}
		} else if (key == 1) {
			paused = !paused;
		}
		
	}
	
}
