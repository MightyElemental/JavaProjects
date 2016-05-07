package net.mightyelemental.mowergame.entities.avoid;

import java.awt.Point;

import org.newdawn.slick.GameContainer;

public class MovePath {

	protected Point destination;

	public boolean hasReached;

	public float speed = 2f;

	public MovePath(float x, float y) {
		this.destination = new Point((int) x, (int) y);
	}

	public MovePath(float x, float y, float speed) {
		this(x, y);
		this.speed = speed;
	}

	public void update(GameContainer gc, int delta, EntityAvoid ent) {
		float amountToMoveX = (speed * (float) Math.cos(Math.toRadians(ent.angle)));
		float amountToMoveY = (speed * (float) Math.sin(Math.toRadians(ent.angle)));
		if (!hasReached) {
			ent.setCenterX(ent.getCenterX() + amountToMoveX);
			ent.setCenterY(ent.getCenterY() + amountToMoveY);
		}
		if (ent.getCenterY() > destination.getY() - 5 && ent.getCenterY() < destination.getY() + 5) {
			if (ent.getCenterX() > destination.getX() - 5 && ent.getCenterX() < destination.getX() + 5) {
				hasReached = true;
			}
		}
	}

	public int getX() {
		return destination.x;
	}

	public int getY() {
		return destination.y;
	}

}
