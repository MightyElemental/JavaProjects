package net.mightyelemental.mowergame;

import java.awt.Point;

public class MathHelper {

	public static float getAngle(Point object1, Point object2) {
		float angle = (float) Math.toDegrees(Math.atan2(object1.y - object2.y, object1.x - object2.x));
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}

}
