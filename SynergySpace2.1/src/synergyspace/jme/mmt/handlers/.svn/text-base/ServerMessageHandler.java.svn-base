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

package synergyspace.jme.mmt.handlers;


import java.util.ArrayList;
import java.util.HashMap;

import synergyspace.jme.mmt.messages.mtmessages.AnnounceTablesListMessage;
import synergyspace.jme.mmt.messages.mtmessages.RegisterTableMessage;
import synergyspace.jme.mmt.messages.mtmessages.UnregisterTableMessage;
import synergyspace.jme.mmt.utility.TableInfo;

import com.captiveimagination.jgn.clientserver.JGNConnection;
import com.captiveimagination.jgn.clientserver.JGNConnectionListener;
import com.captiveimagination.jgn.clientserver.JGNServer;
import com.captiveimagination.jgn.event.MessageListener;
import com.captiveimagination.jgn.message.Message;

public class ServerMessageHandler implements JGNConnectionListener, MessageListener{
	
	private JGNServer server;
	private HashMap<Short,TableInfo> clientTableMap  = new HashMap<Short,TableInfo>();
	
	public ServerMessageHandler(JGNServer server)
	{
		this.server = server;
	}
	
	public void connected(JGNConnection connection) {
		// TODO Auto-generated method stub
		
	}

	public void disconnected(JGNConnection connection) {
		// TODO Auto-generated method stub
		System.out.println("Table "+ connection.getPlayerId() + " is disconnected");
		if(clientTableMap.containsKey(connection.getPlayerId()))
		{
			clientTableMap.remove(connection.getPlayerId());
			UnregisterTableMessage message = new UnregisterTableMessage(connection.getPlayerId());
			System.out.println("send unregister msg to all");
			server.sendToAll(message);
		}
	}

	public void messageCertified(Message message) {
		// TODO Auto-generated method stub
		
	}

	public void messageFailed(Message message) {
		// TODO Auto-generated method stub
		
	}

	public void messageReceived(Message message) {
		// TODO Auto-generated method stub
		if(message instanceof RegisterTableMessage)
		{

			RegisterTableMessage msg = (RegisterTableMessage) message;
			clientTableMap.remove(new Short(msg.getClientId()));
			clientTableMap.put(new Short(msg.getClientId()),msg.getTableInfo());
			System.out.println("Table registered : "+ msg.getClientId() + " , " + msg.getTablePosition());
			server.sendToPlayer(new AnnounceTablesListMessage(new ArrayList<TableInfo>(clientTableMap.values())),msg.getClientId());
			server.sendToAllExcept(msg, msg.getClientId());

		}
		
		if(message instanceof UnregisterTableMessage)
		{
			UnregisterTableMessage msg = (UnregisterTableMessage) message;
			clientTableMap.remove(new Short(msg.getClientId()));
			server.sendToAll(msg);
		}
	}
	
	public void messageSent(Message message) {
		// TODO Auto-generated method stub
		
	}


}
