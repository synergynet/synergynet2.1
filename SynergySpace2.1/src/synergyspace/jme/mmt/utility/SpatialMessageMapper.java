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

package synergyspace.jme.mmt.utility;

import java.net.URL;

import synergyspace.jme.cursorsystem.MultiTouchElement;
import synergyspace.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;
import synergyspace.jme.gfx.twod.ImageQuadFactory;
import synergyspace.jme.mmt.messages.createmessages.SpatialCreateMessage;
import synergyspace.jme.mmt.messages.createmessages.TransferredQuadWithImageCreateMessage;
import synergyspace.jme.mmt.messages.createmessages.TransferredSpatialCreateMessage;
import synergyspace.jme.mmt.utility.SpatialMessageUtility;
import synergyspace.jmeapps.networkflick.Client;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Quad;


public class SpatialMessageMapper implements SpatialMessageUtility{

	public SpatialCreateMessage getCreateMessageForMultiTouchElement(
			MultiTouchElement multiTouchElement, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	public TransferredSpatialCreateMessage getCreateMessageForTransferredMultiTouchElement(
			MultiTouchElement multiTouchElement, short targetClientId, String type,
			Vector3f newPosition, Vector3f linearVelocity, float deceleration) {
		// TODO Auto-generated method stub
		
		if(type.equals("QuadWithImage"))
		{
			TransferredQuadWithImageCreateMessage message = new TransferredQuadWithImageCreateMessage();
			message.setName(IDGenerator.getRandomID(10));
			String imageName = Client.spatialToImageMap.get(multiTouchElement.getName());
			message.setImage(imageName);
			Client.spatialToImageMap.remove(multiTouchElement.getName());
			message.setWidth(200);
			message.setLinearVelocity(linearVelocity);
			message.setDeceleration(deceleration);
			message.setPosition(newPosition);
			message.setRotation(multiTouchElement.getTargetSpatial().getLocalRotation());
			message.setScale(multiTouchElement.getTargetSpatial().getLocalScale());
			return message;
		}
		return null;
	}

	public MultiTouchElement getMultiTouchElementFromCreateMessage(
			SpatialCreateMessage msg) {
		// TODO Auto-generated method stub
		return null;
	}

	public MultiTouchElement getTransferredMultiTouchElementFromCreateMessage(Class<?> resourceClass,
			TransferredSpatialCreateMessage msg) {
		
		if(msg instanceof TransferredQuadWithImageCreateMessage)
		{
			TransferredQuadWithImageCreateMessage message = (TransferredQuadWithImageCreateMessage) msg;
			Quad quad = null;
			URL imageUrl = resourceClass.getResource(message.getImage());
			quad = ImageQuadFactory.createQuadWithImageResource(message.getName(), message.getWidth(), imageUrl);
			Client.spatialToImageMap.put(message.getName(), message.getImage());
			quad.setLocalTranslation(message.getPosition());
			quad.setLocalScale(message.getScale());
			quad.setLocalRotation(message.getRotation());
			quad.setModelBound(new OrthogonalBoundingBox());
			quad.updateModelBound();
			OrthoControlPointRotateTranslateScale ocprts1 = new OrthoControlPointRotateTranslateScale(quad);
			ocprts1.setPickMeOnly(true);
			return ocprts1;
		}
		
		return null;
	}





}
