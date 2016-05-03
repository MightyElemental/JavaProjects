package net.mightyelemental.mowergame.states;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.World;

public class GameState extends BasicGameState {

	public final int ID;

	public World worldObj;

	public GameState(int ID, Random rand) {
		this.ID = ID;
		worldObj = new World(rand);
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		worldObj.init(gc, sbg);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		worldObj.draw(gc, sbg, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		worldObj.update(gc, delta);
	}

	@Override
	public int getID() {
		return ID;
	}

}
