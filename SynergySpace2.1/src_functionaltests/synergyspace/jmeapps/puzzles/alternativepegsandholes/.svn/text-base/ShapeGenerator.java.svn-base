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

package synergyspace.jmeapps.puzzles.alternativepegsandholes;

import java.util.ArrayList;


import synergyspace.jme.gfx.twod.PolygonMesh;

import synergyspace.jme.cursorsystem.elements.twod.OrthoBringToTop;
import synergyspace.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;
import synergyspace.jme.cursorsystem.flicksystem.FlickSystem;
import synergyspace.jmeapps.puzzles.framework.Piece;
import synergyspace.jmeapps.puzzles.framework.Puzzle;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.renderer.ColorRGBA;

class ShapeGenerator {

	private final static float friction = 1.0f;

	private ArrayList<Piece> objects = new ArrayList<Piece>();
	private ArrayList<Piece> holes = new ArrayList<Piece>();
	private ArrayList<OrthoControlPointRotateTranslateScale> movers =
											new ArrayList<OrthoControlPointRotateTranslateScale>();
	private ArrayList<OrthoBringToTop> topps = new ArrayList<OrthoBringToTop>();
	private FlickSystem flicking;

	private ArrayList<String> svgNames = new ArrayList<String>();
	private ArrayList<float[][]> svgCoords = new ArrayList<float[][]>();
	private ArrayList<ColorRGBA> svgColours = new ArrayList<ColorRGBA>();

	public ShapeGenerator(FlickSystem flicking){
		this.flicking = flicking;
	}

	public void createPuzzlePart(String name, float[][] coords, ColorRGBA colour){
		svgNames.add(name);
		svgCoords.add(coords);
		svgColours.add(colour);
	}

	public void setOutSVG(){
		for (int i = 0; i < svgNames.size(); i++){
			prepare(svgNames.get(i), svgCoords.get(i), svgColours.get(i));
		}
	}

	public void prepare(String name, float[][] coords, ColorRGBA colour){

		ColorRGBA holeColour;

		if (Puzzle.inverse.equals(ColorRGBA.white)){
			holeColour = ColorRGBA.black;
			PolygonMesh.setBorder(ColorRGBA.gray, 8);
		}else{
			holeColour = ColorRGBA.white;
			PolygonMesh.setBorder(ColorRGBA.lightGray, 8);
		}

		PolygonMesh hole = new PolygonMesh(name + "void", calcWidth(coords), calcHeight(coords));
		hole.addPolygonFilled(coords, holeColour, false);
		hole.setModelBound(new OrthogonalBoundingBox());
		hole.updateModelBound();
		hole.lockMeshes();
		hole.updateRenderState();
		Piece p = new Piece(hole, hole.getWidth(), hole.getHeight());
		holes.add(p);

		PolygonMesh object = new PolygonMesh(name + "object", calcWidth(coords), calcHeight(coords));
		hole.addPolygonFilled(coords, colour, false);
		object.setModelBound(new OrthogonalBoundingBox());
		object.updateModelBound();
		object.lockMeshes();
		object.updateRenderState();
		Piece q = new Piece(object, hole.getWidth(), hole.getHeight());

		objects.add(q);

		OrthoControlPointRotateTranslateScale ocprts = new OrthoControlPointRotateTranslateScale(object);
		ocprts.setPickMeOnly(true);
		ocprts.setScaleLimits(0.8f, 2.0f);

		flicking.makeFlickable(object, ocprts, friction);

		movers.add(ocprts);
		OrthoBringToTop obtt = new OrthoBringToTop(object);
		obtt.setPickMeOnly(true);
		topps.add(obtt);

	}

	public void createPuzzlePart(String name, String type, ColorRGBA colour){

		PolygonMesh object;
		PolygonMesh hole;

		ColorRGBA holeColour;

		if (Puzzle.inverse.equals(ColorRGBA.white)){
			holeColour = ColorRGBA.black;
			PolygonMesh.setBorder(ColorRGBA.gray, 8);
		}else{
			holeColour = ColorRGBA.white;
			PolygonMesh.setBorder(ColorRGBA.lightGray, 8);
		}

		if (type.equals("circle")){
			object = new PolygonMesh(name, 50, 50);
			hole = new PolygonMesh(name, 50, 50);
			object.addCircleFilled(250, colour, true);
			hole.addCircleFilled(250, holeColour, false);
			object.setShapeType("circle");
			hole.setShapeType("circle");
		} else if (type.equals("triangle")){
			object = new PolygonMesh(name, 75, 75);
			hole = new PolygonMesh(name, 75, 75);
			object.addTriangleFilled(colour, true);
			hole.addTriangleFilled(holeColour, false);
			object.setShapeType("triangle");
			hole.setShapeType("triangle");
		}else if (type.equals("square")){
			object = new PolygonMesh(name, 50, 50);
			hole = new PolygonMesh(name, 50, 50);
			object.addSquareFilled(colour, 50, true);
			hole.addSquareFilled(holeColour, 50, false);
			object.setShapeType("square");
			hole.setShapeType("square");
		}else if (type.equals("rectangle")){
			object = new PolygonMesh(name, 85, 50);
			hole = new PolygonMesh(name, 85, 50);
			object.addSquareFilled(colour, 85, true);
			hole.addSquareFilled(holeColour, 85, false);
			object.setShapeType("rectangle");
			hole.setShapeType("rectangle");
		}else{
			object = new PolygonMesh(name, 85, 50);
			hole = new PolygonMesh(name, 85, 50);
			object.addOvalFilled(250, 125, colour, true);
			hole.addOvalFilled(250, 125, holeColour, false);
			object.setShapeType("oval");
			hole.setShapeType("oval");
		}

		hole.setOtherColour(colour);
		hole.changeTextureMode();
		object.changeTextureMode();

		hole.setModelBound(new OrthogonalBoundingBox());
		hole.updateModelBound();
		hole.lockMeshes();
		hole.updateRenderState();

		Piece p = new Piece(hole, hole.getWidth(), hole.getHeight());
		holes.add(p);

		object.setModelBound(new OrthogonalBoundingBox());
		object.updateModelBound();
		object.lockMeshes();
		object.updateRenderState();
		Piece q = new Piece(object, hole.getWidth(), hole.getHeight());

		objects.add(q);

		OrthoControlPointRotateTranslateScale ocprts = new OrthoControlPointRotateTranslateScale(object);
		ocprts.setPickMeOnly(true);
		ocprts.setScaleLimits(0.8f, 2.0f);

		flicking.makeFlickable(object, ocprts, friction);

		movers.add(ocprts);
		OrthoBringToTop obtt = new OrthoBringToTop(object);
		obtt.setPickMeOnly(true);
		topps.add(obtt);

	}

	public int calcWidth(float[][] theseCoords) {
		float xMin = 0;
		float xMax = 0;
		if (theseCoords.length > 0){
			xMin = theseCoords[0][0];
			xMax = theseCoords[0][0];
			for (int i = 0; i < theseCoords.length; i++){
				if (xMin > theseCoords[i][0]){
					xMin = theseCoords[i][0];
				}
				if (xMax < theseCoords[i][0]){
					xMax = theseCoords[i][0];
				}
			}
		}
		return (int)(xMax - xMin);
	}

	public int calcHeight(float[][] theseCoords) {
		float yMin = 0;
		float yMax = 0;
		if (theseCoords.length > 0){
			yMin = theseCoords[0][1];
			yMax = theseCoords[0][1];
			for (int i = 0; i < theseCoords.length; i++){
				if (yMin > theseCoords[i][1]){
					yMin = theseCoords[i][1];
				}
				if (yMax < theseCoords[i][1]){
					yMax = theseCoords[i][1];
				}
			}
		}
		return (int)(yMax - yMin);
	}

	public ArrayList<Piece> getObjects(){
		return objects;
	}

	public ArrayList<Piece> getHoles(){
		return holes;
	}

	public ArrayList<OrthoBringToTop> getBringToTopMovers(){
		return topps;
	}

	public ArrayList<OrthoControlPointRotateTranslateScale> getMovers(){
		return movers;
	}
}
