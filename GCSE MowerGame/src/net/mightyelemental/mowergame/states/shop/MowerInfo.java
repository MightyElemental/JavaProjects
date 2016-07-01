package net.mightyelemental.mowergame.states.shop;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.entities.MowerType;

public class MowerInfo {

	public float x, y;
	public float width = 290, height = 300;
	public ShopState ss;

	public MowerInfo(float x, float y, ShopState ss) {
		this.x = x;
		this.y = y;
		this.ss = ss;
	}

	Color c = new Color(0.8f, 0.8f, 0.8f, 0.8f);

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g, MowerType mower) {
		g.setColor(c);
		g.fillRoundRect(x, y, width, height, 15);
		g.setColor(Color.black);
		g.drawRoundRect(x, y, width, height, 15);

		String text = mower.getName();
		float width = g.getFont().getWidth(text);
		g.drawString(text, x + width / 2, y + 5);
		drawSelectedMower(gc, sbg, g, mower);
	}

	public void drawSelectedMower(GameContainer gc, StateBasedGame sbg, Graphics g, MowerType mower) {
		int h = 100 - 15;
		g.setColor(Color.white.darker());
		g.fillRoundRect(x + width - h - 5, y + 4, h + 1, h + 1, 15, 15);
		g.setColor(Color.black);
		g.drawRoundRect(x + width - h - 5, y + 4, h + 1, h + 1, 15, 15);
		g.drawImage(mower.getDisplayIcon().getScaledCopy(h - 1, h - 1), x + width - h - 5, y + 5);
	}

}
