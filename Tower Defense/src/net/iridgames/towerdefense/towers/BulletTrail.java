package net.iridgames.towerdefense.towers;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import net.iridgames.towerdefense.world.World;

public class BulletTrail {

	private static float startX, startY, endX, endY, fade, fadeRate;

	// {startX, startY, endX, endY, fade, fadeRate}
	public static void update(Object[] data, World worldObj, int delta) {
		setTempVars(data);
		fade += delta / 1000f * fadeRate;
		saveVarsToArray(data);
	}

	public static void render(Graphics g, Object[] data, int xoffset, int yoffset) {
		setTempVars(data);
		g.setColor(new Color(0.2f, 0.2f, 0.2f, (1 - fade)));
		g.setLineWidth(5f * fade);
		g.drawLine(startX + xoffset, startY + yoffset, endX + xoffset, endY + yoffset);

		g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.9f * (1 - fade*1.2f)));
		float size = 10f * (fade * 2f + 0.1f);
		g.fillOval(endX + xoffset - size / 2, endY + yoffset - size / 2, size, size);
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
