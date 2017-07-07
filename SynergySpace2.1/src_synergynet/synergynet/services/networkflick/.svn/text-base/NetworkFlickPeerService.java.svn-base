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

package synergynet.services.networkflick;

import java.io.IOException;
import java.util.logging.Logger;

import synergynet.services.SynergyNetService;
import synergynet.services.exceptions.CouldNotStartServiceException;
import synergynet.services.exceptions.ServiceNotRunningException;
import synergynet.table.apps.DefaultSynergyNetApp;

public class NetworkFlickPeerService extends SynergyNetService{

	private static final Logger log = Logger.getLogger(NetworkFlickPeerService.class.getName());
	private static NetworkFlickPeer peer;
	private static final String SERVICE_TYPE = "SynergyNet";
	private static final String SERVICE_NAME = "networkflick";
	
	protected boolean hasStarted = false;
	
	@Override
	public boolean hasStarted() {
		return hasStarted;
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void start() throws CouldNotStartServiceException {
		synchronized(this) {
			hasStarted = true;
		}
		peer = new NetworkFlickPeer(SERVICE_NAME, SERVICE_TYPE);
		peer.setTimeOut(1000);
		try {
			peer.connect();
			Thread.sleep(1000);
		} catch (IOException e) {
			log.warning(e.toString());
		} catch (InterruptedException e) {
			log.warning(e.toString());
		}	
		
		log.info("NetworkFlickPeerService started");
	}

	@Override
	public void stop() throws ServiceNotRunningException {
		if(peer.client != null)	peer.client.stop();
		else if(peer.server != null) peer.server.stop();
		
		log.info("NetworkFlickPeerService stopped");
	}

	@Override
	public void update() {
		if(peer != null && peer.client != null && peer.client.isConnected()) peer.update();
	}

	public void registerLocalTable(DefaultSynergyNetApp app, int locationX, int locationY, float angle, int width, int height) throws IOException {
		peer.registerLocalTable(app, locationX, locationY, angle, width, height);
		
		log.info("Register local table for NetworkFlickPeerService");
	}
	
	public void unregisterLocalTable() throws IOException{
		peer.unregisterLocalTable();
		log.info("Unregister local table from NetworkFlickPeerService");
	}
}
