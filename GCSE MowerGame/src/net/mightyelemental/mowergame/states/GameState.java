package net.mightyelemental.mowergame.states;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.MowerGame;
import net.mightyelemental.mowergame.grass.Grass;
import net.mightyelemental.mowergame.grass.GrassController;

public class GameState extends BasicGameState {
	
	
	protected Image grassImg;
	protected Image grassMowImg;
	
	public GrassController grassCon;
	
	public final int ID;
	
	public GameState( int ID, Random rand ) {
		this.ID = ID;
		grassCon = new GrassController(rand);
	}
	
	int size = 20;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		grassCon.generateGrass(gc, size);
		grassImg = MowerGame.resLoader.loadImage("grass");
		grassMowImg = MowerGame.resLoader.loadImage("grass_mow");
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		for (int i = 0; i < grassCon.grassList.size(); i++) {
			Grass grass = grassCon.grassList.get(i);
			if (grass.isMowed()) {
				g.drawImage(grassMowImg, grass.getX(), grass.getY(), size, size, 0, 0);
			} else {
				g.drawImage(grassImg, grass.getX(), grass.getY(), size, size, 0, 0);
			}
		}
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		int x = gc.getInput().getMouseX();
		int y = gc.getInput().getMouseY();
		grassCon.setMowed(x, y);
	}
	
	@Override
	public int getID() {
		return ID;
	}
	
}
