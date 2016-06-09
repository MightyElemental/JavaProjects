package net.mightyelemental.mowergame.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import net.mightyelemental.mowergame.World;

public class EntityPlayer extends Entity {

	private static final long serialVersionUID = -8560323326163209720L;

	public int hairType = 0; // 0 = default, 1 = trump
	public Color hairColor = new Color(255, 231, 73);

	public EntityPlayer(float x, float y, World worldObj) {
		super(x, y, 50, 50, worldObj);
	}

	public void setHairType(int type) {
		this.hairType = type;
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		
	}

}
