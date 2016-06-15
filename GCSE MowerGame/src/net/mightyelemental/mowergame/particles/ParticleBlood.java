package net.mightyelemental.mowergame.particles;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.World;

public class ParticleBlood extends Particle {
	
	
	private static final long serialVersionUID = -883641454987905964L;
	
	public Color color = new Color(1f, 0, 0, 1f);
	
	public float speed = 1f;
	
	public ParticleBlood( float x, float y, World worldObj ) {
		super(x, y, 6, 6, worldObj);
		setIcon(getIcon().getScaledCopy((int) getWidth(), (int) getHeight()));
		angle = worldObj.rand.nextInt(360);
		speed = (worldObj.rand.nextInt(50) / 50f) + 1f;
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
		this.x += amountToMoveX * speed;
		this.y += amountToMoveY * speed;
		color.a -= 0.01f * (delta / 17f) * speed;
	}
	
}
