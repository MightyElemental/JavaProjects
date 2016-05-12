package net.mightyelemental.mowergame.entities.avoid;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import net.mightyelemental.mowergame.World;

public class EntityGnome extends EntityAvoid {

	private static final long serialVersionUID = 2566965099530442913L;

	public EntityGnome(float x, float y, World worldObj) {
		super(x, y, 50, 50, worldObj);
		this.setIcon("entities.bush");
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		super.update(gc, delta);
		angle += 180;
		if (path == null) {
			path = new MovePath(worldObj.lawnMower, 1.5f);
		}
		// path.update(gc, delta, this);
	}

}
