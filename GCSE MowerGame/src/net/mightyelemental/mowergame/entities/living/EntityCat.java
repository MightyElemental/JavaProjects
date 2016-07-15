package net.mightyelemental.mowergame.entities.living;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import net.mightyelemental.mowergame.World;

public class EntityCat extends EntityLiving {

	private static final long serialVersionUID = -3701251139422836127L;

	public EntityCat(float x, float y, World worldObj) {
		super(x, y, 55, 55, worldObj);
		this.setIcon("entities.cat");
		this.timeGain = 5;
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		super.update(gc, delta);
		if (path == null) {
			path = new MovePath(rand.nextInt(1280), rand.nextInt(720));
		}
		if (path.hasReached) {
			if (rand.nextInt(1000) < 15) {
				path = null;
				int x = (int) (rand.nextInt(1280));
				if (x <= 0) {
					x = 0;
				}
				if (x >= gc.getWidth()) {
					x = gc.getWidth();
				}
				path = new MovePath(x, rand.nextInt(720));
			}
		}
		path.update(gc, delta, this);
		// if (worldObj.getCollidingEntity(this) != null) {
		// path = null;
		// if (worldObj.getCollidingEntity(this).path != null) {
		// float newX = worldObj.getCollidingEntity(this).getX() -
		// rand.nextInt(100) + 50;
		// float newY = worldObj.getCollidingEntity(this).getY() -
		// rand.nextInt(100) + 50;
		// path = new MovePath(newX, newY, 3f);
		// }
		// }
	}

}
