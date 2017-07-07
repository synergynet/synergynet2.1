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

package synergynet.table.apps.mathpadapp.networkmanager.managers;

import java.util.ArrayList;
import java.util.HashMap;

import synergynet.contentsystem.ContentSystem;
import synergynet.services.net.localpresence.TableIdentity;
import synergynet.services.net.tablecomms.client.TableCommsClientService;
import synergynet.table.apps.mathpadapp.mathtool.MathTool;
import synergynet.table.apps.mathpadapp.mathtool.MathToolInitSettings;
import synergynet.table.apps.mathpadapp.networkmanager.managers.syncmanager.SyncManager;
import synergynet.table.apps.mathpadapp.networkmanager.messages.fromprojector.ProjectorResponse;
import synergynet.table.apps.mathpadapp.networkmanager.utils.UserIdentity;

public class ProjectorManager extends NetworkedContentManager{

	protected boolean isBusy = false;
	protected TableIdentity controllerTable;
	
	public ProjectorManager(ContentSystem contentSystem, TableCommsClientService comms, ArrayList<Class<?>> receiverClasses) {
		super(contentSystem, comms, receiverClasses);
		super.setSyncManager(new SyncManager(this));
	}
	
	public boolean isProjectorBusy(){
		return isBusy;
	}
	
	public void setProjectorBusy(boolean isBusy){
		this.isBusy = isBusy;
	}

	public void leaseProjector(TableIdentity requesterTable){
		boolean leaseSucceed = false;
		if(!isBusy){
			leaseSucceed = true;
			this.setProjectorBusy(true);
			this.setController(requesterTable);
		}
		for(Class<?> receiverClass: this.getReceiverClasses()){
			ProjectorResponse response = new ProjectorResponse(receiverClass, leaseSucceed, requesterTable);
			this.sendMessage(response);
		}
	}

	public void releaseProjector(TableIdentity tableId) {
		if(controllerTable.hashCode() == tableId.hashCode()){
			this.setProjectorBusy(false);
			this.setController(null);
		}
	}
	
	private void setController(TableIdentity requesterTable) {
		this.controllerTable = requesterTable;
	}

	public void mathPadItemsReceivedFromTable(TableIdentity senderTable, HashMap<UserIdentity, MathToolInitSettings> toolSettings) {
		MathTool mathTool;
		for(UserIdentity userId: toolSettings.keySet()){
			if(!mathPads.containsKey(userId)){
				mathTool = new MathTool(contentSystem);
				this.registerMathPad(userId, mathTool);
			}else{
				mathTool = mathPads.get(userId);
			}
			mathTool.init(toolSettings.get(userId));
		}
	}

	public void clearProjector() {
		contentSystem.removeAllContentItems();
	}
}
