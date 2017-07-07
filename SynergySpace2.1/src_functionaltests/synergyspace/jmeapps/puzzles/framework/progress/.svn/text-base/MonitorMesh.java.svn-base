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

package synergyspace.jmeapps.puzzles.framework.progress;

import java.awt.BasicStroke;
import java.awt.Color;

import com.jme.renderer.ColorRGBA;

import synergyspace.jme.gfx.twod.PolygonMesh;

public class MonitorMesh extends PolygonMesh {

	private static final long serialVersionUID = 1L;
	static private int outerRadius;

	public MonitorMesh(String name, int x, int y) {
		super(name, x, y);
	}

	public void addCircleBorder(int radius, ColorRGBA c){

		Color colour = new Color(c.r, c.g, c.b);

		gfx.setColor(colour);

		gfx.setStroke(new BasicStroke(3));

		outerRadius = radius;

		gfx.drawArc(outerRadius/2-(radius-1)/2, outerRadius/4-(radius/2-1)/2,
				radius-1, radius/2-1, 0, 360);
	}

	public void addArcFilled(int startDegree, int endDegree, int radius, ColorRGBA c){

		Color colour = new Color(c.r, c.g, c.b);

		gfx.setStroke(new BasicStroke(1));
		gfx.setColor(colour);

		gfx.fillArc(outerRadius/2-(radius-1)/2, outerRadius/4-(radius/2-1)/2,
				radius-1, radius/2-1, startDegree, endDegree);

	}


}
