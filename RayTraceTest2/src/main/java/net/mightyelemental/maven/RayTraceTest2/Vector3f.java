package net.mightyelemental.maven.RayTraceTest2;

public class Vector3f {

	public float x;
	public float y;
	public float z;

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public void addX(float x) {
		this.x += x;
	}

	public void addY(float y) {
		this.y += y;
	}

	public void addZ(float z) {
		this.z += z;
	}

	public Vector3f sum(Vector3f v) {
		return new Vector3f(v.x + x, v.y + y, v.z + z);
	}

	public Vector3f getUnitVec() {
		float l = getLength();
		return new Vector3f(x / l, y / l, z / l);
	}

	public Vector3f normalize() {
		float l = getLength();
		if (l != 0 && l != 1) {
			x /= l;
			y /= l;
			z /= l;
		}
		return this;
	}

	public Vector3f vecTo(Vector3f v) {
		return new Vector3f(v.x - x, v.y - y, v.z - z);
	}

	public float dot(Vector3f v) {
		return x * v.x + y * v.y + z * v.z;
	}

	public float getLength() {
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}

	public String toString() {
		return "{" + x + "," + y + "," + z + "}";
	}

	public Vector3f sub(Vector3f v) {
		return new Vector3f(x - v.x, y - v.y, z - v.z);
	}

	public Vector3f mul(float s) {
		return new Vector3f(x * s, y * s, z * s);
	}

	public Vector3f getNegative() {
		return new Vector3f(-x, -y, -z);
	}

	public Vector3f negate() {
		this.x = -x;
		this.y = -y;
		this.z = -z;
		return this;
	}

	public Vector3f sum(float s) {
		return new Vector3f(x + s, y + s, z + s);
	}

	public Vector3f mul(Vector3f v) {
		return new Vector3f(x * v.x, y * v.y, z * v.z);
	}

	public static Vector3f origin() {
		return new Vector3f(0, 0, 0);
	}

	public final Vector3f cross(Vector3f B) {
		return new Vector3f(y * B.z - z * B.y, z * B.x - x * B.z, x * B.y - y * B.x);
	}

	public float dot(float f, float g, float h) {
		return dot(new Vector3f(f, g, h));
	}

	public static Vector3f bisect(Vector3f rayDir, Vector3f vecToLight) {
		float len = rayDir.sum(vecToLight).getLength();
		return rayDir.sum(vecToLight).mul(1f / len);
	}

	public Vector3f getReflectedVector(Vector3f normal) {
		return this.sub(normal.mul(2 * this.dot(normal)));
	}

	public float getAngle(Vector3f vec) {// TODO: might cause instability
		float dot = this.dot(vec);
		float mul = vec.getLength() * this.getLength();
		return (float) Math.acos(dot / mul);
	}

	public boolean equals(Object o) {
		if (!(o instanceof Vector3f))
			return false;
		Vector3f v = (Vector3f) o;
		if (v.x == x && v.y == y && v.z == z)
			return true;
		return false;
	}

	public Vector3f removeY() {
		this.y = 0;
		return this;
	}

}
