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

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jme.intersection.CollisionData;
import com.jme.intersection.CollisionResults;
import com.jme.intersection.TriangleCollisionResults;
import com.jme.math.FastMath;
import com.jme.math.Ray;
import com.jme.math.Triangle;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.system.DisplaySystem;

import synergyspace.jme.cursorsystem.MultiTouchElement;
import synergyspace.jme.cursorsystem.ThreeDMultiTouchElement;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.jme.cursorsystem.cursordata.WorldCursorRecord;
import synergyspace.jme.sysutils.BorderUtility;
import synergyspace.jme.sysutils.SpatialUtility;
import synergyspace.mtinput.events.MultiTouchCursorEvent;


public class FlickMover extends ThreeDMultiTouchElement {

	public static float lowerThreshold = 0.2f;
	public static float speedLimit = 2000f;
	
	public static int coordLimit = 100;

	private boolean flickEnabled = false;
	
	private boolean autoFlicked = false;

	private boolean elementReleased  = false;

	// units per second
	private Vector3f linearVelocity = Vector3f.ZERO;

	// units per second per second
	private float deceleration = 1f;

	private MultiTouchElement movingElement;

	private FlickSystem flickSystem;

	protected List<FlickListener> listeners = new ArrayList<FlickListener>();
	
	private ArrayList<Integer> coordsIndex = new ArrayList<Integer>();

	private boolean reflective = false;
	private boolean bounceOverride = true;
	private boolean sticky = false;
	private boolean justTheMiddle = false;

	public FlickMover(Spatial pickSpatial, MultiTouchElement movingElement, float deceleration/*, boolean isTransferable*/) {

		this(pickSpatial, pickSpatial, movingElement, deceleration/*, boolean isTransferable*/);

	}

	public FlickMover(Spatial pickSpatial, Spatial targetSpatial, MultiTouchElement movingElement, float deceleration/*, boolean isTransferable*/) {
		super(pickSpatial, targetSpatial);
		this.movingElement = movingElement;
		this.deceleration = deceleration;
		flickSystem = FlickSystem.getInstance();
		
		try{
			coordsIndex = SpatialUtility.getIndexOfRelevantCoords(targetSpatial);
		}catch(Exception e){
			justTheMiddle = true;
		}
		
	}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		switchFlickOff();
		elementReleased = false;
		toBeTransferred = false;
		if (toIgnore)noLongerIgnoring();
		linearVelocity = new Vector3f(0,0,0);
	}


	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		if(screenCursors.size() > 1) return;
		for (int i = 0; i < flickSystem.getBouncers().size(); i++){
			if (targetSpatial.hasCollision(flickSystem.getBouncers().get(i), true)){
					return;
			}
		}

		setLinearVelocity();
		switchFlickOn();
		elementReleased = true;
	}

	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {}


	private void switchFlickOn() {
		flickEnabled = true;
	}

	private void switchFlickOff() {
		flickEnabled = false;
	}
	
	public boolean isSticky(){
		return sticky;
	}


	public void update(float tpf) {

		if(flickEnabled) {
			moveToNewPosition(tpf);
			applyFriction(tpf);
			if(BorderUtility.isBoundaryEnabled() && bounceOverride) bounce();
			if(toIgnore)checkWithinBorders();
			
			if(autoFlicked){
				ignoreCurrentClosestEdge();
				if(inCentre()){
					setAutoFlicked(false);	
				}
			}
		}
	}
	
	private boolean inCentre() {
		Ray ray = new Ray(targetSpatial.getWorldTranslation(), linearVelocity);
		
		int count = 0;
		
		ArrayList<Triangle> triangles = BorderUtility.getBorderWall();
		
		for (Triangle t: triangles){
			if (ray.intersect(t)){
				count++;
			}
		}
		
		if(count == 1){
			return true;
		}else{
			return false;
		}
		
	}

	public boolean isOutsideEnvironment(){
		if (targetSpatial.getWorldTranslation().x < 0 || targetSpatial.getWorldTranslation().x > DisplaySystem.getDisplaySystem().getWidth() 
				|| targetSpatial.getWorldTranslation().y < 0 || targetSpatial.getWorldTranslation().y > DisplaySystem.getDisplaySystem().getHeight()){
			return true;
		}else{
			return false;
		}
	}

	
	private void checkWithinBorders() {
		if (!autoFlicked && !toBeTransferred){
			Ray ray = new Ray(targetSpatial.getWorldTranslation(), linearVelocity.normalize());

			ArrayList<Triangle> triangles = BorderUtility.getBorderWall();
			
			boolean collides = false;
					
			for (Triangle t : triangles){
				if (ray.intersect(t)){
					collides = true;
				}
			}
			
			if (!collides){
				linearVelocity.set(0,0,0);
				flickEnabled = false;
			}
		}
	}

	public boolean elementReleased(){
		return elementReleased;
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
		linearVelocity.setZ(0);
	}

	private void applyFriction(float tpf) {
		if(linearVelocity == null) return;
		linearVelocity.subtractLocal(linearVelocity.mult(tpf * deceleration));
		if (FastMath.abs(linearVelocity.x) > FastMath.abs(linearVelocity.y)){
			if (FastMath.abs(linearVelocity.x) > speedLimit){
				float difference = FastMath.abs(linearVelocity.x)/speedLimit;
				if (linearVelocity.x < 0){
					linearVelocity.setX(-speedLimit);
				}else{
					linearVelocity.setX(speedLimit);
				}
				linearVelocity.setY(linearVelocity.y/difference);
			}
		}else{
			if (FastMath.abs(linearVelocity.y) > speedLimit){
				float difference = FastMath.abs(linearVelocity.y)/speedLimit;
				if (linearVelocity.y < 0){
					linearVelocity.setY(-speedLimit);
				}else{
					linearVelocity.setY(speedLimit);
				}
				linearVelocity.setX(linearVelocity.x/difference);
			}
		}
		if(linearVelocity.length() < lowerThreshold){
			flickEnabled = false;
			if (toIgnore)noLongerIgnoring();
		}
	}
	
	public void noLongerIgnoring(){
		toIgnore = false;
		edgeToIgnore.clear();
	}

	private void moveToNewPosition(float tpf) {
		if(linearVelocity == null) return;
		Vector3f previousSpeed = linearVelocity;
		Vector3f pos = targetSpatial.getLocalTranslation().clone();
		pos.addLocal(linearVelocity.mult(tpf));
		float oldX = targetSpatial.getLocalTranslation().x;
		float oldY = targetSpatial.getLocalTranslation().y;
		targetSpatial.setLocalTranslation(pos);
		targetSpatial.updateWorldVectors();
		float newX = targetSpatial.getLocalTranslation().x;
		float newY = targetSpatial.getLocalTranslation().y;

        if(FastMath.abs(previousSpeed.x) < FastMath.abs(linearVelocity.x)){
        	if (linearVelocity.x < 0){
        		linearVelocity.setX(-FastMath.abs(previousSpeed.x));
        	}else{
        		linearVelocity.setX(FastMath.abs(previousSpeed.x));
        	}
        }

        if(FastMath.abs(previousSpeed.y) < FastMath.abs(linearVelocity.y)){
        	if (linearVelocity.y < 0){
        		linearVelocity.setY(-FastMath.abs(previousSpeed.y));
        	}else{
        		linearVelocity.setY(FastMath.abs(previousSpeed.y));
        	}
        }
        if (listeners !=null){
			for (int i = 0; i < listeners.size(); i++){
		        if (listeners.get(i) !=null){
		        	listeners.get(i).itemFlicked(this, targetSpatial, newX, newY, oldX, oldY);
		        }
			}
        }
	}
	
	public void simpleRectangleBounce(){
		float[] edgeDistances = new float[4];
						
		edgeDistances[0] = FastMath.abs(targetSpatial.getWorldTranslation().x)/DisplaySystem.getDisplaySystem().getWidth();
		edgeDistances[1] = FastMath.abs(DisplaySystem.getDisplaySystem().getWidth() - targetSpatial.getWorldTranslation().x)/DisplaySystem.getDisplaySystem().getWidth();
		edgeDistances[2] = FastMath.abs(targetSpatial.getWorldTranslation().y)/DisplaySystem.getDisplaySystem().getHeight();
		edgeDistances[3] = FastMath.abs(DisplaySystem.getDisplaySystem().getHeight() - targetSpatial.getWorldTranslation().y)/DisplaySystem.getDisplaySystem().getHeight();
		
		float lowest = edgeDistances[0];
		int indexOfLowest = 0;
		
		for (int j = 1; j < 4; j++){
			if (edgeDistances[j] > lowest){
				indexOfLowest = j;
				lowest = edgeDistances[j];
			}

		}
		
		switch (indexOfLowest){
			
			case 0: 
					if (linearVelocity.x > 0)linearVelocity.setX(-linearVelocity.x);
					break;
					
			case 1: 
					if (linearVelocity.x < 0)linearVelocity.setX(-linearVelocity.x);
					break;
				
			case 2: 
					if (linearVelocity.y > 0)linearVelocity.setY(-linearVelocity.y);
					break;
				
			case 3: 
					if (linearVelocity.y < 0)linearVelocity.setY(-linearVelocity.y);
					break;
		
		}
	}

	public void bounce() {

		if(movingElement != null && !autoFlicked){
			
			for (int i = 0; i < flickSystem.getBouncers().size(); i++){
			
				if (movingElement.getTargetSpatial().hasCollision(flickSystem.getBouncers().get(i), true)){
														
					if (sticky){
						linearVelocity = new Vector3f();
					}else if(reflective){
						linearVelocity = new Vector3f(-linearVelocity.x, -linearVelocity.y, 0);
					}else if(flickSystem.getBouncers().get(i).getName().equals("default")){											
						simpleRectangleBounce();
					}else{				
						
						Vector3f normalVector = getCollisionNormal(flickSystem.getBouncers().get(i));

						Vector3f collisionVector = new Vector3f(linearVelocity.x, linearVelocity.y, 0);
	                    float a = 2* FastMath.abs(collisionVector.dot(normalVector));
	                    Vector3f resultingVector = new Vector3f(a*normalVector.x,a*normalVector.y,0);
	                    float previousVectorSum = FastMath.abs(linearVelocity.x) + FastMath.abs(linearVelocity.y);
	                    linearVelocity = linearVelocity.add(resultingVector);	                    
	                 
	                    float newVectorSum = FastMath.abs(linearVelocity.x) + FastMath.abs(linearVelocity.y);
	                   
	                    if (newVectorSum > previousVectorSum){
	                    	float scale = previousVectorSum;
	                    	scale /= newVectorSum;
	                    	linearVelocity.setX(linearVelocity.x*scale);
	                    	linearVelocity.setY(linearVelocity.y*scale);
	                    }
						
					}
				}					
			}
		}
	}
	
	public boolean isIgnoring(){
		return toIgnore;
	}
	
	private boolean toIgnore = false;
	private ArrayList<Vector3f[]> edgeToIgnore = new ArrayList<Vector3f[]>();
	
	public void ignoreCurrentClosestEdge() {
				
				ArrayList<Triangle> triangles = BorderUtility.getBorderWall();         
                        	            	
				ArrayList<Triangle> trianglesTwo = SpatialUtility.createTriangleWall(targetSpatial);
				
				Ray ray;
				
				for (Triangle u: triangles){
					for (Triangle v: trianglesTwo){
						float acceptableDistance = u.get(0).distance(u.get(1));
						Vector3f direction = new Vector3f(u.get(1).x - u.get(0).x,u.get(1).y - u.get(0).y,0);
						
						ray = new Ray(u.get(0),direction);
						
						Vector3f loc = new Vector3f();

						if (ray.intersectWhere(v, loc)){						
							if (loc.distance(u.get(0)) < acceptableDistance){
								toIgnore = true;
								Vector3f[] toIgnoreVecs = {u.get(0), u.get(1)};
								edgeToIgnore.add(toIgnoreVecs);
							}
						}
						
						
					}
				}
				
          
	}
	
	private Vector3f getCollisionNormal(Spatial s) {
		
        Vector3f normalVector = new Vector3f();
        CollisionResults results = new TriangleCollisionResults();
        targetSpatial.findCollisions(s, results);
        for (int j=0; j < results.getNumber(); j++){
        	CollisionData collisionData = results.getCollisionData(j);
            for(int k =0; k < collisionData.getSourceTris().size(); k++){            	
		        TriMesh geom =(TriMesh)collisionData.getTargetMesh();
		        TriMesh geomTwo =(TriMesh)collisionData.getSourceMesh();
		        int triIndex=collisionData.getSourceTris().get(k);
		        int[]vertices=new int[3];
		        geom.getTriangle(triIndex, vertices);
		        FloatBuffer normalBuffer=geom.getWorldNormals(null);
		        normalVector = new Vector3f(normalBuffer.get((vertices[0]*3)), normalBuffer.get((vertices[0]*3)+1), 0);
		        if (normalBuffer.get((vertices[0]*3)+2) > 0 && !s.getName().contains("border")){
		        	
		        	ArrayList<Triangle> triangles = SpatialUtility.generateTriangles(s);
		        	
		        	ArrayList<Vector3f> coordsTwo;
		        	
		        	if (!justTheMiddle){
		        		coordsTwo = SpatialUtility.generateRelevantCoords(geomTwo, coordsIndex);
		        	}else{
		        		coordsTwo = new ArrayList<Vector3f>();
		        		coordsTwo.add(targetSpatial.getWorldTranslation());
		        	}
	                
	                float distance = 0;
	                boolean first = true;
	                
	                Vector3f face = null;
	                Vector3f collisionPosition = null;   
		        	
		            Ray ray = new Ray();
                	
	                Vector3f reverse = new Vector3f(-linearVelocity.x, -linearVelocity.y, 0);
	                
	                Triangle toStore = new Triangle();
	                
	                for (int i = 0; i < coordsTwo.size()*2; i++){		                	
	                	Vector3f direction = linearVelocity;
	                	Vector3f coord = new Vector3f();
	                	
	                	if (i >= coordsTwo.size()){
	                		direction = reverse;
	                		coord = coordsTwo.get(i - coordsTwo.size());
	                	}else{
	                		coord = coordsTwo.get(i);
	                	}
	                	
	                	ray.setOrigin(coord);
	                	ray.setDirection(direction);
	                	for (int h = 0; h < triangles.size(); h++){	
	                		
	                		Vector3f loc = new Vector3f();	         		
	                		
	                		if (ray.intersectWhere(triangles.get(h), loc)){  	                			
	                			if (first){         				          
	                				face = new Vector3f();
	    	                		face.setX(triangles.get(h).get(1).x - triangles.get(h).get(0).x);
	    	                		face.setY(triangles.get(h).get(1).y - triangles.get(h).get(0).y);
	    	                		collisionPosition = loc;
	    	                		toStore = triangles.get(h);
	                				
	                				distance = targetSpatial.getWorldTranslation().distance(loc);
	                				first = false;
	                			}else{
	                				if (targetSpatial.getWorldTranslation().distance(loc) < distance){
	                					distance = targetSpatial.getWorldTranslation().distance(loc);
		                				face = new Vector3f();
		    	                		face.setX(triangles.get(h).get(1).x - triangles.get(h).get(0).x);
		    	                		face.setY(triangles.get(h).get(1).y - triangles.get(h).get(0).y);
		    	                		collisionPosition = loc;
		    	                		toStore = triangles.get(h);
	                				}
	                			}

	                		}
	                	}
	                }       
	                if (toIgnore){
	                	
	                	if (toStore.get(0) != null && toStore.get(1) != null){	 
	                		for (Vector3f[] edge: edgeToIgnore){
				                if ((toStore.get(0).equals(edge[0]) && toStore.get(1).equals(edge[1])) || 
				                (toStore.get(1).equals(edge[0]) && toStore.get(0).equals(edge[1]))){
				                	
				                	return linearVelocity;	
				                }
	                		}
	                	}
	                }
               	                
	                if (face != null && collisionPosition != null){	  
	                
	                	Vector3f possNormOne = new Vector3f(face.y, -face.x, 0);
	                	Vector3f possNormTwo = new Vector3f(-face.y, face.x, 0);
	                	
	                	Vector3f posOne = new Vector3f(collisionPosition.x + possNormOne.x, collisionPosition.y + possNormOne.y,0);
	                	Vector3f posTwo = new Vector3f(collisionPosition.x + possNormTwo.x, collisionPosition.y + possNormTwo.y,0);
	                	
	                	if (posOne.distance(targetSpatial.getWorldTranslation()) < posTwo.distance(targetSpatial.getWorldTranslation())){		                	
		                	normalVector = possNormOne;
	                	}else{		                	
		                	normalVector = possNormTwo;
	                	}
	
	                }        

	                normalVector.normalizeLocal();  
		        }

            }
        }
        return normalVector;
	} 

	public boolean isAutoFlicked(){
		return autoFlicked;
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

	public void addFlickListener(FlickListener l){
		listeners.add(l);
	}

	public void removeFlickListener(FlickListener l){
		if (listeners.contains(l))
			listeners.remove(l);
	}

	public interface FlickListener{
		public void itemFlicked(FlickMover multiTouchElement, Spatial targetSpatial,  float newLocationX, float newLocationY, float oldLocationX, float oldLocationY);
	}

	public void enableSticky() {
		sticky = true;
	}

	public void disableSticky() {
		sticky = false;
	}
	
	public void makeReflective() {
		reflective = true;
	}

	public void makeUnreflective() {
		reflective = false;
	}

	public void disableBoundary() {
		bounceOverride = false;
	}

	public void enableBoundary() {
		bounceOverride = true;
	}

	public boolean getBoundaryEnabled() {
		return bounceOverride;
	}

	public void setAutoFlicked(boolean b) {
		autoFlicked = b;
	}

	Spatial previousBounce = null;

	public float getSpeed() {
		return FastMath.sqrt(FastMath.sqr(linearVelocity.x)+FastMath.sqr(linearVelocity.y));
	}

	public boolean toBeTransferred = false;

}

