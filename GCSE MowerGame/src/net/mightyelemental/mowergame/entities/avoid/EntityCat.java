package net.mightyelemental.mowergame.entities.avoid;

import org.newdawn.slick.GameContainer;

import net.mightyelemental.mowergame.World;

public class EntityCat extends EntityAvoid {

	private static final long serialVersionUID = -3701251139422836127L;

	public EntityCat(float x, float y, World worldObj) {
		super(x, y, 50, 50, worldObj);
	}

	@Override
	public void update(GameContainer gc, int delta) {

	}

}
