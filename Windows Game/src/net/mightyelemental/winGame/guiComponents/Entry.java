package net.mightyelemental.winGame.guiComponents;

import org.newdawn.slick.Color;

public class Entry {

	private String text;
	private boolean onRight;
	private Color color;

	public Entry(String text, boolean onRight, Color c) {
		this.text = text;
		this.onRight = onRight;
		this.color = c;
	}

	public Entry(String text) {
		this.text = text;
		onRight = false;
		color = null;
	}

	public String getText() {
		return text;
	}

	public boolean isOnRight() {
		return onRight;
	}

	public Color getColor() {
		return color;
	}

}
