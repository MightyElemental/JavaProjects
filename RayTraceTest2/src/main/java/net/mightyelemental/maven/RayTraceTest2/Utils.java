package net.mightyelemental.maven.RayTraceTest2;

public class Utils {

	public static Vector3f lambertainShade(Vector3f normal, Vector3f vecToLight, float diffuseCoeff, Vector3f color) {
		return color.mul(diffuseCoeff * Math.max(0, normal.dot(vecToLight)));
	}

	public static Vector3f specularShade(Vector3f rayDir, Vector3f vecToLight, Vector3f normal, float specCoeff, float shininess,
			Vector3f color) {
		Vector3f h = Vector3f.bisect(rayDir, vecToLight);
		float gamma = (int) Math.pow(2, 2);
		float lambda = 1 - vecToLight.getReflectedVector(normal).dot(rayDir);
		float beta = shininess / gamma;
		float max = Math.max(0, 1 - beta * lambda);
		return color.mul(specCoeff * (float) Math.pow(max, gamma));
	}
	
	public static TexData convertVecToCubeUV(String skyboxImg, Vector3f vec) {
		TexData td = new TexData();
		float absX = Math.abs(vec.x);
		float absY = Math.abs(vec.y);
		float absZ = Math.abs(vec.z);

		boolean isXPositive = vec.x > 0;
		boolean isYPositive = vec.y > 0;
		boolean isZPositive = vec.z > 0;

		float maxAxis = 1, uc = 0, vc = 0;

		if (isXPositive && absX >= absY && absX >= absZ) {
			maxAxis = absX;
			uc = -vec.z;
			vc = vec.y;
			td.img = ResourceLoader.loadImage(skyboxImg + "side_0.png");
		}
		if (!isXPositive && absX >= absY && absX >= absZ) {
			maxAxis = absX;
			uc = vec.z;
			vc = vec.y;
			td.img = ResourceLoader.loadImage(skyboxImg + "side_1.png");
		}
		if (isYPositive && absY >= absX && absY >= absZ) {
			maxAxis = absY;
			uc = vec.x;
			vc = -vec.z;
			td.img = ResourceLoader.loadImage(skyboxImg + "side_2.png");
		}
		if (!isYPositive && absY >= absX && absY >= absZ) {
			maxAxis = absY;
			uc = vec.x;
			vc = vec.z;
			td.img = ResourceLoader.loadImage(skyboxImg + "side_3.png");
		}
		if (isZPositive && absZ >= absX && absZ >= absY) {
			maxAxis = absZ;
			uc = vec.x;
			vc = vec.y;
			td.img = ResourceLoader.loadImage(skyboxImg + "side_4.png");
		}
		if (!isZPositive && absZ >= absX && absZ >= absY) {
			maxAxis = absZ;
			uc = -vec.x;
			vc = vec.y;
			td.img = ResourceLoader.loadImage(skyboxImg + "side_5.png");
		}
		td.uvX = 0.5f * (uc / maxAxis + 1f);
		td.uvY = 0.5f * (vc / maxAxis + 1.0f);
		return td;
	}

}
