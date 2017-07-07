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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.ImageTextLabel;
import synergynet.contentsystem.items.OrthoContentItem;
import synergynet.contentsystem.items.PPTViewer;
import synergynet.contentsystem.items.VideoPlayer;
import synergynet.contentsystem.items.utils.Location;
import synergynet.services.net.localpresence.TableIdentity;
import synergynet.services.net.networkedcontentmanager.NetworkedContentManager;
import synergynet.services.net.networkedcontentmanager.messages.networkedflick.RegisterTableMessage;
import synergynet.services.net.networkedcontentmanager.messages.networkedflick.TransferableContentItem;
import synergynet.services.net.networkedcontentmanager.messages.networkedflick.UnregisterTableMessage;
import synergynet.services.net.tablecomms.client.TableCommsClientService;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergyspace.jme.cursorsystem.flicksystem.FlickMover;
import synergyspace.jme.cursorsystem.flicksystem.FlickSystem;
import synergyspace.jme.sysutils.BorderUtility;
import synergyspace.jme.sysutils.SpatialUtility;

import com.jme.math.FastMath;
import com.jme.math.Ray;
import com.jme.math.Triangle;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;

public class TransferController
{
	
	private static final Logger log = Logger.getLogger(TransferController.class.getName());
	
	protected ArrayList<VirtualTable> virtualTables  = new ArrayList<VirtualTable>();
	protected TableInfo localTableInfo = null;
	protected TableCommsClientService comms;
	protected DefaultSynergyNetApp app;

	private ContentSystem content;
	private Node orthoNode;

	private NetworkedContentManager networkedContentManager;

	public TransferController(DefaultSynergyNetApp app, TableCommsClientService comms,NetworkedContentManager networkedContentManager){
		this.app = app;
		this.content = ContentSystem.getContentSystemForSynergyNetApp(app);
		this.orthoNode = app.getOrthoNode();
		this.comms = comms;
		this.networkedContentManager = networkedContentManager;
		BorderUtility.disableScreenBoundary();
		log.info("Transfer controller created.");
	}

	public void setLocalTableInfo(TableInfo localTableInfo){
		this.localTableInfo = localTableInfo;
	}

	public TableInfo getLocalTableInfo(){
		return localTableInfo;
	}
	
	private float[] arrivalLocationStats;
	private VirtualTable targetTable;

	public boolean isDestinationTableAvailable(Spatial s, FlickMover fm){
		
		Ray ray = new Ray(s.getLocalTranslation(), fm.getLinearVelocity().normalize());
		
    	if(FlickSystem.getInstance().getBouncers().contains(BorderUtility.getBorderSpatial())){
    		
	    	ArrayList<Triangle> borderTriangles = BorderUtility.getBorderWall();
			int count = 0;
			
			for (Triangle t: borderTriangles)if (ray.intersect(t))count++;
	    
			if(count>1)return false;
    	}
		
		for(VirtualTable virtualTable : virtualTables){
			float[] record = virtualTable.intersectsWhere(ray);
			if(record != null)	
				if (withinStoppingDistance(s, fm)){
					targetTable = virtualTable;
					arrivalLocationStats = record;
					return true;
				}
		}
		return false;
	}

	private boolean withinStoppingDistance(Spatial s, FlickMover fm) {
		
		float maxDimension = SpatialUtility.getMaxDimension(s);
		
		float stoppingDistance = -fm.getSpeed()*2/(2*-fm.getDeceleration());
				
		if (stoppingDistance > maxDimension * 3){
			return true;
		}else{
			return false;
		}
	}
	
	public ContentItem applyTransferableContentItem(TransferableContentItem message){
		ContentItem item = message.getContentItem();
		item.name = content.generateUniqueName();
		content.addContentItem(item);
		item.setName(content.generateUniqueName());
		item.init();
		item.setScale(message.getScale() / (DisplaySystem.getDisplaySystem().getWidth()/1024f));
		//TODO:  This works for now but should be replaced with something more dynamic in the future
		try {
			if(item instanceof ImageTextLabel){
				
				ImageTextLabel itemImplementation = (ImageTextLabel)item;
				
				String address = toLocalString(itemImplementation.getImageInfo().getImageResource().toString());
				
				URL add = new URL(address);
				itemImplementation.setImageInfo(add);						
				
			}else if(item instanceof VideoPlayer){
				
				VideoPlayer itemImplementation = (VideoPlayer)item;
				double time = itemImplementation.getVideoTime();
				String address = toLocalString(itemImplementation.getVideoURL().toString());
				
				URL add = new URL(address);
				itemImplementation.setVideoURL(add);						
				
				itemImplementation.setVideoTime(time);
				
			}else if(item instanceof PPTViewer){
				
				PPTViewer itemImplementation = (PPTViewer)item;
				int page = itemImplementation.getCurrentPageIndex();
				String address = toLocalString(itemImplementation.getPPTFile().toString());
				
				URL add = new URL(address);
				itemImplementation.setPPTFile(add);		
				itemImplementation.gotoPage(page);			
			}
		} catch (Exception e) {
			log.warning(e.toString());
		}
				
		((OrthoContentItem)item).setRotateTranslateScalable(true);
		((OrthoContentItem)item).makeFlickable(message.getDeceleration());
		
		Spatial s = (Spatial)((OrthoContentItem)item).getImplementationObject();
		
		Vector2f rotatedFlick = new Vector2f(message.getLinearVelocityX(), message.getLinearVelocityY());
		rotatedFlick.rotateAroundOrigin(localTableInfo.getAngle(), false);
				
		FlickMover fm = FlickSystem.getFlickMover(s);
		if (fm != null){
			fm.setAutoFlicked(true);
		}			
			
		Vector3f collisionPosition = SpatialUtility.getCollisionPos(message.getLocationStats(), BorderUtility.getBorderSpatial());
			
		Vector3f direction = new Vector3f(-rotatedFlick.x, -rotatedFlick.y, 0);
			
		float directionDistance = FastMath.sqrt(FastMath.sqr(direction.x)+FastMath.sqr(direction.y));
		
		float scale = SpatialUtility.getMaxDimension(s)/directionDistance;
		
		float xChange = scale*direction.x;
		float yChange = scale*direction.y;
		
		Vector3f newPos = new Vector3f(collisionPosition.x + xChange, collisionPosition.y + yChange, 0);	
				
		((OrthoContentItem)item).setLocation(newPos.x, newPos.y);
		
		((OrthoContentItem)item).setAngle(((OrthoContentItem)item).getAngle() + (message.getRoation() + localTableInfo.getAngle()));	
		
		((OrthoContentItem)item).flick(rotatedFlick.x, rotatedFlick.y, message.getDeceleration());
		
		log.info("Apply transferable content item: "+item.getClass().getName()+"-"+item.getName());
		
		return item;
	}

	private String toLocalString(String s) throws Exception{
		String result = "";
		
		String localAddress = TransferController.class.getResource("TransferController.class").toString();
		
		String[] remoteAddParts = s.split("/bin/");
		String[] localAddParts = localAddress.split("/bin/");
			
		result = localAddParts[0] + "/bin/" + remoteAddParts[1];		
		
		return result;
	}
		
	public void registerRemoteTable(final TableInfo remoteTableInfo)	{
		VirtualTable temp = findTableById(remoteTableInfo.getTableId());
		if(temp != null) virtualTables.remove(temp);
		if(remoteTableInfo.getTableId().equals(localTableInfo.getTableId())) return;
		
		VirtualTable virtualTable = new VirtualTable(app, remoteTableInfo);
		generateLocalTable();
								
		Vector2f remoteTablePos = new Vector2f(remoteTableInfo.getTablePositionX(), remoteTableInfo.getTablePositionY());
		Vector2f localTablePos = new Vector2f(localTableInfo.getTablePositionX(), localTableInfo.getTablePositionY());
		Vector2f relativePosition = remoteTablePos.subtract(localTablePos);
		
		float rx = (relativePosition.x * FastMath.cos(localTableInfo.getAngle())) - (relativePosition.y * FastMath.sin(localTableInfo.getAngle()));
		float ry = (relativePosition.x * FastMath.sin(localTableInfo.getAngle())) + (relativePosition.y * FastMath.cos(localTableInfo.getAngle()));
		
		virtualTable.shape.setLocation(rx + (virtualTable.getWidth()/2), ry + (virtualTable.getHeight()/2));
				
		virtualTable.shape.setAngle(-(-localTableInfo.getAngle() + remoteTableInfo.getAngle()));
							
		virtualTable.establishTriangleWall();
		
		if(!checkLocationConflict(virtualTable)){
			virtualTables.add(virtualTable);
			log.info("Register remote table-"+remoteTableInfo.getTableId().toString()+" for flicking items");
		}

	}

	public void cleanUpUnregisteredTable(UnregisterTableMessage msg){

		if(msg.getSender().equals(localTableInfo.getTableId())){
			for(VirtualTable virtualTable : virtualTables)
				detachVirtualTable(virtualTable);
			virtualTables.clear();
			localTableInfo = null;
		}
		else{
			VirtualTable virtualTable = findTableById(msg.getSender());
			if(virtualTable != null){
				virtualTables.remove(virtualTable);
				detachVirtualTable(virtualTable);
			}
		}
		
		log.info("Unregister remote table-"+msg.getSender().toString()+" from flicking operation");

	}

	private void detachVirtualTable(final VirtualTable table){
        GameTaskQueueManager.getManager().update(new Callable<Object>() {
  		    public Object call() throws Exception {
  		    		if(orthoNode != null)
  		    			orthoNode.detachChildNamed("Pivot_"+ table.getTableId().toString());
  		    	return null;
  		    }
        });
	}

	private VirtualTable findTableById(TableIdentity tableId){
		for(VirtualTable table : virtualTables)
			if(table.getTableId().equals(tableId))
				return table;
		return null;
	}
	
	private ArrayList<Spatial> toBeTransferred = new ArrayList<Spatial>();
	
	public void update(){
		Iterator<FlickMover> iter = FlickSystem.getInstance().getMovingElements().iterator();
		while(iter.hasNext()){
			FlickMover fm = iter.next();
			Spatial s = fm.getTargetSpatial();
			if(BorderUtility.getBorderSpatial().getName().equals("default") && !toBeTransferred.contains(fm.getTargetSpatial())){
				if (s.getWorldTranslation().x > DisplaySystem.getDisplaySystem().getWidth() || s.getWorldTranslation().x < 0 ||
						s.getWorldTranslation().y > DisplaySystem.getDisplaySystem().getHeight() || s.getWorldTranslation().y < 0){
					fm.simpleRectangleBounce();
				}
			}
			if (s.hasCollision(BorderUtility.getBorderSpatial(), true)){
				if((!networkedContentManager.getNetworkedFlickController().isFlickEnabled() || 
						virtualTables.isEmpty() || !isDestinationTableAvailable(s, fm)) && !toBeTransferred.contains(fm.getTargetSpatial())){
					fm.bounce();
				}else{
					if (networkedContentManager.getNetworkedFlickController().isFlickEnabled() && !virtualTables.isEmpty() && !fm.isIgnoring()){
												
						if (!toBeTransferred.contains(fm.getTargetSpatial())){
	
							fm.toBeTransferred = true;
							toBeTransferred.add(fm.getTargetSpatial());
							
							float[] thisArrivalLocation = {arrivalLocationStats[0],arrivalLocationStats[1], arrivalLocationStats[2]};
							
							VirtualTable vt = new VirtualTable(targetTable);
							Runnable r = new CustomThread(fm, thisArrivalLocation, vt);
							new Thread(r).start();
						}
	
					}			
	
				}
			}else{
				if (fm.isIgnoring())fm.noLongerIgnoring();
			}
		}
	}

	public void sendRegistrationMessage(TableIdentity targeTableId) {
		try {
			for(Class<?> receiverClass: networkedContentManager.getReceiverClasses()){
				comms.sendMessage(new RegisterTableMessage(receiverClass,localTableInfo, targeTableId));
				log.info("Send table registration message to table-"+targeTableId.toString()+" for flicking operation.");
			}
		} catch (IOException e) {
			log.info(e.toString());
		}
	}
	
	private VirtualTable virtualLocalTable;

	private void generateLocalTable() {
		virtualLocalTable = new VirtualTable(app, localTableInfo);
		virtualLocalTable.shape.setLocalLocation(new Location(DisplaySystem.getDisplaySystem().getWidth()/2, DisplaySystem.getDisplaySystem().getHeight()/2, 0));
		virtualLocalTable.establishTriangleWall();
	}
	
	private boolean checkLocationConflict(VirtualTable virtualRemoteTable) {
		if(virtualLocalTable.hasTableCollision(virtualRemoteTable)){
			log.info("***** Conflict position *****");
			return true;
		}
		return false;
	}
	
	public void transfer(FlickMover fm, Spatial s, float[] thisArrivalLocationStats, VirtualTable table) {		

		if (fm.toBeTransferred && (fm.getTargetSpatial().hasCollision(BorderUtility.getBorderSpatial(), true) || fm.isOutsideEnvironment()) 
				&& !fm.isIgnoring() && !fm.isAutoFlicked()){
		
			ContentItem item = content.getContentItem(s.getName());
			
			((OrthoContentItem)item).makeUnflickable();
						
			Vector2f rotatedFlick = new Vector2f(fm.getLinearVelocity().x, fm.getLinearVelocity().y);
			rotatedFlick.rotateAroundOrigin(localTableInfo.getAngle(), true);
			
			for(Class<?> receiverClass: networkedContentManager.getReceiverClasses()){
				TransferableContentItem msg = new TransferableContentItem(receiverClass, item, table.getTableId());
				msg.setDeceleration(fm.getDeceleration());
				msg.setLinearVelocity(rotatedFlick.x, rotatedFlick.y);
				msg.setLocationStats(thisArrivalLocationStats);
				msg.setRoation(-localTableInfo.getAngle());
				msg.setScale((((OrthoContentItem)item).getScale() * (DisplaySystem.getDisplaySystem().getWidth()/1024f)));
				networkedContentManager.sendMessage(msg);
				log.info("Send flicked item: "+item.getClass().getName()+"-"+item.getName()+" to table-"+table.getTableId().toString());
			}
			networkedContentManager.removeContentItem(item);
			toBeTransferred.remove(fm.getTargetSpatial());
			
		
		}else{
			toBeTransferred.remove(fm.getTargetSpatial());
		}
		
	}
	
	public class CustomThread implements Runnable {

		   private FlickMover fm;
		   private float[] thisArrivalLocationStats;
		   private Vector3f initialPos;
		   private VirtualTable vt;
		   private float maxDistance;
		   	   
		   public CustomThread(FlickMover fm, float[] thisArrivalLocationStats, VirtualTable vt) {
			   this.vt = vt;
			   setup(fm, thisArrivalLocationStats);
		   }
		
		   private void setup(FlickMover fm, float[] thisArrivalLocationStats) {
		       this.fm = fm;
		       this.thisArrivalLocationStats = thisArrivalLocationStats;
		       this.maxDistance = SpatialUtility.getMaxDimension(fm.getTargetSpatial());
		       this.maxDistance *= 1.5;
		       this.initialPos = new Vector3f(fm.getTargetSpatial().getWorldTranslation().x, fm.getTargetSpatial().getWorldTranslation().y, 0);	
		   }
		   

			public void run() {
				   
				   while (fm.getTargetSpatial().getWorldTranslation().distance(initialPos) < maxDistance){	 
					   try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				    }
				   transfer(fm, fm.getTargetSpatial(), thisArrivalLocationStats, vt);
					  
			   }
		}


}
