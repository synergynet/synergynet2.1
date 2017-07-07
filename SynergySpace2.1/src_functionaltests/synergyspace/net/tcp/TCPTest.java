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

package synergyspace.net.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

import synergyspace.net.objectmessaging.Client;
import synergyspace.net.objectmessaging.Server;
import synergyspace.net.objectmessaging.connections.ConnectionHandler;
import synergyspace.net.objectmessaging.connections.MessageHandler;

public class TCPTest implements MessageHandler {

	protected String name;

	public TCPTest(String name) {
		this.name = name;
	}

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
		Server s = new Server();
		new Thread(s).start();
		s.bind(8015);
		s.addMessageHandler(new TCPTest("Server"));
		Client c = new Client();
		new Thread(c).start();
		c.addMessageHandler(new TCPTest("Client"));
		c.connect(5000, InetAddress.getLocalHost(), 8015);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String msg = "";
		while(!msg.equals("quit")) {
			System.out.print("> "); System.out.flush();
			msg = br.readLine();
			if(!msg.equals("quit")) {

				c.sendTCP(msg);

			}
		}
		System.exit(0);
	}

	public void messageReceived(Object obj, ConnectionHandler handler) {
		System.out.println(name + " received message " + obj);
	}

	@Override
	public void handlerConnected(ConnectionHandler connectionHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlerDisconnected(ConnectionHandler connectionHandler) {
		// TODO Auto-generated method stub
		
	}
}
