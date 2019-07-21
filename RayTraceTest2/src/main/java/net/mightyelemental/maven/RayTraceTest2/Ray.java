package net.mightyelemental.maven.RayTraceTest2;

import java.util.List;

import net.mightyelemental.maven.RayTraceTest2.objects.Renderable;

public class Ray {

	private Vector3f	direction, start;
	public float		t0		= Float.MAX_VALUE;
	public float		t1		= Float.MAX_VALUE;
	public float		tnear	= Float.MAX_VALUE;

	public Ray(Vector3f dir, Vector3f start) {
		this.direction = dir;
		this.start = start;
	}

	public float getDistanceToPoint(Vector3f v) {
		// Vector3f AC = start.vecTo(v);
		//
		// Vector3f unit = direction.getUnitVec();
		//
		// float AD = AC.dot(unit);
		//
		// float ACl = AC.getLength();
		//
		// float dist = (float) Math.sqrt(Math.pow(ACl, 2) - Math.pow(AD, 2));

		// System.out.println(dist);

		return direction.cross(v.sub(start)).getLength();
	}

	public Ray getRayToPoint(Vector3f v) {
		Vector3f dir = direction.cross(v.sub(start));
		// ray.origin + ray.direction * Vector3.Dot(ray.direction, point - ray.origin)
		Vector3f sPoint = start.sum(direction.mul(direction.dot(v.sub(start))));

		return new Ray(dir, sPoint);
	}

	public Vector3f getDirection() {
		return direction;
	}

	public Vector3f getOrig() {
		return start;
	}

	public Renderable trace(List<Renderable> objects, int depth) {
		Renderable closest = null;
		for ( Renderable o : objects ) {
			if(o.ignoreRay(depth)) continue;
			t0 = Float.MAX_VALUE;
			t1 = Float.MAX_VALUE;
			if ( !o.intersects(this) ) continue;
			if ( t0 < 0 ) t0 = t1;
			if ( t0 < tnear ) {
				tnear = t0;
				closest = o;
			}
		}
		return closest;
	}

	public Vector3f getHitPoint() {
		return start.sum(direction.mul(tnear));
	}

	public Vector3f getReflectedVector(Vector3f normal) {
		return direction.getReflectedVector(normal);
	}

	public void setOrig(Vector3f pos) {
		this.start = pos;
	}

}
