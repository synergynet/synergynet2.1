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

package synergynet.table.apps.mathpadapp.controllerapp.tablecontroller;

import java.util.ArrayList;
import java.util.List;

import com.jme.system.DisplaySystem;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.OrthoContentItem;
import synergynet.contentsystem.items.Window;
import synergynet.services.net.localpresence.TableIdentity;
import synergynet.table.apps.mathpadapp.conceptmapping.GraphLink;
import synergynet.table.apps.mathpadapp.conceptmapping.GraphNode;
import synergynet.table.apps.mathpadapp.networkmanager.managers.ControllerManager;
import synergynet.table.apps.mathpadapp.networkmanager.managers.NetworkedContentManager;
import synergynet.table.apps.mathpadapp.networkmanager.managers.remotedesktopmanager.RemoteDesktopManager;
import synergynet.table.apps.mathpadapp.networkmanager.managers.syncmanager.SyncRenderer;

public abstract class RemoteDesktop extends GraphNode{
	protected Window desktopWindow;
	protected RemoteDesktopManager remoteDesktopManager;
	protected TableIdentity tableId;
	protected NetworkedContentManager networkManager;
	protected List<ContentItem> onlineItems = new ArrayList<ContentItem>();
	protected SyncRenderer syncRenderer;
	
	public RemoteDesktop(final ContentSystem contentSystem, final NetworkedContentManager networkManager){
		super(((ControllerManager)networkManager).getGraphManager(), (Window) contentSystem.createContentItem(Window.class));
		desktopWindow = (Window)this.getNodeItem();
		this.networkManager = networkManager;
		ControllerManager controllerManager = (ControllerManager) networkManager;
		this.createSyncRenderer();
		this.remoteDesktopManager = controllerManager.getRemoteDesktopManager();
		this.contentSystem = networkManager.getContentSystem();
		
		desktopWindow.setWidth((int)(DisplaySystem.getDisplaySystem().getWidth()));
		desktopWindow.setHeight((int)(DisplaySystem.getDisplaySystem().getHeight()));
		
		desktopWindow.setLocalLocation(DisplaySystem.getDisplaySystem().getWidth()/2,DisplaySystem.getDisplaySystem().getHeight()/2);
		desktopWindow.setOrder(-1);
		
		desktopWindow.setLocalLocation(0,0);
		this.addConceptMapListener(new ConceptMapListener(){
			@Override
			public void nodeConnected(GraphLink link) {
				//if(!(link.getTargetNode() instanceof ProjectorNode)){
				//	RemoteDesktop.this.graphManager.detachGraphLink(link);
				//	return;
				//}
				//link.setArrowMode(LineItem.ARROW_TO_TARGET);
			}
			@Override
			public void nodeDisconnected(GraphLink link) {}
		});
	}
	
	public TableIdentity getTableId() {
		return tableId;
	}

	public void setTableId(TableIdentity tableId) {
		this.tableId = tableId;
	}

	public Window getDesktopWindow() {
		return desktopWindow;
	}
	
	public void updateNode(){
		this.updateConnectionPoints();
	}
	
	public abstract void createSyncRenderer();
	
	public SyncRenderer getSyncRenderer(){
		return syncRenderer;
	}
	
	public void setSyncRenderer(SyncRenderer syncRenderer){
		this.syncRenderer = syncRenderer;
	}

	public void addOnlineItem(ContentItem item){
		this.getDesktopWindow().addSubItem(item);
		if(item instanceof OrthoContentItem){
			((OrthoContentItem)item).setLocation(((OrthoContentItem)item).getLocalLocation());
			((OrthoContentItem)item).setRotateTranslateScalable(false);
		}
		
	}
	
	public void addOnlineItems(List<ContentItem> items){
		for(ContentItem item: items){
			this.getDesktopWindow().addSubItem(item);
			if(item instanceof OrthoContentItem) ((OrthoContentItem)item).setRotateTranslateScalable(false);
		}
	}
	
	public List<ContentItem> getOnlineItems() {
		return onlineItems;
	}

	public void removeOnlineItem(ContentItem item){
			this.desktopWindow.removeSubItem(item);
			onlineItems.remove(item);
	}
	
	public void removeOnlineItems(){
		for(ContentItem item: onlineItems)
			this.desktopWindow.removeSubItem(item);
		onlineItems.clear();
	}
}
