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

package synergynet.table.config.developer;

import javax.swing.JPanel;

/**
 *
 * @author dcs0ah1
 */
public class DeveloperConfigPanel extends JPanel {

	private static final long serialVersionUID = 1959347964564852506L;
	private DeveloperConfigPrefsItem prefsItem;


	/** Creates new form DeveloperConfigPanel
	 * @param developerConfigPrefsItem */
    public DeveloperConfigPanel(DeveloperConfigPrefsItem developerConfigPrefsItem) {
    	this.prefsItem = developerConfigPrefsItem;
        initComponents();
    }

    private void initComponents() {

        cbEnableSceneMonitor = new javax.swing.JCheckBox();

        cbEnableSceneMonitor.setText("Enable Scene Monitor"); // NOI18N
        cbEnableSceneMonitor.setSelected(prefsItem.getShowSceneMonitor());
        cbEnableSceneMonitor.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        cbEnableSceneMonitor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableSceneMonitor(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
            	.addContainerGap()
                .addComponent(cbEnableSceneMonitor)
                .addContainerGap(228, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(cbEnableSceneMonitor)
                .addContainerGap(271, Short.MAX_VALUE))
        );
    }// </editor-fold>

    private void enableSceneMonitor(java.awt.event.ActionEvent evt) {
    	prefsItem.setShowSceneMonitor(cbEnableSceneMonitor.isSelected());
    }

    // Variables declaration - do not modify
    private javax.swing.JCheckBox cbEnableSceneMonitor;
    // End of variables declaration

}
