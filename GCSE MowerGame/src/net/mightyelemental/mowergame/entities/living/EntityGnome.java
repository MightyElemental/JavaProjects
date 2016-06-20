package net.mightyelemental.mowergame.entities.living;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import net.mightyelemental.mowergame.MathHelper;
import net.mightyelemental.mowergame.World;
import net.mightyelemental.mowergame.particles.ParticleShatter;

public class EntityGnome extends EntityLiving {

	private static final long serialVersionUID = 2566965099530442913L;

	public static float moneyGain = 24.50f;

	public EntityGnome(float x, float y, World worldObj) {
		super(x, y, 50, 50, worldObj);
		this.setIcon("entities.gnome");
		this.damageToMower = 2;
		this.timeGain = 8;
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		super.update(gc, delta);
		if (path == null || path.hasReached) {
			path = new MovePath(worldObj.lawnMower, 1.5f);
		}
		if (MathHelper.getDistance(this, worldObj.lawnMower) < 300) {
			if (path.destEnt == null) {
				path = null;
				path = new MovePath(worldObj.lawnMower, 1.5f);
			}
			angle += 180;
		} else {
			if (this.getCenterX() < 0 || this.getCenterX() > gc.getWidth() || this.getCenterY() < 0
					|| this.getCenterY() > gc.getHeight()) {
				path = null;
				path = new MovePath(worldObj.rand.nextInt(1280), worldObj.rand.nextInt(720), 1.5f);
			}
		}
		if (MathHelper.getDistance(this, worldObj.lawnMower) < 300 || (!path.hasReached && path.destEnt == null)) {
			path.update(gc, delta, this);
		}
	}

	@Override
	public void setDead() {
		for (int i = 0; i < worldObj.rand.nextInt(5) + 20; i++) {
			worldObj.createParticle(new ParticleShatter(this.getCenterX(), getCenterY(), worldObj));
		}
		this.dead = true;
	}

}
