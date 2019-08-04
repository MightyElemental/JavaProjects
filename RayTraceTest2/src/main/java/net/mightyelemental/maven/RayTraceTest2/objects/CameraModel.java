package net.mightyelemental.maven.RayTraceTest2.objects;

import net.mightyelemental.maven.RayTraceTest2.Vector3f;
import net.mightyelemental.maven.RayTraceTest2.materials.Material;

public class CameraModel extends Sphere {

	Material mat = new Material(0.21f, 1f, 1f);

	public CameraModel(Vector3f center) {
		super(center, 1f);
	}

	Vector3f col = new Vector3f(0, 0, 0);

	@Override
	public Vector3f getColor() {
		return col;
	}

	public boolean ignoreRay(int depth) {
		return depth == 0;
	}

	@Override
	public Material getMaterial() {
		return mat;
	}

}
