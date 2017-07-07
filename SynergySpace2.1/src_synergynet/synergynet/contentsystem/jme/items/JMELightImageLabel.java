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

package synergynet.contentsystem.jme.items;

import java.net.URL;

import javax.swing.ImageIcon;

import com.jme.image.Texture;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.LightImageLabel;
import synergynet.contentsystem.items.implementation.interfaces.ILightImageLabelImplementation;

public class JMELightImageLabel extends JMEQuadContentItem implements ILightImageLabelImplementation {

	private static final long serialVersionUID = -8018722192828541571L;
	
	LightImageLabel item;
	
	public JMELightImageLabel(ContentItem contentItem) {
		super(contentItem, new Quad(contentItem.getName(), 1,1));
		item = (LightImageLabel) contentItem;
		((Quad)spatial).updateGeometry(item.getWidth(), item.getHeight());
		((Quad)spatial).updateRenderState();
	}

	@Override
	public void init(){
		super.init();
		drawImage(item.getImageResource());
		((Quad)spatial).updateGeometry(item.getWidth(), item.getHeight());
		((Quad)spatial).updateRenderState();
	}

	@Override
	public void setHeight(int height) {
		float width = item.getWidth();
		if(item.isAutoFitSize() && item.getImageResource() != null){
			ImageIcon image = new ImageIcon(item.getImageResource());
			float aspectRatio = (float) image.getIconWidth() / (float) image.getIconHeight();
			width = height * aspectRatio;
		}
		((Quad)spatial).updateGeometry(width, height);
		((Quad)spatial).updateRenderState();
	}

	@Override
	public void setWidth(int width) {
		float height = item.getHeight();
		if(item.isAspectRatioEnabled() && item.getImageResource() != null){
			ImageIcon image = new ImageIcon(item.getImageResource());
			float aspectRatio = (float) image.getIconWidth() / (float) image.getIconHeight();
			height = width / aspectRatio;
		}
		((Quad)spatial).updateGeometry(width, height);
		((Quad)spatial).updateRenderState();
	}

	@Override
	public void drawImage(URL imageResource) {
		if(imageResource == null) return;
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		Texture t = TextureManager.loadTexture(imageResource, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		ts.setTexture(t);

		((Quad)spatial).setRenderState(ts);

		if(item.isAplaEnabled()) {
			BlendState as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
			as.setBlendEnabled(true);
			as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
			as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
			as.setTestEnabled(true);
			as.setTestFunction(BlendState.TestFunction.GreaterThan);
			spatial.setRenderState(as);
		}
		((Quad)spatial).updateGeometry(t.getImage().getWidth(), t.getImage().getHeight());
		((Quad)spatial).updateRenderState();
	}

	@Override
	public URL getImageResource() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setAutoFitSize(boolean isEnabled) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isAplaEnabled() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void useAlpha(boolean useAlpha) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enableAspectRatio(boolean isAspectRationEnabled) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isAspectRatioEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}
