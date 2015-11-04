package net.iridgames.munchkin;

import java.awt.Font;
import java.io.InputStream;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.munchkin.states.StateGame;
import net.iridgames.munchkin.states.StateProfile;
import net.iridgames.munchkin.states.menu.StateMenu;

public class Munchkin extends StateBasedGame {

	public static final String	GAME_NAME	= "Muchkin";
	public static final String	VERSION		= "0.0.0";
	public static final String	TITLE		= GAME_NAME + " | v" + VERSION;
	public static final int		WIDTH		= 1600;

	public static final int	STATE_MENU		= 0;
	public static final int	STATE_GAME		= 1;
	public static final int	STATE_PROFILE	= 2;

	public static Image NULL_IMAGE;

	public static ResourceLoader loader = new ResourceLoader();

	public static TrueTypeFont font;

	public StateGame stateGame = new StateGame(STATE_GAME);

	public Munchkin( String name ) {
		super(name);
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
			appGc.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		NULL_IMAGE = loader.loadImage("noImage");
		try {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("assets/monkey.ttf");
			Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont2 = awtFont2.deriveFont(20f); // set font size
			font = new TrueTypeFont(awtFont2, true);
			// System.out.println("Created new font size ("+i+")");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.getState(STATE_MENU).init(gc, this);
		this.getState(STATE_GAME).init(gc, this);
		this.getState(STATE_PROFILE).init(gc, this);
		this.enterState(STATE_MENU);
	}

}
