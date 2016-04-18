package net.mightyelemental.mowergame;

import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.states.GameState;
import net.mightyelemental.mowergame.states.LoadState;
import net.mightyelemental.mowergame.states.MenuState;
import net.mightyelemental.mowergame.states.ShopState;

public class MowerGame extends StateBasedGame {

	public static ResourceLoader resLoader = new ResourceLoader();
	public static Random rand = new Random();

	public static final int STATE_GAME = 0;
	public static final int STATE_MENU = 1;
	public static final int STATE_SHOP = 2;
	public static final int STATE_LOAD = 3;

	public GameState gameState = new GameState(STATE_GAME, rand);
	public MenuState menuState = new MenuState(STATE_MENU);
	public ShopState shopState = new ShopState(STATE_SHOP);
	public LoadState loadState = new LoadState(STATE_LOAD);

	public static final String TITLE = "GCSE Mower Game";

	public static final int WIDTH = 1280;

	public static Image NULL_IMAGE;
	public static boolean fullLoaded = true;

	public MowerGame(String name) {
		super(name);

		for (int i = 0; i <= 1312; i++) {
			if (i / 16.0 * 9.0 - Math.round(i / 16.0 * 9.0) == 0) {
				System.out.println("Width: " + i + " Height:" + (i / 16.0 * 9.0));
			}
		}

		addState(loadState);
		addState(gameState);
		addState(menuState);
		addState(shopState);

		AppGameContainer appGc;
		try {
			appGc = new AppGameContainer(this);
			appGc.setDisplayMode(WIDTH, (int) (WIDTH / 16.0 * 9.0), false);
			appGc.setTargetFrameRate(60);
			appGc.setShowFPS(false);
			appGc.setAlwaysRender(true);
			appGc.setFullscreen(true);
			appGc.setShowFPS(true);
			appGc.start();
		} catch (SlickException e) {
			e.printStackTrace();
			System.exit(1);
		}
		NULL_IMAGE = resLoader.loadImage("noImage");
	}

	public static void main(String[] args) {
		new MowerGame(TITLE);
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.enterState(STATE_GAME);
	}

}
