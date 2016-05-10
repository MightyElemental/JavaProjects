package net.mightyelemental.mowergame.entities.avoid;

import java.awt.Point;
import java.util.Random;

import org.newdawn.slick.GameContainer;

import net.mightyelemental.mowergame.MathHelper;
import net.mightyelemental.mowergame.World;

public class EntityCat extends EntityAvoid {

	private static final long serialVersionUID = -3701251139422836127L;

	private Random rand;

	public EntityCat(float x, float y, World worldObj, Random rand) {
		super(x, y, 55, 55, worldObj);
		this.rand = rand;
		this.setIcon("entities.cat");
	}

	@Override
	public void update(GameContainer gc, int delta) {
		if (path == null) {
			path = new MovePath(rand.nextInt(1280), rand.nextInt(720));
		}
		if (path.hasReached) {
			if (rand.nextInt(1000) < 15) {
				path = null;
				path = new MovePath(rand.nextInt(1280), rand.nextInt(720));
			}
		}
		path.update(gc, delta, this);
		if (!path.hasReached) {
			angle = MathHelper.getAngle(new Point(path.getX(), path.getY()),
					new Point((int) getCenterX(), (int) getCenterY()));
		}
		/*
		 * if (worldObj.getCollidingEntity(this) != null) { path = null; // if
		 * (worldObj.getCollidingEntity(this).path != null) { float newX =
		 * worldObj.getCollidingEntity(this).getX() - rand.nextInt(100) + 50;
		 * float newY = worldObj.getCollidingEntity(this).getY() -
		 * rand.nextInt(100) + 50; path = new MovePath(newX, newY, 3f); // } }
		 */
		this.getIcon().setRotation(angle + 90);
	}

}
