package net.iridgames.towerdefense.towers;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;

import net.iridgames.towerdefense.MathHelper;
import net.iridgames.towerdefense.ResourceLoader;
import net.iridgames.towerdefense.StateGame;
import net.iridgames.towerdefense.monsters.Monster;
import net.iridgames.towerdefense.world.World;

public class TowerV2 {

	public static final int	TYPE_SNIPER		= 0;
	public static final int	TYPE_GATLING	= 1;

	public static final int	TARGET_CLOSEST		= 0;	// Farthest along path
	public static final int	TARGET_MOST_HEALTH	= 1;
	public static final int	TARGET_LEAST_HEALTH	= 2;

	// {{radius, radiusMultiplier, coolDown, damage}}
	private static float[][] turretTypeInfo = { { 7.5f, 1f, 1500, 20 }, { 4f, 1.1f, 200, 5 } };

	private static float	x, y, angle, charge, level;
	private static int		targetType, turretType;
	private static boolean	remove;

	private static Circle area = new Circle(0f, 0f, 5f);

	// {x, y, angle, charge, level, removeFlag, targetType, type, ID}
	public static void update(Object[] data, World worldObj, int d) {
		setTempVars(data);
		charge += d;

		boolean flag = !getMonsterInRadius(worldObj).isEmpty();
		Monster target = null;
		if ( flag ) {
			target = getMonsterInRadius(worldObj).get(0);
			angle = MathHelper.getAngle(new Point(x, y), new Point(target.getCenterX(), target.getCenterY()))
					- 180;
		}
		if ( flag ) {
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
		if ( charge >= turretTypeInfo[turretType][2] ) {
			boolean success = worldObj.addProjectile((x + StateGame.tileSize / 2), // TODO some towers shoot
																					// bullets and some shoot
																					// projectiles
					(y + StateGame.tileSize / 2), angle, 5, turretTypeInfo[turretType][3]);
			if ( success ) charge = 0;
		}
	}

	// {startX, startY, endX, endY, fade, fadeRate, colour}
	private static void fireBullet(World worldObj, Monster target) {
		if ( charge >= turretTypeInfo[turretType][2] ) {
			boolean success = worldObj.addBulletTrail((x + StateGame.tileSize / 2),
					(y + StateGame.tileSize / 2), target.getCenterX(), target.getCenterY(), 1);
			target.health -= turretTypeInfo[turretType][3];
			if ( success ) charge = 0;
		}
	}

	/**
	 * Scans through all monsters and returns the monster(s) that is in the radius
	 */
	private static List<Monster> getMonsterInRadius(World worldObj) {
		List<Monster> lm = new ArrayList<Monster>();
		for ( int i = 0; i < worldObj.monsterList.size(); i++ ) {
			Monster m = worldObj.monsterList.get(i);
			if ( m.intersects(area) ) {
				lm.add(m);
				return lm;
			}
		}
		return lm;
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

	public static void render(Graphics g, Object[] obj, int xoffset, int yoffset) {
		setTempVars(obj);
		getIcon(turretType).setRotation(angle);
		g.drawImage(getIcon(turretType), x + xoffset - StateGame.tileSize / 2,
				y + yoffset - StateGame.tileSize / 2);
		getIcon(turretType).setRotation(0);

//		g.setColor(new Color(0f, 0f, 0f, 1f));// TODO Only render when mouse hovers over
//		g.drawOval(area.getX() + xoffset + StateGame.tileSize / 2,
//				area.getY() + yoffset + StateGame.tileSize / 2, 2 * area.radius, 2 * area.radius);

		g.setColor(Color.cyan.darker());
		float temp = (StateGame.tileSize / turretTypeInfo[turretType][2] * charge);
		g.fillRect(xoffset + x, yoffset + y, 4, temp > StateGame.tileSize ? StateGame.tileSize : temp);
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
		float rad = turretTypeInfo[turretType][0] * StateGame.tileSize;
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
