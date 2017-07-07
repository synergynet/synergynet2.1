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

package synergyspacetest.net.tcpmsg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import synergyspace.net.objectmessaging.Client;
import synergyspace.net.objectmessaging.Server;
import synergyspace.net.objectmessaging.connections.ConnectionHandler;
import synergyspace.net.objectmessaging.connections.MessageHandler;

public class TestTCPMessageServer {

	private static final int PORT = 18214;

	@Test(timeout = 1000)
	public void testOpenClose() {
		Server server = new Server();
		new Thread(server).start();
		try {
			server.bind(PORT);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		server.addMessageHandler(new MessageHandler() {
			public void handlerDisconnected(ConnectionHandler connectionHandler) {
			}

			public void messageReceived(Object obj, ConnectionHandler handler) {
			}

			@Override
			public void handlerConnected(ConnectionHandler connectionHandler) {
				// TODO Auto-generated method stub

			}			
		});
		try {
			while(!server.isRunning()) {
				System.out.println("Not ready, waiting...");
				Thread.sleep(100);
			}
			assertTrue(server.isRunning());
			server.stop();
			assertFalse(server.isRunning());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Test(timeout = 1000)
	public void testOpenConnectClose() {
		Server server = new Server();
		new Thread(server).start();
		try {
			server.bind(PORT);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		server.addMessageHandler(new MessageHandler() {
			public void handlerDisconnected(ConnectionHandler connectionHandler) {
			}

			public void messageReceived(Object obj, ConnectionHandler handler) {
			}

			@Override
			public void handlerConnected(ConnectionHandler connectionHandler) {
				// TODO Auto-generated method stub

			}
		});
		try {
			while(!server.isRunning()) {
				System.out.println("Not ready, waiting...");
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(server.isRunning());

		Client client = new Client();
		new Thread(client).start();
		client.addMessageHandler(new MessageHandler() {
			public void handlerDisconnected(ConnectionHandler connectionHandler) {}
			public void messageReceived(Object obj, ConnectionHandler handler) {}
			public void handlerConnected(ConnectionHandler connectionHandler) {}				
		});
		try {
			client.connect(5000, InetAddress.getByName("localhost"), PORT);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		assertEquals(1, server.getHandlers().size());

		server.stop();

		assertEquals(0, server.getHandlers().size());
		assertFalse(server.isRunning());
	}

}
