package net.mightyelemental.mowergame.grass;

import org.newdawn.slick.geom.Rectangle;

public class Grass extends Rectangle {

	private static final long serialVersionUID = -9081761503186991190L;

	protected boolean mowed = false;

	public Grass(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	public boolean mowGrass() {
		boolean flag = !mowed;
		mowed = true;
		return flag;
	}

	public boolean isMowed() {
		return mowed;
	}

}
