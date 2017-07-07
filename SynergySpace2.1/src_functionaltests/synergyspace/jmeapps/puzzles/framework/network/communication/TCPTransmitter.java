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

package synergyspace.jmeapps.puzzles.framework.network.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class TCPTransmitter extends Thread{

	private List<String> messages = new ArrayList<String>();
	private boolean keepGoing = true;
	private List<Socket> sockets = new ArrayList<Socket>();
	private List<PrintWriter> writers = new ArrayList<PrintWriter>();

	public TCPTransmitter(List<String[]> addresses){
		super("TCPAnnouncer");
	}

	public void setupAddresses(List<String[]> addresses){
		for (int i = 1; i < addresses.size(); i++){
			try {
				sockets.add(new Socket(addresses.get(i)[0], Integer.parseInt(addresses.get(i)[1])));
				writers.add(new PrintWriter(sockets.get(i-1).getOutputStream(), true));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMessage(String message){
		messages.add("message: " + message);
	}

	public void sendObject(Object o){
		messages.add("object: " + new XStream(new DomDriver()).toXML(o));
	}

	public void close(){
		keepGoing = false;
		for (int i = 0; i < sockets.size(); i++){
			if (sockets.get(i) != null){
				try {
					writers.get(i).close();
					if (sockets.get(i).isClosed()){
						sockets.get(i).close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void run(){
		while (keepGoing){
			if (messages.size() != 0){
				for (int i = 0; i < sockets.size(); i++){
					writers.get(i).print(messages.get(0));
				}
				messages.remove(0);
			}
			try {
				Thread.sleep(2000);
			}catch (InterruptedException ie){}
		}
	}
}

