package net.iridgames.munchkin.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import net.iridgames.munchkin.Munchkin;

public class CheckBox extends GUIObject {

	private static final long serialVersionUID = 4586661367699670288L;

	private String text = "Check Box";

	private Rectangle box;

	private int	textX;
	private int	textY;

	private boolean checked;

	public CheckBox( float x, float y ) {
		super(x, y, 40, 40);
		box = new Rectangle(x, y, 40, 40);
		textY = (int) (box.getCenterY() - Munchkin.font.getHeight() / 2);
		textX = (int) (box.getMaxX() + 10);
		this.setWidth(box.getWidth() + 10 + Munchkin.font.getWidth(text));
	}

	/** @return the text */
	public String getText() {
		return text;
	}

	/** @param text
	 *            the text to set */
	public CheckBox setText(String text) {
		this.text = text;
		this.setWidth(Munchkin.font.getWidth(this.text) + 10 + box.getWidth());
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

	@Override
	public void draw(Graphics g) {
		g.setColor(this.getColor());
		g.fill(box);
		g.setColor(this.getTextColor());
		g.draw(box);
		g.drawString(text, textX, textY);
		if (checked) {
			float temp = box.getWidth() / 7f;
			g.drawLine(box.getX() + temp, box.getY() + temp, box.getMaxX() - temp, box.getMaxY() - temp);
			g.drawLine(box.getX() + temp, box.getMaxY() - temp, box.getMaxX() - temp, box.getY() + temp);
		}
	}

	/** @return the checked */
	public boolean isChecked() {
		return checked;
	}

	/** @param checked
	 *            the checked to set */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
