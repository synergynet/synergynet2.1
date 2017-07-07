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

package synergyspace.jme.mmt.flicksystem;

import java.util.List;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import synergyspace.jme.cursorsystem.MultiTouchElement;
import synergyspace.jme.cursorsystem.ThreeDMultiTouchElement;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.jme.cursorsystem.cursordata.WorldCursorRecord;
import synergyspace.mtinput.events.MultiTouchCursorEvent;


public class FlickMover extends ThreeDMultiTouchElement {
	
	private boolean flickEnabled = false;

	private boolean transferEnabled = false;

	// units per second
	private Vector3f linearVelocity;

	// units per second per second
	private float deceleration = 1f;

	private MultiTouchElement movingElement;

	private FlickSystem flickSystem;

	public FlickMover(Spatial pickSpatial, MultiTouchElement movingElement, float deceleration/*, boolean isTransferable*/) {
		super(pickSpatial);
		this.movingElement = movingElement;
		this.deceleration = deceleration;
		flickSystem = FlickSystem.getInstance();
	}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		switchFlickOff();
	}


	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		if(screenCursors.size() == 1) {
			setLinearVelocity();
			switchFlickOn();
		}

	}

	private void switchFlickOn() {
		flickEnabled = true;
		transferEnabled = true;
	}

	protected void switchFlickOff() {
		flickEnabled = false;
		transferEnabled = false; 
	}
	
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {		

	}

	public void update(float tpf) {
		if(flickEnabled) {
			moveToNewPosition(tpf);
			applyFriction(tpf);
			if(flickSystem.isTransferEnabled && transferEnabled){
				flickSystem.transferController.applyTransfer(movingElement,linearVelocity, deceleration);
			}
			else if(FlickSystem.getInstance().isBoundaryEnabled()) {
				bounce();
			}

		}
	}

	
	private void setLinearVelocity() {
		if(movingElement.getWorldLocations().size() < 4) return;
		
		List<WorldCursorRecord> positions = movingElement.getWorldLocations();
		int lastIndex = positions.size() - 1;
		int nextLastIndex = lastIndex - 2;
		Vector3f lastPosition = positions.get(lastIndex).worldLocation;
		long lastTime = positions.get(lastIndex).time;
		Vector3f nextLastPosition = positions.get(nextLastIndex).worldLocation;
		long nextLastTime = positions.get(nextLastIndex).time;	
		float diffTimeMS = (lastTime - nextLastTime) / 1000000f;
		linearVelocity = lastPosition.subtract(nextLastPosition).mult(1f/diffTimeMS * 1000f);
	}
	
	private void applyFriction(float tpf) {
		if(linearVelocity == null) return;
		linearVelocity.subtractLocal(linearVelocity.mult(tpf * deceleration));
		if(linearVelocity.length() < 0.2f) flickEnabled = false;		
	}

	private void moveToNewPosition(float tpf) {		
		if(linearVelocity == null) return;
		Vector3f pos = targetSpatial.getLocalTranslation().clone();
		pos.addLocal(linearVelocity.mult(tpf));
		targetSpatial.setLocalTranslation(pos);
		targetSpatial.updateWorldVectors();
	}

	private void bounce() {
		if (movingElement.getTargetSpatial().hasCollision(flickSystem.getBoundary().getLeft(), true) && linearVelocity.x < 0){
			linearVelocity.setX(-linearVelocity.x);
		}
		if (movingElement.getTargetSpatial().hasCollision(flickSystem.getBoundary().getRight(), true) && linearVelocity.x > 0){
			linearVelocity.setX(-linearVelocity.x);
		}
		if (movingElement.getTargetSpatial().hasCollision(flickSystem.getBoundary().getTop(), true) && linearVelocity.y > 0){
			linearVelocity.setY(-linearVelocity.y);
		}
		if (movingElement.getTargetSpatial().hasCollision(flickSystem.getBoundary().getBottom(), true) && linearVelocity.y < 0){
			linearVelocity.setY(-linearVelocity.y);
		}		
	}
	
	public void setDeceleration(float deceleration)	{
		this.deceleration = deceleration;
	}
	
	public void setLinearVelocity(Vector3f linearVelocity)	{
		this.linearVelocity = linearVelocity;
		flickEnabled = true;
	}
	
	public float getDeceleration()	{
		return deceleration;
	}
	
	public Vector3f getLinearVelocity()	{
		return linearVelocity;
	}
	
	public MultiTouchElement getMovingElement()	{
		return movingElement;
	}
}

