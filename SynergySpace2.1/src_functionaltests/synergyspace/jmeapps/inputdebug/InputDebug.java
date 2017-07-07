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

package synergyspace.jmeapps.inputdebug;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.scene.shape.Quad;

import synergyspace.jme.abstractapps.AbstractMultiTouchTwoDeeApp;
import synergyspace.jme.config.AppConfig;
import synergyspace.jme.cursorsystem.elements.twod.OrthoBringToTop;
import synergyspace.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;
import synergyspace.jme.cursorsystem.flicksystem.FlickSystem;
import synergyspace.jme.gfx.twod.ImageQuadFactory;

public class InputDebug extends AbstractMultiTouchTwoDeeApp {

	@Override
	protected void setupContent() {
		final Quad quad1 = ImageQuadFactory.createQuadWithUncompressedImageResource("aotn", 200, InputDebug.class.getResource("aotn.jpg"), ImageQuadFactory.ALPHA_DISABLE);
		quad1.setModelBound(new OrthogonalBoundingBox());
		quad1.updateModelBound();		
		OrthoControlPointRotateTranslateScale oc1 = new OrthoControlPointRotateTranslateScale(quad1);
		oc1.setScaleLimits(0.5f, 3f);
		oc1.setPickMeOnly(true);
		new OrthoBringToTop(quad1);
		FlickSystem.getInstance().makeFlickable(quad1, oc1, 2f);
		
		final Quad quad2 = ImageQuadFactory.createQuadWithUncompressedImageResource("fa", 200, InputDebug.class.getResource("fa.jpg"), ImageQuadFactory.ALPHA_DISABLE);
		quad2.setModelBound(new OrthogonalBoundingBox());
		quad2.updateModelBound();		
		OrthoControlPointRotateTranslateScale oc2 = new OrthoControlPointRotateTranslateScale(quad2);
		oc2.setScaleLimits(0.5f, 3f);
		oc2.setPickMeOnly(true);
		new OrthoBringToTop(quad1);
		FlickSystem.getInstance().makeFlickable(quad2, oc2, 2f);
		
		final Quad quad3 = ImageQuadFactory.createQuadWithUncompressedImageResource("hf", 200, InputDebug.class.getResource("hf.jpg"), ImageQuadFactory.ALPHA_DISABLE);
		quad3.setModelBound(new OrthogonalBoundingBox());
		quad3.updateModelBound();		
		OrthoControlPointRotateTranslateScale oc3 = new OrthoControlPointRotateTranslateScale(quad3);
		oc3.setScaleLimits(0.5f, 3f);
		oc3.setPickMeOnly(true);
		new OrthoBringToTop(quad3);
		FlickSystem.getInstance().makeFlickable(quad3, oc3, 2f);
		
		orthoRoot.attachChild(quad1);
		orthoRoot.attachChild(quad2);
		orthoRoot.attachChild(quad3);
	}

	@Override
	protected void setupLighting() {}

	@Override
	protected void setupSystem() {}
	
	public static void main(String[] args) {		
		AppConfig.debugToolsFlag = AppConfig.INPUT_DEBUGTOOLS_ON;

		InputDebug app = new InputDebug();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);		
		app.start();
	}

}
