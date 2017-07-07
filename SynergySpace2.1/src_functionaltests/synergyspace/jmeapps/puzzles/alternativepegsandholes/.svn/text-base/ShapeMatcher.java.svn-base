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
import java.util.List;

import synergyspace.jme.gfx.twod.PolygonMesh;

import com.jme.math.FastMath;
import com.jme.scene.Spatial;

import synergyspace.jmeapps.puzzles.framework.Matcher;
import synergyspace.jmeapps.puzzles.framework.Piece;

public class ShapeMatcher extends Matcher {

	private static final double scaleTolerance = 0.2;
	private static final int rotTolerance = 10;

	public ShapeMatcher(Spatial target) {
		super(target);
	}

	@Override
	public boolean isSolution(Piece p){
		boolean result = false;
		result = vertexMatch(p);
		return result;
	}

	public boolean vertexMatch(Piece p){

		//A better method would be comparing the image graphics

		PolygonMesh a = (PolygonMesh)p.getSpatial();
		PolygonMesh b = (PolygonMesh)target;
		if (!a.getShapeType().equals("") && !b.getShapeType().equals("")){
			if (a.getShapeType().equals(b.getShapeType())){
				if (b.getLocalScale().x > 1-scaleTolerance && b.getLocalScale().x < 1+scaleTolerance){
					if (b.getShapeType().equals("circle")){
						return true;
					} else {
						List<Integer> rotationValues = new ArrayList<Integer>();
						rotationValues.add(0);
						double rotation = FastMath.RAD_TO_DEG * b.getLocalRotation().z;
						if (b.getShapeType().equals("rectangle") || b.getShapeType().equals("oval")){
							rotationValues.add(180);
							rotationValues.add(-180);
						}else if (b.getShapeType().equals("square")){
							System.out.println(rotation);
							rotationValues.add(45);
							rotationValues.add(-45);
							rotationValues.add(90);
							rotationValues.add(-90);
							rotationValues.add(180);
							rotationValues.add(-180);
						}
						return withinTolerance(rotation, rotationValues);
					}
				}
			}
		}
		return false;

	}

	private boolean withinTolerance(double rotation, List<Integer> rotationValues) {
		for (int i : rotationValues){
			if (i - rotTolerance < rotation && i + rotTolerance > rotation){
				return true;
			}
		}
		return false;
	}


}
