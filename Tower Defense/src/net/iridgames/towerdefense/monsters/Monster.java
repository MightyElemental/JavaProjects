package net.iridgames.towerdefense.monsters;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.towerdefense.MathHelper;
import net.iridgames.towerdefense.StateGame;
import net.iridgames.towerdefense.world.World;

public class Monster extends Rectangle {
	
	
	public float angle;
	
	public Monster( World worldObj, float x, float y ) {
		super(x, y, 36, 42);
		this.worldObj = worldObj;
	}
	
	/** Which path tiles the monster has walked on */
	public List<Point> touchedPath = new ArrayList<Point>();
	
	public boolean touchedTile(float f, float g) {
		f = (float) Math.floor(f / StateGame.tileSize);
		g = (float) Math.floor(g / StateGame.tileSize);
		for (int i = 0; i < touchedPath.size(); i++) {
			float tx = touchedPath.get(i).getX();
			float ty = touchedPath.get(i).getY();
			if (f == tx && ty == g) { return true; }
		}
		return false;
	}
	
	protected World worldObj;
	
	public Point getClosestPath() {
		List<Point> pathLocations = new ArrayList<Point>();
		float pathDistance = Integer.MAX_VALUE;
		Point closestPath = null;
		
		for (int y = 0; y < worldObj.loadedLevel.height; y++) {
			for (int x = 0; x < worldObj.loadedLevel.width; x++) {
				if (worldObj.loadedLevel.getTile(x, y) == '-') {
					pathLocations.add(new Point((x + 0.5f) * StateGame.tileSize, (y + 0.5f) * StateGame.tileSize));
				}
			}
		}
		
		for (int i = 0; i < pathLocations.size(); i++) {
			if (touchedTile(pathLocations.get(i).getX(), pathLocations.get(i).getY())) continue;
			if (MathHelper.getDistance(this.getCenterX(), this.getCenterY(), pathLocations.get(i)) < pathDistance) {
				pathDistance = MathHelper.getDistance(this.getCenterX(), this.getCenterY(), pathLocations.get(i));
				closestPath = pathLocations.get(i);
			}
		}
		return closestPath;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		Point nextPath = getClosestPath();
		float distance = MathHelper.getDistance(this.getCenterX(), this.getCenterY(), nextPath);
		angle = MathHelper.getAngle(new Point(this.getCenterX(), this.getCenterY()), nextPath) - 180;
		// System.out.println(nextPath.getX());
		
		if (distance < 5) {
			touchedPath.add(new Point((float) Math.floor(getX() / StateGame.tileSize), (float) Math.floor(getY() / StateGame.tileSize)));
		}
		
		x += Math.cos(Math.toRadians(angle));
		y += Math.sin(Math.toRadians(angle));
	}
	
	@Override
	public float getCenterX() {
		return this.getX() + this.getWidth() / 2;
	}
	
	@Override
	public float getCenterY() {
		return this.getY() + this.getHeight() / 2;
	}
	
}
