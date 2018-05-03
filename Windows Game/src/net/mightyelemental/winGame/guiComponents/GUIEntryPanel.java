package net.mightyelemental.winGame.guiComponents;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

public class GUIEntryPanel extends GUIComponent {

	private static final long serialVersionUID = -8339400027648252407L;

	private List<Entry> entries = new ArrayList<Entry>();

	public GUIEntryPanel(float x, float y, float width, float height, String uid) {
		super(x, y, width, height, uid);
	}

	public GUIEntryPanel(float width, float height, String uid, AppWindow aw) {
		super(0, 0, width, height, uid);
		this.setLinkedWindow(aw);
	}

	public void addEntry(String text) {
		addEntry(text, false, null);
	}

	public void addEntry(String text, boolean onRight) {
		addEntry(text, onRight, null);
	}

	public void addEntry(String text, boolean onRight, Color c) {
		entries.add(new Entry(text, onRight, c));
	}

	@Override
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(this.color);
		g.fill(this);
	}

}
