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

package synergynet.services.net.localpresence;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import synergynet.services.ServiceManager;
import synergynet.services.SynergyNetService;
import synergynet.services.exceptions.CouldNotStartServiceException;
import synergynet.services.net.netservicediscovery.NetworkServiceDiscoveryService;
import synergyspace.net.landiscovery.ServiceAnnounceSystem;
import synergyspace.net.landiscovery.ServiceDescriptor;
import synergyspace.net.landiscovery.ServiceDiscoverySystem;

public class LocalPresenceAnnouncerService extends SynergyNetService {

	private static final Logger log = Logger.getLogger(LocalPresenceAnnouncerService.class.getName());
	
	private static final String SERVICE_TYPE = "_snn._tcp.local.";
	private static final String SERVICE_NAME = "presence";
	protected static final int SERVICE_PORT = 1268;

	protected ServiceDiscoverySystem serviceDiscovery;
	protected ServiceAnnounceSystem serviceAnnouncer;
	protected ServiceDescriptor info;
	protected boolean isrunning;

	public LocalPresenceAnnouncerService() {}

	@Override
	public void start() throws CouldNotStartServiceException {	
		log.info("Local presence announcer service startup...");
		new Thread() {
			public void run() {
				if(serviceDiscovery == null) {
					NetworkServiceDiscoveryService nsds;
					try {
						nsds = (NetworkServiceDiscoveryService) ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);
						serviceDiscovery = nsds.getServiceDiscovery();

						info = new ServiceDescriptor();
						info.setServiceType(SERVICE_TYPE);
						info.setServiceName(SERVICE_NAME);
						info.setServicePort(SERVICE_PORT);
						try {
							info.setServiceAddress(InetAddress.getLocalHost());
						} catch (UnknownHostException e) {
							log.warning(e.toString());
						}
						info.setUserData(TableIdentity.getTableIdentity().toString());
						serviceAnnouncer.registerService(info);
						isrunning = true;
					} catch (CouldNotStartServiceException e1) {
						log.warning(e1.toString());
					}
				}}
		}.start();
		log.info("Local presence announcer service startup completed.");
	}

	@Override
	public void stop() {
		log.info("Local presence announcer service stopping.");
		serviceAnnouncer.unregisterService(info);
		isrunning = false;
		log.info("Local presence announcer service stopped.");
	}

	@Override
	public void shutdown() {
		isrunning = false;
		log.info("Local presence announcer service shut down.");
	}

	@Override
	public boolean hasStarted() {
		return isrunning;
	}

	@Override
	public void update() {}
}
