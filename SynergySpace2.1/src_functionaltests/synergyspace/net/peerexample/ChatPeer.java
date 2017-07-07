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

package synergyspace.net.peerexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import synergyspace.net.landiscovery.ServiceDescriptor;
import synergyspace.net.objectmessaging.Client;
import synergyspace.net.objectmessaging.Server;
import synergyspace.net.objectmessaging.connections.ConnectionHandler;
import synergyspace.net.objectmessaging.connections.MessageHandler;
import synergyspace.net.peer.LocalPeer;

public class ChatPeer extends LocalPeer implements MessageHandler {

	private static final int PORT = 1234;
	protected Server server;
	protected Client client;

	public ChatPeer(String serviceName, String serviceType) {
		super(serviceName, serviceType);
	}

	@Override
	public void foundServer(ServiceDescriptor info) {
		System.out.println("Server found, connecting.");
		startClient(info.getServiceAddress(), PORT);
	}

	@Override
	public void startServer() {
		ChatServerHandler serverHandler = new ChatServerHandler();
		server = new Server();
		new Thread(server).start();
		try {
			server.bind(PORT);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		server.addMessageHandler(serverHandler);
		serverHandler.setServer(server);
		System.out.println("Connecting to self...");
		try {
			startClient(InetAddress.getLocalHost(), 1234);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	protected void startClient(InetAddress host, int port) {
		client = new Client();
		new Thread(client).start();
		client.addMessageHandler(this);
		try {
			client.connect(5000, host, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public static void main(String[] args) {
		ChatPeer peer = new ChatPeer("chatsrv", "_http._tcp.local.");
		try {
			peer.setTimeOut(10000);
			peer.connect();

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String msg = "";
			while(!msg.equals("quit")) {
				System.out.print("> "); System.out.flush();
				msg = br.readLine();
				if(!msg.equals("quit")) {
					if(peer.client != null) {
						peer.client.sendTCP(msg);
					}
				}
			}
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void messageReceived(Object obj, ConnectionHandler handler) {
		System.out.println("Received " + obj);		
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
			e.printStackTrace();
		}
		getServiceAnnounceSystem().registerService(sd);
	}

	public void handlerDisconnected(ConnectionHandler connectionHandler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handlerConnected(ConnectionHandler connectionHandler) {
		// TODO Auto-generated method stub
		
	}
}
