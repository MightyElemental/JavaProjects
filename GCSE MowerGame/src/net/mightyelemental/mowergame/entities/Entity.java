package net.mightyelemental.mowergame.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;

public abstract class Entity extends Rectangle {

	private static final long serialVersionUID = 1193830639910816843L;

	public Entity(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	public abstract void update(GameContainer gc, int delta);

}
