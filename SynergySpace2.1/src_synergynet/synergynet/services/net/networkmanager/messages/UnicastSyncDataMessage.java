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

package synergynet.services.net.networkmanager.messages;

import java.util.HashMap;

import synergynet.services.net.localpresence.TableIdentity;
import synergynet.services.net.tablecomms.messages.application.UnicastApplicationMessage;
import synergyspace.net.objectmessaging.messages.UDPMessage;

public class UnicastSyncDataMessage extends UnicastApplicationMessage implements UDPMessage{

	private static final long serialVersionUID = -546746341111227474L;
	
	protected HashMap<String, HashMap<Short, Object>> syncData = new HashMap<String, HashMap<Short, Object>>();

	public UnicastSyncDataMessage(){
		super();
	}
	
	public UnicastSyncDataMessage(Class<?> targetClass, TableIdentity tableId, HashMap<String, HashMap<Short, Object>> items) {
		super(targetClass);
		this.setSyncData(syncData);
		this.setRecipient(tableId);
	}

	public HashMap<String, HashMap<Short, Object>> getSyncData() {
		return syncData;
	}

	public void setSyncData(HashMap<String, HashMap<Short, Object>> syncData){
		this.syncData.clear();
		this.syncData.putAll(syncData);
	}
}
