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
import java.util.HashMap;

import javax.swing.ImageIcon;

import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.RoundFrame;
import synergynet.contentsystem.items.implementation.interfaces.IRoundFrameImplementation;
import synergynet.contentsystem.items.utils.Background;
import synergynet.contentsystem.items.utils.Border;
import synergynet.contentsystem.items.utils.ImageInfo;
import synergyspace.jme.gfx.twod.utils.GraphicsImageDisc;

import com.jmex.awt.swingui.ImageGraphics;

public class JMERoundFrame extends JMERoundContentItem implements IRoundFrameImplementation {
	
	protected ImageGraphics gfx;
	protected RoundFrame item;
	protected GraphicsImageDisc graphicsImageDisc;
	
	public JMERoundFrame(ContentItem contentItem) {
		super(contentItem, new GraphicsImageDisc(contentItem.getName(), 100));
		graphicsImageDisc = (GraphicsImageDisc)this.spatial;
		gfx = graphicsImageDisc.getImageGraphics();		
		item = (RoundFrame)contentItem;
		graphicsImageDisc.setLocalTranslation(0, 0, 0);
	}
	
	@Override
	public void init(){
		super.init();
		updateSize();
	}
	
	@Override
	public void setBackGround(Background backGround) {
		render();		
	}

	@Override
	public void setBorder(Border border) {
		render();		
	}
	
	@Override
	public void setRadius(float radius) {
		updateSize();
	}

	protected void render() {
		
		//draw background
		gfx.setColor(item.getBackgroundColour());
		gfx.fillRect(0, 0, (int)(item.getRadius()*2), (int)(item.getRadius()*2));	
		
		//draw content
		draw();
			
		//draw border
		drawBorder();
		
		//draw images
		drawImages();
		
		this.spatial.updateRenderState();
		this.spatial.updateGeometricState(0f, false);
	}
	
	protected void draw(){
		
	}

	protected void updateSize(){
		
		float r = item.getRadius();	
		this.graphicsImageDisc.updateGeometry(r);
		
		if(r < 1) r=1;
		this.graphicsImageDisc.recreateImageForSize(r);
		gfx = graphicsImageDisc.getImageGraphics();
		
		render();
	}
	
	private void drawBorder(){
			
		int borderSize = item.getBorderSize();	
		gfx.setColor(item.getBorderColour());
		
		for (int i=0; i<borderSize; i++)
			gfx.drawOval(i, i, (int)(item.getRadius()*2)-i*2, (int)(item.getRadius()*2)-i*2);
		
	}

	protected void drawImages(){
		//draw images
		if(item.getImageResources() != null){
			for(ImageInfo imgInfo: item.getImageResources().values()){
				if(imgInfo.getImageResource() != null){
					Image image = new ImageIcon(imgInfo.getImageResource()).getImage();
					gfx.drawImage(image,imgInfo.getX() ,imgInfo.getY(), imgInfo.getWidth(), imgInfo.getHeight(), null);
				}
			}
		}

	}

	@Override
	public void drawImage(URL imageResource) {
		render();
	}

	@Override
	public void drawImage(URL imageResource, int x, int y, int width, int height) {
		render();
	}

	@Override
	public HashMap<URL, ImageInfo> getImages() {
		return item.getImages();		
	}

	@Override
	public void removeAllImages() {
		render();
	}

	@Override
	public void removeImage(URL imageResource) {
		render();
	}
}
