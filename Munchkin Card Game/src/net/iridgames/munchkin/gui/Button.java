package net.iridgames.munchkin.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import net.iridgames.munchkin.Munchkin;

public class Button extends Rectangle {

	private static final long serialVersionUID = 6625839121578821815L;

	private String text = "Button";

	private Color	color		= Color.white;
	private Color	textColor	= Color.black;

	private int	textX;
	private int	textY;

	public Button( float x, float y, float width, float height ) {
		super(x, y, width, height);
	}

	/** @return the text */
	public String getText() {
		return text;
	}

	/** @param text
	 *            the text to set */
	public Button setText(String text) {
		this.text = text;
		textX = (int) (this.getCenterX() - Munchkin.font.getWidth(text) / 2.0);
		textY = (int) (this.getCenterY() - Munchkin.font.getHeight() / 2.0);
		return this;
	}

	/** @return the color */
	public Color getColor() {
		return color;
	}

	/** @param color
	 *            the color to set */
	public void setColor(Color color) {
		this.color = color;
	}

	/** @return the textColor */
	public Color getTextColor() {
		return textColor;
	}

	/** @param textColor
	 *            the textColor to set */
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	/** @return the textX */
	public int getTextX() {
		return textX;
	}

	/** @return the textY */
	public int getTextY() {
		return textY;
	}

	/** @param g
	 *            the graphics instance */
	public void draw(Graphics g) {
		g.setColor(getColor());
		g.fill(this);
		g.setColor(getTextColor());
		g.drawString(getText(), getTextX(), getTextY());
		g.setColor(getTextColor());
		g.draw(this);
	}

}
