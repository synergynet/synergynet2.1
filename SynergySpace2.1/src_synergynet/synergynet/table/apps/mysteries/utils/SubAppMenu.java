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

package synergynet.table.apps.mysteries.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ListContainer;
import synergynet.contentsystem.items.SimpleButton;
import synergynet.contentsystem.items.listener.SimpleButtonAdapter;
import synergynet.contentsystem.items.listener.SubAppMenuEventListener;

public class SubAppMenu {
	
	public final static String SUB_APP_LONDONFIRE = "LONDON FIRE";
	public final static String SUB_APP_TIPPINGWAITRESSES = "TIPPING WAITRESSES";
	public final static String SUB_APP_ROBERTDIXON = "ROBERT DIXON";
	public final static String SUB_APP_DINNERDISASTER = "DINNER DISASTER";
	public final static String SUB_APP_WALTZER = "WALTZER";
	public final static String SUB_APP_SNEAKYSYDNEY = "SNEAKY SYDNEY";
	public final static String SUB_APP_SCHOOLTRIP = "SCHOOL TRIP";

	protected ContentSystem contentSystem;
	protected List<SubAppMenuEventListener> subAppMenuEventListener = new ArrayList<SubAppMenuEventListener>();
	
	public SubAppMenu(ContentSystem contentSystem){
		this.contentSystem = contentSystem;
	}
	
	public ListContainer getSubAppMenu(){

		final ListContainer menu = (ListContainer)contentSystem.createContentItem(ListContainer.class);
//		menu.setSuperSteadfast(true);
		menu.setWidth(200);
		menu.setItemHeight(30);
		menu.getBackgroundFrame().setBackgroundColour(Color.gray);	
		
		SimpleButton fireOfLondonButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		fireOfLondonButton.setAutoFitSize(false);
		fireOfLondonButton.setText(SUB_APP_LONDONFIRE);
		fireOfLondonButton.setBackgroundColour(Color.lightGray);
		fireOfLondonButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {					
				for (SubAppMenuEventListener l:subAppMenuEventListener)					
					l.menuSelected("data/mysteries/great_fire_of_london/great_fire_of_london.xml", SUB_APP_LONDONFIRE);
				menu.setVisible(false);
			}			
		});	
		
		SimpleButton dinnerDisasterButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		dinnerDisasterButton.setAutoFitSize(false);
		dinnerDisasterButton.setText(SUB_APP_DINNERDISASTER);
		dinnerDisasterButton.setBackgroundColour(Color.lightGray);
		dinnerDisasterButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {	
				for (SubAppMenuEventListener l:subAppMenuEventListener)
					l.menuSelected("data/mysteries/dinnerdisaster/dinnerdisaster.xml", SUB_APP_DINNERDISASTER);
				menu.setVisible(false);
			}			
		});	
		
		SimpleButton robertDixonButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		robertDixonButton.setAutoFitSize(false);
		robertDixonButton.setText(SUB_APP_ROBERTDIXON);
		robertDixonButton.setBackgroundColour(Color.lightGray);
		robertDixonButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				for (SubAppMenuEventListener l:subAppMenuEventListener)
					l.menuSelected("data/mysteries/robert_dixon/robert_dixon.xml", SUB_APP_ROBERTDIXON);
				menu.setVisible(false);				
			}			
		});	
		
		SimpleButton schoolTripButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);		
		schoolTripButton.setAutoFitSize(false);
		schoolTripButton.setText(SUB_APP_SCHOOLTRIP);
		schoolTripButton.setBackgroundColour(Color.lightGray);
		schoolTripButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {	
				for (SubAppMenuEventListener l:subAppMenuEventListener)
					l.menuSelected("data/mysteries/schooltrip/schooltrip.xml", SUB_APP_SCHOOLTRIP);
				menu.setVisible(false);		
			}			
		});	
				
		SimpleButton tippingWaitressButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);		
		tippingWaitressButton.setAutoFitSize(false);
		tippingWaitressButton.setText(SUB_APP_TIPPINGWAITRESSES);
		tippingWaitressButton.setBackgroundColour(Color.lightGray);
		tippingWaitressButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {	
				for (SubAppMenuEventListener l:subAppMenuEventListener)
					l.menuSelected("data/mysteries/tipping_waitresses/tipping_waitresses.xml", SUB_APP_TIPPINGWAITRESSES);
				menu.setVisible(false);			
			}			
		});	
		
		SimpleButton waltzerButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);		
		waltzerButton.setAutoFitSize(false);
		waltzerButton.setText(SUB_APP_WALTZER);
		waltzerButton.setBackgroundColour(Color.lightGray);
		waltzerButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {	
				for (SubAppMenuEventListener l:subAppMenuEventListener)
					l.menuSelected("data/mysteries/waltzer/waltzer.xml", SUB_APP_WALTZER);
				menu.setVisible(false);			
			}			
		});	
		
		SimpleButton sneakyButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);		
		sneakyButton.setAutoFitSize(false);
		sneakyButton.setText(SUB_APP_SNEAKYSYDNEY);
		sneakyButton.setBackgroundColour(Color.lightGray);
		sneakyButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {	
				for (SubAppMenuEventListener l:subAppMenuEventListener)
					l.menuSelected("data/mysteries/sneakysydney/sneakysydney.xml", SUB_APP_SNEAKYSYDNEY);
				menu.setVisible(false);			
			}			
		});	
		
		
		menu.addSubItem(fireOfLondonButton);
		menu.addSubItem(dinnerDisasterButton);
		menu.addSubItem(robertDixonButton);
		menu.addSubItem(schoolTripButton);	
		menu.addSubItem(tippingWaitressButton);
		menu.addSubItem(waltzerButton);	
		menu.addSubItem(sneakyButton);	
		
		return menu;
	}
	

	public void addSubAppMenuEventListener(SubAppMenuEventListener l){
		if (this.subAppMenuEventListener==null)
			this.subAppMenuEventListener = new ArrayList<SubAppMenuEventListener>();
		
		if(!this.subAppMenuEventListener.contains(l))
			this.subAppMenuEventListener.add(l);
	}
	
	public void removeSubAppMenuEventListeners(){
		subAppMenuEventListener.clear();
	}
	
	public void removeSubAppMenuEventListener(SubAppMenuEventListener l){
		subAppMenuEventListener.remove(l);
	}
}
