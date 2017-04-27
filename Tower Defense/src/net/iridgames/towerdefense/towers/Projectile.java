package net.iridgames.towerdefense.towers;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.towerdefense.StateGame;
import net.iridgames.towerdefense.monsters.Monster;
import net.iridgames.towerdefense.world.World;

public class Projectile extends Circle {
	
	
	public float angle, damage, speed;
	public boolean delete;
	public World worldObj;
	
	public Projectile( World worldObj, float x, float y, float angle, float speed, float damage ) {
		super(x - 2.5f, y - 2.5f, 5);
		this.angle = angle;
		this.speed = speed;
		this.damage = damage;
		this.worldObj = worldObj;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		if (x > worldObj.loadedLevel.width * StateGame.tileSize || x < 0) delete = true;
		if (y > worldObj.loadedLevel.height * StateGame.tileSize || y < 0) delete = true;
		if (delete) return;
		for (Monster m : worldObj.monsterList) {
			if (this.intersects(m)) {
				m.health -= damage;
				this.delete = true;
				x = -1000;
				y = -1000;
				break;
			}
		}
		if (!delete) {
			x += Math.cos(Math.toRadians(angle)) * speed;
			y += Math.sin(Math.toRadians(angle)) * speed;
		}
	}
	
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g, int startingPointX, int startingPointY) {
		g.fillOval(startingPointX + x, startingPointY + y, 2 * radius, 2 * radius);
	}
	
	private static final long serialVersionUID = -8947291093119814539L;
	
}
