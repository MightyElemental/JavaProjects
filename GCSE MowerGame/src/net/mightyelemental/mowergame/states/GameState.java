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

	public float timeTotalMs = 1000 * 120;

	public float timeMs = timeTotalMs;

	public boolean running = true;

	public EndGameOverlay ego;

	public Random rand;

	public GameState(int ID, Random rand) {
		this.ID = ID;
		this.rand = rand;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		worldObj = new World(rand, false);
		worldObj.init(gc, sbg);
	}

	Color barColor = new Color(255, 0, 0, 0.5f);

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		worldObj.draw(gc, sbg, g);

		renderProgBar(g, 20, 20, 160, 100, worldObj.lawnMower.health, "Health " + worldObj.lawnMower.health + "%");
		renderBars(gc, sbg, g);

		// Grass
		renderProgBar(g, 20, 80, 300, 100, (100 - worldObj.grassCon.getPercentageMowed()),
				"Mowed " + MathHelper.round(worldObj.grassCon.getPercentageMowed(), 1) + "%");

		// Game Over Overlay
		if (ego != null) {
			ego.render(gc, sbg, g);
		}
	}

	public void renderBars(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		renderTimeBar(gc, sbg, g);
	}

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
			worldObj.update(gc, delta);
			timeMs -= delta;
		} else {
			if (ego == null) {
				ego = new EndGameOverlay(this);
			}
			ego.update(delta);
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
