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

package synergyspace.awtapps.geometry;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import synergyspace.awt.AWTAppRenderer;
import synergyspace.mtinput.Blob;

public class GeometryAppRenderer extends AWTAppRenderer {

	protected int frameCount = 0;
	protected long lastTime = 0;
	protected int fps = 0;
	
	protected List<Shape> shapes = new ArrayList<Shape>();
	
	public void addShape(Shape s) {
		if(!shapes.contains(s)) shapes.add(s);
	}
	
	public void render(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		calculateFPS();
		drawBackground(g2d);
		findPicks();
		drawShapes(g2d);
		drawBlobs(g2d);
		drawFPS(g2d);
	}

	private void findPicks() {
		synchronized(blobs) {
			for(Blob b : blobs.values()) {
				Point2D.Float blobLoc = new Point2D.Float(b.x, this.height - b.y);
				for(Shape s : shapes) {
					if(s.contains(blobLoc)) {
						System.out.println("Blob inside " + s);
					}
				}
			}
		}
		
	}

	private void drawShapes(Graphics2D g2d) {
		g2d.setColor(Color.white);
		for(Shape s : shapes) {
			g2d.draw(s);
		}
	}

	private void drawBlobs(Graphics2D g2d) {
		g2d.setColor(Color.blue);
		synchronized(blobs) {
			for(Blob b : blobs.values()) {
				AffineTransform origTrans = g2d.getTransform();

				g2d.translate(b.x, this.height - b.y);				
				g2d.setColor(Color.blue);
				int size = 10 + (int)b.pressure;				
				g2d.fillOval(-size/2, -size/2, size, size);
				g2d.setColor(Color.white);
				g2d.drawString(b.id + " (" + b.x + "," + b.y + ")", 20, 20);

				g2d.setTransform(origTrans);
			}
		}
	}

	private void drawBackground(Graphics2D g2d) {
		g2d.setColor(Color.darkGray);
		g2d.fillRect(0, 0, width, height);
	}
	
	private void drawFPS(Graphics2D g2d) {
		g2d.setColor(Color.lightGray);
		g2d.drawString("FPS: " + fps, 4, 12);
	}

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
