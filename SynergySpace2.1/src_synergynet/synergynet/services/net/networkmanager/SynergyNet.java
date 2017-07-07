package synergynet.services.net.networkmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.OrthoContentItem;
import synergynet.services.ServiceManager;
import synergynet.services.exceptions.CouldNotStartServiceException;
import synergynet.services.exceptions.ServiceNotRunningException;
import synergynet.services.net.localpresence.TableIdentity;
import synergynet.services.net.netservicediscovery.NetworkServiceDiscoveryService;
import synergynet.services.net.networkmanager.constructionmanagers.ConstructionManager;
import synergynet.services.net.networkmanager.handlers.DefaultMessageHandler;
import synergynet.services.net.networkmanager.handlers.MessageProcessor;
import synergynet.services.net.networkmanager.handlers.NetworkFlickMessageProcessor;
import synergynet.services.net.networkmanager.handlers.NetworkedContentMessageProcessor;
import synergynet.services.net.networkmanager.handlers.NetworkedContentMessageProcessor.NetworkedContentListener;
import synergynet.services.net.networkmanager.messages.BroadcastItemConstructionMessage;
import synergynet.services.net.networkmanager.messages.BroadcastItemsMessage;
import synergynet.services.net.networkmanager.messages.PostItemConstructionMessage;
import synergynet.services.net.networkmanager.messages.PostItemsMessage;
import synergynet.services.net.networkmanager.messages.RequestSyncItemsMessage;
import synergynet.services.net.networkmanager.messages.networkflick.AnnounceTableMessage;
import synergynet.services.net.networkmanager.messages.networkflick.EnableFlickMessage;
import synergynet.services.net.networkmanager.messages.networkflick.UnregisterTableMessage;
import synergynet.services.net.networkmanager.syncmanager.OrthoRotateTranslateScaleSyncManager;
import synergynet.services.net.networkmanager.syncmanager.SyncManager;
import synergynet.services.net.networkmanager.utils.networkflick.TableInfo;
import synergynet.services.net.networkmanager.utils.networkflick.TransferController;
import synergynet.services.net.tablecomms.client.TableCommsApplicationListener;
import synergynet.services.net.tablecomms.client.TableCommsClientService;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergyspace.jme.sysutils.BorderUtility;

public class SynergyNet{

	private static final Logger log = Logger.getLogger(SynergyNet.class.getName());
	
	protected static TableCommsClientService comms;
	protected static HashMap<Class<? extends ContentItem>, SyncManager> syncManagers = new HashMap<Class<? extends ContentItem>, SyncManager>();
	protected static HashMap<Class<? extends ContentItem>, ConstructionManager> constructionManagers = new HashMap<Class<? extends ContentItem>, ConstructionManager>();
	protected static List<NetworkedContentListener> contentListeners = new ArrayList<NetworkedContentListener>();
	protected static List<Class<?>> receiverClasses = new ArrayList<Class<?>>();
	protected static boolean autoReconnect = false;
	public static boolean isConnected = false;
	protected static boolean isFlickEnabled = false;
	protected static int reconnectTime = 1000;
	protected static DefaultMessageHandler messageHandler =  new DefaultMessageHandler();
	protected static DefaultSynergyNetApp app;
	protected static TransferController transferController;
	protected static TableInfo tableInfo = new TableInfo(TableIdentity.getTableIdentity(),0, 0, 0, BorderUtility.getBorderFileText());
	protected static NetworkFlickMessageProcessor nfmp;
	protected static NetworkedContentMessageProcessor processor;
	protected static NetworkServiceDiscoveryService nsds = null;
	
	public enum SyncType{UNIDIRECTIONAL_SYNC, BIDIRECTIONAL_SYNC};

	public static void broadcastItem(ContentItem item) throws IOException {
		List<ContentItem> items = new ArrayList<ContentItem>();
		items.add(item);
		broadcastItems(items);
	}

	public static void broadcastItems(List<ContentItem> items) throws IOException {
		if(comms == null) throw new IOException("Table is not connected");
		BroadcastItemsMessage msgBindableItems = null;
		BroadcastItemConstructionMessage msgNonBindableItems = null;
		List<ContentItem> bindableItems = new ArrayList<ContentItem>();
		List<ContentItem> nonBindableItems = new ArrayList<ContentItem>();
		for(ContentItem item: items){
			if(constructionManagers.containsKey(item.getClass())) nonBindableItems.add(item);
			else bindableItems.add(item);
		}
		if(!bindableItems.isEmpty()){
			msgBindableItems = new BroadcastItemsMessage();
			msgBindableItems.setItems(bindableItems);
		}
		if(!nonBindableItems.isEmpty()){
			HashMap<ContentItem, HashMap<String, Object>> constructInfo = new HashMap<ContentItem, HashMap<String, Object>>();
			for(ContentItem item: nonBindableItems){
				ConstructionManager contruct = constructionManagers.get(item.getClass());
				constructInfo.put(item, contruct.buildConstructionInfo(item));
			}
			msgNonBindableItems = new BroadcastItemConstructionMessage();
			msgNonBindableItems.setConstructionInfo(constructInfo);
		}
		
		for(Class<?> targetClass: receiverClasses){
			if(msgBindableItems != null){
				msgBindableItems.setTargetClassName(targetClass.getName());
				comms.sendMessage(msgBindableItems);
				log.info("Broadcast bindable items");
			}else if(msgNonBindableItems != null){
				msgNonBindableItems.setTargetClassName(targetClass.getName());
				comms.sendMessage(msgNonBindableItems);
				log.info("Broadcast non-bindable items");
			}
		}
	}
	
	
	public static void postItem(ContentItem item, TableIdentity tableIdentity) throws IOException {
		List<ContentItem> items = new ArrayList<ContentItem>();
		items.add(item);
		postItems(items, tableIdentity);
		
	}
	
	public static void postItems(List<ContentItem> items, TableIdentity tableIdentity) throws IOException {
		if(comms == null) throw new IOException("Table is not connected");
		PostItemsMessage msgBindableItems = null;
		PostItemConstructionMessage msgNonBindableItems = null;
		List<ContentItem> bindableItems = new ArrayList<ContentItem>();
		List<ContentItem> nonBindableItems = new ArrayList<ContentItem>();
		for(ContentItem item: items){
			if(constructionManagers.containsKey(item.getClass())) nonBindableItems.add(item);
			else bindableItems.add(item);
		}
		if(!bindableItems.isEmpty()){
			msgBindableItems = new PostItemsMessage();
			msgBindableItems.setRecipient(tableIdentity);
			msgBindableItems.setItems(bindableItems);
		}
		if(!nonBindableItems.isEmpty()){
			HashMap<ContentItem, HashMap<String, Object>> constructInfo = new HashMap<ContentItem, HashMap<String, Object>>();
			for(ContentItem item: nonBindableItems){
				ConstructionManager contruct = constructionManagers.get(item.getClass());
				constructInfo.put(item, contruct.buildConstructionInfo(item));
			}
			msgNonBindableItems = new PostItemConstructionMessage();
			msgNonBindableItems.setRecipient(tableIdentity);
			msgNonBindableItems.setConstructionInfo(constructInfo);
		}
		
		for(Class<?> targetClass: receiverClasses){
			if(msgBindableItems != null){
				msgBindableItems.setTargetClassName(targetClass.getName());
				comms.sendMessage(msgBindableItems);
				log.info("Post bindable items to table-"+tableIdentity.toString());
			}else if(msgNonBindableItems != null){
				msgNonBindableItems.setTargetClassName(targetClass.getName());
				comms.sendMessage(msgNonBindableItems);
				log.info("Post non-bindable items to table-"+tableIdentity.toString());
			}
		}
	}

	public static void registerConstructionManager(Class<? extends ContentItem> itemClass, ConstructionManager constructManager) {
		constructionManagers.put(itemClass, constructManager);
		log.info("Register construction manager");
	}


	public static void registerSyncManager(Class<? extends ContentItem> itemClass,
			SyncManager syncManager) {
		syncManagers.put(itemClass, syncManager);
		log.info("Register syncManager");
	}


	public static void shareItem(ContentItem item, TableIdentity tableIdentity, SyncType syncType) throws IOException {
		if(comms == null) throw new IOException("Table is not connected");
		List<ContentItem> items = new ArrayList<ContentItem>();
		items.add(item);
		shareItems(items, tableIdentity, syncType);
	}


	
	public static void shareItems(List<ContentItem> items,
			TableIdentity tableIdentity, SyncType syncType) throws IOException {
		if(comms == null) throw new IOException("Table is not connected");
		List<String> itemNames = new ArrayList<String>();
		for(ContentItem item: items) itemNames.add(item.getName());
		for(Class<?> targetClass: receiverClasses){
			RequestSyncItemsMessage msg = new RequestSyncItemsMessage(targetClass, tableIdentity, itemNames, true, syncType);
			comms.sendMessage(msg);
		}
		for(ContentItem item: items){
			SyncManager syncManager = SynergyNet.getSyncManagerForItemClass(item.getClass());
			if(syncManager != null){
				syncManager.registerSyncTableApp(tableIdentity, receiverClasses);
				syncManager.addSyncListeners(item);
			}
		}
		
		log.info("Share items with table-"+tableIdentity.toString());
	}

	public static void unshareItem(ContentItem item, TableIdentity tableIdentity, SyncType syncType) throws IOException {
		List<ContentItem> items = new ArrayList<ContentItem>();
		items.add(item);
		unshareItems(items, tableIdentity, syncType);
	}


	public static void unshareItems(List<ContentItem> items,
			TableIdentity tableIdentity, SyncType syncType) throws IOException {
		if(comms == null) throw new IOException("Table is not connected");
		List<String> itemNames = new ArrayList<String>();
		for(ContentItem item: items) itemNames.add(item.getName());
		for(Class<?> targetClass: receiverClasses){
			RequestSyncItemsMessage msg = new RequestSyncItemsMessage(targetClass, tableIdentity, itemNames, false, syncType);
			comms.sendMessage(msg);
		}		
		for(ContentItem item: items){
			SyncManager syncManager = SynergyNet.getSyncManagerForItemClass(item.getClass());
			if(syncManager != null){
				syncManager.removeSyncListeners(item);
			}
		}
		
		log.info("Unshare items from table-"+tableIdentity.toString());
		
	}

	public static void connect(final DefaultSynergyNetApp app){
		SynergyNet.app = app;
		//if(!receiverClasses.contains(app.getClass())) receiverClasses.add(app.getClass());
		new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					if(comms == null) comms = (TableCommsClientService) ServiceManager.getInstance().get(TableCommsClientService.class);
					if(comms != null && SynergyNet.autoReconnect){
							while(!comms.isClientConnected()){
							Thread.sleep(reconnectTime);
							if(nsds == null){
								nsds = (NetworkServiceDiscoveryService) ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);
							}else{
								nsds.shutdown();
							}
							nsds.start();
							comms.start();
						}
						if(nsds != null) nsds.shutdown();
						syncManagers.put(OrthoContentItem.class, new OrthoRotateTranslateScaleSyncManager());
						if(processor != null) processor.removeNetworkedContentListeners();
						processor = new NetworkedContentMessageProcessor(ContentSystem.getContentSystemForSynergyNetApp(app));
						messageHandler.registerMessageProcessor(processor);
						processor.addNetworkedContentListener(new NetworkedContentListener(){

							@Override
							public void itemsReceived(List<ContentItem> item,	TableIdentity tableId) {
								for(NetworkedContentListener listener: contentListeners){
									listener.itemsReceived(item, tableId);
								}
							}

							@Override
							public void tableDisconnected() {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void tableConnected() {
								// TODO Auto-generated method stub
								
							}
							
						});
						comms.register(app, messageHandler);
						comms.register("connection_listener", new TableCommsApplicationListener(){
							@Override
							public void messageReceived(Object obj) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void tableDisconnected() {
								if(comms != null)
									try {
										comms.stop();
									} catch (ServiceNotRunningException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								comms = null;
								isConnected = false;
								for(NetworkedContentListener listener: contentListeners){
									listener.tableDisconnected();
								}
								if(SynergyNet.autoReconnect) connect(app);
							}
							
						});
						isConnected = true;
						for(NetworkedContentListener listener: contentListeners){
							listener.tableConnected();
						}
					}
				} catch (CouldNotStartServiceException e1) {
					log.warning(e1.toString());
				} catch (IOException e) {
					log.warning(e.toString());
				} catch (InterruptedException e) {
					log.warning(e.toString());
				}
			}
		}).start();
		
		log.info("Connected to server");
	}

	public static TableCommsClientService getTableCommsClientService(){
		return comms;
	}
	
	public static void update(){
		for(SyncManager syncManager: syncManagers.values()) syncManager.update();
		if(transferController != null) transferController.update();
	}
	
	public static HashMap<Class<? extends ContentItem>,SyncManager> getSyncManagers(){
		return syncManagers;
	}
	
	
	@SuppressWarnings("unchecked")
	public static SyncManager getSyncManagerForItemClass(Class<? extends ContentItem> itemClass){
		if(syncManagers.containsKey(itemClass)) return syncManagers.get(itemClass);
		else if(itemClass.getSuperclass() != null) return getSyncManagerForItemClass((Class<? extends ContentItem>)itemClass.getSuperclass());
		else return null;
	}
	
	public static HashMap<Class<? extends ContentItem>, ConstructionManager> getConstructionManagers(){
		return constructionManagers;
	}
	
	public static void setAutoReconnect(boolean autoReconnect){
		SynergyNet.autoReconnect = autoReconnect;
	}
	
	public static void setAutoConnectTime(int reconnectTime){
		SynergyNet.reconnectTime = reconnectTime;
	}
	
	public static void registerMessageProcessor(MessageProcessor processor){
		if(!messageHandler.getMessageProcessors().contains(processor)) messageHandler.registerMessageProcessor(processor);
	}

	public static void removeMessageProcessor(MessageProcessor processor){
		messageHandler.removeMessageProcessor(processor);
	}
	
	public static List<Class<?>> getReceiverClasses() {
		return receiverClasses;
	}
	
	
	public static void setTablePosition(int x, int y){
		tableInfo.setTablePosition(x, y);
		try {
			SynergyNet.enableNetworkFlick(SynergyNet.isFlickEnabled);
		} catch (IOException e) {
			log.info(e.toString());
		}
	}
	
	public static void setTableOrientation(float angle){
		tableInfo.setAngle(angle);
		try {
			SynergyNet.enableNetworkFlick(SynergyNet.isFlickEnabled);
		} catch (IOException e) {
			log.info(e.toString());
		}
	}
	
	public static void setBorder(String objFile){
		tableInfo.setObjFile(objFile);
		try {
			SynergyNet.enableNetworkFlick(SynergyNet.isFlickEnabled);
		} catch (IOException e) {
			log.info(e.toString());
		}
	}
	
	public static void enableNetworkFlick(boolean isFlickEnabled) throws IOException{
		if(comms == null) throw new IOException("Table is not connected");
		if(isFlickEnabled){
			SynergyNet.isFlickEnabled = isFlickEnabled;
			if(transferController == null) transferController = new TransferController(app);
			transferController.enableNetworkFlick(isFlickEnabled);
			if(nfmp == null) nfmp = new NetworkFlickMessageProcessor(transferController);
			SynergyNet.registerMessageProcessor(nfmp);
			transferController.setLocalTableInfo(tableInfo);
			for(Class<?> receiverClass: SynergyNet.getReceiverClasses()){
				comms.sendMessage( new AnnounceTableMessage(receiverClass, tableInfo));
				comms.sendMessage(new EnableFlickMessage(receiverClass,true));
			}
		}else{
			for(Class<?> receiverClass: SynergyNet.getReceiverClasses()){
				if(transferController != null) transferController.enableNetworkFlick(isFlickEnabled);
				comms.sendMessage( new UnregisterTableMessage(receiverClass));
				comms.sendMessage(new EnableFlickMessage(receiverClass,false));
			}
		}
		
		log.info("Enable network flick");
	}
	
	public static void addNetworkedContentListener(NetworkedContentListener listener){
		contentListeners.add(listener);
	}
	
	public static void removeNetworkedContentListeners(){
		contentListeners.clear();
	}
}
