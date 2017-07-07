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

package synergyspace.jme.cursorsystem.spinsystem;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

import synergyspace.jme.cursorsystem.MultiTouchElement;
import synergyspace.jme.cursorsystem.ThreeDMultiTouchElement;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.jme.cursorsystem.cursordata.WorldCursorRecord;
import synergyspace.jme.cursorsystem.flicksystem.FlickSystem;
import synergyspace.jme.sysutils.BorderUtility;
import synergyspace.mtinput.events.MultiTouchCursorEvent;


public class SpinMover extends ThreeDMultiTouchElement {

	public static float stopThreshold = 0.05f;

	private boolean spinEnabled = false;

	private boolean elementReleased  = false;

	// units per second
	private float rotationVelocityX = 0;
	private float rotationVelocityY = 0;
	private float rotationVelocityZ = 0;

	// units per second per second
	private float deceleration = 1f;

	private MultiTouchElement movingElement;

	protected List<SpinListener> listeners = new ArrayList<SpinListener>();

	public SpinMover(Spatial pickSpatial, MultiTouchElement movingElement, float deceleration/*, boolean isTransferable*/) {

		this(pickSpatial, pickSpatial, movingElement, deceleration/*, boolean isTransferable*/);

	}

	public SpinMover(Spatial pickSpatial, Spatial targetSpatial, MultiTouchElement movingElement, float deceleration/*, boolean isTransferable*/) {
		super(pickSpatial, targetSpatial);
		this.movingElement = movingElement;
		this.deceleration = deceleration;
	}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		switchSpinOff();
		movingElement.getWorldLocations().clear();
		elementReleased = false;
	}


	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		if(screenCursors.size() > 1) {
			setLinearVelocity();
			switchSpinOn();
			elementReleased = true;
		}

	}

	private void switchSpinOn() {
		spinEnabled = true;
	}

	private void switchSpinOff() {
		spinEnabled = false;
	}

	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {setLinearVelocity();}

	public void update(float tpf) {
		if(spinEnabled) {
			moveToNewPosition(tpf);
			applyFriction(tpf);
			if(BorderUtility.isBoundaryEnabled())
				bounce();
		}
	}

	public void bounce() {
		if(movingElement != null){
			for (int i = 0; i < FlickSystem.getInstance().getBouncers().size(); i++){
				if (targetSpatial.hasCollision(FlickSystem.getInstance().getBouncers().get(i), true)){
					
					if(FlickSystem.getFlickMover(targetSpatial).isSticky()){
						rotationVelocityX =0;
						rotationVelocityY =0;
						rotationVelocityZ =0;
					}else{
						rotationVelocityX =-rotationVelocityX;
						rotationVelocityY =-rotationVelocityY;
						rotationVelocityZ =-rotationVelocityZ;
					}
				}
			}
		}
	}

	public boolean elementReleased(){
		return elementReleased;
	}

	private void setLinearVelocity() {
		if(movingElement.getWorldLocations().size() < 4) return;

		List<WorldCursorRecord> rotations = movingElement.getWorldLocations();
		int lastIndex = rotations.size() - 1;
		int nextLastIndex = lastIndex - 2;

		float xDiff = 0f;
		float yDiff = 0f;
		float zDiff = 0f;

		Quaternion oldAngle = rotations.get(nextLastIndex).worldRotation;
		Quaternion newAngle = rotations.get(lastIndex).worldRotation;

		Quaternion oldAngleInv = oldAngle.inverse();

		Quaternion diffAngle = newAngle.mult(oldAngleInv);

		xDiff = diffAngle.x;
		yDiff = diffAngle.y;
		zDiff = diffAngle.z;

		long lastTime = rotations.get(lastIndex).time;
		long nextLastTime = rotations.get(nextLastIndex).time;
		float diffTimeMS = (lastTime - nextLastTime) / 1000000f;
		rotationVelocityX = xDiff * (1f/diffTimeMS * 1000f);
		rotationVelocityY = yDiff * (1f/diffTimeMS * 1000f);
		rotationVelocityZ = zDiff * (1f/diffTimeMS * 1000f);
	}

	private void applyFriction(float tpf) {
		if(rotationVelocityX != 0)
			rotationVelocityX -= (rotationVelocityX * (tpf * deceleration));
		if(rotationVelocityY != 0)
			rotationVelocityY -= (rotationVelocityY * (tpf * deceleration));
		if(rotationVelocityZ != 0)
			rotationVelocityZ -= (rotationVelocityZ * (tpf * deceleration));
		if (FastMath.abs(rotationVelocityX) < stopThreshold && FastMath.abs(rotationVelocityY) < stopThreshold && FastMath.abs(rotationVelocityZ) < stopThreshold)
			spinEnabled = false;
	}

	private void moveToNewPosition(float tpf) {
		if(rotationVelocityX == 0 && rotationVelocityY == 0 && rotationVelocityZ == 0) return;
		float xDiff = rotationVelocityX * tpf;
		float yDiff = rotationVelocityY * tpf;
		float zDiff = rotationVelocityZ * tpf;
		float[] oldXYZ = {pickingSpatial.getLocalRotation().x,pickingSpatial.getLocalRotation().y,pickingSpatial.getLocalRotation().z};

		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(xDiff, new Vector3f(1,0,0).normalize());
		tq.fromAngleAxis(yDiff, new Vector3f(0,1,0).normalize());
		tq.fromAngleAxis(zDiff, new Vector3f(0,0,1).normalize());
		tq.multLocal(targetSpatial.getLocalRotation());
		float[] axisR = new float[3];
		tq.toAngles(axisR);
		targetSpatial.setLocalRotation(tq);

		float[] newXYZ = {pickingSpatial.getLocalRotation().x,pickingSpatial.getLocalRotation().y,pickingSpatial.getLocalRotation().z};

		for (SpinListener l: listeners)
			l.itemSpun(this, pickingSpatial, newXYZ, oldXYZ);

	}


	public void setDeceleration(float deceleration)	{
		this.deceleration = deceleration;
	}

	public void setSpinVelocity(float linearVelocity)	{
		this.rotationVelocityX = linearVelocity;
		spinEnabled = true;
	}

	public float getDeceleration()	{
		return deceleration;
	}

	public float getSpinVelocity()	{
		return rotationVelocityX;
	}

	public MultiTouchElement getMovingElement()	{
		return movingElement;
	}

	public void addSpinListener(SpinListener l){
		listeners.add(l);
	}

	public void removeSpinListener(SpinListener l){
		if (listeners.contains(l))
			listeners.remove(l);
	}

	public interface SpinListener{
		public void itemSpun(SpinMover multiTouchElement, Spatial targetSpatial,  float[] newRotationZ, float[] oldRotationZ);
	}
}

