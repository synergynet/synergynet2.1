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
import java.util.HashMap;

import synergyspace.jme.mmt.TransferController;
import synergyspace.jme.mmt.flicksystem.FlickSystem;
import synergyspace.jme.mmt.handlers.ClientMessageHandler;
import synergyspace.jme.mmt.messages.createmessages.SpatialCreateMessage;
import synergyspace.jme.mmt.messages.createmessages.TransferredSpatialCreateMessage;
import synergyspace.jme.mmt.messages.mtmessages.AnnounceTablesListMessage;
import synergyspace.jme.mmt.messages.mtmessages.RegisterTableMessage;
import synergyspace.jme.mmt.messages.mtmessages.UnregisterTableMessage;


import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.clientserver.JGNClient;
import com.jme.math.Vector2f;


public class Client{
	
	//TODO: make this a SynergySpace app
	//TODO: translate into using services with network discovery
	
	public static String SERVER_IP = "129.234.79.204";
	public static Vector2f TABLE_LOCATION = new Vector2f(800,0);
	
	public static HashMap<String, String> spatialToImageMap = new HashMap<String, String>();
	
	public Client() throws Exception {
		
		JGN.register(AnnounceTablesListMessage.class);
		JGN.register(RegisterTableMessage.class);
		JGN.register(UnregisterTableMessage.class);	
		JGN.register(SpatialCreateMessage.class);	
		JGN.register(TransferredSpatialCreateMessage.class);	
		
		final LightBox l = new LightBox();
        new Thread() {
        	public void run() {
        		
        		l.start();
        	}
        }.start();
        
        while(l.node == null){}
        
		InetSocketAddress serverReliable = new InetSocketAddress(SERVER_IP, 9100);
		
        InetSocketAddress serverFast = new InetSocketAddress(SERVER_IP, 9200);
        
        InetSocketAddress clientReliable = new InetSocketAddress(InetAddress.getLocalHost(), 0);
		
        InetSocketAddress clientFast = new InetSocketAddress(InetAddress.getLocalHost(), 0);
		
        JGNClient client = new JGNClient(clientReliable, clientFast);
		
		JGN.createThread(client).start();
		
		client.connectAndWait(serverReliable, serverFast, 5000);
				
		TransferController controller = new TransferController(client, l.node);
		
		FlickSystem.getInstance().enableTransferObjects(controller);
		
		ClientMessageHandler handler = new ClientMessageHandler(controller);
		
		client.addClientConnectionListener(handler);
		
		client.addMessageListener(handler);

		controller.registerLocalTable(TABLE_LOCATION);
	}
	
	public static void main(String[] args)
	{
		try {
			new Client();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
