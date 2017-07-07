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

import java.applet.Applet;

import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.jme.cursorsystem.elements.twod.OrthoBringToTop;
import synergyspace.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;
import synergyspace.jmeapps.puzzles.framework.TemplateProblem;
import synergyspace.mtinput.events.MultiTouchCursorEvent;

import com.jme.scene.Spatial;


public class PiecesAligned extends TemplateProblem {

	private OrthoControlPointRotateTranslateScale OPRST = null;
	private OrthoBringToTop top = null;

	public PiecesAligned(Spatial pickingAndTargetSpatial) {
		super(pickingAndTargetSpatial);
	}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		if (pieces != null && !solved){
			if (checkSolved()){
				solved = true;
				lockPieces();
			}
		}
	}

	public void addController(OrthoControlPointRotateTranslateScale OPRST){
		this.OPRST = OPRST;
	}

	public void lockPieces() {

		targetSpatial.setLocalTranslation(satisfyingPiece.getLocalTranslation());
		targetSpatial.setLocalRotation(satisfyingPiece.getLocalRotation());
		targetSpatial.setLocalScale(satisfyingPiece.getLocalScale());

		if (OPRST != null){
			OPRST.setActive(false);
		}

		if (top != null){
			top.setActive(false);
		}

		satisfyingPiece.getParent().detachChild(satisfyingPiece);

		Applet.newAudioClip(PiecesAligned.class.getResource("beep.wav")).play();
	}

	public void addToTopController(OrthoBringToTop orthoBringToTop) {
		top = orthoBringToTop;
	}

}