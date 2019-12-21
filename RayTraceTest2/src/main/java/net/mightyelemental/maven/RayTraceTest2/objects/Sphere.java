package net.mightyelemental.maven.RayTraceTest2.objects;

import java.util.List;

import net.mightyelemental.maven.RayTraceTest2.App;
import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Utils;
import net.mightyelemental.maven.RayTraceTest2.Vec3f;
import net.mightyelemental.maven.RayTraceTest2.materials.Material;

public class Sphere implements Renderable {

	public Vec3f center;
	public float radius;
	public Vec3f col;

	private Material mat = Material.basic();

	public Sphere(Vec3f center, float radius) { this.center = center; this.radius = radius; }

	public Sphere(float radius) { this( new Vec3f( 0, 0, 0 ), radius ); }

	public boolean intersects(Ray r) {
		// if ( r.getDistanceToPoint(center) > radius ) return false;
		Vec3f l = center.sub( r.getOrig() );
		float tca = l.dot( r.getDirection() );
		if (tca < 0) return false;
		float d2 = l.dot( l ) - tca * tca;
		if (d2 > radius * radius) return false;
		float thc = (float) Math.sqrt( radius * radius - d2 );

		r.t0 = tca - thc;
		r.t1 = tca + thc;

		return true;
	}

	@Deprecated
	public Vec3f shade(Vec3f rayDir, Vec3f hit, List<Light> lights) {
		Vec3f dir = hit.vecTo( lights.get( 0 ).pos ).normalize();
		Vec3f lamb = Utils.lambertainShade( getNormal( hit, rayDir ).getUnitVec(), dir, .9f,
				getColor() );
		// Vector3f spec = Utils.specularShade(rayDir, dir, getNormal(hit), .9f, 1f,
		// getColor());
		return getColor().mul( App.ambientCoeff ).sum( lamb.mul( 1 - App.ambientCoeff ) );
		// return col != null ? col : new Vector3f(0, 0, 1f);
		// new Color((int) (Math.random() * Math.pow(255, 3)))
	}

	public Vec3f getNormal(Vec3f hit, Vec3f rayDir) { return center.vecTo( hit ); }

	public Vec3f getColor() { return col == null ? new Vec3f( 1, 1, 1 ) : col; }

	@Override
	public void setPos(Vec3f pos) { this.center = pos; }

	@Override
	public boolean ignoreRay(int depth) { return false; }

	@Override
	public boolean isPointWithin(Vec3f vec) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Vec3f getPos() { return center; }

	@Override
	public Material getMaterial() { return mat; }

	@Override
	public void setMaterial(Material mat) { this.mat = mat; }

	@Override
	public void setMaterial(float reflec, float opac, float ior) {
		mat = new Material( reflec, opac, ior );
	}

	@Override
	public void setColor(Vec3f vec) { col = vec; }

	@Override
	public void translate(Vec3f transVec) { center = center.sum( transVec ); }

	@Override
	public void rotate(Vec3f rotVec) {
		// nothing to rotate.
	}

	@Override
	public BoundingBox generateBoundingBox() {// VERIFY the box is accurate...
		Vec3f min = this.center.sub( new Vec3f( radius, radius, radius ) );
		Vec3f max = this.center.sum( new Vec3f( radius, radius, radius ) );
		return new BoundingBox( min, max );
	}

}
