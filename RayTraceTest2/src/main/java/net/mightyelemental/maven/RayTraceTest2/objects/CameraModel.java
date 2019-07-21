package net.mightyelemental.maven.RayTraceTest2.objects;

import net.mightyelemental.maven.RayTraceTest2.Vector3f;

public class CameraModel extends Sphere {

	public CameraModel(Vector3f center) {
		super(center, 1f);
	}

	Vector3f col = new Vector3f(0, 0, 0);

	@Override
	public float getReflectivity() {
		return 0.21f;
	}

	@Override
	public Vector3f getColor() {
		return col;
	}

	public boolean ignoreRay(int depth) {
		return depth == 0;
	}

}
