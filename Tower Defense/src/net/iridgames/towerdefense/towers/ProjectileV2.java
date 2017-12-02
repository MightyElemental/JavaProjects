package net.iridgames.towerdefense.towers;

import org.newdawn.slick.geom.Circle;

import net.iridgames.towerdefense.StateGame;
import net.iridgames.towerdefense.monsters.Monster;
import net.iridgames.towerdefense.world.World;

public class ProjectileV2 {

	private static float	x, y, angle, speed, damage;
	private static boolean	delete;
	private static Circle	hitbox	= new Circle(0, 0, 5);

	// {posX, posY, angle, speed, damage, remove}
	public static void update(Object[] data, World worldObj, int delta) {
		setTempVars(data);

		if ( x > worldObj.loadedLevel.width * StateGame.tileSize || x < 0 ) delete = true;
		if ( y > worldObj.loadedLevel.height * StateGame.tileSize || y < 0 ) delete = true;
		if ( delete ) {
			saveVarsToArray(data);
			return;
		}
		for ( Monster m : worldObj.monsterList ) {
			if ( hitbox.intersects(m) ) {
				m.health -= damage;
				delete = true;
				x = -1000;
				y = -1000;
				break;
			}
		}
		if ( !delete ) {
			x += Math.cos(Math.toRadians(angle)) * speed * (delta / 9f);
			y += Math.sin(Math.toRadians(angle)) * speed * (delta / 9f);
		}

		saveVarsToArray(data);
	}

	public static void setTempVars(Object[] data) {
		x = (float) data[0];
		y = (float) data[1];
		hitbox.setCenterX(x);
		hitbox.setCenterY(y);
		angle = Float.parseFloat(data[2].toString());
		speed = Float.parseFloat(data[3].toString());
		damage = Float.parseFloat(data[4].toString());
		delete = (boolean) data[5];
	}

	public static void saveVarsToArray(Object[] data) {
		data[0] = x;
		data[1] = y;
		data[2] = angle;
		data[3] = speed;
		data[4] = damage;
		data[5] = delete;
		hitbox.setCenterX(0);
		hitbox.setCenterY(0);
	}

}
