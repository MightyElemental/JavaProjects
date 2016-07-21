package net.mightyelemental.mowergame.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import net.mightyelemental.mowergame.MowerGame;
import net.mightyelemental.mowergame.World;

public abstract class Entity extends Rectangle {
	
	
	private static final long serialVersionUID = 1193830639910816843L;
	
	protected Image displayIcon;
	
	protected World worldObj;
	
	public Entity( float x, float y, float width, float height, World worldObj ) {
		super(x, y, width, height);
		this.worldObj = worldObj;
		if (displayIcon == null) {
			displayIcon = MowerGame.resLoader.loadImage("null");
			displayIcon = displayIcon.getScaledCopy((int) width, (int) height);
		}
	}
	
	public float angle = 0;
	
	/** Use to get the angle of the entity */
	public float getAngle() {
		return angle;
	}
	
	/** Use to set display icon of entity with an image */
	public Entity setIcon(Image icon) {
		if (icon == null) { return this; }
		displayIcon = icon;
		displayIcon = displayIcon.getScaledCopy((int) width, (int) height);
		return this;
	}
	
	/** Use to set display icon of entity with a location */
	public Entity setIcon(String icon) {
		displayIcon = MowerGame.resLoader.loadImage(icon).getScaledCopy((int) width, (int) height);
		return this;
	}
	
	/** Use to get display icon */
	public Image getIcon() {
		if (displayIcon == null) {
			displayIcon = MowerGame.resLoader.loadImage("null");
			displayIcon = displayIcon.getScaledCopy((int) width, (int) height);
		}
		return displayIcon;
	}
	
	public abstract void update(GameContainer gc, int delta) throws SlickException;
	
}
