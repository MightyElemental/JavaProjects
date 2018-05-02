package net.mightyelemental.winGame.guiComponents;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

public class GUIButton extends GUIComponent {

	private static final long serialVersionUID = -8958146127262539180L;

	private String text = "";

	public GUIButton(float x, float y, float width, float height, String uid) {
		super(x, y, width, height, uid);
		this.setTransparent(false);
	}

	public GUIButton(float width, float height, String uid, AppWindow aw) {
		this(0, 0, width, height, uid);
		this.setLinkedWindow(aw);
	}

	@Override
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.draw(gc, sbg, g);
		String tempText = text;
		while (g.getFont().getWidth(tempText) >= width) {
			tempText = tempText.substring(0, tempText.length() - 1);
		}
		g.setColor(getInvertColor(color));
		g.drawString(tempText, x + (width / 2) - g.getFont().getWidth(tempText) / 2,
				y + (height / 2) - g.getFont().getHeight(tempText) / 2);
	}

	public Color getInvertColor(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		return new Color(255 - r, 255 - g, 255 - b);
	}

	public String getText() {
		return text;
	}

	public GUIButton setText(String text) {
		this.text = text;
		return this;
	}

	public void onMousePressed(int button) {
		color = getInvertColor(color);
	}

	public void onMouseReleased(int button) {
		color = getInvertColor(color);
	}

}
