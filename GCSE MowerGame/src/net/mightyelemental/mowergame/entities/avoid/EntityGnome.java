package net.mightyelemental.mowergame.entities.avoid;

import org.newdawn.slick.GameContainer;

import net.mightyelemental.mowergame.World;

public class EntityGnome extends EntityAvoid {
	
	
	private static final long serialVersionUID = 2566965099530442913L;
	
	public EntityGnome( float x, float y, float width, float height, World worldObj ) {
		super(x, y, width, height, worldObj);
	}
	
	@Override
	public void update(GameContainer gc, int delta) {
		
	}
	
}
