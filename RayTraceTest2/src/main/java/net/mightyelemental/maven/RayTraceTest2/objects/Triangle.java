package net.mightyelemental.maven.RayTraceTest2.objects;

import java.util.ArrayList;
import java.util.List;

import net.mightyelemental.maven.RayTraceTest2.Mat4f;
import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Utils;
import net.mightyelemental.maven.RayTraceTest2.Vec3f;

public class Triangle extends Plane {

	public PolyPoint p1, p2, p3;

	public boolean smoothNormal = false;

	@Override
	public boolean intersects(Ray r) {
		boolean inter = super.intersects( r );
		if (inter) {
			Vec3f hit = r.getOrig().sum( r.getDirection().mul( r.t0 ) );

			Vec3f edge0 = p2.location.sub( p1.location );
			Vec3f vp0 = hit.sub( p1.location );
			if (normal.dot( edge0.cross( vp0 ) ) < 0) return false;

			Vec3f edge1 = p3.location.sub( p2.location );
			Vec3f vp1 = hit.sub( p2.location );
			if (normal.dot( edge1.cross( vp1 ) ) < 0) return false;

			Vec3f edge2 = p1.location.sub( p3.location );
			Vec3f vp2 = hit.sub( p3.location );
			if (normal.dot( edge2.cross( vp2 ) ) < 0) return false;

			return true;
		}
		return false;
	}

	public Triangle(Vec3f p1, Vec3f p2, Vec3f p3) {
		super( p3.sub( p2 ).cross( p1.sub( p2 ) ).normalize(), p1 );
		this.p1 = new PolyPoint( p1 );
		this.p2 = new PolyPoint( p2 );
		this.p3 = new PolyPoint( p3 );
		smoothNormal = false;
//		float x = (p1.x + p2.x + p3.x) / 3f;
//		float y = (p1.y + p2.y + p3.y) / 3f;
//		float z = (p1.z + p2.z + p3.z) / 3f;
//		this.origin = new Vector3f(x, y, z);
	}

	public Triangle(PolyPoint p1, PolyPoint p2, PolyPoint p3) {
		super( p3.location.sub( p2.location ).cross( p1.location.sub( p2.location ) ).normalize(),
				p1.location );
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		smoothNormal = true;// TODO: change once added feature
	}

	public void translate(final Vec3f transVec) {
		p1.location.sumSave( transVec );
		p2.location.sumSave( transVec );
		p3.location.sumSave( transVec );
	}

	@Override
	public boolean isPointWithin(Vec3f hit) {
		if (!super.isPointWithin( hit )) return false;
		Vec3f edge0 = p2.location.sub( p1.location );
		Vec3f vp0 = hit.sub( p1.location );
		if (normal.dot( edge0.cross( vp0 ) ) < 0) return false;

		Vec3f edge1 = p3.location.sub( p2.location );
		Vec3f vp1 = hit.sub( p2.location );
		if (normal.dot( edge1.cross( vp1 ) ) < 0) return false;

		Vec3f edge2 = p1.location.sub( p3.location );
		Vec3f vp2 = hit.sub( p3.location );
		if (normal.dot( edge2.cross( vp2 ) ) < 0) return false;

		return true;
	}

	public static List<Triangle> getTrisFromPoints(PolyPoint... vecs) {
		if (vecs.length < 3)
			System.err.println( "Not enough points were given to form any triangles!" );
		List<Triangle> tris = new ArrayList<Triangle>();
		for (int i = 1; i < vecs.length - 1; i++) {
			tris.add( new Triangle( vecs[0], vecs[i], vecs[i + 1] ) );
		}
		return tris;
	}

	public void rotate(Vec3f rotVec) {
		Mat4f rotMat = Mat4f.getFullRotationDeg( rotVec );
		p1.location = rotMat.multiply( p1.location );
		p2.location = rotMat.multiply( p2.location );
		p3.location = rotMat.multiply( p3.location );
		origin = p1.location;
		normal = p3.location.sub( p2.location ).cross( p1.location.sub( p2.location ) ).normalize();
	}

	@Override
	public Vec3f getNormal(Vec3f hit, Vec3f rayDir) {
		if (smoothNormal) {
//			const Vec3f &n0 = N[trisIndex[triIndex * 3]]; 
//		    const Vec3f &n1 = N[trisIndex[triIndex * 3 + 1]]; 
//		    const Vec3f &n2 = N[trisIndex[triIndex * 3 + 2]]; 
//		    hitNormal = (1 - uv.x - uv.y) * n0 + uv.x * n1 + uv.y * n2; 

			return p1.normal.sum( p2.normal ).sum( p3.normal );
		} else {
			if (rayDir.dot( normal ) >= 0) {
				return normal.getNegative();
			} else {
				return normal;
			}
		}

	}

	@Override
	public BoundingBox generateBoundingBox() {
		Vec3f min = Utils.minVec( p1.location, p2.location, p3.location );
		Vec3f max = Utils.maxVec( p1.location, p2.location, p3.location );
		return new BoundingBox( min, max );
	}

}
