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

package synergyspace.jmeapps.lightbox;

import com.jme.math.FastMath;
import com.jme.math.Plane;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

import synergyspace.jme.cursorsystem.ThreeDMultiTouchElement;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.jme.gfx.JMEGfxUtils;
import synergyspace.jme.pickingsystem.data.ThreeDPickResultData;
import synergyspace.mtinput.events.MultiTouchCursorEvent;

public class ControlPointRotateTranslateScaleFixedZ extends ThreeDMultiTouchElement {

	private Plane planeAtFirstCursorPick;

	public ControlPointRotateTranslateScaleFixedZ(Spatial pickingAndTargetSpatial) {
		super(pickingAndTargetSpatial);
	}

	public ControlPointRotateTranslateScaleFixedZ(Spatial pickSpatial, Spatial targetSpatial) {
		super(pickSpatial, targetSpatial);
	}

	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
		if(screenCursors.size() <2 ) {
			
			Vector2f screenPos = c.getCurrentCursorScreenPosition().getPosition();

			long eventCursorID = event.getCursorID();
			ThreeDPickResultData pickResultData = (ThreeDPickResultData)this.getPickDataForCursorID(eventCursorID);

			Vector2f cursorPosition = screenPos.subtract(pickResultData.getCursorToSpatialScreenOffset());
			Vector3f newPosition = JMEGfxUtils.getCursorWoldCoorinatesOnPlane(cursorPosition, targetSpatial, planeAtFirstCursorPick);
			if(newPosition != null)
				targetSpatial.getParent().worldToLocal(newPosition, targetSpatial.getLocalTranslation());
			
		}else{
			Vector2f originalScreenPosCursor1 = getScreenCursorByIndex(0).getCursorOrigin().getPosition();
			Vector2f originalScreenPosCursor2 = getScreenCursorByIndex(1).getCursorOrigin().getPosition();
			Vector2f screenPosCursor1 = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().getPosition();
			Vector2f screenPosCursor2 = getScreenCursorByIndex(1).getCurrentCursorScreenPosition().getPosition();
			ThreeDPickResultData prd = (ThreeDPickResultData) getScreenCursorByIndex(0).getPickResult();
			Vector2f spatialScreenAtPick = prd.getSpatialScreenLocationAtPick();
			Vector2f cursor1ToSpatialAtPick = spatialScreenAtPick.subtract(originalScreenPosCursor1);

			float originalCursorAngle = originalScreenPosCursor2.subtract(originalScreenPosCursor1).getAngle();
			float newCursorAngle = screenPosCursor2.subtract(screenPosCursor1).getAngle();
			float theta = newCursorAngle - originalCursorAngle;

			float newCursor1ToSpatialAngle = cursor1ToSpatialAtPick.getAngle() + theta;

			float scale = screenPosCursor2.subtract(screenPosCursor1).length() / originalScreenPosCursor2.subtract(originalScreenPosCursor1).length();

			float newDistFromCursor1ToSpatial = scale * cursor1ToSpatialAtPick.length();
			float dx = newDistFromCursor1ToSpatial * FastMath.cos(newCursor1ToSpatialAngle);
			float dy = newDistFromCursor1ToSpatial * FastMath.sin(newCursor1ToSpatialAngle);

			Vector2f newScreenPosition = screenPosCursor1.add(new Vector2f(dx, -dy));

			Vector3f newPosition = JMEGfxUtils.getCursorWoldCoorinatesOnPlane(newScreenPosition, targetSpatial, planeAtFirstCursorPick);
			if(newPosition != null) {
				targetSpatial.getParent().worldToLocal(newPosition, targetSpatial.getLocalTranslation());
				targetSpatial.setLocalRotation(getCurrentTargetSpatialRotationFromCursorChange());	
				targetSpatial.setLocalScale(getScaleAtOrigin().mult(scale));				
				targetSpatial.updateGeometricState(0f, true);
			}
				
		}
	}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		if(screenCursors.size() == 1) {			
			planeAtFirstCursorPick = new Plane();			
			Vector3f cameraLocation = DisplaySystem.getDisplaySystem().getRenderer().getCamera().getLocation();
			Vector3f camToTargetSpatial = cameraLocation.subtract(targetSpatial.getWorldTranslation());
			planeAtFirstCursorPick.setNormal(camToTargetSpatial.normalize());
			planeAtFirstCursorPick.setConstant(camToTargetSpatial.length());
//			System.out.println("plane at " + planeAtFirstCursorPick);
		}
	}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {}

}
