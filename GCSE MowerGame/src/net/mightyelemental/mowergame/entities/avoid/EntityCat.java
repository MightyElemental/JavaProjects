package net.mightyelemental.mowergame.entities.avoid;

import java.awt.Point;
import java.util.Random;

import org.newdawn.slick.GameContainer;

import net.mightyelemental.mowergame.MathHelper;
import net.mightyelemental.mowergame.World;

public class EntityCat extends EntityAvoid {

	private static final long serialVersionUID = -3701251139422836127L;

	protected MovePath path;

	private Random rand;

	public EntityCat(float x, float y, World worldObj, Random rand) {
		super(x, y, 55, 55, worldObj);
		this.rand = rand;
	}

	@Override
	public void update(GameContainer gc, int delta) {
		if (path == null) {
			path = new MovePath(rand.nextInt(1280), rand.nextInt(720));
		}
		if (path.hasReached) {
			path = null;
			path = new MovePath(rand.nextInt(1280), rand.nextInt(720));
		}
		path.update(gc, delta, this);
		angle = MathHelper.getAngle(new Point(path.getX(), path.getY()), new Point((int) x, (int) y));
		this.getIcon().setRotation(angle + 90);
	}

}
