package net.iridgames.towerdefense.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.towerdefense.StateGame;
import net.iridgames.towerdefense.monsters.Monster;
import net.iridgames.towerdefense.towers.BulletTrail;
import net.iridgames.towerdefense.towers.ProjectileV2;
import net.iridgames.towerdefense.towers.TowerV2;

public class World {

	public List<Level>		levelList	= new ArrayList<Level>();
	public List<Monster>	monsterList	= new ArrayList<Monster>();

	// {posx, posy, angle, charge, level, removeFlag, targetType, type, ID}
	// public List<Tower> towerList = new ArrayList<Tower>();
	private List<Object[]> towerList = new ArrayList<Object[]>();

	// {posX, posY, angle, speed, damage, removeFlag}
	private List<Object[]> projectileList = new ArrayList<Object[]>();

	private List<Object[]> bulletTrailList = new ArrayList<Object[]>();

	public Level loadedLevel;

	public World() {
		loadLevels();
		loadedLevel = levelList.get(0);
	}

	public void loadLevels() {
		for ( int i = 0; i <= 5; i++ ) {
			levelList.add(new Level("level", i));
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		for ( int i = 0; i < monsterList.size(); i++ ) {
			monsterList.get(i).update(gc, sbg, delta);
			if ( monsterList.get(i).dead ) {
				monsterList.remove(i);
				break;
			}
		}
		for ( int i = 0; i < towerList.size(); i++ ) {
			TowerV2.update(towerList.get(i), this, delta);
			if ( (boolean) towerList.get(i)[5] ) {
				towerList.remove(i);
				break;
			}
		}
		for ( int i = 0; i < projectileList.size(); i++ ) {
			ProjectileV2.update(projectileList.get(i), this, delta);
			if ( (boolean) projectileList.get(i)[5] ) {
				projectileList.remove(i);
				break;
			}
		}
		for ( int i = 0; i < bulletTrailList.size(); i++ ) {
			BulletTrail.update(bulletTrailList.get(i), this, delta);
			float fade = Float.parseFloat(bulletTrailList.get(i)[4].toString());
			if ( fade >= 1 ) {
				bulletTrailList.remove(i);
				break;
			}
		}
	}

	Random rand = new Random(System.nanoTime());

	public void spawn() {
		// int r = rand.nextInt(loadedLevel.spawningPoints.size());
		for ( int i = 0; i < loadedLevel.spawningPoints.size(); i++ ) {
			float x = loadedLevel.spawningPoints.get(i).getCenterX() * StateGame.tileSize;
			float y = loadedLevel.spawningPoints.get(i).getCenterY() * StateGame.tileSize;
			monsterList.add(new Monster(this, x, y));
		}

	}

	public List<Object[]> getTowerList() {
		return towerList;
	}

	public List<Object[]> getProjectileList() {
		return projectileList;
	}

	public List<Object[]> getBulletTrailList() {
		return bulletTrailList;
	}

	// {posX, posY, angle, speed, damage, removeFlag}
	public boolean addProjectile(float x, float y, float angle, float speed, float damage) {// TODO add lines
																							// as an
																							// alternative
		if ( projectileList.size() < 210 ) {
			projectileList.add(new Object[] { x, y, angle, speed, damage, false });
			return true;
		} else {
			System.err.println("Too many objects");
			return false;
		}
	}

	int lastTurret = 0;

	// {x, y, angle, charge, level, removeFlag, targetType, type, ID}
	public void addTower(float x, float y, float angle, float charge, float level, int targetType, int turretType) {
		if ( !doesTowerExist(x, y) ) {
			towerList.add(new Object[] { x, y, angle, charge, level, false, targetType, turretType, lastTurret });
			lastTurret++;
		}
	}

	// {startX, startY, endX, endY, fade, fadeRate, colour}
	public boolean addBulletTrail(float sx, float sy, float ex, float ey, float fr) {
		fr = (rand.nextInt(5) + 15) / 20f;// TODO FIX
		if ( bulletTrailList.size() < 600 ) {
			bulletTrailList.add(new Object[] { sx, sy, ex, ey, 0.05f, fr });
			return true;
		} else {
			System.err.println("Too many objects");
			return false;
		}
	}

	private boolean doesTowerExist(float x, float y) {
		int smallx = (int) (x / StateGame.tileSize);
		int smally = (int) (y / StateGame.tileSize);
		for ( Object[] obj : towerList ) {
			int sx2 = (int) ((float) obj[0]) / StateGame.tileSize;
			if ( smallx != sx2 ) continue;
			int sy2 = (int) ((float) obj[1]) / StateGame.tileSize;
			if ( smally == sy2 ) return true;
		}
		return false;
	}

}
