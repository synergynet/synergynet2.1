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

package synergyspace.jmeapps;

import com.jme.bounding.BoundingSphere;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Teapot;

import synergyspace.jme.abstractapps.AbstractMultiTouchThreeDApp;
import synergyspace.jme.cursorsystem.elements.threed.MultiTouchScale;

public class ScaleDemo extends AbstractMultiTouchThreeDApp {

	@Override
	protected void setupSystem() {}
	
	@Override
	protected void setupContent() {
		final Teapot tp1 = new Teapot("tp1");
		tp1.setLocalTranslation(new Vector3f(0f, 0f, -40f));
		tp1.setModelBound(new BoundingSphere());
		tp1.updateModelBound();
		rootNode.attachChild(tp1);
		
		new MultiTouchScale(tp1, 0.2f);
	}
	
	public static void main(String[] args) {	
		ScaleDemo demo = new ScaleDemo();
		demo.setConfigShowMode(ConfigShowMode.AlwaysShow);
		demo.start();
	}

	@Override
	protected void setupLighting() {
		this.lightState.detachAll();
		
		PointLight pointlight = new PointLight();
		pointlight.setLocation(new Vector3f(50f, 20f, 10f));
		pointlight.setAmbient(ColorRGBA.red);
		pointlight.setAttenuate(true);
		pointlight.setEnabled(true);
		this.lightState.attach(pointlight);

		pointlight = new PointLight();
		pointlight.setLocation(new Vector3f(-100f, 20f, -10f));
		pointlight.setAmbient(ColorRGBA.blue);
		pointlight.setAttenuate(true);
		pointlight.setEnabled(true);
		this.lightState.attach(pointlight);		
	}

}
