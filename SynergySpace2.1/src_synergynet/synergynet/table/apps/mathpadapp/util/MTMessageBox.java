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
package synergynet.table.apps.mathpadapp.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.MultiLineTextLabel;
import synergynet.contentsystem.items.SimpleButton;
import synergynet.contentsystem.items.listener.SimpleButtonAdapter;

public class MTMessageBox extends MTDialog{

	protected MultiLineTextLabel messageBox;
	protected SimpleButton okBtn, cancelBtn;
	protected transient List<MessageListener> listeners = new ArrayList<MessageListener>();
	
	public MTMessageBox(MTFrame parentFrame, ContentSystem contentSystem) {
		super(parentFrame, contentSystem);
		messageBox = (MultiLineTextLabel) contentSystem.createContentItem(MultiLineTextLabel.class);
		messageBox.setBorderSize(0);
		messageBox.setBackgroundColour(this.getWindow().getBackgroundColour());
		messageBox.setTextColour(Color.black);
		messageBox.setLocalLocation(0, 40);
		this.getWindow().addSubItem(messageBox);
		this.setModal(true);
		this.setHeight(180);
		okBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		okBtn.setBorderSize(1);
		okBtn.setBorderColour(Color.black);
		okBtn.setBackgroundColour(Color.LIGHT_GRAY);
		okBtn.setTextColour(Color.black);
		okBtn.setAutoFitSize(false);
		okBtn.setWidth(65);
		okBtn.setHeight(25);
		okBtn.setText("OK");
		okBtn.setLocalLocation(-40, -60);
		okBtn.addButtonListener(new SimpleButtonAdapter(){
			@Override
			public void buttonClicked(SimpleButton b, long id, float x, float y,
					float pressure) {
				for(MessageListener listener: listeners){
					listener.buttonClicked("OK");
				}
			}

			@Override
			public void buttonPressed(SimpleButton b, long id, float x, float y,
					float pressure) {
				okBtn.setBackgroundColour(Color.DARK_GRAY);
				okBtn.setTextColour(Color.white);
			}

			@Override
			public void buttonReleased(SimpleButton b, long id, float x, float y,
					float pressure) {
				for(MessageListener listener: listeners){
					listener.buttonReleased("OK");
				}
				
				okBtn.setBackgroundColour(Color.LIGHT_GRAY);
				okBtn.setTextColour(Color.black);
			}
		});
		this.getWindow().addSubItem(okBtn);
		
		cancelBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		cancelBtn.setBorderSize(1);
		cancelBtn.setBorderColour(Color.black);
		cancelBtn.setBackgroundColour(Color.LIGHT_GRAY);
		cancelBtn.setTextColour(Color.black);
		cancelBtn.setAutoFitSize(false);
		cancelBtn.setWidth(65);
		cancelBtn.setHeight(25);
		cancelBtn.setText("Cancel");
		cancelBtn.setLocalLocation(40, -60);
		cancelBtn.addButtonListener(new SimpleButtonAdapter(){
			@Override
			public void buttonClicked(SimpleButton b, long id, float x, float y,
					float pressure) {
				for(MessageListener listener: listeners){
					listener.buttonClicked("Cancel");
				}
			}

			@Override
			public void buttonPressed(SimpleButton b, long id, float x, float y,
					float pressure) {
				cancelBtn.setBackgroundColour(Color.DARK_GRAY);
				cancelBtn.setTextColour(Color.white);
			}

			@Override
			public void buttonReleased(SimpleButton b, long id, float x, float y,
					float pressure) {
				for(MessageListener listener: listeners){
					listener.buttonReleased("Cancel");
				}
				
				cancelBtn.setBackgroundColour(Color.LIGHT_GRAY);
				cancelBtn.setTextColour(Color.black);
			}
		});
		this.getWindow().addSubItem(cancelBtn);
	}
	
	public void addMessageListener(MessageListener listener){
		if(!listeners.contains(listener)) listeners.add(listener);
	}
	
	public void removeMessageListeners(){ listeners.clear();}
	
	public void setMessage(String text){
		messageBox.setCRLFSeparatedString(text);
	}
	
	public SimpleButton getOkButton(){
		return okBtn;
	}
	
	public SimpleButton getCancelButton(){
		return cancelBtn;
	}

	public interface MessageListener{
		public void buttonClicked(String buttonId);
		public void buttonReleased(String buttonId);
	}
}
