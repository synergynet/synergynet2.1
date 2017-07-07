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

package synergynet.table.apps.remotecontrol;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.BackgroundController;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.Frame;
import synergynet.contentsystem.items.ListContainer;
import synergynet.contentsystem.items.SimpleButton;
import synergynet.contentsystem.items.listener.ItemEventAdapter;
import synergynet.contentsystem.items.listener.SimpleButtonAdapter;
import synergynet.contentsystem.items.listener.SubAppMenuEventListener;
import synergynet.services.ServiceManager;
import synergynet.services.exceptions.CouldNotStartServiceException;
import synergynet.services.exceptions.ServiceNotRunningException;
import synergynet.services.net.netservicediscovery.NetworkServiceDiscoveryService;
import synergynet.services.net.tablecomms.client.TableCommsApplicationListener;
import synergynet.services.net.tablecomms.client.TableCommsClientService;
import synergynet.table.SynergyNetDesktop;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.menucontrol.HoldBottomLeftExit;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergynet.table.apps.mathpadapp.conceptmapping.GraphManager;
import synergynet.table.apps.mysteries.utils.SubAppMenu;
import synergynet.table.apps.remotecontrol.Test01.SyncTester;
import synergynet.table.apps.remotecontrol.networkmanager.handlers.DefaultMessageHandler;
import synergynet.table.apps.remotecontrol.networkmanager.managers.NetworkedContentManager;
import synergynet.table.apps.remotecontrol.networkmanager.managers.TablePortalManager;
import synergynet.table.apps.remotecontrol.tableportal.TablePortal;
import synergynet.table.apps.remotecontrol.tableportal.WorkspaceManager;

public class TablePortalApp extends DefaultSynergyNetApp implements TableCommsApplicationListener{

	private static int COMICSTRIP_MODE = 1;
	//private static int EMPTY_TABLE_MODE = 2;
	private static int MYSTERY_MODE = 3;

	private int currentMode = COMICSTRIP_MODE;
	private boolean enableSimulation = true;

	
	protected TableCommsClientService comms;
	protected DefaultMessageHandler messageHandler;
	protected NetworkedContentManager networkManager;
	protected GraphManager graphManager;
	private ContentSystem contentSystem;
	private static final Logger log = Logger.getLogger(TableCommsClientService.class.getName());

	
	protected SubAppMenu subAppMenu;
	protected ListContainer menu;
	protected String currentSubApp = "";
	
	private List<ContentItem> items = new ArrayList<ContentItem>();
	protected SyncTester syncTester;
	
	public TablePortalApp(ApplicationInfo info) {
		super(info);

	}

	@Override
	public void addContent() {
//		setAllContentSteadfast();
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		graphManager = new GraphManager(contentSystem);
		setMenuController(new HoldBottomLeftExit());
		SimpleButton createPortalBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		createPortalBtn.setText("Create Portal");
		createPortalBtn.setLocation(55,20);
		createPortalBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				TablePortal portal = new TablePortal(contentSystem, networkManager, graphManager);
				TablePortalManager.getInstance().registerTablePortal(portal);
			}
		});
		
		if(enableSimulation) syncTester = new SyncTester();

		if(currentMode == COMICSTRIP_MODE){
			File dir = new File("src_synergynet/synergynet/table/apps/remotecontrol/comicstrip");
			String[] children = dir.list();
			if (children == null) {
				log.severe("Could not read folder content");
			} else {
			    for (int i=0; i<children.length; i++) {
			    	String filename = children[i];
			    	if(!filename.endsWith("jpg") && !filename.endsWith("jpeg") && !filename.endsWith("png") && !filename.endsWith("bmp") && !filename.endsWith("gif")) continue;
			        Frame frame = (Frame) contentSystem.createContentItem(Frame.class);
			        frame.setBorderSize(3);
			        ImageIcon image = new ImageIcon(TablePortalApp.class.getResource("comicstrip/"+filename));
			        frame.setWidth(image.getIconWidth());
			        frame.setHeight(image.getIconHeight());
			        frame.setScale(0.3f);
			        frame.drawImage(TablePortalApp.class.getResource("comicstrip/"+filename));
			        frame.rotateRandom();
			        frame.setLocalLocation(100, 500);
			        items.add(frame);
			    }
				if(enableSimulation && syncTester != null)	syncTester.setItems(items);
			}
		}
		
		if(currentMode == MYSTERY_MODE){
			BackgroundController backgroundController;
			backgroundController = (BackgroundController)contentSystem.createContentItem(BackgroundController.class);
			backgroundController.setOrder(Integer.MIN_VALUE);
		
			subAppMenu = new SubAppMenu(contentSystem);
			subAppMenu.addSubAppMenuEventListener(new SubAppMenuEventListener(){
				@Override
				public void menuSelected(String filePath, String appName) {
					loadContent(filePath, appName);				
				}			
			});
		
			SimpleButton backToMainMenuButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);		
			backToMainMenuButton.setAutoFitSize(false);
			backToMainMenuButton.setText("BACK TO MAIN MENU");
			backToMainMenuButton.setBackgroundColour(Color.lightGray);
			backToMainMenuButton.addButtonListener(new SimpleButtonAdapter(){
				public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {
					try {
						SynergyNetDesktop.getInstance().showMainMenu();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}			
			});	
			
			SimpleButton closeMenuButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);		
			closeMenuButton.setAutoFitSize(false);
			closeMenuButton.setText("HIDE MENU");
			closeMenuButton.setBackgroundColour(Color.lightGray);
			closeMenuButton.addButtonListener(new SimpleButtonAdapter(){
				public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {
					menu.setVisible(false);
				}			
			});

			menu = subAppMenu.getSubAppMenu();
			menu.addSubItem(backToMainMenuButton);
			menu.addSubItem(closeMenuButton);
			menu.setLocalLocation(200, 200);
			menu.setBackgroundColour(Color.blue);
	
			backgroundController.addItemListener(new ItemEventAdapter(){ 
	
				boolean topRightCornerSelected = false;
	
				@Override
				public void cursorPressed(ContentItem item, long id, float x,
						float y, float pressure) {
					super.cursorPressed(item, id, x, y, pressure);
					Rectangle rightTopCorner = new Rectangle(contentSystem.getScreenWidth()-30, 0, contentSystem.getScreenWidth(), 30);
					if (rightTopCorner.contains(x, y)) topRightCornerSelected = true;
				}
	
				@Override
				public void cursorReleased(ContentItem item, long id, float x,
						float y, float pressure) {
					super.cursorReleased(item, id, x, y, pressure);
					Rectangle rightTopCorner = new Rectangle(contentSystem.getScreenWidth()-30, contentSystem.getScreenWidth(), 30, 30);
					if (rightTopCorner.contains(x, y)) topRightCornerSelected = false;
	
				}
	
	
	
				@Override
				public void cursorLongHeld(ContentItem b, long id, float x, float y, float pressure) {
					Rectangle rightBottomCorner = new Rectangle(0, contentSystem.getScreenHeight()-30, 30, contentSystem.getScreenHeight());
					if (!rightBottomCorner.contains(x, y)) return;
					if (!topRightCornerSelected) return;
					if (menu.isVisible()){
						menu.setVisible(false);
					}
					else{
						menu.setVisible(true);
						menu.setLocalLocation(x, contentSystem.getScreenHeight()-y);
					}
				}
			});
		}
	}
	
	protected void loadContent(String filePath, String appName) {
		removeAllItems();
		items.addAll(contentSystem.loadContentItems(filePath));
		/*
		if(enableSimulation){
			for(ContentItem item: items){
				((OrthoContentItem)item).placeRandom();
				((OrthoContentItem)item).rotateRandom();
			}
		}else{
			for(ContentItem item: items){
				((OrthoContentItem)item).centerItem();
				((OrthoContentItem)item).rotateRandom();
			}
		}
		*/
		if(enableSimulation && syncTester != null)	syncTester.setItems(items);
		if(comms != null){
			if(networkManager != null)	networkManager.registerContentItems(items);
		}
	}

	public void removeAllItems(){
		if(comms != null && networkManager!=null) networkManager.unregisterContentItems(new ArrayList<ContentItem>(networkManager.getOnlineItems().values()));
		items.clear();
	}
	
	@Override
	public void onActivate() {
		connect();
	}
	
		
	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(contentSystem != null) contentSystem.update(tpf);
		if(networkManager != null) networkManager.update(tpf);
		TablePortalManager.getInstance().update();
		if(syncTester != null && this.enableSimulation) syncTester.update(10);
	}
	
	public void connect(){
		new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					if(comms == null) comms = (TableCommsClientService) ServiceManager.getInstance().get(TableCommsClientService.class);
					if(comms != null){
							NetworkServiceDiscoveryService nsds = (NetworkServiceDiscoveryService) ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);
							while(!comms.isClientConnected()){
							Thread.sleep(1000);
							nsds.start();
							comms.start();
						}
						nsds.stop();
						ArrayList<Class<?>> receiverClasses = new ArrayList<Class<?>>();
						receiverClasses.add(TablePortalApp.class);
						
						networkManager = new NetworkedContentManager(contentSystem, comms, receiverClasses);
						messageHandler = new DefaultMessageHandler(networkManager);
						WorkspaceManager.getInstance().setNetworkedContentManager(networkManager);
						
						comms.register(TablePortalApp.this, messageHandler);
						comms.register("connection_listener", TablePortalApp.this);
						
						if(networkManager!= null && items != null) networkManager.registerContentItems(items);
						
						if(enableSimulation && syncTester != null) syncTester.setNetworkedContentManager(networkManager);
					}
				} catch (CouldNotStartServiceException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void tableDisconnected() {
		try {
			if(comms != null) comms.stop();
		} catch (ServiceNotRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		comms = null;
		connect();
	}

	@Override
	public void messageReceived(Object obj) {
		// TODO Auto-generated method stub
		
	}

}
