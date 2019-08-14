package net.mightyelemental.maven.RayTraceTest2.objects;

import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Vector3f;

public class Triangle extends Plane {

	public Vector3f p1, p2, p3;

	@Override
	public boolean intersects(Ray r) {
		boolean inter = super.intersects(r);
		if (inter) {
			Vector3f hit = r.getOrig().sum(r.getDirection().mul(r.t0));

			Vector3f edge0 = p2.sub(p1);
			Vector3f vp0 = hit.sub(p1);
			if (normal.dot(edge0.cross(vp0)) < 0)
				return false;

			Vector3f edge1 = p3.sub(p2);
			Vector3f vp1 = hit.sub(p2);
			if (normal.dot(edge1.cross(vp1)) < 0)
				return false;

			Vector3f edge2 = p1.sub(p3);
			Vector3f vp2 = hit.sub(p3);
			if (normal.dot(edge2.cross(vp2)) < 0)
				return false;

			return true;
		}
		return false;
	}

	public Triangle(Vector3f p1, Vector3f p2, Vector3f p3) {
		super(p3.sub(p2).cross(p1.sub(p2)).normalize(), p1);
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
//		float x = (p1.x + p2.x + p3.x) / 3f;
//		float y = (p1.y + p2.y + p3.y) / 3f;
//		float z = (p1.z + p2.z + p3.z) / 3f;
//		this.origin = new Vector3f(x, y, z);
	}

	@Override
	public boolean isPointWithin(Vector3f hit) {
		if (!super.isPointWithin(hit))
			return false;
		Vector3f edge0 = p2.sub(p1);
		Vector3f vp0 = hit.sub(p1);
		if (normal.dot(edge0.cross(vp0)) < 0)
			return false;

		Vector3f edge1 = p3.sub(p2);
		Vector3f vp1 = hit.sub(p2);
		if (normal.dot(edge1.cross(vp1)) < 0)
			return false;

		Vector3f edge2 = p1.sub(p3);
		Vector3f vp2 = hit.sub(p3);
		if (normal.dot(edge2.cross(vp2)) < 0)
			return false;

		return true;
	}

}
