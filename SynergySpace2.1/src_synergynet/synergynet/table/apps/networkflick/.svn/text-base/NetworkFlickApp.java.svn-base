package synergynet.table.apps.networkflick;


import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import com.jme.math.FastMath;
//import com.sun.java.util.collections.Random;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ImageTextLabel;
import synergynet.contentsystem.items.SketchPad;
//import synergynet.contentsystem.items.VideoPlayer;
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
import synergynet.table.apps.conceptmap.ConceptMapApp;
import synergynet.table.config.ConfigurationSystem;
import synergynet.table.config.position.PositionConfigPrefsItem;
import synergyspace.jme.sysutils.BorderUtility;
import synergyspace.jmeapps.networkflick.LightBox;

public class NetworkFlickApp extends DefaultSynergyNetApp{

	private TableCommsClientService comms;
	protected ContentSystem contentSystem;
	protected DefaultMessageHandler messageHandler;
	protected NetworkedContentManager networkedContentManager;
	protected TransferController transferController;
	private Font arialFont = new Font("Arial", Font.PLAIN, 12);

	public NetworkFlickApp(ApplicationInfo info) {
		super(info);
	}

	@Override
	public void addContent() {
		setMenuController(new HoldBottomLeftExit());

		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);


		addImageWithCaption(LightBox.class.getResource("images/app1.jpg"), "Mysteries");
		addImageWithCaption(LightBox.class.getResource("images/app2.jpg"), "Teacher console");
		addImageWithCaption(LightBox.class.getResource("images/app3.jpg"), "Analysis");
		addImageWithCaption(LightBox.class.getResource("images/d2.jpg"), "The Castle");
		addImageWithCaption(LightBox.class.getResource("images/d5.jpg"), "Techno Cafe");

		addImageWithCaption(ConceptMapApp.class.getResource("images/synergynet-classroom-final.png"), "Classroom vision");
		
//		VideoPlayer videoTemp = (VideoPlayer)contentSystem.createContentItem(VideoPlayer.class);
//		videoTemp.setVideoURL(NetworkFlickApp.class.getResource("smallvid.mp4"));
//		videoTemp.makeFlickable(0.5f);
//		videoTemp.setBoundaryEnabled(false);
//		videoTemp.centerItem();
		
//		Random ran = new Random();
//		
//		
//		ArrayList<String> pics = new ArrayList<String>();
//		for (int i = 1; i < 16; i++){
//			pics.add(i+".jpg");
//		}
//		
//		
//		for (int i = 0; i < 10; i++){
//			int result = ran.nextInt(pics.size());
//			addImageWithCaption(NetworkFlickApp.class.getResource(pics.get(result)), "");
//			pics.remove(result);
//		}
//		
		
		SketchPad pad = (SketchPad) this.contentSystem.createContentItem(SketchPad.class);
		pad.setBorderSize(0);
		pad.setWidth(300);
		pad.setHeight(200);
		pad.setSketchArea(new Rectangle(0,40,300,200));
		pad.centerItem();
		pad.fillRectangle(new Rectangle(0,0,300,40), Color.red);
		pad.fillRectangle(new Rectangle(270,5,25,25), Color.black);
		pad.setClearArea(new Rectangle(270,5,25,25));
		pad.setTextColor(Color.black);
		pad.drawString("Sketch Pad", 110, 15);
		pad.makeFlickable(0.5f);

	}

	private void addImageWithCaption(URL resource, String str) {
		ImageTextLabel mlt3 = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
		mlt3.setImageInfo(resource);
		mlt3.setCRLFSeparatedString(str);
		mlt3.setBorderSize(3);
		mlt3.setBorderColour(Color.white);
		mlt3.setBackgroundColour(Color.black);
		mlt3.setFont(arialFont);
		mlt3.setTextColour(Color.lightGray);
		mlt3.makeFlickable(0.5f);
		mlt3.setBoundaryEnabled(false);
		mlt3.centerItem();
	}

	@Override
	public void onActivate(){
		if (networkedContentManager!=null) return;
		try{
			if(comms == null) {
				comms = (TableCommsClientService) ServiceManager.getInstance().get(TableCommsClientService.class);
				List<Class<?>> receiverClasses = new ArrayList<Class<?>>();
				receiverClasses.add(NetworkFlickApp.class);
				networkedContentManager = new NetworkedContentManager(contentSystem, comms, receiverClasses);
				transferController = new TransferController(this, comms, networkedContentManager);
				networkedContentManager.getNetworkedFlickController().setNetworkFlickEnabled(true);
				messageHandler = new DefaultMessageHandler(contentSystem, this.networkedContentManager);
				((DefaultMessageHandler)messageHandler).setTransferController(transferController);
			}
			if(comms != null) comms.register(this, messageHandler);
			if(comms != null) comms.sendMessage(new ApplicationCommsRequest(NetworkFlickApp.class.getName()));

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
