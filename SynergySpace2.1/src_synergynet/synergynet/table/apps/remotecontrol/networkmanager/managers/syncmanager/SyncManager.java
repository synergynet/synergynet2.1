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

package synergynet.table.apps.remotecontrol.networkmanager.managers.syncmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.OrthoContentItem;
import synergynet.contentsystem.items.SketchPad;
import synergynet.contentsystem.items.SketchPad.DrawListener;
import synergynet.contentsystem.items.listener.BringToTopListener;
import synergynet.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener;
import synergynet.contentsystem.items.utils.Location;
import synergynet.contentsystem.jme.items.utils.DrawData;
import synergynet.services.net.localpresence.TableIdentity;
import synergynet.table.apps.remotecontrol.networkmanager.managers.NetworkedContentManager;
import synergynet.table.apps.remotecontrol.networkmanager.messages.UnicastSyncDataPortalMessage;

public class SyncManager{

	public static final short SYNC_ITEM_ANGLE = 1;
	public static final short SYNC_ITEM_SCALE = 2;
	public static final short SYNC_ITEM_LOCATION_X = 3;
	public static final short SYNC_ITEM_LOCATION_Y = 4;
	public static final short SYNC_ITEM_LOCATION_Z = 5;
	public static final short SYNC_ITEM_SORTORDER = 6;
	public static final short SYNC_ITEM_DRAW_DATA = 7;
	public static final short SYNC_ITEM_CLEAR_PAD = 8;
	
	protected NetworkedContentManager networkManager;
	protected Map<String, Map<Short, Object>> syncData = new HashMap<String, Map<Short, Object>>();
	protected List<TableIdentity> syncTables = new ArrayList<TableIdentity>();

	public SyncManager(NetworkedContentManager networkManager){
		this.networkManager = networkManager;
	}
	
	public void registerSyncTable(TableIdentity tableId){
		if(!syncTables.contains(tableId)) syncTables.add(tableId);
	}
	
	public void unregisterSyncTable(TableIdentity tableId){
		syncTables.remove(tableId);
	}
	
	public void addSyncListeners(ContentItem item){

		OrthoContentItem orthoItem = (OrthoContentItem)item;
		orthoItem.addBringToTopListener(new BringToTopListener(){
			@Override
			public void itemBringToToped(ContentItem item) {
				this.registerItem(item);
				syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_SORTORDER, String.valueOf(1000));
				
			}
			
			private void registerItem(ContentItem item){
				if (syncData.get(item.getName())==null){
					syncData.put(item.getName(), new HashMap<Short, Object>());
				}
			}
			
		});
		orthoItem.addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleListener(){

			@Override
			public void itemRotated(ContentItem item, float newAngle,
					float oldAngle) {
				SyncManager.this.registerItem(item);
				syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_ANGLE, newAngle);	
			}

			@Override
			public void itemScaled(ContentItem item, float newScaleFactor,
					float oldScaleFactor) {
				SyncManager.this.registerItem(item);
				syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_SCALE, newScaleFactor);		

			}

			@Override
			public void itemTranslated(ContentItem item,
					float newLocationX, float newLocationY,
					float oldLocationX, float oldLocationY) {
				SyncManager.this.registerItem(item);
				Location loc = new Location(newLocationX, newLocationY,0);
				if(((OrthoContentItem)item).getParent() != null) loc = ((OrthoContentItem)item).getLocation();
				syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_LOCATION_X, loc.x);		
				syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_LOCATION_Y, loc.y);

			}
		});
		
		if(orthoItem instanceof SketchPad){
			final SketchPad pad = ((SketchPad)orthoItem);
			pad.addDrawListener(new DrawListener(){

				@SuppressWarnings("unchecked")
				@Override
				public void itemDrawn(DrawData drawData) {
					SyncManager.this.registerItem(pad);
					if(!syncData.get(pad.getName()).containsKey(SyncManager.SYNC_ITEM_DRAW_DATA))
						syncData.get(pad.getName()).put(SyncManager.SYNC_ITEM_DRAW_DATA, new ArrayList<DrawData>());
					((ArrayList<DrawData>)syncData.get(pad.getName()).get(SyncManager.SYNC_ITEM_DRAW_DATA)).add(drawData);
				}

				@Override
				public void padCleared() {
					SyncManager.this.registerItem(pad);
					syncData.get(pad.getName()).put(SyncManager.SYNC_ITEM_CLEAR_PAD, "clear");
				}
			});
		}
	}
	
	private void registerItem(ContentItem item){
		if (syncData.get(item.getName())==null){
			syncData.put(item.getName(), new HashMap<Short, Object>());
		}
	}
	
	public void unregisterContentItem(ContentItem item) {
		((OrthoContentItem)item).removeBringToTopListeners();
		((OrthoContentItem)item).removeOrthoControlPointRotateTranslateScaleListeners();
	}
	
	public void update() {
		if(!syncTables.isEmpty() && !syncData.isEmpty()){
			for(TableIdentity tableId: syncTables){
				for(Class<?> receiverClass: networkManager.getReceiverClasses()){
					UnicastSyncDataPortalMessage msg = new UnicastSyncDataPortalMessage(receiverClass, tableId, syncData);
					networkManager.sendMessage(msg);
				}
			}
			syncData.clear();
		}
	}

	public void updateSyncData() {
		for(ContentItem item: networkManager.onlineItemsList.values()){
			this.registerItem(item);
			syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_ANGLE, String.valueOf(item.getAngle()));
			syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_SCALE, String.valueOf(item.getScale()));
			Location loc = ((OrthoContentItem)item).getLocation();
			syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_LOCATION_X, String.valueOf(loc.x));		
			syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_LOCATION_Y, String.valueOf(loc.y));

		}
	}


	// Testing methods
	
	public void fireItemScaled(ContentItem item){
		SyncManager.this.registerItem(item);
		syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_SCALE, String.valueOf(item.getScale()));		
		
	}

	public void fireItemRotated(ContentItem item) {
		SyncManager.this.registerItem(item);
		syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_ANGLE, String.valueOf(item.getAngle()));	
	}

	public void fireItemMoved(ContentItem item) {
		SyncManager.this.registerItem(item);
		Location loc = new Location(item.getLocalLocation().x, item.getLocalLocation().y,0);
		if(((OrthoContentItem)item).getParent() != null) loc = ((OrthoContentItem)item).getLocation();
		syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_LOCATION_X, String.valueOf(loc.x));		
		syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_LOCATION_Y, String.valueOf(loc.y));

	}


}
