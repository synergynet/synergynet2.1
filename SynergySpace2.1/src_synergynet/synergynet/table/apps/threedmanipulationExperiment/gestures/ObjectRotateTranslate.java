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

package synergynet.table.apps.threedmanipulationExperiment.gestures;

import java.util.ArrayList;
import java.util.List;

import com.jme.intersection.PickResults;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

import synergyspace.jme.cursorsystem.ThreeDMultiTouchElement;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursorRecord;
import synergyspace.jme.gfx.JMEGfxUtils;
import synergyspace.jme.pickingsystem.data.ThreeDPickResultData;
import synergyspace.mtinput.events.MultiTouchCursorEvent;

public class ObjectRotateTranslate extends ThreeDMultiTouchElement {

	protected Vector2f cursor1Pos = new Vector2f();
	protected Vector2f cursor2Pos = new Vector2f();
	protected Vector2f cursor1OldPos = new Vector2f();
	protected Vector2f cursor2OldPos = new Vector2f();
	
	protected float scaleMin = Float.NaN;
	protected float scaleMax = Float.NaN;
	protected PickResults pickResults = null;
	protected Spatial remoteObject = null;
	
	protected float threshold = 0.25f;
	
	protected int rotationSpeed = 100;
	
	protected List<ObjectRotateTranslateScaleListener> listeners = new ArrayList<ObjectRotateTranslateScaleListener>();
	protected List<ExperimentEventListener> experimentEventlisteners = new ArrayList<ExperimentEventListener>();
	
	protected int touchNumber=0;
	
	protected boolean hasTaskCompletionMessageSent=false;
	
	public ObjectRotateTranslate(Spatial pickingAndTargetSpatial) {
		super(pickingAndTargetSpatial);
	}

	public ObjectRotateTranslate(Spatial pickSpatial, Spatial targetSpatial) {
		super(pickSpatial, targetSpatial);
	}
	
	public ObjectRotateTranslate(Spatial pickSpatial, Spatial targetSpatial, Spatial remoteObject) {
		super(pickSpatial, targetSpatial);
		this.remoteObject = remoteObject;

	}
	
	public void resetTouchNumber(){
		touchNumber =0;
		hasTaskCompletionMessageSent=false;
	}
	
	public void setRotationSpeed(int rotationSpeed){
		this.rotationSpeed = rotationSpeed;
	}
	
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
		
		if(screenCursors.size() == 1) {
			updateCursor1();
			applySingleCursorTransform();
			setOldCursor();
		}else if (screenCursors.size() == 2){
			updateCursor1();
			updateCursor2();
			applyMultiCursorTransform();
			setOldCursor();
		}
		
		
			
	}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		if(screenCursors.size() == 1) {
			cursor1OldPos.x = c.getCurrentCursorScreenPosition().x;
			cursor1OldPos.y = c.getCurrentCursorScreenPosition().y;
			this.touchNumber++;
		}else if(screenCursors.size() == 2) {
			updateCursor1();
			updateCursor2();
			cursor1OldPos = cursor1Pos.clone();
			cursor2OldPos = cursor2Pos.clone();
		}

	}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {}

	protected void applySingleCursorTransform() {
		int previousPostionIndex=0;
		if (getScreenCursorByIndex(0).getCurrentPositionIndex()>0){
			previousPostionIndex = getScreenCursorByIndex(0).getCurrentPositionIndex()-1;
		}
		
		Vector2f originalScreenPosCursor1 = getScreenCursorByIndex(0).getPositionAtIndex(previousPostionIndex).getPosition();
		Vector2f screenPosCursor1 = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().getPosition();	
		Vector2f cursor1ToSpatial = screenPosCursor1.subtract(originalScreenPosCursor1);

		float[] angles = {-cursor1ToSpatial.y/rotationSpeed, cursor1ToSpatial.x/rotationSpeed, 0};
		Quaternion q = new Quaternion();
		q.fromAngles(angles);	
		q.multLocal(targetSpatial.getLocalRotation());
		targetSpatial.setLocalRotation(q);
		
		if (remoteObject!=null){
			remoteObject.setLocalRotation(q);
			remoteObject.updateGeometricState(0f, true);
		}
		
		Quaternion currentAngle = this.targetSpatial.getLocalRotation();
		float angleToX = 0;
		float angleToY = 0;
		float angleToZ = 0;
		angleToX = currentAngle.toAngleAxis(Vector3f.UNIT_X);
		angleToY = currentAngle.toAngleAxis(Vector3f.UNIT_Y);
		angleToZ = currentAngle.toAngleAxis(Vector3f.UNIT_Z);
			
		System.out.println(angleToX);
		
		if ((Math.abs(angleToX)<=threshold || FastMath.TWO_PI-angleToX<=threshold) && (Math.abs(angleToY)<=threshold || FastMath.TWO_PI-angleToY<=threshold) && (Math.abs(angleToZ)<=threshold || FastMath.TWO_PI-angleToZ<=threshold) ){
			if (hasTaskCompletionMessageSent) return;
			hasTaskCompletionMessageSent=true;
			for (ExperimentEventListener l: experimentEventlisteners){
				l.taskCompleted(touchNumber);
			}
		}
	}

	protected void applyMultiCursorTransform() {
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

		//float cursorScale = screenPosCursor2.subtract(screenPosCursor1).length() / originalScreenPosCursor2.subtract(originalScreenPosCursor1).length();
	
		//float newDistFromCursor1ToSpatial = cursorScale * cursor1ToSpatialAtPick.length();
		
		float newDistFromCursor1ToSpatial = cursor1ToSpatialAtPick.length();

		float dx = newDistFromCursor1ToSpatial * FastMath.cos(newCursor1ToSpatialAngle);
		float dy = newDistFromCursor1ToSpatial * FastMath.sin(newCursor1ToSpatialAngle);

		Vector2f newScreenPosition = screenPosCursor1.add(new Vector2f(dx, -dy));

		Vector3f newPosition = JMEGfxUtils.getCursorWorldCoordinatesOnSpatialPlane(newScreenPosition, targetSpatial);
		if(newPosition != null) {
										
		//	targetSpatial.getParent().worldToLocal(newPosition, targetSpatial.getLocalTranslation());
			
		//	worldLocations.add(new WorldCursorRecord(targetSpatial.getWorldTranslation().clone(),
		//			new Quaternion(targetSpatial.getWorldRotation().clone()), targetSpatial.getWorldScale().clone(), System.nanoTime()));
			
			for (ObjectRotateTranslateScaleListener l: listeners)
				l.itemMoved(targetSpatial, newPosition.x, newPosition.y, spatialScreenAtPick.x, spatialScreenAtPick.y);
			
			
			targetSpatial.setLocalRotation(getCurrentTargetSpatialRotationFromCursorChange());
					
			//if((getScaleAtOrigin().mult(cursorScale).x < scaleMax) && (getScaleAtOrigin().mult(cursorScale).x > scaleMin)) {
				//targetSpatial.setLocalScale(getScaleAtOrigin().mult(cursorScale));
				//targetSpatial.setLocalTranslation(targetSpatial.getLocalTranslation().x, targetSpatial.getLocalTranslation().y, targetSpatial.getLocalTranslation().z+(cursorScale-1)/10);
			//}

			targetSpatial.updateGeometricState(0f, true);
			
			if (remoteObject!=null){
				remoteObject.setLocalRotation(targetSpatial.getLocalRotation());
				remoteObject.updateGeometricState(0f, true);
			}	
			
			Quaternion currentAngle = this.targetSpatial.getLocalRotation();
			float angleToX = 0;
			float angleToY = 0;
			float angleToZ = 0;
			angleToX = currentAngle.toAngleAxis(Vector3f.UNIT_X);
			angleToY = currentAngle.toAngleAxis(Vector3f.UNIT_Y);
			angleToZ = currentAngle.toAngleAxis(Vector3f.UNIT_Z);
				
			if ((Math.abs(angleToX)<=threshold || FastMath.TWO_PI-angleToX<=threshold) && (Math.abs(angleToY)<=threshold || FastMath.TWO_PI-angleToY<=threshold) && (Math.abs(angleToZ)<=threshold || FastMath.TWO_PI-angleToZ<=threshold) ){
					
				if (hasTaskCompletionMessageSent) return;
				hasTaskCompletionMessageSent=true;
				for (ExperimentEventListener l: experimentEventlisteners){
					l.taskCompleted(touchNumber);
				}
			}
		}
	
	}
	
	protected void updateCursor1() {

		cursor1Pos.x = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().x;
		cursor1Pos.y = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().y;


		cursor1OldPos.x = getScreenCursorByIndex(0).getOldCursorScreenPosition().x;
		cursor1OldPos.y = getScreenCursorByIndex(0).getOldCursorScreenPosition().y;
	}

	protected void updateCursor2() {
		cursor2Pos.x = getScreenCursorByIndex(1).getCurrentCursorScreenPosition().x;
		cursor2Pos.y = getScreenCursorByIndex(1).getCurrentCursorScreenPosition().y;



		cursor2OldPos.x = getScreenCursorByIndex(1).getOldCursorScreenPosition().x;
		cursor2OldPos.y = getScreenCursorByIndex(1).getOldCursorScreenPosition().y;
	}
	
	protected void setOldCursor(){
		for (ScreenCursor c:screenCursors){
			ScreenCursorRecord s = new ScreenCursorRecord(c.getCurrentCursorScreenPosition().x, c.getCurrentCursorScreenPosition().y );
			c.setOldCursorScreenPosition(s);
		}
	}

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
	
	public void addRotateTranslateScaleListener(ObjectRotateTranslateScaleListener l){
		listeners.add(l);
	}

	public void removeRotateTranslateScaleListener(ObjectRotateTranslateScaleListener l){
		if (listeners.contains(l))
			listeners.remove(l);
	}
	
	public interface ObjectRotateTranslateScaleListener {
		public void itemMoved(Spatial targetSpatial,  float newLocationX, float newLocationY, float oldLocationX, float oldLocationY);
	}
	
	public void addExperimentEventListener(ExperimentEventListener l){
		experimentEventlisteners.add(l);
	}

	public void removeExperimentEventListener(ExperimentEventListener l){
		if (experimentEventlisteners.contains(l))
			experimentEventlisteners.remove(l);
	}

	public interface ExperimentEventListener {
		public void taskCompleted(int touchNumber);
	}

}
