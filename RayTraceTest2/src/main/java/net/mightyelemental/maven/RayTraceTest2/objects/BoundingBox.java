package net.mightyelemental.maven.RayTraceTest2.objects;

import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Vec3f;

public class BoundingBox {

	public Vec3f min, max;

	public BoundingBox(Vec3f min, Vec3f max) { this.min = min; this.max = max; }
	
	private static final BoundingBox nullBox = new BoundingBox(Vec3f.nullVec(), Vec3f.nullVec());
	private static final BoundingBox infBox = new BoundingBox(Vec3f.minVec(), Vec3f.maxVec());

	/**
	 * Tests if a ray will intersect with the bounding box
	 * 
	 * @param r - the ray you are testing for intersection with
	 * @return true if the ray intersects
	 */
	public boolean intersects(final Ray r) {
		Vec3f invDir;
		float tmin, tmax, tymin, tymax, tzmin, tzmax;
		invDir = new Vec3f( 1f / r.getDirection().x, 1f / r.getDirection().y,
				1f / r.getDirection().z );
		boolean signX = r.getDirection().x < 0;
		boolean signY = r.getDirection().y < 0;
		boolean signZ = r.getDirection().z < 0;
		tmin = (bbox_bounds( signX ).x - r.getOrig().x) * invDir.x;
		tmax = (bbox_bounds( !signX ).x - r.getOrig().x) * invDir.x;
		tymin = (bbox_bounds( signY ).y - r.getOrig().y) * invDir.y;
		tymax = (bbox_bounds( !signY ).y - r.getOrig().y) * invDir.y;
		if (tmin > tymax || tymin > tmax) return (false);
		if (tymin > tmin) tmin = tymin;
		if (tymax < tmax) tmax = tymax;
		tzmin = (bbox_bounds( signZ ).z - r.getOrig().z) * invDir.z;
		tzmax = (bbox_bounds( !signZ ).z - r.getOrig().z) * invDir.z;
		if (tmin > tzmax || tzmin > tmax) return (false);
		if (tzmin > tmin) tmin = tzmin;
		if (tzmax < tmax) tmax = tzmax;
		return (tmin >= 0 || tmax >= 0);
	}

	private Vec3f bbox_bounds(boolean wantMax) {
		if (wantMax)
			return (max);
		else
			return (min);
	}

	public Vec3f getCenter() { return min.mul( 0.5f ).sum( max.mul( 0.5f ) ); }
	
	public boolean isNull() { return min.isNullVec() || max.isNullVec(); }

	public static BoundingBox nullBox() { return nullBox; }
	
	public static BoundingBox infBox() { return infBox; }
	
	public boolean isInfinite() { return min.isMinVec() || max.isMaxVec(); } 

}
