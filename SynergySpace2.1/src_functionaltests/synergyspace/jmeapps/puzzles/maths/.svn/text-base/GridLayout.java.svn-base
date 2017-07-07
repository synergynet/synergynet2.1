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

import com.jme.math.FastMath;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

import synergyspace.jmeapps.puzzles.framework.Layout;

public class GridLayout extends Layout {

	public GridLayout(Node rootNode) {
		super(rootNode);
	}

	private int y = 200;
	private int questionCount = 1;

	@Override
	public void setupPiece(Spatial s){
		int x = 0;
		if (questionCount < 5){
			x = -250;
		} else if (questionCount >= 9 && questionCount < 13){
			x = 250;
		}

		if (questionCount >= 13){
			System.out.println("Question " + questionCount + " is an invalid question");
		}else{
			s.setLocalTranslation(x, y-50, 0);
			y -= 100;
			if (y == -200){
				y = 200;
			}
		}
		questionCount++;
		pieces.add(s);
	}

	@Override
	public void setupProblem(Spatial s){

		float x = FastMath.rand.nextFloat() * 300f;
		float y = FastMath.rand.nextFloat() * 200f;

		if (FastMath.rand.nextInt(2) == 1){
			x = -x;
		}
		if (FastMath.rand.nextInt(2) == 1){
			y = -y;
		}

		s.setLocalTranslation(x, y, 0f);
		problems.add(s);
	}

	public void addPartsToRoot() {
		for (Spatial s: pieces){
			rootNode.attachChild(s);
		}
		for (Spatial s: problems){
			rootNode.attachChild(s);
		}

	}

}
