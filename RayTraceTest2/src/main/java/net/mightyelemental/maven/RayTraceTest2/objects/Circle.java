package net.mightyelemental.maven.RayTraceTest2.objects;

import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Vec3f;

public class Circle extends Plane {

	@Override
	public boolean intersects(Ray r) {// VERIFY the initial intersection test is even needed
		boolean inter = super.intersects( r );
		if (inter) {
			Vec3f hit = r.getOrig().sum( r.getDirection().mul( r.t0 ) );
			Vec3f pq = hit.sub( origin );
			return pq.dot( pq ) <= radius * radius;
		}
		return false;
	}

	public float radius = 1;

	public Circle(Vec3f normal, Vec3f origin, float radius) {
		super( normal, origin );
		this.radius = radius;
	}

	@Override
	public BoundingBox generateBoundingBox() {// TODO: change from sphere to circle
		Vec3f min = this.origin.sub( new Vec3f( radius, radius, radius ) );
		Vec3f max = this.origin.sum( new Vec3f( radius, radius, radius ) );
		return new BoundingBox( min, max );
	}

}
