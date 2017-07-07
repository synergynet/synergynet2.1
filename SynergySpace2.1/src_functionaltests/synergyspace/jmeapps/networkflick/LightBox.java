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

package synergyspace.jmeapps.networkflick;

import java.io.File;
import java.io.FileFilter;
import java.util.Random;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;

import synergyspace.jme.abstractapps.AbstractMultiTouchTwoDeeApp;
import synergyspace.jme.cursorsystem.elements.twod.OrthoBringToTop;
import synergyspace.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;

import synergyspace.jme.gfx.twod.ImageQuadFactory;
import synergyspace.jme.mmt.flicksystem.FlickSystem;


public class LightBox extends AbstractMultiTouchTwoDeeApp {
	
	public Node node;

	public static void main(String[] args) {
		LightBox app = new LightBox();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	@Override
	protected void setupContent() {
		

		FlickSystem.getInstance().enableScreenBoundary(orthoRoot);
		
		//TODO: ensure all read-only operations use X.class.getResourceAsStream
		
	    FileFilter fileFilter = new FileFilter() {
	        public boolean accept(File file) {
	            return file.getName().endsWith("jpg") || file.getName().endsWith("gif") || file.getName().endsWith("png") || file.getName().endsWith("bmp");
	        }
	    };
		
		File imageFolder = new File("src_functionaltests/synergyspace/jmeapps/networkflick/images");
		File[] imageFiles = imageFolder.listFiles(fileFilter);
		
		Random randomizer = new Random();
		int width = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		int height = DisplaySystem.getDisplaySystem().getRenderer().getHeight();

		for(int i=0; i<imageFiles.length; i++){
				Quad quad = ImageQuadFactory.createQuadWithImageResource(imageFiles[i].getName(), 200, LightBox.class.getResource("images/"+imageFiles[i].getName()));
				quad.setLocalTranslation(randomizer.nextInt(width)- width/2, randomizer.nextInt(height)- height/2, 0);
				quad.setModelBound(new OrthogonalBoundingBox());
				quad.updateModelBound();		
				OrthoControlPointRotateTranslateScale ocprts = new OrthoControlPointRotateTranslateScale(quad);
				ocprts.setPickMeOnly(true);
				FlickSystem.getInstance().makeFlickable(quad, ocprts, 1f);
				new OrthoBringToTop(quad);
				orthoRoot.attachChild(quad);
				Client.spatialToImageMap.put(quad.getName(),"images/"+imageFiles[i].getName());
		}
		this.addItemForUpdating(FlickSystem.getInstance());
		
		orthoRoot.updateGeometricState(0f, false);
		node = orthoRoot;
		
	}
	
	@Override
	protected void setupLighting() {
		
	}

	@Override
	protected void setupSystem() {
	}
	
}
