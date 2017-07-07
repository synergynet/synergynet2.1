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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class TCPReceiver extends Thread{

	private List<String> messages = new ArrayList<String>();
	private List<Object> objects = new ArrayList<Object>();
	private boolean keepGoing = true;

	private ServerSocket serverSocket = null;
	private Socket listenSocket = null;
	private BufferedReader in = null;

	public TCPReceiver(String[] addressAndPort){
		super("TCPListener");
		try {
			serverSocket = new ServerSocket(Integer.parseInt(addressAndPort[1]));
			listenSocket = serverSocket.accept();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int messages(){
		return messages.size();
	}

	public String getLatestMessage(){
		if (messages.size() == 0){
			return "";
		}else{
			return messages.remove(0);
		}
	}

	public Object getLatestObject(){
		if (objects.size() == 0){
			return null;
		}else{
			return objects.remove(0);
		}
	}

	public void close(){
		keepGoing = false;
		try {
			listenSocket.close();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		while (keepGoing){
			try {
				in = new BufferedReader(new InputStreamReader(listenSocket.getInputStream()));
				if (in != null){
					if (in.ready()) {
					    String s = in.readLine();
					    if ((s != null) &&  (s.length() != 0)) {
					    	String ID = s.split(" ")[0];
					    	s = s.replace(ID, "");
					    	if (ID.equals("message:")){
					    		messages.add(s);
					    	}else if(ID.equals("object:")){
					    		objects.add(new XStream(new DomDriver()).fromXML(s));
					    	}
					    }
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
