package net.mightyelemental.maven.RayTraceTest2.objects;

import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Vector3f;

public class Circle extends Plane {

	@Override
	public boolean intersects(Ray r) {
		boolean inter = super.intersects(r);
		Vector3f hit = r.getOrig().sum(r.getDirection().mul(r.t0));
		Vector3f pq = hit.sub(origin);
		boolean test = pq.dot(pq) <= radius * radius;
		// System.out.println(r.getHitPoint());
		return inter && test;
	}

	public float radius = 1;

	public Circle(Vector3f normal, Vector3f origin, float radius) {
		super(normal, origin);
		this.radius = radius;
	}

}
