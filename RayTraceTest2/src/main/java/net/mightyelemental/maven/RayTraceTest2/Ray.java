package net.mightyelemental.maven.RayTraceTest2;

import java.util.List;

import net.mightyelemental.maven.RayTraceTest2.materials.Material;
import net.mightyelemental.maven.RayTraceTest2.objects.Polyhedron;
import net.mightyelemental.maven.RayTraceTest2.objects.Renderable;

public class Ray {

	private Vec3f direction, start;
	public float  t0    = Float.MAX_VALUE;
	public float  t1    = Float.MAX_VALUE;
	public float  tnear = Float.MAX_VALUE;

	/** The material the ray starts in. */
	public Material startMat = Material.AIR;

	public Ray(Vec3f dir, Vec3f start) {
		this.direction = dir;
		this.start = start;
	}

	public Ray(Vec3f dir, Vec3f start, Material mat) {
		this( dir, start );
		this.startMat = mat;
	}

	public float getDistanceToPoint(Vec3f v) {
		// Vector3f AC = start.vecTo(v);
		//
		// Vector3f unit = direction.getUnitVec();
		//
		// float AD = AC.dot(unit);
		//
		// float ACl = AC.getLength();
		//
		// float dist = (float) Math.sqrt(Math.pow(ACl, 2) - Math.pow(AD, 2));

		// System.out.println(dist);

		return direction.cross( v.sub( start ) ).getLength();
	}

	public Ray getRayToPoint(Vec3f v) {
		Vec3f dir = direction.cross( v.sub( start ) );
		// ray.origin + ray.direction * Vector3.Dot(ray.direction, point - ray.origin)
		Vec3f sPoint = start
				.sum( direction.mul( direction.dot( v.sub( start ) ) ) );

		return new Ray( dir, sPoint );
	}

	public Vec3f getDirection() { return direction; }

	public Vec3f getOrig() { return start; }

	public Renderable trace(List<Renderable> objects, int depth) {
		Renderable closest = null;
		float tnear = Float.MAX_VALUE;
		for (Renderable o : objects) {
			if (o.ignoreRay( depth )) continue;
			t0 = Float.MAX_VALUE;
			t1 = Float.MAX_VALUE;
			if (o instanceof Polyhedron) {
//				ComplexRenderable compO = ((ComplexRenderable) o);
//				if (compO.boundingBox.intersects( this )) {// TODO: FIX PERSPECTIVE ISSUE
//					List<Renderable> compRends = new ArrayList<Renderable>();
//					compO.objs.forEach( e -> compRends.add( e ) );
//					Renderable subRend = trace( compRends, depth );
//					if (t0 < 0) t0 = t1;
//					if (t0 < tnear) {
//						tnear = t0;
//						closest = subRend;
//					}
//				}
			}
			if (!o.intersects( this )) continue;
			if (t0 < 0) t0 = t1;
			if (t0 < tnear) {
				tnear = t0;
				closest = o;
			}

		}
		this.tnear = tnear;
		return closest;
	}

	public Vec3f getHitPoint() { return start.sum( direction.mul( tnear ) ); }

	public Vec3f getReflectedVector(Vec3f normal) {
		return direction.getReflectedVector( normal );
	}

	public void setOrig(Vec3f pos) { this.start = pos; }

	// ior = n1/n2
	public Vec3f getRefractionVector(float ior, Vec3f normToObj) {
//		Vector3f t = this.getDirection().mul(ior);
		// double cosA = this.getDirection().getCosOfAngle(normToObj);
//		double sin2ot = ior * ior * (1 - cosA * cosA);
//		//System.out.println(sin2ot);
//		if (sin2ot > 1)
//			return null;
//		Vector3f print = normToObj.mul((float) (ior * cosA - Math.sqrt(1 - sin2ot)));
//		t = t.sum(print);
//		//System.out.println(t + "|" + this.getDirection());
//		// System.out.println(ior + " | " + getDirection() + " | " + t + " | " + angle +
//		// " | " + print);
//		return t.normalize();

		// float c1 = (float) cosA;// normToObj.dot(getDirection());
		// float c2 = (float) Math.sqrt(1 - ior * ior * (1 - cosA * cosA));

		// return getDirection().mul(ior).sum(normToObj.mul(ior * c1 - c2)).normalize();

		// from wolfgang: self.direction * eta - inter.normal * (-n_dot_d + eta * n_dot_d);
		float eta = 2 - ior;
		float ndt = normToObj.dot( getDirection() );
		return getDirection().mul( eta ).sub( normToObj.mul( -ndt + eta * ndt ) );

	}

	public Material getStartingMaterial() { return startMat; }

}
