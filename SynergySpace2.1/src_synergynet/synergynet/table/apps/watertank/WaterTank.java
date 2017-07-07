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

package synergynet.table.apps.watertank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

import javax.swing.ImageIcon;

import com.jme.scene.Spatial;
import com.jmex.awt.swingui.ImageGraphics;

import synergynet.contentsystem.ContentSystem;
import synergynet.table.gfx.FullScreenCanvas;
import synergyspace.jme.gfx.twod.DrawableSpatialImage;

public class WaterTank extends FullScreenCanvas implements DrawableSpatialImage {

	private static final long serialVersionUID = -7014741028303257761L;
	private ImageGraphics gfx;

	protected int width,height,hwidth,hheight;
	protected MemoryImageSource source;
	protected Image image, offImage;
	protected Graphics offGraphics;
	protected int i,a,b;
	protected int MouseX,MouseY;
	protected int delay,size;
	protected float tolerance = 10;
	protected short ripplemap[];
	protected int texture[];
	protected int ripple[];
	protected int oldind,newind,mapind;
	protected int riprad;
	protected Image im;



	public WaterTank(String name, float width, float height, int imageWidth, int imageHeight, ContentSystem contentSystem) {
		super(name, contentSystem);
		gfx = getGraphics();
		init();
		draw();

	}

	@Override
	public void cursorDragged(long id, int x, int y) {
		disturb(x, y, 2);
	}

	@Override
	public void cursorPressed(long cursorID, int x, int y) {
		disturb(x, y, 2);
	}

	@Override
	public void cursorReleased(long cursorID, int x, int y) {
	 	disturb(x, y, 2);
	}
	
	@Override
	public void cursorClicked(long cursorID, int x, int y) {}

	public void draw() {
		calculateFPS();
		newframe();
		source.newPixels();
		offGraphics.drawImage(image,0,0,width,height,null);
		offGraphics.drawString("FPS: " + fps, 20, 20);
		gfx.drawImage(offImage,0,0,null);
	}

	public Spatial getSpatial() {
		return this;
	}

	int frameCount = 0;
	long lastTime = 0;
	int fps = 0;

	private void calculateFPS() {
		long currentTime = System.currentTimeMillis();
		if(currentTime - lastTime > 1000) {
			fps = frameCount;
			frameCount = 0;
			lastTime = currentTime;
		}
		frameCount++;
	}

	public void init() {
		//Retrieve the base image
		ImageIcon ii = new ImageIcon(WaterTank.class.getResource("pebbles.jpg"));
		im = ii.getImage();


		//How many milliseconds between frames?
		fps = 60;
//		delay = (fps > 0) ? (1000 / fps) : 100;
		delay = 0;

		width = Toolkit.getDefaultToolkit().getScreenSize().width;
		height = Toolkit.getDefaultToolkit().getScreenSize().height;
		hwidth = width>>1;
		hheight = height>>1;

		size = width * (height+2) * 2;
		ripplemap = new short[size];
		ripple = new int[width*height];
		texture = new int[width*height];
		oldind = width;
		newind = width * (height+3);

		PixelGrabber pg = new PixelGrabber(im,0,0,width,height,texture,0,width);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {}


		source = new MemoryImageSource(width, height, ripple, 0, width);
		source.setAnimated(true);
		source.setFullBufferUpdates(true);

		image = Toolkit.getDefaultToolkit().createImage(source);
		offImage = new BufferedImage(width, height, 1);
		offGraphics = offImage.getGraphics();
		offGraphics.setColor(Color.red); // for fps drawing
	}



	public void disturb(int dx, int dy, int radius) {
		for (int j=dy-radius;j<dy+radius;j++) {
			for (int k=dx-radius;k<dx+radius;k++) {
				if (j>=0 && j<height && k>=0 && k<width) {
					ripplemap[oldind+(j*width)+k] += 512;
				}
			}
		}
	}


	public void newframe() {

		//Toggle maps each frame
		i=oldind;
		oldind=newind;
		newind=i;

		i=0;
		mapind=oldind;
		for (int y=0;y<height;y++) {
			for (int x=0;x<width;x++) {
				short data = (short)((ripplemap[mapind-width]+ripplemap[mapind+width]+ripplemap[mapind-1]+ripplemap[mapind+1])>>1);
				data -= ripplemap[newind+i];
				data -= data >> 5;
				ripplemap[newind+i]=data;

				//where data=0 then still, where data>0 then wave
				data = (short)(1024-data);

				//offsets
				a=((x-hwidth)*data/1024)+hwidth;
				b=((y-hheight)*data/1024)+hheight;

				//bounds check
				if (a>=width) a=width-1;
				if (a<0) a=0;
				if (b>=height) b=height-1;
				if (b<0) b=0;

				ripple[i]=texture[a+(b*width)];
				mapind++;
				i++;
			}
		}
	}




}
