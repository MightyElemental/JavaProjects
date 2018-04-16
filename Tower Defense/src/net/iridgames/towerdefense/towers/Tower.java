package net.iridgames.towerdefense.towers;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.towerdefense.MathHelper;
import net.iridgames.towerdefense.ResourceLoader;
import net.iridgames.towerdefense.StateGame;
import net.iridgames.towerdefense.monsters.Monster;
import net.iridgames.towerdefense.world.World;

@Deprecated
public abstract class Tower extends Rectangle {
	
	
	private static final long serialVersionUID = 2910144072239689558L;
	public Image topLayer;
	
	//protected float i = StateGame.tileSize; // THIS IS THE REAL CODE
	protected float i = 0; // THIS IS THE FAKE CODE
	public boolean removed;
	protected World worldObj;
	
	/** @param x
	 *            co-ord in tiles
	 * @param y
	 *            co-ord in tiles
	 * @param radius
	 *            in tiles (can be fractional) */
	public Tower( World worldObj, float x, float y, float radius ) {
		super(x, y, 48, 48);
		this.worldObj = worldObj;
		area = new Circle(x * i, y * i, radius * i);
		area.setCenterX(getCenterX());
		area.setCenterY(getCenterY());
	}
	
	protected Circle area;
	private float angle;
	protected Monster target;
	/** Time in ms */
	private float time = 500;
	private float activeTime;
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		targetMonster();
		if (target != null) {
			angle = MathHelper.getAngle(new Point(this.getCenterX(), this.getCenterY()),
				new Point(target.getCenterX(), target.getCenterY())) - 180;
			topLayer.setRotation(angle);
		}
		
		activeTime += delta;
		if (activeTime > time && !getMonsterInRadius().isEmpty()) {
			activeTime = 0;
			attackMonsters(getMonsterInRadius());
		}
	}
	
	/** Scans through all monsters and returns the monster(s) that is in the radius */
	public List<Monster> getMonsterInRadius() {
		List<Monster> lm = new ArrayList<Monster>();
		for (int i = 0; i < worldObj.monsterList.size(); i++) {
			Monster m = worldObj.monsterList.get(i);
			if (m.intersects(area)) {
				lm.add(m);
				return lm;
			}
		}
		return lm;
	}
	
	/** Attacks any monsters in the radius */
	public abstract void attackMonsters(List<Monster> monsters);
	
	public Monster targetMonster() {
		if (getMonsterInRadius().isEmpty()) return null;
		Monster m = getMonsterInRadius().get(0);
		if (m != null) {
			target = m;
		}
		return m;
	}
	
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g, int startingPointX, int startingPointY) {
		// g.setColor(Color.cyan);
		// g.fillRect(startingPointX + x * i, startingPointY + y * i, width, height);
		g.setColor(new Color(0f, 0f, 0f, 0.3f));
		g.drawOval(area.getX() + startingPointX, area.getY() + startingPointY, 2 * area.radius, 2 * area.radius);
		g.setColor(Color.cyan.darker());
		float temp = (height / time * activeTime);
		g.fillRect(startingPointX + x * i, startingPointY + y * i, 4, temp > height ? height : temp);
		g.drawImage(topLayer, startingPointX + (x - 0.5f) * i, startingPointY + (y - 0.5f) * i);
	}
	
	@Override
	public float getCenterX() {
		return this.getX() * i + this.getWidth() / 2;
	}
	
	@Override
	public float getCenterY() {
		return this.getY() * i + this.getHeight() / 2;
	}
	
	public void setTime(float time) {
		this.time = time;
	}
	
	public void setTopLayer(String location) {
		this.topLayer = ResourceLoader.loadImage(location).copy();
		// if (topLayer != null) {
		// topLayer = topLayer.getScaledCopy(48, 48);
		// }
	}
	
}
