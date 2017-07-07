package synergynet.table.config.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class ServerConfigPanel extends JPanel {
	private static final long serialVersionUID = -8701347662757286944L;
	private ServerConfigPrefsItem prefs;

    public ServerConfigPanel(ServerConfigPrefsItem serverConfigPrefsItem) {
    	this.prefs = serverConfigPrefsItem;
    	initComponents();
	}

	private void initComponents() {
		final JPanel instance = this;
        jLabel1 = new javax.swing.JLabel();
        txtWebServerPort = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtWebServerDir = new javax.swing.JTextField();
        btnSelectDir = new javax.swing.JButton();

        setName("Form"); // NOI18N

        jLabel1.setText("Web Server Port");
        txtWebServerPort.setText(prefs.getPort() + "");
        txtWebServerPort.addKeyListener(new KeyAdapter() {
        	@Override
			public void keyReleased(KeyEvent e) {
				store();
			}			

			@Override
			public void keyTyped(KeyEvent e) {
				store();
			}			
			
			private void store() {
				prefs.setPort(Integer.parseInt(txtWebServerPort.getText()));
			}
		});
        
        
        jLabel2.setText("Web Server Dir");
        txtWebServerDir.setText(prefs.getWebDirectory());
        txtWebServerDir.setEditable(false);

        btnSelectDir.setText("Select...");
        btnSelectDir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(new File(prefs.getWebDirectory()));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(instance);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            txtWebServerDir.setText(file.getAbsolutePath());
		            prefs.setWebDirectory(file.getAbsolutePath());
		        }	
			}
        	
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtWebServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtWebServerDir, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelectDir)))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtWebServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtWebServerDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelectDir))
                .addContainerGap(230, Short.MAX_VALUE))
        );
    }// </editor-fold>


    // Variables declaration - do not modify
    private javax.swing.JButton btnSelectDir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField txtWebServerDir;
    private javax.swing.JTextField txtWebServerPort;
    // End of variables declaration

}