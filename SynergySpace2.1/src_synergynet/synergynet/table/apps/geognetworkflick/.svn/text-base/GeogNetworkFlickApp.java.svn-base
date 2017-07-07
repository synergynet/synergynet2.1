package synergynet.table.apps.geognetworkflick;


import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;

import netflick.geogtask.GeogTaskResource;

import com.jme.math.FastMath;
import com.jme.util.GameTaskQueueManager;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ImageTextLabel;
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
import synergynet.table.config.ConfigurationSystem;
import synergynet.table.config.position.PositionConfigPrefsItem;
import synergyspace.jme.sysutils.BorderUtility;

public class GeogNetworkFlickApp extends DefaultSynergyNetApp{

	private TableCommsClientService comms;
	protected ContentSystem contentSystem;
	protected DefaultMessageHandler messageHandler;
	protected NetworkedContentManager networkedContentManager;
	protected TransferController transferController;

	public GeogNetworkFlickApp(ApplicationInfo info) {
		super(info);
	}

	@Override
	public void addContent() {
		setMenuController(new HoldBottomLeftExit());

		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
	
		TableIdentity yellowTableID = new TableIdentity("yellow");
		TableIdentity greenTableID = new TableIdentity("green");
		TableIdentity blueTableID = new TableIdentity("blue");
		TableIdentity redTableID = new TableIdentity("red");
		
		if(TableIdentity.getTableIdentity().equals(yellowTableID)) {
			backgroundLoad(yellowTable);
		}else if(TableIdentity.getTableIdentity().equals(greenTableID)) {
			backgroundLoad(greenTable);
		}else if(TableIdentity.getTableIdentity().equals(blueTableID)) {
			backgroundLoad(blueTable);
		}else if(TableIdentity.getTableIdentity().equals(redTableID)) {
			backgroundLoad(redTable);
		}else{
			backgroundLoad(textImages);
		}

	}
	
	protected void backgroundLoad(final String[] imageSet) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				for(final String img : imageSet) {


					GameTaskQueueManager.getManager().update(new Callable<Void>() {

						@Override
						public Void call() throws Exception {
							addImage(GeogTaskResource.class.getResource(img));						
							return null;
						}					
					});
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}).start();

	}

	private void addImage(URL resource) {
		
		ImageTextLabel mlt3 = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
		mlt3.setImageInfo(resource);
        ImageIcon image = new ImageIcon(resource);
        mlt3.setAutoFit(false);
        mlt3.setWidth(image.getIconWidth());
        mlt3.setHeight(image.getIconHeight());
        mlt3.setScale(0.4f);
		mlt3.setBorderSize(3);
		mlt3.setBorderColour(Color.white);
		mlt3.setBackgroundColour(Color.black);
		mlt3.setTextColour(Color.lightGray);
		mlt3.makeFlickable(0.5f);
		mlt3.setBoundaryEnabled(false);
		mlt3.centerItem();
	}


	String[] textImages = {
			"text_beijing.jpg",
			"text_brasilia.jpg",
			"text_pretoria.jpg",
			"text_warsaw.jpg"
	};
	
	String[] redTable = {
			"animal_ostrich.jpg",
			"animal_european_bison.jpg",
			"cmap_africa.gif",
			"landmark_christ-rio.jpg",
			"landmark_st-mary-krakow.jpg"
	};
			
	String[] blueTable = {
			"animal_spidermonkey.jpg",
			"cmap_asia.gif",
			"cmap_europe.gif",
			"landmark_wall_of_china.jpg",
			"map_beijing.jpg"
			
	};

	String[] greenTable = {
			"map_brasilia.jpg",
			"map_china.jpg",
			"map_poland.jpg",
			"map_south-america.gif",
			"animal_red-panda.jpg",
	};
	
	String[] yellowTable = {
			"map_brazil.jpg",
			"map_south-africa.jpg",
			"map_warsaw.gif",
			"landmark_tablemountain.jpg",
			"map_pretoria.gif"
			
	};
	
	@Override
	public void onActivate(){
		if (networkedContentManager!=null) return;
		try{
			if(comms == null) {
				comms = (TableCommsClientService) ServiceManager.getInstance().get(TableCommsClientService.class);
				List<Class<?>> receiverClasses = new ArrayList<Class<?>>();
				receiverClasses.add(GeogNetworkFlickApp.class);
				networkedContentManager = new NetworkedContentManager(contentSystem, comms, receiverClasses);
				transferController = new TransferController(this, comms, networkedContentManager);
				networkedContentManager.getNetworkedFlickController().setNetworkFlickEnabled(true);
				messageHandler = new DefaultMessageHandler(contentSystem, this.networkedContentManager);
				((DefaultMessageHandler)messageHandler).setTransferController(transferController);
			}
			if(comms != null) comms.register(this, messageHandler);
			if(comms != null) comms.sendMessage(new ApplicationCommsRequest(GeogNetworkFlickApp.class.getName()));

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
