package net.mightyelemental.mowergame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
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
import net.mightyelemental.mowergame.particles.Particle;
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
	public List<Particle> particles = new ArrayList<Particle>();
	
	protected int size = 20;
	
	public boolean mowerHasAI;
	
	public World( Random rand, boolean mowerHasAI ) {
		grassCon = new GrassController(rand);
		this.rand = rand;
		this.mowerHasAI = mowerHasAI;
	}
	
	public void spawnEntity(EntityLiving e) {
		liveEntities.add(e);
	}
	
	public void createParticle(Particle p) {
		particles.add(p);
	}
	
	/** Used to update the world objects
	 * 
	 * @throws SlickException */
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
		for (Particle p : particles) {
			p.update(gc, delta);
		}
		
		if (grassCon.getPercentageMowed() == 100) {
			MowerGame.gameState.running = false;
		}
	}
	
	public void updateBlood(GameContainer gc, int delta) throws SlickException {
		for (int i = 0; i < particles.size(); i++) {
			if (particles.get(i) instanceof ParticleBloodSplat) {
				ParticleBloodSplat pbs = (ParticleBloodSplat) particles.get(i);
				for (int j = 0; j < pbs.splatParts.size(); j++) {
					if (pbs.splatColors.get(j).a < 0f) {
						pbs.splatParts.remove(j);// REMOVES TOO EARLY
						pbs.splatColors.remove(j);
					}
				}
				if (pbs.splatParts.isEmpty()) {
					particles.remove(i);
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
	
	Image tileImage = MowerGame.resLoader.loadImage("path").getScaledCopy(size, size);
	
	/** Initialise world objects */
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		grassCon.generateGrass(gc, size);
		grassImg = MowerGame.resLoader.loadImage("grass");
		grassMowImg = MowerGame.resLoader.loadImage("grass_mow");
		lawnMower = new EntityMower(200, 200, this, mowerHasAI);
		generateEntities();
	}
	
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		for (int x = 0; x < gc.getWidth(); x += size) {
			for (int y = 0; y < gc.getHeight(); y += size) {
				tileImage.draw(x, y);
			}
		}
		for (int i = 0; i < grassCon.grassList.size(); i++) {
			Grass grass = grassCon.grassList.get(i);
			if (grass.isMowed()) {
				g.drawImage(grassMowImg, grass.getX(), grass.getY(), size, size, 0, 0);
			} else {
				g.drawImage(grassImg, grass.getX(), grass.getY(), size, size, 0, 0);
			}
		}
		// System.out.println("bush did 9/11"); // KEEAN'S CODE
		for (Particle p : particles) {
			if (p == null) {
				continue;
			}
			p.draw(gc, sbg, g);
			// g.drawImage(ebs.getIcon(), ebs.getX(), ebs.getY());
		}
		g.setColor(Color.blue);
		for (EntityLiving ea : liveEntities) {
			if (ea == null) {
				continue;
			}
			
			// g.fillRect(ea.getX(), ea.getY(), ea.getWidth(), ea.getHeight());
			g.drawImage(ea.getIcon(), ea.getX(), ea.getY());
			
			// if (ea.getPath() != null) {
			// g.setColor(Color.black);
			// g.drawLine(ea.getPath().getX(), ea.getPath().getY(),
			// ea.getCenterX(), ea.getCenterY());
			// }
			
		}
		g.drawImage(lawnMower.getIcon(), lawnMower.getX(), lawnMower.getY());
		// g.fill(lawnMower.bladeArea);
	}
	
	public EntityLiving getCollidingEntity(Shape ent) {
		for (Entity ea : liveEntities) {
			if (ent.equals(ea)) {
				continue;
			}
			if (ent.intersects(ea)) { return (EntityLiving) ea; }
		}
		return null;
	}
	
	public void generateEntities() {
		int randAmount = rand.nextInt(2) + 1;
		for (int i = 0; i < randAmount; i++) {
			this.spawnEntity(new EntityGnome(rand.nextInt(1280), rand.nextInt(720), this));
		}
		randAmount = rand.nextInt(2) + 1;
		for (int i = 0; i < randAmount; i++) {
			this.spawnEntity(new EntityDog(rand.nextInt(1280) + 800, rand.nextInt(720), this));
		}
		randAmount = rand.nextInt(5) + 5;
		for (int i = 0; i < randAmount; i++) {
			this.spawnEntity(new EntityCat(rand.nextInt(1280) + 800, rand.nextInt(720), this));
		}
	}
	
}
