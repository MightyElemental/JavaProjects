package net.mightyelemental.mowergame.gui;

import java.util.regex.Pattern;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class TextBox extends GUIObject {

	private static final long serialVersionUID = -585231526144004270L;

	private int maxLetters = 5;

	private String subText = "TextBox";
	private Color subTextColor = Color.lightGray;

	private Pattern letterRegex;

	private boolean selected;

	public TextBox(float x, float y, float width, float height, int maxLetters, Pattern letterRegex) {
		super(x, y, width, height);
		this.setMaxLetters(maxLetters);
		this.letterRegex = letterRegex;
	}

	public TextBox setSubText(String text) {
		subText = text;
		return this;
	}

	public void update(GameContainer gc, StateBasedGame sbg, int ticks) throws SlickException {
		int mouseX = gc.getInput().getMouseX();
		int mouseY = gc.getInput().getMouseY();
		StringBuilder sb = new StringBuilder(getText());

		if (this.contains(mouseX, mouseY)) {
			selected = true;
		} else {
			selected = false;
		}

		if (sb.length() > maxLetters) {
			sb.delete(maxLetters, sb.length());
		}
		setText(sb.toString());
	}

	public void keyPressed(int key, char c) {
		String c1 = "" + c;
		if (key == Input.KEY_BACK) {
			StringBuilder sb = new StringBuilder(getText());
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			setText(sb.toString());
			return;
		}
		c1 = c1.replaceAll(letterRegex.pattern(), "");
		setText(getText() + c1);
	}

	@Override
	public void draw(Graphics g) {
		if (selected) {
			g.setColor(getColor().darker());
		} else {
			g.setColor(getColor());
		}
		g.fill(this);
		g.setColor(subTextColor);
		g.drawString(subText, getX() + 10, getY() + 10);
		g.setColor(getTextColor());
		g.draw(this);
		g.drawString(getText(), getCenterX() - g.getFont().getWidth(getText()) / 2,
				getCenterY() - g.getFont().getHeight(getText()) / 2);
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public TextBox setText(String text) {
		super.setText(text);
		return this;
	}

	/** @return the maxLetters */
	public int getMaxLetters() {
		return maxLetters;
	}

	/**
	 * @param maxLetters
	 *            the maxLetters to set
	 */
	public void setMaxLetters(int maxLetters) {
		this.maxLetters = maxLetters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.iridgames.munchkin.gui.GUIObject#setTextColor(org.newdawn.slick.
	 * Color)
	 */
	@Override
	public TextBox setTextColor(Color textColor) {
		super.setTextColor(textColor);
		textColor.a = 0.5f;
		subTextColor = textColor;
		return this;
	}

	/** @return the selected */
	public boolean isSelected() {
		return selected;
	}

}
