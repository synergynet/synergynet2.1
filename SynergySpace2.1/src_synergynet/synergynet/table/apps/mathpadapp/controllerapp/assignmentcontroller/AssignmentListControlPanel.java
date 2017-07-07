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

package synergynet.table.apps.mathpadapp.controllerapp.assignmentcontroller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.OrthoContainer;
import synergynet.contentsystem.items.SimpleButton;
import synergynet.contentsystem.items.listener.SimpleButtonListener;
import synergynet.table.apps.mathpadapp.clientapp.MathPadClient;
import synergynet.table.apps.mathpadapp.controllerapp.assignmentbuilder.AssignmentBuilder;
import synergynet.table.apps.mathpadapp.controllerapp.assignmentbuilder.AssignmentBuilderListenerImpl;
import synergynet.table.apps.mathpadapp.controllerapp.assignmentbuilder.AssignmentManager;
import synergynet.table.apps.mathpadapp.controllerapp.usercontroller.UserInfo;
import synergynet.table.apps.mathpadapp.networkmanager.managers.ControllerManager;
import synergynet.table.apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.CancelMathAssignmentMessage;
import synergynet.table.apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.RequestResultsFromUserMessage;
import synergynet.table.apps.mathpadapp.util.MTList;
import synergynet.table.apps.mathpadapp.util.MTListManager;
import synergynet.table.apps.mathpadapp.util.MTMessageBox;
import synergynet.table.apps.mathpadapp.util.MTMessageBox.MessageListener;

public class AssignmentListControlPanel {
	
	protected OrthoContainer container;
	private MTList assignList;
	private MTListManager assignPanelManager;
	public static final int controlAssignPanelHeight = 140;
	private ControllerManager controllerManager;
	private ContentSystem contentSystem;
	
	public AssignmentListControlPanel(ContentSystem contentSystem, final MTList assignList, ControllerManager controllerManager){
		container  = (OrthoContainer) contentSystem.createContentItem(OrthoContainer.class);
		this.assignList = assignList;
		this.assignPanelManager = assignList.getManager();
		this.controllerManager = controllerManager;
		this.contentSystem = contentSystem;
		List<String> buttonActions = new ArrayList<String>();
		buttonActions.add("Select All");
		buttonActions.add("Deselect All");
		buttonActions.add("Edit");
		buttonActions.add("Delete");
		buttonActions.add("Retrieve Results");
		
		int panelWidth = AssignmentControllerWindow.windowWidth- 20;
		int shiftX = -panelWidth/2;
		int shiftY = 0;
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
				assignPanelManager.selectAllItems();
			}else if(b.getText().equalsIgnoreCase("Deselect All")){
				assignPanelManager.deselectAllItems();
			}else if(b.getText().equalsIgnoreCase("Edit")){
				for(Object item: assignPanelManager.getSelectedItems()){
					AssignmentSession session = (AssignmentSession) item;
					AssignmentBuilder ab = new AssignmentBuilder(contentSystem, controllerManager.getGraphManager());
					ab.getAssignmentHandler().setAssignment(session.getAssignment());
					ab.getAssignmentHandler().drawAssignment();
					if(session.getSolution().getHandwritingResult() != null){
						ab.getAnswerDialog().getAnswerPad().draw(session.getSolution().getHandwritingResult());
					}
					ab.addMathToolListener(new AssignmentBuilderListenerImpl(controllerManager));
				}
			}else if(b.getText().equalsIgnoreCase("Delete")){
				if(assignPanelManager.getSelectedItems().isEmpty()) return;
				final MTMessageBox msg = new MTMessageBox(null,contentSystem);
				msg.setTitle("Delete");
				msg.setMessage("Are you sure you want to delete\n the selected assignment?");
				msg.getWindow().setAsTopObject();
				msg.getWindow().setAngle(assignList.getContainer().getParent().getAngle());
				msg.getWindow().setScale(assignList.getContainer().getParent().getScale());
				msg.getWindow().setLocalLocation(assignList.getContainer().getParent().getLocalLocation());

				msg.addMessageListener(new MessageListener(){
					@Override
					public void buttonReleased(String buttonId) {
						if(buttonId.equals("OK")){
							for(Object item: assignPanelManager.getSelectedItems()){
								AssignmentSession session = (AssignmentSession) item;
								AssignmentManager.getManager().getAssignmentSessions().remove(session.getAssignment().getAssignmentId());
								for(UserInfo userInfo: session.getRecipients()){
									CancelMathAssignmentMessage msg = new CancelMathAssignmentMessage(MathPadClient.class, userInfo.getTableIdentity(), userInfo.getUserIdentity());
									controllerManager.sendMessage(msg);
								}
							}
							assignPanelManager.deleteSelectedItems();
						}
						msg.close();
					}

					@Override
					public void buttonClicked(String buttonId) {
						// TODO Auto-generated method stub
						
					}
				});
			}else if(b.getText().equalsIgnoreCase("Retrieve Results")){
				List<Object> items = assignPanelManager.getSelectedItems();
				for(Object item: items){
					AssignmentSession session = (AssignmentSession) item; 
					ResultDialog resultDialog = new ResultDialog(session.getAssignment().getAssignmentId(),contentSystem, controllerManager);
					resultDialog.close();
					resultDialog = new ResultDialog(session.getAssignment().getAssignmentId(),contentSystem, controllerManager);
					resultDialog.getWindow().setAsTopObject();
					if(controllerManager != null){
						for(UserInfo receipent: session.getRecipients()){
							RequestResultsFromUserMessage msg = new RequestResultsFromUserMessage(MathPadClient.class, receipent.getTableIdentity(), receipent.getUserIdentity());
							controllerManager.sendMessage(msg);
						}
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
