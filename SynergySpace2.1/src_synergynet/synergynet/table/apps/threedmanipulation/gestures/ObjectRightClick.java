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

package synergynet.table.apps.threedmanipulation.gestures;

import java.util.ArrayList;
import java.util.List;

import com.jme.scene.Spatial;

import synergyspace.jme.cursorsystem.ThreeDMultiTouchElement;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.mtinput.events.MultiTouchCursorEvent;

public class ObjectRightClick extends ThreeDMultiTouchElement {

	protected float rightClickDistance = 10f;
	protected List<CursorEventListener> listeners = new ArrayList<CursorEventListener>();
	
	public ObjectRightClick(Spatial pickingAndTargetSpatial) {
		super(pickingAndTargetSpatial);
	}
	
	public ObjectRightClick(Spatial pickingSpatial,
			Spatial targetSpatial) {
		super(pickingSpatial, targetSpatial);
	}
	
	public void setRightClickDistance(float rightClickDistance){
		this.rightClickDistance = rightClickDistance;
	}

	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {
			
		for (CursorEventListener l: listeners){
			
			//if (RightClickDetector.isDoubleClick(screenCursors, c, rightClickDistance)){
				l.cursorRightClicked( c, event);
			//}
		}
	}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {}
	
	public interface CursorEventListener {
			public void cursorRightClicked(ScreenCursor c, MultiTouchCursorEvent event);
	}
	
	public void addMultiTouchListener(CursorEventListener l) {
		if(!listeners.contains(l))
			listeners.add(l);
	}
	
	public void removeMultiTouchListener(CursorEventListener l) {
		listeners.remove(l);
	}

}
