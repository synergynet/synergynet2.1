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

package synergyspace.jme.cursorsystem.elements.threed;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

import synergyspace.jme.cursorsystem.ThreeDMultiTouchElement;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.jme.cursorsystem.cursordata.WorldCursorRecord;
import synergyspace.jme.gfx.JMEGfxUtils;
import synergyspace.jme.pickingsystem.data.ThreeDPickResultData;
import synergyspace.mtinput.events.MultiTouchCursorEvent;

public class ControlPointRotateTranslateScale extends ThreeDMultiTouchElement {

	protected float scaleMin = Float.NaN;
	protected float scaleMax = Float.NaN;
	
	protected List<RotateTranslateScaleListener> listeners = new ArrayList<RotateTranslateScaleListener>();

	public ControlPointRotateTranslateScale(Spatial pickingAndTargetSpatial) {
		super(pickingAndTargetSpatial);
	}

	public ControlPointRotateTranslateScale(Spatial pickSpatial, Spatial targetSpatial) {
		super(pickSpatial, targetSpatial);
	}

	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
		if(screenCursors.size() <2 ) {

			Vector2f screenPos = c.getCurrentCursorScreenPosition().getPosition();

			long eventCursorID = event.getCursorID();
			ThreeDPickResultData pickResultData = getPickDataForCursorID(eventCursorID);

			Vector2f cursorPosition = screenPos.subtract(pickResultData.getCursorToSpatialScreenOffset());
			Vector3f newPosition = JMEGfxUtils.getCursorWorldCoordinatesOnSpatialPlane(cursorPosition, targetSpatial);
			if(newPosition != null) {
				targetSpatial.getParent().worldToLocal(newPosition, targetSpatial.getLocalTranslation());
				worldLocations.add(new WorldCursorRecord(targetSpatial.getWorldTranslation().clone(),
						new Quaternion(targetSpatial.getWorldRotation().clone()), targetSpatial.getWorldScale().clone(), System.nanoTime()));
				
				for (RotateTranslateScaleListener l: listeners)
					l.itemMoved(targetSpatial, newPosition.x, newPosition.y, cursorPosition.x, cursorPosition.y);
		
			}

		}else if(screenCursors.size() == 2){
			Vector2f originalScreenPosCursor1 = getScreenCursorByIndex(0).getCursorOrigin().getPosition();
			Vector2f originalScreenPosCursor2 = getScreenCursorByIndex(1).getCursorOrigin().getPosition();
			Vector2f screenPosCursor1 = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().getPosition();
			Vector2f screenPosCursor2 = getScreenCursorByIndex(1).getCurrentCursorScreenPosition().getPosition();
			ThreeDPickResultData pr = getPickResultFromCursorIndex(0);
			Vector2f spatialScreenAtPick = pr.getSpatialScreenLocationAtPick();
			Vector2f cursor1ToSpatialAtPick = spatialScreenAtPick.subtract(originalScreenPosCursor1);

			float originalCursorAngle = originalScreenPosCursor2.subtract(originalScreenPosCursor1).getAngle();
			float newCursorAngle = screenPosCursor2.subtract(screenPosCursor1).getAngle();
			float theta = newCursorAngle - originalCursorAngle;

			float newCursor1ToSpatialAngle = cursor1ToSpatialAtPick.getAngle() + theta;

			float cursorScale = screenPosCursor2.subtract(screenPosCursor1).length() / originalScreenPosCursor2.subtract(originalScreenPosCursor1).length();

			float newDistFromCursor1ToSpatial = cursorScale * cursor1ToSpatialAtPick.length();

			float dx = newDistFromCursor1ToSpatial * FastMath.cos(newCursor1ToSpatialAngle);
			float dy = newDistFromCursor1ToSpatial * FastMath.sin(newCursor1ToSpatialAngle);

			Vector2f newScreenPosition = screenPosCursor1.add(new Vector2f(dx, -dy));

			Vector3f newPosition = JMEGfxUtils.getCursorWorldCoordinatesOnSpatialPlane(newScreenPosition, targetSpatial);
			if(newPosition != null) {
				targetSpatial.getParent().worldToLocal(newPosition, targetSpatial.getLocalTranslation());
				worldLocations.add(new WorldCursorRecord(targetSpatial.getWorldTranslation().clone(),
						new Quaternion(targetSpatial.getWorldRotation().clone()), targetSpatial.getWorldScale().clone(), System.nanoTime()));
				
				for (RotateTranslateScaleListener l: listeners)
					l.itemMoved(targetSpatial, newScreenPosition.x, newScreenPosition.y, cursor1ToSpatialAtPick.x, cursor1ToSpatialAtPick.y);
		
				
				targetSpatial.setLocalRotation(getCurrentTargetSpatialRotationFromCursorChange());

				if((getScaleAtOrigin().mult(cursorScale).x < scaleMax) && (getScaleAtOrigin().mult(cursorScale).x > scaleMin)) {
					targetSpatial.setLocalScale(getScaleAtOrigin().mult(cursorScale));
				}

				targetSpatial.updateGeometricState(0f, true);
			}

		}
	}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {}


	public float getScaleMin() {
		return scaleMin;
	}

	public void setScaleMin(float scaleMin) {
		this.scaleMin = scaleMin;
	}

	public float getScaleMax() {
		return scaleMax;
	}

	public void setScaleMax(float scaleMax) {
		this.scaleMax = scaleMax;
	}

	public void setScaleLimits(float min, float max) {
		setScaleMin(min);
		setScaleMax(max);
	}
	
	public void addRotateTranslateScaleListener(RotateTranslateScaleListener l){
		listeners.add(l);
	}

	public void removeRotateTranslateScaleListener(RotateTranslateScaleListener l){
		if (listeners.contains(l))
			listeners.remove(l);
	}
	
	public interface RotateTranslateScaleListener {
		public void itemMoved(Spatial targetSpatial,  float newLocationX, float newLocationY, float oldLocationX, float oldLocationY);
	}

}
