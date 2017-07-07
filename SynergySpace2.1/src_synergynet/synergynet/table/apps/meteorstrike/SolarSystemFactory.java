/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergySpace' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynet.table.apps.meteorstrike;

import org.ankh.unfall.planet.Planet;
import org.ankh.unfall.planet.PlanetInformations;
import org.ankh.unfall.planet.texgen.ContinentalGenerator;
import org.ankh.unfall.planet.texgen.PlanetGenerator;
import org.ankh.unfall.planet.texgen.palette.EarthPalette;
import org.ankh.unfall.planet.texgen.palette.TerrainPalette;

import synergyspace.jme.cursorsystem.elements.threed.MultiTouchScale;
import synergyspace.jme.cursorsystem.elements.threed.MultiTouchScale.ScaleChangeListener;

import com.jme.animation.SpatialTransformer;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.image.Texture.WrapMode;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.GLSLShaderObjectsState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.geometry.PhysicsSphere;

public class SolarSystemFactory {

	public static Skybox createSpace() {
		Skybox skybox = new Skybox("skybox", 10, 10, 10);
		Class<?> c = SolarSystemFactory.class;
		Texture north = TextureManager.loadTexture(c
				.getResource("stars_fr.jpg"),
				Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear,
				Image.Format.GuessNoCompression, 1.0f, true);

		Texture south = TextureManager.loadTexture(c
				.getResource("stars_bk.jpg"),
				Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear,
				Image.Format.GuessNoCompression, 1.0f, true);
		
		Texture east = TextureManager.loadTexture(c
				.getResource("stars_rt.jpg"),
				Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear,
				Image.Format.GuessNoCompression, 1.0f, true);
		
		Texture west = TextureManager.loadTexture(c
				.getResource("stars_lf.jpg"),
				Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear,
				Image.Format.GuessNoCompression, 1.0f, true);

		Texture up = TextureManager.loadTexture(c
				.getResource("stars_up.jpg"),
				Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear,
				Image.Format.GuessNoCompression, 1.0f, true);

		Texture down = TextureManager.loadTexture(c
				.getResource("stars_dn.jpg"),
				Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear,
				Image.Format.GuessNoCompression, 1.0f, true);

		skybox.setTexture(Skybox.Face.North, north);
		skybox.setTexture(Skybox.Face.South, south);
		skybox.setTexture(Skybox.Face.East, east);
		skybox.setTexture(Skybox.Face.West, west);
		skybox.setTexture(Skybox.Face.Up, up);
		skybox.setTexture(Skybox.Face.Down, down);
		skybox.setLightCombineMode( Spatial.LightCombineMode.Off);
		skybox.preloadTextures();

		skybox.updateRenderState();

		return skybox;
	}
	
	public static Spatial createEarth() {
		if(GLSLShaderObjectsState.isSupported()) {
			return createNiceEarth();
		}else{
			return createSimpleEarth();
		}
	}

	private static Planet createNiceEarth() {
		PlanetInformations infos = new PlanetInformations();
		infos.setDaytime(360);
		infos.setEquatorTemperature(45);
		infos.setPoleTemperature(-20);
		infos.setRadius(10f);
		infos.setWaterInPercent(0.7f);
		infos.setHeightFactor(0.2f);
		infos.setSeed((int)System.currentTimeMillis());
		infos.setHumidity(1.0f);
		TerrainPalette palette = new EarthPalette(infos);
		infos.setHasCloud(true);
//		infos.setHasCloud(false);
		infos.setAtmosphereDensity(1.0f);
//		PlanetGenerator generator = new ContinentalGenerator(1024, 1024, infos, palette );
		PlanetGenerator generator = new ContinentalGenerator(256, 256, infos, palette );
		Planet p = new Planet(infos, generator, DisplaySystem.getDisplaySystem().getRenderer());	
		p.updateGeometricState(-1, true);
		return p;
	}

	public static DynamicPhysicsNode createAndAttachNewMeteor(int id, Vector3f pos, PhysicsSpace physicsSpace, Node rootNode) {
		final DynamicPhysicsNode dynamicNode = physicsSpace.createDynamicNode();		
		rootNode.attachChild( dynamicNode );
		String name = "meteor" + id;
//		Sphere meteorGeometry = new Sphere("Meteor", 10, 10, 0.3f);
		Meteor meteorGeometry = new Meteor(name);
		
		MultiTouchScale sc = new MultiTouchScale(meteorGeometry.getSpatial(), 0.2f);
		sc.setPickMeOnly(true);
		sc.addScaleChangeListener(new ScaleChangeListener() {
			public void scaleChanged(float newValue) {
				dynamicNode.setMass(newValue);
			}			
		});

		dynamicNode.attachChild(meteorGeometry);
		PhysicsSphere sphere = dynamicNode.createSphere("");
		sphere.setLocalScale(0.1f);
		dynamicNode.setAffectedByGravity(false);
		dynamicNode.getLocalTranslation().set(pos);		

		return dynamicNode;
	}

	static Spatial createSimpleEarth() {
		return new Earth();
	}

	static Spatial createEvenSimplerEarth() {
		Sphere s = new Sphere("SimpleEarth", 64, 64, 10f);

//		Quaternion q = new Quaternion();		
//		q.fromAngleAxis(FastMath.DEG_TO_RAD * 90, Vector3f.UNIT_X);
//		s.setLocalRotation(q);
		
		SpatialTransformer trans = new SpatialTransformer(1);
		trans.setObject(s, 0, 0);
		trans.setRepeatType(SpatialTransformer.RT_WRAP);
		
		
		
		trans.setSpeed(0.5f/360f);
		trans.setRotation(0, 0, new Quaternion().fromAngleAxis(0, Vector3f.UNIT_X) );
		trans.setRotation(0, 0.5f, new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_X));
		trans.setRotation(0, 1.0f, new Quaternion().fromAngleAxis(FastMath.TWO_PI, Vector3f.UNIT_X));
		
		trans.setActive(true);
		s.addController(trans);
		
		Texture ice = TextureManager.loadTexture(MeteorStrikeApp.class.getResource("earth.jpg"), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, Image.Format.GuessNoCompression, 0, false);
		ice.setWrap(WrapMode.Repeat);
		TextureState iceTextureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		iceTextureState.setTexture(ice, 0);
		iceTextureState.setEnabled(true);
		s.setRenderState(iceTextureState);
		s.updateRenderState();

		return s;
	}

	public static void createLighting(LightState lightState, Node rootNode) {
		lightState.detachAll();
//		PointLight light = new PointLight();
//		light.setDiffuse(new ColorRGBA(0.75f, 0.05f, 0.05f, 0.75f));
//		light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
//		light.setLocation(new Vector3f(100, 100, 100));
//		light.setEnabled(true);
//		lightState.attach(light);	

//		PointLight light2 = new PointLight();
//		light2.setDiffuse(new ColorRGBA(0.05f, 0.75f, 0.75f, 0.75f));
//		light2.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
//		light2.setLocation(new Vector3f(-100, 100, 100));
//		light2.setEnabled(true);		
//		lightState.attach(light2);

		PointLight sun = new PointLight();
		sun.setDiffuse(new ColorRGBA(1.0f, 1.0f, 0.85f, 0.75f));
//		sun.setAmbient(new ColorRGBA(1.0f, 1.0f, 0.85f, 0.75f));
		sun.setLocation(new Vector3f(100, 100, 100));
		sun.setShadowCaster(true);
		sun.setEnabled(true);
		lightState.attach(sun);			

		lightState.setTwoSidedLighting(false);

		rootNode.setRenderState(lightState);
	}

}
