package net.mightyelemental.neuralnet.pong;

import java.awt.Rectangle;

public class Ball extends Rectangle {

	public double xDouble, yDouble;

	private static final long serialVersionUID = -6260374963869799807L;

	public Ball(double x, double y, double width, double height) {
		super((int) x, (int) y, (int) width, (int) height);
		this.xDouble = (int) x;
		this.yDouble = (int) y;
	}

	public void setX(double x) {
		this.xDouble = x;
		this.x = (int) x;
	}

	public void setY(double y) {
		this.yDouble = y;
		this.y = (int) y;
	}

	public void setLoc(double x, double y) {
		this.xDouble = x;
		this.yDouble = y;
		this.x = (int) x;
		this.y = (int) y;
	}

}
