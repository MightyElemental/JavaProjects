package net.wolfgangts.render;

import java.awt.Rectangle;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.ldsracing.LDSRacing;

public class GUIButton {
	private Rectangle buttonRect;
	private String title;
	private Runnable clickEvent;
	private Runnable hoverEvent;
	private Color color;
	private Color fontColor;

	public int x, y, width, height;

	public GUIButton(float x, float y, float w, float h) {
		this.buttonRect = new Rectangle((int) x, (int) y, (int) w, (int) h);
		this.color = new Color(255, 255, 255, 255);
		this.fontColor = new Color(0, 0, 0, 255);
		this.x = (int) x;
		this.y = (int) y;
		this.width = (int) w;
		this.height = (int) h;
	}

	public GUIButton(float x, float y, float w, float h, String title) {
		this(x, y, w, h);
		this.setTitle(title);
	}

	public GUIButton setColor(Color color) {
		this.color = color;

		return this;
	}

	public Color getColor() {
		return this.color;
	}

	public GUIButton setFontColor(Color color) {
		this.fontColor = color;

		return this;
	}

	public Color getFontColor() {
		return this.fontColor;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setPosition(Rectangle rect) {
		this.buttonRect = rect;
	}

	public void setPosition(int x, int y, int i, int j) {
		this.buttonRect = new Rectangle(x, y, i, j);
	}

	public Rectangle getPosition() {
		return this.buttonRect;
	}

	public GUIButton setClickEvent(Runnable clickEvent) {
		this.clickEvent = clickEvent;

		return this;
	}

	public void callClickEvent() {
		if (this.clickEvent != null) this.clickEvent.run();
	}

	public GUIButton setHoverEvent(Runnable hoverEvent) {
		this.hoverEvent = hoverEvent;

		return this;
	}

	public void callHoverEvent() {
		if (this.hoverEvent != null) this.hoverEvent.run();
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		Rectangle position = this.buttonRect;

		g.setColor(this.color);
		g.fillRect(position.x, position.y, position.width, position.height);

		g.setColor(this.fontColor);
		g.setFont(LDSRacing.font);

		int height = LDSRacing.font.getHeight();

		if (LDSRacing.font.getWidth(title) <= position.width - 5) {
			int width = LDSRacing.font.getWidth(this.title);
			g.drawString(this.title, position.x + position.width / 2 - width / 2, position.y + position.height / 2 - height / 2);
		} else {
			int temp = LDSRacing.font.getWidth("a");
			int temp2 = (position.width - 5) / temp;
			StringBuilder sb = new StringBuilder(title);
			sb.setLength(temp2 - 1);
			int width = LDSRacing.font.getWidth(sb.toString() + "...");
			g.drawString(sb.toString() + "...", position.x + position.width / 2 - width / 2, position.y + position.height / 2 - height / 2);
		}

	}

}
