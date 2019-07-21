package net.iridgames.towerdefense.monsters;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.towerdefense.Camera;
import net.iridgames.towerdefense.MathHelper;
import net.iridgames.towerdefense.world.World;

public class Monster extends Rectangle {

	private static final long serialVersionUID = 5852336672518609530L;

	private float angle;

	public float maxHealth = 70;
	public float health = maxHealth;

	private float speed = 0.5f;

	public Monster(World worldObj, float x, float y) {
		super(x, y, 36, 42);
		this.worldObj = worldObj;
		route = this.getShortestRoute();
	}

	public Monster(World worldObj, float x, float y, float maxHealth) {
		this(worldObj, x, y);
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}

	/** Which path tiles the monster has walked on */
	public List<Point> touchedPath = new ArrayList<Point>();

	public int[][] mark;

	/** The calculated route that the monster will move to */
	public List<Point> route = new ArrayList<Point>();

	public boolean touchedTile(float f, float g) {
		f = (float) Math.floor(f / 48);
		g = (float) Math.floor(g / 48);
		for (int i = 0; i < touchedPath.size(); i++) {
			float tx = touchedPath.get(i).getX();
			float ty = touchedPath.get(i).getY();
			if (f == tx && ty == g) {
				return true;
			}
		}
		return false;
	}

	protected World worldObj;

	public boolean dead = false, won = false;

	public List<Long> ignoreList = new ArrayList<Long>();

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
			lp.set(i, new Point(48 * (lp.get(i).getX() + 0.5f), 48 * (lp.get(i).getY() + 0.5f)));
		}

		return lp;
	}

	private int[][] flood(int[][] mark, int x, int y, int currentNum) {// TODO FIX THE BROKEN PATH FINDER
		// make sure row and col are inside the image
		if (!isPointInWorld(x, y))
			return null;

		// make sure this pixel hasn't been visited yet
		if (mark[x][y] > 0)
			return null;

		// make sure this tile is walkable
		if (!isTileWalkable(x, y))
			return null;

		// fill pixel with target color and mark it as visited
		mark[x][y] = currentNum;
		currentNum++;
		// if ( testForAdjacentWalkableTiles(x, y) > 2 ) {}

		for (int a = 0; a < 360; a += 90) {
			int x1 = (int) (Math.cos(Math.toRadians(a)));
			int y1 = (int) (Math.sin(Math.toRadians(a)));
			flood(mark, x + x1, y + y1, currentNum);
		}
		return mark;
	}

	private char testForTile(int x, int y) {
		return worldObj.loadedLevel.getTile(x, y);
	}

	@Deprecated
	int testForAdjacentWalkableTiles(int x, int y) {
		int total = 0;
		for (int a = 0; a < 360; a += 90) {
			int x1 = (int) (Math.cos(Math.toRadians(a)));
			int y1 = (int) (Math.sin(Math.toRadians(a)));
			if (isPointInWorld(x + x1, y + y1) && isTileWalkable(x + x1, y + y1)) {
				total++;
			}
		}
		return total;
	}

	private boolean isTileWalkable(int x, int y) {
		return testForTile(x, y) == '-' || testForTile(x, y) == 's';
	}

	private boolean isPointInWorld(int x, int y) {
		if (x < 0)
			return false;
		if (y < 0)
			return false;
		if (x > worldObj.loadedLevel.width - 1)
			return false;
		if (y > worldObj.loadedLevel.height - 1)
			return false;
		return true;
	}

	private List<Point> findRoute(int[][] mark, int x, int y, int lowestNum, List<Point> lp) {
		List<Point> potentialPoints = new ArrayList<Point>();
		// System.out.println(mark[x][y] + "|" + x + "|" + y + "|" + lowestNum);
		int newLowest = lowestNum;

		if (x < worldObj.loadedLevel.width - 1) {
			if (mark[x + 1][y] < lowestNum && mark[x + 1][y] != 0) {
				newLowest = mark[x + 1][y];
				potentialPoints.add(new Point(x + 1, y));
			}
		}
		if (x > 0) {
			if (mark[x - 1][y] < lowestNum && mark[x - 1][y] != 0) {
				newLowest = mark[x - 1][y];
				potentialPoints.add(new Point(x - 1, y));
			}
		}
		if (y < worldObj.loadedLevel.height - 1) {
			if (mark[x][y + 1] < lowestNum && mark[x][y + 1] != 0) {
				newLowest = mark[x][y + 1];
				potentialPoints.add(new Point(x, y + 1));
			}
		}
		if (y > 0) {
			if (mark[x][y - 1] < lowestNum && mark[x][y - 1] != 0) {
				newLowest = mark[x][y - 1];
				potentialPoints.add(new Point(x, y - 1));
			}
		}

		if (potentialPoints.isEmpty()) {
			return lp;
		}
		lowestNum = newLowest;
		Point lowestPoint = potentialPoints.get(worldObj.rand.nextInt(potentialPoints.size()));

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
					// pathLocations.add(new Point((x + 0.5f) * StateGame.tileSize, (y + 0.5f) *
					// StateGame.tileSize)); // uncomment - this is an error
				}
			}
		}

		for (int i = 0; i < pathLocations.size(); i++) {
			if (touchedTile(pathLocations.get(i).getX(), pathLocations.get(i).getY()))
				continue;
			if (MathHelper.getDistance(this.getCenterX(), this.getCenterY(), pathLocations.get(i)) < pathDistance) {
				pathDistance = MathHelper.getDistance(this.getCenterX(), this.getCenterY(), pathLocations.get(i));
				closestPath = pathLocations.get(i);
			}
		}
		return closestPath;
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		float dRatio = delta / 9f;
		// speed = 0.5f * (1 - (health / maxHealth)) + 0.1f;
		// System.out.println("asd");
		// Point nextPath = getClosestPath();
		// if (nextPath != null) {
		// float distance = MathHelper.getDistance(this.getCenterX(), this.getCenterY(),
		// nextPath);
		// angle = MathHelper.getAngle(new Point(this.getCenterX(), this.getCenterY()),
		// nextPath) - 180;
		// // System.out.println(nextPath.getX());
		//
		// if (distance < 5) {
		// touchedPath.add(new Point((float) Math.floor(getCenterX() /
		// StateGame.tileSize),
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
			this.won = true;
		}
		if (health <= 0) {
			this.dead = true;
		}

		x += Math.cos(Math.toRadians(angle)) * speed * dRatio;
		y += Math.sin(Math.toRadians(angle)) * speed * dRatio;
	}

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) {
		g.setColor(new Color(health / maxHealth, 0f, 0f, 1f));
		g.fillRect(getX(), getY(), width, height);
		// for ( int j = 0; j < route.size(); j++ ) {
		// if ( route.get(j) != null ) {
		// g.fillOval(Camera.xOffset + route.get(j).getX() - 5, Camera.yOffset +
		// route.get(j).getY() - 5, 10, 10);
		// g.drawString("c"+j, Camera.xOffset + route.get(j).getX(), Camera.yOffset +
		// route.get(j).getY());
		// }
		// }
		// drawFlood(g);
	}

	public void drawFlood(Graphics g) {
		for (int x = 0; x < mark.length; x++) {
			for (int y = 0; y < mark[x].length; y++) {
				if (mark[x][y] != 0) {
					g.drawString("c" + mark[x][y], x * Camera.tileSize, y * Camera.tileSize);
				}
			}
		}
	}

	public int getPathSize() {
		return route.size();
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
		return new Point((float) Math.floor(getCenterX() / 48), (float) Math.floor(getCenterY() / 48));
	}

	public Monster setSpeed(float newSpeed) {
		this.speed = newSpeed;
		return this;
	}

	public float getSpeed() {
		return speed;
	}
}
