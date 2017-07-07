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

package synergyspace.jmeapps.puzzles.framework;

import java.util.ArrayList;
import java.util.List;

import com.jme.scene.Spatial;

import synergyspace.jme.cursorsystem.MultiTouchElement;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.mtinput.events.MultiTouchCursorEvent;

public abstract class TemplateProblem extends MultiTouchElement{

	public boolean solved = false;
	public static List<Piece> pieces;
	private List<Matcher> matchers = new ArrayList<Matcher>();
	public Spatial satisfyingPiece = null;
	private Spatial spatial = null;

	public TemplateProblem(Spatial spatial) {
		super(spatial);
		this.spatial = spatial;
	}

	public Spatial getSpatial(){
		return spatial;
	}

	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {}

	public boolean checkSolved() {
		Piece p = null;
		boolean matchTruth = false;
		for (Matcher m: matchers){
			for(int i = 0; i < pieces.size(); i++){
				if (m.isSolution(pieces.get(i))){
					matchTruth = true;
					p = pieces.get(i);
					satisfyingPiece = p.getSpatial();
					i = pieces.size();
				}
			}
			if (!matchTruth){
				return false;
			}
		}
		pieces.remove(p);
		return true;
	}

	public void addMatcher(Matcher matcher) {
		matchers.add(matcher);
	}

	public boolean getSolved(){
		return solved;
	}

}
