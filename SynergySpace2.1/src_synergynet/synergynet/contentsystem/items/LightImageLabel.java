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

package synergynet.contentsystem.items;

import java.io.Serializable;
import java.net.URL;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.implementation.interfaces.ILightImageLabelImplementation;


public class LightImageLabel extends QuadContentItem implements Serializable, Cloneable, ILightImageLabelImplementation {
	
	protected URL imageResource;
	protected boolean useAlpha = true;
	protected boolean isAspectRationEnabled = false;
	
	private static final long serialVersionUID = -701321502547177425L;

	public LightImageLabel(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	public void drawImage(URL imageResource){
		this.imageResource = imageResource;
		((ILightImageLabelImplementation)this.contentItemImplementation).drawImage(imageResource);

	}
	
	public URL getImageResource(){
		return imageResource;
	}
	
	public void useAlpha(boolean useAlpha){
		this.useAlpha = useAlpha;
		((ILightImageLabelImplementation)this.contentItemImplementation).drawImage(imageResource);

	}
	
	public boolean isAplaEnabled(){
		return useAlpha;
	}

	@Override
	public void enableAspectRatio(boolean isAspectRationEnabled) {
		this.isAspectRationEnabled = isAspectRationEnabled;
	}

	@Override
	public boolean isAspectRatioEnabled() {
		return isAspectRationEnabled;
	}
	
	public void setImageLabelHeight(int height) {
		((ILightImageLabelImplementation)this.contentItemImplementation).setHeight(height);
	}
	
	public Object clone() throws CloneNotSupportedException {
		LightImageLabel clonedItem = (LightImageLabel)super.clone();
		clonedItem.imageResource = imageResource;
		clonedItem.useAlpha = useAlpha;
		clonedItem.isAspectRationEnabled = isAspectRationEnabled;
		return clonedItem;
		
	}

}
