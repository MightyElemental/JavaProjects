package net.mightyelemental.mowergame;

import java.io.File;
import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.gui.GUIListenerHandler;
import net.mightyelemental.mowergame.states.GameState;
import net.mightyelemental.mowergame.states.LoadState;
import net.mightyelemental.mowergame.states.MenuState;
import net.mightyelemental.mowergame.states.shop.ShopState;

public class MowerGame extends StateBasedGame {
	
	
	public static Image NULL_IMAGE;
	public static ResourceLoader resLoader = new ResourceLoader();
	public static Random rand = new Random();
	
	public static GUIListenerHandler buttonHandler = new GUIListenerHandler();
	
	public static float money = 0f;
	
	public static final int STATE_GAME = 0;
	public static final int STATE_MENU = 1;
	public static final int STATE_SHOP = 2;
	public static final int STATE_LOAD = 3;
	
	public LoadState loadState = new LoadState(STATE_LOAD);
	public static GameState gameState = new GameState(STATE_GAME, rand);
	public MenuState menuState = new MenuState(STATE_MENU, rand);
	public static ShopState shopState = new ShopState(STATE_SHOP);
	
	public static final String TITLE = "Mowve Mania";
	
	public static final int WIDTH = 1280;
	
	public static boolean fullLoaded = true;
	
	public MowerGame( String name ) {
		super(name);
		
		// for (int i = 0; i <= 1312; i++) {
		// if (i / 16.0 * 9.0 - Math.round(i / 16.0 * 9.0) == 0) {
		// System.out.println("Width: " + i + " Height:" + (i / 16.0 * 9.0));
		// }
		// }
		
		addState(loadState);
		addState(menuState);
		addState(shopState);
		addState(gameState);
		
		AppGameContainer appGc;
		try {
			appGc = new AppGameContainer(this);
			appGc.setDisplayMode(WIDTH, (int) (WIDTH / 16.0 * 9.0), false);
			appGc.setTargetFrameRate(120);
			appGc.setShowFPS(false);
			appGc.setAlwaysRender(true);
			appGc.setFullscreen(false);
			appGc.setIcons(new String[] { "./assets/textures/logo32.png" });
			appGc.setShowFPS(true);
			appGc.start();
		} catch (SlickException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		NULL_IMAGE = resLoader.loadImage("null");
		
	}
	
	public static void main(String[] args) {
		System.setProperty("java.library.path", "lib");
		String os = System.getProperty("os.name").toLowerCase();
		String path = "windows";
		if (os.contains("mac")) {
			path = "macosx";
		} else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
			path = "linux"; 
		} else if (os.contains("sunos")) {
			path = "solaris";
		}
		System.setProperty("org.lwjgl.librarypath", new File("lib/natives/" + path).getAbsolutePath());
		new MowerGame(TITLE);
	}
	
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.enterState(STATE_LOAD);
	}
	
}
