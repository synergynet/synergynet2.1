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

package synergynet.services.networkflick;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.OrthoContentItem;
import synergynet.services.net.localpresence.TableIdentity;
import synergynet.services.networkflick.messages.configmessages.AnnounceTableListMessage;
import synergynet.services.networkflick.messages.configmessages.UnregisterTableMessage;
import synergynet.services.networkflick.messages.createmessages.TransferableContentItem;
import synergynet.services.networkflick.utility.TableInfo;
import synergynet.services.networkflick.utility.VirtualTable;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergyspace.jme.cursorsystem.flicksystem.FlickMover;
import synergyspace.jme.cursorsystem.flicksystem.FlickSystem;
import synergyspace.jme.sysutils.BorderUtility;
import synergyspace.net.objectmessaging.Client;


import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.intersection.IntersectionRecord;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;
import com.jme.util.GameTaskQueueManager;

public class TransferController
{
	private static final Logger log = Logger.getLogger(TransferController.class.getName());
	
	protected ArrayList<VirtualTable> virtualTables  = new ArrayList<VirtualTable>();
	protected TableInfo localTableInfo = null;
	protected Client client;
	protected DefaultSynergyNetApp app;

	private ContentSystem content;
	private Node orthoNode;

	public TransferController(DefaultSynergyNetApp app){
		this.app = app;
		this.content = ContentSystem.getContentSystemForSynergyNetApp(app);
		this.orthoNode = app.getOrthoNode();
		BorderUtility.disableScreenBoundary();
		
		log.info("Transfer controller created.");
	}

	public void setLocalTableInfo(TableInfo localTableInfo){
		this.localTableInfo = localTableInfo;
	}

	public TableInfo getLocalTableInfo(){
		return localTableInfo;
	}

	public boolean isDestinationTableAvailable(Spatial s, Vector3f direction){
		Ray ray = new Ray(s.getLocalTranslation(), direction);
		for(VirtualTable virtualTable : virtualTables){
			IntersectionRecord record = virtualTable.getWorldBound().intersectsWhere(ray);
			if(record != null && record.getQuantity() > 0)
				return true;
		}
		return false;
	}

	public void applyTransferableContentItem(TransferableContentItem message){
		ContentItem item = message.getContentItem();
		item.setContentSystem(content);
		content.addContentItem(item);
		((OrthoContentItem)item).setRotateTranslateScalable(true);
		((OrthoContentItem)item).makeFlickable(message.getDeceleration());
		float rx = FastMath.cos(localTableInfo.getAngle()) * message.getLinearVelocityX() + FastMath.sin(localTableInfo.getAngle()) *  message.getLinearVelocityY();
		float ry = FastMath.cos(localTableInfo.getAngle()) * message.getLinearVelocityY() - FastMath.sin(localTableInfo.getAngle()) *  message.getLinearVelocityX();
		((OrthoContentItem)item).flick(rx, ry, message.getDeceleration());

		log.info("Apply transferable content item: "+item.getClass().getName()+"-"+item.getName());
		
	}

	public void registerTableList(AnnounceTableListMessage msg ){
		ArrayList<TableInfo> tablesInfo = msg.getTablesInfo();
		for(TableInfo tableInfo : tablesInfo)
			registerRemoteTable(tableInfo);
	}

	public void registerRemoteTable(final TableInfo remoteTableInfo)	{
		VirtualTable temp = findTableById(remoteTableInfo.getTableId());
		if(temp != null) virtualTables.remove(temp);
		if(remoteTableInfo.getTableId().equals(localTableInfo.getTableId())) return;
        GameTaskQueueManager.getManager().update(new Callable<Object>() {
	    public Object call() throws Exception {
			VirtualTable virtualTable = new VirtualTable(remoteTableInfo);
			virtualTable.setModelBound(new OrthogonalBoundingBox());
			virtualTable.updateModelBound();
			Node pivotNode = new Node("Pivot_"+virtualTable.getTableId().toString());
			pivotNode.setLocalTranslation(localTableInfo.getWidth()/2, localTableInfo.getHeight()/2,0);
			pivotNode.attachChild(virtualTable);
			Vector2f remoteTablePos = new Vector2f(remoteTableInfo.getTablePositionX(), remoteTableInfo.getTablePositionY());
			Vector2f localTablePos = new Vector2f(localTableInfo.getTablePositionX(), localTableInfo.getTablePositionY());
 			Vector2f relativePosition = remoteTablePos.subtract(localTablePos);
 			virtualTable.setLocalTranslation(relativePosition.x, relativePosition.y,0);
 			Quaternion q = new Quaternion();
    		q.fromAngleAxis(remoteTableInfo.getAngle() ,new Vector3f(0,0,1).normalize());
    		virtualTable.setLocalRotation(q);
 			q = new Quaternion();
    		q.fromAngleAxis(-localTableInfo.getAngle() ,new Vector3f(0,0,1).normalize());
    		pivotNode.setLocalRotation(q);
    		pivotNode.setCullHint(CullHint.Always);
    		if(!checkLocationConflict(virtualTable)){
       			orthoNode.attachChild(pivotNode);
    			virtualTables.add(virtualTable);
    			log.info("Register remote table-"+remoteTableInfo.getTableId().toString()+" for flicking items");
    		}
			return null;
	    }});
	}

	public void cleanUpUnregisteredTable(UnregisterTableMessage msg){

		if(msg.getTableId().equals(localTableInfo.getTableId())){
			for(VirtualTable virtualTable : virtualTables)
				detachVirtualTable(virtualTable);
			virtualTables.clear();
			localTableInfo = null;
		}
		else{
			VirtualTable virtualTable = findTableById(msg.getTableId());
			if(virtualTable != null){
				virtualTables.remove(virtualTable);
				detachVirtualTable(virtualTable);
			}
		}
		
		log.info("Unregister remote table-"+msg.getTableId().toString()+" from flicking operation");

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

	public void setClient(Client client){
		this.client = client;
	}

	private VirtualTable findTableById(TableIdentity tableId){
		for(VirtualTable table : virtualTables)
			if(table.getTableId().equals(tableId))
				return table;
		return null;
	}

	public void update(){
		Iterator<FlickMover> iter = FlickSystem.getInstance().getMovingElements().iterator();
		while(iter.hasNext()){
			FlickMover fm = iter.next();
			Spatial s = fm.getTargetSpatial();
			if(!fm.elementReleased() || virtualTables.isEmpty() || !isDestinationTableAvailable(s, fm.getLinearVelocity().normalize())){
				fm.bounce();
			}
			else{
				Vector3f pos = new Vector3f();
				for(VirtualTable table : virtualTables){
					ContentItem item = content.getContentItem(s.getName());
					if(s.hasCollision(table, true)){
						FlickSystem.getInstance().getMovingElements().remove(fm);
						table.worldToLocal(s.getLocalTranslation(), pos);
						pos.addLocal(table.getWidth()/2, table.getHeight()/2,0);
						item.setLocalLocation(pos.x, pos.y);
						TransferableContentItem msg = new TransferableContentItem();
						msg.setContentItem(item);
						msg.setDeceleration(fm.getDeceleration());
						float rx = FastMath.cos(localTableInfo.getAngle()) * fm.getLinearVelocity().x - FastMath.sin(localTableInfo.getAngle()) *  fm.getLinearVelocity().y;
						float ry = FastMath.cos(localTableInfo.getAngle()) * fm.getLinearVelocity().y + FastMath.sin(localTableInfo.getAngle()) *  fm.getLinearVelocity().x;
						msg.setLinearVelocity(rx, ry);
						msg.setSourceTableId(localTableInfo.getTableId());
						msg.setTargetTableId(table.getTableId());
						content.removeContentItem(item);
						try {
							client.sendTCP(msg);
						} catch (IOException e) {
							log.warning(e.toString());
						}
					}
				}
			}
		}
	}

	private boolean checkLocationConflict(VirtualTable virtualRemoteTable) {
		VirtualTable virtualLocalTable = new VirtualTable(localTableInfo);
		virtualLocalTable.setModelBound(new OrthogonalBoundingBox());
		virtualLocalTable.updateModelBound();
		if(virtualLocalTable.hasCollision(virtualRemoteTable, true)){
			log.info("***** Conflict position *****");
			return true;
		}
		return false;
	}
}
