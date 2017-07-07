package synergynet.services.net.networkmanager.syncmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import synergynet.contentsystem.items.ContentItem;
import synergynet.services.net.localpresence.TableIdentity;
import synergynet.services.net.networkmanager.SynergyNet;
import synergynet.services.net.networkmanager.messages.UnicastSyncDataMessage;

public abstract class SyncManager {
	
	private static final Logger log = Logger.getLogger(SyncManager.class.getName());
	protected HashMap<TableIdentity, List<Class<?>>> syncTableApps = new HashMap<TableIdentity, List<Class<?>>>();
	protected HashMap<String, HashMap<Short, Object>> syncData = new HashMap<String, HashMap<Short, Object>>();
	protected HashMap<Short, Float> dataMasks = new HashMap<Short, Float>();

	public SyncManager(){
	}
	
	public void registerSyncTableApp(TableIdentity tableId, List<Class<?>> targetClasses){
		if(!syncTableApps.containsKey(tableId)) syncTableApps.put(tableId, targetClasses);
		log.info("SyncManager registered table-"+tableId.toString());
	}
	
	public void registerSyncTableApp(TableIdentity tableId, Class<?> targetClass){
		if(syncTableApps.containsKey(tableId)) syncTableApps.get(tableId).add(targetClass);
		else{
			List<Class<?>> targetClasses = new ArrayList<Class<?>>();
			targetClasses.add(targetClass);
			syncTableApps.put(tableId, targetClasses);
		}
		log.info("SyncManager registered table-"+tableId.toString());
	}
	
	public void unregisterSyncTableApp(TableIdentity tableId){
		syncTableApps.remove(tableId);
		log.info("SyncManager unregistered table-"+tableId.toString());
	}
	
	protected void addSyncData(ContentItem item, short key, Object data){
		if (syncData.get(item.getName())==null)	syncData.put(item.getName(), new HashMap<Short, Object>());
		syncData.get(item.getName()).put(key, data);
	} 
	
	protected HashMap<Short, Object> getSyncData(ContentItem item){
		return syncData.get(item.getName());
	}
	
	protected Object getSyncData(ContentItem item, short key){
		if(syncData.get(item.getName()) != null) return syncData.get(item.getName()).get(key);
		return null;
	}
	
	protected void removeSyncDataForItem(ContentItem item){
		syncData.remove(item.getName());
	}
	
	public abstract void addSyncListeners(ContentItem item);
	public abstract void removeSyncListeners(ContentItem item);
	public 	abstract void renderSyncData( ContentItem item, Map<Short, Object> itemAttrs);

	
	public void update() {
		if(!syncTableApps.isEmpty() && !syncData.isEmpty()){
		for(TableIdentity tableId: syncTableApps.keySet()){
				UnicastSyncDataMessage msg = new UnicastSyncDataMessage();
				for(Class<?> targetClass: syncTableApps.get(tableId)){
					try {
						msg.setRecipient(tableId);
						msg.setTargetClassName(targetClass.getName());
						msg.setSyncData(syncData);
						SynergyNet.getTableCommsClientService().sendMessage(msg);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		syncData.clear();
	}

}
