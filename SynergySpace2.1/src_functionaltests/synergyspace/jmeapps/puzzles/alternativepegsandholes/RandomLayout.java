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

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

import synergyspace.jmeapps.puzzles.framework.Layout;

public class RandomLayout extends Layout {

	private int height = 0;
	private int width = 0;
	private int attemptCount = 0;
	private int maxAttempts = 40;
	private ArrayList<PolygonMesh> objects = new ArrayList<PolygonMesh>();
	private ArrayList<PolygonMesh> holes = new ArrayList<PolygonMesh>();
	public boolean wasPossible = true;

	public RandomLayout(Node rootNode, PolygonMesh brick) {
		super(rootNode);
		width = (int)brick.getWidth()/2;
		height = (int)brick.getHeight()/2;
	}

	@Override
	public void setupPiece(Spatial s){
		setTranslation((PolygonMesh)s, "hole");
		if (wasPossible){
			rootNode.attachChild(s);
		}
	}

	@Override
	public void setupProblem(Spatial s){
		randomRotation(s);
		randomScale(s);
		setTranslation((PolygonMesh)s, "object");
		if(wasPossible){
			rootNode.attachChild(s);
		}
	}

	private void setTranslation(PolygonMesh part, String type){
		float maxWidth = 0;
		float maxHeight = 0;

		ArrayList<PolygonMesh> otherParts = null;

		part.setLocalTranslation(0, 0, 0);

		if (type.equals("object")){
			maxWidth = DisplaySystem.getDisplaySystem().getWidth()/2;
			maxHeight = DisplaySystem.getDisplaySystem().getHeight()/2;

			otherParts = objects;

			part.setZOrder(-1);

		}else{
			maxWidth = width;
			maxHeight = height;

			otherParts = holes;

			part.setZOrder(-2);
		}

		maxWidth -= (part.getWidth() + 5);
		maxHeight -= (part.getHeight() + 5);

		float x = FastMath.rand.nextFloat() * maxWidth;
		float y = FastMath.rand.nextFloat() * maxHeight;

		if (FastMath.rand.nextInt(2) == 1){
			x = -x;
		}
		if (FastMath.rand.nextInt(2) == 1){
			y = -y;
		}

		part.setLocalTranslation(x, y, 0);

		boolean moveAgain = false;

		if (type.equals("object")){
			if (x == 0 || y == 0){
				moveAgain = true;
			}else{
				if (x > 0){
					if (x < width){
						moveAgain = true;
					}
				}else{
					if (-x < width){
						moveAgain = true;
					}
				}
				if (y > 0){
					if (y < height){
						moveAgain = true;
					}
				}else{
					if (-y < height){
						moveAgain = true;
					}
				}
			}
		}
		for(PolygonMesh otherPart : otherParts){
			if (part.hasCollision(otherPart, true)){
				moveAgain = true;
			}
		}


		attemptCount++;

		if (moveAgain && attemptCount < maxAttempts){
			setTranslation(part, type);
		}

		attemptCount = 0;

		if (type.equals("object")){
			objects.add(part);
		}else{
			holes.add(part);
		}

	}

	public void randomRotation(Spatial spatial){
		Quaternion rot = new Quaternion();
		rot.fromAngles(0f, 0f, FastMath.rand.nextFloat() * FastMath.PI);
		spatial.setLocalRotation(rot);
	}

	public void randomScale(Spatial spatial){
		Vector3f s = spatial.getLocalScale();
		float rand = FastMath.rand.nextFloat() * 0.3f;
		if (FastMath.rand.nextInt(2) == 1){
			rand = -rand;
		}
		s.scaleAdd(rand, s);
		spatial.setLocalScale(s);
	}

}
