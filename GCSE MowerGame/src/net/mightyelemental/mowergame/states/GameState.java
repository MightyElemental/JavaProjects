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

	public GameState(int ID, Random rand) {
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

		renderBars(gc, sbg, g);

		// Grass
		g.setColor(Color.red.darker());
		g.fillRoundRect(20, 80, 130, 20, 5);
		if (worldObj.lawnMower.health >= 0) {
			g.setColor(Color.red);
			g.fillRoundRect(20, 80, (130f * (100 - worldObj.grassCon.getPercentageMowed()) / 100f), 20, 5);
		}
		g.setColor(Color.black);
		g.drawString("Mowed " + MathHelper.round(worldObj.grassCon.getPercentageMowed(), 1) + "%", 20, 81);

		// Game Over Overlay
		if (!running) {
			g.setColor(blackOverlay);
			g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
			if (blackOverlay.a >= 0.8f) {
				g.setColor(endTextColor);
				int wid = g.getFont().getWidth("---GAME OVER---");
				g.drawString("---GAME OVER---", gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2);
				String text = "You mowed " + MathHelper.round(worldObj.grassCon.getPercentageMowed(), 1)
						+ "% of the lawn";
				wid = g.getFont().getWidth(text);
				g.drawString(text, gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2 + 30);

			}
		}
	}

	public void renderBars(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		renderHealthBar(gc, sbg, g);
		renderTimeBar(gc, sbg, g);
	}

	public void renderHealthBar(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// Health
		g.setColor(Color.red.darker());
		g.fillRoundRect(20, 20, 130, 20, 5);
		if (worldObj.lawnMower.health >= 0) {
			g.setColor(Color.red);
			g.fillRoundRect(20, 20, 130f / 100f * worldObj.lawnMower.health, 20, 5);
		}
		g.setColor(Color.black);
		g.drawString("Health", 22, 21);
		if (worldObj.lawnMower.health > 0) {
			g.drawString(worldObj.lawnMower.health + "%", 110, 21);
		}
	}

	public void renderTimeBar(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// Time
		float temp = MathHelper.round(timeMs / 1000f, 1);
		if (temp < 0) {
			temp = 0;
		}
		String str = "Time " + temp + "s";// game can be finished in 140
											// seconds

		g.setColor(Color.red.darker());
		g.fillRoundRect(20, 50, 130, 20, 5);
		if (worldObj.lawnMower.health >= 0) {
			g.setColor(Color.red);
			g.fillRoundRect(20, 50, (130f / timeTotalMs) * timeMs, 20, 5);
		}
		g.setColor(Color.black);
		g.drawString(str, 22, 51);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (running) {
			worldObj.update(gc, delta);
			timeMs -= delta;
		} else {
			if (blackOverlay.a <= 0.8f) {
				blackOverlay.a += (1f / 17f / 6f) * (delta / 17f);
			} else if (endTextColor.a < 1f) {
				endTextColor.a += (1f / 17f / 2f) * (delta / 17f);
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
