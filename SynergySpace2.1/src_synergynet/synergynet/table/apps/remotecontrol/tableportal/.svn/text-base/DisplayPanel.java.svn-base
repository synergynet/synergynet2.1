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



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.Frame;
import synergynet.contentsystem.items.OrthoContentItem;
import synergynet.contentsystem.items.Window;
import synergynet.contentsystem.items.listener.BringToTopListener;
import synergynet.table.apps.remotecontrol.networkmanager.managers.syncmanager.SyncRenderer;
import synergynet.table.apps.remotecontrol.tableportal.TablePortal.OperationMode;

public class DisplayPanel {
	
	protected TablePortal portal;
	protected Window window;
	protected SyncRenderer syncRenderer;
	protected ContentSystem contentSystem;
	protected Map<String, ContentItem> onlineItemsList = new HashMap<String, ContentItem>();

	
	public DisplayPanel(ContentSystem contentSystem, TablePortal portal){
		this.contentSystem = contentSystem;
		this.portal = portal;
		syncRenderer = new SyncRenderer();
		window = (Window) contentSystem.createContentItem(Window.class);
		window.getBackgroundFrame().setVisible(false);
		window.getBackgroundFrame().setRotateTranslateScalable(false);

	}
	
	public Window getWindow(){
		return window;
	}

	public void registerContentItems(List<ContentItem> items) {
		for (ContentItem item:items){
			registerContentItem(item);
			if(item instanceof Frame){
				((Frame)item).init();
			}
		}
	}
	
	public void registerContentItem(ContentItem item) {
		boolean isVisible = item.isVisible();
		System.out.println("EEEEEEEEEEEEE = "+isVisible);
		if(!onlineItemsList.containsKey(portal.id+"@"+portal.remoteTableId+"@"+item.getName())){
			contentSystem.addContentItem(item);
			String name = item.getName();
			if(item.getName().contains("@")) name = name.substring(name.lastIndexOf("@"));
			item.setName(portal.id+"@"+portal.remoteTableId+"@"+item.getName());
			window.addSubItem(item);
			if(item instanceof OrthoContentItem) ((OrthoContentItem)item).setLocation(((OrthoContentItem)item).getLocalLocation());
			onlineItemsList.put(item.getName(),item);
			portal.syncManager.addSyncListeners(item);
			this.getWindow().setOrder(9999);
			if(((OrthoContentItem)item).isRotateTranslateScaleEnabled()) ((OrthoContentItem)item).setRotateTranslateScalable(true, false, this.getWindow().getBackgroundFrame());
			if(((OrthoContentItem)item).isBringToTopEnabled()) ((OrthoContentItem)item).addBringToTopListener(new BringToTopListener(){

				@Override
				public void itemBringToToped(ContentItem item) {
					DisplayPanel.this.getWindow().setTopItem(item);
					DisplayPanel.this.getWindow().setOrder(9999);
				}
			});
			
			WorkspaceManager.getInstance().addWorkspaceListener(item);
			portal.cullManager.registerItemForClipping(item);
			item.setVisible(isVisible);
		}
	}

	public void unregisterAllItems() {
		for(ContentItem item: onlineItemsList.values()){	
			portal.syncManager.unregisterContentItem(item);
			WorkspaceManager.getInstance().unregisterContentItem(item);
			window.removeSubItem(item);
		}
		onlineItemsList.clear();
	}

	public void unregisterItem(String itemName) {
		ContentItem contentItem = onlineItemsList.remove(itemName);
		window.removeSubItem(contentItem);
	}
	
	protected void syncContent(Map<String, Map<Short, Object>> itemSyncDataMap) {
		if (onlineItemsList.isEmpty()) return;
			for (String name:itemSyncDataMap.keySet()){
				if (!onlineItemsList.containsKey(portal.id+"@"+portal.remoteTableId + "@" + name))	continue;
				OrthoContentItem item = (OrthoContentItem)onlineItemsList.get(portal.id+"@"+portal.remoteTableId + "@" + name);
				syncRenderer.renderSyncData(item, itemSyncDataMap.get(name));
			}
	}

	protected void setMode(OperationMode mode) {
		for(ContentItem item: onlineItemsList.values()){	
			if(mode == OperationMode.WRITE){
				((OrthoContentItem)item).setRotateTranslateScalable(true, false, this.getWindow().getBackgroundFrame());
				((OrthoContentItem)item).setBringToTopable(true);
				((OrthoContentItem)item).addBringToTopListener(new BringToTopListener(){

					@Override
					public void itemBringToToped(ContentItem item) {
						DisplayPanel.this.getWindow().setTopItem(item);
					}
				});

			}else{
				((OrthoContentItem)item).setRotateTranslateScalable(false);
				((OrthoContentItem)item).setBringToTopable(false);
				((OrthoContentItem)item).removeBringToTopListeners();
			}
		}
	}
}
