package net.iridgames.towerdefense;

import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.towerdefense.towers.TowerCannon;
import net.iridgames.towerdefense.world.World;

public class StateGame extends BasicGameState {
	
	
	public char[] charList = { '-', 'x', 'u', 's', 'g' };
	public int selectedChar = 0;
	
	public StateGame( World wobj ) {
		worldObj = wobj;
	}
	
	public World worldObj;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		renderTiles(gc, sbg, g);
		changeColor(g, charList[selectedChar]);
		g.fillRect(gc.getWidth() - 50, 100, 25, 25);
		g.setColor(Color.black);
		g.drawString("" + charList[selectedChar], gc.getWidth() - 45, 105);
		renderMonsters(gc, sbg, g);
		renderTowers(gc, sbg, g);
		renderProjectiles(gc, sbg, g);
	}
	
	private void renderTowers(GameContainer gc, StateBasedGame sbg, Graphics g) {
		for (int i = 0; i < worldObj.towerList.size(); i++) {
			worldObj.towerList.get(i).draw(gc, sbg, g, startingPointX, startingPointY);
		}
	}
	
	private void renderProjectiles(GameContainer gc, StateBasedGame sbg, Graphics g) {
		for (int i = 0; i < worldObj.projectileList.size(); i++) {
			worldObj.projectileList.get(i).draw(gc, sbg, g, startingPointX, startingPointY);
		}
	}
	
	public static final int tileSize = 48;
	public int startingPointX = 5;
	public int startingPointY = 5;
	
	public void changeColor(Graphics g, int x, int y) {
		switch (worldObj.loadedLevel.getTile(x, y)) {
			case '-':// path
				g.setColor(new Color(0f, 0f, 0f, 0f));
				g.drawImage(ResourceLoader.loadImage("path"), startingPointX + x * tileSize, startingPointY + y * tileSize);
				break;
			case 'u':// usable to player
				g.setColor(new Color(0f, 0f, 0f, 0f));
				g.drawImage(ResourceLoader.loadImage("base"), startingPointX + x * tileSize, startingPointY + y * tileSize);
				break;
			case 'x':// scenary
				g.setColor(new Color(0f, 0f, 0f, 0f));
				g.drawImage(ResourceLoader.loadImage("grass"), startingPointX + x * tileSize, startingPointY + y * tileSize);
				break;
			case 's':// spawn
				g.setColor(Color.gray.darker());
				break;
			case 'g':// goal
				g.drawImage(ResourceLoader.loadImage("turret"), startingPointX + x * tileSize, startingPointY + y * tileSize);
				g.setColor(new Color(0f, 0f, 0f, 0f));
				break;
			default:
				g.setColor(Color.white);
		}
	}
	
	public void changeColor(Graphics g, char val) {
		switch (val) {
			case '-':// path
				g.setColor(Color.gray);
				break;
			case 'u':// usable to player
				g.setColor(Color.blue);
				break;
			case 'x':// scenary
				g.setColor(Color.green);
				break;
			case 's':// spawn
				g.setColor(Color.gray.darker());
				break;
			case 'g':// goal
				g.setColor(Color.red);
				break;
			default:
				g.setColor(Color.white);
		}
	}
	
	public void renderTiles(GameContainer gc, StateBasedGame sbg, Graphics g) {
		startingPointX = gc.getWidth() / 2 - (worldObj.loadedLevel.width * tileSize) / 2;
		startingPointY = gc.getHeight() / 2 - (worldObj.loadedLevel.height * tileSize) / 2;
		for (int y = 0; y < worldObj.loadedLevel.levelLayout.size(); y++) {
			for (int x = 0; x < worldObj.loadedLevel.levelLayout.get(y).size(); x++) {
				changeColor(g, x, y);
				g.fillRect(startingPointX + x * tileSize, startingPointY + y * tileSize, tileSize, tileSize);
				g.setColor(Color.black);
				g.drawRect(startingPointX + x * tileSize, startingPointY + y * tileSize, tileSize, tileSize);
			}
		}
	}
	
	public void renderMonsters(GameContainer gc, StateBasedGame sbg, Graphics g) {
		// g.setColor(Color.red.darker());
		for (int i = 0; i < worldObj.monsterList.size(); i++) {
			// Monster m = worldObj.monsterList.get(i);
			// List<Point> n = m.route;
			// g.fillRect(startingPointX + m.getX(), startingPointY + m.getY(), 36, 42);
			// for (int j = 0; j < n.size(); j++) {
			// if (n.get(j) != null) g.fillOval(startingPointX + n.get(j).getX() - 5, startingPointY + n.get(j).getY() -
			// 5, 10, 10);
			// }
			// for (int x = 0; x < m.mark.length; x++) {
			// for (int y = 0; y < m.mark[x].length; y++) {
			// g.drawString(m.mark[x][y] + "", startingPointX + x * tileSize, startingPointY + y * tileSize);
			// }
			// }
			worldObj.monsterList.get(i).draw(gc, sbg, g, startingPointX, startingPointY);
		}
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// worldObj.loadedLevel = worldObj.levelList.get(selectedChar % 4);
		worldObj.update(gc, sbg, delta);
	}
	
	@Override
	public int getID() {
		return 0;
	}
	
	@Override
	public void mouseWheelMoved(int newValue) {
		super.mouseWheelMoved(newValue);
		selectedChar += Math.signum(newValue);
		if (selectedChar < 0) selectedChar += charList.length;
		selectedChar = selectedChar % (charList.length);
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int x, int y) {
		// worldObj.loadedLevel.setTile((x - startingPointX) / tileSize, (y - startingPointY) / tileSize,
		// charList[selectedChar]);
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		// worldObj.loadedLevel.setTile((x - startingPointX) / tileSize, (y - startingPointY) / tileSize,
		// charList[selectedChar]);
		worldObj.towerList.add(new TowerCannon(worldObj, (x - startingPointX) / tileSize, (y - startingPointY) / tileSize));
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_S) {
			try {
				worldObj.loadedLevel.saveLevelToFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (key == Input.KEY_R) {
			worldObj.loadedLevel.clear();
		}
		if (key == Input.KEY_SPACE) {
			worldObj.spawn();
		}
		super.keyPressed(key, c);
	}
	
}
