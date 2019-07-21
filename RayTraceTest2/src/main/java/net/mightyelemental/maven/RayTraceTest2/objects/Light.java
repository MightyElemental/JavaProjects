package net.mightyelemental.maven.RayTraceTest2.objects;

import net.mightyelemental.maven.RayTraceTest2.Vector3f;

public class Light {

	public Vector3f	pos, color;
	public float	brightness = 1;

	public Light(float x, float y, float z) {
		this(new Vector3f(x, y, z));
	}

	public Light(Vector3f pos) {
		this.pos = pos;
		this.color = new Vector3f(1, 1, 1);
	}

}
