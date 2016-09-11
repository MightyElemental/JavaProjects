package net.mightyelemental.tree;

import java.awt.Point;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

public class MathHelper {
	
	
	public static float getAngle(Point object1, Point object2) {
		float angle = (float) Math.toDegrees(Math.atan2(object1.y - object2.y, object1.x - object2.x));
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}
	
	public static float goldenRatio() {
		return 1.6179775280898876f;
	}
	
	public static float getAngle(Line l) {
		float angle = (float) Math.toDegrees(Math.atan2(l.getStart().y - l.getEnd().y, l.getStart().x - l.getEnd().x));
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}
	
	public static float round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (float) ((double) Math.round(value * scale) / scale);
	}
	
	public static float getDistance(Point object1, Point object2) {
		double d1 = object1.getX() - object2.getX();
		double d2 = object1.getY() - object2.getY();
		d1 = Math.pow(d1, 2);
		d2 = Math.pow(d2, 2);
		float d = (float) Math.sqrt(d1 + d2);
		return d;
	}
	
	public static float getDistance(Rectangle object1, Rectangle object2) {
		double d1 = object1.getCenterX() - object2.getCenterX();
		double d2 = object1.getCenterY() - object2.getCenterY();
		d1 = Math.pow(d1, 2);
		d2 = Math.pow(d2, 2);
		float d = (float) Math.sqrt(d1 + d2);
		return d;
	}
	
}
