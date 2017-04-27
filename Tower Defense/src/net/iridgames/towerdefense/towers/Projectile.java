package net.iridgames.towerdefense.towers;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.towerdefense.monsters.Monster;

public class Projectile extends Circle {
	
	
	public float angle, damage, speed;
	public boolean delete;
	public Monster target;
	
	public Projectile( float x, float y, float angle, float speed, float damage, Monster target ) {
		super(x - 2.5f, y - 2.5f, 5);
		this.angle = angle;
		this.speed = speed;
		this.damage = damage;
		this.target = target;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		if (this.intersects(target)) {
			target.health -= damage;
			this.delete = true;
			x = -1000;
			y = -1000;
		} else if (!delete) {
			x += Math.cos(Math.toRadians(angle)) * speed;
			y += Math.sin(Math.toRadians(angle)) * speed;
		}
	}
	
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g, int startingPointX, int startingPointY) {
		g.fillOval(startingPointX + x, startingPointY + y, 2 * radius, 2 * radius);
	}
	
	private static final long serialVersionUID = -8947291093119814539L;
	
}
