package net.iridgames.towerdefense;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.towerdefense.world.World;

public class Camera {

	public static float xOffset, yOffset, scale = 40f / 48f;

	public static float tileSize = 48;

	public static void init(GameContainer gc, StateBasedGame sbg, World worldObj) {
		tileSize *= scale;
		xOffset = gc.getWidth() / 2 - (worldObj.loadedLevel.width * tileSize) / 2;
		yOffset = gc.getHeight() / 2 - (worldObj.loadedLevel.height * tileSize) / 2;
	}

	public static void mouseDragged(int oldx, int oldy, int x, int y) {
		// worldObj.loadedLevel.setTile((x - startingPointX) / tileSize, (y -
		// startingPointY) / tileSize,
		// charList[selectedChar]);
	}

}
