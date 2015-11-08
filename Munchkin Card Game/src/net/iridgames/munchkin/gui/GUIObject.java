package net.iridgames.munchkin.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public abstract class GUIObject extends Rectangle {

	private static final long serialVersionUID = 5638854255109217878L;

	private Color	color		= Color.white;
	private Color	textColor	= Color.black;

	private String	text	= "GUIObject";
	protected int	textX;
	protected int	textY;

	public GUIObject( float x, float y, float width, float height ) {
		super(x, y, width, height);
	}

	/** @return the textX */
	public int getTextX() {
		return textX;
	}

	/** @return the textY */
	public int getTextY() {
		return textY;
	}

	/** @return the text */
	public String getText() {
		return text;
	}

	/** @param text
	 *            the text to set */
	public GUIObject setText(String text) {
		this.text = text;
		return this;
	}

	/** @return the color */
	public Color getColor() {
		return color;
	}

	/** @param color
	 *            the color to set */
	public GUIObject setColor(Color color) {
		this.color = color;
		return this;
	}

	/** @return the textColor */
	public Color getTextColor() {
		return textColor;
	}

	/** @param textColor
	 *            the textColor to set */
	public GUIObject setTextColor(Color textColor) {
		this.textColor = textColor;
		return this;
	}

	public abstract void draw(Graphics g);

}
