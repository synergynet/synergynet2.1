package synergynet.table.config.network;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author  dcs0ah1
 */
public class NetworkPrefsPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 1808994055508198383L;
	private NetworkConfigPrefsItem prefs;
	public NetworkPrefsPanel(NetworkConfigPrefsItem networkConfigPrefsItem) {
		this.prefs = networkConfigPrefsItem;
		initComponents();
	}

	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jTextField2 = new javax.swing.JTextField();
		jCheckBox1 = new javax.swing.JCheckBox();

		setName("Form"); // NOI18N

		jLabel1.setText("HTTP Proxy Host");
		jLabel2.setText("HTTP Proxy Port");

		jTextField1.setText(prefs.getProxyHost());
		jTextField1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				store();
			}			

			@Override
			public void keyTyped(KeyEvent e) {
				store();
			}			
			
			private void store() {
				prefs.setProxyHost(jTextField1.getText());
			}
		});
		
		jTextField2.setText(prefs.getProxyPort() + "");
		jTextField2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				store();
			}			

			@Override
			public void keyTyped(KeyEvent e) {
				store();
			}			
			
			private void store() {
				prefs.setProxyPort(Integer.parseInt(jTextField2.getText()));
			}
		});

		jCheckBox1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
		jCheckBox1.setText("HTTP Proxy enabled");
		jCheckBox1.setSelected(prefs.getProxyEnabled());
		jCheckBox1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				prefs.setProxyEnabled(jCheckBox1.isSelected());
			}
			
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addGroup(layout.createSequentialGroup()
												.addComponent(jLabel1)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(jTextField1))
												.addGroup(layout.createSequentialGroup()
														.addComponent(jLabel2)
														.addGap(18, 18, 18)
														.addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
														.addComponent(jCheckBox1))
														.addContainerGap(75, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel1)
								.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel2)
										.addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jCheckBox1)
										.addContainerGap(191, Short.MAX_VALUE))
		);
	}// </editor-fold>


	// Variables declaration - do not modify
	private javax.swing.JCheckBox jCheckBox1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JTextField jTextField2;
	// End of variables declaration

}