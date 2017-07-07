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

package synergynet.services;

import java.util.ArrayList;
import java.util.List;

import synergynet.services.exceptions.CouldNotStartServiceException;
import synergynet.services.exceptions.ServiceNotRunningException;

public abstract class SynergyNetService {
	protected abstract void start() throws CouldNotStartServiceException;
	public abstract void stop() throws ServiceNotRunningException;
	public abstract void shutdown();
	public abstract boolean hasStarted();
	/**
	 * Used to avoid threading issues. Should be called periodically
	 * to ensure the service updates appropriately.
	 */
	public abstract void update();	
	
	List<ServiceMessageListener> serviceMessageListeners = new ArrayList<ServiceMessageListener>();
	
	/**
	 * Services can publish messages. Subscribe to these messages
	 * by adding ServiceMessageListener instances.
	 */
	public void registerServiceMessageListener(ServiceMessageListener listener) {
		if(!serviceMessageListeners.contains(listener)) serviceMessageListeners.add(listener);
		
	}
	
	/**
	 * Will be called by services to publish messages to those
	 * listening for them.
	 * @param message
	 */
	protected void sendServiceMessage(String message) {
		for(ServiceMessageListener l : serviceMessageListeners) {
			l.serviceMessage(this, message);
		}
	}
	
}
