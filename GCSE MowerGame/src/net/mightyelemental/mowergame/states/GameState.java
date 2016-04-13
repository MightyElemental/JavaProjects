package net.mightyelemental.mowergame.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.MowerGame;

public class GameState extends BasicGameState {
	
	
	public final int ID;
	
	public GameState( int ID ) {
		this.ID = ID;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		MowerGame.grassCon.generateGrass(gc, 2);
		renderGrass(gc, sbg);
	}
	
	public Image grass;
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if (grass != null) {
			g.drawImage(grass, 0, 0);
		}
		g.drawString("hi", 50, 50);
	}
	
	public void renderGrass(GameContainer gc, StateBasedGame sbg) throws SlickException {
		grass = new Image(gc.getWidth(), gc.getHeight());
		Graphics g = grass.getGraphics();
		for (int i = 0; i < MowerGame.grassCon.grassList.size(); i++) {
			g.setColor(MowerGame.grassCon.grassList.get(i).color);
			g.fill(MowerGame.grassCon.grassList.get(i));
		}
		g.destroy();
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
	}
	
	@Override
	public int getID() {
		return ID;
	}
	
}
