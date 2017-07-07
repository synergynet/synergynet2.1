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

package synergynet.table.config.display;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import synergynet.table.config.ConfigurationSystem;
import synergynet.table.config.PreferencesItem;
import synergyspace.jme.sysutils.StereoRenderPass.StereoMode;

public class DisplayConfigPrefsItem implements PreferencesItem {

	private static Preferences prefs = ConfigurationSystem.getPreferences(DisplayConfigPrefsItem.class);

	private static final String DISPLAY_WIDTH = "DISPLAY_WIDTH";
	private static final String DISPLAY_HEIGHT = "DISPLAY_HEIGHT";
	private static final String DISPLAY_FREQ = "DISPLAY_FREQ";
	private static final String DISPLAY_DEPTH = "DISPLAY_DEPTH";
	private static final String DISPLAY_THREEDEE = "DISPLAY_3D";
	private static final String DISPLAY_FULLSCREEN = "DISPLAY_FULLSCREEN";
	private static final String DISPLAY_SHAPE = "DISPLAY_SHAPE";
	private static final String DISPLAY_DEFAULT_SHAPE = "DISPLAY_DEFAULT_SHAPE";
	private static final String DISPLAY_MIN_AA_SAMPLES = "DISPLAY_MIN_AA_SAMPLES";

	private JComboBox jcb = new JComboBox();
	private JComboBox jcbd = new JComboBox();
	private JCheckBox fullscreen;
	private JLabel jLabelDisplaySize = new JLabel();
	private JLabel jLabelThreeDeeMode = new JLabel();
	private JLabel jLabelDisplayShape = new JLabel();
	private JTextField jTextFieldShape = new JTextField();
	private JButton jButtonBrowse = new JButton();
    private JCheckBox cbDefaultShape = new JCheckBox();
    private JLabel lblAntiAlias = new JLabel();
    private JTextField txtAntiAliasSamples = new JTextField();

	public DisplayConfigPrefsItem() {}

	@Override
	public String getName() {
		return "Display";
	}

	@Override
	public JPanel getConfigurationPanel() {
		final JPanel panel = new JPanel();

		jLabelDisplaySize.setText("Display Size:");

		DisplayMode[] modes;
		try {
			modes = Display.getAvailableDisplayModes();
			Arrays.sort(modes, new DisplayModeComparator());
			Display.destroy();

			jcb = new JComboBox(modes);
			jcb.setSelectedIndex(getCurrentDisplayModeIndex(modes));
			jcb.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						DisplayMode m = (DisplayMode) jcb.getSelectedItem();
						setWidth(m.getWidth());
						setHeight(m.getHeight());
						setBitDepth(m.getBitsPerPixel());
						setFrequency(m.getFrequency());
					}
				}

			});

		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		fullscreen = new JCheckBox("Full Screen", getFullScreen());
		fullscreen.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
		fullscreen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setFullScreen(fullscreen.isSelected());
			}
		});

		jLabelDisplayShape.setText("Display Shape:");


        jTextFieldShape.setText(getDisplayShape());
        jTextFieldShape.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				 setDisplayShape(jTextFieldShape.getText());
			}

			@Override
			public void keyTyped(KeyEvent e) {
				setDisplayShape(jTextFieldShape.getText());
			}
        });

        jButtonBrowse.setText("Browse");
        jButtonBrowse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(new File(getDisplayShape()));
				fc.setFileFilter(new OBJFilter());
				int returnVal = fc.showOpenDialog(panel);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            jTextFieldShape.setText(file.getAbsolutePath());
		            setDisplayShape(file.getAbsolutePath());
		        }
			}

        });

        cbDefaultShape.setText("Use default shape (Rectangle)");
        cbDefaultShape.setSelected(getDefaultShape());
        cbDefaultShape.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        cbDefaultShape.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	setDefaultShape(cbDefaultShape.isSelected());
            	if (cbDefaultShape.isSelected()){
            		jTextFieldShape.setEditable(false);
            		jButtonBrowse.setEnabled(false);
            	}else{
            		jTextFieldShape.setEditable(true);
            		jButtonBrowse.setEnabled(true);
            	}
            }
        });

        if (cbDefaultShape.isSelected()){
    		jTextFieldShape.setEditable(false);
    		jButtonBrowse.setEnabled(false);
        }
        
        lblAntiAlias.setText("Anti-alias minimum samples:");
        txtAntiAliasSamples.setText(""+getMinimumAntiAliasSamples());
        txtAntiAliasSamples.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				store();
			}			

			@Override
			public void keyTyped(KeyEvent e) {
				store();
			}			
			
			private void store() {
				if(txtAntiAliasSamples.getText().length() > 0) {
					setMinimumAntiAliasSamples(Integer.parseInt(txtAntiAliasSamples.getText()));
				}
			}
		});
        
        jLabelThreeDeeMode.setText("Stereo Mode:");
        
        String[] threeDModes = {"NONE", StereoMode.ANAGLYPH.toString(), StereoMode.SIDE_BY_SIDE.toString(), StereoMode.STEREO_BUFFER.toString()};
        
        int choice = 0;
        String current = getThreeDee();
        
        for (int i = 0; i < threeDModes.length; i++){
        	if (current.equals(threeDModes[i])){
        		choice = i;
        	}
        }
        
        jcbd = new JComboBox(threeDModes);
		jcbd.setSelectedIndex(choice);  
		jcbd.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					setThreeDee((String)jcbd.getSelectedItem());
				}
			}

		});
			
		panel.setLayout(null);

		jLabelDisplaySize.setBounds(new Rectangle(30, 30, 200, 24));
		lblAntiAlias.setBounds(new Rectangle(30, 60, 200, 24));
		txtAntiAliasSamples.setBounds(new Rectangle(250, 60, 100, 24));
		jcb.setBounds(new Rectangle(400/2, 30, 200, 24));
		fullscreen.setBounds(new Rectangle((400/2) + 210, 30, 125, 24));
		jLabelDisplayShape.setBounds(new Rectangle(30, 130, 200, 24));
		jTextFieldShape.setBounds(new Rectangle(400/2, 130, 300, 24));
		jButtonBrowse.setBounds(new Rectangle(400/2+300, 130, 100, 24));
		cbDefaultShape.setBounds(new Rectangle(400/2, 155, 300, 24));
		jLabelThreeDeeMode.setBounds(new Rectangle(30, 200, 200, 24));
		jcbd.setBounds(new Rectangle(400/2, 200, 200, 24));
		

		panel.add(jLabelDisplaySize);
		panel.add(jcb);
		panel.add(fullscreen);
		panel.add(lblAntiAlias); panel.add(txtAntiAliasSamples);
		panel.add(jLabelDisplayShape);
		panel.add(jTextFieldShape);
		panel.add(jButtonBrowse);
		panel.add(cbDefaultShape);
		panel.add(jLabelThreeDeeMode);
		panel.add(jcbd);

		return panel;
	}

	public int getCurrentDisplayModeIndex(DisplayMode[] modes) {
		for(int i = 0; i < modes.length; i++) {
			DisplayMode m = modes[i];
			if(m.getHeight() == getHeight() &&
				m.getWidth() == getWidth() &&
				m.getBitsPerPixel() == getBitDepth() &&
				m.getFrequency() == getFrequency())
			{
				return i;
			}
		}

		return -1;
	}

	public DisplayMode getCurrentDisplayMode(DisplayMode[] modes) {
		for(DisplayMode m : modes) {
			if(m.getHeight() == getHeight() &&
				m.getWidth() == getWidth() &&
				m.getBitsPerPixel() == getBitDepth() &&
				m.getFrequency() == getFrequency())
			{
				return m;
			}
		}

		return null;
	}

	public void setWidth(int w) {
		prefs.putInt(DISPLAY_WIDTH, w);
	}

	public int getWidth() {
		return prefs.getInt(DISPLAY_WIDTH, 1024);
	}

	public int getHeight() {
		return prefs.getInt(DISPLAY_HEIGHT, 768);
	}

	public void setHeight(int h) {
		prefs.putInt(DISPLAY_HEIGHT, h);
	}

	public int getBitDepth() {
		return prefs.getInt(DISPLAY_DEPTH, 16);
	}

	public void setBitDepth(int b) {
		prefs.putInt(DISPLAY_DEPTH, b);
	}

	public int getFrequency() {
		return prefs.getInt(DISPLAY_FREQ, -1);
	}

	public void setFrequency(int f) {
		prefs.putInt(DISPLAY_FREQ, f);
	}

	public boolean getFullScreen() {
		return prefs.getBoolean(DISPLAY_FULLSCREEN, false);
	}
	
	public void setThreeDee(String s) {
		prefs.put(DISPLAY_THREEDEE, s);
	}

	public String getThreeDee() {
		return prefs.get(DISPLAY_THREEDEE, "NONE");
	}
	
	public void setFullScreen(boolean fs) {
		prefs.putBoolean(DISPLAY_FULLSCREEN, fs);
	}

	public int getMinimumAntiAliasSamples() {
		return prefs.getInt(DISPLAY_MIN_AA_SAMPLES, 0);
	}
	
	public void setMinimumAntiAliasSamples(int samples) {
		prefs.putInt(DISPLAY_MIN_AA_SAMPLES, samples);
	}

	private void setDisplayShape(String s) {
		prefs.put(DISPLAY_SHAPE, s);
	}

	public String getDisplayShape() {
		return prefs.get(DISPLAY_SHAPE, "");
	}

	public void setDefaultShape(boolean fs) {
		prefs.putBoolean(DISPLAY_DEFAULT_SHAPE, fs);
	}

	public boolean getDefaultShape() {
		return prefs.getBoolean(DISPLAY_DEFAULT_SHAPE, true);
	}
	
	public class OBJFilter extends javax.swing.filechooser.FileFilter 
	{

	  public boolean accept(File f) {
	    if (f.isDirectory()) 
	      return true;
	  
	    String extension = getExtension(f);
	    if ((extension.equals("obj")) || (extension.equals("Obj"))) 
	       return true; 

	    return false;
	  }
	    
	  public String getDescription(){
	      return "Obj (Waveform) files";
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