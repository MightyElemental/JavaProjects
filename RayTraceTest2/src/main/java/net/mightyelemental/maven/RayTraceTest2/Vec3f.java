package net.mightyelemental.maven.RayTraceTest2;

public class Vec3f {

	public float x;
	public float y;
	public float z;

	public Vec3f(float x, float y, float z) {
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

	public Vec3f sum(Vec3f v) {
		return new Vec3f(v.x + x, v.y + y, v.z + z);
	}

	public Vec3f getUnitVec() {
		float l = getLength();
		return new Vec3f(x / l, y / l, z / l);
	}

	public Vec3f normalize() {
		float l = getLength();
		if (l != 0 && l != 1) {
			x /= l;
			y /= l;
			z /= l;
		}
		return this;
	}

	public Vec3f vecTo(Vec3f v) {
		return new Vec3f(v.x - x, v.y - y, v.z - z);
	}

	public float dot(Vec3f v) {
		return x * v.x + y * v.y + z * v.z;
	}

	public float getLength() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public String toString() {
		return "{" + x + "," + y + "," + z + "}";
	}

	public Vec3f sub(Vec3f v) {
		return new Vec3f(x - v.x, y - v.y, z - v.z);
	}

	public Vec3f mul(float s) {
		return new Vec3f(x * s, y * s, z * s);
	}

	public Vec3f getNegative() {
		return new Vec3f(-x, -y, -z);
	}

	public Vec3f negate() {
		this.x = -x;
		this.y = -y;
		this.z = -z;
		return this;
	}

	public Vec3f sum(float s) {
		return new Vec3f(x + s, y + s, z + s);
	}

	public Vec3f mul(Vec3f v) {
		return new Vec3f(x * v.x, y * v.y, z * v.z);
	}

	public static Vec3f origin() {
		return new Vec3f(0, 0, 0);
	}

	public final Vec3f cross(Vec3f B) {
		return new Vec3f(y * B.z - z * B.y, z * B.x - x * B.z, x * B.y - y * B.x);
	}

	public float dot(float f, float g, float h) {
		return dot(new Vec3f(f, g, h));
	}

	public static Vec3f bisect(Vec3f rayDir, Vec3f vecToLight) {
		float len = rayDir.sum(vecToLight).getLength();
		return rayDir.sum(vecToLight).mul(1f / len);
	}

	public Vec3f getReflectedVector(Vec3f normal) {
		return this.sub(normal.mul(2 * this.dot(normal)));
	}
	
	public float getCosOfAngle(Vec3f vec) {
		float dot = this.dot(vec);
		float mul = vec.getLength() * this.getLength();
		return dot/mul;
	}

	public float getAngle(Vec3f vec) {// TODO: might cause instability
		return (float) Math.acos(getCosOfAngle(vec));
	}

	public boolean equals(Object o) {
		if (!(o instanceof Vec3f))
			return false;
		Vec3f v = (Vec3f) o;
		if (v.x == x && v.y == y && v.z == z)
			return true;
		return false;
	}

	public Vec3f removeY() {
		this.y = 0;
		return this;
	}

	public static Vec3f nullVec() {
		return new Vec3f(Float.NaN, Float.NaN, Float.NaN);
	}

	public static boolean isNullVec(Vec3f vec) {
		return Float.isNaN(vec.x) && Float.isNaN(vec.y) && Float.isNaN(vec.z);
	}

}
