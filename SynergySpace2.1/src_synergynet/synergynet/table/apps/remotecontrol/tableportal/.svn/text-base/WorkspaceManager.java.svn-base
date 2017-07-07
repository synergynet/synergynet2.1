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
package synergynet.table.apps.remotecontrol.tableportal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jme.math.Vector2f;
import com.jme.scene.Spatial;

import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.OrthoContentItem;
import synergynet.contentsystem.items.listener.ScreenCursorListener;
import synergynet.contentsystem.items.utils.Location;
import synergynet.services.net.localpresence.TableIdentity;
import synergynet.table.apps.remotecontrol.networkmanager.managers.NetworkedContentManager;
import synergynet.table.apps.remotecontrol.networkmanager.managers.TablePortalManager;
import synergyspace.jme.mtinputbridge.MultiTouchInputFilterManager;
import synergyspace.jme.pickingsystem.IJMEMultiTouchPicker;
import synergyspace.jme.pickingsystem.PickSystemException;
import synergyspace.jme.pickingsystem.data.PickRequest;
import synergyspace.jme.pickingsystem.data.PickResultData;

public class WorkspaceManager{
	
	private IJMEMultiTouchPicker pickSystem;
	private NetworkedContentManager networkManager;
	public HashMap<String, Location> itemLoc = new HashMap<String, Location>();
	
	private static WorkspaceManager instance = new WorkspaceManager();
	
	public static WorkspaceManager getInstance() {
		synchronized(TablePortalManager.class) {
			if(instance == null) instance = new WorkspaceManager();
			return instance;
		}
	}
	
	public void setNetworkedContentManager(NetworkedContentManager networkManager){
		this.networkManager = networkManager;
	}
	
	private WorkspaceManager(){
		this.pickSystem = MultiTouchInputFilterManager.getInstance().getPickingSystem();
	}
	
	public void addWorkspaceListener(ContentItem item){
		((OrthoContentItem)item).addScreenCursorListener(new ScreenCursorListener(){

			@Override
			public void screenCursorChanged(ContentItem item, long id, float x,	float y, float pressure) {}

			@Override
			public void screenCursorClicked(ContentItem item, long id, float x,	float y, float pressure) {}

			@Override
			public void screenCursorPressed(ContentItem item, long id, float x,	float y, float pressure) {}

			@Override
			public void screenCursorReleased(ContentItem item, long id,	float x, float y, float pressure) {
				TablePortal sourcePortal = getSourceTablePortal(item);
				TablePortal targetPortal = getTargetTablePortal(item, id, x, y);
				TableIdentity sourceTableId, targetTableId;
				
				if(sourcePortal != null)
					sourceTableId = sourcePortal.getTableId();
				else
					sourceTableId = TableIdentity.getTableIdentity();
					
				if(targetPortal != null)
					targetTableId = targetPortal.getTableId();
				else
					targetTableId = TableIdentity.getTableIdentity();
				if(sourceTableId != null && targetTableId != null)
					networkManager.transferContentItem(item,sourceTableId, targetTableId, new Location(x,y,0));
			}
		});
	}
	
	private TablePortal getTargetTablePortal(ContentItem item, long id, float x, float y){
		List<Spatial> spatials = getPickedSpatials(id, new Vector2f(x,y));
		TablePortal sourcePortal = getSourceTablePortal(item);
		for(Spatial spatial: spatials){
			if(sourcePortal != null && sourcePortal.getWindow().getBackgroundFrame().getName().equals(spatial.getName())) return sourcePortal;
			for(TablePortal portal: TablePortalManager.getInstance().getTablePortals()){
				if(portal.getWindow().getBackgroundFrame().getName().equals(spatial.getName()))
					return portal;
			}
		}
		return null;
	}
	
	public TablePortal getSourceTablePortal(ContentItem item){
		if(!item.getName().contains("@")) return null;
		String[] strs = item.getName().split("@");
		if(strs.length <3) return null;
		return TablePortalManager.getInstance().getTablePortal(strs[0]);
	}
	
	private List<Spatial> getPickedSpatials(long id, Vector2f position)
	{
		PickRequest req = new PickRequest(id, position);
		List<PickResultData> pickResults;
		List<Spatial> pickedSpatials = new ArrayList<Spatial>();
		try {
				pickResults = pickSystem.doPick(req);
				for(PickResultData pr : pickResults) {
					pickedSpatials.add(pr.getPickedSpatial());
				}
			}
		catch (PickSystemException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return pickedSpatials;
	}
	
	public void unregisterContentItem(ContentItem item){
		((OrthoContentItem)item).removeScreenCursorListeners();
	}

}
