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

package synergyspace.jme.mmt.messages.mtmessages;

import synergyspace.jme.mmt.utility.TableInfo;

import com.jme.math.Vector2f;

public class RegisterTableMessage extends MTMessage {
	
	private TableInfo tableInfo;
	
	public RegisterTableMessage()
	{
		tableInfo = new TableInfo();
	}
	
	public RegisterTableMessage(TableInfo tableInfo)
	{
		this.tableInfo = tableInfo;
	}
	
	public RegisterTableMessage(short clientId, Vector2f tablePosition,int width, int height)
	{
		this.tableInfo = new TableInfo(clientId, tablePosition, width, height);
	}
	
	public void setClientId(short clientId)
	{
		this.tableInfo.setClientId(clientId);
	}
	
	public TableInfo getTableInfo()
	{
		return tableInfo;
	}
	
	public short getClientId()
	{
		return this.tableInfo.getClientId();
	}
	
	public void setTablePosition(Vector2f tablePosition)
	{
		this.tableInfo.setTablePosition(tablePosition);
	}

	public Vector2f getTablePosition()
	{
		return this.tableInfo.getTablePosition();
	}
}
