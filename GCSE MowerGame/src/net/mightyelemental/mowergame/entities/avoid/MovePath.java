package net.mightyelemental.mowergame.entities.avoid;

import java.awt.Point;

import org.newdawn.slick.GameContainer;

import net.mightyelemental.mowergame.entities.Entity;

public class MovePath {

	protected Point destination;

	public boolean hasReached;

	public float speed = 2f;

	public Entity destEnt;

	public MovePath(float x, float y) {
		this.destination = new Point((int) x, (int) y);
	}

	public MovePath(float x, float y, float speed) {
		this(x, y);
		this.speed = speed;
	}

	public MovePath(Entity ent, float speed) {
		this.speed = speed;
		this.destination = new Point(100, 100);
		this.destEnt = ent;
	}

	public void update(GameContainer gc, int delta, EntityAvoid ent) {

		if (destEnt != null) {
			this.destination.x = (int) destEnt.getCenterX();
			this.destination.y = (int) destEnt.getCenterY();
		}

		float amountToMoveX = (speed / 17f * delta * (float) Math.cos(Math.toRadians(ent.angle)));
		float amountToMoveY = (speed / 17f * delta * (float) Math.sin(Math.toRadians(ent.angle)));
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
