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

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;


import synergyspace.jme.Updateable;
import synergyspace.jme.cursorsystem.MultiTouchElement;
import synergyspace.jme.mmt.TransferController;

//TODO: merge the two FlickSystem implementations

public class FlickSystem implements Updateable {	
	private static FlickSystem instance = new FlickSystem();

	protected Queue<FlickMover> movableElements = new ConcurrentLinkedQueue<FlickMover>();
	protected boolean useBoundary = false;
	protected Boundary boundary;

	protected boolean isTransferEnabled = false;
	
	protected TransferController transferController;
	
	public static FlickSystem getInstance() {
		return instance;
	}
	
	private FlickSystem() {}

	public void makeFlickable(Spatial s, MultiTouchElement movingElement, float deceleration) {
		FlickMover fm = new FlickMover(s, movingElement, deceleration);		
		movableElements.add(fm);
	}
	
	public void update(float timePerFrame) {
		for(FlickMover fm : movableElements) {
			fm.update(timePerFrame);
		}		
	}
	
	public void enableScreenBoundary(Node rootNode) {
		boundary = new Boundary(rootNode);
		this.useBoundary = true;
	}
	
	public void disableScreenBoundary() {
		this.useBoundary = false;
	}
	
	public void enableTransferObjects(TransferController transferController)
	{
		this.transferController = transferController;
		this.isTransferEnabled = true;
	}
		
	public boolean isBoundaryEnabled() {
		return useBoundary;
	}
	
	public Boundary getBoundary() {
		return boundary;
	}
	
	public void makeUnflickable(MultiTouchElement movingElement)
	{
		for(Iterator<FlickMover> iter = movableElements.iterator(); iter.hasNext();)
		{
			FlickMover flickMover = iter.next();
			
			if(flickMover.getMovingElement().equals(movingElement))
			{
				flickMover.switchFlickOff();
				iter.remove();
			}
		}
	}
	
	public void flick(MultiTouchElement movingElement, Vector3f linearVelocity, float deceleration)
	{
		for(FlickMover fm : movableElements)
		{
			if(fm.getMovingElement().equals(movingElement))
			{
				fm.setDeceleration(deceleration);
				fm.setLinearVelocity(linearVelocity);
			}
		}		
	}

}
