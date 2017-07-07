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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import synergynet.services.net.localpresence.TableIdentity;
import synergynet.services.networkflick.messages.configmessages.AnnounceTableListMessage;
import synergynet.services.networkflick.messages.configmessages.RegisterTableMessage;
import synergynet.services.networkflick.messages.configmessages.UnregisterTableMessage;
import synergynet.services.networkflick.messages.createmessages.TransferableContentItem;
import synergynet.services.networkflick.utility.TableInfo;
import synergyspace.net.objectmessaging.connections.ConnectionHandler;
import synergyspace.net.objectmessaging.connections.MessageHandler;


public class ServerHandler implements MessageHandler{
	
	private static final Logger log = Logger.getLogger(ServerHandler.class.getName());
	private HashMap<TableIdentity,ConnectionHandler> tableClientMap  = new HashMap<TableIdentity,ConnectionHandler>();
	private ArrayList<TableInfo> tableInfos = new ArrayList<TableInfo>();
	
	public ServerHandler(){}
	
	@Override
	public void handlerDisconnected(ConnectionHandler connectionHandler) {
		// TODO Auto-generated method stub
	}

	@Override
	public void messageReceived(Object obj, ConnectionHandler handler) {
		if(obj instanceof RegisterTableMessage){
			log.info("Server received: RegisterTableMessage");
			RegisterTableMessage msg = (RegisterTableMessage) obj;
			tableClientMap.remove(msg.getTableId());
			tableClientMap.put(msg.getTableId(), handler);
			tableInfos.add(msg.getTableInfo());
			try {
				handler.sendTCP(new AnnounceTableListMessage(tableInfos));
			} catch (IOException e) {
				log.warning(e.toString());
			}
			for(ConnectionHandler h: tableClientMap.values())
				if(h != handler)
					try {
						h.sendTCP(obj);
					} catch (IOException e) {
						log.warning(e.toString());
					}
		}
		else if(obj instanceof UnregisterTableMessage){
			log.info("Server received: UnregisterTableMessage");
			UnregisterTableMessage msg = (UnregisterTableMessage) obj;
			tableClientMap.remove(msg.getTableId());
			removeTableInfo(msg.getTableId());
			for(ConnectionHandler h: tableClientMap.values()){
				try {
					h.sendTCP(msg);
				} catch (IOException e) {
					log.warning(e.toString());
				}
			}
		}
		else if(obj instanceof TransferableContentItem){
			log.info("Server received: TransferableContentItem");
			TransferableContentItem msg = (TransferableContentItem) obj;
			ConnectionHandler targetHandler = tableClientMap.get(msg.getTargetTableId());
			if(targetHandler != null)
				try {
					targetHandler.sendTCP(msg);
				} catch (IOException e) {
					log.warning(e.toString());
				}
		}
		else{
			log.info("Server received "+obj.toString());
		}
	}
	
	private void removeTableInfo(TableIdentity tableId){
		Iterator<TableInfo> iter = tableInfos.iterator();
		while(iter.hasNext()){
			if(iter.next().getTableId().equals(tableId)) iter.remove();
		}
	}

	@Override
	public void handlerConnected(ConnectionHandler connectionHandler) {
		// TODO Auto-generated method stub
		
	}

}
