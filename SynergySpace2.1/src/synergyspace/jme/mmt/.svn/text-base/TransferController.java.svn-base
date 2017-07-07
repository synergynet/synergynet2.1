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

package synergyspace.jme.mmt;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import synergyspace.jme.cursorsystem.MultiTouchElement;
import synergyspace.jme.mmt.flicksystem.FlickSystem;
import synergyspace.jme.mmt.messages.createmessages.TransferredSpatialCreateMessage;
import synergyspace.jme.mmt.messages.mtmessages.AnnounceTablesListMessage;
import synergyspace.jme.mmt.messages.mtmessages.MTMessage;
import synergyspace.jme.mmt.messages.mtmessages.RegisterTableMessage;
import synergyspace.jme.mmt.messages.mtmessages.UnregisterTableMessage;
import synergyspace.jme.mmt.utility.SpatialMessageMapper;
import synergyspace.jme.mmt.utility.TableInfo;
import synergyspace.jme.mmt.utility.TransferArea;
import synergyspace.jmeapps.networkflick.Client;

import com.captiveimagination.jgn.clientserver.JGNClient;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;


public class TransferController
{
	private static final Logger log = Logger.getLogger(TransferController.class.getName());
	
	private ConcurrentLinkedQueue<TransferArea> transferAreas  = new ConcurrentLinkedQueue<TransferArea>();
	private JGNClient client;
	private TableInfo localTableInfo = null;
	private Node rootNode;
	private SpatialMessageMapper spatialMsgMapper;

	
	public TransferController(JGNClient client, Node rootNode)
	{
		this.client = client;
		this.rootNode = rootNode;
		spatialMsgMapper = new SpatialMessageMapper();
	}
	
	public void applyTransfer(MultiTouchElement movingElement, Vector3f linearVelocity, float deceleration)
	{
		for(TransferArea transferArea : transferAreas)
		{
			if(movingElement.getTargetSpatial().hasCollision(transferArea, true))
			{
				FlickSystem.getInstance().makeUnflickable(movingElement);
				// Calculate position in world space
		        Vector3f newPosition = movingElement.getTargetSpatial().getLocalTranslation().add(localTableInfo.getTablePosition().x,localTableInfo.getTablePosition().y,0);
		        TransferredSpatialCreateMessage message = spatialMsgMapper.getCreateMessageForTransferredMultiTouchElement(movingElement, transferArea.getClientId(),"QuadWithImage", newPosition, linearVelocity, deceleration);
				detachSpatial(movingElement.getTargetSpatial());
		        client.sendToPlayer(message, transferArea.getClientId());
		        return;
			}
		}
	}
	
	public void applyMTMessage(MTMessage message)
	{
		if(!isLocalTableRegistered())
			return;
		
		if(message instanceof AnnounceTablesListMessage)
		{
			log.info("Announce message received");
			AnnounceTablesListMessage msg = (AnnounceTablesListMessage) message;
			ArrayList<TableInfo> tablesInfo = msg.getTablesInfo();
			for(TableInfo tableInfo : tablesInfo)
				registerRemoteTable(tableInfo);
		}
		if(message instanceof RegisterTableMessage)
		{

			RegisterTableMessage msg = (RegisterTableMessage) message;
			log.info("register table  msg : "+ msg.getClientId() + " , "+ msg.getTablePosition());
			registerRemoteTable(msg.getTableInfo());
		}
		if(message instanceof UnregisterTableMessage)
		{
			UnregisterTableMessage msg = (UnregisterTableMessage) message;
			if(msg.getClientId() == localTableInfo.getClientId())
			{
				for(TransferArea transferArea : transferAreas)
					detachSpatial(transferArea);
				transferAreas.clear();
				localTableInfo = null;
			}
			else
			{	
				TransferArea transferArea = findAreaById(msg.getClientId());
				if(transferArea != null)
				{
					transferAreas.remove(transferArea);	
					detachSpatial(transferArea);
				}
			}
		}
	}
	
	public void applySpatialCreateMessage(TransferredSpatialCreateMessage message) 
	{
		if(!isLocalTableRegistered())
			return;

		MultiTouchElement multiTouchElement = spatialMsgMapper.getTransferredMultiTouchElementFromCreateMessage(Client.class, message);
		// Translate world position to local position
		Vector3f relativePosition = message.getPosition().subtract(localTableInfo.getTablePosition().x,localTableInfo.getTablePosition().y,0);
		multiTouchElement.getTargetSpatial().setLocalTranslation(relativePosition);
		FlickSystem.getInstance().makeFlickable(multiTouchElement.getTargetSpatial(), multiTouchElement, message.getDeceleration());
		attachSpatial(multiTouchElement.getTargetSpatial());
	   	FlickSystem.getInstance().flick(multiTouchElement, message.getLinearVelocity(), message.getDeceleration());
	}
	
	private boolean registerRemoteTable(TableInfo remoteTableInfo)
	{
		TransferArea temp = findAreaById(remoteTableInfo.getClientId());
		if(temp == null && remoteTableInfo.getClientId() != localTableInfo.getClientId())
		{
			TransferArea transferArea = new TransferArea(remoteTableInfo);
			Vector2f relativePosition = remoteTableInfo.getTablePosition().subtract(localTableInfo.getTablePosition());
			transferArea.setLocalTranslation(relativePosition.x, relativePosition.y, 0);
			attachSpatial(transferArea);
			transferAreas.add(transferArea);

			return true;
		}
		else
			return false;
	}
	
	private TransferArea findAreaById(short clientId)
	{
		for(TransferArea area : transferAreas)
		{
			if(area.getClientId() == clientId)
				return area;
		}
		return null;
	}
	
	public void registerLocalTable(Vector2f tablePosition)
	{
		if(client != null && tablePosition != null)
		{
			localTableInfo = new TableInfo(client.getPlayerId(),tablePosition, DisplaySystem.getDisplaySystem().getWidth(), DisplaySystem.getDisplaySystem().getHeight());
			RegisterTableMessage msg = new RegisterTableMessage(localTableInfo);
			client.sendToServer(msg);
		}
	}
	
	public void unregisterTable()
	{
		if(client != null && localTableInfo != null)
		{
			UnregisterTableMessage msg = new UnregisterTableMessage(client.getPlayerId());
			client.sendToServer(msg);
			this.applyMTMessage(msg);
		}
	}
	
	private boolean isLocalTableRegistered()
	{
		if(localTableInfo != null)
			return true;
		return false;
	}
	
	private void attachSpatial(Spatial spatial)
	{
		final Spatial temp = spatial;
		
        GameTaskQueueManager.getManager().update(new Callable<Object>() {

            public Object call() throws Exception {
    			rootNode.attachChild(temp);
    			rootNode.updateRenderState();
    			rootNode.updateGeometricState(0f, false);
                return null;
            }
        });		
	}
	
	private void detachSpatial(Spatial spatial)
	{
		final Spatial temp = spatial;
        GameTaskQueueManager.getManager().update(new Callable<Object>() {

            public Object call() throws Exception {
				rootNode.detachChild(temp);
				rootNode.updateRenderState();
    			rootNode.updateGeometricState(0f, false);
                return null;
            }
        });
	}
}
