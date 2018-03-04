package net.mightyelemental.winGame;

import java.io.File;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.states.StateDesktop;
import net.mightyelemental.winGame.states.StateLoading;
import net.mightyelemental.winGame.states.StateLogin;

public class WindowsMain extends StateBasedGame {

	public WindowsMain() {
		super("WinGameXP");
		this.addState(loadState);
		this.addState(loginState);
		this.addState(desktopState);

		AppGameContainer appGc;
		try {
			appGc = new AppGameContainer(this);
			appGc.setDisplayMode(WIDTH, (int) (WIDTH / 16.0 * 9.0), false);
			appGc.setTargetFrameRate(120);
			appGc.setShowFPS(false);
			appGc.setAlwaysRender(true);
			appGc.setFullscreen(false);
			appGc.setShowFPS(true);
			appGc.start();
		} catch (SlickException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static ResourceLoader resLoader = new ResourceLoader();

	public static final int WIDTH = 1280;

	public static final Image NULL_IMAGE = null;

	public static final int	STATE_LOADING	= 0;
	public static final int	STATE_LOGIN		= 1;
	public static final int	STATE_DESKTOP	= 2;

	public StateLoading	loadState		= new StateLoading(STATE_LOADING);
	public StateLogin	loginState		= new StateLogin();
	public StateDesktop	desktopState	= new StateDesktop();

	private static void resetLib() {
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
	}

	public static void main(String[] args) {
		resetLib();
		new WindowsMain();
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.enterState(STATE_DESKTOP);
	}

}
