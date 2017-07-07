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

package synergyspace.jmeapps.networkflick;


import java.net.InetAddress;
import java.net.InetSocketAddress;

import synergyspace.jme.mmt.handlers.ServerMessageHandler;
import synergyspace.jme.mmt.messages.createmessages.SpatialCreateMessage;
import synergyspace.jme.mmt.messages.createmessages.TransferredSpatialCreateMessage;
import synergyspace.jme.mmt.messages.mtmessages.AnnounceTablesListMessage;
import synergyspace.jme.mmt.messages.mtmessages.RegisterTableMessage;
import synergyspace.jme.mmt.messages.mtmessages.UnregisterTableMessage;


import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.clientserver.JGNServer;

public class Server {
	

	JGNServer server;
	ServerMessageHandler messageHandler;
	
	public Server() throws Exception 
	{
		JGN.register(AnnounceTablesListMessage.class);
		JGN.register(RegisterTableMessage.class);
		JGN.register(UnregisterTableMessage.class);	
		JGN.register(SpatialCreateMessage.class);	
		JGN.register(TransferredSpatialCreateMessage.class);
		
        InetSocketAddress serverReliable = new InetSocketAddress(InetAddress.getLocalHost(), 9100);
		
        InetSocketAddress serverFast = new InetSocketAddress(InetAddress.getLocalHost(), 9200);
		
        server = new JGNServer(serverReliable, serverFast);
		
        server.setConnectionLinking(true);
        
        messageHandler = new ServerMessageHandler(server);
    	
    	server.addClientConnectionListener(messageHandler);
    	
    	server.addMessageListener(messageHandler);
		
		JGN.createThread(server).start();
	}
	
	public static void main(String[] args)
	{
		try {
			new Server();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




}

