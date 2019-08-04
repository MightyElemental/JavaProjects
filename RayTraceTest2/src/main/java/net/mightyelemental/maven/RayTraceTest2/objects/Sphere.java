package net.mightyelemental.maven.RayTraceTest2.objects;

import java.util.List;

import net.mightyelemental.maven.RayTraceTest2.App;
import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Utils;
import net.mightyelemental.maven.RayTraceTest2.Vector3f;
import net.mightyelemental.maven.RayTraceTest2.materials.Material;

public class Sphere implements Renderable {

	public Vector3f center;
	public float radius;
	public Vector3f col;

	private Material mat = Material.basic();

	public Sphere(Vector3f center, float radius) {
		this.center = center;
		this.radius = radius;
	}

	public Sphere(float radius) {
		this(new Vector3f(0, 0, 0), radius);
	}

	public boolean intersects(Ray r) {
		// if ( r.getDistanceToPoint(center) > radius ) return false;
		Vector3f l = center.sub(r.getOrig());
		float tca = l.dot(r.getDirection());
		if (tca < 0)
			return false;
		float d2 = l.dot(l) - tca * tca;
		if (d2 > radius * radius)
			return false;
		float thc = (float) Math.sqrt(radius * radius - d2);

		r.t0 = tca - thc;
		r.t1 = tca + thc;

		return true;
	}

	@Deprecated
	public Vector3f shade(Vector3f rayDir, Vector3f hit, List<Light> lights) {
		Vector3f dir = hit.vecTo(lights.get(0).pos).normalize();
		Vector3f lamb = Utils.lambertainShade(getNormal(hit).getUnitVec(), dir, .9f, getColor());
		// Vector3f spec = Utils.specularShade(rayDir, dir, getNormal(hit), .9f, 1f,
		// getColor());
		return getColor().mul(App.ambientCoeff).sum(lamb.mul(1 - App.ambientCoeff));
		// return col != null ? col : new Vector3f(0, 0, 1f);
		// new Color((int) (Math.random() * Math.pow(255, 3)))
	}

	public Vector3f getNormal(Vector3f hit) {
		return center.vecTo(hit);
	}

	public Vector3f getColor() {
		return col == null ? new Vector3f(1, 1, 1) : col;
	}

	@Override
	public void setPos(Vector3f pos) {
		this.center = pos;
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
		return center;
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
