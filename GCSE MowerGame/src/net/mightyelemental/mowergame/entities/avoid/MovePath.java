package net.mightyelemental.mowergame.entities.avoid;

import java.awt.Point;

import org.newdawn.slick.GameContainer;

public class MovePath {

	protected Point destination;

	public boolean hasReached;

	public MovePath(float x, float y) {
		this.destination = new Point((int) x, (int) y);
	}

	public void update(GameContainer gc, int delta, EntityAvoid ent) {
		float amountToMoveX = (2 * (float) Math.cos(Math.toRadians(ent.angle)));
		float amountToMoveY = (2 * (float) Math.sin(Math.toRadians(ent.angle)));
		ent.setCenterX(ent.getCenterX() + amountToMoveX);
		ent.setCenterY(ent.getCenterY() + amountToMoveY);
		if (ent.getY() > destination.getY() - 5 && ent.getY() < destination.getY() + 5) {
			if (ent.getX() > destination.getX() - 5 && ent.getX() < destination.getX() + 5) {
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
