/*
 * Copyright (c) 2008 University of Durham, England
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


package synergyspace.jmeapps.effects.shadows;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.light.DirectionalLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.pass.ShadowedRenderPass;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Teapot;
import com.jme.scene.state.CullState;
import com.jme.scene.state.CullState.Face;

import synergyspace.jme.abstractapps.AbstractMultiTouchThreeDApp;
import synergyspace.jme.config.AppConfig;
import synergyspace.jme.cursorsystem.elements.threed.MultiTouchMoveableXYPlane;

public class ShadowApp extends AbstractMultiTouchThreeDApp {

	protected ShadowedRenderPass shadowRenderPass;

	@Override
	protected void setupContent() {
		cam.setLocation(new Vector3f(0f, 10f, -20f));
		cam.lookAt(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 1f, 0f));
		cam.update();

		if(passManager.get(0) instanceof ShadowedRenderPass) {
			shadowRenderPass = (ShadowedRenderPass) passManager.get(0);		
//			shadowRenderPass.setLightingMethod(ShadowedRenderPass.ADDITIVE);
			shadowRenderPass.add(rootNode);
		}

		Spatial floorVisual = new Box( "floor", new Vector3f(), 1000, 0.1f, 1000 );
		floorVisual.setModelBound( new BoundingBox() );
		floorVisual.updateModelBound();
		floorVisual.setLocalTranslation( new Vector3f( 0, 0f, 0 ) );
		rootNode.attachChild(floorVisual);

		Teapot tp = new Teapot("_tp");
		tp.setLocalTranslation(new Vector3f(0f, 5f, 0f));
		tp.setModelBound(new BoundingSphere());
		tp.updateModelBound();
		shadowRenderPass.addOccluder(tp);
		rootNode.attachChild(tp);
		new MultiTouchMoveableXYPlane(tp);
		
		rootNode.updateGeometricState(0f, true);
	}

	@Override
	protected void setupLighting() {
		lightState.detachAll();
		
		DirectionalLight dr2 = new DirectionalLight();
		dr2.setEnabled(true);
		dr2.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		dr2.setAmbient(new ColorRGBA(.2f, .2f, .2f, .4f));
		dr2.setDirection(new Vector3f(-0.2f, -0.3f, -.2f).normalizeLocal());
		dr2.setShadowCaster(true);
		lightState.attach(dr2);
		lightState.setGlobalAmbient(new ColorRGBA(0.6f, 0.6f, 0.6f, 1.0f));
		rootNode.setRenderState(lightState);
		
		CullState cs = display.getRenderer().createCullState();
		cs.setCullFace(Face.Front);
		cs.setEnabled(true);
		rootNode.setRenderState(cs);

		rootNode.updateRenderState();
	}

	public static void main(String[] args) {
		AppConfig.cameraType = AppConfig.CAMERA_TYPE_PERSPECTIVE;
		AppConfig.stencilBits = AppConfig.STENCIL_BITS_SHADOWSUPPORT;
		AppConfig.appTitle = "Shadows";
		AppConfig.inputStyle = AppConfig.INPUT_STYLE_KEYBOARDLOOK;
		AppConfig.setRenderPasses(AppConfig.RENDERPASS_SHADOW, AppConfig.RENDERPASS_STANDARD);
		AppConfig.showFPS = true;
		AppConfig.useLighting = true;

		ShadowApp app = new ShadowApp();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);	
		app.start();
	}

	@Override
	protected void setupSystem() {


	}


}
