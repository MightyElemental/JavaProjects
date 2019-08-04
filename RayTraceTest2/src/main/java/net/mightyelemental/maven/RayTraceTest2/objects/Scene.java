package net.mightyelemental.maven.RayTraceTest2.objects;

import java.util.Vector;

public class Scene {

	public Vector<Renderable> objectList = new Vector<Renderable>();
	public Vector<Light> lightList = new Vector<Light>();
	
	public String skyboxLocation = "./imgs/skybox/";

	public Scene() {
		
	}

	public void add(Renderable rend) {
		if (rend instanceof ComplexRenderable) {
			objectList.addAll(((ComplexRenderable) rend).objs);
		} else {
			objectList.add(rend);
		}
	}
	
	public void setSkyBox(String skyboxLocation) {
		this.skyboxLocation = skyboxLocation;
	}

	public void add(Light light) {
		lightList.add(light);
	}

	public String getSkybox() {
		return skyboxLocation;
	}

}
