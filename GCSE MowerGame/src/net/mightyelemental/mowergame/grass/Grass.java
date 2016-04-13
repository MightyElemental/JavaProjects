package net.mightyelemental.mowergame.grass;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

public class Grass extends Rectangle {
	
	
	private static final long serialVersionUID = -9081761503186991190L;
	
	public Color color;
	
	public int x, y;
	
	public Grass( float x, float y, float width, float height, int green ) {
		super(x, y, width, height);
		color = new Color(0, green, 0);
	}
	
}
