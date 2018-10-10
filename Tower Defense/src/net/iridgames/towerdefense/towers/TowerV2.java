package net.iridgames.towerdefense.towers;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;

import net.iridgames.towerdefense.Camera;
import net.iridgames.towerdefense.MathHelper;
import net.iridgames.towerdefense.ResourceLoader;
import net.iridgames.towerdefense.monsters.Monster;
import net.iridgames.towerdefense.world.World;

public class TowerV2 {

	public static final int TYPE_SNIPER = 0;
	public static final int TYPE_GATLING = 1;

	public static final int TARGET_CLOSEST_TO_TURRET = 0; // Closest to turret
	public static final int TARGET_CLOSEST = 1; // Closest to goal
	public static final int TARGET_MOST_HEALTH = 2;
	public static final int TARGET_LEAST_HEALTH = 3;

	// {{radius, radiusMultiplier, coolDown, damage, cost}}
	private static float[][] turretTypeInfo = { { 6f, 1f, 1200, 50, 200 }, { 4f, 1f, 200, 5, 150 } };

	private static float x, y, angle, charge, level;
	private static int targetType, turretType;
	private static boolean remove;

	private static Circle area = new Circle(0f, 0f, 5f);

	// {x, y, angle, charge, level, removeFlag, targetType, type, ID}
	public static void update(Object[] data, World worldObj, int d) {
		setTempVars(data);
		charge += d;
		List<Monster> mList = getMonstersInRadius(worldObj);
		boolean flag = !mList.isEmpty();
		Monster target = null;
		if (flag) {
			switch (targetType) {
			case TARGET_LEAST_HEALTH:
				target = getLeastHealth(mList);
				break;
			case TARGET_MOST_HEALTH:
				target = getMostHealth(mList);
				break;
			case TARGET_CLOSEST_TO_TURRET:
				target = getClosestToTurret(mList);
				break;
			case TARGET_CLOSEST:
				target = getClosestToGoal(mList);
				break;
			default:
				target = mList.get(0);
				break;
			}
			angle = MathHelper.getAngle(new Point(x + 24, y + 24), new Point(target.getCenterX(), target.getCenterY()))
					- 180;
		}
		if (flag) {
			switch (turretType) {
			case TYPE_SNIPER:
				fireProjectiles(worldObj);
				break;
			case TYPE_GATLING:
				fireBullet(worldObj, target);
			}
		}
		saveVarsToArray(data);
	}

	private static void fireProjectiles(World worldObj) {
		if (charge >= turretTypeInfo[turretType][2]) {
			float sX = (float) ((x + 24) + Math.cos(Math.toRadians(angle)) * 48);
			float sY = (float) ((y + 24) + Math.sin(Math.toRadians(angle)) * 48);
			boolean success = worldObj.addProjectile(sX, sY, angle, 5, turretTypeInfo[turretType][3]);
			if (success) {
				charge = 0;
				worldObj.addSmoke(sX, sY);
			}
		}
	}

	// {startX, startY, endX, endY, fade, fadeRate, colour}
	private static void fireBullet(World worldObj, Monster target) {
		if (charge >= turretTypeInfo[turretType][2]) {

			float sX = (float) ((x + 24) + Math.cos(Math.toRadians(angle)) * 48);
			float sY = (float) ((y + 24) + Math.sin(Math.toRadians(angle)) * 48);

			boolean success = worldObj.addBulletTrail(sX, sY, target.getCenterX(), target.getCenterY(), 1);
			target.health -= turretTypeInfo[turretType][3];
			if (success) {
				charge = 0;
				worldObj.addSmoke(sX, sY);
			}
		}
	}

	/**
	 * Scans through all monsters and returns the monster(s) that is in the radius
	 */
	private static List<Monster> getMonstersInRadius(World worldObj) {
		List<Monster> lm = new ArrayList<Monster>();
		for (int i = 0; i < worldObj.monsterList.size(); i++) {
			Monster m = worldObj.monsterList.get(i);
			if (m.intersects(area)) {
				lm.add(m);
			}
		}
		return lm;
	}

	private static Monster getLeastHealth(List<Monster> mList) {
		Monster least = null;
		float lHealth = Float.MAX_VALUE;
		for (Monster m : mList) {
			if (m.health < lHealth) {
				lHealth = m.health;
				least = m;
			}
		}
		return least;
	}

	private static Monster getMostHealth(List<Monster> mList) {
		Monster most = null;
		float mHealth = Integer.MIN_VALUE;
		for (Monster m : mList) {
			if (m.health > mHealth) {
				mHealth = m.health;
				most = m;
			}
		}
		return most;
	}

	private static Monster getClosestToTurret(List<Monster> mList) {
		Monster closest = null;
		float distance = Integer.MAX_VALUE;
		for (Monster m : mList) {
			float mDist = MathHelper.getDistance(new Point(x + 24, y + 24), new Point(m.getCenterX(), m.getCenterY()));
			if (mDist < distance) {
				distance = mDist;
				closest = m;
			}
		}
		return closest;
	}

	private static Monster getClosestToGoal(List<Monster> mList) {
		Monster closest = null;
		float dist = Integer.MAX_VALUE;
		for (Monster m : mList) {
			if (m.getPathSize() < dist) {
				dist = m.getPathSize();
				closest = m;
			}
		}
		return closest;
	}

	public static int getCost(int turretType) {
		return (int) turretTypeInfo[turretType][4];
	}

	/*
	 * 
	 * RENDER STUFF
	 * 
	 */

	public static Image getIcon(int turretType) {
		switch (turretType) {
		case TYPE_SNIPER:
			return ResourceLoader.loadImage("sniper");
		case TYPE_GATLING:
			return ResourceLoader.loadImage("gatling");
		}
		return ResourceLoader.loadImage("null");
	}

	public static void render(Graphics g, Object[] obj) {
		setTempVars(obj);
		getIcon(turretType).setRotation(angle);
		g.drawImage(getIcon(turretType), x - Camera.tileSize / 2, y - Camera.tileSize / 2);
		getIcon(turretType).setRotation(0);

		// g.setColor(new Color(0f, 0f, 0f, 1f));// TODO Only render when mouse hovers
		// over
		// g.drawOval(area.getX() + xoffset + StateGame.tileSize / 2,
		// area.getY() + yoffset + StateGame.tileSize / 2, 2 * area.radius, 2 *
		// area.radius);

		g.setColor(Color.cyan.darker());
		float temp = (Camera.tileSize / turretTypeInfo[turretType][2] * charge);
		g.fillRect(x, y, 4, temp > Camera.tileSize ? Camera.tileSize : temp);
	}

	/*
	 * 
	 * DATA STUFF
	 * 
	 */

	public static void setTempVars(Object[] data) {
		x = Float.parseFloat(data[0].toString());
		y = Float.parseFloat(data[1].toString());
		angle = Float.parseFloat(data[2].toString());
		charge = Float.parseFloat(data[3].toString());
		level = Float.parseFloat(data[4].toString());
		remove = (boolean) data[5];
		targetType = Integer.parseInt(data[6].toString());
		turretType = Integer.parseInt(data[7].toString());
		setArea();
	}

	public static void setArea() {
		float rad = turretTypeInfo[turretType][0] * 48;
		float mul = (float) Math.pow(turretTypeInfo[turretType][1], level);
		area.setRadius(rad * mul);
		area.setCenterX(x);
		area.setCenterY(y);
	}

	public static void saveVarsToArray(Object[] data) {
		data[0] = x;
		data[1] = y;
		data[2] = angle;
		data[3] = charge;
		data[4] = level;
		data[5] = remove;
		data[6] = targetType;
		data[7] = turretType;
	}

}
