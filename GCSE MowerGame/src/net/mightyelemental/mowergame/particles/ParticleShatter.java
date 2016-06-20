package net.mightyelemental.mowergame.particles;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.World;

public class ParticleShatter extends Particle {

	private static final long serialVersionUID = -8522275449457156570L;

	public Color color;

	public float speed = 1f;

	public ParticleShatter(float x, float y, World worldObj) {
		super(x, y, 12, 12, worldObj);
		setIcon(getIcon().getScaledCopy((int) getWidth(), (int) getHeight()));
		angle = worldObj.rand.nextInt(360);
		speed = (worldObj.rand.nextInt(50) / 50f) + 1f;
		float c = worldObj.rand.nextInt(90) / 100 + .1f;
		color = new Color(c, c, c, 1f);
	}

	@Override
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) {
		g.setColor(color);
		g.fillOval(getX(), getY(), getWidth(), getHeight());
	}

	public float angle;

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		float amountToMoveX = (2f / 25f * delta * (float) Math.cos(Math.toRadians(angle)));
		float amountToMoveY = (2f / 25f * delta * (float) Math.sin(Math.toRadians(angle)));
		this.x += amountToMoveX * speed * 1.1f;
		this.y += amountToMoveY * speed * 1.1f;
		color.a -= 0.01f * (delta / 17f) * speed;
	}

}
