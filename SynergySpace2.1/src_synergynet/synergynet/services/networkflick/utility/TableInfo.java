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

package synergynet.services.networkflick.utility;

import java.io.Serializable;

import synergynet.services.net.localpresence.TableIdentity;

public class TableInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2158376162046543629L;
	protected TableIdentity tableId;
	protected int positionX;
	protected int positionY;
	protected float angle;
	protected int width;
	protected int height;
	
	public TableInfo()
	{}
	
	public TableInfo(TableIdentity tableId, int positionX, int positionY, float angle, int width, int height)	{
		this.tableId = tableId;
		this.positionX = positionX;
		this.positionY = positionY;
		this.angle = angle;
		this.width = width;
		this.height = height;
	}
	
	public void setTablePosition(int positionX, int positionY)	{
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	public int getTablePositionX()	{
		return positionX;
	}
	
	public int getTablePositionY()	{
		return positionY;
	}
	
	public void setAngle(float angle){
		this.angle = angle;
	}
	
	public float getAngle(){
		return angle;
	}
	
	public void setTableId(TableIdentity tableId)	{
		this.tableId = tableId;
	}
	
	public TableIdentity getTableId(){
		return tableId;
	}
	
	public void setWidth(int width){
		this.width = width;
	}
	
	public int getWidth(){
		return width;
	}
	
	public void setHeight(int height){
		this.height = height;
	}
	
	public int getHeight(){
		return height;
	}
}
