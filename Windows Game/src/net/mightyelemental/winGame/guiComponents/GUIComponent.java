package net.mightyelemental.winGame.guiComponents;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

public class GUIComponent extends Rectangle {

	private static final long serialVersionUID = 5967548527327574045L;

	public Color	color	= Color.white;
	private String	UID;

	private boolean selected, transparent = true;

	private AppWindow linkedWindow;

	private Shape	selectedShape;
	protected Font	f;

	public GUIComponent setColor(Color c) {
		color = c;
		return this;
	}

	public GUIComponent setTransparent(boolean t) {
		this.transparent = t;
		return this;
	}

	public GUIComponent(float x, float y, float width, float height, String uid) {
		super(x, y, width, height);
		if ( uid.startsWith("#") ) {
			UID = uid.toUpperCase();
		} else {
			UID = (System.currentTimeMillis() % 15937) + "_" + uid.toUpperCase();
		}
	}

	public GUIComponent(float width, float height, String uid, AppWindow aw) {
		this(0, 0, width, height, uid);
		this.linkedWindow = aw;
	}

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if ( f == null ) f = g.getFont();
		if ( !transparent ) {
			g.setColor(color);
			g.fillRoundRect(x, y, width, height, 3);
		}
		if ( this.isSelected() ) {
			g.setColor(color.darker());
			if ( selectedShape != null ) {
				g.draw(selectedShape);
			} else {
				g.drawRoundRect(x, y, width, height, 3);
			}
		}
	}

	public void onMousePressed(int button) {

	}

	public void onMouseReleased(int button) {

	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public String getUID() {
		return UID;
	}

	public String getNID() {
		if ( !getUID().startsWith("#") ) {
			return getUID().split("_", 2)[0];
		} else {
			return getUID();
		}
	}

	public void onKeyPressed(int key, char c) {

	}

	public Shape getSelectedShape() {
		return selectedShape;
	}

	public GUIComponent setSelectedShape(Shape selectedShape) {
		this.selectedShape = selectedShape;
		return this;
	}

	public AppWindow getLinkedWindow() {
		return linkedWindow;
	}

	protected void setLinkedWindow(AppWindow aw) {
		this.linkedWindow = aw;
	}

}
