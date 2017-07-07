package synergynet.table.apps.lightrays.raytracer.shaders;

import synergynet.table.apps.lightrays.raytracer.scene.Colour;
import synergynet.table.apps.lightrays.raytracer.scene.OpticalProperties;
import synergynet.table.apps.lightrays.raytracer.scene.Ray;
import synergynet.table.apps.lightrays.raytracer.scene.Scene;
import synergynet.table.apps.lightrays.raytracer.scene.SceneObject;

public abstract class Shader {

	protected OpticalProperties optical_properties;
	protected Scene scene;
	protected Ray incident_ray;
	protected int depth;
	
	public Shader(Scene scene, Ray incident_ray, SceneObject obj) {
		this.scene = scene;
		this.optical_properties = obj.getOpticProperties();
		this.incident_ray = incident_ray;
		this.depth = incident_ray.getDepth();
	}

	public abstract void getColour(Colour c);

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Ray getIncident_ray() {
		return incident_ray;
	}

	public void setIncident_ray(Ray incident_ray) {
		this.incident_ray = incident_ray;
	}

	public OpticalProperties getOptical_properties() {
		return optical_properties;
	}

	public void setOptical_properties(OpticalProperties optical_properties) {
		this.optical_properties = optical_properties;
	}	
}
