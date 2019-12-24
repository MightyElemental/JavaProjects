package net.mightyelemental.maven.RayTraceTest2;

import net.mightyelemental.maven.RayTraceTest2.objects.Renderable;

public abstract class Camera {

	public Vec3f      cameraPos, cameraAngle;
	public Renderable camObj;

	public Mat4f rotMat;

	public float width, height;

	protected void calcRotMat() {
		Mat4f rotX = Mat4f.getXRotationDeg( cameraAngle.x );
		Mat4f rotY = Mat4f.getYRotationDeg( cameraAngle.y );
		Mat4f rotZ = Mat4f.getZRotationDeg( cameraAngle.z );
		rotMat = rotX.multiply( rotZ ).multiply( rotY );
	}

	public abstract Ray createRay(int x, int y);

	public Renderable getCamObj() { return camObj; }

	public void setCamObj(Renderable camObj) {
		this.camObj = camObj;
		if (camObj != null) { camObj.setPos( cameraPos ); }
	}

	public void setResolution(int width, int height) { this.width = width; this.height = height; }

	public void moveTo(float x, float y, float z) {
		cameraPos.setX( x );
		cameraPos.setY( y );
		cameraPos.setZ( z );
		if (camObj != null) { camObj.setPos( cameraPos ); }
	}

}
