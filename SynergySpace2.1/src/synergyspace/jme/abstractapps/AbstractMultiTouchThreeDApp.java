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

package synergyspace.jme.abstractapps;

import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.LightState;

import synergyspace.jme.JMEMultiTouchApplication;
import synergyspace.jme.config.AppConfig;

public abstract class AbstractMultiTouchThreeDApp extends JMEMultiTouchApplication {

	protected abstract void setupSystem();
	protected abstract void setupLighting();
	protected abstract void setupContent();	
	
	@Override
	protected void initSystem() {
		AppConfig.cameraType = AppConfig.CAMERA_TYPE_PERSPECTIVE;
		super.initSystem();
		
		setupSystem();
	}
	
	@Override
	protected void initGame() {
		super.initGame();
		
		addDefaultLighting();
		setupLighting();
		setupContent();
		
		timer.reset();
		rootNode.updateRenderState();
		rootNode.updateModelBound();
		rootNode.updateGeometricState(0f, true);
		timer.reset();
	}
	
	protected LightState getAndAttachLightState() {
		if(lightState == null) {
			lightState = display.getRenderer().createLightState();
			rootNode.setRenderState(lightState);
			lightState.setEnabled(AppConfig.useLighting);			
		}
		return lightState;
	}
	
	private void addDefaultLighting() {
		this.lightState.detachAll();
		
		PointLight pointlight = new PointLight();
		pointlight.setLocation(new Vector3f(50f, 20f, 50f));
		pointlight.setAmbient(ColorRGBA.white);
		pointlight.setAttenuate(true);
		pointlight.setEnabled(true);
		this.lightState.attach(pointlight);
		
		pointlight = new PointLight();
		pointlight.setAttenuate(true);
		pointlight.setLocation(new Vector3f(-50f, 20f, 50f));
		pointlight.setAmbient(ColorRGBA.white);
		pointlight.setEnabled(true);
		this.lightState.attach(pointlight);	
	}
}
