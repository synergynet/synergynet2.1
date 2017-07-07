package synergynet.services.net.networkmanager.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sun.media.Log;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.services.net.localpresence.TableIdentity;
import synergynet.services.net.networkmanager.SynergyNet;
import synergynet.services.net.networkmanager.constructionmanagers.ConstructionManager;
import synergynet.services.net.networkmanager.messages.BroadcastItemConstructionMessage;
import synergynet.services.net.networkmanager.messages.BroadcastItemsMessage;
import synergynet.services.net.networkmanager.messages.PostItemConstructionMessage;
import synergynet.services.net.networkmanager.messages.PostItemsMessage;
import synergynet.services.net.networkmanager.messages.RequestSyncItemsMessage;
import synergynet.services.net.networkmanager.messages.UnicastSyncDataMessage;
import synergynet.services.net.networkmanager.syncmanager.SyncManager;
import synergynet.services.net.tablecomms.messages.TableMessage;

public class NetworkedContentMessageProcessor implements MessageProcessor{
	
	protected ContentSystem contentSystem;
	protected List<NetworkedContentListener> listeners = new ArrayList<NetworkedContentListener>();

	public NetworkedContentMessageProcessor(ContentSystem contentSystem){
		this.contentSystem = contentSystem;
	}
	
	@Override
	public void process(Object obj){
		if(obj instanceof PostItemsMessage){
			for(ContentItem item: ((PostItemsMessage)obj).getItems()){
				registerContentItem(item);
			}
			for(NetworkedContentListener listener: listeners) listener.itemsReceived(((PostItemsMessage)obj).getItems(), ((PostItemsMessage)obj).getSender());
		}else if(obj instanceof PostItemConstructionMessage){
			HashMap<ContentItem, HashMap<String, Object>> constructionMap = ((PostItemConstructionMessage)obj).getConstructionInfo();
			restoreItemsFromConstructionInfo(constructionMap);
		}else if(obj instanceof BroadcastItemsMessage){
			for(ContentItem item: ((BroadcastItemsMessage)obj).getItems()){
				registerContentItem(item);
			}
			for(NetworkedContentListener listener: listeners) listener.itemsReceived(((BroadcastItemsMessage)obj).getItems(), ((BroadcastItemsMessage)obj).getSender());
		}else if(obj instanceof BroadcastItemConstructionMessage){
			HashMap<ContentItem, HashMap<String, Object>> constructionMap = ((BroadcastItemConstructionMessage)obj).getConstructionInfo();
			restoreItemsFromConstructionInfo(constructionMap);
		}else if(obj instanceof RequestSyncItemsMessage){
			Class<?> targetAppClass = null;
			try {
				targetAppClass = Class.forName(((RequestSyncItemsMessage)obj).getTargetClassName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if(((RequestSyncItemsMessage)obj).isSyncEnabled() && ((RequestSyncItemsMessage)obj).getSyncType() == SynergyNet.SyncType.BIDIRECTIONAL_SYNC)	
				this.syncItems(((RequestSyncItemsMessage)obj).getSyncItemNames(), ((TableMessage)obj).getSender(), targetAppClass);
			else if(!((RequestSyncItemsMessage)obj).isSyncEnabled())
				this.unsyncItems(((RequestSyncItemsMessage)obj).getSyncItemNames());
		}else if(obj instanceof UnicastSyncDataMessage){
			this.renderSyncData(((UnicastSyncDataMessage)obj).getSyncData());
		}
	}
	
	private void restoreItemsFromConstructionInfo(HashMap<ContentItem, HashMap<String, Object>> constructionMap) {
		for(ContentItem sentItem: constructionMap.keySet()){
			ContentItem constructedItem  = contentSystem.createContentItem(sentItem.getClass());
			constructedItem.setName(sentItem.getName());
			constructedItem.setLocation(sentItem.getLocalLocation());
			constructedItem.setScale(sentItem.getScale());
			constructedItem.setAngle(sentItem.getAngle());
			ConstructionManager constructManager = SynergyNet.getConstructionManagers().get(sentItem.getClass());
			if(constructManager == null){ 
				Log.error("No construction manager was found for the item:"+ sentItem.getClass().getName());
				return;
			}
			constructManager.processConstructionInfo(constructedItem, constructionMap.get(sentItem));
			sentItem.getClass().cast(constructedItem).init();
		}		
	}

	private void unsyncItems(List<String> syncItemNames) {
		for(String itemName: syncItemNames){
			ContentItem item = contentSystem.getContentItem(itemName);
			SyncManager syncManager = SynergyNet.getSyncManagerForItemClass(item.getClass());
			if(syncManager != null){
				syncManager.removeSyncListeners(item);
			}
		}
	}

	private void registerContentItem(ContentItem item){
		if(contentSystem.getAllContentItems().containsKey(item.getName())) 
			contentSystem.removeContentItem(item);
		contentSystem.addContentItem(item);
	}

	private void syncItems(List<String> syncItemNames, TableIdentity tableIdentity, Class<?> targetClass) {
		for(String itemName: syncItemNames){
			ContentItem item = contentSystem.getContentItem(itemName);
			if(item == null) continue;
			SyncManager syncManager = SynergyNet.getSyncManagerForItemClass(item.getClass());
			if(syncManager != null){
				syncManager.registerSyncTableApp(tableIdentity, targetClass);
				syncManager.addSyncListeners(item);
			}
		}
	}
	
	private void renderSyncData(HashMap<String, HashMap<Short, Object>> itemSyncDataMap) {
		for(String itemName: itemSyncDataMap.keySet()){
			ContentItem item = contentSystem.getContentItem(itemName);
			if(item == null) continue;
			SyncManager syncManager = SynergyNet.getSyncManagerForItemClass(item.getClass());
			if(syncManager != null){
				syncManager.renderSyncData(item, itemSyncDataMap.get(item.getName()));
			}
		}
	}
	
	
	public void addNetworkedContentListener(NetworkedContentListener listener){
		listeners.add(listener);
	}
	
	public void removeNetworkedContentListeners(){
		listeners.clear();
	}
	
	public interface NetworkedContentListener{
		public void itemsReceived(List<ContentItem> item, TableIdentity tableId);
		public void tableDisconnected();
		public void tableConnected();
	}
}
