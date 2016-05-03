package net.mightyelemental.mowergame.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import net.mightyelemental.mowergame.MowerGame;
import net.mightyelemental.mowergame.World;

public abstract class Entity extends Rectangle {

	private static final long serialVersionUID = 1193830639910816843L;

	protected Image displayIcon = MowerGame.NULL_IMAGE;

	protected World worldObj;

	public Entity(float x, float y, float width, float height, World worldObj) {
		super(x, y, width, height);
		this.worldObj = worldObj;
	}

	/** Use to set display icon of entity with an image */
	public Entity setIcon(Image icon) {
		displayIcon = icon;
		return this;
	}

	/** Use to set display icon of entity with a location */
	public Entity setIcon(String icon) {
		displayIcon = MowerGame.resLoader.loadImage(icon);
		return this;
	}

	/** Use to get display icon */
	public Image getIcon() {
		return displayIcon;
	}

	public abstract void update(GameContainer gc, int delta) throws SlickException;

}
