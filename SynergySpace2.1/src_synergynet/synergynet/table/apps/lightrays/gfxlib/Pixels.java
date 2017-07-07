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

package synergynet.table.apps.lightrays.gfxlib;

import java.awt.Dimension;

public class Pixels {

	// 0xAARRGGBB
	public int[] pixels;
	public int width;
	public int height;
	
	public Pixels(int width, int height) {
		this(new int[width * height], width, height);
	}	
	
	public Pixels(int[] pixels, int w, int h) {
		this.pixels = pixels;
		this.width = w;
		this.height = h;
	}	
	
	public void setRGB(int x, int y, int r, int g, int b) {
		int index = x + (y * width);
		pixels[index] = rgbToInt(r, g, b);
	}
	
	public static int rgbToInt(int r, int g, int b) {
		int c = (r << 24) | (g << 16) | b;
		return c;
	}
	
	public int getPixel(int x, int y) {
		int index = x + (y * width);
		return pixels[index];
	}

	public int getHeight() {
		return height;
	}

	public int[] getPixels() {
		return pixels;
	}

	public int getWidth() {
		return width;
	}
	
	public String toString() {
		return width + "," + height;
	}	

	public Dimension getSize() {
		return new Dimension(width, height);
	}

	public void setPixel(int x, int y, int c) {
		pixels[(y*width) + x] = c;		
	}

}
