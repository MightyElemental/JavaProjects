package net.mightyelemental.mowergame.particles;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.World;
import net.mightyelemental.mowergame.entities.Entity;

public abstract class Particle extends Entity {

	private static final long serialVersionUID = 2768410055109970172L;

	public Particle(float x, float y, float width, float height, World worldObj) {
		super(x, y, width, height, worldObj);
	}

	public abstract void draw(GameContainer gc, StateBasedGame sbg, Graphics g);

}
