package net.mightyelemental.maven.RayTraceTest2.objects;

import java.util.ArrayList;

import net.mightyelemental.maven.RayTraceTest2.Vector3f;

public class Box extends ComplexRenderable {

	public Box(Vector3f origin, float width, float height, float depth) {
		objs = new ArrayList<Renderable>();
		// 6 sides -> 12 triangles
		this.origin = origin;

		for (int i = 0; i < 2; i++) {
			Vector3f orig = origin.sum(new Vector3f(0, 0, depth * i));
			Vector3f p1 = origin.sum(new Vector3f(width, 0, depth * i));
			Vector3f p2 = origin.sum(new Vector3f(0, height, depth * i));
			Vector3f p3 = origin.sum(new Vector3f(width, height, depth * i));
			objs.add(new Triangle(orig, p1, p2));
			objs.add(new Triangle(p1, p2, p3));
		}

		for (int i = 0; i < 2; i++) {
			Vector3f orig = origin.sum(new Vector3f(width * i, 0, 0));
			Vector3f p1 = origin.sum(new Vector3f(width * i, height, 0));
			Vector3f p2 = origin.sum(new Vector3f(width * i, 0, depth));
			Vector3f p3 = origin.sum(new Vector3f(width*i, height, depth));
			objs.add(new Triangle(orig, p1, p3));
			objs.add(new Triangle(orig, p2, p3));
		}
		
		for (int i = 0; i < 2; i++) {
			Vector3f orig = origin.sum(new Vector3f(0, height*i, 0));
			Vector3f p1 = origin.sum(new Vector3f(width, height*i, 0));
			Vector3f p2 = origin.sum(new Vector3f(width, height*i, depth));
			Vector3f p3 = origin.sum(new Vector3f(0, height*i, depth));
			objs.add(new Triangle(orig, p1, p2));
			objs.add(new Triangle(orig, p2, p3));
		}
	}

}
