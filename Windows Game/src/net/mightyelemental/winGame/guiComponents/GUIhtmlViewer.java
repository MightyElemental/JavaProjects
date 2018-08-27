package net.mightyelemental.winGame.guiComponents;

import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.ResourceLoader;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

public class GUIhtmlViewer extends GUIComponent {

	private static final long serialVersionUID = 3497817514161328855L;

	private Image page;

	public GUIhtmlViewer(float width, float height, String uid, AppWindow aw) {
		super(width, height, uid, aw);
	}

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.draw(gc, sbg, g);
		if ( page != null ) {
			g.drawImage(page, 0, 0);
		}
	}

	public void displayWebsite(String url) throws IOException, InterruptedException {
		String fileName = url.replaceAll("[^A-Za-z0-9]", "");
		//page = ResourceLoader.loadImage("webcache/" + fileName);
		Process process = new ProcessBuilder("./lib/3rdparty/phantomjs.exe", "/lib/3rdparty/rasterize.js", url,
				"./assets/textures/webcache/" + fileName + ".png", "1280*720px").start();
		process.waitFor();
		page = ResourceLoader.loadImage("webcache/" + fileName);
	}

}
