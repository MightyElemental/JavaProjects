package net.mightyelemental.maven.RayTraceTest2.objects;

import java.util.List;

import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Vector3f;
import net.mightyelemental.maven.RayTraceTest2.materials.Material;

public class ComplexRenderable implements Renderable {

	public List<Renderable> objs;
	public Vector3f origin;

	private Material mat = Material.basic();

	public ComplexRenderable(Vector3f origin, List<Renderable> objects) {
		objs = objects;
		this.origin = origin;
	}

	@Override
	public boolean intersects(Ray r) {
		for (Renderable rend : objs) {
			if (rend.intersects(r))
				return true;
		}
		return false;
	}

	@Override
	public Vector3f getNormal(Vector3f hit) {
		for (Renderable rend : objs) {
			if (rend.isPointWithin(hit))
				return rend.getNormal(hit);
		}
		return Vector3f.nullVec();
	}

	@Override
	public Vector3f getColor() {
		// TODO Auto-generated method stub
		return new Vector3f(0.5f, 0.5f, 0);
	}

	@Override
	public void setPos(Vector3f pos) {
		Vector3f diff = origin.sub(pos);
		for (Renderable rend : objs) {
			rend.setPos(rend.getPos().sum(diff));
		}
	}

	@Override
	public boolean isPointWithin(Vector3f vec) {
		for (Renderable rend : objs) {
			if (rend.isPointWithin(vec))
				return true;
		}
		return false;
	}

	@Override
	public Vector3f getPos() {
		return origin;
	}

	@Override
	public Material getMaterial() {
		return mat;
	}

	@Override
	public void setMaterial(Material mat) {
		this.mat = mat;
	}

	@Override
	public void setMaterial(float reflec, float opac, float ior) {
		mat = new Material(reflec, opac, ior);
	}

}
