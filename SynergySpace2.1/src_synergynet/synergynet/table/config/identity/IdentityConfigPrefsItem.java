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

package synergynet.table.config.identity;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.UUID;
import java.util.prefs.Preferences;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import synergynet.table.config.ConfigurationSystem;
import synergynet.table.config.PreferencesItem;

public class IdentityConfigPrefsItem implements PreferencesItem {

	private static final Preferences prefs = ConfigurationSystem.getPreferences(IdentityConfigPrefsItem.class);
	
	private static final String PREFS_TABLE_ID = "TABLE_ID";
	private static final String PREFS_DEFINE_TABLE_ID = "FALSE";
	
	@Override
	public JPanel getConfigurationPanel() {
		JPanel panel = new JPanel();
		final JCheckBox defineIdBox = new JCheckBox();
		final JCheckBox autoGenerateBox = new JCheckBox();

		final JTextField tableId = new JTextField(30);
		tableId.setText(getID());
		tableId.setEditable(false);
		tableId.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e){
				IdentityConfigPrefsItem.this.setID(tableId.getText().trim());
			}
		});
		
		defineIdBox.setText("Define Table Id:");
		defineIdBox.setSelected(isTableIdDefined());
		if(defineIdBox.isSelected()) tableId.setEditable(true);
		defineIdBox.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent event) {
				autoGenerateBox.setSelected(!defineIdBox.isSelected());
				tableId.setEditable(defineIdBox.isSelected());
				setTableIdDefined(defineIdBox.isSelected());
			}
			
		});
 
		autoGenerateBox.setText("Auto Generate:");
		autoGenerateBox.setSelected(!isTableIdDefined());
		if(autoGenerateBox.isSelected()) tableId.setEditable(false);
		autoGenerateBox.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent event) {
				defineIdBox.setSelected(!autoGenerateBox.isSelected());
				setTableIdDefined(!autoGenerateBox.isSelected());
				if(autoGenerateBox.isSelected()){
					setID(UUID.randomUUID().toString());
					tableId.setEditable(!autoGenerateBox.isSelected());
					tableId.setText(getID());
				}
			}
		});
		
		panel.add(defineIdBox);
		panel.add(autoGenerateBox);
		panel.add(tableId);	
		return panel;
	}

	@Override
	public String getName() {
		return "Identity";
	}
	
	public String getID() {
		if(!isTableIdDefined()) setID(UUID.randomUUID().toString());
		return prefs.get(PREFS_TABLE_ID, "<no identity>");
	}

	public void setID(String uid) {
		prefs.put(PREFS_TABLE_ID, uid);		
	}
	
	public boolean isTableIdDefined(){
		return prefs.getBoolean(PREFS_DEFINE_TABLE_ID, false);
	}
	
	public void setTableIdDefined(boolean isTableIdDefined){
		prefs.putBoolean(PREFS_DEFINE_TABLE_ID, isTableIdDefined);
	}

}
