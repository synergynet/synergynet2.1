package synergynet.table.config.apikeys;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
 * APIKeyPanel.java
 *
 * Created on November 14, 2009, 9:58 PM
 */
public class APIKeyPanel extends javax.swing.JPanel {
	private static final long serialVersionUID = 723710188130075122L;
	private APIKeysConfigPrefsItem prefs;
	/** Creates new form APIKeyPanel 
	 * @param apiKeysConfigPrefsItem */
	public APIKeyPanel(APIKeysConfigPrefsItem apiKeysConfigPrefsItem) {
		this.prefs = apiKeysConfigPrefsItem;
		initComponents();
	}

	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		flickrAPIKey = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		flickrAPISecret = new javax.swing.JTextField();

		setName("Form"); // NOI18N

		jLabel1.setText("Flickr API Key");
		flickrAPIKey.setText(prefs.getFlickrAPIKey());
		flickrAPIKey.addKeyListener(new KeyAdapter() {	
			@Override
			public void keyReleased(KeyEvent e) {
				store();
			}			

			@Override
			public void keyTyped(KeyEvent e) {
				store();
			}			
			
			private void store() {
				prefs.setFlickrAPIKey(flickrAPIKey.getText());	
			}
		});

		jLabel2.setText("Flickr API Secret");
		flickrAPISecret.setText(prefs.getFlickrAPISecret());
		flickrAPISecret.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				store();
			}
			
			@Override
			public void keyTyped(KeyEvent e) {
				store();
			}

			private void store() {
				prefs.setFlickrAPISecret(flickrAPISecret.getText());				
			}		
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addGroup(layout.createSequentialGroup()
										.addComponent(jLabel1)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(flickrAPIKey, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGroup(layout.createSequentialGroup()
												.addComponent(jLabel2)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(flickrAPISecret)))
												.addContainerGap(45, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel1)
								.addComponent(flickrAPIKey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel2)
										.addComponent(flickrAPISecret, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap(232, Short.MAX_VALUE))
		);
	}


	// Variables declaration - do not modify
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JTextField flickrAPIKey;
	private javax.swing.JTextField flickrAPISecret;
	// End of variables declaration

}
