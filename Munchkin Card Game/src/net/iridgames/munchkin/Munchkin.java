package net.iridgames.munchkin;

import java.awt.Font;
import java.io.InputStream;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.munchkin.gui.GUIListenerHandler;
import net.iridgames.munchkin.states.StateGame;
import net.iridgames.munchkin.states.StateLoading;
import net.iridgames.munchkin.states.StateProfile;
import net.iridgames.munchkin.states.menu.StateMenu;

public class Munchkin extends StateBasedGame {

	public static final String	GAME_NAME	= "Muchkin";
	public static final String	VERSION		= "0.0.0";
	public static final String	TITLE		= GAME_NAME + " | v" + VERSION;
	public static final int		WIDTH		= 1600;

	public static final int	STATE_PRE_LOAD	= 0;
	public static final int	STATE_MENU		= 1;
	public static final int	STATE_GAME		= 2;
	public static final int	STATE_PROFILE	= 3;

	public static GUIListenerHandler buttonHandler = new GUIListenerHandler();

	public static Image NULL_IMAGE;

	public static ResourceLoader loader = new ResourceLoader();

	public static TrueTypeFont font;

	public static boolean fullLoaded = false;

	public StateGame stateGame = new StateGame(STATE_GAME);

	public Munchkin( String name ) {
		super(name);
		addState(new StateLoading(STATE_PRE_LOAD));
		addState(new StateMenu(STATE_MENU));
		addState(stateGame);
		addState(new StateProfile(STATE_PROFILE));
	}

	public static void main(String[] settings) {
		new Munchkin(TITLE);
		setupClient();
	}

	private static void setupClient() {

		AppGameContainer appGc;
		try {
			appGc = new AppGameContainer(new Munchkin(TITLE));
			appGc.setDisplayMode(WIDTH, (int) (WIDTH / 16.0 * 9.0), false);
			appGc.setTargetFrameRate(60);
			appGc.setShowFPS(false);
			appGc.setAlwaysRender(true);
			appGc.setUpdateOnlyWhenVisible(false);
			appGc.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private Thread loadThread = new Thread() {

		public void run() {
			// mainMenuSong = resLoader.loadMusic("MainMenu");
			// normalGameSong = resLoader.loadMusic("NormalGame");
			fullLoaded = true;
			try {
				loadThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	private void loadAxeCop() {
		String[] doors = { "lev1-duck", "class-cop", "blood-on-you", "curse-cherry-rainbow", "lev10-candycandy", "lev20-axe-cop",
				"power-axe", "curse-bad-guys-wished", "lev1-annoying-singing-tree", "lev6-lamp-that-comes-alive" };
		String[] treas = { "ally-hand-cuff-man", "bonus-visit-weapon-store", "item-blade-gun", "item-flute", "levelup-dance" };
		for (int i = 0; i < doors.length; i++) {
			loader.loadImage("cardVers.axeCop.door." + doors[i]);
		}
		for (int i = 0; i < treas.length; i++) {
			loader.loadImage("cardVers.axeCop.treasure." + treas[i]);
		}
	}

	private void loadMiscImages() {
		String[] misc = { "IridiumGames", "menu.axe-cop", "menu.munchkin-logo", "menu.munchkin-original" };
		for (int i = 0; i < misc.length; i++) {
			loader.loadImage(misc[i]);
		}
	}

	private void loadImages() {
		loadAxeCop();
		loadMiscImages();
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		NULL_IMAGE = loader.loadImage("noImage");
		if (NULL_IMAGE == null) {
			gc.exit();
			try {
				throw new GameBreakException(
						"Munchkin.NULL_IMAGE is null!\nThis could cause a lot of issues, especially if the game is missing textures!");
			} catch (GameBreakException e) {
				e.printStackTrace();
			}
		}
		try {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("assets/monkey.ttf");
			Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont2 = awtFont2.deriveFont(20f); // set font size
			font = new TrueTypeFont(awtFont2, true);
			// System.out.println("Created new font size ("+i+")");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.getState(STATE_PROFILE).init(gc, this);
		this.getState(STATE_GAME).init(gc, this);
		this.getState(STATE_MENU).init(gc, this);
		this.enterState(STATE_PRE_LOAD);
		loadThread.start();
		loadImages();
		// this.getState(STATE_MENU).init(gc, this);
	}

}
