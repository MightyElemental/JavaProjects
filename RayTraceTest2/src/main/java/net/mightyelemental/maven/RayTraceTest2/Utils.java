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

}
