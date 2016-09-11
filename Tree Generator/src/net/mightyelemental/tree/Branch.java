package net.mightyelemental.tree;

import org.newdawn.slick.geom.Line;

public class Branch extends Line {
	
	
	public Branch( float x1, float y1, float x2, float y2 ) {
		super(x1, y1, x2, y2);
	}
	
	private boolean drawn = false;
	private float percent;
	private Branch parent;
	
	public boolean drawn() {
		return drawn;
	}
	
	public void setDrawn(boolean d) {
		this.drawn = d;
	}
	
	public float getAngle() {
		return MathHelper.getAngle(this);
	}
	
	public Line getProgressLine() {
		float x = (this.length() / 100f * percent) * ((float) Math.cos(Math.toRadians(getAngle() - 180))) + this.getStart().x;
		float y = (this.length() / 100f * percent) * ((float) Math.sin(Math.toRadians(getAngle() - 180))) + this.getStart().y;
		if (percent >= 100) {
			drawn = true;
			percent = 100;
		}
		return new Line(getStart().x, getStart().y, x, y);
	}
	
	public void nextStep(int delta) {
		percent += (100f / (length() / 1.2f)) * (delta / 17f);
	}
	
	public Branch getParent() {
		return parent;
	}
	
	public void setParent(Branch parent) {
		this.parent = parent;
	}
}
