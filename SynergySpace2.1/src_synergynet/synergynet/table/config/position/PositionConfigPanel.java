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

package synergynet.table.config.position;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PositionConfigPanel extends JPanel {

	private static final long serialVersionUID = 1959347964564852506L;
	private PositionConfigPrefsItem prefsItem;

    private JCheckBox cbEnableNormalPositionMode = new JCheckBox();
    private JCheckBox cbEnableDeveloperMode = new JCheckBox();
	private JLabel jLabelTablePosition = new JLabel();
	private JLabel jLabelRotationAngle = new JLabel();
	private JLabel jLabelX = new JLabel();
	private JLabel jLabelY = new JLabel();
	private JLabel jLabelWarning = new JLabel();
	private JTextField jTextFieldPositionX = new JTextField();
	private JTextField jTextFieldPositionY = new JTextField();
	private JTextField jTextFieldAngle = new JTextField();
	private JLabel jLabelTableDistances = new JLabel();
	private JLabel jLabelXDistance = new JLabel();
	private JLabel jLabelYDistance = new JLabel();
	private JTextField jTextFieldDistanceX = new JTextField();
	private JTextField jTextFieldDistanceY = new JTextField();
    private JCheckBox horizontal = new JCheckBox();
    private JCheckBox vertical = new JCheckBox();

	private JLabel jLabelTableLimits = new JLabel();
	private JLabel jLabelXLimit = new JLabel();
	private JLabel jLabelYLimit = new JLabel();
	private JTextField jTextFieldLimitX = new JTextField();
	private JTextField jTextFieldLimitY = new JTextField();
	private JLabel jLabelPlacement = new JLabel();



    public PositionConfigPanel(PositionConfigPrefsItem positionConfigPrefsItem) {
    	this.prefsItem = positionConfigPrefsItem;
        initComponents();
    }

    private void initComponents() {

	    setLayout(null);

        cbEnableNormalPositionMode.setText("Enable User Defined Table Positioning");
        cbEnableNormalPositionMode.setSelected(!prefsItem.getDeveloperMode());
        cbEnableNormalPositionMode.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        cbEnableNormalPositionMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if (cbEnableNormalPositionMode.isSelected()){
                	cbEnableDeveloperMode.setSelected(!cbEnableNormalPositionMode.isSelected());
                	enableDeveloperMode(evt);
            	}else{
            		cbEnableNormalPositionMode.setSelected(true);
            	}
            }
        });

	    jLabelTablePosition.setText("Position:");

	    jLabelX.setText("X = ");

	    jTextFieldPositionX.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!prefsItem.setXPos(jTextFieldPositionX.getText())){
					warn();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (!prefsItem.setXPos(jTextFieldPositionX.getText())){
					warn();
				}
			}
		});

	    jLabelY.setText("Y =");

	    jTextFieldPositionY.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!prefsItem.setYPos(jTextFieldPositionY.getText())){
					warn();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (!prefsItem.setYPos(jTextFieldPositionY.getText())){
					warn();
				}
			}
		});

	    jLabelRotationAngle.setText("Rotation Angle (in degrees) = ");

	    jTextFieldAngle.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!prefsItem.setAngle(jTextFieldAngle.getText())){
					warn();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (!prefsItem.setAngle(jTextFieldAngle.getText())){
					warn();
				}
			}
		});

        cbEnableDeveloperMode.setText("Enable Developer Mode");
        cbEnableDeveloperMode.setSelected(prefsItem.getDeveloperMode());
        cbEnableDeveloperMode.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        cbEnableDeveloperMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if (cbEnableDeveloperMode.isSelected()){
	            	cbEnableNormalPositionMode.setSelected(!cbEnableNormalPositionMode.isSelected());
	                enableDeveloperMode(evt);
            	}else{
            		cbEnableDeveloperMode.setSelected(true);
            	}
            }
        });

        jLabelTableDistances.setText("Distances between displays:");

        jLabelXDistance.setText("X = ");

        jTextFieldDistanceX.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!prefsItem.setXDistance(jTextFieldDistanceX.getText())){
					warn();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (!prefsItem.setXDistance(jTextFieldDistanceX.getText())){
					warn();
				}
			}
		});

	    jLabelYDistance.setText("Y =");

	    jTextFieldDistanceY.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!prefsItem.setYDistance(jTextFieldDistanceY.getText())){
					warn();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (!prefsItem.setYDistance(jTextFieldDistanceY.getText())){
					warn();
				}
			}
		});

	    jLabelTableLimits.setText("Grid Limit (0 = no limit):");

	    jLabelXLimit.setText("X = ");

	    jTextFieldLimitX.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!prefsItem.setXLimit(jTextFieldLimitX.getText())){
					warn();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (!prefsItem.setXLimit(jTextFieldLimitX.getText())){
					warn();
				}
			}
		});

	    jLabelYLimit.setText("Y =");

	    jTextFieldLimitY.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!prefsItem.setYLimit(jTextFieldLimitY.getText())){
					warn();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (!prefsItem.setYLimit(jTextFieldLimitY.getText())){
					warn();
				}
			}
		});

	    jLabelPlacement.setText("Placement:");

	    horizontal.setText("Horizontal");
	    horizontal.setSelected(prefsItem.getHorizontalPlacement());
	    horizontal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if (horizontal.isSelected()){
	            	vertical.setSelected(!horizontal.isSelected());
	            	enableHorizontalPlacement(evt);
            	}else{
            		horizontal.setSelected(true);
            	}
            }
        });

	    vertical.setText("Vertical");
	    vertical.setSelected(!prefsItem.getHorizontalPlacement());

	    vertical.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if (horizontal.isSelected()){
	            	horizontal.setSelected(!vertical.isSelected());
	            	enableHorizontalPlacement(evt);
            	}else{
            		vertical.setSelected(false);
            	}
            }
        });


	    jLabelWarning.setText("");

        cbEnableNormalPositionMode.setBounds(new Rectangle(10, 10, 300, 24));
        jLabelTablePosition.setBounds(new Rectangle(50, 30, 133, 46));
	    jLabelX.setBounds(new Rectangle(270, 30, 39, 46));
	    jTextFieldPositionX.setBounds(new Rectangle(295, 42, 57, 24));
	    jLabelY.setBounds(new Rectangle(420, 30, 39, 46));
	    jTextFieldPositionY.setBounds(new Rectangle(445, 42, 57, 24));
	    jLabelRotationAngle.setBounds(new Rectangle(50, 65, 250, 46));
	    jTextFieldAngle.setBounds(new Rectangle(270, 77, 57, 24));
        cbEnableDeveloperMode.setBounds(new Rectangle(10, 150, 300, 24));
	    jLabelTableDistances.setBounds(new Rectangle(50, 170, 210, 46));
	    jLabelXDistance.setBounds(new Rectangle(270, 170, 39, 46));
	    jTextFieldDistanceX.setBounds(new Rectangle(295, 182, 57, 24));
	    jLabelYDistance.setBounds(new Rectangle(420, 170, 39, 46));
	    jTextFieldDistanceY.setBounds(new Rectangle(445, 182, 57, 24));
	    jLabelTableLimits.setBounds(new Rectangle(50, 205, 200, 46));
	    jLabelXLimit.setBounds(new Rectangle(270, 205, 39, 46));
	    jTextFieldLimitX.setBounds(new Rectangle(295, 217, 57, 24));
	    jLabelYLimit.setBounds(new Rectangle(420, 205, 39, 46));
	    jTextFieldLimitY.setBounds(new Rectangle(445, 217, 57, 24));
	    jLabelPlacement.setBounds(new Rectangle(50, 240, 210, 46));
	    horizontal.setBounds(new Rectangle(295, 252, 150, 24));
	    vertical.setBounds(new Rectangle(445, 252, 155, 24));
	    jLabelWarning.setBounds(new Rectangle(26, 300, 500, 46));


	    add(cbEnableNormalPositionMode, null);
	    add(jLabelTablePosition, null);
	    add(jLabelX, null);
	    add(jTextFieldPositionX, null);
	    add(jLabelY, null);
	    add(jTextFieldPositionY, null);
	    add(jLabelRotationAngle, null);
	    add(jTextFieldAngle, null);
	    add(cbEnableDeveloperMode, null);
	    add(jLabelTableDistances, null);
	    add(jLabelXDistance, null);
	    add(jTextFieldDistanceX, null);
	    add(jLabelYDistance, null);
	    add(jTextFieldDistanceY, null);
	    add(jLabelTableLimits, null);
	    add(jLabelXLimit, null);
	    add(jTextFieldLimitX, null);
	    add(jLabelYLimit, null);
	    add(jTextFieldLimitY, null);
	    add(jLabelPlacement, null);
	    add(horizontal,null);
	    add(vertical,null);
	    add(jLabelWarning, null);
	    loadPreferences();

    }

	private void warn(){
		jLabelWarning.setText("Invalid parameters.  Please enter numeric values.");
		new Thread(){
			public void run(){
				try {
					sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				jLabelWarning.setText("");
			}
		}.start();
	}

	private void loadPreferences(){
		jTextFieldPositionX.setText(prefsItem.getXPos());
		jTextFieldPositionY.setText(prefsItem.getYPos());
		jTextFieldAngle.setText(prefsItem.getAngle());
		jTextFieldDistanceX.setText(prefsItem.getGridDistanceX());
		jTextFieldDistanceY.setText(prefsItem.getGridDistanceY());
		jTextFieldLimitX.setText(prefsItem.getGridLimitX());
		jTextFieldLimitY.setText(prefsItem.getGridLimitY());
	}

	private void enableDeveloperMode(ActionEvent evt) {
		prefsItem.setDeveloperMode(cbEnableDeveloperMode.isSelected());
	}

	private void enableHorizontalPlacement(ActionEvent evt) {
		prefsItem.setHorizontalPlacement(horizontal.isSelected());
	}
}
