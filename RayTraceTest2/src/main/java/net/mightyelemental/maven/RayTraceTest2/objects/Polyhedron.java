package net.mightyelemental.maven.RayTraceTest2.objects;

import java.util.List;

import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Vec3f;
import net.mightyelemental.maven.RayTraceTest2.materials.Material;

public class Polyhedron implements Renderable {

	public List<Triangle> objs;
	public Vec3f          origin;

	private Material mat = Material.basic();

	protected Polyhedron() {}

	public Polyhedron(List<Triangle> objects) {
		objs = objects;
		for (Triangle rend : objs) { rend.setMaterial( mat ); }
		BoundingBox b = this.generateBoundingBox();
		origin = b.getCenter();
	}

	@Override
	public boolean intersects(final Ray r) {
		boolean flag = false;
		for (Triangle rend : objs) { if (rend.intersects( r )) flag = true; }
		return flag;
	}

	@Override
	public Vec3f getNormal(Vec3f hit, Vec3f rayDir) {
		for (Triangle rend : objs) {
			if (rend.isPointWithin( hit )) return rend.getNormal( hit, rayDir );
		}
		return Vec3f.nullVec();
	}

	@Override
	public Vec3f getColor() { return new Vec3f( 0.5f, 0.5f, 0 ); }

	@Override
	public void setPos(Vec3f pos) {
		Vec3f diff = origin.sub( pos );
		for (Triangle rend : objs) { rend.setPos( rend.getPos().sum( diff ) ); }
	}

	@Override
	public boolean isPointWithin(Vec3f vec) {
		for (Triangle rend : objs) { if (rend.isPointWithin( vec )) return true; }
		return false;
	}

	@Override
	public Vec3f getPos() { return origin; }

	@Override
	public Material getMaterial() { return mat; }

	@Override
	public void setMaterial(Material mat) {
		this.mat = mat;
		for (Triangle rend : objs) { rend.setMaterial( mat ); }
	}

	/**
	 * Used to set the material of the object
	 * 
	 * @param reflec - the reflectivity value ranging from 0-1
	 * @param opac   - the opacity value ranging from 0-1
	 * @param ior    - the index of refraction value
	 */
	@Override
	public void setMaterial(float reflec, float opac, float ior) {
		mat = new Material( reflec, opac, ior );
		for (Triangle rend : objs) { rend.setMaterial( mat ); }
	}

	/**
	 * Set the color for all tris that make up the polyhedron
	 * 
	 * @param col - the color in vector form. Each value should range from 0-1
	 */
	public void setColor(Vec3f col) { for (Triangle rend : objs) { rend.setColor( col ); } }

	/** Used to assign a random color to each of the triangles in the polyhedron */
	public void useRandomColors() {
		for (Triangle rend : objs) {
			Vec3f col = new Vec3f( (float) Math.random(), (float) Math.random(),
					(float) Math.random() );
			rend.setColor( col );
		}
	}

	@Override
	public void translate(Vec3f transVec) {
		for (Triangle rend : objs) { rend.translate( transVec ); }
	}

	@Override
	public void rotate(Vec3f rotVec) { for (Triangle rend : objs) { rend.rotate( rotVec ); } }

	@Override
	public BoundingBox generateBoundingBox() {
		Vec3f min = new Vec3f( Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE );
		Vec3f max = new Vec3f( Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE );

		for (Triangle rend : objs) {
			Vec3f a[] = {
					rend.p1.location, rend.p2.location, rend.p3.location
			};
			for (Vec3f pp : a) {
				if (pp.x < min.x) { min.x = pp.x; }
				if (pp.x > max.x) { max.x = pp.x; }
				if (pp.y < min.y) { min.y = pp.y; }
				if (pp.y > max.y) { max.y = pp.y; }
				if (pp.z < min.z) { min.z = pp.z; }
				if (pp.z > max.z) { max.z = pp.z; }
			}
		}
		//System.out.println( min );
		//System.out.println( max );
		return new BoundingBox( min, max );
	}

}
