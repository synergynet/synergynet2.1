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

package synergyspace.jmeapps.ortholightbox;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.scene.shape.Quad;

import synergyspace.jme.abstractapps.AbstractMultiTouchTwoDeeApp;
import synergyspace.jme.cursorsystem.elements.twod.OrthoBringToTop;
import synergyspace.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;
import synergyspace.jme.cursorsystem.flicksystem.FlickSystem;
import synergyspace.jme.gfx.twod.ImageQuadFactory;

public class LightBox2 extends AbstractMultiTouchTwoDeeApp {

	public static void main(String[] args) {
		LightBox2 app = new LightBox2();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	@Override
	protected void setupContent() {
		
		FlickSystem.getInstance().enableScreenBoundary(orthoRoot);
		
		final Quad quad1 = ImageQuadFactory.createQuadWithImageResource("a", 200, LightBox2.class.getResource("a.jpg"));
		quad1.setModelBound(new OrthogonalBoundingBox());
		quad1.updateModelBound();		
		OrthoControlPointRotateTranslateScale ocprts1 = new OrthoControlPointRotateTranslateScale(quad1);
		ocprts1.setPickMeOnly(true);
		//FlickSystem.getInstance().makeFlickable(quad1, ocprts1, 2f);
		new OrthoBringToTop(quad1);
		
		final Quad quad2 = ImageQuadFactory.createQuadWithImageResource("b", 200, LightBox2.class.getResource("b.jpg"));
		quad2.setLocalTranslation(20f, 200f, 0f);
		quad2.setModelBound(new OrthogonalBoundingBox());
		quad2.updateModelBound();
		OrthoControlPointRotateTranslateScale ocprts2 = new OrthoControlPointRotateTranslateScale(quad2, quad2);
		ocprts2.setPickMeOnly(true);
		//FlickSystem.getInstance().makeFlickable(quad2, ocprts2, 2f);
		new OrthoBringToTop(quad2);
		
		//this.addItemForUpdating(FlickSystem.getInstance());
		
		orthoRoot.attachChild(quad1);
		orthoRoot.attachChild(quad2);
		orthoRoot.updateGeometricState(0f, false);
		
	}

	@Override
	protected void setupLighting() {
		
	}

	@Override
	protected void setupSystem() {
	}
	
}
