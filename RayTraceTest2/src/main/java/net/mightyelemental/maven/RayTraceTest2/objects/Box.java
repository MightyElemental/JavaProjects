package net.mightyelemental.maven.RayTraceTest2.objects;

import java.util.ArrayList;

import net.mightyelemental.maven.RayTraceTest2.Vec3f;

public class Box extends Polyhedron {

	public Box(Vec3f origin, float width, float height, float depth) {
		objs = new ArrayList<Triangle>();
		// 6 sides -> 12 triangles
		this.origin = origin;

		for (int i = 0; i < 2; i++) {
			Vec3f orig = origin.sum( new Vec3f( 0, 0, depth * i ) );
			Vec3f p1 = origin.sum( new Vec3f( width, 0, depth * i ) );
			Vec3f p2 = origin.sum( new Vec3f( 0, height, depth * i ) );
			Vec3f p3 = origin.sum( new Vec3f( width, height, depth * i ) );
			objs.add( new Triangle( orig, p1, p2 ) );
			objs.add( new Triangle( p1, p2, p3 ) );
		}

		for (int i = 0; i < 2; i++) {
			Vec3f orig = origin.sum( new Vec3f( width * i, 0, 0 ) );
			Vec3f p1 = origin.sum( new Vec3f( width * i, height, 0 ) );
			Vec3f p2 = origin.sum( new Vec3f( width * i, 0, depth ) );
			Vec3f p3 = origin.sum( new Vec3f( width * i, height, depth ) );
			objs.add( new Triangle( orig, p1, p3 ) );
			objs.add( new Triangle( orig, p2, p3 ) );
		}

		for (int i = 0; i < 2; i++) {
			Vec3f orig = origin.sum( new Vec3f( 0, height * i, 0 ) );
			Vec3f p1 = origin.sum( new Vec3f( width, height * i, 0 ) );
			Vec3f p2 = origin.sum( new Vec3f( width, height * i, depth ) );
			Vec3f p3 = origin.sum( new Vec3f( 0, height * i, depth ) );
			objs.add( new Triangle( orig, p1, p2 ) );
			objs.add( new Triangle( orig, p2, p3 ) );
		}
	}

	public Box(Vec3f min, Vec3f max) {
		this( min, max.x - min.x, max.y - min.y, max.z - min.z );
	}

}
