package net.mightyelemental.maven.RayTraceTest2;

public class OrthoCamera extends Camera {

	public float planeWidth, planeHeight;

	public OrthoCamera(Vec3f pos, float rw, float rh, float width, float height) {
		this.cameraPos = pos;
		this.width = rw;
		this.height = rh;
		this.planeWidth = width;
		this.planeHeight = height;
		cameraAngle = new Vec3f( 0, -30, 0 );
		this.calcRotMat();
	}

	@Override
	public Ray createRay(int x, int y) {
		if (x == 0 && y == 0) { calcRotMat(); }

		float widRatio = planeWidth / width;
		float heiRatio = planeHeight / height;

		// Vec3f dirVec = new Vec3f( 0, 0, -1 );

		Vec3f dirVec = rotMat.multiply( new Vec3f( 0, 0, -1 ) );
		Vec3f subStrtVec = new Vec3f( -(planeWidth / 2) + widRatio * x,
				(planeHeight / 2) - heiRatio * y, 0 );
		subStrtVec = rotMat.multiply( subStrtVec );
		Vec3f strtVec = cameraPos.sum( subStrtVec );

		// System.out.println(dirVec);

		return new Ray( dirVec, strtVec );
	}

}
