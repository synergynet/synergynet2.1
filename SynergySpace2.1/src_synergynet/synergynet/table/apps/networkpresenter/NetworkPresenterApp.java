package synergynet.table.apps.networkpresenter;


import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import com.jme.math.FastMath;
import com.jme.system.DisplaySystem;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.ImageTextLabel;
import synergynet.contentsystem.items.PPTViewer;
import synergynet.contentsystem.items.VideoPlayer;
import synergynet.contentsystem.items.listener.ItemEventAdapter;
import synergynet.services.ServiceManager;
import synergynet.services.exceptions.CouldNotStartServiceException;
import synergynet.services.net.localpresence.TableIdentity;
import synergynet.services.net.networkedcontentmanager.NetworkedContentManager;
import synergynet.services.net.networkedcontentmanager.messagehandler.DefaultMessageHandler;
import synergynet.services.net.networkedcontentmanager.messages.networkedflick.AnnounceTableMessage;
import synergynet.services.net.networkedcontentmanager.messages.networkedflick.EnableFlickMessage;
import synergynet.services.net.networkedcontentmanager.utils.networkedflick.TableInfo;
import synergynet.services.net.networkedcontentmanager.utils.networkedflick.TransferController;
import synergynet.services.net.tablecomms.client.TableCommsClientService;
import synergynet.services.net.tablecomms.messages.control.fromclient.ApplicationCommsRequest;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.menucontrol.HoldBottomLeftExit;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergynet.table.apps.docviewer.DocViewerApp;
import synergynet.table.config.ConfigurationSystem;
import synergynet.table.config.position.PositionConfigPrefsItem;
import synergyspace.jme.sysutils.BorderUtility;

public class NetworkPresenterApp extends DefaultSynergyNetApp{

	private TableCommsClientService comms;
	protected ContentSystem contentSystem;
	protected DefaultMessageHandler messageHandler;
	protected NetworkedContentManager networkedContentManager;
	protected TransferController transferController;

	public NetworkPresenterApp(ApplicationInfo info) {
		super(info);
	}

	@Override
	public void addContent() {
		setMenuController(new HoldBottomLeftExit());
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);

		//		addImageWithCaption(LightBox.class.getResource("images/app1.jpg"), "Mysteries");
		//		addImageWithCaption(LightBox.class.getResource("images/app2.jpg"), "Teacher console");
		//		addImageWithCaption(LightBox.class.getResource("images/app3.jpg"), "Analysis");
		//		addImageWithCaption(LightBox.class.getResource("images/d2.jpg"), "The Castle");
		//		addImageWithCaption(LightBox.class.getResource("images/d5.jpg"), "Techno Cafe");
		//		addImageWithCaption(ConceptMapApp.class.getResource("images/synergynet-classroom-final.png"), "Classroom vision");

		addPPTButton();
		int xstart = 120;
		int gap = 80;
		addVideoButton("apps",  "buttons/apps.png", "software_apps.mp4",   xstart,          DisplaySystem.getDisplaySystem().getRenderer().getHeight() - 30);
		addVideoButton("lab",   "buttons/lab.png",  "usability_lab.mp4",   xstart + gap,    DisplaySystem.getDisplaySystem().getRenderer().getHeight() - 30);
		addVideoButton("net",   "buttons/net.png",  "net_content.mp4",     xstart + 2*gap,  DisplaySystem.getDisplaySystem().getRenderer().getHeight() - 30);		
		addVideoButton("table", "buttons/table.png", "student_table.mp4",   xstart + 3*gap,  DisplaySystem.getDisplaySystem().getRenderer().getHeight() - 30);
		//addVideosButton("exp", "buttons/exp.png",   xstart + 4*gap,  DisplaySystem.getDisplaySystem().getRenderer().getHeight() - 30, "firstmt.mp4", "macarth.mp4");
		addImagesButton("img", "buttons/pics.png", xstart + 5*gap, DisplaySystem.getDisplaySystem().getRenderer().getHeight() - 30,
				"d12.jpg", "d16.jpg", "d18.jpg", "d26.jpg",
				"ed12.jpg", "ed18.jpg", "ed19.jpg", "ed2.jpg", "ed24.jpg", "r17.jpg", "r22.jpg", "r23.jpg");
	
	}

	private void addImagesButton(String name, String icon, int i, int j,
			final String... images) {
		
		final ImageTextLabel button = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
//		final Map<String,VideoPlayer> players = this.players;
		button.setAutoFit(false);
		button.setImageInfo(NetworkPresenterApp.class.getResource(icon));
		button.setBorderSize(1);
		button.setBorderColour(Color.white);
		button.setBackgroundColour(Color.black);
		button.setRotateTranslateScalable(false);
		button.setLocation(i, j);
		button.setWidth(64);
		button.setHeight(64);
		button.addItemListener(new ItemEventAdapter() {
			private boolean loaded = false;
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
				if(loaded) return;
				for(String imgres : images) {
					final ImageTextLabel img = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
//					final Map<String,VideoPlayer> players = this.players;
					img.setAutoFit(true);
					img.setImageInfo(NetworkPresenterApp.class.getResource(imgres));
					img.setBorderSize(1);
					img.setBorderColour(Color.white);
					img.setBackgroundColour(Color.black);
					
					img.setLocation(300, 300);
					img.makeFlickable(0.5f);
				}
			}
		});
		
		

	}

//	private void addVideosButton(final String name, final String icon, final int i, final int j, final String... vids) {
//		final ImageTextLabel button = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
////		final Map<String,VideoPlayer> players = this.players;
//		button.setAutoFit(false);
//		button.setImageInfo(NetworkPresenterApp.class.getResource(icon));
//		button.setBorderSize(1);
//		button.setBorderColour(Color.white);
//		button.setBackgroundColour(Color.black);
//		button.setRotateTranslateScalable(false);
//		button.setLocation(i, j);
//		button.setWidth(64);
//		button.setHeight(64);
//		button.addItemListener(new ItemEventAdapter() {
//			private boolean buttonOn = false;
//			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
//				buttonOn = !buttonOn;
//				if(buttonOn) button.setBorderColour(Color.blue);
//				else button.setBorderColour(Color.white);
//				for(String s : vids) {
//					setVideoVisibility(name+s, icon, s, i, j, buttonOn);
//				}
//			}
//		});
//		
//	}

	Map<String,VideoPlayer> players = new HashMap<String,VideoPlayer>();
	private void addVideoButton(final String name, final String icon, final String video, final int i, final int j) {
		final ImageTextLabel button = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
//		final Map<String,VideoPlayer> players = this.players;
		button.setAutoFit(false);
		button.setImageInfo(NetworkPresenterApp.class.getResource(icon));
		button.setBorderSize(1);
		button.setBorderColour(Color.white);
		button.setBackgroundColour(Color.black);
		button.setRotateTranslateScalable(false);
		button.setLocation(i, j);
		button.setWidth(64);
		button.setHeight(64);
		button.addItemListener(new ItemEventAdapter() {
			private boolean buttonOn = false;
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
				buttonOn = !buttonOn;
				if(buttonOn) button.setBorderColour(Color.blue);
				else button.setBorderColour(Color.white);
				setVideoVisibility(name, icon, video, i, j, buttonOn);
			}
		});
	}

	protected void setVideoVisibility(String name, String icon, String video, int i, int j, boolean visible) {
		VideoPlayer player = players.get(name);
		if(player == null) {
			player = (VideoPlayer)contentSystem.createContentItem(VideoPlayer.class);
			player.setVideoURL(NetworkPresenterApp.class.getResource(video));
			player.makeFlickable(0.5f);
			player.setBoundaryEnabled(false);
			player.centerItem();
			players.put(name, player);
		}

		if(!player.isVisible() && visible) {
			player.centerItem();
		}
		
		player.setVisible(visible);
		if(!player.isVisible()) {
			player.stop();
		}else{
			player.setVideoTime(0);
			player.play();
		}
	}

	private void stopAllVideoPlayers() {
		for(VideoPlayer p : players.values()) {
			if(p.isPlaying()) {
				p.stop();
			}
		}		
	}
	
	private void startAllVisibleVideoPlayers() {
		for(VideoPlayer p : players.values()) {
			if(p.isVisible()) {
				p.play();
			}
		}
	}
	
	private void addPPTButton() {
		final ImageTextLabel button = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
		button.setAutoFit(false);
		button.setImageInfo(NetworkPresenterApp.class.getResource("buttons/viewslides.png"));
		button.setBorderSize(1);
		button.setBorderColour(Color.white);
		button.setBackgroundColour(Color.black);
		button.setRotateTranslateScalable(false);
		button.setLocation(42, DisplaySystem.getDisplaySystem().getRenderer().getHeight() - 30);
		button.setWidth(64);
		button.setHeight(64);
		button.addItemListener(new ItemEventAdapter() {
			private boolean buttonOn = false;
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
				buttonOn = !buttonOn;
				if(buttonOn) button.setBorderColour(Color.blue);
				else button.setBorderColour(Color.white);
				setPPTViewerVisibility(buttonOn);
			}
		});
	}


	private PPTViewer ppt;
	private void setPPTViewerVisibility(boolean visible) {
		if(ppt == null) {
			ppt = (PPTViewer)contentSystem.createContentItem(PPTViewer.class);
			ppt.setPPTFile(DocViewerApp.class.getResource("synergynet-july09.ppt"));
			ppt.makeFlickable(0.5f);
			ppt.setBoundaryEnabled(false);
			ppt.setScale(0.5f);
			ppt.centerItem();
		}
		if(!ppt.isVisible() && visible) {
			ppt.centerItem();
		}
		ppt.setVisible(visible);
	}

	@Override
	public void onDeactivate() {
		super.onDeactivate();
		stopAllVideoPlayers();
	}

	@Override
	public void onActivate(){
		super.onActivate();
		startAllVisibleVideoPlayers();
		if (networkedContentManager!=null) return;
		try{
			if(comms == null) {
				comms = (TableCommsClientService) ServiceManager.getInstance().get(TableCommsClientService.class);
				List<Class<?>> receiverClasses = new ArrayList<Class<?>>();
				receiverClasses.add(NetworkPresenterApp.class);
				networkedContentManager = new NetworkedContentManager(contentSystem, comms, receiverClasses);
				transferController = new TransferController(this, comms, networkedContentManager);
				networkedContentManager.getNetworkedFlickController().setNetworkFlickEnabled(true);
				messageHandler = new DefaultMessageHandler(contentSystem, this.networkedContentManager);
				((DefaultMessageHandler)messageHandler).setTransferController(transferController);
			}
			if(comms != null) comms.register(this, messageHandler);
			if(comms != null) comms.sendMessage(new ApplicationCommsRequest(NetworkPresenterApp.class.getName()));

			Preferences prefs = ConfigurationSystem.getPreferences(PositionConfigPrefsItem.class);
			int location_x = prefs.getInt(PositionConfigPrefsItem.PREFS_LOCATION_X, 0);
			int location_y = prefs.getInt(PositionConfigPrefsItem.PREFS_LOCATION_Y, 0);
			float angle = prefs.getFloat(PositionConfigPrefsItem.PREFS_ANGLE, 0);


			if (new PositionConfigPrefsItem().getDeveloperMode()){
				if (prefs.get(PositionConfigPrefsItem.HORIZONTAL_PLACEMENT, "false").equals("true")){
					if(prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_X, 0) != 0){
						int xPos = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) % prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_X, 0);
						location_x = xPos * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_X, 0));
						int yPos = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) / prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_X, 0);
						location_y = yPos * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_Y, 0));
					}else{
						location_x = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_X, 0));
						location_y = 0;
					}
				}else{
					if(prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_Y, 0) != 0){
						int yPos = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) % prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_Y, 0);
						location_y = yPos * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_Y, 0));
						int xPos = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) / prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_Y, 0);
						location_x = xPos * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_X, 0));
					}else{
						location_y = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_Y, 0));
						location_x = 0;
					}
				}
				angle = 0;
			}

			TableInfo tableInfo = new TableInfo(TableIdentity.getTableIdentity(),location_x, location_y, angle * FastMath.DEG_TO_RAD, BorderUtility.getBorderFileText());
			transferController.setLocalTableInfo(tableInfo);
			for(Class<?> receiverClass: networkedContentManager.getReceiverClasses()){
				if(comms == null) return;
				comms.sendMessage( new AnnounceTableMessage(receiverClass, tableInfo));
				comms.sendMessage(new EnableFlickMessage(receiverClass,true));
			}
		}catch(IOException e){
			e.printStackTrace();

		} catch (CouldNotStartServiceException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(networkedContentManager!= null) networkedContentManager.stateUpdate(tpf);
		if(transferController != null) transferController.update();
		if(contentSystem != null) contentSystem.update(tpf);
	}
}
