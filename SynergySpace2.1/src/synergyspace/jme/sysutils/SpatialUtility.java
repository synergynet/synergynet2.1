package synergyspace.jme.sysutils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import synergyspace.jme.cursorsystem.flicksystem.FlickSystem;

import com.jme.math.Ray;
import com.jme.math.Triangle;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;

public class SpatialUtility {
	
	private static HashMap<Spatial, ArrayList<Vector3f[]>> edgeCollection = new HashMap<Spatial, ArrayList<Vector3f[]>>();
	private static HashMap<Spatial, ArrayList<Integer>> relevantCoordsCollection = new HashMap<Spatial, ArrayList<Integer>>();
	private static HashMap<Spatial, ArrayList<int[]>> wallCoordsCollection = new HashMap<Spatial, ArrayList<int[]>>();
	
	public static void clearCollections(){
		edgeCollection.clear();
		relevantCoordsCollection.clear();
		wallCoordsCollection.clear();
	}
	
	public static float getMaxRadius(Spatial targetSpatial){
		float result = -1;
		if(!(targetSpatial instanceof TriMesh)) return result;
		TriMesh geom = (TriMesh)targetSpatial;
		
    	FloatBuffer coordBuffer=geom.getWorldCoords(null);
        ArrayList<Vector3f> coords = new ArrayList<Vector3f>();
           
        for (int i = 0; i < coordBuffer.limit(); i += 3){
        	coords.add(new Vector3f(coordBuffer.get(i), coordBuffer.get(i+1), 0));
        }
        
        for (int i = 0; i < coords.size(); i++){

            	float newDistance = coords.get(i).distance(targetSpatial.getLocalTranslation());
            	if (result ==-1){
            		result = newDistance;
            	}else{
            		if (newDistance < result){
            			result = newDistance;
            		}
            	}
            
        }
		return result;
	}
	
	public static float getMaxDimension(Spatial targetSpatial){
		float result = -1;
		if(!(targetSpatial instanceof TriMesh)) return result;
		TriMesh geom = (TriMesh)targetSpatial;
		
    	FloatBuffer coordBuffer=geom.getWorldCoords(null);
        ArrayList<Vector3f> coords = new ArrayList<Vector3f>();
           
        for (int i = 0; i < coordBuffer.limit(); i += 3){
        	coords.add(new Vector3f(coordBuffer.get(i), coordBuffer.get(i+1), 0));
        }
        
        for (int i = 0; i < coords.size(); i++){
            for (int j = i+1; j < coords.size(); j++){
            	float newDistance = coords.get(i).distance(coords.get(j));
            	if (result ==-1){
            		result = newDistance;
            	}else{
            		if (newDistance < result){
            			result = newDistance;
            		}
            	}
            }
        }
		return result;
	}
	
	public static Vector3f getCollisionAbsolutePos(Spatial s, Spatial targetSpatial){
		
		ArrayList<Triangle> triangles = SpatialUtility.createTriangleWall(s);         
    	
		ArrayList<Triangle> trianglesTwo = SpatialUtility.createTriangleWall(targetSpatial);
		
		Ray ray;
		
		Vector3f toReturn = null;
		Vector3f otherVector = null;
		
		float distance = -1;
		
		for (Triangle u: triangles){
			for (Triangle v: trianglesTwo){
				float acceptableDistance = u.get(0).distance(u.get(1));
				Vector3f direction = new Vector3f(u.get(1).x - u.get(0).x,u.get(1).y - u.get(0).y,0);				
				
				ray = new Ray(u.get(0),direction);
				
				Vector3f loc = new Vector3f();

				if (ray.intersectWhere(v, loc)){						
					if (loc.distance(u.get(0)) < acceptableDistance){
						if (distance < 0 || distance > loc.distance(s.getLocalTranslation())){
							distance = loc.distance(s.getLocalTranslation());
							otherVector = toReturn;
							toReturn = loc;
						}						
					}
				}
				
				//TODO:  May be better to get point between two closest points
			}
		}
		
		if(otherVector != null){
			Vector3f newVec = otherVector.subtract(toReturn);
			newVec = newVec.divide(2);
			toReturn = toReturn.add(newVec);
		}
		
	    return toReturn;
    
	}
	
	public static float[] getCollisionWallStats(Vector3f locOne, Vector3f locTwo, Vector3f collisionPos, Spatial targetSpatial){
		
		float[] result = {0f,0f,0f};
		collisionPos.setZ(0);
		
		TriMesh geom = (TriMesh)targetSpatial;
		
    	FloatBuffer worldCoordBuffer=geom.getWorldCoords(null);
		    	   	
        for (int i = 0; i < worldCoordBuffer.limit()-3; i += 3){
        	for (int j = i+3; j < worldCoordBuffer.limit(); j += 3){
            	if ((worldCoordBuffer.get(i) == locOne.x && worldCoordBuffer.get(i+1) == locOne.y && 
            			worldCoordBuffer.get(j) == locTwo.x && worldCoordBuffer.get(j+1) == locTwo.y) || 
            			(worldCoordBuffer.get(i) == locTwo.x && worldCoordBuffer.get(i+1) == locTwo.y && 
            					worldCoordBuffer.get(j) == locOne.x && worldCoordBuffer.get(j+1) == locOne.y)){
            		result[0] = i;
            		result[1] = j;            		
            		result[2] = collisionPos.distance(new Vector3f(worldCoordBuffer.get(i), worldCoordBuffer.get(i+1), 0)); 
            	}
        	}
        }
        	
		return result;
	}
	
	public static Vector3f getCollisionPos(float[] locationStats, Spatial targetSpatial) {
		
		TriMesh geom = (TriMesh)targetSpatial;
		FloatBuffer worldCoordBuffer=geom.getWorldCoords(null);
		
		Vector3f pointOne = new Vector3f(worldCoordBuffer.get((int)locationStats[0]), worldCoordBuffer.get((int)locationStats[0]+1),0);
		Vector3f pointTwo = new Vector3f(worldCoordBuffer.get((int)locationStats[1]), worldCoordBuffer.get((int)locationStats[1]+1),0);
			
		float distance = locationStats[2];
		
		float scale = distance/(pointOne.distance(pointTwo));
		float xChange = scale * (pointOne.x - pointTwo.x);
		float yChange = scale * (pointOne.y - pointTwo.y);
		
		Vector3f result = new Vector3f(pointOne.x - xChange, pointOne.y - yChange, 0);
		
		return result;
	}
	
	public static ArrayList<Integer> getIndexOfRelevantCoords(Spatial s) {		
		
		if (relevantCoordsCollection.containsKey(s))return relevantCoordsCollection.get(s);
		
		ArrayList<Vector3f[]>edges = findEdges(s, false);
		
		ArrayList<Integer> coordsIndex = new ArrayList<Integer>();
		TriMesh geom=(TriMesh)s;
		ArrayList<Vector3f> coordsOfInterest = new ArrayList<Vector3f>();
		for (int i = 0; i<edges.size(); i++){
			if(!coordsOfInterest.contains(edges.get(i)[0]))
				coordsOfInterest.add(edges.get(i)[0]);
			if(!coordsOfInterest.contains(edges.get(i)[1]))
				coordsOfInterest.add(edges.get(i)[1]);
		}
	    FloatBuffer coordBufferTwo=geom.getVertexBuffer();
          
        ArrayList<Vector3f> usefulCoords = new ArrayList<Vector3f>();
         
        for (int i = 0; i < coordBufferTwo.limit(); i += 3){
        	usefulCoords.add(new Vector3f(coordBufferTwo.get(i), coordBufferTwo.get(i+1), 0));
        }
        
        for(int i = 0; i < coordsOfInterest.size();i++){
        	coordsIndex.add(usefulCoords.indexOf(coordsOfInterest.get(i)));
        }
        
        relevantCoordsCollection.put(s, coordsIndex);
        
        return coordsIndex;
	}

	public static ArrayList<int[]> createWallCoords(Spatial s, ArrayList<Vector3f[]> edges) {
		
		if (wallCoordsCollection.containsKey(s))return wallCoordsCollection.get(s);
		
		TriMesh geom = (TriMesh)s;
		    	       
    	FloatBuffer vertexBuffer=geom.getVertexBuffer();
        ArrayList<Vector3f> vertici = new ArrayList<Vector3f>();
           
        for (int i = 0; i < vertexBuffer.limit(); i += 3){
        	vertici.add(new Vector3f(vertexBuffer.get(i), vertexBuffer.get(i+1), 0));
        }
        
        ArrayList<int[]> edgeVertexIndexes = new ArrayList<int[]>();
        	        	
    	for (int i = 0; i < edges.size(); i++){
    		int[] indexes = {vertici.indexOf(edges.get(i)[0]), vertici.indexOf(edges.get(i)[1])};
    		edgeVertexIndexes.add(indexes);		        				
    	}	
    	
    	wallCoordsCollection.put(s, edgeVertexIndexes);
    	
    	return edgeVertexIndexes;
		
	}
	
	public static ArrayList<Triangle> createTriangleWall(Spatial s) {	
		
		if(!(s instanceof TriMesh)){
			try{
				s = ((Node)((Node)s).getChild(0)).getChild(0);
			}catch(Exception e){				
				return new ArrayList<Triangle>();
			}
		}
		
		ArrayList<Vector3f[]>edges = findEdges(s, true);
		
		TriMesh geom = (TriMesh)s;
				
    	FloatBuffer coordBuffer=geom.getWorldCoords(null);
        ArrayList<Vector3f> coords = new ArrayList<Vector3f>();
           
        for (int i = 0; i < coordBuffer.limit(); i += 3){
        	coords.add(new Vector3f(coordBuffer.get(i), coordBuffer.get(i+1), 0));
        }
        
    	FloatBuffer vertexBuffer=geom.getVertexBuffer();
        ArrayList<Vector3f> vertici = new ArrayList<Vector3f>();
           
        for (int i = 0; i < vertexBuffer.limit(); i += 3){
        	vertici.add(new Vector3f(vertexBuffer.get(i), vertexBuffer.get(i+1), 0));
        }
        
        ArrayList<int[]> edgeVertexIndexes = new ArrayList<int[]>();
        	        	
    	for (int i = 0; i < edges.size(); i++){
    		int[] indexes = {vertici.indexOf(edges.get(i)[0]), vertici.indexOf(edges.get(i)[1])};
    		edgeVertexIndexes.add(indexes);		        				
    	}	
    	
    	ArrayList<Triangle> triangles = new ArrayList<Triangle>();
    	
    	for (int i = 0; i < edgeVertexIndexes.size(); i++){
    		
    		Vector3f vecOne = coords.get(edgeVertexIndexes.get(i)[0]);
    		Vector3f vecTwo = coords.get(edgeVertexIndexes.get(i)[1]);
    		
    		Vector3f halfway = new Vector3f(vecTwo.x - vecOne.x,vecTwo.y - vecOne.y,1);
    		
    		Triangle t = new Triangle(vecOne, vecTwo, halfway);                		
    		triangles.add(t);
    	}
    	   	
    	return triangles;   
		
	}	

	public static ArrayList<Vector3f[]> findEdges(Spatial s, boolean removeContainer) {	
		
		if (edgeCollection.containsKey(s))return edgeCollection.get(s);
		
		ArrayList<Vector3f[]> edges = new ArrayList<Vector3f[]>();
			
		TriMesh geom = (TriMesh)s;
		
		geom.updateWorldBound();
		geom.updateModelBound();
		
    	Triangle[] tris = geom.getMeshAsTriangles(null);

    	for (Triangle t: tris){
    		   		
    		ArrayList<Vector3f> containerVectors = new ArrayList<Vector3f>();
    		
    		if (removeContainer){    			
    			containerVectors.add(new Vector3f(-522,-394,0));
    			containerVectors.add(new Vector3f(-522,394,0));
    			containerVectors.add(new Vector3f(522,-394,0));
    			containerVectors.add(new Vector3f(522,394,0));
    		}
    			 

    		Vector3f[] vecOne = {t.get(0), t.get(1)};
    		ArrayList<Vector3f[]> aResult = containsSameEdge(vecOne, edges);
    		
   		
    		if (aResult == null && !containerVectors.contains(t.get(0)) && !containerVectors.contains(t.get(1))){    			
    			edges.add(vecOne);
    		}else if(aResult != null){
    			edges = aResult;
    		}
    		
    		Vector3f[] vecTwo = {t.get(1), t.get(2)};
    		aResult = containsSameEdge(vecTwo, edges);
    		if (aResult == null && !containerVectors.contains(t.get(1)) && !containerVectors.contains(t.get(2))){
    			edges.add(vecTwo);
    		}else if(aResult != null){
    			edges = aResult;
    		}
    		
    		Vector3f[] vecThree = {t.get(2), t.get(0)};
    		aResult = containsSameEdge(vecThree, edges);
    		if (aResult == null && !containerVectors.contains(t.get(2)) && !containerVectors.contains(t.get(0))){
    			edges.add(vecThree);
    		} 	else if(aResult != null){
    			edges = aResult;
    		}	
    	}

    	edgeCollection.put(s, edges);
    	
    	return edges;
    	
	}

	private static ArrayList<Vector3f[]> containsSameEdge(Vector3f[] vec, ArrayList<Vector3f[]> edges) {	
		
		boolean result = false;
		
		ArrayList<Vector3f[]> newEdges = new ArrayList<Vector3f[]>();
		
		for (int i = 0; i < edges.size(); i++){
			if ((vec[0].equals(edges.get(i)[0]) && vec[1].equals(edges.get(i)[1])) || (vec[0].equals(edges.get(i)[1]) && vec[1].equals(edges.get(i)[0]))){
				result = true;
			}else{
				newEdges.add(edges.get(i));
			}
		}	
		
		if (!result) newEdges = null;
		
		return newEdges;
	}
	
	public static ArrayList<Triangle> generateTriangles(Spatial s) {
		
		TriMesh geom = (TriMesh)s;
		
    	ArrayList<Triangle> triangles = new ArrayList<Triangle>();
    	   	
    	if (BorderUtility.getBorderSpatial().equals(s)){
    		triangles = BorderUtility.getBorderWall();
    	}else{
    		ArrayList<int[]> edgeVertexIndexes = FlickSystem.getWallCoords().get(FlickSystem.getInstance().getBouncers().indexOf(s));
    			        	
        	FloatBuffer coordBuffer=geom.getWorldCoords(null);
            ArrayList<Vector3f> coords = new ArrayList<Vector3f>();
               
            for (int i = 0; i < coordBuffer.limit(); i += 3){
            	coords.add(new Vector3f(coordBuffer.get(i), coordBuffer.get(i+1), 0));
            }

        	for (int i = 0; i < edgeVertexIndexes.size(); i++){
        		       		
        		Vector3f vecOne = coords.get(edgeVertexIndexes.get(i)[0]);
        		Vector3f vecTwo = coords.get(edgeVertexIndexes.get(i)[1]);
        		
        		Vector3f halfway = new Vector3f(vecTwo.x - vecOne.x,vecTwo.y - vecOne.y,1);
        		
        		Triangle t = new Triangle(vecOne, vecTwo, halfway);                		
        		triangles.add(t);
        	}
    	
    	}
    	
		return triangles;
	}
	
	public static ArrayList<Vector3f> generateRelevantCoords(Spatial s, ArrayList<Integer> coordsIndex) {
		
		TriMesh geom = (TriMesh)s;
		
	    FloatBuffer coordBufferTwo=geom.getWorldCoords(null);
        
        ArrayList<Vector3f> usefulCoords = new ArrayList<Vector3f>();
       
        for (int i = 0; i < coordBufferTwo.limit(); i += 3){
        	usefulCoords.add(new Vector3f(coordBufferTwo.get(i), coordBufferTwo.get(i+1), 0));
        }
        
        ArrayList<Vector3f> coordsTwo = new ArrayList<Vector3f>();
        
        for (int i = 0 ; i < coordsIndex.size(); i++){
        	coordsTwo.add(usefulCoords.get(coordsIndex.get(i)));
        }
        	                	                
		return coordsTwo;
	}
	
	public static Vector2f getClosestInnerVertex(Spatial s, Vector2f vec){
		Vector2f result = new Vector2f();
		
		TriMesh geom = (TriMesh)s;
		
	    FloatBuffer coordBuffer=geom.getWorldCoords(null);
        
        ArrayList<Vector2f> usefulCoords = new ArrayList<Vector2f>();
       
        for (int i = 0; i < coordBuffer.limit(); i += 3){
        	if (coordBuffer.get(i) >= 0 && coordBuffer.get(i+1) >= 0)
        	usefulCoords.add(new Vector2f(coordBuffer.get(i), coordBuffer.get(i+1)));
        }
        
        float distance = 0;
        
        for (int i = 0; i < usefulCoords.size(); i++){
        	if (i == 0){
        		distance = usefulCoords.get(i).distance(vec);
        		result.set(usefulCoords.get(i));
        	}else{
        		float newdistance = usefulCoords.get(i).distance(vec);
        		if (newdistance < distance){
            		distance = usefulCoords.get(i).distance(vec);
            		result.set(usefulCoords.get(i));
        		}
        	}
        }
		
		return result;
	}
	
	
}
