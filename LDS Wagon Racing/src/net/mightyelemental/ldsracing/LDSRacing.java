package net.mightyelemental.ldsracing;

import java.awt.Font;
import java.io.InputStream;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

/** @author MightyElemental */
public class LDSRacing extends StateBasedGame {

	public static final String	GAME_VERSION		= "0.0.1";
	public static final String	GAME_NAME			= "LDS Wagon Racing";
	public static final String	GAME_NAME_DISPLAY	= GAME_NAME + " | Version " + GAME_VERSION;

	public static final int	STATE_MENU		= 0;
	public static final int	STATE_PLAY		= 1;
	public static final int	STATE_CREATE	= 2;

	public static PlayState playState = new PlayState(STATE_PLAY);

	public static ResourceLoader	resLoader	= new ResourceLoader();
	public static Image				NULL_IMAGE;

	public static final float[][]	commonRatios		= { { 16, 9 }, { 5, 4 }, { 4, 3 } };
	public static final int[][]		commonResolutions	= { { 1280, 1600, 1920, 2048 }, { 1280, 1024 },
			{ 800, 1024, 1152, 1280, 1400, 1600 } };

	public static int		width;
	public static float[]	aspectRatio;
	public static int		maxFPS;
	public static boolean	fullscreen;
	public static boolean	showFPS;
	public static boolean	vsync;

	public LDSRacing( String name ) {
		super(name);
		this.addState(new MenuState(STATE_MENU));
		this.addState(playState);
		this.addState(new CreateState(STATE_CREATE));
	}

	public static void main(String[] args) {
		StartupSettings settingMenu = new StartupSettings();

		settingMenu.startUp();
	}

	public static TrueTypeFont font;

	public static void startGame() {
		AppGameContainer appGc;

		try {
			appGc = new AppGameContainer(new LDSRacing(GAME_NAME_DISPLAY));
			appGc.setDisplayMode(width, (int) (width / aspectRatio[0] * aspectRatio[1]), fullscreen);
			appGc.setTargetFrameRate(maxFPS);
			appGc.setMaximumLogicUpdateInterval(30);
			appGc.setMinimumLogicUpdateInterval(15);
			appGc.setShowFPS(showFPS);
			appGc.setVSync(vsync);
			appGc.setAlwaysRender(true);
			appGc.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(STATE_MENU).init(gc, this);
		this.getState(STATE_PLAY).init(gc, this);
		NULL_IMAGE = resLoader.loadImage("noImage");

		try {
			InputStream inputStream = org.newdawn.slick.util.ResourceLoader.getResourceAsStream("res/assets/fonts/arial.ttf");
			Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont2 = awtFont2.deriveFont(20f); // set font size
			font = new TrueTypeFont(awtFont2, true);
			// System.out.println("Created new font size ("+i+")");
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.enterState(STATE_MENU);
	}

}
