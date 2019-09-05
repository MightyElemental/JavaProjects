package net.mightyelemental.maven.RayTraceTest2.objects;

import net.mightyelemental.maven.RayTraceTest2.Vector3f;

public class PolyPoint {

	public Vector3f location;
	public Vector3f uvMapping;
	public Vector3f normal;

	public PolyPoint(Vector3f loc, Vector3f uv, Vector3f norm) {
		this.location = loc;
		this.uvMapping = uv;
		this.normal = norm;
	}

	public PolyPoint(Vector3f loc) {
		this.location = loc;
	}

	public static PolyPoint empty() {
		return new PolyPoint(null, null, null);
	}

}
