/*
 * Copyright (c) 2008 University of Durham, England
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

package synergyspace.jme.cursorsystem.elements.twod;

import java.awt.Point;
import com.jme.math.Vector3f;

import synergyspace.jme.cursorsystem.TwoDMultiTouchElement;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.jme.gfx.twod.DrawableSpatialImage;
import synergyspace.jme.pickingsystem.AccuratePickingUtility;
import synergyspace.mtinput.events.MultiTouchCursorEvent;

public class CursorEventDispatcher extends TwoDMultiTouchElement {
	protected float pixelsPerUnit;
	protected DrawableSpatialImage quad;


	public CursorEventDispatcher(DrawableSpatialImage drawableSpatialImage, float pixelsPerUnit) {
		super(drawableSpatialImage.getSpatial());

		this.quad = drawableSpatialImage;
		this.pixelsPerUnit = pixelsPerUnit;
	}

	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
		Point p = getCurrentElement2DCoordsForCursor(getScreenCursorByID(event.getCursorID()));
		quad.cursorDragged(event.getCursorID(), p.x, p.y);		
	}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {
	}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		Point p = getCurrentElement2DCoordsForCursor(getScreenCursorByID(event.getCursorID()));
		quad.cursorPressed(event.getCursorID(), p.x, p.y);		
	}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {		
		Point p = getCurrentElement2DCoordsForCursor(getScreenCursorByID(event.getCursorID()));
		quad.cursorReleased(event.getCursorID(), p.x, p.y);
	}

	protected Point getCurrentElement2DCoordsForCursorIndex(int index) {	
		Vector3f cursorPosition = AccuratePickingUtility.getPointOfIntersection(pickingSpatial.getParent(), this.screenCursors.get(index).getCurrentCursorScreenPosition().getPosition(), pickingSpatial);
		if(cursorPosition == null) return null;
		Vector3f selectionLocal = new Vector3f();
		pickingSpatial.worldToLocal(cursorPosition, selectionLocal);
		selectionLocal.addLocal(new Vector3f(quad.getWidth()/2f, quad.getHeight()/2f, 0f));		
		Point p = new Point();
		p.x = (int)(selectionLocal.x / quad.getWidth() * quad.getImageWidth());
		p.y = quad.getImageHeight() - ((int)(selectionLocal.y / quad.getHeight() * quad.getImageHeight()));
		return p;		

	}

	protected Point getCurrentElement2DCoordsForCursor(ScreenCursor cursor) {
		if(cursor == null) return null;
		Vector3f cursorPosition = new Vector3f(cursor.getCurrentCursorScreenPosition().x, cursor.getCurrentCursorScreenPosition().y, 0f);
		if(cursorPosition == null) return null;
		Vector3f selectionLocal = new Vector3f();
		pickingSpatial.worldToLocal(cursorPosition, selectionLocal);
		selectionLocal.addLocal(new Vector3f(quad.getWidth()/2f, quad.getHeight()/2f, 0f));		
		Point p = new Point();
		p.x = (int)(selectionLocal.x / quad.getWidth() * quad.getImageWidth());
		p.y = quad.getImageHeight() - ((int)(selectionLocal.y / quad.getHeight() * quad.getImageHeight()));
		return p;		
	}

}
