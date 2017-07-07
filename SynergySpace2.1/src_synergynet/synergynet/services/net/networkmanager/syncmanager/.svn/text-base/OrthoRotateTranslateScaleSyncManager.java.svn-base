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

package synergynet.services.net.networkmanager.syncmanager;

import java.util.HashMap;
import java.util.Map;

import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.OrthoContentItem;
import synergynet.contentsystem.items.listener.BringToTopListener;
import synergynet.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener;
import synergynet.contentsystem.items.utils.Location;

public class OrthoRotateTranslateScaleSyncManager extends SyncManager{

	public static final short SYNC_ITEM_ANGLE = 1;
	public static final short SYNC_ITEM_SCALE = 2;
	public static final short SYNC_ITEM_LOCATION_X = 3;
	public static final short SYNC_ITEM_LOCATION_Y = 4;
	public static final short SYNC_ITEM_LOCATION_Z = 5;
	public static final short SYNC_ITEM_SORTORDER = 6;
	


	public OrthoRotateTranslateScaleSyncManager(){	}
	
	
	public void addSyncListeners(ContentItem item){
		OrthoContentItem orthoItem = (OrthoContentItem)item;
		orthoItem.addBringToTopListener(new BringToTopListener(){
			@Override
			public void itemBringToToped(ContentItem item) {
				OrthoRotateTranslateScaleSyncManager.this.addSyncData(item, SYNC_ITEM_SORTORDER, String.valueOf(1000));
			}
		});
		orthoItem.addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleListener(){

			@Override
			public void itemRotated(ContentItem item, float newAngle, float oldAngle) {
				OrthoRotateTranslateScaleSyncManager.this.addSyncData(item, SYNC_ITEM_ANGLE, newAngle);
			}

			@Override
			public void itemScaled(ContentItem item, float newScaleFactor,
					float oldScaleFactor) {
				OrthoRotateTranslateScaleSyncManager.this.addSyncData(item, SYNC_ITEM_SCALE, newScaleFactor);
			}

			@Override
			public void itemTranslated(ContentItem item,
					float newLocationX, float newLocationY,
					float oldLocationX, float oldLocationY) {
				OrthoRotateTranslateScaleSyncManager.this.registerItem(item);
				Location loc = new Location(newLocationX, newLocationY,0);
				if(((OrthoContentItem)item).getParent() != null) loc = ((OrthoContentItem)item).getLocation();
				OrthoRotateTranslateScaleSyncManager.this.addSyncData(item, SYNC_ITEM_LOCATION_X, loc.x);
				OrthoRotateTranslateScaleSyncManager.this.addSyncData(item, SYNC_ITEM_LOCATION_Y, loc.y);
			}
		});
	
	}
	

	
	protected void registerItem(ContentItem item){
		if (syncData.get(item.getName())==null){
			syncData.put(item.getName(), new HashMap<Short, Object>());
		}
	}
	
	
	@Override
	public void renderSyncData( ContentItem item, Map<Short, Object> itemAttrs) {
		//set the item with scale
		float scale;
		if (itemAttrs.containsKey(SYNC_ITEM_SCALE)){
			if(dataMasks.containsKey(SYNC_ITEM_SCALE)){
				scale = dataMasks.get(SYNC_ITEM_SCALE);
			}else{
				scale = (Float)itemAttrs.get(SYNC_ITEM_SCALE);
			}
			item.setScale(scale);
		}
		
		//set the item with angle
		float angle;
		if (itemAttrs.containsKey(SYNC_ITEM_ANGLE)){
			if(dataMasks.containsKey(SYNC_ITEM_ANGLE)){
				angle = dataMasks.get(SYNC_ITEM_ANGLE);
			}else{
				angle = (Float)itemAttrs.get(SYNC_ITEM_ANGLE);
			}
			item.setAngle(angle);  
		}
		
		//location
		float locationX = 0;
		float locationY = 0;
		if (itemAttrs.containsKey(SYNC_ITEM_LOCATION_X)){
			locationX = (Float)itemAttrs.get(SYNC_ITEM_LOCATION_X) ;
			((OrthoContentItem)item).setLocation(locationX, ((OrthoContentItem)item).getLocation().getY());
		}
					
		if (itemAttrs.containsKey(SYNC_ITEM_LOCATION_Y)){
			locationY = (Float)itemAttrs.get(SYNC_ITEM_LOCATION_Y);
			((OrthoContentItem)item).setLocation(((OrthoContentItem)item).getLocation().getX(), locationY);
		}
		
		//set z-order
		if (itemAttrs.containsKey(SYNC_ITEM_SORTORDER)){
			if (item!=null && ((OrthoContentItem)item).getParent() != null){				
				((OrthoContentItem)item).getParent().setTopItem(item);
			}else if(item != null && ((OrthoContentItem)item).getParent() == null){
				((OrthoContentItem)item).setAsTopObject();
			}
		}
		
	}



	@Override
	public void removeSyncListeners(ContentItem item) {
		((OrthoContentItem)item).removeBringToTopListeners();
		((OrthoContentItem)item).removeOrthoControlPointRotateTranslateScaleListeners();
	}

}
