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
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class UDPTransmitter extends Thread{

	private List<InetAddress> inetAddresses = new ArrayList<InetAddress>();
	private List<Integer> ports = new ArrayList<Integer>();
	private List<String> messages = new ArrayList<String>();
	private DatagramSocket socket = null;

	private boolean keepGoing = true;
    private byte[] buf;

	public UDPTransmitter(DatagramSocket socket){
		super("UDPAnnouncer");
		buf = new byte[256];
		this.socket = socket;
	}

	public void setupAddresses(List<String[]> addresses){
		for (int i = 1; i < addresses.size(); i++){
			try {
				inetAddresses.add(InetAddress.getByName(addresses.get(i)[0]));
				ports.add(Integer.parseInt(addresses.get(i)[1]));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
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
	}

	@Override
	public void run(){
		while (keepGoing){			
			if (messages.size() != 0){
				buf = messages.remove(0).getBytes();
				for (int i = 0; i < inetAddresses.size(); i++){
	                try {
	                    DatagramPacket packet = new DatagramPacket(buf, buf.length,
	                    		inetAddresses.get(i), ports.get(i));
	                    socket.send(packet);	                    
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(2000);
			}catch (InterruptedException ie){}
		}
		if (socket != null){
			synchronized(socket){
				socket.close();
			}
		}
	}
}

