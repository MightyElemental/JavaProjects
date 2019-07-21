package net.mightyelemental.maven.RayTraceTest2;

import java.util.Arrays;

public class Mat4f {

	float[] mat = new float[4 * 4];

	public Mat4f(float[] mat) {
		this.mat = mat;
	}

	public static Mat4f getYRotationDeg(float deg) {
		float sin = (float) Math.sin(Math.toRadians(deg));
		float cos = (float) Math.cos(Math.toRadians(deg));
		return new Mat4f(new float[] { cos, 0, sin, 0,
				0, 1, 0, 0, 
				-sin, 0, cos, 0,
				0, 0, 0, 1});
	}

	public static Mat4f getXRotationDeg(float deg) {
		float sin = (float) Math.sin(Math.toRadians(deg));
		float cos = (float) Math.cos(Math.toRadians(deg));
		return new Mat4f(new float[] { 1, 0, 0, 0,
				0, cos, -sin, 0,
				0, sin, cos, 0,
				0, 0, 0, 1});
	}

	public static Mat4f getZRotationDeg(float deg) {
		float sin = (float) Math.sin(Math.toRadians(deg));
		float cos = (float) Math.cos(Math.toRadians(deg));
		return new Mat4f(new float[] { cos, -sin, 0, 0,
				sin, cos, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1 });
	}

	public static Mat4f getIdentity() {
		return new Mat4f(new float[] { 1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1});
	}

	public static Vector3f multiply(Mat4f mat, Vector3f vec) {
		float nx = mat.get(0, 0) * vec.x + mat.get(1, 0) * vec.y + mat.get(2, 0) * vec.z + mat.get(3, 0);
		float ny = mat.get(0, 1) * vec.x + mat.get(1, 1) * vec.y + mat.get(2, 1) * vec.z + mat.get(3, 1);
		float nz = mat.get(0, 2) * vec.x + mat.get(1, 2) * vec.y + mat.get(2, 2) * vec.z + mat.get(3, 2);
		return new Vector3f(nx, ny, nz);
	}

	public static Mat4f multiply(Mat4f matA, Mat4f matB) {
		float[] product = new float[4 * 4];
		for ( int i = 0; i < 4; i++ ) {
			for ( int j = 0; j < 4; j++ ) {
				for ( int k = 0; k < 4; k++ ) {
					product[i + j * 4] += matA.get(i, k) * matB.get(k, j);
				}
			}
		}
		return new Mat4f(product);
	}

	public Vector3f multiply(Vector3f vec) {
		return multiply(this, vec);
	}

	public Mat4f multiply(Mat4f mat) {
		return multiply(this, mat);
	}

	public float get(int v) {
		if ( v > 15 || v < 0 ) return Integer.MAX_VALUE;
		return mat[v];
	}

	public float get(int x, int y) {
		if ( x > 4 || x < 0 ) return Integer.MAX_VALUE;
		if ( y > 4 || y < 0 ) return Integer.MAX_VALUE;
		return mat[x + y * 4];
	}

	public String toString() {
		return Arrays.toString(mat);
	}

}
