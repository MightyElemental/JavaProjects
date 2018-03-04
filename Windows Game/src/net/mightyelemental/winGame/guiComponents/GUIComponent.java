package net.mightyelemental.winGame.guiComponents;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

@SuppressWarnings("serial")
public abstract class GUIComponent extends Rectangle {

	public Color	color	= Color.white;
	private String	UID;

	private boolean selected;

	private Shape selectedShape;

	public boolean transparent = true;

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

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if ( !transparent ) {
			g.setColor(color);
			g.fillRect(x, y, width, height);
		}
		if ( this.isSelected() ) {
			g.setColor(color.darker());
			if ( selectedShape != null ) {
				g.draw(selectedShape);
			} else {
				g.drawRect(x, y, width, height);
			}
		}
	}

	public void onMousePressed(int button, int x, int y) {

	}

	public void onMouseReleased(int button, int x, int y) {

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

}
