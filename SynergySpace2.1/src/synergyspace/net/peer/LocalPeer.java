/*
 * Copyright (c) 2008 University of Durham, England
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

package synergyspace.net.peer;

import java.io.IOException;

import synergyspace.net.landiscovery.ServiceAnnounceSystem;
import synergyspace.net.landiscovery.ServiceDescriptor;
import synergyspace.net.landiscovery.ServiceDiscoverySystem;
import synergyspace.net.landiscovery.ServiceSystemFactory;

public abstract class LocalPeer {
	
	private ServiceDiscoverySystem serviceDiscoverySystem;
	private ServiceAnnounceSystem serviceAnnounceSystem;
	protected String serviceType;
	protected String serviceName;
	protected long timeOut = 5 * 1000;
	
	public LocalPeer(String serviceName, String serviceType) {
		this.serviceName = serviceName;
		this.serviceType = serviceType;
	}
	
	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}
	
	public void connect() throws IOException {
		
			setServiceDiscoverySystem(ServiceSystemFactory.getServiceDiscoverySystem());
			setServiceAnnounceSystem(ServiceSystemFactory.getServiceAnnouncerSystem());
			ServerStatusMonitor smon = new ServerStatusMonitor(serviceType, serviceName, timeOut);
			getServiceDiscoverySystem().registerListener(smon);
			getServiceDiscoverySystem().registerServiceForListening(serviceType, serviceName);
			try {
				smon.connect();
				boolean serverFound = smon.serverFound();
				if(!serverFound) {
					advertiseServer();
					startServer();
				}else{
					foundServer(smon.getServerServiceDescriptor());
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		
	}
	
	public void setServiceDiscoverySystem(ServiceDiscoverySystem serviceDiscoverySystem) {
		this.serviceDiscoverySystem = serviceDiscoverySystem;
	}

	public ServiceDiscoverySystem getServiceDiscoverySystem() {
		return serviceDiscoverySystem;
	}
	
	public void setServiceAnnounceSystem(ServiceAnnounceSystem serviceAnnounceSystem) {
		this.serviceAnnounceSystem = serviceAnnounceSystem;
	}

	public ServiceAnnounceSystem getServiceAnnounceSystem() {
		return serviceAnnounceSystem;
	}
	
	public abstract void advertiseServer();
	public abstract void startServer();
	public abstract void foundServer(ServiceDescriptor serviceDescriptor);
}
