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

package synergyspace.jme.cursorsystem.scalesystem;

import java.util.ArrayList;
import java.util.List;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import synergyspace.jme.cursorsystem.MultiTouchElement;
import synergyspace.jme.cursorsystem.ThreeDMultiTouchElement;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.jme.cursorsystem.cursordata.WorldCursorRecord;
import synergyspace.jme.cursorsystem.flicksystem.FlickSystem;
import synergyspace.mtinput.events.MultiTouchCursorEvent;


public class ScaleMover extends ThreeDMultiTouchElement {

	private boolean scaleMotionEnabled = false;

	private boolean elementReleased  = false;

	// units per second
	private Vector3f scaleMotionVelocity = Vector3f.ZERO;

	// units per second per second
	private float deceleration = 1f;

	private MultiTouchElement movingElement;

	private float minScale = 0.5f;
	private float maxScale= 2.0f;

	protected List<ScaleMotionListener> listeners = new ArrayList<ScaleMotionListener>();

	public ScaleMover(Spatial pickSpatial, MultiTouchElement movingElement, float deceleration/*, boolean isTransferable*/) {

		this(pickSpatial, pickSpatial, movingElement, deceleration/*, boolean isTransferable*/);

	}

	public ScaleMover(Spatial pickSpatial, Spatial targetSpatial, MultiTouchElement movingElement, float deceleration) {
		super(pickSpatial, targetSpatial);
		this.movingElement = movingElement;
		this.deceleration = deceleration;
	}

	public ScaleMover(Spatial pickSpatial, Spatial targetSpatial, MultiTouchElement movingElement, float deceleration, float minScale, float maxScale) {
		super(pickSpatial, targetSpatial);
		this.movingElement = movingElement;
		this.deceleration = deceleration;
		this.minScale = minScale;
		this.maxScale = maxScale;
	}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		switchFlickOff();
		elementReleased = false;
	}


	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		if(screenCursors.size() > 1) {
			setScaleMotionVelocity();
			switchFlickOn();
			elementReleased = true;
		}

	}

	private void switchFlickOn() {
		scaleMotionEnabled = true;
	}

	private void switchFlickOff() {
		scaleMotionEnabled = false;
	}

	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {

	}

	public void update(float tpf) {
		if(scaleMotionEnabled) {
			moveToNewPosition(tpf);
			applyFriction(tpf);
			bounce(tpf);
		}
	}

	public boolean elementReleased(){
		return elementReleased;
	}

	private void setScaleMotionVelocity() {
		if(movingElement.getWorldLocations().size() < 4) return;

		List<WorldCursorRecord> positions = movingElement.getWorldLocations();
		int lastIndex = positions.size() - 1;
		int nextLastIndex = lastIndex - 2;
		Vector3f lastPosition = positions.get(lastIndex).worldScale;
		long lastTime = positions.get(lastIndex).time;
		Vector3f nextLastPosition = positions.get(nextLastIndex).worldScale;
		long nextLastTime = positions.get(nextLastIndex).time;
		float diffTimeMS = (lastTime - nextLastTime) / 1000000f;
		scaleMotionVelocity = lastPosition.subtract(nextLastPosition).mult(1f/diffTimeMS * 1000f);
	}

	private void applyFriction(float tpf) {
		if(scaleMotionVelocity == null) return;
		scaleMotionVelocity.subtractLocal(scaleMotionVelocity.mult(tpf * deceleration));
		if(scaleMotionVelocity.length() < 0.2f) scaleMotionEnabled = false;
	}

	private void moveToNewPosition(float tpf) {
		if(scaleMotionVelocity == null) return;
		Vector3f pos = targetSpatial.getLocalScale().clone();
		pos.addLocal(scaleMotionVelocity.mult(tpf));
		float oldScale = targetSpatial.getLocalScale().x;
		targetSpatial.setLocalScale(pos);
		targetSpatial.updateWorldVectors();
		float newScale = targetSpatial.getLocalScale().x;
		for (ScaleMotionListener l: listeners)
			l.itemScaleMotioned(this, targetSpatial, newScale, oldScale);
	}

	public void bounce(float tpf) {
		
		if(FlickSystem.getFlickMover(targetSpatial).isSticky()){
			for (int i = 0; i < FlickSystem.getInstance().getBouncers().size(); i++){
				if (targetSpatial.hasCollision(FlickSystem.getInstance().getBouncers().get(i), true)){
					scaleMotionVelocity = new Vector3f(0, 0, 0);
				}
			}
		}

		Vector3f pos = targetSpatial.getLocalScale().clone();
		pos.addLocal(scaleMotionVelocity.mult(tpf));

		if ( pos.x < minScale || pos.x > maxScale){
			if(ScaleMotionSystem.getInstance().isBounceEnabled()){
				scaleMotionVelocity = new Vector3f(-scaleMotionVelocity.x, -scaleMotionVelocity.y, -scaleMotionVelocity.z);
			}else{
				scaleMotionVelocity = new Vector3f(0, 0, 0);
			}
		}
	}

	public void setDeceleration(float deceleration)	{
		this.deceleration = deceleration;
	}

	public void setLinearVelocity(Vector3f linearVelocity)	{
		this.scaleMotionVelocity = linearVelocity;
		scaleMotionEnabled = true;
	}

	public float getDeceleration()	{
		return deceleration;
	}

	public Vector3f getScaleMotionVelocity()	{
		return scaleMotionVelocity;
	}

	public MultiTouchElement getMovingElement()	{
		return movingElement;
	}

	public void addScaleMotionListener(ScaleMotionListener l){
		listeners.add(l);
	}

	public void removeScaleMotionListener(ScaleMotionListener l){
		if (listeners.contains(l))
			listeners.remove(l);
	}

	public interface ScaleMotionListener{
		public void itemScaleMotioned(ScaleMover multiTouchElement, Spatial targetSpatial,  float newScale, float oldScale);
	}

	public void setScaleMotionLimits(float minScale, float maxScale) {
		this.minScale = minScale;
		this.maxScale = maxScale;

	}
}

