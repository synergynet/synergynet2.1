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

package synergyspace.jmeapps.puzzles.framework.network;

import java.util.ArrayList;
import java.util.List;

import synergyspace.jmeapps.puzzles.framework.network.communication.TCPReceiver;
import synergyspace.jmeapps.puzzles.framework.network.communication.TCPTransmitter;
import synergyspace.jmeapps.puzzles.framework.network.communication.UDPReceiver;
import synergyspace.jmeapps.puzzles.framework.network.communication.UDPTransmitter;
import synergyspace.jmeapps.puzzles.framework.network.discovery.DiscoveryAnnouncer;
import synergyspace.jmeapps.puzzles.framework.network.discovery.DiscoveryListener;

abstract public class NetworkHandler {

	public static List<String[]> addresses = new ArrayList<String[]>();

	public static DiscoveryAnnouncer discoveryAnnouncer = null;
	public static DiscoveryListener discoveryListener = null;

	public static TCPTransmitter tcpTransmitter = null;
	public static TCPReceiver tcpReceiver = null;

	public static UDPTransmitter udpTransmitter = null;
	public static UDPReceiver udpReceiver = null;

	private static boolean tcpMode;
	private static boolean setup = false;

	public static void setup(String name, boolean tcpMode){
		NetworkHandler.tcpMode = tcpMode;
//		new Thread() {
//        	public void run() {
//        		app.start();
//        	}
//        }.start();
        discoveryAnnouncer = new DiscoveryAnnouncer(name);
        addresses.add(discoveryAnnouncer.getLocalAddress());
        discoveryListener = new DiscoveryListener(name, addresses.get(0));
        if (tcpMode){
        	tcpTransmitter = new TCPTransmitter(addresses);
	        tcpReceiver = new TCPReceiver(addresses.get(0));
	    }else{
	        udpReceiver = new UDPReceiver(addresses.get(0));
	        udpTransmitter = new UDPTransmitter(udpReceiver.getSocket());
	    }
	    setup = true;
	}

	public static void startDiscovery(){
		new Thread() {
        	public void run() {
        		discoveryAnnouncer.start();
        	}
        }.start();
		new Thread() {
        	public void run() {
        		discoveryListener.start();
        	}
        }.start();
	}

	public static boolean startGame(){
		return discoveryListener.startGame();
	}

	public static void closeDiscovery(){
		if (tcpMode){
			tcpTransmitter.setupAddresses(addresses);
		}else{
			udpTransmitter.setupAddresses(addresses);
		}
		new Thread() {
        	public void run() {
        		discoveryAnnouncer.close();
        	}
        }.start();
		new Thread() {
        	public void run() {
        		discoveryListener.close();
        	}
        }.start();
	}
	
	public static void startListening(){
		if (setup){
			if (tcpMode){
				tcpReceiver.start();
			}else{
				udpReceiver.start();
			}
		}
	}

	public static void startAnnouncing(){
		if (setup){
			if (tcpMode){
				tcpTransmitter.start();
			}else{
				udpTransmitter.start();
			}
		}
	}

	public static void sendMessage(String message){
		if (setup){
			if (tcpMode){
				tcpTransmitter.sendMessage(message);
			}else{				 
				udpTransmitter.sendMessage(message);
			}
		}
	}

	public static void sendObject(Object o){
		if (setup){
			if (tcpMode){
				tcpTransmitter.sendObject(o);
			}else{				
				udpTransmitter.sendObject(o);
			}
		}
	}

	public static int messages(){
		if (setup){
			if (tcpMode){
				return tcpReceiver.messages();
			}else{
				return udpReceiver.messages();
			}
		}
		return 0;
	}

	public static String getMessage(){
		if (setup){
			if (tcpMode){
				return tcpReceiver.getLatestMessage();
			}else{
				return udpReceiver.getLatestMessage();
			}
		}
		return "";
	}

	public static Object getObject(){
		if (setup){
			if (tcpMode){
				return tcpReceiver.getLatestObject();
			}else{
				return udpReceiver.getLatestObject();
			}
		}
		return null;
	}

	public static void closeCommunication(){
		if (setup){
	        if (tcpMode){
		        tcpReceiver.close();
		        tcpTransmitter.close();
		    }else{
		        udpReceiver.close();
		        udpTransmitter.close();
		    }
		}
	}

}
