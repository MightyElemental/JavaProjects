package net.iridgames.towerdefense.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.towerdefense.StateGame;
import net.iridgames.towerdefense.monsters.Monster;

public class World {
	
	
	public List<Level> levelList = new ArrayList<Level>();
	public List<Monster> monsterList = new ArrayList<Monster>();
	
	public Level loadedLevel;
	
	public World() {
		loadLevels();
		loadedLevel = levelList.get(0);
	}
	
	public void loadLevels() {
		for (int i = 0; i <= 5; i++) {
			levelList.add(new Level("level", i));
		}
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		for (int i = 0; i < monsterList.size(); i++) {
			monsterList.get(i).update(gc, sbg, delta);
			if (monsterList.get(i).dead) {
				monsterList.remove(i);
				break;
			}
		}
	}
	
	Random rand = new Random(System.nanoTime());
	
	public void spawn() {
		int r = rand.nextInt(loadedLevel.spawningPoints.size());
		// for (int i = 0; i < loadedLevel.spawningPoints.size(); i++) {
		float x = loadedLevel.spawningPoints.get(r).getCenterX() * StateGame.tileSize;
		float y = loadedLevel.spawningPoints.get(r).getCenterY() * StateGame.tileSize;
		monsterList.add(new Monster(this, x, y));
		// }
		
	}
	
}
