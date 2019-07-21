package net.iridgames.towerdefense;

import java.io.File;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.towerdefense.world.World;

public class TowerDefense extends StateBasedGame {

	public static World		world	= new World();
	public static StateGame	sGame	= new StateGame(world);

	public static boolean isCtrlDown;

	public static int money = 450;

	public TowerDefense(String title) {
		super(title);
		AppGameContainer appGc;
		try {
			appGc = new AppGameContainer(this);
			appGc.setDisplayMode(1366, (int) (1366/16.0f*9.0f), false);
			appGc.setTargetFrameRate(120);
			appGc.setShowFPS(false);
			appGc.setAlwaysRender(true);
			appGc.setFullscreen(true);
			appGc.setShowFPS(false);
			appGc.start();
		} catch (SlickException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		System.setProperty("java.library.path", "lib");
		String os = System.getProperty("os.name").toLowerCase();
		String path = "windows";
		if ( os.contains("mac") ) {
			path = "macosx";
		} else if ( os.contains("nix") || os.contains("nux") || os.contains("aix") ) {
			path = "linux";
		} else if ( os.contains("sunos") ) {
			path = "solaris";
		}
		System.setProperty("org.lwjgl.librarypath", new File("lib/natives/" + path).getAbsolutePath());
		new TowerDefense("Generic Tower Defense");
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.addState(sGame);
	}

}
