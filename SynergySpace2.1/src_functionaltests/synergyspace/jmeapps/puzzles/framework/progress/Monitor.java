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

import java.util.ArrayList;
import java.util.List;

import synergyspace.jmeapps.puzzles.framework.Puzzle;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class Monitor {

	static private List<MonitorMesh> allSpatials = new ArrayList<MonitorMesh>();
	static private List<MonitorMesh> innerSpatials = new ArrayList<MonitorMesh>();
	static private List<ColorRGBA> colours = new ArrayList<ColorRGBA>();
	static private MonitorMesh outerSpatial;
	static private List<Integer> levels = new ArrayList<Integer>();
	private Node rootNode;
	private int size;
	private float segmentSize;
	private int radius;
	private Vector3f translation;

	public Monitor(int size, int radius, List<ColorRGBA> colours, Vector3f translation, Node rootNode) {
		this.rootNode = rootNode;
		this.size = size;
		this.translation = translation;
		this.radius = radius;
		Monitor.colours = colours;

		outerSpatial = new MonitorMesh("outerCircle", radius, radius);
		outerSpatial.addCircleBorder(radius, Puzzle.inverse);
		outerSpatial.setZOrder(-3);
		allSpatials.add(outerSpatial);

		segmentSize = 360/colours.size();

		for (int i = 0; i < colours.size(); i++){
			makeSegment(radius, colours.get(i));
		}
		outerSpatial.setLocalTranslation(translation);

	}

	private void makeSegment(float radius, ColorRGBA colour) {

		MonitorMesh s = new MonitorMesh("innerSegment" + innerSpatials.size(), (int)radius, (int)radius);
		s.setLocalTranslation(translation);
		s.setZOrder(-3);
		levels.add(1);
		innerSpatials.add(s);
		allSpatials.add(s);
	}

	public void changeColour(int i, ColorRGBA colour){
		colours.set(i, colour);
	}

	public void addSpatialsToNode(){
		for (Spatial s: allSpatials){
			rootNode.attachChild(s);
		}
	}

	public void increase(int i) {
		float offset = i * segmentSize;
		innerSpatials.get(i).clear();
		innerSpatials.get(i).addArcFilled((int)offset, (int)(segmentSize),
				radius * levels.get(i)/size, colours.get(i));
		levels.set(i, levels.get(i)+1);
	}

}