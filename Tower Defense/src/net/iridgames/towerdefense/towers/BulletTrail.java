package net.iridgames.towerdefense.towers;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import net.iridgames.towerdefense.world.World;

public class BulletTrail {

	private static float	startX, startY, endX, endY, fade, fadeRate;
	private static Color	col;

	// {startX, startY, endX, endY, fade, fadeRate, colour}
	public static void update(Object[] data, World worldObj, int delta) {
		setTempVars(data);
		fade += delta / 1000f * fadeRate;
		saveVarsToArray(data);
	}

	public static void render(Graphics g, Object[] data, int xoffset, int yoffset) {
		setTempVars(data);
		g.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f * (1 - fade)));
		g.setLineWidth(5);
		g.drawLine(startX + xoffset, startY + yoffset, endX + xoffset, endY + yoffset);
		g.setColor(new Color(0f, 0f, 0f, (1 - fade)));
		g.setLineWidth(1);
		g.drawLine(startX + xoffset, startY + yoffset, endX + xoffset, endY + yoffset);
	}

	public static void setTempVars(Object[] data) {
		startX = Float.parseFloat(data[0].toString());
		startY = Float.parseFloat(data[1].toString());
		endX = Float.parseFloat(data[2].toString());
		endY = Float.parseFloat(data[3].toString());
		fade = Float.parseFloat(data[4].toString());
		fadeRate = Float.parseFloat(data[5].toString());
	}

	public static void saveVarsToArray(Object[] data) {
		data[0] = startX;
		data[1] = startY;
		data[2] = endX;
		data[3] = endY;
		data[4] = fade;
		data[5] = fadeRate;
	}

}
