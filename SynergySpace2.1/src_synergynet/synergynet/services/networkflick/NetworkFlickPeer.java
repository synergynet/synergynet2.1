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

package synergynet.services.networkflick;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import synergynet.services.net.localpresence.TableIdentity;
import synergynet.services.networkflick.handlers.ClientHandler;
import synergynet.services.networkflick.handlers.ServerHandler;
import synergynet.services.networkflick.messages.configmessages.RegisterTableMessage;
import synergynet.services.networkflick.messages.configmessages.UnregisterTableMessage;
import synergynet.services.networkflick.utility.TableInfo;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergyspace.net.landiscovery.ServiceDescriptor;
import synergyspace.net.objectmessaging.Client;
import synergyspace.net.objectmessaging.Server;
import synergyspace.net.peer.LocalPeer;

public class NetworkFlickPeer extends LocalPeer{

	private static final Logger log = Logger.getLogger(NetworkFlickPeer.class.getName());
	
	public static final int SERVER_PORT = 1234;
	Server server;
	Client client;
	private ClientHandler handler;
	private TransferController transferController;
	public NetworkFlickPeer(String serviceName, String serviceType) {
		super(serviceName, serviceType);
	}
	
	@Override
	public void advertiseServer() {
		ServiceDescriptor sd = new ServiceDescriptor();
		sd.setServiceType(super.serviceType);
		sd.setServiceName(super.serviceName);
		sd.setServicePort(1268);
		sd.setUserData("path=index.html");
		try {
			sd.setServiceAddress(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			log.warning(e.toString());
		}
		getServiceAnnounceSystem().registerService(sd);
		
		log.info("Advertising server for network flicking");
		
	}

	@Override
	public void foundServer(ServiceDescriptor serviceDescriptor) {
		log.info("Server found!. Starting a client ....");
		startClient(serviceDescriptor.getServiceAddress(), SERVER_PORT);
	}

	@Override
	public void startServer() {
		log.info("Server not found!. Start a new one.");
		ServerHandler handler = new ServerHandler();
		server = new Server();
		new Thread(server).start();
		try {
			server.bind(SERVER_PORT);
		} catch (IOException e) {
			log.warning(e.toString());
		}
		server.addMessageHandler(handler);
		try {
			startClient(InetAddress.getLocalHost(), SERVER_PORT);
		} catch (UnknownHostException e) {
			log.warning(e.toString());
		}
	}
	
	protected void startClient(InetAddress serverAddress, int serverPort){
		client = new Client();
		handler = new ClientHandler();
		new Thread(client).start();
		client.addMessageHandler(handler);
		try {
			client.connect(5000, serverAddress, serverPort);
		} catch (IOException e) {
			log.warning(e.toString());
		}
	}
	
	public void registerLocalTable(DefaultSynergyNetApp app, int tablePositionX, int tablePositionY, float angle, int width, int height) throws IOException{
		if(client != null){
			TableInfo localTableInfo = new TableInfo(TableIdentity.getTableIdentity(), tablePositionX, tablePositionY, angle, width, height);
			transferController = new TransferController(app);
			transferController.setLocalTableInfo(localTableInfo);
			transferController.setClient(client);
			handler.setTransferController(transferController);
			RegisterTableMessage msg = new RegisterTableMessage(localTableInfo);
			client.sendTCP(msg);
			
			log.info("Register local table for network flick");
		}
	}
	

	public void unregisterLocalTable() throws IOException
	{
		if(client != null){
			UnregisterTableMessage msg = new UnregisterTableMessage(transferController.getLocalTableInfo().getTableId());
			client.sendTCP(msg);
			transferController.cleanUpUnregisteredTable(msg);
			
			log.info("Unregister local table");
		}
	}
	
	public void update(){
		if(transferController != null) transferController.update();
	}
}
