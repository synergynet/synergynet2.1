package synergyspace.jme.sysutils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import com.jme.math.Ray;
import com.jme.math.Triangle;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ObjShape;
import synergynet.table.config.display.DisplayConfigPrefsItem;
import synergyspace.jme.cursorsystem.MultiTouchElement;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.jme.cursorsystem.flicksystem.FlickSystem;
import synergyspace.mtinput.events.MultiTouchCursorEvent;

public class BorderUtility {

	private static BorderUtility instance = new BorderUtility();
	private static final Logger log = Logger.getLogger(BorderUtility.class.getName());
	protected static boolean useBoundary, useVirtualRectangle, gotBottomLeft, gotTopRight, gotTriangles, isDefault = false;
	private static int defaultSteadfastLimit = 3;
	private static String borderFileText = "";
	private static ObjShape border;
	private static Spatial borderSpatial = null;
	private static String borderAdd = "";
	private static float scale = 1f;
	private static float rotation, translationX, translationY, differenceX, differenceY = 0f;
	private static Vector2f bottomLeft, topRight = new Vector2f();
	private static Vector2f[] rectangleVertices;
	private static ArrayList<Triangle> virtualRectangleTriangles = new ArrayList<Triangle>();
	private static ArrayList<Triangle> borderWall = new ArrayList<Triangle>();

	
	public static void enableScreenBoundary(ContentSystem contentSystem) {
		if (new DisplayConfigPrefsItem().getDisplayShape().length() == 0){
			new DisplayConfigPrefsItem().setDefaultShape(true);
		}		
		if (new DisplayConfigPrefsItem().getDefaultShape()){
			defaultBorders(contentSystem);
		}else{
			customBorders(contentSystem);
		}		
		BorderUtility.getInstance().new IgnoreClicks(borderSpatial);
		borderWall = SpatialUtility.createTriangleWall(borderSpatial);
		useBoundary = true;
	}
	
	public static void borderReset(Node orthoNode){
		if (borderSpatial != null){
			orthoNode.attachChild(borderSpatial);
			orthoNode.updateGeometricState(0f, true);
			orthoNode.updateModelBound();			
		}
	}
	
	public static boolean isDefaultBorder(){
		return isDefault;
	}
	
	public static BorderUtility getInstance() {
		return instance;
	}

	public static void disableScreenBoundary() {
		useBoundary = false;
	}
	
	public static boolean isBoundaryEnabled() {
		return useBoundary;
	}
	
	private static void customBorders(ContentSystem contentSystem) {		

		try{
					
			String address = borderAdd;
			
			if (address.equals("")){
				address = new DisplayConfigPrefsItem().getDisplayShape();
				address = "file:/" + address;
				borderAdd = address;
			}
				
			URL url = new URL(address);	
			
			if(url.openConnection().getContentLength() > 0) {

				border = (ObjShape)contentSystem.createContentItem(ObjShape.class);	
				border.setSteadfastLimit(0);
				border.setShapeGeometry(url);		
				border.setAsTopObject();
				border.setBringToTopable(false);
				border.setSolidColour(ColorRGBA.black);
				float scaleY = new DisplayConfigPrefsItem().getHeight();
				scaleY /= 768;
				float scaleX = new DisplayConfigPrefsItem().getWidth();
				scaleX /= 1024;
				((Spatial)border.getImplementationObject()).setLocalScale(new Vector3f(scaleX, scaleY, 1));	
				border.setLocation(DisplaySystem.getDisplaySystem().getWidth()/2, DisplaySystem.getDisplaySystem().getHeight()/2);
				border.setRotateTranslateScalable(false);	
				borderFileText = border.getObjFileText();
				borderSpatial = (Spatial)border.getImplementationObject();
			}else{
				log.info("Custom display shape border could not be loaded.  Default rectangular border loaded instead.");
				defaultBorders(contentSystem);
			}
		}catch(Exception e){
			log.info("Custom display shape border could not be loaded.  Default rectangular border loaded instead.");
			defaultBorders(contentSystem);
		}

	}

	private static void defaultBorders(ContentSystem contentSystem) {	
		isDefault = true;
		useVirtualRectangle = true;
		border = (ObjShape)contentSystem.createContentItem(ObjShape.class);
		border.setShapeGeometry(BorderUtility.class.getResource("defaultBorder.obj"));	
		border.setSteadfastLimit(0);
		borderAdd = BorderUtility.class.getResource("defaultBorder.obj").toString();
		border.setSolidColour(ColorRGBA.black);
		border.setAsTopObject();
		border.setBringToTopable(false);
		float scaleY = new DisplayConfigPrefsItem().getHeight();
		scaleY /= 768;
		float scaleX = new DisplayConfigPrefsItem().getWidth();
		scaleX /= 1024;
		((Spatial)border.getImplementationObject()).setLocalScale(new Vector3f(scaleX, scaleY, 1));	
		border.setLocation(DisplaySystem.getDisplaySystem().getWidth()/2, DisplaySystem.getDisplaySystem().getHeight()/2);
		border.setRotateTranslateScalable(false);
		borderFileText = border.getObjFileText();
		borderSpatial = (Spatial)border.getImplementationObject();
		borderSpatial.setName("default");
	}	
	
	public static Spatial getBorderSpatial(){
		return borderSpatial;
	}
	
	public static String getBorderFileText(){
		return borderFileText;
	}
	
	private static void getVirtualRectangleEnvironment(){
		
		useVirtualRectangle = true;
		
		String address = borderAdd;
		address = address.replace("file:/", "");
		address = address.replace("%20", " ");
		
		rectangleVertices = new Vector2f[4];
			
		try{
		    FileInputStream fstream = new FileInputStream(address);
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		    while ((strLine = br.readLine()) != null)   {
		    	if (strLine.contains("# Rectangle1")){
		    		rectangleVertices[0]= new Vector2f(Float.parseFloat(strLine.split(" ")[2]),Float.parseFloat(strLine.split(" ")[3]));
		    	}else if (strLine.contains("# Rectangle2")){
		    		rectangleVertices[1]= new Vector2f(Float.parseFloat(strLine.split(" ")[2]),Float.parseFloat(strLine.split(" ")[3]));
		    	}else if (strLine.contains("# Rectangle3")){
		    		rectangleVertices[2]= new Vector2f(Float.parseFloat(strLine.split(" ")[2]),Float.parseFloat(strLine.split(" ")[3]));
		    	}else if (strLine.contains("# Rectangle4")){
		    		rectangleVertices[3]= new Vector2f(Float.parseFloat(strLine.split(" ")[2]),Float.parseFloat(strLine.split(" ")[3]));
		    	}		     
		    }
		    in.close();
		    }catch (Exception e){
		    	log.info("Could not open border .obj file.");
		    }
		    
		    float aspectRatio = 1024f/768f;
		    
		    float[] distances = new float[6];
		    int count = 0;
		    
		    for (int i = 0; i < rectangleVertices.length; i++){
			    for (int j = i; j < rectangleVertices.length; j++){
			    	if (i!=j){
			    		distances[count] = rectangleVertices[i].distance(rectangleVertices[j]);			    		
			    		count++;
			    	}
			    }
		    }
		    
		    Arrays.sort(distances);
		    
		    if (distances[0] != distances[1] || distances[2] != distances[3]){
		    	useVirtualRectangle = false;
		    }else{
		    	float newAspectRatio = distances[0]/distances[2];

		    	if (newAspectRatio > aspectRatio){
		    		scale = distances[2]/1024f;
		    	}else{
		    		scale = distances[0]/768f;
		    	}
		    	
		    	boolean exit = false;
		    	
			    for (int i = 0; i < rectangleVertices.length && !exit; i++){
				    for (int j = i; j < rectangleVertices.length; j++){
				    	if (i!=j){
				    		 if (rectangleVertices[i].distance(rectangleVertices[j]) == distances[2]){
				    			 Vector2f difference;
				    			 if (rectangleVertices[i].x > rectangleVertices[j].x){
				    				 difference = rectangleVertices[j].subtract(rectangleVertices[i]);
				    			 }else{
				    				 difference = rectangleVertices[i].subtract(rectangleVertices[j]);
				    			 }
				    			 
			    				 Vector2f axis = new Vector2f(-distances[2],0);
			    				 rotation = difference.angleBetween(axis);
				    			 exit = true;
				    		 }				    		
				    	}
				    }
			    }
			    
			    exit = false;
		    	
			    for (int i = 0; i < rectangleVertices.length && !exit; i++){
				    for (int j = i; j < rectangleVertices.length; j++){
				    	if (i!=j){
				    		 if (rectangleVertices[i].distance(rectangleVertices[j]) == distances[4]){
				    			 Vector2f difference = rectangleVertices[j].subtract(rectangleVertices[i]);

				    			 Vector2f center = new Vector2f(rectangleVertices[i].x + (difference.x/2), rectangleVertices[i].y + (difference.y/2));
				    			 			    			 
				    			 translationX = center.x * (DisplaySystem.getDisplaySystem().getWidth()/1024f);
				    			 translationY = center.y * (DisplaySystem.getDisplaySystem().getHeight()/768f);
				    			 			    			 
				    			 exit = true;
				    		 }				    		
				    	}
				    }
			    }	
			    if (exit){
					Vector2f p = new Vector2f(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2 + translationX, 
							DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2+ translationY);
					
					Vector2f q = new Vector2f(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2*scale, 
							DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2*scale);
					q.rotateAroundOrigin(BorderUtility.getVirtualRectangleEnvironmentRotation(), true);
					
					differenceX = p.x - q.x;
					differenceY = p.y - q.y;
					
			    }
		    	
		    }
		    
	}
	
	public static Vector2f forcePull(float x, float y) {
		
		Vector2f result = new Vector2f();
		
		Vector3f currentPos = new Vector3f(x,y,0);
		
		Vector2f upVec = new Vector2f(0,1);
		Vector2f downVec = new Vector2f(0,-1);
		Vector2f leftVec = new Vector2f(-1,0);
		Vector2f rightVec = new Vector2f(1,0);
		
		upVec.rotateAroundOrigin(BorderUtility.getVirtualRectangleEnvironmentRotation(), true);
		downVec.rotateAroundOrigin(BorderUtility.getVirtualRectangleEnvironmentRotation(), true);
		leftVec.rotateAroundOrigin(BorderUtility.getVirtualRectangleEnvironmentRotation(), true);
		rightVec.rotateAroundOrigin(BorderUtility.getVirtualRectangleEnvironmentRotation(), true);
				
		Ray up = new Ray(currentPos, new Vector3f(upVec.x, upVec.y, 0));
		Ray down = new Ray(currentPos, new Vector3f(downVec.x, downVec.y, 0));
		Ray left = new Ray(currentPos, new Vector3f(leftVec.x, leftVec.y, 0));
		Ray right = new Ray(currentPos, new Vector3f(rightVec.x, rightVec.y, 0));
		
		ArrayList<Triangle> virtualRectangleTriangles = BorderUtility.getVirtualRectangleTriangles();
		
		Vector3f upVirtualRectangleCollisionPos = new Vector3f();
		Vector3f downVirtualRectangleCollisionPos = new Vector3f();
		Vector3f leftVirtualRectangleCollisionPos = new Vector3f();
		Vector3f rightVirtualRectangleCollisionPos = new Vector3f();
		
		for (int i = 0; i < virtualRectangleTriangles.size(); i++){
			if (up.intersect(virtualRectangleTriangles.get(i)))up.intersectWhere(virtualRectangleTriangles.get(i), upVirtualRectangleCollisionPos);
			if (down.intersect(virtualRectangleTriangles.get(i)))down.intersectWhere(virtualRectangleTriangles.get(i), downVirtualRectangleCollisionPos);
			if (left.intersect(virtualRectangleTriangles.get(i)))left.intersectWhere(virtualRectangleTriangles.get(i), leftVirtualRectangleCollisionPos);
			if (right.intersect(virtualRectangleTriangles.get(i)))right.intersectWhere(virtualRectangleTriangles.get(i), rightVirtualRectangleCollisionPos);
		}	
		
    	ArrayList<Triangle> borderTriangles = new ArrayList<Triangle>();
    	
		Vector3f upBorderCollisionPos = null;
		Vector3f downBorderCollisionPos = null;
		Vector3f leftBorderCollisionPos = null;
		Vector3f rightBorderCollisionPos = null;
    	
    	if(FlickSystem.getInstance().getBouncers().contains(borderSpatial)){
    		
    		borderTriangles = BorderUtility.getBorderWall();
    		
			for (int i = 0; i < borderTriangles.size(); i++){
				if (up.intersect(borderTriangles.get(i))){
					Vector3f tempVec = new Vector3f();
					up.intersectWhere(borderTriangles.get(i), tempVec);
					if (upBorderCollisionPos == null){
						upBorderCollisionPos = tempVec;
					}else{
						if (currentPos.distance(tempVec) < currentPos.distance(upBorderCollisionPos)){
							upBorderCollisionPos = tempVec;
						}
					}
				}
				if (down.intersect(borderTriangles.get(i))){
					Vector3f tempVec = new Vector3f();
					down.intersectWhere(borderTriangles.get(i), tempVec);
					if (downBorderCollisionPos == null){
						downBorderCollisionPos = tempVec;
					}else{
						if (currentPos.distance(tempVec) < currentPos.distance(downBorderCollisionPos)){
							downBorderCollisionPos = tempVec;
						}
					}
				}
				if (left.intersect(borderTriangles.get(i))){
					Vector3f tempVec = new Vector3f();
					left.intersectWhere(borderTriangles.get(i), tempVec);
					if (leftBorderCollisionPos == null){
						leftBorderCollisionPos = tempVec;
					}else{
						if (currentPos.distance(tempVec) < currentPos.distance(leftBorderCollisionPos)){
							leftBorderCollisionPos = tempVec;
						}
					}
				}
				if (right.intersect(borderTriangles.get(i))){
					Vector3f tempVec = new Vector3f();
					right.intersectWhere(borderTriangles.get(i), tempVec);
					if (rightBorderCollisionPos == null){						
						rightBorderCollisionPos = tempVec;
					}else{
						if (currentPos.distance(tempVec) < currentPos.distance(rightBorderCollisionPos)){
							rightBorderCollisionPos = tempVec;							
						}
					}
				}
			}
			
			if (upBorderCollisionPos != null && downBorderCollisionPos != null && leftBorderCollisionPos != null && rightBorderCollisionPos != null){
							
				float upDistance = upVirtualRectangleCollisionPos.distance(upBorderCollisionPos);
				upDistance *= (upDistance/(currentPos.distance(upVirtualRectangleCollisionPos) + upDistance));		
				float downDistance = downVirtualRectangleCollisionPos.distance(downBorderCollisionPos);
				downDistance *= (downDistance/(currentPos.distance(downVirtualRectangleCollisionPos) + downDistance));
				float leftDistance = leftVirtualRectangleCollisionPos.distance(leftBorderCollisionPos);
				leftDistance *= (leftDistance/(currentPos.distance(leftVirtualRectangleCollisionPos) + leftDistance));
				float rightDistance = rightVirtualRectangleCollisionPos.distance(rightBorderCollisionPos);
				rightDistance *= (rightDistance/(currentPos.distance(rightVirtualRectangleCollisionPos) + rightDistance));
								
				float verticalDistance = upDistance - downDistance;
				float horizontalDistance = rightDistance - leftDistance;
				
				Vector2f vertical = new Vector2f(up.getDirection().x * verticalDistance, up.getDirection().y * verticalDistance);
				Vector2f horizontal = new Vector2f(right.getDirection().x * horizontalDistance, right.getDirection().y * horizontalDistance);
				
				result.setX(vertical.x + horizontal.x);
				result.setY(vertical.y + horizontal.y);
							
//				Ray checkPos = new Ray(currentPos, new Vector3f(result.x, result.y, 0));
//				Vector3f collisionPosition = null;
//				
//				for (int i = 0; i < borderTriangles.size(); i++){
//					if (checkPos.intersect(borderTriangles.get(i))){
//						Vector3f tempVec = new Vector3f();
//						checkPos.intersectWhere(borderTriangles.get(i), tempVec);
//						if (collisionPosition == null){
//							collisionPosition = tempVec;
//						}else{
//							if (currentPos.distance(tempVec) < currentPos.distance(collisionPosition)){
//								collisionPosition = tempVec;
//							}
//						}
//					}
//				}
//				
//				if (collisionPosition!=null){
//					if (currentPos.distance(new Vector3f(x + result.x, y + result.y, 0)) > currentPos.distance(collisionPosition)){
//						result = new Vector2f(collisionPosition.x - x, collisionPosition.y - y);
//					}
//				}
			}
    	}
		return result;
	}
	
	public static void setBorderColour(ColorRGBA colour){
		border.setSolidColour(colour);
	}
	
	public static boolean isVirtualRectangleValid(){
		if (!useVirtualRectangle)getVirtualRectangleEnvironment();
		return useVirtualRectangle;
	}
	
	public static float getVirtualRectangleEnvironmentScale(){
		if (!useVirtualRectangle)getVirtualRectangleEnvironment();
		return scale;
	}
		
	public static float getVirtualRectangleEnvironmentRotation(){
		if (!useVirtualRectangle)getVirtualRectangleEnvironment();
		return rotation;
	}
	
	public static float getVirtualRectangleEnvironmentTranslationX(){
		if (!useVirtualRectangle)getVirtualRectangleEnvironment();
		return translationX;
	}
	
	public static float getVirtualRectangleEnvironmentTranslationY(){
		if (!useVirtualRectangle)getVirtualRectangleEnvironment();
		return translationY;
	}
	
	public static float getVirtualRectangleEnvironmentOffsetX(){
		if (!useVirtualRectangle)getVirtualRectangleEnvironment();
		return differenceX;
	}
	
	public static float getVirtualRectangleEnvironmentOffsetY(){
		if (!useVirtualRectangle)getVirtualRectangleEnvironment();
		return differenceY;
	}
	
	
	public class IgnoreClicks extends MultiTouchElement {			
		
		public IgnoreClicks(Spatial s) {	
			super(s);
			this.setPickMeOnly(true);
		}

		@Override
		public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {}

		@Override
		public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}

		@Override
		public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {}

		@Override
		public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {}
	}
	

	public static Vector2f getBottomLeftCorner(){
		if (!gotBottomLeft)getBottomLeft();
		return bottomLeft;
	}
	
	public static void getBottomLeft(){
		bottomLeft = SpatialUtility.getClosestInnerVertex(borderSpatial, new Vector2f(0,0));
		gotBottomLeft = true;
	}	
	
	public static Vector2f getTopRightCorner(){
		if (!gotTopRight)getTopRight();
		return topRight;
	}
	
	public static void getTopRight(){
		topRight = SpatialUtility.getClosestInnerVertex(borderSpatial, new Vector2f(DisplaySystem.getDisplaySystem().getWidth(),DisplaySystem.getDisplaySystem().getHeight()));
		gotTopRight = true;
	}
	
	public static ArrayList<Triangle> getVirtualRectangleTriangles(){
		if (!useVirtualRectangle)getVirtualRectangleEnvironment();
		if (!gotTriangles){
			
			
			float maxDistanceOne = rectangleVertices[0].distance(rectangleVertices[1]);
			float maxDistanceTwo = rectangleVertices[0].distance(rectangleVertices[1]);
			
			for (int i = 0; i<3; i++){
				for (int j = i+1; j<4; j++){
					float distance = rectangleVertices[i].distance(rectangleVertices[j]);
					if (distance > maxDistanceOne){
						maxDistanceTwo = maxDistanceOne;
						maxDistanceOne = distance;
					}else if(distance > maxDistanceTwo){
						maxDistanceTwo = distance;
					}
				}
			}
			
			for (int i = 0; i<3; i++){
				for (int j = i+1; j<4; j++){
					float distance = rectangleVertices[i].distance(rectangleVertices[j]);
					if (distance != maxDistanceOne && distance != maxDistanceTwo){
						
						Vector3f newPosOne = new Vector3f(((rectangleVertices[i].x/1024f)*DisplaySystem.getDisplaySystem().getWidth()) + DisplaySystem.getDisplaySystem().getWidth()/2,
								((rectangleVertices[i].y/768f)*DisplaySystem.getDisplaySystem().getHeight()) + DisplaySystem.getDisplaySystem().getHeight()/2, 0);
						Vector3f newPosTwo = new Vector3f(((rectangleVertices[j].x/1024f)*DisplaySystem.getDisplaySystem().getWidth()) + DisplaySystem.getDisplaySystem().getWidth()/2,
								((rectangleVertices[j].y/768f)*DisplaySystem.getDisplaySystem().getHeight()) + DisplaySystem.getDisplaySystem().getHeight()/2, 0);
						Vector3f halfway = new Vector3f(newPosTwo.x - newPosOne.x, newPosTwo.y - newPosOne.y, 1);
			    		Triangle t = new Triangle(newPosOne, newPosTwo, halfway);                		
			    		virtualRectangleTriangles.add(t);

					}
				}
			}
			
			gotTriangles = true;
		}
		return virtualRectangleTriangles;
	}

	public static ArrayList<Triangle> getBorderWall() {
		return borderWall;
	}
	
	public static void setDefaultSteadfastLimit(int i){
		defaultSteadfastLimit = i;
	}

	public static int getDefaultSteadfastLimit() {
		return defaultSteadfastLimit;
	}
	
}
