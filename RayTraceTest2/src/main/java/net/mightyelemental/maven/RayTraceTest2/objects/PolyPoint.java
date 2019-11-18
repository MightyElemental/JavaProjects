package net.mightyelemental.maven.RayTraceTest2.objects;

import net.mightyelemental.maven.RayTraceTest2.Vec3f;

public class PolyPoint {

	public Vec3f location;
	public Vec3f uvMapping;
	public Vec3f normal;

	public PolyPoint(Vec3f loc, Vec3f uv, Vec3f norm) {
		this.location = loc;
		this.uvMapping = uv;
		this.normal = norm;
	}

	public PolyPoint(Vec3f loc) {
		this.location = loc;
	}

	public static PolyPoint empty() {
		return new PolyPoint(null, null, null);
	}

}
