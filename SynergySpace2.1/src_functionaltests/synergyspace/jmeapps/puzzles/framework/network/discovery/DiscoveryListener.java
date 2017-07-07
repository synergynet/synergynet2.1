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

package synergyspace.jmeapps.puzzles.framework.network.discovery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;


public class DiscoveryListener{

    static class SampleListener implements ServiceListener {
        public void serviceAdded(ServiceEvent event) {
            String eventName = event.getName();
            if (eventName.contains(".")){
            	String[] info = eventName.split("-");
            	if (info.length == 3){
            		if (name.equals(info[0])){
                		if (!addresses.contains(info[1])){
    		            	addresses.add(info[1]);
    		            	ports.add(info[2]);
    		            	System.out.println("Service added: " + info[1] + " " + info[2]);
                		}
            		}
            	}
            }
        }
        public void serviceRemoved(ServiceEvent event) {
        	String eventName = event.getName();
            if (eventName.contains(".")){
            	String[] info = eventName.split("-");
            	if (info.length == 3){
            		if (name.equals(info[0])){
                		if (addresses.contains(info[1])){
    		            	startGame = true;
                		}
            		}
            	}
            }
        }
        public void serviceResolved(ServiceEvent event) {
            System.out.println("Service resolved: " + event.getInfo());
        }
    }

    private static boolean startGame = false;
	private static String name;
	private static List<String> addresses = new ArrayList<String>();
	private static List<String> ports = new ArrayList<String>();
	private static JmDNS jmdns;

	public DiscoveryListener(String name, String[] addressAndPort) {
		DiscoveryListener.name = name;
		addresses.add(addressAndPort[0]);
		ports.add(addressAndPort[1]);
	}

	public void close(){
		jmdns.close();
	}

	public boolean startGame(){
		return startGame;
	}

	public List<String[]> getAddresses(){
		List<String[]> addressesAndPorts = new ArrayList<String[]>();
		for (int i = 0; i < addresses.size(); i ++){
			String address = addresses.get(i);
			if (address.contains("/")){
				address = address.split("/")[1];
			}
			String[] addPo = {address, ports.get(i)};
			addressesAndPorts.add(addPo);
		}
		return addressesAndPorts;
	}

	public void start() {
        try {
			jmdns = JmDNS.create();
			jmdns.addServiceListener("_http._tcp.local.", new SampleListener());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
