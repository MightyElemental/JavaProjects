package net.mightyelemental.mowergame.entities.avoid;

import org.newdawn.slick.GameContainer;

import net.mightyelemental.mowergame.World;

public class EntityDog extends EntityAvoid {
	
	
	private static final long serialVersionUID = -9006257819132425251L;
	
	public EntityDog( float x, float y, float width, float height, World worldObj ) {
		super(x, y, width, height, worldObj);
	}
	
	@Override
	public void update(GameContainer gc, int delta) {
		
	}
	
}
