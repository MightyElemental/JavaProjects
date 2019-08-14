package net.mightyelemental.maven.RayTraceTest2.objects;

import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Vector3f;
import net.mightyelemental.maven.RayTraceTest2.materials.Material;

public class Tube implements Renderable {

	public Ray defLine;
	public float length, radius;

	private Material mat = Material.basic();

	public Tube(Vector3f orient, float length, float radius) {
		this.defLine = new Ray(orient.getUnitVec(), new Vector3f(0, 0, 0));
		this.length = length;
		this.radius = radius;
	}

	public Tube(Vector3f orient, Vector3f pos, float length, float radius) {
		this(orient, length, radius);
		defLine.setOrig(pos);
	}

	@Override
	public boolean intersects(Ray r) {
		Vector3f v = r.getDirection();
		Vector3f va = defLine.getDirection().getUnitVec();
		Vector3f pa = defLine.getOrig();
		Vector3f p = r.getOrig();
		Vector3f delP = p.sub(pa);
		float B = 2 * (v.sub(va.mul(v.dot(va)))).dot(delP.sub(va.mul(delP.dot(va))));
		Vector3f Av = v.sub(va.mul(v.dot(va)));
		float A = Av.dot(Av);
		Vector3f Cv = delP.sub(va.mul(delP.dot(va)));
		float C = Cv.dot(Cv) - radius * radius;
		float det = B * B - 4 * A * C;
		// if ( det < 0 ) return false;
		if (va.dot(delP) <= 0)
			return false;

		det = (float) Math.sqrt(det);
		r.t0 = -B - det / (2 * A);
		r.t1 = -B + det / (2 * A);
		if (r.t0 < 0 && r.t1 < 0)
			return false;

		if (r.t0 < 0)
			r.t0 = r.t1;
		Vector3f hit = p.sum(v.mul(r.t0));
		if (hit.sub(pa).dot(hit.sub(pa)) > radius * radius)
			return false;

		return true;
	}

	@Override
	public Vector3f getNormal(Vector3f hit, Vector3f rayDir) {
		return defLine.getRayToPoint(hit).getDirection();
	}

	@Override
	public Vector3f getColor() {
		// TODO Auto-generated method stub
		return new Vector3f(0.5f, 0, 0.5f);
	}

	@Override
	public void setPos(Vector3f pos) {
		defLine.setOrig(pos);
	}

	@Override
	public boolean ignoreRay(int depth) {
		return false;
	}

	@Override
	public boolean isPointWithin(Vector3f vec) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Vector3f getPos() {
		return defLine.getOrig();
	}

	@Override
	public Material getMaterial() {
		return mat;
	}

	@Override
	public void setMaterial(Material mat) {
		this.mat = mat;
	}

	@Override
	public void setMaterial(float reflec, float opac, float ior) {
		mat = new Material(reflec, opac, ior);
	}

}
