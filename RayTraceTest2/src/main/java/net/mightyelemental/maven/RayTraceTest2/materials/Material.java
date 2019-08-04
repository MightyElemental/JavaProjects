package net.mightyelemental.maven.RayTraceTest2.materials;

public class Material {

	public float reflectiveness = 0, opacity = 1, refractiveIndex = 1;

	public Material(float reflec, float opac, float ior) {
		this.reflectiveness = reflec;
		this.opacity = opac;
		this.refractiveIndex = ior;
	}

	public float getOpacity() {
		return opacity;
	}

	public float getIOR() {
		return refractiveIndex;
	}

	public float getReflectivity() {
		return reflectiveness;
	}

	public static Material basic() {
		return new Material(0f, 1f, 1f);
	}

}
