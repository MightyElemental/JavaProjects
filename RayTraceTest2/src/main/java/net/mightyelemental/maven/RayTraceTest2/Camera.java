package net.mightyelemental.maven.RayTraceTest2;

import net.mightyelemental.maven.RayTraceTest2.objects.Renderable;

public class Camera {

	public Vector3f	cameraPos, cameraAngle;
	public float	fov	= 90;
	Mat4f			rotMat;

	public float width, height;
	
	public Renderable camObj;

	public Camera(Vector3f pos, float fov, int width, int height) {
		this.cameraPos = pos;
		this.fov = fov;
		this.width = width;
		this.height = height;
		cameraAngle = new Vector3f(0, -30, 0);
		Mat4f rotX = Mat4f.getXRotationDeg(cameraAngle.x);
		Mat4f rotY = Mat4f.getYRotationDeg(cameraAngle.y);
		Mat4f rotZ = Mat4f.getZRotationDeg(cameraAngle.z);
		rotMat = rotX.multiply(rotZ).multiply(rotY);
	}

	/** Creates a ray based on the pixel coordinates */
	public Ray createRay(int x, int y) {

		if ( x == 0 && y == 0 ) {
			Mat4f rotX = Mat4f.getXRotationDeg(cameraAngle.x);
			Mat4f rotY = Mat4f.getYRotationDeg(cameraAngle.y);
			Mat4f rotZ = Mat4f.getZRotationDeg(cameraAngle.z);
			rotMat = rotX.multiply(rotZ).multiply(rotY);
			if(camObj != null) {
				camObj.setPos(cameraPos);
			}
		}

		float invWidth = 1f / width, invHeight = 1f / height;
		float aspRat = width / ((float) height);
		float angle = (float) Math.tan(Math.PI * 0.5 * fov / 180.0);

		float xx = (2 * ((x + 0.5f) * invWidth) - 1) * angle * aspRat;
		float yy = (1 - 2 * ((y + 0.5f) * invHeight)) * angle;

		Vector3f dirVec = new Vector3f(xx, yy, -1).normalize();
		dirVec = rotMat.multiply(dirVec);
		// System.out.println(dirVec);

		return new Ray(dirVec, cameraPos);
	}

	public void moveTo(float x, float y, float z) {
		cameraPos.setX(x);
		cameraPos.setY(y);
		cameraPos.setZ(z);
	}

	
	public Renderable getCamObj() {
		return camObj;
	}

	public void setCamObj(Renderable camObj) {
		this.camObj = camObj;
	}
	
	public void setDim(int width, int height) {
		this.width = width;
		this.height = height;
	}

}
