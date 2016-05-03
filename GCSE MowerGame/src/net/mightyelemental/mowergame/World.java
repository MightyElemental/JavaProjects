package net.mightyelemental.mowergame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.entities.EntityMower;
import net.mightyelemental.mowergame.entities.avoid.EntityAvoid;
import net.mightyelemental.mowergame.grass.Grass;
import net.mightyelemental.mowergame.grass.GrassController;

public class World {

	protected Image grassImg;
	protected Image grassMowImg;

	public GrassController grassCon;

	/** The entity that is the player */
	public EntityMower lawnMower;

	protected List<EntityAvoid> liveEntities = new ArrayList<EntityAvoid>();

	protected int size = 20;

	public World(Random rand) {
		grassCon = new GrassController(rand);
	}

	public void spawnEntity(EntityAvoid e) {
		liveEntities.add(e);
	}

	/**
	 * Used to update the world objects
	 * 
	 * @throws SlickException
	 */
	public void update(GameContainer gc, int delta) throws SlickException {
		for (int i = 0; i < liveEntities.size(); i++) {
			if (liveEntities.get(i) != null) {
				liveEntities.get(i).update(gc, delta);
			}
		}
		lawnMower.update(gc, delta);
	}

	/** Initialise world objects */
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		grassCon.generateGrass(gc, size);
		grassImg = MowerGame.resLoader.loadImage("grass");
		grassMowImg = MowerGame.resLoader.loadImage("grass_mow");
		lawnMower = new EntityMower(50, 50, this);
	}

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		for (int i = 0; i < grassCon.grassList.size(); i++) {
			Grass grass = grassCon.grassList.get(i);
			if (grass.isMowed()) {
				g.drawImage(grassMowImg, grass.getX(), grass.getY(), size, size, 0, 0);
			} else {
				g.drawImage(grassImg, grass.getX(), grass.getY(), size, size, 0, 0);
			}
		}
		for (EntityAvoid ea : liveEntities) {
			g.drawImage(ea.getIcon().getScaledCopy((int) ea.getWidth(), (int) ea.getHeight()), ea.getX(), ea.getY());
		}
		g.drawImage(lawnMower.getIcon().getScaledCopy((int) lawnMower.getWidth(), (int) lawnMower.getHeight()),
				lawnMower.getX(), lawnMower.getY());

	}

}
