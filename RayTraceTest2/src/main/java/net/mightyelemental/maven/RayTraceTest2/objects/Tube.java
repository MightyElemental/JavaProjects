package net.mightyelemental.maven.RayTraceTest2.objects;

import net.mightyelemental.maven.RayTraceTest2.Mat4f;
import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Vec3f;
import net.mightyelemental.maven.RayTraceTest2.materials.Material;

public class Tube implements Renderable {

	public Ray   defLine;
	public float length, radius;

	private Material mat = Material.basic();

	public Tube(Vec3f orient, float length, float radius) {
		this.defLine = new Ray( orient.getUnitVec(), new Vec3f( 0, 0, 0 ) );
		this.length = length;
		this.radius = radius;
	}

	public Tube(Vec3f orient, Vec3f pos, float length, float radius) {
		this( orient, length, radius );
		defLine.setOrig( pos );
	}

	@Override
	public boolean intersects(Ray r) {
		Vec3f v = r.getDirection();
		Vec3f va = defLine.getDirection().getUnitVec();
		Vec3f pa = defLine.getOrig();
		Vec3f p = r.getOrig();
		Vec3f delP = p.sub( pa );
		float B = 2 * (v.sub( va.mul( v.dot( va ) ) )).dot( delP.sub( va.mul( delP.dot( va ) ) ) );
		Vec3f Av = v.sub( va.mul( v.dot( va ) ) );
		float A = Av.dot( Av );
		Vec3f Cv = delP.sub( va.mul( delP.dot( va ) ) );
		float C = Cv.dot( Cv ) - radius * radius;
		float det = B * B - 4 * A * C;
		// if ( det < 0 ) return false;
		if (va.dot( delP ) <= 0) return false;

		det = (float) Math.sqrt( det );
		r.t0 = -B - det / (2 * A);
		r.t1 = -B + det / (2 * A);
		if (r.t0 < 0 && r.t1 < 0) return false;

		if (r.t0 < 0) r.t0 = r.t1;
		Vec3f hit = p.sum( v.mul( r.t0 ) );
		if (hit.sub( pa ).dot( hit.sub( pa ) ) > radius * radius) return false;

		return true;
	}

	@Override
	public Vec3f getNormal(Vec3f hit, Vec3f rayDir) {
		return defLine.getRayToPoint( hit ).getDirection();
	}

	@Override
	public Vec3f getColor() {
		// TODO Auto-generated method stub
		return new Vec3f( 0.5f, 0, 0.5f );
	}

	@Override
	public void setPos(Vec3f pos) { defLine.setOrig( pos ); }

	@Override
	public boolean ignoreRay(int depth) { return false; }

	@Override
	public boolean isPointWithin(Vec3f vec) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Vec3f getPos() { return defLine.getOrig(); }

	@Override
	public Material getMaterial() { return mat; }

	@Override
	public void setMaterial(Material mat) { this.mat = mat; }

	@Override
	public void setMaterial(float reflec, float opac, float ior) {
		mat = new Material( reflec, opac, ior );
	}

	@Override
	public void setColor(Vec3f vec) {

	}

	@Override
	public void translate(Vec3f transVec) { defLine.setOrig( defLine.getOrig().sum( transVec ) ); }

	@Override
	public void rotate(Vec3f rotVec) {
		Mat4f rotMat = Mat4f.getFullRotationDeg( rotVec );
		defLine.setOrig( rotMat.multiply( defLine.getOrig() ) );
		// TODO: add rotation for direction
	}

	@Override
	public BoundingBox generateBoundingBox() { // TODO Auto-generated method stub
		return null;
	}

}
