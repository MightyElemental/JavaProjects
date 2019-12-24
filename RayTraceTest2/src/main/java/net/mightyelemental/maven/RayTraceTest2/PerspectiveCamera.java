package net.mightyelemental.maven.RayTraceTest2;

import java.util.Vector;

import net.mightyelemental.maven.RayTraceTest2.materials.Material;
import net.mightyelemental.maven.RayTraceTest2.objects.Renderable;

public class PerspectiveCamera extends Camera {

	public float fov = 90;

	@Deprecated
	public Material rayStartingMaterial = Material.AIR;

	public PerspectiveCamera(Vec3f pos, float fov, int width, int height) {
		this.cameraPos = pos;
		this.fov = fov;
		this.width = width;
		this.height = height;
		cameraAngle = new Vec3f( 0, -30, 0 );
		calcRotMat();
	}

	/** Creates a ray based on the pixel coordinates */
	public Ray createRay(int x, int y) {

		if (x == 0 && y == 0) { calcRotMat(); }

		float invWidth = 1f / width, invHeight = 1f / height;
		float aspRat = width / ((float) height);
		float angle = (float) Math.tan( Math.PI * 0.5 * fov / 180.0 );

		float xx = (2 * ((x + 0.5f) * invWidth) - 1) * angle * aspRat;
		float yy = (1 - 2 * ((y + 0.5f) * invHeight)) * angle;

		Vec3f dirVec = new Vec3f( xx, yy, -1 ).normalize();
		dirVec = rotMat.multiply( dirVec );
		// System.out.println(dirVec);

		return new Ray( dirVec, cameraPos );
	}

	@Deprecated
	public void calculateStartingMaterial(Vector<Renderable> objs) {
		for (Renderable rend : objs) {
			if (rend.isPointWithin( cameraPos )) {
				rayStartingMaterial = rend.getMaterial();
				return;
			}
		}
	}

}
