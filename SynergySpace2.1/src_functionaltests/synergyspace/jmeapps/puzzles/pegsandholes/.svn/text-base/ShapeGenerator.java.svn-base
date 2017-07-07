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

package synergyspace.jmeapps.puzzles.pegsandholes;

import java.util.ArrayList;

import synergyspace.jme.cursorsystem.elements.twod.OrthoBringToTop;
import synergyspace.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;

abstract class ShapeGenerator {

	private ArrayList<PuzzlePart> objects = new ArrayList<PuzzlePart>();
	private ArrayList<PuzzlePart> holes = new ArrayList<PuzzlePart>();
	private ArrayList<OrthoControlPointRotateTranslateScale> movers =
											new ArrayList<OrthoControlPointRotateTranslateScale>();
	private ArrayList<OrthoBringToTop> topps = new ArrayList<OrthoBringToTop>();

	public ShapeGenerator(){}

	public void getShape(String shapeName, int count){

	}

	public void createPuzzlePart(String name, float[][] coords, float width, float height, ColorRGBA colour){

		PuzzlePart hole = new PuzzlePart(name + "void", coords, width, height, 
				DisplaySystem.getDisplaySystem().getRenderer().getBackgroundColor());
		hole.setModelBound(new OrthogonalBoundingBox());
		hole.updateModelBound();
		hole.lockMeshes();
		hole.updateRenderState();
		holes.add(hole);

		PuzzlePart object = new PuzzlePart(name + "object", coords, width, height, colour);
		object.setModelBound(new OrthogonalBoundingBox());
		object.updateModelBound();
		object.lockMeshes();

		object.updateRenderState();
		objects.add(object);

		OrthoControlPointRotateTranslateScale ocprts = new OrthoControlPointRotateTranslateScale(object);
		ocprts.setScaleLimits(0.8f, 2.0f);

		movers.add(ocprts);
		topps.add(new OrthoBringToTop(object));

	}

	public ArrayList<PuzzlePart> getObjects(){
		return objects;
	}

	public ArrayList<PuzzlePart> getHoles(){
		return holes;
	}

	public ArrayList<OrthoBringToTop> getBringToTopMovers(){
		return topps;
	}

	public ArrayList<OrthoControlPointRotateTranslateScale> getMovers(){
		return movers;
	}
}
