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

package synergynet.table.apps.mathpadapp.controllerapp.projectorcontroller;


import java.util.HashMap;
import java.util.List;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.SimpleButton;
import synergynet.contentsystem.items.listener.SimpleButtonAdapter;
import synergynet.services.net.localpresence.TableIdentity;
import synergynet.table.apps.mathpadapp.MathPadUtils;
import synergynet.table.apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo;
import synergynet.table.apps.mathpadapp.mathtool.MathToolInitSettings;
import synergynet.table.apps.mathpadapp.networkmanager.managers.ControllerManager;
import synergynet.table.apps.mathpadapp.networkmanager.managers.ControllerManager.ControllerNetworkListener;
import synergynet.table.apps.mathpadapp.networkmanager.utils.UserIdentity;
import synergynet.table.apps.mathpadapp.util.MTFrame;
import synergynet.table.apps.mathpadapp.util.MTList;
import synergyspace.jme.cursorsystem.elements.twod.OrthoBringToTop;

public class ProjectorControllerWindow extends MTFrame implements ControllerNetworkListener{
	
	public static final int windowWidth = 350;
	public static final int windowHeight = 440;
	
	protected MTList projectorPanel;
	protected ProjectorListControlPanel controlPanel;
	protected ControllerManager controllerManager;
	
	public ProjectorControllerWindow(final ContentSystem contentSystem, final ControllerManager controllerManager){
		super(contentSystem);
		this.controllerManager = controllerManager;
		this.setWidth(windowWidth);
		this.setHeight(windowHeight);
		
		projectorPanel = new MTList(contentSystem);
		this.getWindow().addSubItem(projectorPanel.getContainer());
		projectorPanel.setHeight(350);
		projectorPanel.setWidth(330);
		projectorPanel.getContainer().setLocalLocation(0, -10);
		
		controlPanel = new ProjectorListControlPanel(contentSystem, projectorPanel, controllerManager);
		controlPanel.getContainer().setLocalLocation(controlPanel.getContainer().getLocalLocation().x, -190);
		this.getWindow().addSubItem(controlPanel.getContainer());
		
		this.getWindow().setOrder(OrthoBringToTop.bottomMost);
		
		this.setTitle("Online Projectors");
		
		this.closeButton.removeButtonListeners();
		this.closeButton.addButtonListener(new SimpleButtonAdapter(){
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				ProjectorControllerWindow.this.close();
			}
		});
		if(controllerManager != null) controllerManager.addNetworkListener(this);
		
		projectorPanel.getManager().addItem("temp", "temp");
		projectorPanel.getManager().deleteAllItems();
	}

	public MTList getAssignmentSessionList() {
		return this.projectorPanel;
	}

	@Override
	public void resultsReceivedFromUser(TableIdentity tableId,
			UserIdentity userId, AssignmentInfo assignInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void userIdsReceived(TableIdentity tableId,
			List<UserIdentity> userIds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void userRegistrationReceived(TableIdentity tableId,
			UserIdentity userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void userUnregistrationReceived(TableIdentity tableId,
			UserIdentity userId) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void projectorFound(final TableIdentity tableId, boolean isLeaseSuccessful) {
		if(projectorPanel.getManager().getAllItems().contains(tableId)) return;
		projectorPanel.getManager().addItem("Projector " + (projectorPanel.getManager().getAllItems().size()+1), tableId);
		projectorPanel.getManager().setIcon(tableId, MathPadUtils.class.getResource("images/controlBar/Projectors.jpg"));
	}
	
	public void close(){
		if(controllerManager != null) controllerManager.removeNetworkListener(ProjectorControllerWindow.this);
		contentSystem.removeContentItem(ProjectorControllerWindow.this.getWindow());
	}

	@Override
	public void userMathPadReceived(TableIdentity tableId,
			UserIdentity userId, MathToolInitSettings mathToolSettings) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remoteDesktopContentReceived(TableIdentity tableId,
			HashMap<UserIdentity, MathToolInitSettings> items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void syncDataReceived(TableIdentity sender,
			HashMap<UserIdentity, HashMap<Short, Object>> mathPadSyncData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tableIdReceived(TableIdentity tableId) {
		// TODO Auto-generated method stub
		
	}


}
