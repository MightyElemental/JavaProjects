package net.mightyelemental.mowergame.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import net.mightyelemental.mowergame.World;

public class EntityBloodSplat extends Entity {

	private static final long serialVersionUID = 2375105190211764804L;

	public EntityBloodSplat(float x, float y, World worldObj) {
		super(x, y, 70, 70, worldObj);
		this.setIcon("entities.bloodsplat");
		this.getIcon();
		this.setWidth(worldObj.rand.nextInt(70) + 70);
		this.setHeight(this.getWidth());
		this.setCenterX(x);
		this.setCenterY(y);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		this.getIcon().setAlpha(this.getIcon().getAlpha() - 0.003f * (delta / 17f));
	}

}
