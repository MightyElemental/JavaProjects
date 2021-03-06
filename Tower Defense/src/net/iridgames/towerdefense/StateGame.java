package net.iridgames.towerdefense;

import java.io.IOException;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.towerdefense.towers.BulletTrail;
import net.iridgames.towerdefense.towers.TowerType;
import net.iridgames.towerdefense.towers.TowerV2;
import net.iridgames.towerdefense.world.World;

public class StateGame extends BasicGameState {

	public char[] charList = { '-', 'x', 'u', 's', 'g' };
	public int selectedChar = 0;

	public StateGame(World wobj) {
		worldObj = wobj;
	}

	public Image levelImg;
	private Graphics gLevelImg;

	public World worldObj;

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		Camera.init(gc, sbg, worldObj);
		String[] imgs = { "grass", "path", "base", "turret", "gatling", "sniper", "louis" };
		ResourceLoader.loadImageBatch(imgs);
		initLevelImg(gc);
		worldObj.init();
	}

	private void initLevelImg(GameContainer gc) throws SlickException {
		levelImg = new Image(gc.getWidth(), gc.getHeight());
		gLevelImg = levelImg.getGraphics();
		renderTiles(gc, gLevelImg);
		gLevelImg.destroy();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.scale(Camera.scale, Camera.scale);
		g.translate(Camera.xOffset, Camera.yOffset);
		// levelImg.getScaledCopy(Camera.scale).draw(Camera.xOffset, Camera.yOffset);
		renderTiles(gc, g);
		// levelImg.draw();
		renderMonsters(gc, sbg, g);
		renderTowers(gc, sbg, g);
		renderProjectiles(gc, sbg, g);
		renderBulletTrails(gc, sbg, g);
		renderPulses(gc, sbg, g);
		worldObj.renderSmoke(gc, sbg, g);
		renderWorldEditSym(gc, sbg, g);
		// System.out.println(Camera.yOffset + 100 * Camera.scale);
		g.resetTransform();
		g.setColor(Color.white);
		String text = "Money: $" + TowerDefense.money;
		float length = g.getFont().getWidth(text);
		g.fillRect(70 * Camera.scale - 5, 290 * Camera.scale - 5, length + 10, 22);
		g.setColor(Color.black);
		g.drawString(text, 70 * Camera.scale, 290 * Camera.scale);
		if (TowerDefense.money < 0) {
			g.setColor(new Color(0, 0, 0, 0.9f));
			g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
			g.setColor(Color.white);
			String words = "YOU DIED";
			String words2 = "YOU SURVIVED " + worldObj.time / 1000 + " SECONDS";
			g.drawString(words, gc.getWidth() / 2 - g.getFont().getWidth(words) - 200, gc.getHeight() / 2 + 100);
			g.drawString(words2, gc.getWidth() / 2 - g.getFont().getWidth(words2) / 2 - 200, gc.getHeight() / 2 + 130);
		}
	}

	private void renderWorldEditSym(GameContainer gc, StateBasedGame sbg, Graphics g) {
		changeColor(g, charList[selectedChar]);
		g.fillRect(gc.getWidth() - 50, 100, 25, 25);
		g.setColor(Color.black);
		g.drawString("" + charList[selectedChar], gc.getWidth() - 45, 105);
	}

	private void renderTowers(GameContainer gc, StateBasedGame sbg, Graphics g) {
		for (Object[] obj : worldObj.getTowerList()) {
			// TODO create renderer
			// worldObj.getTowerList().get(i).draw(gc, sbg, g,
			// startingPointX,startingPointY);
			TowerV2.render(g, obj);
		}
	}

	private void renderBulletTrails(GameContainer gc, StateBasedGame sbg, Graphics g) {
		for (int i = 0; i < worldObj.getBulletTrailList().size(); i++) {
			BulletTrail.render(g, worldObj.getBulletTrailList().get(i));
		}
	}

	private void renderProjectiles(GameContainer gc, StateBasedGame sbg, Graphics g) {
		Circle c = new Circle(0, 0, 5);
		for (int i = 0; i < worldObj.getProjectileList().size(); i++) {
			float x = (float) worldObj.getProjectileList().get(i)[0];
			float y = (float) worldObj.getProjectileList().get(i)[1];
			c.setCenterX(x);
			c.setCenterY(y);
			g.fill(c);
		}
	}

	private void renderPulses(GameContainer gc, StateBasedGame sbg, Graphics g) {
		g.setColor(new Color(0.5f, 0.2f, 0.9f, 0.5f));
		Circle rad = new Circle(0,0,5);
		for(int i = 0; i < worldObj.getPulseList().size(); i++) {
			float x = (float)worldObj.getPulseList().get(i)[0];
			float y = (float)worldObj.getPulseList().get(i)[1];
			float r = Float.parseFloat(worldObj.getPulseList().get(i)[3].toString());
			rad.setRadius(r*Camera.tileSize);
			rad.setCenterX(x);
			rad.setCenterY(y);
			g.fill(rad);
		}
	}

	private void renderMonsters(GameContainer gc, StateBasedGame sbg, Graphics g) {
		// g.setColor(Color.red.darker());
		for (int i = 0; i < worldObj.monsterList.size(); i++) {
			// Monster m = worldObj.monsterList.get(i);
			// List<Point> n = m.route;
			// g.fillRect(startingPointX + m.getX(), startingPointY + m.getY(), 36, 42);
			// for (int j = 0; j < n.size(); j++) {
			// if (n.get(j) != null) g.fillOval(startingPointX + n.get(j).getX() - 5,
			// startingPointY + n.get(j).getY() -
			// 5, 10, 10);
			// }
			// for (int x = 0; x < m.mark.length; x++) {
			// for (int y = 0; y < m.mark[x].length; y++) {
			// g.drawString(m.mark[x][y] + "", startingPointX + x * tileSize, startingPointY
			// + y * tileSize);
			// }
			// }
			worldObj.monsterList.get(i).draw(gc, sbg, g);
		}
	}

	private void renderTiles(GameContainer gc, Graphics g) {
		g.setLineWidth(1);
		for (int y = 0; y < worldObj.loadedLevel.levelLayout.size(); y++) {
			for (int x = 0; x < worldObj.loadedLevel.levelLayout.get(y).size(); x++) {
				changeColor(g, x, y);
				g.fillRect(x * Camera.tileSize, y * Camera.tileSize, Camera.tileSize, Camera.tileSize);
				g.setColor(new Color(0f, 0f, 0f, 0.5f));
				g.drawRect(x * Camera.tileSize, y * Camera.tileSize, Camera.tileSize, Camera.tileSize);
			}
		}
	}

	public void changeColor(Graphics g, int x, int y) {
		switch (worldObj.loadedLevel.getTile(x, y)) {
		case '-':// path
			g.setColor(new Color(0f, 0f, 0f, 0f));
			g.drawImage(ResourceLoader.loadImage("path"), x * 48, y * 48);
			break;
		case 'u':// usable to player
			g.setColor(new Color(0f, 0f, 0f, 0f));
			g.drawImage(ResourceLoader.loadImage("base"), x * 48, y * 48);
			break;
		case 'x':// scenary
			g.setColor(new Color(0f, 0f, 0f, 0f));
			g.drawImage(ResourceLoader.loadImage("grass"), x * 48, y * 48);
			break;
		case 's':// spawn
			g.setColor(Color.gray.darker());
			break;
		case 'g':// goal
			g.drawImage(ResourceLoader.loadImage("turret"), x * 48, y * 48);
			g.setColor(new Color(0f, 0f, 0f, 0f));
			break;
		default:
			g.drawImage(ResourceLoader.loadImage("null"), Camera.xOffset + x * 48, Camera.yOffset + y * 48);
			break;
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

	int ticks = 0;

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (TowerDefense.money >= 0) {
			// worldObj.loadedLevel = worldObj.levelList.get(selectedChar % 4);
			ticks++;
			// if ( ticks % 2 == 0 ) {
			worldObj.update(gc, sbg, delta);
			// }
		}
	}

	@Override
	public int getID() {
		return 0;
	}

	@Override
	public void mouseWheelMoved(int newValue) {
		super.mouseWheelMoved(newValue);
		selectedChar += Math.signum(newValue);
		if (selectedChar < 0)
			selectedChar += charList.length;
		selectedChar = selectedChar % (charList.length);
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int x, int y) {
		// worldObj.loadedLevel.setTile((x - startingPointX) / tileSize, (y -
		// startingPointY) / tileSize,
		// charList[selectedChar]);
		Camera.mouseDragged(oldx, oldy, x, y);
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		int newx = (int) ((x - Camera.xOffset) / Camera.tileSize);
		int newy = (int) ((y - Camera.yOffset) / Camera.tileSize);
		// if (!TowerDefense.isCtrlDown) {
		// worldObj.loadedLevel.setTile(newx, newy, charList[selectedChar]);
		// }
		// System.out.println(newx + "|" + newy);

		char c = worldObj.loadedLevel.getTile(newx, newy);
		if (!TowerDefense.isCtrlDown) {
			if (c == 'u') {
				if (button == 0) {
					worldObj.addTower(newx * 48, newy * 48, 0, 0, 0, TowerV2.TARGET_CLOSEST_TO_TURRET, TowerType.PULSE);
				} else if (button == 1) {
					worldObj.addTower(newx * 48, newy * 48, 0, 0, 0, TowerV2.TARGET_CLOSEST, TowerType.GATLING);
				}
			} else {
				worldObj.addTower(newx * 48, newy * 48, 0, 0, 0, TowerV2.TARGET_LEAST_HEALTH, TowerType.LOUIS);
			}
		}

	}

	Random rand = new Random(System.nanoTime());

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
		if (key == Input.KEY_LCONTROL || key == Input.KEY_RCONTROL) {
			TowerDefense.isCtrlDown = true;
		}
		super.keyPressed(key, c);
	}

	@Override
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_LCONTROL || key == Input.KEY_RCONTROL) {
			TowerDefense.isCtrlDown = false;
		}
	}

}
