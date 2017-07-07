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

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.jme.scene.Spatial;

import synergyspace.jme.Updateable;
import synergyspace.jme.cursorsystem.MultiTouchElement;
import synergyspace.jme.cursorsystem.MultiTouchElementRegistry;

public class SpinSystem implements Updateable {
	private static SpinSystem instance = new SpinSystem();

	protected Queue<SpinMover> movableElements = new ConcurrentLinkedQueue<SpinMover>();
	protected boolean useBoundary = false;

	public static SpinSystem getInstance() {
		return instance;
	}

	private SpinSystem() {}

	public void makeSpinnable(Spatial s, MultiTouchElement movingElement, float deceleration) {
		this.makeSpinnable(s, s, movingElement, deceleration);
	}

	public void makeSpinnable(Spatial pickingSpatial, Spatial targetSpatial, MultiTouchElement movingElement, float deceleration) {
		SpinMover fm = new SpinMover(pickingSpatial, targetSpatial, movingElement, deceleration);
		movableElements.add(fm);
	}

	public void update(float timePerFrame) {
		for(SpinMover fm : movableElements) {
			fm.update(timePerFrame);
		}
	}

	public void makeUnspinnable(Spatial s){
		for(Iterator<SpinMover> iter = movableElements.iterator(); iter.hasNext();){
			SpinMover flickMover = iter.next();

			if(flickMover.getTargetSpatial().getName().equals(s.getName())){
				iter.remove();
				if(MultiTouchElementRegistry.getInstance().isRegistered(flickMover)) {
					MultiTouchElementRegistry.getInstance().unregister(flickMover);
				}
			}
		}
	}

	public boolean isSpinnable(Spatial s){
		for(SpinMover fm: movableElements){
			if(fm.getTargetSpatial().getName().equals(s.getName()))
				return true;
		}
		return false;
	}

	public void spin(Spatial s, float linearVelocity, float deceleration){
		for(SpinMover fm : movableElements){
			if(fm.getTargetSpatial().getName().equals(s.getName()))	{
				fm.setDeceleration(deceleration);
				fm.setSpinVelocity(linearVelocity);
			}
		}

	}

	public SpinMover getMovingElement(Spatial s){
		for(SpinMover fm : movableElements){
			if(fm.getTargetSpatial().getName().equals(s.getName()))	{
				return fm;
			}
		}
		return null;
	}

	public Queue<SpinMover> getMovingElements(){
		return movableElements;
	}

}
