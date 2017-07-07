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

package synergyspace.jmeapps.puzzles.maths;

import synergyspace.jme.gfx.twod.MTText;

import com.jme.math.FastMath;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;

import synergyspace.jmeapps.puzzles.framework.Matcher;
import synergyspace.jmeapps.puzzles.framework.Piece;
import synergyspace.jmeapps.puzzles.framework.Puzzle;

public class LocRotScaleMatcher extends Matcher{

	private float gap = 0;
	private float xTolerancePos = 75;
	private float xToleranceNeg = 50;
	private float yTolerance = 20;
	private float rotationTolerance = 30;
	private float scaleTolerance = 3f;

	public LocRotScaleMatcher(Spatial target, float x, float y) {
		super(target, x, y);
	}

	@Override
	public boolean isSolution(Piece p){
		gap = x/2 + p.getWidth()/2;
		float yDifference = p.getSpatial().getWorldTranslation().y - target.getWorldTranslation().y;
		float xDifference = p.getSpatial().getWorldTranslation().x + gap - target.getWorldTranslation().x;
		if((yDifference > -yTolerance &&  yDifference < yTolerance) &&
				(xDifference > -xTolerancePos &&  xDifference < xToleranceNeg)){
			if (FastMath.RAD_TO_DEG*target.getLocalRotation().z < rotationTolerance &&
					FastMath.RAD_TO_DEG*target.getLocalRotation().z > -rotationTolerance){
				if (target.getLocalScale().x < scaleTolerance &&
						target.getLocalScale().x > 1/scaleTolerance){
					final MTText text = (MTText)p.getSpatial();
					text.changeColour(ColorRGBA.red);
					new Thread() {
			        	public void run() {
			        		try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
			        		text.changeColour(Puzzle.inverse);
			        	}
			        }.start();
					return true;
				}
			}
		}
		return false;
	}

}