package net.mightyelemental.maven.RayTraceTest2.objects;

import net.mightyelemental.maven.RayTraceTest2.Vector3f;

public class Triangle extends Plane {

	public Triangle(Vector3f p1, Vector3f p2, Vector3f p3) {
		super(null, p1);
		this.normal = p3.sub(p2).cross(p1.sub(p2)).normalize();
//		float x = (p1.x + p2.x + p3.x) / 3f;
//		float y = (p1.y + p2.y + p3.y) / 3f;
//		float z = (p1.z + p2.z + p3.z) / 3f;
//		this.origin = new Vector3f(x, y, z);
	}

}
