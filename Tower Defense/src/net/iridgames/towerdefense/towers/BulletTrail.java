package net.iridgames.towerdefense.towers;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import net.iridgames.towerdefense.Camera;
import net.iridgames.towerdefense.world.World;

public class BulletTrail {

	private static float startX, startY, endX, endY, fade, fadeRate;

	// {startX, startY, endX, endY, fade, fadeRate}
	public static void update(Object[] data, World worldObj, int delta) {
		setTempVars(data);
		fade += delta / 1000f * fadeRate;
		saveVarsToArray(data);
	}

	public static void render(Graphics g, Object[] data, float xoffset, float yoffset) {
		setTempVars(data);
		g.setColor(new Color(0.2f, 0.2f, 0.2f, (1 - fade)));
		g.setLineWidth(5f * fade);
		g.drawLine(startX * Camera.scale + xoffset, startY * Camera.scale + yoffset, endX * Camera.scale + xoffset,
				endY * Camera.scale + yoffset);

		g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.9f * (1 - fade * 1.2f)));
		float size = 10f * (fade * 2f + 0.1f);
		g.fillOval(endX * Camera.scale + xoffset - size * Camera.scale / 2,
				endY * Camera.scale + yoffset - size * Camera.scale / 2, size * Camera.scale, size * Camera.scale);
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
