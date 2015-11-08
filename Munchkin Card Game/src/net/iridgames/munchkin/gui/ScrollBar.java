package net.iridgames.munchkin.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import net.iridgames.munchkin.Munchkin;

public class ScrollBar extends GUIObject {

	private static final long serialVersionUID = 6710372896922363972L;

	private float	minVal	= 0;
	private float	maxVal	= 10;

	private Color barColor = Color.darkGray;

	private Rectangle bar;

	public ScrollBar( float x, float y, float width, float height, float minVal, float maxVal ) {
		super(x, y, width, height);
		this.minVal = minVal;
		this.maxVal = maxVal;
		bar = new Rectangle(x, y, (width * 10) / (maxVal - minVal), height);
	}

	public ScrollBar changeBarPos(float x) {
		if (x + bar.getWidth() - getX() > getWidth()) {
			x = width - bar.getWidth() + getX();
		}
		if (x < getX()) {
			x = getX();
		}
		bar.setX(x);
		return this;
	}

	public float getValue() {
		return (bar.getX() - getX()) / ((getWidth() - bar.getWidth()) / (maxVal - minVal));
	}

	/** @param text
	 *            the text to set */
	public ScrollBar setText(String text) {
		super.setText(text);
		return this;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(this.getColor());
		g.fill(this);
		g.setColor(barColor);
		g.fill(bar);
		g.setColor(getTextColor());
		g.draw(this);
		g.drawString(getText(), this.getCenterX() - Munchkin.font.getWidth(getText()) / 2, getCenterY() - Munchkin.font.getHeight() / 2);
		int temp = Math.round(getValue());
		g.drawString(temp + "", getWidth() + getX() - Munchkin.font.getWidth(temp + "") - 20, getCenterY() - Munchkin.font.getHeight() / 2);
	}

	/** @return the bar */
	public Rectangle getBar() {
		return bar;
	}

}
