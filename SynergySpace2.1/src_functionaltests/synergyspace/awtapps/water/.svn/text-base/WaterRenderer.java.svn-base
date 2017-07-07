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

package synergyspace.awtapps.water;

/*
 * Name:     Water
 * Date:     December 2004
 * Author:   Neil Wallis
 * Purpose:  Simulate ripples on water.
 *
 *
 * Modified by d6ammt for SynergySpace
 * 
 * Photo 'Pebbles' by Pingu1963
 * 
 */


import java.awt.*;
import java.awt.image.*;

import javax.swing.ImageIcon;

import synergyspace.awt.AWTAppRenderer;
import synergyspace.mtinput.events.MultiTouchCursorEvent;

public class WaterRenderer extends AWTAppRenderer{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	String str;
	int width,height,hwidth,hheight;
	MemoryImageSource source;
	Image image, offImage;
	Graphics offGraphics;
	int i,a,b;
	int MouseX,MouseY;
	int delay,size;
	float tolerance = 10;

	short ripplemap[];
	int texture[];
	int ripple[];
	int oldind,newind,mapind;
	int riprad;
	Image im;

	Thread animatorThread;

	public WaterRenderer() {
		super();
		init();
	}

	public void init() {
		//Retrieve the base image
		str = "pebbles.jpg";
		if (str != null) {
			ImageIcon ii = new ImageIcon(WaterRenderer.class.getResource("pebbles.jpg"));
			im = ii.getImage();
		}

		//How many milliseconds between frames?
		fps = 40;
		delay = (fps > 0) ? (1000 / fps) : 100;

		width = Toolkit.getDefaultToolkit().getScreenSize().width;
		height = Toolkit.getDefaultToolkit().getScreenSize().height;
		hwidth = width>>1;
		hheight = height>>1;
		riprad=2;

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
	}

	public void disturb(int dx, int dy) {
		System.out.println("disturb() " + dx + "," + dy);
		for (int j=dy-riprad;j<dy+riprad;j++) {
			for (int k=dx-riprad;k<dx+riprad;k++) {
				if (j>=0 && j<height && k>=0 && k<width) {
					ripplemap[oldind+(j*width)+k] += 512;
				}
			}
		}
	}

	public void cursorReleased(MultiTouchCursorEvent event) {
		super.cursorReleased(event);
		disturb((int)(event.getPosition().x * width),
				height-(int)((event.getPosition().y * height)));

	}

	public void cursorPressed(MultiTouchCursorEvent event) {
		super.cursorPressed(event);
		disturb((int)(event.getPosition().x * width),
				height-(int)((event.getPosition().y * height)));

	}

	public void cursorChanged(MultiTouchCursorEvent event) {
		super.cursorChanged(event);
		disturb((int)(event.getPosition().x * width),
				height-(int)((event.getPosition().y * height)));
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

	@Override
	public void render(Graphics2D g2d) {
		calculateFPS();
		newframe();
		source.newPixels();
		offGraphics.drawImage(image,0,0,width,height,null);
		g2d.drawImage(offImage,0,0,null);
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

}

