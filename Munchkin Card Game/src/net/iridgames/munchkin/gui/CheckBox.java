package net.iridgames.munchkin.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import net.iridgames.munchkin.Munchkin;

public class CheckBox extends GUIObject {

	private static final long serialVersionUID = 4586661367699670288L;

	private Rectangle box;

	private boolean checked;

	public CheckBox( float x, float y ) {
		super(x, y, 40, 40);
		box = new Rectangle(x, y, 40, 40);
		textY = (int) (box.getCenterY() - Munchkin.font.getHeight() / 2);
		textX = (int) (box.getMaxX() + 10);
		this.setWidth(box.getWidth() + 10 + Munchkin.font.getWidth(getText()));
	}

	/** @param text
	 *            the text to set */
	public CheckBox setText(String text) {
		super.setText(text);
		this.setWidth(Munchkin.font.getWidth(getText()) + 10 + box.getWidth());
		return this;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(this.getColor());
		g.fill(box);
		g.setColor(this.getTextColor());
		g.draw(box);
		g.drawString(getText(), textX, textY);
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
