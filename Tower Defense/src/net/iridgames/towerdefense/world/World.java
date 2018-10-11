package net.iridgames.towerdefense.world;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.towerdefense.ResourceLoader;
import net.iridgames.towerdefense.TowerDefense;
import net.iridgames.towerdefense.monsters.Monster;
import net.iridgames.towerdefense.towers.BulletTrail;
import net.iridgames.towerdefense.towers.ProjectileV2;
import net.iridgames.towerdefense.towers.TowerType;
import net.iridgames.towerdefense.towers.TowerV2;

public class World {

	public List<Level> levelList = new ArrayList<Level>();
	public List<Monster> monsterList = new ArrayList<Monster>();

	// {posx, posy, angle, charge, level, removeFlag, targetType, type, ID}
	// public List<Tower> towerList = new ArrayList<Tower>();
	private List<Object[]> towerList = new ArrayList<Object[]>();

	// {posX, posY, angle, speed, damage, removeFlag}
	private List<Object[]> projectileList = new ArrayList<Object[]>();
	private List<Object[]> projectilesToAdd = new ArrayList<Object[]>();

	private List<Object[]> bulletTrailList = new ArrayList<Object[]>();

	public Level loadedLevel;

	public ParticleSystem ps;

	public World() {
		loadLevels();
		loadedLevel = levelList.get(3);
		smokeConfig = new File("assets/particles/smoke_emitter.xml");
	}

	public void init() {
		ps = new ParticleSystem(ResourceLoader.loadImage("smoke"), 400);
		ps.setRemoveCompletedEmitters(true);
		ps.setBlendingMode(ParticleSystem.BLEND_COMBINE);
	}

	public void loadLevels() {
		for (int i = 0; i <= 8; i++) {
			levelList.add(new Level("level", i));
		}
	}

	public void renderSmoke(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		ps.render();
	}

	public int time = 0;

	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		ps.update(delta);
		time += delta;
		if (monsterList.size() < 1 * (time / 5000f + 1) && monsterList.size() < 40) {
			spawn();
		}
		for (int i = 0; i < monsterList.size(); i++) {
			Monster m = monsterList.get(i);
			m.update(gc, sbg, delta);
			if (m.dead) {
				monsterList.remove(i);
				TowerDefense.money += 9 * (-(1200 / (time / 1000f + 1200)) + 2);
			} else if (m.won) {
				TowerDefense.money -= (15 + m.health);
				monsterList.remove(i);
			}
		}
		for (int i = 0; i < towerList.size(); i++) {
			TowerV2.update(towerList.get(i), this, delta);
			if ((boolean) towerList.get(i)[5]) {
				towerList.remove(i);
			}
		}
		while (projectileList.size() < 210 && !projectilesToAdd.isEmpty()) {
			projectileList.add(projectilesToAdd.remove(0));
		}
		for (int i = 0; i < projectileList.size(); i++) {
			ProjectileV2.update(projectileList.get(i), this, delta);
			if ((boolean) projectileList.get(i)[5]) {
				projectileList.remove(i);
			}
		}
		for (int i = 0; i < bulletTrailList.size(); i++) {
			BulletTrail.update(bulletTrailList.get(i), this, delta);
			float fade = Float.parseFloat(bulletTrailList.get(i)[4].toString());
			if (fade >= 1) {
				bulletTrailList.remove(i);
			}
		}
	}

	public Random rand = new Random(System.nanoTime());

	public List<Object[]> getTowerList() {
		return towerList;
	}

	public List<Object[]> getProjectileList() {
		return projectileList;
	}

	public List<Object[]> getBulletTrailList() {
		return bulletTrailList;
	}

	public void spawn() {
		// int r = rand.nextInt(loadedLevel.spawningPoints.size());
		for (int r = 0; r < loadedLevel.spawningPoints.size(); r++) {
			float x = loadedLevel.spawningPoints.get(r).getCenterX() * 48;
			float y = loadedLevel.spawningPoints.get(r).getCenterY() * 48;
			Monster m = new Monster(this, x, y, 50 * (time / 100000f + 1));
			m.setSpeed(m.getSpeed() * (time / 100000f + 1));
			if (rand.nextInt(100) < 20 * (-8 / ((time / 1000f) + 8f) + 1)) {
				m.setSpeed(m.getSpeed() * 1.5f);
			}
			monsterList.add(m);
		}

	}

	// {posX, posY, angle, speed, damage, removeFlag}
	public boolean addProjectile(float x, float y, float angle, float speed, float damage) {
		projectilesToAdd.add(new Object[] { x, y, angle, speed, damage, false });
		// System.out.println(projectilesToAdd);
		if (projectileList.size() < 210) {
			return true;
		} else {
			System.err.println("Too many objects");
			return false;
		}
	}

	int lastTurret = 0;

	// {x, y, angle, charge, level, removeFlag, targetType, type, ID}
	public void addTower(float x, float y, float angle, float charge, float level, int targetType,
			TowerType towerType) {
		if (!doesTowerExist(x, y) && TowerDefense.money >= TowerV2.getCost(towerType)) {
			towerList.add(new Object[] { x, y, angle, charge, level, false, targetType, towerType, lastTurret });
			lastTurret++;
			TowerDefense.money -= TowerV2.getCost(towerType);
		}
	}

	// {startX, startY, endX, endY, fade, fadeRate, colour}
	public boolean addBulletTrail(float sx, float sy, float ex, float ey, float fr) {
		if (bulletTrailList.size() < 400) {
			bulletTrailList.add(new Object[] { sx, sy, ex, ey, 0.05f, fr });
			return true;
		} else {
			System.err.println("Too many objects");
			return false;
		}
	}

	private boolean doesTowerExist(float x, float y) {
		int smallx = (int) (x / 48);
		int smally = (int) (y / 48);
		for (Object[] obj : towerList) {
			int sx2 = (int) ((float) obj[0]) / 48;
			if (smallx != sx2)
				continue;
			int sy2 = (int) ((float) obj[1]) / 48;
			if (smally == sy2)
				return true;
		}
		return false;
	}

	private File smokeConfig;

	public void addSmoke(float x, float y) {// TODO: Pin to world
		try {
			ConfigurableEmitter emitter = ParticleIO.loadEmitter(smokeConfig);
			emitter.setPosition(x, y, false);
			ps.addEmitter(emitter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
