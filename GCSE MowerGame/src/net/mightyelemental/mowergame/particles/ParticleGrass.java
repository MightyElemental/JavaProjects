package net.mightyelemental.mowergame.particles;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.World;

public class ParticleGrass extends Particle {
	
	
	private static final long serialVersionUID = 1L;
	
	public Color color = new Color(0, 0.5f, 0, 1f);
	
	public ParticleGrass( float x, float y, World worldObj ) {
		super(x, y, 6, 8, worldObj);
		this.setIcon("grass");
		setIcon(getIcon().getScaledCopy((int) getWidth(), (int) getHeight()));
		angle = worldObj.lawnMower.angle - 180 + worldObj.rand.nextInt(100) - 50;
	}
	
	@Override
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) {
		g.drawImage(getIcon(), getX(), getY(), color);
	}
	
	public float angle;
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		float amountToMoveX = (2f / 25f * delta * (float) Math.cos(Math.toRadians(angle)));
		float amountToMoveY = (2f / 25f * delta * (float) Math.sin(Math.toRadians(angle)));
		this.x += amountToMoveX;
		this.y += amountToMoveY;
		color.a -= 0.03f * (delta / 17f);
	}
	
}
