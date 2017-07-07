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

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.util.geom.BufferUtils;

import synergyspace.jmeapps.puzzles.framework.Matcher;
import synergyspace.jmeapps.puzzles.framework.Piece;

public class VertexMatcher extends Matcher {

	private float tolerance = 10f;

	public VertexMatcher(Spatial target) {
		super(target);
	}

	@Override
	public boolean isSolution(Piece p){
		int count = 0;
		PuzzlePart a = (PuzzlePart)p.getSpatial();
		PuzzlePart b = (PuzzlePart)target;
		Vector3f[] theseCoords = BufferUtils.getVector3Array(a.getVertexBuffer());
		Vector3f[] otherCoords = BufferUtils.getVector3Array(b.getVertexBuffer());
		boolean[] foundCorrospondant = new boolean[otherCoords.length];
		if (theseCoords.length == otherCoords.length){
			for (int i = 0; i < theseCoords.length; i++){
				for (int j = 0; j < theseCoords.length; j++){
					if (vertexMatch(a.getWorldTranslation(), theseCoords[i],
							a.getWorldTranslation(),  otherCoords[j])){
						//Check that this vertex has not been paired up before
						if (!foundCorrospondant[j]){
							foundCorrospondant[j] = true;
							count++;
							j = theseCoords.length;
						}
					}
				}
			}
		}else{
			return false;
		}
		if (count == theseCoords.length){
			return true;
		}else{
			return false;
		}
	}

	private boolean vertexMatch(Vector3f translationOne, Vector3f vertexOne,
			Vector3f translationTwo, Vector3f vertexTwo) {
		//Check that each vertex is close to its supposed counterpart
		Vector3f vOne = new Vector3f(translationOne.x + vertexOne.x, translationOne.y + vertexOne.y, 0);
		Vector3f vTwo = new Vector3f(translationTwo.x + vertexTwo.x, translationTwo.y + vertexTwo.y, 0);
		if (vOne.distance(vTwo) < tolerance){
			return true;
		}
		return false;
	}

}
