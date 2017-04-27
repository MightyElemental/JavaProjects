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
	
	
	private static final long serialVersionUID = 5852336672518609530L;
	
	public float angle;
	
	public float health = 50;
	
	public Monster( World worldObj, float x, float y ) {
		super(x, y, 36, 42);
		this.worldObj = worldObj;
		route = this.getShortestRoute();
	}
	
	/** Which path tiles the monster has walked on */
	public List<Point> touchedPath = new ArrayList<Point>();
	
	public int[][] mark;
	
	/** The calculated route that the monster will move to */
	public List<Point> route = new ArrayList<Point>();
	
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
	
	public boolean dead = false;
	
	/** Uses floodfill to work */
	public List<Point> getShortestRoute() {
		List<Point> lp = new ArrayList<Point>();
		mark = new int[worldObj.loadedLevel.width][worldObj.loadedLevel.height];
		int x = (int) this.getCurrentTile().getX();
		int y = (int) this.getCurrentTile().getY();
		flood(mark, x, y, 0);
		// Print out world
		// for (y = 0; y < worldObj.loadedLevel.height; y++) {
		// for (x = 0; x < worldObj.loadedLevel.width; x++) {
		// if (mark[x][y] > 50) {
		// System.out.print(" x|");
		// } else if (mark[x][y] > 9) {
		// System.out.print(mark[x][y] + "|");
		// } else {
		// System.out.print(" " + mark[x][y] + "|");
		// }
		// }
		// System.out.println();
		// }
		x = (int) worldObj.loadedLevel.getGoal().getX();
		y = (int) worldObj.loadedLevel.getGoal().getY();
		findRoute(mark, x, y, Integer.MAX_VALUE, lp);
		
		for (int i = 0; i < lp.size(); i++) {
			lp.set(i, new Point(StateGame.tileSize * (lp.get(i).getX() + 0.5f), StateGame.tileSize * (lp.get(i).getY() + 0.5f)));
		}
		
		return lp;
	}
	
	private int[][] flood(int[][] mark, int x, int y, int currentNum) {
		// make sure row and col are inside the image
		if (x < 0) return null;
		if (y < 0) return null;
		if (x > worldObj.loadedLevel.width - 1) return null;
		if (y > worldObj.loadedLevel.height - 1) return null;
		
		// make sure this pixel hasn't been visited yet
		if (mark[x][y] > 0) return null;
		
		// make sure this pixel is the right color to fill
		if (worldObj.loadedLevel.getTile(x, y) != '-' && worldObj.loadedLevel.getTile(x, y) != 's') return null;
		
		// fill pixel with target color and mark it as visited
		// img.set(col, row, tgtColor);
		// if (worldObj.loadedLevel.getTile(x, y) == '-') {
		// worldObj.loadedLevel.setTile(x, y, (char) currentNum);
		mark[x][y] = currentNum;
		currentNum++;
		// } else {
		// mark[x][y] = Integer.MAX_VALUE;
		// }
		// recursively fill surrounding pixels
		// (this is equivelant to depth-first search)
		
		flood(mark, x - 1, y, currentNum);
		flood(mark, x + 1, y, currentNum);
		flood(mark, x, y - 1, currentNum);
		flood(mark, x, y + 1, currentNum);
		return mark;
	}
	
	private List<Point> findRoute(int[][] mark, int x, int y, int lowestNum, List<Point> lp) {
		Point lowestPoint = null;
		// System.out.println(mark[x][y] + "|" + x + "|" + y + "|" + lowestNum);
		
		if (x < worldObj.loadedLevel.width - 1) {
			if (mark[x + 1][y] < lowestNum && mark[x + 1][y] != 0) {
				lowestNum = mark[x + 1][y];
				lowestPoint = new Point(x + 1, y);
			}
		}
		if (x > 0) {
			if (mark[x - 1][y] < lowestNum && mark[x - 1][y] != 0) {
				lowestNum = mark[x - 1][y];
				lowestPoint = new Point(x - 1, y);
			}
		}
		if (y < worldObj.loadedLevel.height - 1) {
			if (mark[x][y + 1] < lowestNum && mark[x][y + 1] != 0) {
				lowestNum = mark[x][y + 1];
				lowestPoint = new Point(x, y + 1);
			}
		}
		if (y > 0) {
			if (mark[x][y - 1] < lowestNum && mark[x][y - 1] != 0) {
				lowestNum = mark[x][y - 1];
				lowestPoint = new Point(x, y - 1);
			}
		}
		
		if (lowestPoint == null) { return lp; }
		
		lp.add(0, lowestPoint);
		
		findRoute(mark, (int) lowestPoint.getX(), (int) lowestPoint.getY(), lowestNum, lp);
		
		return lp;
	}
	
	@Deprecated
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
		// Point nextPath = getClosestPath();
		// if (nextPath != null) {
		// float distance = MathHelper.getDistance(this.getCenterX(), this.getCenterY(), nextPath);
		// angle = MathHelper.getAngle(new Point(this.getCenterX(), this.getCenterY()), nextPath) - 180;
		// // System.out.println(nextPath.getX());
		//
		// if (distance < 5) {
		// touchedPath.add(new Point((float) Math.floor(getCenterX() / StateGame.tileSize),
		// (float) Math.floor(getCenterY() / StateGame.tileSize)));
		// }
		// }
		
		if (route.size() > 0) {
			Point p = route.get(0);
			float distance = MathHelper.getDistance(this.getCenterX(), this.getCenterY(), p);
			angle = MathHelper.getAngle(new Point(this.getCenterX(), this.getCenterY()), p) - 180;
			if (distance < 5) {
				route.remove(0);
			}
		}
		
		if (worldObj.loadedLevel.getGoal().getX() == getCurrentTile().getX()
			&& worldObj.loadedLevel.getGoal().getY() == getCurrentTile().getY()) {
			this.dead = true;
		}
		if (health <= 0) {
			this.dead = true;
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
	
	public Point getCurrentTile() {
		return new Point((float) Math.floor(getCenterX() / StateGame.tileSize), (float) Math.floor(getCenterY() / StateGame.tileSize));
	}
	
}
