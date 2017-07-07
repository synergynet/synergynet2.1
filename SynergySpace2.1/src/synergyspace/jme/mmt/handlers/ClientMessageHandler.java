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

package synergyspace.jme.mmt.handlers;



import synergyspace.jme.mmt.TransferController;
import synergyspace.jme.mmt.messages.createmessages.TransferredSpatialCreateMessage;
import synergyspace.jme.mmt.messages.mtmessages.MTMessage;
import synergyspace.jme.mmt.messages.mtmessages.UnregisterTableMessage;

import com.captiveimagination.jgn.clientserver.JGNConnection;
import com.captiveimagination.jgn.clientserver.JGNConnectionListener;
import com.captiveimagination.jgn.event.MessageListener;
import com.captiveimagination.jgn.message.Message;

public class ClientMessageHandler implements JGNConnectionListener, MessageListener{
	
	private TransferController transferController;
	
	public ClientMessageHandler(TransferController transferController)
	{
		this.transferController = transferController;
	}
	
	public void connected(JGNConnection connection) {
		// TODO Auto-generated method stub
	}

	public void disconnected(JGNConnection connection) {
		// TODO Auto-generated method stub
		System.out.println("disconnect ........");
		UnregisterTableMessage msg = new UnregisterTableMessage(connection.getPlayerId());
		transferController.applyMTMessage(msg);
	}

	public void messageCertified(Message message) {
		// TODO Auto-generated method stub
	}

	public void messageFailed(Message message) {
		// TODO Auto-generated method stub
	}

	public void messageReceived(Message message) {
		// TODO Auto-generated method stub
		if(transferController != null)
		{
			if(message instanceof MTMessage)
				transferController.applyMTMessage((MTMessage)message);
			if(message instanceof TransferredSpatialCreateMessage)
				transferController.applySpatialCreateMessage((TransferredSpatialCreateMessage)message);
		}
	}

	public void messageSent(Message message) {
		// TODO Auto-generated method stub
	}
	
}
