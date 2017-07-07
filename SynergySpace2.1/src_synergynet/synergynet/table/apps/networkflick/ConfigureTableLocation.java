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

package synergynet.table.apps.networkflick;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.*;

import synergynet.table.config.ConfigurationSystem;
import synergynet.table.config.position.PositionConfigPrefsItem;


public class ConfigureTableLocation extends JFrame implements ActionListener{

  /**
	 *
	 */
	private static final long serialVersionUID = -5429875875260416L;

private JLabel jLabelTablePosition = new JLabel();
  private JLabel jLabelRotationAngle = new JLabel();
  private JTextField jTextFieldPositionX = new JTextField();
  private JLabel jLabelX = new JLabel();
  private JLabel jLabelY = new JLabel();
  private JTextField jTextFieldPositionY = new JTextField();
  private JTextField jTextFieldAngle = new JTextField();
  private JButton jButtonOK = new JButton();
  private JButton jButtonCancel = new JButton();
  private final Preferences prefs = ConfigurationSystem.getPreferences(PositionConfigPrefsItem.class);

  public ConfigureTableLocation() {
	  		this.setTitle("Table Settings");
		    jLabelTablePosition.setText("Position");
		    jLabelTablePosition.setBounds(new Rectangle(25, 15, 133, 46));
		    this.getContentPane().setLayout(null);
		    jLabelRotationAngle.setText("Rotation Angle (in degrees)");
		    jLabelRotationAngle.setBounds(new Rectangle(26, 59, 171, 46));
		    jTextFieldPositionX.setBounds(new Rectangle(211, 28, 57, 24));
		    jLabelX.setText("X = ");
		    jLabelX.setBounds(new Rectangle(185, 25, 39, 29));
		    jLabelY.setText("Y =");
		    jLabelY.setBounds(new Rectangle(290, 25, 37, 29));
		    jTextFieldPositionY.setBounds(new Rectangle(316, 28, 57, 24));
		    jTextFieldAngle.setBounds(new Rectangle(210, 71, 57, 24));
		    jButtonOK.setBounds(new Rectangle(120, 116, 81, 23));
		    jButtonOK.setText("OK");
		    jButtonOK.setActionCommand("OK");
		    jButtonOK.addActionListener(this);
		    jButtonCancel.setBounds(new Rectangle(204, 116, 81, 23));
		    jButtonCancel.setText("Cancel");
		    jButtonCancel.setActionCommand("Cancel");
		    jButtonCancel.addActionListener(this);
		    this.getContentPane().add(jLabelRotationAngle, null);
		    this.getContentPane().add(jTextFieldPositionX, null);
		    this.getContentPane().add(jTextFieldPositionY, null);
		    this.getContentPane().add(jButtonOK, null);
		    this.getContentPane().add(jButtonCancel, null);
		    this.getContentPane().add(jLabelTablePosition, null);
		    this.getContentPane().add(jLabelX, null);
		    this.getContentPane().add(jLabelY, null);
		    this.getContentPane().add(jTextFieldAngle, null);
		    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		    this.setSize(new Dimension(400,190));
		    this.setLocationRelativeTo(null);
		    loadPreferences();
		    this.setVisible(true);
	    }
  public static void main(String[] args) {
    new ConfigureTableLocation();
  }
@Override
public void actionPerformed(ActionEvent e) {
	if(e.getActionCommand().equals("OK")){
		try{
			prefs.putInt(PositionConfigPrefsItem.PREFS_LOCATION_X, Integer.parseInt(jTextFieldPositionX.getText()));
			prefs.putInt(PositionConfigPrefsItem.PREFS_LOCATION_Y, Integer.parseInt(jTextFieldPositionY.getText()));
			prefs.putFloat(PositionConfigPrefsItem.PREFS_ANGLE, Float.parseFloat(jTextFieldAngle.getText()));
			System.exit(0);
		} catch(Exception exp){
			JOptionPane.showMessageDialog(this, "Invalid parameters. Please enter numeric values", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	else if(e.getActionCommand().equals("Cancel"))
		System.exit(0);
}

private void loadPreferences(){
	jTextFieldPositionX.setText(prefs.get(PositionConfigPrefsItem.PREFS_LOCATION_X, "0"));
	jTextFieldPositionY.setText(prefs.get(PositionConfigPrefsItem.PREFS_LOCATION_Y, "0"));
	jTextFieldAngle.setText(prefs.get(PositionConfigPrefsItem.PREFS_ANGLE, "0"));
}

}

