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

package synergynet.table.apps.mathpadapp.controllerapp.usercontroller;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.DropDownList;
import synergynet.contentsystem.items.OrthoContainer;
import synergynet.contentsystem.items.SimpleButton;
import synergynet.contentsystem.items.TextLabel;
import synergynet.contentsystem.items.DropDownList.DropDownListItem;
import synergynet.contentsystem.items.DropDownList.DropDownListListener;
import synergynet.contentsystem.items.listener.SimpleButtonListener;
import synergynet.services.net.localpresence.TableIdentity;
import synergynet.table.apps.mathpadapp.MathPadUtils;
import synergynet.table.apps.mathpadapp.clientapp.MathPadClient;
import synergynet.table.apps.mathpadapp.controllerapp.usercontroller.UserControllerWindow;
import synergynet.table.apps.mathpadapp.controllerapp.usercontroller.UserInfo;
import synergynet.table.apps.mathpadapp.controllerapp.usercontroller.UserInfo.UserStatus;
import synergynet.table.apps.mathpadapp.networkmanager.managers.ControllerManager;
import synergynet.table.apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.BlockUserMathPadMessage;
import synergynet.table.apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.HideUserMathPadMessage;
import synergynet.table.apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.RequestMathPadItemFromUserMessage;
import synergynet.table.apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.UnicastSyncWithUserMessage;
import synergynet.table.apps.mathpadapp.util.MTList;
import synergynet.table.apps.mathpadapp.util.MTListManager;

public class UserListControlPanel {
	
	protected OrthoContainer container;
	private MTListManager userPanelManager;
	public static final int controlUserPanelHeight = 140;
	private ControllerManager controllerManager;
	private DropDownList tableList;
	
	public UserListControlPanel(ContentSystem contentSystem, final MTList userPanelList, ControllerManager controllerManager){
		container  = (OrthoContainer) contentSystem.createContentItem(OrthoContainer.class);
		this.userPanelManager = userPanelList.getManager();
		this.controllerManager = controllerManager;
		
		TextLabel filter = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		filter.setBorderSize(0);
		filter.setBackgroundColour(Color.LIGHT_GRAY);
		filter.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		filter.setText("Filter By Table:");
		filter.setLocalLocation(-170, 23);
		container.addSubItem(filter);
		
		tableList = (DropDownList) contentSystem.createContentItem(DropDownList.class);
		tableList.setWidth(userPanelList.listItemWidth -100);
		tableList.setBorderColour(Color.black);
		tableList.setBorderSize(1);
		tableList.setItemHeight(25);

		tableList.addListItem("All","ALL");
		for(TableIdentity tableId: controllerManager.getOnlineTables()){
			if(!tableId.equals(TableIdentity.getTableIdentity())) tableList.addListItem(tableId.toString(), tableId.toString());
		}
		tableList.setSelectedItem(tableList.getListItems().get(0));
		tableList.addDropDownListListener(new DropDownListListener(){

			@Override
			public void itemSelected(DropDownListItem dropListItem) {
				if(dropListItem.getValue().equalsIgnoreCase("ALL")){
					for(ContentItem contentItem: userPanelManager.getListItems().values()){
						if(contentItem instanceof SimpleButton) ((SimpleButton)contentItem).setVisible(true);
					}
				}
				else{
					for(Object item: userPanelManager.getAllItems()){
						UserInfo userInfo = (UserInfo)item;
						if(userInfo.getTableIdentity().toString().equals(dropListItem.getValue()))
							userPanelManager.getListItem(item).setVisible(true);
						else
							userPanelManager.getListItem(item).setVisible(false);
					}
				}
			}
			
		});
		tableList.setLocalLocation(50, 23);
		container.addSubItem(tableList);
		
		List<String> buttonActions = new ArrayList<String>();
		buttonActions.add("Block");
		buttonActions.add("Unblock");
		//buttonActions.add("Delete");
		buttonActions.add("Show Pad");
		buttonActions.add("Hide pad");
		buttonActions.add("Share Pad");
		buttonActions.add("Control Pad");
		buttonActions.add("Select All");
		buttonActions.add("Deselect All");
		//buttonActions.add("Send Message");
		//buttonActions.add("Send Assignment");
		//buttonActions.add("Retrieve Results");
		
		int panelWidth = UserControllerWindow.windowWidth- 20;
		int shiftX = -panelWidth/2;
		int shiftY = -tableList.getItemHeight()/2;
		for(int i=0; i<buttonActions.size(); i++){
			SimpleButton button = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
			button.setText(buttonActions.get(i));
			button.addButtonListener(new ButtonAction());
			button.setLocalLocation(shiftX + button.getWidth()/2, shiftY);
			shiftX+= button.getWidth()+ 5;
			if((shiftX+ button.getWidth()/2)> panelWidth/2){
				shiftX = -panelWidth/2;
				shiftY-= button.getHeight() + 5;
			}
			container.addSubItem(button);
		}
	}
	
	class ButtonAction implements SimpleButtonListener{

		@Override
		public void buttonClicked(SimpleButton b, long id, float x, float y,
				float pressure) {
			if(b.getText().equalsIgnoreCase("Select All")){
				userPanelManager.selectAllItems();
			}else if(b.getText().equalsIgnoreCase("Deselect All")){
				userPanelManager.deselectAllItems();
			}else if(b.getText().equalsIgnoreCase("Delete")){
				userPanelManager.deleteSelectedItems();
			}else if(b.getText().equalsIgnoreCase("Block")){
				for(Object item: userPanelManager.getSelectedItems()){
					UserInfo userInfo = (UserInfo) item;
					if(controllerManager != null){	
						BlockUserMathPadMessage message = new BlockUserMathPadMessage(MathPadClient.class, userInfo.getTableIdentity(), userInfo.getUserIdentity(), true);
						controllerManager.sendMessage(message);
					}
				}
				for(Object item: userPanelManager.getSelectedItems()){
					UserInfo userInfo = (UserInfo) item;
					userInfo.setUserStatus(UserStatus.BLOCKED);
					userPanelManager.setIcon(item, MathPadUtils.class.getResource("images/userstatus/blocked.jpg"));
				}
			}else if(b.getText().equalsIgnoreCase("Unblock")){
				for(Object item: userPanelManager.getSelectedItems()){
					UserInfo userInfo = (UserInfo) item;
					if(controllerManager != null){	
						BlockUserMathPadMessage message = new BlockUserMathPadMessage(MathPadClient.class, userInfo.getTableIdentity(), userInfo.getUserIdentity(), false);
						controllerManager.sendMessage(message);
					}
				}
				for(Object item: userPanelManager.getSelectedItems()){
					UserInfo userInfo = (UserInfo) item;
					userInfo.setUserStatus(UserStatus.ONLINE);
					userPanelManager.setIcon(item, MathPadUtils.class.getResource("images/userstatus/online.jpg"));
				}
			}else if(b.getText().equalsIgnoreCase("Show Pad")){
				for(Object item: userPanelManager.getSelectedItems()){
					UserInfo userInfo = (UserInfo) item;
					if(controllerManager != null){	
						HideUserMathPadMessage message = new HideUserMathPadMessage(MathPadClient.class, userInfo.getTableIdentity(), userInfo.getUserIdentity(), false);
						controllerManager.sendMessage(message);
					}
				}
				for(Object item: userPanelManager.getSelectedItems()){
					UserInfo userInfo = (UserInfo) item;
					userInfo.setUserStatus(UserStatus.ONLINE);
					userPanelManager.setIcon(item, MathPadUtils.class.getResource("images/userstatus/online.jpg"));
				}
			}else if(b.getText().equalsIgnoreCase("Hide Pad")){
				for(Object item: userPanelManager.getSelectedItems()){
					UserInfo userInfo = (UserInfo) item;
					if(controllerManager != null){	
						HideUserMathPadMessage message = new HideUserMathPadMessage(MathPadClient.class, userInfo.getTableIdentity(), userInfo.getUserIdentity(), true);
						controllerManager.sendMessage(message);
					}
				}
				for(Object item: userPanelManager.getSelectedItems()){
					UserInfo userInfo = (UserInfo) item;
					userInfo.setUserStatus(UserStatus.OFFLINE);
					userPanelManager.setIcon(item, MathPadUtils.class.getResource("images/userstatus/offline.jpg"));
				}
			}else if(b.getText().equalsIgnoreCase("Share Pad")){
				for(Object item: userPanelManager.getSelectedItems()){
					UserInfo userInfo = (UserInfo) item;
					RequestMathPadItemFromUserMessage message = new RequestMathPadItemFromUserMessage(MathPadClient.class, userInfo.getTableIdentity(), userInfo.getUserIdentity());
					if(controllerManager != null){
						controllerManager.sendMessage(message);
						UnicastSyncWithUserMessage syncMessage = new UnicastSyncWithUserMessage(MathPadClient.class, userInfo.getTableIdentity(), userInfo.getUserIdentity(),true);
						controllerManager.sendMessage(syncMessage);
						controllerManager.getSyncManager().enableUnicastUserSync(userInfo.getTableIdentity(), userInfo.getUserIdentity(), true);
					}
				}			
			}else if(b.getText().equalsIgnoreCase("Control Pad")){
					for(Object item: userPanelManager.getSelectedItems()){
						UserInfo userInfo = (UserInfo) item;
						RequestMathPadItemFromUserMessage message = new RequestMathPadItemFromUserMessage(MathPadClient.class, userInfo.getTableIdentity(), userInfo.getUserIdentity());
						if(controllerManager != null){
							controllerManager.sendMessage(message);
							BlockUserMathPadMessage blockMessage = new BlockUserMathPadMessage(MathPadClient.class, userInfo.getTableIdentity(), userInfo.getUserIdentity(), true);
							controllerManager.sendMessage(blockMessage);
							userInfo.setUserStatus(UserStatus.BLOCKED);
							userPanelManager.setIcon(item, MathPadUtils.class.getResource("images/userstatus/blocked.jpg"));
							controllerManager.getSyncManager().enableUnicastUserSync(userInfo.getTableIdentity(), userInfo.getUserIdentity(), true);
						}
					}			
				}
		}

		@Override
		public void buttonDragged(SimpleButton b, long id, float x, float y, float pressure) {
		}

		@Override
		public void buttonPressed(SimpleButton b, long id, float x, float y, float pressure) {
			Color bgColor = b.getBackgroundColour();
			b.setBackgroundColour(b.getTextColour());
			b.setTextColour(bgColor);
		}

		@Override
		public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
			Color bgColor = b.getBackgroundColour();
			b.setBackgroundColour(b.getTextColour());
			b.setTextColour(bgColor);
		}
		
	}
	
	public OrthoContainer getContainer(){
		return container;
	}
}
