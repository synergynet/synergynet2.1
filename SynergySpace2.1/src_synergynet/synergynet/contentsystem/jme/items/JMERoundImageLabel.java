/* Copyright (c) 2008 University of Durham, England
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

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.RoundImageLabel;
import synergynet.contentsystem.items.implementation.interfaces.IRoundImageLabelImplementation;

public class JMERoundImageLabel extends JMERoundFrame implements IRoundImageLabelImplementation{
	
	private RoundImageLabel item;
	private int innerImageHeight;
	private int innerImageWidth; 	
	private int labelWidth;
	
	public JMERoundImageLabel(ContentItem contentItem) {
		super(contentItem);
		this.item = (RoundImageLabel)contentItem;	
		
	}
	
	protected void draw() {
		if(item.getImageInfo().getImageResource() != null){
			Image image = new ImageIcon(item.getImageInfo().getImageResource()).getImage();
			gfx.drawImage(image, this.labelWidth/2 - innerImageWidth/2, this.labelWidth/2 - innerImageHeight/2,  innerImageHeight, innerImageHeight, null);
		}
		
	}

	public void resize(){
			
		if (item.isAutoFit()){
			item.getImageInfo().setHeight((int) (item.getRadius()*2));		
		}
		
		this.innerImageHeight = item.getImageInfo().getHeight();
		this.item.getImageInfo().setAutoRatioByImageHeight(true);
		this.innerImageWidth = item.getImageInfo().getWidth();
		this.labelWidth = (int)item.getRadius()*2;
		
		this.updateSize();
	}

	@Override
	public void setImageInfo(URL imageResource) {
		resize();		
	}
	
	@Override
	public void setAutoFit(boolean autoFit) {
		resize();	
	}	
	
	public void setRadius(float radius){
		resize();
	}
	
}
