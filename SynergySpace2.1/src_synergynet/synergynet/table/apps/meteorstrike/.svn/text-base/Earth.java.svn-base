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

import com.jme.animation.SpatialTransformer;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.Node;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.GLSLShaderObjectsState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class Earth extends Node {
	private static final long serialVersionUID = 1374699879641777136L;
	private GLSLShaderObjectsState atmoShader;
	private RenderPass atmoFrontRenderPass;
	private RenderPass atmoBackRenderPass;
	Sphere s;

	public Earth() {
		build();
	}

	private void build() {
		s = new Sphere("SimpleEarth", 64, 64, 10f);

		Texture ice = TextureManager.loadTexture(MeteorStrikeApp.class.getResource("earth.jpg"), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, Image.Format.GuessNoCompression, 0, false);
		ice.setWrap(Texture.WrapMode.Repeat);
		TextureState iceTextureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		iceTextureState.setTexture(ice, 0);
		iceTextureState.setEnabled(true);
		s.updateGeometricState(-1, true);
		s.setRenderState(iceTextureState);
		s.updateRenderState();


		SpatialTransformer trans = new SpatialTransformer(1);
		trans.setObject(s, 0, 0);
		trans.setRepeatType(SpatialTransformer.RT_WRAP);

		trans.setSpeed(1.f/360f);
		trans.setRotation(0, 0, new Quaternion().fromAngleAxis(0, Vector3f.UNIT_Z) );
		trans.setRotation(0, 0.5f, new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Z));
		trans.setRotation(0, 1.0f, new Quaternion().fromAngleAxis(FastMath.TWO_PI, Vector3f.UNIT_Z));

		trans.setActive(true);
		s.addController(trans);

		Renderer renderer = DisplaySystem.getDisplaySystem().getRenderer();


		atmoShader = renderer.createGLSLShaderObjectsState();
		atmoShader.load(getClass().getResource("atmosphere.vert.glsl"), getClass().getResource("atmosphere.frag.glsl"));
		atmoShader.setEnabled(true);

		ZBufferState zbufferEnabledState = renderer.createZBufferState();
		zbufferEnabledState.setFunction(ZBufferState.TestFunction.LessThan);
		zbufferEnabledState.setEnabled(true);

		CullState backFaceCullingState = renderer.createCullState();
		backFaceCullingState.setCullFace(CullState.Face.Back);
		backFaceCullingState.setEnabled(true);

		CullState frontFaceCullingState = renderer.createCullState();
		frontFaceCullingState.setCullFace(CullState.Face.Front);
		frontFaceCullingState.setEnabled(true);

		BlendState alphaBlendingState = renderer.createBlendState();
		alphaBlendingState.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		alphaBlendingState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		alphaBlendingState.setBlendEnabled(true);
		alphaBlendingState.setEnabled(true);


		atmoFrontRenderPass = new RenderPass();
		atmoFrontRenderPass.add(s);
		atmoFrontRenderPass.setPassState(zbufferEnabledState);
		atmoFrontRenderPass.setPassState(frontFaceCullingState);
		atmoFrontRenderPass.setPassState(alphaBlendingState);
		atmoFrontRenderPass.setPassState(atmoShader);

		atmoBackRenderPass = new RenderPass();
		atmoBackRenderPass.add(s);
		atmoBackRenderPass.setPassState(zbufferEnabledState);
		atmoBackRenderPass.setPassState(backFaceCullingState);
		atmoBackRenderPass.setPassState(alphaBlendingState);
		atmoBackRenderPass.setPassState(atmoShader);

		this.attachChild(s);

		this.updateGeometricState(-1, true);
		this.updateRenderState();
		this.updateModelBound();
		this.setIsCollidable(false);


	}


	private void updateStaticUniforms() {

		/* ATMO OBJECT ---------------------------------------------------- */

		atmoShader.setUniform("fCloudHeight", 0.004f);			

		atmoShader.setUniform("fAbsPower", 2f );

		atmoShader.setUniform("fAtmoDensity", 1.f);
		atmoShader.setUniform("fGlowPower", 20f );

		atmoShader.setUniform("fvAtmoColor", new ColorRGBA( .7f, .8f, 1f, 1f));
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

		Vector3f lightPosition = new Vector3f(0,  0,  25000);

		Vector3f lightPositionInModelView = new Vector3f();

		lightPositionInModelView.set(
				-lightPosition.dot(r.getCamera().getLeft()), 
				lightPosition.dot(r.getCamera().getUp()),
				-lightPosition.dot(r.getCamera().getDirection())
		);
		atmoShader.setUniform("fvLightPosition", lightPositionInModelView);

		s.draw(r);

		atmoFrontRenderPass.renderPass(r);
		atmoBackRenderPass.renderPass(r);

	}
}
