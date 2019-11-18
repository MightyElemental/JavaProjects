package net.mightyelemental.maven.RayTraceTest2.objects;

import net.mightyelemental.maven.RayTraceTest2.Vec3f;

public class Light {

	public Vec3f	pos, color;
	public float	brightness = 1;

	public Light(float x, float y, float z) {
		this(new Vec3f(x, y, z));
	}

	public Light(Vec3f pos) {
		this.pos = pos;
		this.color = new Vec3f(1, 1, 1);
	}

}
