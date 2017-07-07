/*    
 	This file is part of jME Planet Demo.

    jME Planet Demo is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation.

    jME Planet Demo is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jME Planet Demo.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.ankh.unfall.planet;

import java.nio.ByteBuffer;

import org.ankh.unfall.planet.texgen.PlanetGenerator;


import com.jme.animation.SpatialTransformer;
import com.jme.bounding.BoundingSphere;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.GLSLShaderObjectsState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.util.TextureManager;

/**
 * Telluric planet object
 * @author Yacine Petitprez (anykeyh)
 */
public class Planet extends Node {
	private static final long serialVersionUID = 1386728938383555208L;

	private Geometry planetGeom;

	private RenderPass atmoBackRenderPass, atmoFrontRenderPass, planetRenderPass;

	private CullState    backFaceCullingState;
	private CullState    frontFaceCullingState;
	private BlendState   alphaBlendingState;
	private ZBufferState zbufferEnabledState;

	private GLSLShaderObjectsState planetShader;
	private GLSLShaderObjectsState atmoShader;
	
	private float counter;
	
	private PlanetInformations informations;
	private PlanetGenerator generator;
	
	private int textureHeight, textureWidth;
	
	//XXX: COMING SOON
	//private List<Planet> moons;
	//private Sun sun;
	
	private Image getBaseMapData() {
		ByteBuffer baseMapData = ByteBuffer.allocateDirect(textureWidth*textureHeight*4);
		int[] colorMap = generator.getColorMap();

		for(int c: colorMap) {
			//ARGB -> RGBA
			//= (ARGB & 0x00FFFFFF) << 8 | 0xFF;
			int newc = ((c & 0x00FFFFFF) << 8) | 0xFF;
			baseMapData.putInt(newc);
		}

		Image baseMap = new Image(Image.Format.RGBA8, textureWidth, textureHeight, baseMapData);

		return baseMap;
	}

	private Image getSpecularMapData() {
		ByteBuffer baseMapData = ByteBuffer.allocateDirect(textureWidth*textureHeight*4);
		int[] colorMap = generator.getSpecularMap();

		for(int c: colorMap) {
			int newc = ((c & 0x00FFFFFF) << 8) | 0xFF;
			baseMapData.putInt(newc);
		}

		Image baseMap = new Image(Image.Format.RGBA8, textureWidth, textureHeight, baseMapData);

		return baseMap;
	}

	private Image getNormalMapData() {
		ByteBuffer baseMapData = ByteBuffer.allocateDirect(textureWidth*textureHeight*4);
		int[] colorMap = generator.getNormalMap();

		for(int c: colorMap) {
			int newc = ((c & 0x00FFFFFF) << 8) | 0xFF;
			baseMapData.putInt(newc);
		}

		Image baseMap = new Image(Image.Format.RGBA8, textureWidth, textureHeight, baseMapData);

		return baseMap;
	}
	
	/**
	 * Update the static (non calculated each frame) uniforms
	 */
	private void updateStaticUniforms() {

		/* PLANET OBJECT ---------------------------------------------------- */
		//XXX Light uniforms will be configured when I'll make the sun object
		planetShader.setUniform("fvSpecular", ColorRGBA.white);
		planetShader.setUniform("fvDiffuse", ColorRGBA.white);
		
		planetShader.setUniform("fSpecularPower", 30.0f);
		
		planetShader.setUniform("fCloudHeight", informations.getCloudHeight());

		planetShader.setUniform("baseMap", 	0);
		planetShader.setUniform("normalMap", 	1);
		planetShader.setUniform("specMap", 	2);
		if(informations.hasCloud()) {
			planetShader.setUniform("cloudsMap", 3);
		} else {
			//pointer to noTextures
			planetShader.setUniform("cloudsMap", 4);
		}
		
		/* ATMO OBJECT ---------------------------------------------------- */

		atmoShader.setUniform("fCloudHeight", informations.getCloudHeight());			
		
		atmoShader.setUniform("fAbsPower", informations.getAtmosphereAbsorptionPower() );
		
		atmoShader.setUniform("fAtmoDensity", informations.getAtmosphereDensity());
		atmoShader.setUniform("fGlowPower", informations.getAtmosphereGlowPower() );

		atmoShader.setUniform("fvAtmoColor", informations.getAtmosphereColor() );
		//The light which comes on atmosphere
		atmoShader.setUniform("fvDiffuse", ColorRGBA.white);
	}
	
		
	
	@Override
	public void updateRenderState() {
		super.updateRenderState();

		//Updating static uniforms:
		updateStaticUniforms();
	}

	@Override
	public void draw(Renderer r) {

		//XXX Light uniforms will be configured when I'll make the sun object
//		Vector3f lightPosition = new Vector3f(0,  0,  25000);
		Vector3f lightPosition = new Vector3f(1000, 1000, 1000);
		
		//On transforme la position de la lumiere vers le repere de la camera
		Vector3f lightPositionInModelView = new Vector3f();
		
		lightPositionInModelView.set(
				-lightPosition.dot(r.getCamera().getLeft()), 
				lightPosition.dot(r.getCamera().getUp()),
				-lightPosition.dot(r.getCamera().getDirection())
		);
		
		planetShader.setUniform("fvLightPosition", lightPositionInModelView);

		planetShader.setUniform("fCloudRotation", counter * 0.0001f );

		atmoShader.setUniform("fvLightPosition", lightPositionInModelView);

		planetRenderPass.renderPass(r);
		atmoFrontRenderPass.renderPass(r);
		atmoBackRenderPass.renderPass(r);
	}

	/**
	 * Construit l'objet 3D d'une planete
	 * @param informations Les informations sur la planete.
	 * @param generator Le générateur de texture utilisé. Si les texture ne sont pas encore généré, le constructeur s'occupera de le faire. 
	 * @param renderer Couche de rendue employée
	 */
	public Planet(PlanetInformations informations, PlanetGenerator generator, Renderer renderer) {

		this.informations = informations;
		this.generator = generator;
		
		textureHeight = generator.getHeight();
		textureWidth = generator.getWidth();

		/* R�cup�ration des textures */
		Texture2D baseMap = new Texture2D();
		baseMap.setImage(getBaseMapData());
		baseMap.setMinificationFilter(Texture.MinificationFilter.Trilinear);
		baseMap.setMagnificationFilter(Texture.MagnificationFilter.Bilinear);

		Texture2D specularMap = new Texture2D();
		specularMap.setImage(getSpecularMapData());
		specularMap.setMinificationFilter(Texture.MinificationFilter.Trilinear);
		specularMap.setMagnificationFilter(Texture.MagnificationFilter.Bilinear);

		Texture normMap = new Texture2D();
		normMap.setImage(getNormalMapData());
		normMap.setMinificationFilter(Texture.MinificationFilter.Trilinear);
		normMap.setMagnificationFilter(Texture.MagnificationFilter.Bilinear);

		Texture cloudMap = TextureManager.loadTexture(getClass().getResource("/org/ankh/unfall/media/textures/clouds.dds"), false);
		cloudMap.setMinificationFilter(Texture.MinificationFilter.Trilinear);
		cloudMap.setMagnificationFilter(Texture.MagnificationFilter.Bilinear);
		cloudMap.setWrap(Texture.WrapMode.Repeat);
		/* Mise en place des textures */
		TextureState textureState = renderer.createTextureState();
		textureState.setTexture(baseMap, 0);
		textureState.setTexture(normMap, 1);
		textureState.setTexture(specularMap, 2);
		textureState.setTexture(cloudMap, 3);

		textureState.setEnabled(true);

		this.setRenderState(textureState);

		this.setLightCombineMode(Spatial.LightCombineMode.Off);

		/* Creating each RenderStates of planet and atmosphere */
		zbufferEnabledState = renderer.createZBufferState();
		zbufferEnabledState.setFunction(ZBufferState.TestFunction.LessThan);
		zbufferEnabledState.setEnabled(true);


		backFaceCullingState = renderer.createCullState();
		backFaceCullingState.setCullFace(CullState.Face.Back);
		backFaceCullingState.setEnabled(true);

		frontFaceCullingState = renderer.createCullState();
		frontFaceCullingState.setCullFace(CullState.Face.Front);
		frontFaceCullingState.setEnabled(true);

		alphaBlendingState = renderer.createBlendState();
		
		alphaBlendingState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		alphaBlendingState.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		alphaBlendingState.setBlendEnabled(true);
		alphaBlendingState.setEnabled(true);

		planetShader = renderer.createGLSLShaderObjectsState();
		planetShader.load(getClass().getResource("/org/ankh/unfall/media/shaders/planet.vert.glsl"), 
				getClass().getResource("/org/ankh/unfall/media/shaders/planet.frag.glsl"));
		planetShader.setEnabled(true);

		atmoShader = renderer.createGLSLShaderObjectsState();
		atmoShader.load(getClass().getResource("/org/ankh/unfall/media/shaders/atmosphere.vert.glsl"), 
				getClass().getResource("/org/ankh/unfall/media/shaders/atmosphere.frag.glsl"));
		atmoShader.setEnabled(true);
		
		//planetGeom = new GeoSphere("Test", false, 5);
		//planetGeom.setLocalScale(Constants.kilometersToUnity(informations.getRadius()));
		planetGeom = new Sphere("Planete Test", 64, 64, informations.getRadius());
		planetGeom.updateGeometricState(-1, true);
		
		
		
		
		/* Creating each render pass: planet, atmosphere back face & atmosphere front face */
		planetRenderPass = new RenderPass();
		planetRenderPass.add(planetGeom);
		planetRenderPass.setPassState(zbufferEnabledState);
		planetRenderPass.setPassState(textureState);
		planetRenderPass.setPassState(backFaceCullingState);
		planetRenderPass.setPassState(planetShader);
		
		atmoFrontRenderPass = new RenderPass();
		atmoFrontRenderPass.add(planetGeom);
		atmoFrontRenderPass.setPassState(zbufferEnabledState);
		atmoFrontRenderPass.setPassState(frontFaceCullingState);
		atmoFrontRenderPass.setPassState(alphaBlendingState);
		atmoFrontRenderPass.setPassState(atmoShader);
		
		atmoBackRenderPass = new RenderPass();
		atmoBackRenderPass.add(planetGeom);
		atmoBackRenderPass.setPassState(zbufferEnabledState);
		atmoBackRenderPass.setPassState(backFaceCullingState);
		atmoBackRenderPass.setPassState(alphaBlendingState);
		atmoBackRenderPass.setPassState(atmoShader);

		
		this.attachChild(planetGeom);

		/* Rotate the planet to have the poles on Y axis */
		getLocalRotation().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);

		/* Day/night rotation */
		SpatialTransformer trans = new SpatialTransformer(1);
		trans.setObject(planetGeom, 0, 0);
		trans.setRepeatType(SpatialTransformer.RT_WRAP);
		
		trans.setSpeed(1.f/informations.getDaytime());
		trans.setRotation(0, 0, new Quaternion().fromAngleAxis(0, Vector3f.UNIT_Z) );
		trans.setRotation(0, 0.5f, new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Z));
		trans.setRotation(0, 1.0f, new Quaternion().fromAngleAxis(FastMath.TWO_PI, Vector3f.UNIT_Z));
		
		trans.setActive(true);
		planetGeom.addController(trans);

		this.setModelBound(new BoundingSphere());

		/* Object state updating */
		this.updateGeometricState(-1, true);
		this.updateRenderState();
		this.updateModelBound();
		this.setIsCollidable(false);
	}

	@Override
	public void updateWorldData(float time) {
		super.updateWorldData(time);
		counter += time; //update counter for shaders
	}




}
