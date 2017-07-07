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

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

import synergyspace.jme.Updateable;
import synergyspace.jme.cursorsystem.MultiTouchElement;
import synergyspace.jme.cursorsystem.MultiTouchElementRegistry;

public class ScaleMotionSystem implements Updateable {
	private static ScaleMotionSystem instance = new ScaleMotionSystem();

	protected Queue<ScaleMover> movableElements = new ConcurrentLinkedQueue<ScaleMover>();
	protected boolean useBounceEffect = false;

	public static ScaleMotionSystem getInstance() {
		return instance;
	}

	private ScaleMotionSystem() {}

	public void makeScaleMotionEnabled(Spatial s, MultiTouchElement movingElement, float deceleration) {
		this.makeScaleMotionEnabled(s, s, movingElement, deceleration);
	}

	public void makeScaleMotionEnabled(Spatial pickingSpatial, Spatial targetSpatial, MultiTouchElement movingElement, float deceleration) {
		ScaleMover fm = new ScaleMover(pickingSpatial, targetSpatial, movingElement, deceleration);
		movableElements.add(fm);
	}

	public void makeScaleMotionEnabled(Spatial pickingSpatial, Spatial targetSpatial, MultiTouchElement movingElement, float deceleration, float minScale, float maxScale) {
		ScaleMover fm = new ScaleMover(pickingSpatial, targetSpatial, movingElement, deceleration, minScale, maxScale);
		movableElements.add(fm);
	}


	public void update(float timePerFrame) {
		for(ScaleMover fm : movableElements) {
			fm.update(timePerFrame);
		}
	}

	public void enableBounceEffect() {
		this.useBounceEffect = true;
	}

	public void disableBounceEffect() {
		this.useBounceEffect = false;
	}

	public boolean isBounceEnabled() {
		return useBounceEffect;
	}

	public void disbaleScaleMotion(Spatial s){
		for(Iterator<ScaleMover> iter = movableElements.iterator(); iter.hasNext();){
			ScaleMover flickMover = iter.next();

			if(flickMover.getTargetSpatial().getName().equals(s.getName())){
				iter.remove();
				if(MultiTouchElementRegistry.getInstance().isRegistered(flickMover)) {
					MultiTouchElementRegistry.getInstance().unregister(flickMover);
				}
			}
		}
	}

	public boolean isScaleMotionEnabled(Spatial s){
		for(ScaleMover fm: movableElements){
			if(fm.getTargetSpatial().getName().equals(s.getName()))
				return true;
		}
		return false;
	}

	public void scaleMotion(Spatial s, Vector3f linearVelocity, float deceleration){
		for(ScaleMover fm : movableElements){
			if(fm.getTargetSpatial().getName().equals(s.getName()))	{
				fm.setDeceleration(deceleration);
				fm.setLinearVelocity(linearVelocity);
			}
		}

	}

	public ScaleMover getMovingElement(Spatial s){
		for(ScaleMover fm : movableElements){
			if(fm.getTargetSpatial().getName().equals(s.getName()))	{
				return fm;
			}
		}
		return null;
	}

	public void setScaleMotionLimits(float minScale, float maxScale){
		for(ScaleMover fm : movableElements){
			fm.setScaleMotionLimits(minScale, maxScale);
		}
	}

	public Queue<ScaleMover> getMovingElements(){
		return movableElements;
	}

}
