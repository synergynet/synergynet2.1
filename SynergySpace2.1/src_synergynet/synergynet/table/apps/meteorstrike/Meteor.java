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

import java.io.IOException;

import com.jme.animation.SpatialTransformer;
import com.jme.bounding.BoundingSphere;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

import com.jme.scene.shape.Sphere;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleMesh;

public class Meteor extends Node {

	private static final long serialVersionUID = 6043384128571224560L;

	private Spatial s;
	
	public Meteor(String name) {
		
		try {
			build(name);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public Spatial getSpatial() {
		return s;
	}

	private void build(String name) throws IOException {
		s = new Sphere(name, 32, 32, 0.3f);
		
//		URL objFile = Meteor.class.getResource("meteor.obj");
//		
//		ObjToJme converter = new ObjToJme();
//		converter.setProperty("mtllib", objFile);
//		ByteArrayOutputStream BO = new ByteArrayOutputStream();		
//		converter.convert(objFile.openStream(), BO);
//		TriMesh s = (TriMesh) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
		
		Texture meteorTexture = TextureManager.loadTexture(MeteorStrikeApp.class.getResource("meteor12.jpg"), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, Image.Format.GuessNoCompression, 0, false);
		meteorTexture.setWrap(Texture.WrapMode.Repeat);
		TextureState meteorTextureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		meteorTextureState.setTexture(meteorTexture, 0);
		meteorTextureState.setEnabled(true);
		

		
		SpatialTransformer trans = new SpatialTransformer(1);
		trans.setObject(s, 0, 0);
		trans.setRepeatType(SpatialTransformer.RT_WRAP);
		
		Vector3f v = new Vector3f(0.1f, 0.15f, 0.1f);
		
		trans.setSpeed(0.05f);
		trans.setRotation(0, 0, new Quaternion().fromAngleAxis(0, v) );
		trans.setRotation(0, 0.5f, new Quaternion().fromAngleAxis(FastMath.PI, v));
		trans.setRotation(0, 1.0f, new Quaternion().fromAngleAxis(FastMath.TWO_PI, v));
		
		trans.setActive(true);
//		s.addController(trans);

		s.setModelBound(new BoundingSphere());		
		s.clearRenderState(RenderState.StateType.Blend);		
		s.setRenderState(meteorTextureState);		
		s.updateModelBound();
		s.updateGeometricState(-1f, true);
		s.updateRenderState();
		
		
		
		attachChild(s);
//		Spatial smoke = getSmokePlume();
//		smoke.setLocalTranslation(new Vector3f(0f, 0f, 0.05f));
//		attachChild(smoke);
		
		this.updateGeometricState(-1f,true);
		this.updateRenderState();
		this.updateModelBound();
		
		
	}

	protected Spatial getSmokePlume() {
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture(
				TextureManager.loadTexture(
						Meteor.class.getClassLoader().getResource(
								"flaresmall.jpg"),
								Texture.MinificationFilter.Trilinear,
								Texture.MagnificationFilter.Bilinear));
		ts.setEnabled(true);
		
	    BlendState as1 = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
	    as1.setBlendEnabled(true);
	    as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
	    as1.setDestinationFunction(BlendState.DestinationFunction.One);
	    as1.setTestEnabled(true);
	    as1.setTestFunction(BlendState.TestFunction.GreaterThan);
	    as1.setEnabled(true);
		
        ParticleMesh particleGeom = ParticleFactory.buildParticles("smoking", 300);        

        particleGeom.warmUp(120);     
        particleGeom.setEmissionDirection(new Vector3f(0.0f, 0.0f, 1.0f));
        particleGeom.setMaximumAngle(0.36651915f);
        particleGeom.setMinimumAngle(0);
        particleGeom.getParticleController().setSpeed(0.2f);
        particleGeom.setMinimumLifeTime(1000.0f);
        particleGeom.setMaximumLifeTime(1500.0f);
        particleGeom.setStartSize(0.2f);
        particleGeom.setEndSize(0.1f);
        particleGeom.setStartColor(new ColorRGBA(0.0f, 0.0f, 0.0f, 1.0f));
        particleGeom.setEndColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 0.0f));
        particleGeom.getParticleController().setControlFlow(false);
        particleGeom.setReleaseRate(300);
        particleGeom.setReleaseVariance(0.0f);
        particleGeom.setInitialVelocity(0.58f);
        particleGeom.setParticleSpinSpeed(0.08f);
        
		particleGeom.setModelBound(new BoundingSphere());
		particleGeom.updateModelBound();

		particleGeom.setRenderState(ts);
		particleGeom.setRenderState(as1);
		
		particleGeom.updateRenderState();
		return particleGeom;
	}
}
