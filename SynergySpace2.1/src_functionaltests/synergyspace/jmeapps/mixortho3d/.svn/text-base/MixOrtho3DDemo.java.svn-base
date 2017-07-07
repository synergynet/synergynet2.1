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

package synergyspace.jmeapps.mixortho3d;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Quad;
import com.jme.scene.shape.Teapot;

import synergyspace.jme.abstractapps.AbstractMultiTouchTwoDeeApp;
import synergyspace.jme.config.AppConfig;
import synergyspace.jme.cursorsystem.elements.threed.ControlPointRotateTranslateScale;
import synergyspace.jme.cursorsystem.elements.twod.OrthoBringToTop;
import synergyspace.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;
import synergyspace.jme.cursorsystem.flicksystem.FlickSystem;
import synergyspace.jme.gfx.twod.ImageQuadFactory;
import synergyspace.jme.gfx.twod.VideoQuad;

public class MixOrtho3DDemo extends AbstractMultiTouchTwoDeeApp {

	public static void main(String[] args) {		
		Logger.getLogger(com.jme.scene.Node.class.getName()).setLevel(Level.OFF);
		AppConfig.debugToolsFlag = AppConfig.INPUT_DEBUGTOOLS_ON;
		MixOrtho3DDemo app = new MixOrtho3DDemo();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	private VideoQuad vidq;

	@Override
	protected void setupContent() {		
		FlickSystem.getInstance().enableScreenBoundary(orthoRoot);
		
		final Quad quad1 = ImageQuadFactory.createQuadWithImageResource("a", 200, MixOrtho3DDemo.class.getResource("a.jpg"));
		quad1.setModelBound(new OrthogonalBoundingBox());
		quad1.updateModelBound();		
		OrthoControlPointRotateTranslateScale oc1 = new OrthoControlPointRotateTranslateScale(quad1);
		oc1.setScaleLimits(0.5f, 3f);
		oc1.setPickMeOnly(true);
		new OrthoBringToTop(quad1);
		FlickSystem.getInstance().makeFlickable(quad1, oc1, 2f);
		
		final Quad quad2 = ImageQuadFactory.createQuadWithImageResource("b", 200, MixOrtho3DDemo.class.getResource("b.jpg"));
		quad2.setLocalTranslation(20f, 200f, 0f);
		quad2.setModelBound(new OrthogonalBoundingBox());
		quad2.updateModelBound();
		OrthoControlPointRotateTranslateScale oc2 = new OrthoControlPointRotateTranslateScale(quad2);
		oc2.setScaleLimits(0.5f, 3f);
		oc2.setPickMeOnly(true);
		new OrthoBringToTop(quad2);
		FlickSystem.getInstance().makeFlickable(quad2, oc2, 2f);
		
		orthoRoot.attachChild(quad1);
		orthoRoot.attachChild(quad2);

		
		vidq = new VideoQuad("video", MixOrtho3DDemo.class.getResource("meta.mp4"), 1f);
		vidq.setModelBound(new OrthogonalBoundingBox());
		vidq.updateModelBound();
		OrthoControlPointRotateTranslateScale oc3 = new OrthoControlPointRotateTranslateScale(vidq);
		new OrthoBringToTop(vidq);
		vidq.setLocalTranslation(150f, 15f, 0f);
		vidq.setLocalScale(0.3f);
		orthoRoot.attachChild(vidq);
		FlickSystem.getInstance().makeFlickable(vidq, oc3, 2f);
		
		orthoRoot.updateGeometricState(0f, false);
		
		final Teapot teapot = new Teapot("teapot");
		teapot.setModelBound(new BoundingBox());
		teapot.updateModelBound();
		ControlPointRotateTranslateScale rts = new ControlPointRotateTranslateScale(teapot);
		rts.setScaleLimits(0.5f, 2f);
		rts.setPickMeOnly(true);
		
		threeDRoot.attachChild(teapot);		
		threeDRoot.updateGeometricState(0f, false);
		threeDRoot.updateRenderState();
		
		this.addItemForUpdating(FlickSystem.getInstance());
	}
	
	@Override
	public void update(float f) {
		super.update(f);
		vidq.update(f);
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

	@Override
	protected void setupSystem() {
	}
}
