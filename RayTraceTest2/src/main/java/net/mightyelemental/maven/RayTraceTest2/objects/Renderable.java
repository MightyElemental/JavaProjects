package net.mightyelemental.maven.RayTraceTest2.objects;

import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Vector3f;

public interface Renderable {

	public boolean intersects(Ray r);

	// public Vector3f shade(Vector3f rayDir, Vector3f hit, List<Light> lights);

	public Vector3f getNormal(Vector3f hit);

	public float getReflectivity();

	public Vector3f getColor();

	public void setPos(Vector3f pos);

	/**
	 * Used to hide objects until a certain ray bounce count<br>
	 * Good for hiding objects and only showing them in refractions / reflections
	 */
	public boolean ignoreRay(int depth);

	public default float getOpacity() {
		return 1;
	}

	public default float getIOR() {
		return 1;
	}

}
