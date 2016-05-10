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
	
	public GameState( int ID, Random rand ) {
		this.ID = ID;
		worldObj = new World(rand);
		this.rand = rand;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		worldObj.init(gc, sbg);
	}
	
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
		if (ego != null) {
			ego.render(gc, sbg, g);
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
		g.setColor(Color.black.brighter());
		g.drawString("Health " + worldObj.lawnMower.health + "%", 22, 21);
	}
	
	public void renderTimeBar(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// Time
		float temp = MathHelper.round(timeMs / 1000f, 1);
		if (temp < 0) {
			temp = 0;
		}
		String str = "Time " + temp + "s";// game can be finished in 90
											// seconds
		
		g.setColor(Color.red.darker());
		g.fillRoundRect(20, 50, 130, 20, 5);
		if (worldObj.lawnMower.health >= 0) {
			g.setColor(Color.red);
			g.fillRoundRect(20, 50, (130f / timeTotalMs) * timeMs, 20, 5);
		}
		g.setColor(Color.black.brighter());
		g.drawString(str, 22, 51);
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
