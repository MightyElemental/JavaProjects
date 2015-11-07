package net.iridgames.munchkin.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public abstract class GUIObject extends Rectangle {

	private static final long serialVersionUID = 5638854255109217878L;

	private Color	color		= Color.white;
	private Color	textColor	= Color.black;

	public GUIObject( float x, float y, float width, float height ) {
		super(x, y, width, height);
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

	public abstract void draw(Graphics g);

}
