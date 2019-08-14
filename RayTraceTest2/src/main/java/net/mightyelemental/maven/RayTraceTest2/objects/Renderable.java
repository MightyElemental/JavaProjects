package net.mightyelemental.maven.RayTraceTest2.objects;

import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Vector3f;
import net.mightyelemental.maven.RayTraceTest2.materials.Material;

public interface Renderable {

	public boolean intersects(Ray r);

	/** Used to test if a point intersects with the renderable object */
	public boolean isPointWithin(Vector3f vec);

	// public Vector3f shade(Vector3f rayDir, Vector3f hit, List<Light> lights);

	public Vector3f getNormal(Vector3f hit, Vector3f rayDir);

	public Vector3f getColor();

	public void setPos(Vector3f pos);
	
	public Material getMaterial();
	
	public void setMaterial(Material mat);
	
	public void setMaterial(float reflec, float opac, float ior);

	/**
	 * Used to hide objects until a certain ray bounce count<br>
	 * Good for hiding objects and only showing them in refractions / reflections
	 */
	public default boolean ignoreRay(int depth) {
		return false;
	}

	public Vector3f getPos();

}
