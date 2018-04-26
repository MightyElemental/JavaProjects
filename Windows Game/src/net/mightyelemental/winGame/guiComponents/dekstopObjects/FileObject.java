package net.mightyelemental.winGame.guiComponents.dekstopObjects;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.OSSettings;
import net.mightyelemental.winGame.guiComponents.GUIComponent;

public class FileObject extends GUIComponent {

	private static final long serialVersionUID = -1947171860799043091L;

	private String title;

	public FileObject(float x, float y, String uid, String title) {
		super(x, y, OSSettings.FILE_DISPLAY_SIZE, OSSettings.FILE_DISPLAY_SIZE, uid);
		this.title = title;
		this.setTransparent(false);
	}

	@Override
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.draw(gc, sbg, g);
		String tempText = getTitle();
		while (f.getWidth(tempText) >= width) {
			tempText = tempText.substring(0, tempText.length() - 1);

		}
		g.setColor(Color.black);
		if (OSSettings.FILE_FONT != null) {
			g.setFont(OSSettings.FILE_FONT);
		}
		g.drawString(tempText, x + (width / 2) - f.getWidth(tempText) / 2, y + height);
	}

	public String getTitle() {
		return this.title;
	}

}
