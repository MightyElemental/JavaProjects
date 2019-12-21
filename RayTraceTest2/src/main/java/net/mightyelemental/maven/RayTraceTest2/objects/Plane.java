package net.mightyelemental.maven.RayTraceTest2.objects;

import java.util.List;

import net.mightyelemental.maven.RayTraceTest2.App;
import net.mightyelemental.maven.RayTraceTest2.Mat4f;
import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Utils;
import net.mightyelemental.maven.RayTraceTest2.Vec3f;
import net.mightyelemental.maven.RayTraceTest2.materials.Material;

public class Plane implements Renderable {

	public Vec3f normal, origin;
	public Vec3f col;
	public float reflectivity;

	Material mat = Material.basic();

	public Plane(Vec3f normal, Vec3f origin) {
		this.normal = normal.getUnitVec();
		this.origin = origin;
	}

	public boolean intersects(Ray r) {
		float d = origin.sub( r.getOrig() ).dot( normal ) / (normal.dot( r.getDirection() ));
		if (d < 0) return false;
		r.t0 = d;
		return true;
	}

	@Deprecated
	public Vec3f shade(Vec3f rayDir, Vec3f hit, List<Light> lights) {
		Vec3f dir = hit.vecTo( lights.get( 0 ).pos ).normalize();
		Vec3f lamb = Utils.lambertainShade( getNormal( hit, rayDir ).getUnitVec(), dir, 1f,
				getColor() );
		// Vector3f spec = Utils.specularShade(rayDir, dir, getNormal(hit), 1f, 1f,
		// getColor());
		return getColor().mul( App.ambientCoeff ).sum( lamb.mul( 1 - App.ambientCoeff ) );
	}

	public Vec3f getColor() { return col == null ? new Vec3f( 0, 1, 0 ) : col; }

	@Override
	public Vec3f getNormal(Vec3f hit, Vec3f rayDir) {
		if (rayDir.dot( normal ) >= 0) {
			return normal.getNegative();
		} else {
			return normal;
		}
	}

	public float getReflectivity() { return reflectivity; }

	@Override
	public void setPos(Vec3f pos) { origin = pos; }

	@Override
	public boolean isPointWithin(Vec3f vec) {
		Vec3f aMinB = vec.sub( origin );
		return Math.abs( aMinB.dot( normal ) ) <= 0.0001f;
	}

	@Override
	public Vec3f getPos() { return origin; }

	@Override
	public Material getMaterial() { return mat; }

	@Override
	public void setMaterial(Material mat) { this.mat = mat; }

	@Override
	public void setMaterial(float reflec, float opac, float ior) {
		mat = new Material( reflec, opac, ior );
	}

	@Override
	public void setColor(Vec3f vec) { this.col = vec; }

	@Override
	public void translate(Vec3f transVec) { origin = origin.sum( transVec ); }

	@Override
	public void rotate(Vec3f rotVec) {
		Mat4f rotMat = Mat4f.getFullRotationDeg( rotVec );
		normal = rotMat.multiply( normal );
	}

	@Override
	public BoundingBox generateBoundingBox() { return null; }

}
