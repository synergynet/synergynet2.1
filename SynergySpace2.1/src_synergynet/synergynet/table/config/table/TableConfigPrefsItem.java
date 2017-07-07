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

package synergynet.table.config.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import synergynet.table.config.ConfigurationSystem;
import synergynet.table.config.PreferencesItem;

public class TableConfigPrefsItem implements PreferencesItem {

	private static final Preferences prefs = ConfigurationSystem.getPreferences(TableConfigPrefsItem.class);
	
	public static final String PREFS_TABLE_TYPE = "TABLE_TYPE";
	public static final String PREFS_MENU_TYPE = "MENU_TYPE";
	
	private static final String PREFS_AUTO_START_MT_SERVER = "FALSE";
	private static final String PREFS_MT_SERVER_PATH = "MT_SERVER_PATH"; 
	
	private JPanel panel;
	private JPanel mtServerInfoPanel;
	
	public static enum TableType {
		JMEDIRECT, TUIOSIM, TUIO, LUMIN
	}
	
	public static enum MenuType {
		COMBO, AUTOMATIC, MANUAL
	}
	
	@Override
	public JPanel getConfigurationPanel() {
		panel = new JPanel();
		mtServerInfoPanel = getMTServerInfoPanel();
		final JLabel tableText = new JLabel();
		tableText.setText("Table Type: ");
		panel.add(tableText);
		final JComboBox jcb = new JComboBox(TableType.values());
		jcb.setSelectedItem(getTableType());
		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTableType(TableType.valueOf(jcb.getSelectedItem().toString()));
			}			
		});
		panel.add(jcb);
		
		
		final JLabel menuText = new JLabel();
		menuText.setText("             Menu Type: ");
		panel.add(menuText);
		final JComboBox jcbTwo = new JComboBox(MenuType.values());
		jcbTwo.setSelectedItem(getMenuType());
		jcbTwo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setMenuType(MenuType.valueOf(jcbTwo.getSelectedItem().toString()));
			}			
		});
		panel.add(jcbTwo);
		setTableType(getTableType());
				
		return panel;
	}

	@Override
	public String getName() {
		return "Table Type";
	}
	
	public void setTableType(TableType type) {
		prefs.put(PREFS_TABLE_TYPE, type.name());
		if(type.equals(TableType.LUMIN)){
			panel.add(mtServerInfoPanel);
		}else{
			panel.remove(mtServerInfoPanel);
		}
		panel.updateUI();
	}

	public TableType getTableType() {			
		return TableType.valueOf(prefs.get(PREFS_TABLE_TYPE, TableType.JMEDIRECT.name()));
	}
	
	public void setMenuType(MenuType type) {
		prefs.put(PREFS_MENU_TYPE, type.name());
	}

	public MenuType getMenuType() {			
		return MenuType.valueOf(prefs.get(PREFS_MENU_TYPE, MenuType.COMBO.name()));
	}
	
	public String getMTServerPath(){
		return prefs.get(PREFS_MT_SERVER_PATH, "");
	}
	
	public void setMTServerPath(String path){
		prefs.put(PREFS_MT_SERVER_PATH, path);
	}
	
	public boolean isAutoStartMTServer(){
		return prefs.getBoolean(PREFS_AUTO_START_MT_SERVER, false);
	}
	
	public void setAutoStartMTServer(boolean isAutoStart){
		prefs.putBoolean(PREFS_AUTO_START_MT_SERVER, isAutoStart);
	}
	
	private JPanel getMTServerInfoPanel(){
		JPanel mtServerPanel = new JPanel();
		JLabel label = new JLabel("            Auto start MT server :");	
		final JTextField serverPathText = new JTextField(30);
		serverPathText.setText(getMTServerPath());
		serverPathText.setEditable(false);
		serverPathText.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e){
				TableConfigPrefsItem.this.setMTServerPath(serverPathText.getText().trim());
			}
		});
		
		final JButton jButtonBrowse = new JButton();
		jButtonBrowse.setText("Browse");
		jButtonBrowse.setEnabled(false);
		jButtonBrowse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(new File(getMTServerPath()));
				fc.setFileFilter(new EXEFilter());
				int returnVal = fc.showOpenDialog(panel);

			    if (returnVal == JFileChooser.APPROVE_OPTION) {
			        File file = fc.getSelectedFile();
			        serverPathText.setText(file.getAbsolutePath());
			        TableConfigPrefsItem.this.setMTServerPath(serverPathText.getText().trim());
			    }
			}

		});
		
		final JCheckBox autoServerCheck = new JCheckBox();
		autoServerCheck.setSelected(isAutoStartMTServer());
		if(autoServerCheck.isSelected()){
			serverPathText.setEditable(true);
			jButtonBrowse.setEnabled(true);			
		}
		autoServerCheck.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent event) {
				serverPathText.setEditable(autoServerCheck.isSelected());
				jButtonBrowse.setEnabled(autoServerCheck.isSelected());	
				setAutoStartMTServer(autoServerCheck.isSelected());
			}
			
		});
		
		mtServerPanel.add(label);
		mtServerPanel.add(autoServerCheck);
		mtServerPanel.add(serverPathText);
		mtServerPanel.add(jButtonBrowse);
		return mtServerPanel;
		
	}
	
	public class EXEFilter extends javax.swing.filechooser.FileFilter 
	{

	  public boolean accept(File f) {
	    if (f.isDirectory()) 
	      return true;
	  
	    String extension = getExtension(f);
	    if (extension.equals("exe") || extension.equals("EXE") || extension.equals("Exe")) 
	       return true; 

	    return false;
	  }
	    
	  public String getDescription(){
	      return "Exe (executable) files";
	  }

	  private String getExtension(File f){
	    String s = f.getName();
	    int i = s.lastIndexOf('.');
	    if (i > 0 &&  i < s.length() - 1) 
	      return s.substring(i+1).toLowerCase();
	    return "";
	  }
	}
	
}
