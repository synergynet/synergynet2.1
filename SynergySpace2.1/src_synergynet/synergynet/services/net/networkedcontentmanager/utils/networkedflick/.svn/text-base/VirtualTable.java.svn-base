
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
package synergynet.services.net.networkedcontentmanager.utils.networkedflick;

import java.util.ArrayList;

import com.jme.intersection.IntersectionRecord;
import com.jme.math.Ray;
import com.jme.math.Triangle;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ObjShape;
import synergynet.contentsystem.items.utils.Direction;
import synergynet.services.net.localpresence.TableIdentity;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergyspace.jme.sysutils.SpatialUtility;

public class VirtualTable{
	
	private static final long serialVersionUID = 3055508502964905444L;
	private TableIdentity tableId;
	private ArrayList<Triangle> triangleWall;
	private float height = 768;
	private float width = 1024;
	
	public ObjShape shape;
	
	public VirtualTable(VirtualTable vt){		
		shape = vt.shape;		
		this.tableId = vt.getTableId();
		triangleWall = vt.triangleWall;
		height *= vt.height;
		width  *= vt.width;
	}
	
	public VirtualTable(DefaultSynergyNetApp app, TableInfo remoteTableInfo){
				
		ContentSystem contentSystem = ContentSystem.getContentSystemForSynergyNetApp(app);
		shape = (ObjShape)contentSystem.createContentItem(ObjShape.class);
		shape.setSteadfastLimit(0);
		shape.setShapeGeometry(remoteTableInfo.getObjFile());		
		shape.setSolidColour(ColorRGBA.black);
		shape.setRotateTranslateScalable(false);
		
		float scaleX = remoteTableInfo.getWidth() / 1024f;		
		float scaleY = remoteTableInfo.getHeight() / 768f;
		
		shape.setScale(scaleX, Direction.X);
		shape.setScale(scaleY, Direction.Y);
		
		width  = remoteTableInfo.getWidth();
		height = remoteTableInfo.getHeight();
		
		this.tableId = remoteTableInfo.getTableId();

	}
	
	public void setTableId(TableIdentity tableId)	{
		this.tableId = tableId;
	}
	
	public TableIdentity getTableId(){
		return tableId;
	}

	public float getHeight(){
		return height;
	}
	
	public float getWidth(){
		return width;
	}
	
	public boolean hasTableCollision(VirtualTable virtualRemoteTable) {
		
		Ray ray;
		
		for (Triangle u: this.triangleWall){
			for (Triangle v: virtualRemoteTable.triangleWall){
				float acceptableDistance = u.get(0).distance(u.get(1));
				Vector3f direction = new Vector3f(u.get(1).x - u.get(0).x,u.get(1).y - u.get(0).y,0);
				
				ray = new Ray(u.get(0),direction);
				
				Vector3f loc = new Vector3f();
				if(u.get(0).equals(v.get(0)) || u.get(1).equals(v.get(1)) || u.get(0).equals(v.get(1)) || u.get(1).equals(v.get(0))){
					return true;
				}else{
					if (ray.intersectWhere(v, loc)){						
						if (loc.distance(u.get(0)) < acceptableDistance){
							return true;
						}
					}
				}
				
			}
		}

		return false;
	}

	public float[] intersectsWhere(Ray ray) {
				
		float[] result = {0f,0f,0f};
		
		Spatial shapeSpatial = (Spatial)shape.getImplementationObject();
		ArrayList<Vector3f> collisions = new ArrayList<Vector3f>();
		ArrayList<Triangle> triangles = new ArrayList<Triangle>();
		IntersectionRecord record = shapeSpatial.getWorldBound().intersectsWhere(ray);
		if(record != null && record.getQuantity() > 0){
		
			for (Triangle t: triangleWall){
				Vector3f loc = new Vector3f();
				if (ray.intersectWhere(t, loc)){					
					collisions.add(loc);
					triangles.add(t);
				}
			}
			if (collisions.size() > 0){
				float closestDistance = collisions.get(0).distance(ray.origin);
				result = SpatialUtility.getCollisionWallStats(triangles.get(0).get(0), triangles.get(0).get(1), collisions.get(0), shapeSpatial);
				for (int i = 1; i < collisions.size(); i++){
					float distance = collisions.get(i).distance(ray.origin);
					if (distance < closestDistance){						
						result = SpatialUtility.getCollisionWallStats(triangles.get(i).get(0), triangles.get(i).get(1), collisions.get(i), shapeSpatial);
						
						closestDistance = distance;
					}
				}			
				
				return result;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}

	public void establishTriangleWall() {
		triangleWall = SpatialUtility.createTriangleWall((Spatial)shape.getImplementationObject());
	}

}
