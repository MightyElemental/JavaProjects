package net.mightyelemental.maven.RayTraceTest2.materials;

/**
 * The material controls different aspects of an object.<br>
 * Currently, a material can control the reflectivity, the opacity and the refractive index of an object
 */
public class Material {

	public static final Material AIR = new Material( 0, 0, 1 );

	public float reflectiveness = 0, opacity = 1, refractiveIndex = 1;

	public Material(float reflec, float opac, float ior) {
		this.reflectiveness = reflec;
		this.opacity = opac;
		this.refractiveIndex = ior;
	}

	public float getOpacity() { return opacity; }

	public float getIOR() { return refractiveIndex; }

	public float getReflectivity() { return reflectiveness; }

	public static Material basic() { return new Material( 0f, 1f, 1f ); }

	public static Material air() { return AIR; }

}
