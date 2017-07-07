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

package synergynet.services.net.networkedcontentmanager.controllers;

import java.util.logging.Logger;

import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.OrthoContentItem;
import synergynet.services.net.networkedcontentmanager.NetworkedContentManager;
import synergynet.services.net.networkedcontentmanager.messages.networkedflick.EnableFlickMessage;

public class NetworkedFlickController {
	
	private static final Logger log = Logger.getLogger(NetworkedContentManager.class.getName());
	
	protected NetworkedContentManager networkedContentManager;
	public static float DefaultDeceleration = 1;
	protected boolean isFlickEnabled = false;
	
	public NetworkedFlickController(NetworkedContentManager networkedContentManager){
		this.networkedContentManager = networkedContentManager;
	}
	
	public void setNetworkFlickEnabled(boolean isFlickEnabled){
		enableFlick(isFlickEnabled);
		for (Class<?> targetClass:networkedContentManager.getReceiverClasses())	
			networkedContentManager.sendMessage(new EnableFlickMessage(targetClass,isFlickEnabled));
		log.info("Broadcast command to enable flicking");
	}
	
	public void enableFlick(boolean isFlickEnabled){
		for(ContentItem item: networkedContentManager.getOnlineItems().values()){
			if(item instanceof OrthoContentItem){
				if(isFlickEnabled) ((OrthoContentItem)item).makeFlickable(DefaultDeceleration);
				else ((OrthoContentItem)item).makeUnflickable();
			}
		}
		this.isFlickEnabled = isFlickEnabled;	
		
		if (isFlickEnabled)
			log.info("Network flick enabled");
		else 
			log.info("Network flick disabled");
	}
	
	public boolean isFlickEnabled(){
		return isFlickEnabled;
	}
}
