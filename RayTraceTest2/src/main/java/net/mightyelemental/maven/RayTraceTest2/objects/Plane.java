package net.mightyelemental.maven.RayTraceTest2.objects;

import java.util.List;

import net.mightyelemental.maven.RayTraceTest2.App;
import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Utils;
import net.mightyelemental.maven.RayTraceTest2.Vector3f;

public class Plane implements Renderable {

	public Vector3f	normal, origin;
	public Vector3f	col;
	public float	reflectivity;

	public Plane(Vector3f normal, Vector3f origin) {
		this.normal = normal.getUnitVec();
		this.origin = origin;
	}

	public boolean intersects(Ray r) {
		float d = origin.sub(r.getOrig()).dot(normal) / (normal.dot(r.getDirection()));
		if ( d < 0 ) return false;
		r.t0 = d;
		return true;
	}

	@Deprecated
	public Vector3f shade(Vector3f rayDir, Vector3f hit, List<Light> lights) {
		Vector3f dir = hit.vecTo(lights.get(0).pos).normalize();
		Vector3f lamb = Utils.lambertainShade(getNormal(hit).getUnitVec(), dir, 1f, getColor());
		// Vector3f spec = Utils.specularShade(rayDir, dir, getNormal(hit), 1f, 1f,
		// getColor());
		return getColor().mul(App.ambientCoeff).sum(lamb.mul(1 - App.ambientCoeff));
	}

	public Vector3f getColor() {
		return col == null ? new Vector3f(0, 1, 0) : col;
	}

	public Vector3f getNormal(Vector3f hit) {
		return normal;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	@Override
	public void setPos(Vector3f pos) {
		origin = pos;
	}

	@Override
	public boolean ignoreRay(int depth) {
		return false;
	}

	@Override
	public float getOpacity() {
		return 1;
	}

}
