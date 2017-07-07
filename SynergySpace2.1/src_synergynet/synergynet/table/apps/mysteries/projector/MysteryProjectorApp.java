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

package synergynet.table.apps.mysteries.projector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.jme.scene.Spatial;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.OrthoContentItem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.QuadContentItem;
import synergynet.contentsystem.items.innernotecontroller.InnerNoteController;
import synergynet.contentsystem.items.innernotecontroller.InnerNoteEditor;
import synergynet.services.ServiceManager;
import synergynet.services.exceptions.CouldNotStartServiceException;
import synergynet.services.net.networkedcontentmanager.NetworkedContentListener;
import synergynet.services.net.networkedcontentmanager.NetworkedContentManager;
import synergynet.services.net.networkedcontentmanager.messagehandler.DefaultMessageHandler;
import synergynet.services.net.networkedcontentmanager.synchroniseddatarender.SynchronisedDataRender;
import synergynet.services.net.networkedcontentmanager.utils.RemoteDesktop;
import synergynet.services.net.tablecomms.client.TableCommsClientService;
import synergynet.services.net.tablecomms.messages.control.fromclient.ApplicationCommsRequest;
import synergynet.table.SynergyNetAppUtils;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergynet.table.apps.mysteries.controller.MysteriesControllerApp;

public class MysteryProjectorApp extends DefaultSynergyNetApp implements NetworkedContentListener {

	
	private TableCommsClientService comms;
	protected ContentSystem contentSystem;
	protected DefaultMessageHandler messageHandler;
	protected NetworkedContentManager networkedContentManager;
	
	protected InnerNoteController innerNoteController;
	
	private File restoreFolder;
	private File exitSettingsFile;
	public static boolean restore = true;
	protected String currentSubApp = "";
	private boolean isLogEnabled = false;
	

	public MysteryProjectorApp(ApplicationInfo info) {
		super(info);

		innerNoteController = new InnerNoteController();
		
		restoreFolder = new File(getApplicationDataDirectory(), "restore");	
		exitSettingsFile = new File(getApplicationDataDirectory(), "safeExitSettings.properties");		
		checkLastExitSettings();
				
		/*
		if(restore){	
			for(String mysteryID: mysteryIDToXMLPath.keySet())
				mysteriesToRestore.add(mysteryID);
		}
		*/
			
	}

	@Override
	public void addContent() {
		
		setDefaultSteadfastLimit(1);
		
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		SynergyNetAppUtils.addTableOverlay(this);

	}
	
	@Override
	public void onActivate() {
		if(comms == null) {
					
			try {
				comms = (TableCommsClientService) ServiceManager.getInstance().get(TableCommsClientService.class);
			} catch (CouldNotStartServiceException e1) {
				e1.printStackTrace();
			}
			List<Class<?>> receiverClasses = new ArrayList<Class<?>>();
			receiverClasses.add(MysteriesControllerApp.class);
			receiverClasses.add(MysteryProjectorApp.class);
			this.networkedContentManager = new NetworkedContentManager(contentSystem, comms, receiverClasses);
			this.networkedContentManager.addNetworkedContentListener(this);
			
			ArrayList<Class<?>> controllerClasses = new ArrayList<Class<?>>();
			ArrayList<Class<?>> projectorClasses = new ArrayList<Class<?>>();
			controllerClasses.add(MysteriesControllerApp.class);
			projectorClasses.add(this.getClass());
			
			this.networkedContentManager.createProjectorController(controllerClasses, projectorClasses);
			messageHandler = new DefaultMessageHandler(contentSystem, this.networkedContentManager);
			try {
				if(comms != null) comms.register(this, messageHandler);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			if(comms != null) comms.sendMessage(new ApplicationCommsRequest(MysteryProjectorApp.class.getName()));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public void stateUpdate(float tpf) {
		
		
		
		if(networkedContentManager!= null) networkedContentManager.stateUpdate(tpf);	
		if(contentSystem != null) contentSystem.update(tpf);
		if(currentSubApp != null && isLogEnabled){
			try {
				logContentState(currentSubApp);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}
	
	@Override
	public void onDeactivate() {}

	
	public void loadContent(String filePath, String name){
		
		this.removeAllItems();
		networkedContentManager.loadLocalContent(filePath, name);		
		
		this.contentLoaded();		
	}
	
	
	
	@Override
	public void contentLoaded() {}
	
	public void removeAllItems(){	
		this.innerNoteController.removeAllNoteEditors();		
		networkedContentManager.removeAllItems();
	}
	
	@Override
	public void renderSynchronisedDate(ContentItem item,
			Map<String, String> itemAttrs) {
		SynchronisedDataRender.render((OrthoContentItem)item, itemAttrs, this.innerNoteController);
		SynchronisedDataRender.renderNote((OrthoContentItem)item, itemAttrs, this.innerNoteController);
		if (innerNoteController.getNodeEditor((QuadContentItem)item)!= null){	
			InnerNoteEditor innerNoteEditor = innerNoteController.getNodeEditor((QuadContentItem)item);
			innerNoteEditor.removeInnerNoteEventListeners();
			innerNoteEditor.getNoteNode().setRotateTranslateScalable(false);
			innerNoteEditor.getNoteNode().removeItemListerners();
		}
	}
	
	private void logContentState(String mysteryID) throws FileNotFoundException {
		if(!restoreFolder.exists()) restoreFolder.mkdir();
		File restoreFile = new File(restoreFolder, mysteryID);
		PrintWriter pw = new PrintWriter(new FileOutputStream(restoreFile));
		pw.println("# Last state for app ID: " + mysteryID);
		pw.println("# Storing started at " + new Date().toString());
		pw.println("# Format is as follows:");
		pw.println("# content Item name, location x, location y, location z, scale x, scale y, scale z, rotation x, rotation y, rotation z, rotation w, zOrder");
		for(ContentItem item: contentSystem.getAllContentItems().values()) {

			Spatial spatial = (Spatial)item.getImplementationObject();
			pw.print(spatial.getName() + ",");
			pw.print(spatial.getLocalTranslation().x + ",");
			pw.print(spatial.getLocalTranslation().y + ",");
			pw.print(spatial.getLocalTranslation().z + ",");
			pw.print(spatial.getLocalScale().x + ",");
			pw.print(spatial.getLocalScale().y + ",");
			pw.print(spatial.getLocalScale().z + ",");
			pw.print(spatial.getLocalRotation().x + ",");
			pw.print(spatial.getLocalRotation().y + ",");
			pw.print(spatial.getLocalRotation().z + ",");
			pw.print(spatial.getLocalRotation().w + ",");
			pw.println(spatial.getZOrder());
		}
		pw.close();
	}
	
	private void checkLastExitSettings(){
		try{
			if(!exitSettingsFile.exists()) exitSettingsFile.createNewFile();
			FileInputStream is = new FileInputStream(exitSettingsFile);
			Properties properties = new Properties();
			properties.load(is);
			String isRestore = properties.getProperty("restore");
			is.close();
			if(isRestore != null && isRestore.equals("1"))	
				MysteryProjectorApp.restore = true;
			else {
				MysteryProjectorApp.restore = false;
				properties.setProperty("restore", "1");
				FileOutputStream os = new FileOutputStream(exitSettingsFile);
				properties.store(os, null);
				os.close();
			}
		}
		catch(IOException exp){
			exp.printStackTrace();
		}
	}
	/*
	private void setSafeExit(){
		try{
			if(!exitSettingsFile.exists()) exitSettingsFile.createNewFile();
			FileOutputStream os = new FileOutputStream(exitSettingsFile);
			Properties properties = new Properties();
			properties.setProperty("restore", "0");
			properties.store(os, "Safe exit on "+ new Date());
			os.close();
		}
		catch(IOException exp){
			exp.printStackTrace();
		}		
	}
	*/

	@Override
	public void channelSwitched() {
	
	}

	@Override
	public void contentItemLoaded(ContentItem item) {
		
	}

	@Override
	public void remoteContentLoaded(RemoteDesktop remoteDesktop) {
	}

	@Override
	public void renderRemoteDesktop(RemoteDesktop remoteDesktop,
			OrthoContentItem item, Map<String, String> map) {
		
	}



	
}
