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

package synergyspace.jme.cursorsystem.flicksystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.system.DisplaySystem;

import synergyspace.jme.Updateable;
import synergyspace.jme.cursorsystem.MultiTouchElement;
import synergyspace.jme.cursorsystem.MultiTouchElementRegistry;
import synergyspace.jme.sysutils.BorderUtility;
import synergyspace.jme.sysutils.SpatialUtility;

public class FlickSystem implements Updateable {
	private static FlickSystem instance = new FlickSystem();
	private static ArrayList<Spatial> bouncers = new ArrayList<Spatial>();
	protected static Queue<FlickMover> movableElements = new ConcurrentLinkedQueue<FlickMover>();
	private static ArrayList<ArrayList<int[]>> wallCoords = new ArrayList<ArrayList<int[]>>();

	public static FlickSystem getInstance() {
		return instance;
	}

	private FlickSystem() {}

	public void makeFlickable(Spatial s, MultiTouchElement movingElement, float deceleration) {
		this.makeFlickable(s, s, movingElement, deceleration);
	}

	public void makeFlickable(Spatial pickingSpatial, Spatial targetSpatial, MultiTouchElement movingElement, float deceleration) {
		FlickMover fm = new FlickMover(pickingSpatial, targetSpatial, movingElement, deceleration);
		movableElements.add(fm);
	}

	public void update(float timePerFrame) {
		for(FlickMover fm : movableElements) {
			fm.update(timePerFrame);
		}
	}

	public void enableScreenBoundary(Node rootNode) {
		defaultBorders(rootNode);
	}

	public void enableBounce(Spatial s) {
		for(FlickMover fm : movableElements){
			if(fm.getTargetSpatial().getName().equals(s.getName()))	{
				fm.enableBoundary();
			}
		}
	}

	public void disableBounce(Spatial s) {
		for(FlickMover fm : movableElements){
			if(fm.getTargetSpatial().getName().equals(s.getName()))	{
				fm.disableBoundary();
			}
		}
	}

	public boolean isBoundaryEnabled(Spatial s) {
		for(FlickMover fm : movableElements){
			if(fm.getTargetSpatial().getName().equals(s.getName()))	{
				return fm.getBoundaryEnabled();
			}
		}
		return false;
	}

	public void makeUnflickable(Spatial s){
		for(Iterator<FlickMover> iter = movableElements.iterator(); iter.hasNext();){
			FlickMover flickMover = iter.next();

			if(flickMover.getTargetSpatial().getName().equals(s.getName())){
				iter.remove();
				if(MultiTouchElementRegistry.getInstance().isRegistered(flickMover)) {
					MultiTouchElementRegistry.getInstance().unregister(flickMover);
				}
			}
		}
	}

	public boolean isFlickable(Spatial s){
		for(FlickMover fm: movableElements){
			if(fm.getTargetSpatial().getName().equals(s.getName()))
				return true;
		}
		return false;
	}


	public void flick(Spatial s, Vector3f linearVelocity, float deceleration){
		for(FlickMover fm : movableElements){
			if(fm.getTargetSpatial().getName().equals(s.getName()))	{
				fm.setAutoFlicked(true);
				fm.setDeceleration(deceleration);
				fm.setLinearVelocity(linearVelocity);
			}
		}


	}

	public FlickMover getMovingElement(Spatial s){
		for(FlickMover fm : movableElements){
			if(fm.getTargetSpatial().getName().equals(s.getName()))	{
				return fm;
			}
		}
		return null;
	}

	public Queue<FlickMover> getMovingElements(){
		return movableElements;
	}

	public void addBouncer(Spatial s){
		
		wallCoords.add(SpatialUtility.createWallCoords(s, SpatialUtility.findEdges(s, false)));
		bouncers.add(s);
	}

	public void removeBouncer(Spatial s){		
		wallCoords.remove(bouncers.indexOf(s));
		bouncers.remove(s);
	}

	public ArrayList<Spatial> getBouncers(){
		return bouncers;
	}

	public void enableSticky(Spatial s){
		for(Iterator<FlickMover> iter = movableElements.iterator(); iter.hasNext();){
			FlickMover flickMover = iter.next();
			if(flickMover.getTargetSpatial().getName().equals(s.getName())){
				flickMover.enableSticky();
			}
		}
	}

	public void disableSticky(Spatial s){
		for(Iterator<FlickMover> iter = movableElements.iterator(); iter.hasNext();){
			FlickMover flickMover = iter.next();
			if(flickMover.getTargetSpatial().getName().equals(s.getName())){
				flickMover.disableSticky();
			}
		}
	}
	
	public void makeReflective(Spatial s){
		for(Iterator<FlickMover> iter = movableElements.iterator(); iter.hasNext();){
			FlickMover flickMover = iter.next();
			if(flickMover.getTargetSpatial().getName().equals(s.getName())){
				flickMover.makeReflective();
			}
		}
	}

	public void makeUnreflective(Spatial s){
		for(Iterator<FlickMover> iter = movableElements.iterator(); iter.hasNext();){
			FlickMover flickMover = iter.next();
			if(flickMover.getTargetSpatial().getName().equals(s.getName())){
				flickMover.makeUnreflective();
			}
		}
	}
	
	public static FlickMover getFlickMover(Spatial s){
		for(Iterator<FlickMover> iter = movableElements.iterator(); iter.hasNext();){
			FlickMover flickMover = iter.next();
			if(flickMover.getTargetSpatial().getName().equals(s.getName())){
				return flickMover;
			}
		}
		return null;
	}
	
	private void defaultBorders(Node rootNode) {

		//Kept here for older application in src_functionaltests

		int boxSize = 10;

		Box defaultOrthoLeft = new Box("borderLeft",
				new Vector3f(- boxSize, 0, 0), boxSize,
				DisplaySystem.getDisplaySystem().getHeight(), boxSize);
		defaultOrthoLeft.setModelBound(new OrthogonalBoundingBox());
		defaultOrthoLeft.setDefaultColor(ColorRGBA.black);
		defaultOrthoLeft.updateModelBound();
		rootNode.attachChild(defaultOrthoLeft);
		bouncers.add(defaultOrthoLeft);

		Box defaultOrthoRight = new Box("borderRight",
				new Vector3f(DisplaySystem.getDisplaySystem().getWidth() + boxSize, 0, 0), boxSize,
				DisplaySystem.getDisplaySystem().getHeight(), boxSize);
		defaultOrthoRight.setDefaultColor(ColorRGBA.black);
		defaultOrthoRight.setModelBound(new OrthogonalBoundingBox());
		defaultOrthoRight.updateModelBound();
		rootNode.attachChild(defaultOrthoRight);
		bouncers.add(defaultOrthoRight);

		Box defaultOrthoTop = new Box("borderTop",
				new Vector3f(0, DisplaySystem.getDisplaySystem().getHeight() + boxSize, 0),
				DisplaySystem.getDisplaySystem().getWidth(), boxSize, boxSize);
		defaultOrthoTop.setDefaultColor(ColorRGBA.black);
		defaultOrthoTop.setModelBound(new OrthogonalBoundingBox());
		defaultOrthoTop.updateModelBound();
		rootNode.attachChild(defaultOrthoTop);
		bouncers.add(defaultOrthoTop);

		Box defaultOrthoBottom = new Box("borderBottom",
				new Vector3f(0, - boxSize, 0),
				DisplaySystem.getDisplaySystem().getWidth(), boxSize, boxSize);
		defaultOrthoBottom.setDefaultColor(ColorRGBA.black);
		defaultOrthoBottom.setModelBound(new OrthogonalBoundingBox());
		defaultOrthoBottom.updateModelBound();
		rootNode.attachChild(defaultOrthoBottom);
		bouncers.add(defaultOrthoBottom);

	}
	
	public static void clearbouncers() {
		wallCoords.clear();
		bouncers.clear();
		SpatialUtility.clearCollections();
	}
	
	public static void activateBorder(){
		bouncers.add(BorderUtility.getBorderSpatial());
		wallCoords.add(null);
	}

	public static ArrayList<ArrayList<int[]>> getWallCoords() {
		return wallCoords;
	}



}
