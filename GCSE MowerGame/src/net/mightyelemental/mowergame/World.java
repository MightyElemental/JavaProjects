package net.mightyelemental.mowergame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.entities.Entity;
import net.mightyelemental.mowergame.entities.EntityMower;
import net.mightyelemental.mowergame.entities.living.EntityCat;
import net.mightyelemental.mowergame.entities.living.EntityDog;
import net.mightyelemental.mowergame.entities.living.EntityGnome;
import net.mightyelemental.mowergame.entities.living.EntityLiving;
import net.mightyelemental.mowergame.grass.Grass;
import net.mightyelemental.mowergame.grass.GrassController;
import net.mightyelemental.mowergame.particles.ParticleBloodSplat;

public class World {

	protected Image grassImg;
	protected Image grassMowImg;

	public Random rand;

	public GrassController grassCon;

	/** Use to slow or speed up the game */
	public float deltaDividor = 1;

	/** The entity that is the player */
	public EntityMower lawnMower;

	public List<EntityLiving> liveEntities = new ArrayList<EntityLiving>();
	public List<ParticleBloodSplat> bloodSplats = new ArrayList<ParticleBloodSplat>();

	protected int size = 20;

	public boolean mowerHasAI;

	public World(Random rand, boolean mowerHasAI) {
		grassCon = new GrassController(rand);
		this.rand = rand;
		this.mowerHasAI = mowerHasAI;
	}

	public void spawnEntity(Entity e) {
		if (e instanceof EntityLiving) {
			liveEntities.add((EntityLiving) e);
		} else if (e instanceof ParticleBloodSplat) {
			bloodSplats.add((ParticleBloodSplat) e);
		}
	}

	/**
	 * Used to update the world objects
	 * 
	 * @throws SlickException
	 */
	public void update(GameContainer gc, int delta) throws SlickException {
		if (this.mowerHasAI) {
			delta /= deltaDividor;
		}
		updateEntities(gc, delta);
		lawnMower.update(gc, delta);
		updateBlood(gc, delta);
		if (lawnMower.health <= 0) {
			updateEntities(gc, delta);
			MowerGame.gameState.running = false;
		}

		if (grassCon.getPercentageMowed() == 100) {
			MowerGame.gameState.running = false;
		}
	}

	public void updateBlood(GameContainer gc, int delta) throws SlickException {
		for (int i = 0; i < bloodSplats.size(); i++) {
			if (bloodSplats.get(i) != null) {
				bloodSplats.get(i).update(gc, delta);
				for (int j = 0; j < bloodSplats.get(i).splatParts.size(); j++) {
					if (bloodSplats.get(i).splatColors.get(j).a <= 0f) {
						bloodSplats.get(i).splatParts.remove(j);
					}
				}
				if (bloodSplats.get(i).splatParts.isEmpty()) {
					bloodSplats.remove(i);
					break;
				}
			}
		}
	}

	public void updateEntities(GameContainer gc, int delta) throws SlickException {
		for (int i = 0; i < liveEntities.size(); i++) {
			if (liveEntities.get(i) != null) {
				liveEntities.get(i).update(gc, delta);
				if (liveEntities.get(i).dead) {
					liveEntities.remove(i);
					break;

				}
			}
		}
	}

	/** Initialise world objects */
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		grassCon.generateGrass(gc, size);
		grassImg = MowerGame.resLoader.loadImage("grass");
		grassMowImg = MowerGame.resLoader.loadImage("grass_mow");
		lawnMower = new EntityMower(200, 200, this, mowerHasAI);
		generateEntities();
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
		// System.out.println("bush did 9/11"); // KEEAN'S CODE
		for (ParticleBloodSplat ebs : bloodSplats) {
			if (ebs == null) {
				continue;
			}
			for (int i = 0; i < ebs.splatParts.size(); i++) {
				Shape s = ebs.splatParts.get(i);
				g.setColor(ebs.splatColors.get(i));
				g.fill(s);
				//g.drawImage(MowerGame.resLoader.loadImage("entities.trump").getScaledCopy(25, 25), s.getX(), s.getY());
			}
			// g.drawImage(ebs.getIcon(), ebs.getX(), ebs.getY());
		}
		for (EntityLiving ea : liveEntities) {
			if (ea == null) {
				continue;
			}
			g.drawImage(ea.getIcon(), ea.getX(), ea.getY());

			// if (ea.getPath() != null) {
			// g.setColor(Color.black);
			// g.drawLine(ea.getPath().getX(), ea.getPath().getY(),
			// ea.getCenterX(), ea.getCenterY());
			// }

		}
		g.drawImage(lawnMower.getIcon(), lawnMower.getX(), lawnMower.getY());
	}

	public EntityLiving getCollidingEntity(Rectangle ent) {
		for (Entity ea : liveEntities) {
			if (ent.equals(ea)) {
				continue;
			}
			if (ent.intersects(ea)) {
				return (EntityLiving) ea;
			}
		}
		return null;
	}

	public void generateEntities() {
		int randAmount = rand.nextInt(5) + 7;
		for (int i = 0; i < randAmount; i++) {
			int randX = 1280 + rand.nextInt(200);
			int randY = rand.nextInt(720);
			if (rand.nextInt(5) == 0) {
				this.spawnEntity(new EntityDog(randX, randY, this));
			} else {
				this.spawnEntity(new EntityCat(randX, randY, this));
			}
		}
		randAmount = rand.nextInt(2) + 1;
		for (int i = 0; i < randAmount; i++) {
			this.spawnEntity(new EntityGnome(rand.nextInt(1280), rand.nextInt(720), this));
		}
	}

}
