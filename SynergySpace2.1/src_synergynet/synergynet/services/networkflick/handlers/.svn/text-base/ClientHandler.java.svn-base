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

package synergynet.services.networkflick.handlers;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

import com.jme.util.GameTaskQueueManager;

import synergynet.services.networkflick.TransferController;
import synergynet.services.networkflick.messages.configmessages.AnnounceTableListMessage;
import synergynet.services.networkflick.messages.configmessages.RegisterTableMessage;
import synergynet.services.networkflick.messages.configmessages.UnregisterTableMessage;
import synergynet.services.networkflick.messages.createmessages.TransferableContentItem;
import synergyspace.net.objectmessaging.connections.ConnectionHandler;
import synergyspace.net.objectmessaging.connections.MessageHandler;


public class ClientHandler implements MessageHandler{
	
	private static final Logger log = Logger.getLogger(ClientHandler.class.getName());
	private TransferController transferController;
	
	public ClientHandler(){}
	
	public void setTransferController(TransferController transferController){
		this.transferController = transferController;
	}
	
	@Override
	public void handlerDisconnected(ConnectionHandler connectionHandler) {
	}

	@Override
	public void messageReceived(final Object obj, ConnectionHandler handler) {
		if(transferController != null)
		{
			if(obj instanceof AnnounceTableListMessage){
				transferController.registerTableList((AnnounceTableListMessage)obj);
				log.info("Client received: AnnounceTableListMessage");
			}
			else if(obj instanceof RegisterTableMessage){
				log.info("Client sent: AnnounceTableListMessage");
				transferController.registerRemoteTable(((RegisterTableMessage)obj).getTableInfo());
			}
			else if(obj instanceof UnregisterTableMessage)
				transferController.cleanUpUnregisteredTable((UnregisterTableMessage)obj);
			else if(obj instanceof TransferableContentItem){
				GameTaskQueueManager.getManager().update(new Callable<Object>(){
					public Object call() throws Exception{
					transferController.applyTransferableContentItem((TransferableContentItem)obj);
					return null;
					}
				});
			}
		}
	}

	@Override
	public void handlerConnected(ConnectionHandler connectionHandler) {
		// TODO Auto-generated method stub
		
	}
}
