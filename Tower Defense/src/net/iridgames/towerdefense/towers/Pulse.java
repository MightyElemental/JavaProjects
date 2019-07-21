package net.iridgames.towerdefense.towers;

import org.newdawn.slick.geom.Circle;

import net.iridgames.towerdefense.Camera;
import net.iridgames.towerdefense.monsters.Monster;
import net.iridgames.towerdefense.world.World;

public class Pulse {

	private static float x, y, maxRad, curRad, damage;
	private static boolean delete;

	private static Circle hitbox = new Circle(0, 0, 5);

	private Pulse() {
	}

	// {posX, posY, maxRad, curRad, damage, remove}
	public static void update(Object[] data, World worldObj, int delta) {
		setTempVars(data);
		if (delete) {
			saveVarsToArray(data);
			return;
		}
		if (curRad >= maxRad) {
			for (Monster m : worldObj.monsterList) {
				if (hitbox.intersects(m)) {
					m.health -= damage;
				}
			}
			delete = true;
		}
		if (!delete && curRad < maxRad) {
			curRad += maxRad / 75f;
		}
		saveVarsToArray(data);
	}

	public static void setTempVars(Object[] data) {
		x = (float) data[0];
		y = (float) data[1];
		maxRad = Float.parseFloat(data[2].toString());
		curRad = Float.parseFloat(data[3].toString());
		hitbox.setRadius(curRad * Camera.tileSize);
		hitbox.setCenterX(x);
		hitbox.setCenterY(y);
		damage = Float.parseFloat(data[4].toString());
		delete = (boolean) data[5];
	}

	public static void saveVarsToArray(Object[] data) {
		data[0] = x;
		data[1] = y;
		data[2] = maxRad;
		data[3] = curRad;
		data[4] = damage;
		data[5] = delete;
		hitbox.setCenterX(0);
		hitbox.setCenterY(0);
		hitbox.setRadius(5);
	}

}
