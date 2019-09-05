package net.mightyelemental.maven.RayTraceTest2.objects;

import java.util.ArrayList;
import java.util.List;

import net.mightyelemental.maven.RayTraceTest2.Mat4f;
import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Vector3f;

public class Triangle extends Plane {

	public PolyPoint p1, p2, p3;

	public boolean smoothNormal = false;

	@Override
	public boolean intersects(Ray r) {
		if (!smoothNormal) {
			boolean inter = super.intersects(r);
			if (inter) {
				Vector3f hit = r.getOrig().sum(r.getDirection().mul(r.t0));

				Vector3f edge0 = p2.location.sub(p1.location);
				Vector3f vp0 = hit.sub(p1.location);
				if (normal.dot(edge0.cross(vp0)) < 0)
					return false;

				Vector3f edge1 = p3.location.sub(p2.location);
				Vector3f vp1 = hit.sub(p2.location);
				if (normal.dot(edge1.cross(vp1)) < 0)
					return false;

				Vector3f edge2 = p1.location.sub(p3.location);
				Vector3f vp2 = hit.sub(p3.location);
				if (normal.dot(edge2.cross(vp2)) < 0)
					return false;

				return true;
			}
		} else {

		}
		return false;
	}

	public Triangle(Vector3f p1, Vector3f p2, Vector3f p3) {
		super(p3.sub(p2).cross(p1.sub(p2)).normalize(), p1);
		this.p1 = new PolyPoint(p1);
		this.p2 = new PolyPoint(p2);
		this.p3 = new PolyPoint(p3);
		smoothNormal = false;
//		float x = (p1.x + p2.x + p3.x) / 3f;
//		float y = (p1.y + p2.y + p3.y) / 3f;
//		float z = (p1.z + p2.z + p3.z) / 3f;
//		this.origin = new Vector3f(x, y, z);
	}

	public Triangle(PolyPoint p1, PolyPoint p2, PolyPoint p3) {
		super(p3.location.sub(p2.location).cross(p1.location.sub(p2.location)).normalize(), p1.location);
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		smoothNormal = false;// TODO: change once added feature
	}

	public void translate(Vector3f transVec) {
		p1.location = p1.location.sum(transVec);
		p2.location = p2.location.sum(transVec);
		p3.location = p3.location.sum(transVec);
	}

	@Override
	public boolean isPointWithin(Vector3f hit) {
		if (!super.isPointWithin(hit))
			return false;
		Vector3f edge0 = p2.location.sub(p1.location);
		Vector3f vp0 = hit.sub(p1.location);
		if (normal.dot(edge0.cross(vp0)) < 0)
			return false;

		Vector3f edge1 = p3.location.sub(p2.location);
		Vector3f vp1 = hit.sub(p2.location);
		if (normal.dot(edge1.cross(vp1)) < 0)
			return false;

		Vector3f edge2 = p1.location.sub(p3.location);
		Vector3f vp2 = hit.sub(p3.location);
		if (normal.dot(edge2.cross(vp2)) < 0)
			return false;

		return true;
	}

	public static List<Triangle> getTrisFromPoints(PolyPoint... vecs) {
		if (vecs.length < 3)
			System.err.println("Not enough points were given to form any triangles!");
		List<Triangle> tris = new ArrayList<Triangle>();
		for (int i = 1; i < vecs.length - 1; i++) {
			tris.add(new Triangle(vecs[0], vecs[i], vecs[i + 1]));
		}
		return tris;
	}

	public void rotate(Vector3f rotVec) {
		Mat4f rotMat = Mat4f.getFullRotationDeg(rotVec);
		p1.location = rotMat.multiply(p1.location);
		p2.location = rotMat.multiply(p2.location);
		p3.location = rotMat.multiply(p3.location);
		origin = p1.location;
		normal = p3.location.sub(p2.location).cross(p1.location.sub(p2.location)).normalize();
	}

}
