package net.mightyelemental.mowergame.states.shop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g, MowerType mower, float y) {
		this.y = y;
		g.setColor(c);
		g.fillRoundRect(x, y, width, height, 15);
		g.setColor(Color.black);
		g.drawRoundRect(x, y, width, height, 15);

		String text = mower.getName();
		float width = g.getFont().getWidth(text);
		g.drawString(text, x + width / 2, y + 5);
		drawSelectedMower(gc, sbg, g, mower);
		drawDescription(g, mower, 0);
		int line = 0;
		line = drawVar(g, "Max Speed", mower.getBaseSpeed() + "+" + (mower.getSpeed() - mower.getBaseSpeed()) + "#= "
				+ mower.getSpeed() + "/u", "ups", line);
		line = drawVar(g, "Durability", mower.getBaseDurability() + "+"
				+ (mower.getDurability() - mower.getBaseDurability()) + "#= " + mower.getDurability() + "/u", "hp",
				line);
		line = drawVar(g, "Acceleration", mower.getBaseAcceleration() + "/u", "upss", line);
	}

	private Object[] splitEverNumChars(String text, int n) {
		char[] chars = new char[n];
		Arrays.fill(chars, '.');
		// String result = new String(chars);
		String[] words = text.split(" ");
		List<String> lines = new ArrayList<String>();
		int lineNum = 0;
		for (int i = 0; i < words.length; i++) {
			int lineFill = 0;
			String line = "";
			while (lineFill < 21) {
				if (i < words.length) {
					lineFill += words[i].length() + 1;
				} else {
					break;
				}
				if (lineFill < 21) {
					line += words[i] + " ";
					i++;
				} else {
					break;
				}
			}
			i--;
			lines.add(line);
			lineNum++;
		}
		// text.replace("(?<=\\G" + result + ")", "#");
		// String[] temp = text.split("(?<=\\G" + result + ")");
		return lines.toArray();
	}

	private void drawDescription(Graphics g, MowerType mower, float y) {// Fix
																		// word
																		// splitting
		String desc = mower.getDescription();
		Object[] lines = splitEverNumChars(desc, 21);
		g.setColor(Color.black);
		for (int i = 0; i < lines.length; i++) {
			if (lines[i] == null) {
				continue;
			}
			g.drawString(lines[i] + "", x + 5, this.y + y + 25 + (17 * i));
		}
	}

	/** @return the offset of the next line relative to the first line */
	public int drawVar(Graphics g, String name, String val, String units, int y) {
		val = val.replace("/u", " " + units);
		int nextLine = y;
		String[] vals = val.split("#");
		g.setColor(Color.black);
		g.drawString(name, x + 10, this.y + y + 120);
		for (int i = 0; i < vals.length; i++) {
			g.setColor(Color.black);
			if (vals[i].contains(units)) {
				g.setColor(Color.green.darker());
			}
			g.drawString(vals[i], x + 130, this.y + y + 120 + (17 * i));
			nextLine += 20;
		}
		return nextLine;
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
