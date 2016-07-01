package net.mightyelemental.mowergame.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Button extends GUIObject {

	private static final long serialVersionUID = 6625839121578821815L;

	private int textX;
	private int textY;

	private boolean enabled = true;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Button(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public Button setText(String text, Graphics g) {
		super.setText(text);
		textX = (int) (this.getCenterX() - g.getFont().getWidth(text) / 2.0);
		textY = (int) (this.getCenterY() - g.getFont().getHeight(text) / 2.0);
		return this;
	}

	/** @return the textX */
	public int getTextX() {
		return textX;
	}

	/** @return the textY */
	public int getTextY() {
		return textY;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public Button setColor(Color color) {
		super.setColor(color);
		return this;
	}

	/**
	 * @param g
	 *            the graphics instance
	 */
	public void draw(Graphics g) {
		g.setColor(getColor());
		if (!enabled) {
			g.setColor(getColor().darker());
		}
		g.fillRoundRect(x, y, width, height, 15);
		g.setColor(getTextColor());
		g.drawString(getText(), getTextX(), getTextY());
		g.setColor(getTextColor());
		g.drawRoundRect(x, y, width, height, 15);
	}

}
