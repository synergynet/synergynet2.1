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

import com.jme.math.Vector3f;

import synergyspace.jme.cursorsystem.MultiTouchElement;
import synergyspace.jme.mmt.messages.createmessages.SpatialCreateMessage;
import synergyspace.jme.mmt.messages.createmessages.TransferredSpatialCreateMessage;

public interface SpatialMessageUtility {
	
	public SpatialCreateMessage getCreateMessageForMultiTouchElement(MultiTouchElement multiTouchElement, String type);

	public TransferredSpatialCreateMessage getCreateMessageForTransferredMultiTouchElement(MultiTouchElement multiTouchElement, short targetClientId, String type, Vector3f newPosition, Vector3f linearVelocity, float deceleration);

	public MultiTouchElement getMultiTouchElementFromCreateMessage(SpatialCreateMessage msg);
	
	public MultiTouchElement getTransferredMultiTouchElementFromCreateMessage(Class<?> resourceClass, TransferredSpatialCreateMessage msg);
}
