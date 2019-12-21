package net.mightyelemental.maven.RayTraceTest2.objects;

import java.util.Optional;
import java.util.Vector;

public class Scene {

	public Vector<Renderable> objectList = new Vector<Renderable>();
	public Vector<Light>      lightList  = new Vector<Light>();

	public String skyboxLocation = "./imgs/yokohama3/";

	public float gravity = 9.8f;

	// public Vector3f backgroundColor = new Vector3f(119 / 255f, 181 / 255f, 254 / 255f);

	public Scene() {

	}

	public void add(Renderable rend) {
		if (rend instanceof Polyhedron) {
			objectList.addAll( ((Polyhedron) rend).objs );
		} else {
			objectList.add( rend );
		}
	}

	public void setSkyBox(String skyboxLocation) {
		this.skyboxLocation = skyboxLocation;
	}

	public void add(Light light) { lightList.add( light ); }

	public String getSkybox() { return skyboxLocation; }

	public void add(Optional<Polyhedron> rend) {
		if (rend.isPresent()) add( rend.get() );
	}

}
