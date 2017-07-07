package synergynet.table.apps.mysteriestableportal;

//import java.io.FileNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.OrthoContentItem;
import synergynet.services.net.localpresence.TableIdentity;
import synergynet.services.net.networkmanager.SynergyNet;
import synergynet.services.net.networkmanager.handlers.MessageProcessor;
import synergynet.services.net.networkmanager.handlers.NetworkedContentMessageProcessor.NetworkedContentListener;
import synergynet.services.net.tablecomms.messages.TableMessage;
import synergynet.table.SynergyNetAppUtils;
import synergynet.table.SynergyNetDesktop;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.menucontrol.HoldTopRightExit;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergynet.table.apps.mysteriestableportal.messages.AnnounceTableMessage;
import synergynet.table.apps.mysteriestableportal.messages.ClearMessage;
import synergynet.table.apps.mysteriestableportal.messages.TableDiscoveryMessage;
import synergynet.table.apps.mysteriestableportal.messages.UnicastClearMessage;
import synergynet.table.apps.mysteriestableportal.messages.UnicastMysteryPathMessage;
import synergynet.table.apps.remotecontrol.networkmanager.managers.NetworkedContentManager;
import synergynet.table.apps.remotecontrol.networkmanager.managers.TablePortalManager;
import synergynet.table.apps.remotecontrol.networkmanager.messages.ConnectTablePortalMessage;
import synergynet.table.apps.remotecontrol.networkmanager.messages.LockTableMessage;
import synergynet.table.apps.remotecontrol.networkmanager.messages.PostItemsPortalMessage;
import synergynet.table.apps.remotecontrol.networkmanager.messages.RequestItemsPortalMessage;
import synergynet.table.apps.remotecontrol.networkmanager.messages.RequestSyncItemsPortalMessage;
import synergynet.table.apps.remotecontrol.networkmanager.messages.TableDiscoveryPortalMessage;
import synergynet.table.apps.remotecontrol.networkmanager.messages.UnicastSyncDataPortalMessage;
import synergynet.table.apps.remotecontrol.tableportal.WorkspaceManager;
import synergyspace.mtinput.filters.LoggingFilter;

public class ClientApp extends DefaultSynergyNetApp {

	private ContentSystem contentSystem;
	private NetworkedContentManager manager;
	private List<ContentItem> items = new ArrayList<ContentItem>();
	private ClientMessageProcessor clientProcessor;
	private StateRecorder stateRecorder;
	private boolean restore = true;
	private String mysteryPath = null;
	
	public ClientApp(ApplicationInfo info) {
		super(info);
	}

	@Override
	public void addContent() {
		setDefaultSteadfastLimit(1);
		SynergyNetAppUtils.addTableOverlay(this);
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		contentSystem.removeAllContentItems();
		clientProcessor = new ClientMessageProcessor();
		stateRecorder = new StateRecorder(this);
		if(restore){
			items.addAll(stateRecorder.loadMysteryState());
		}
		setMenuController(new HoldTopRightExit());
		
		LoggingFilter logFilter;
		try {
			logFilter = new LoggingFilter(this.getClass());
			SynergyNetDesktop.getInstance().getMultiTouchInputComponent().addMultiTouchInputFilter(logFilter);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onActivate() {
		SynergyNet.addNetworkedContentListener(new NetworkedContentListener(){

			@Override
			public void itemsReceived(List<ContentItem> items,
					TableIdentity tableId) {
				
				for(ContentItem item: items){
					item.setName(UUID.randomUUID().toString());
					if(!item.getId().endsWith("Title")) ((OrthoContentItem)item).setRotateTranslateScalable(true);
					((OrthoContentItem)item).setScaleLimit(ControllerApp.minScaleLimit, ControllerApp.maxScaleLimit);
				}
				
				ClientApp.this.items.addAll(items);
				if(manager != null)	manager.registerContentItems(items);
				if(stateRecorder != null && mysteryPath != null) stateRecorder.registerMysteryContentItems(mysteryPath,items);
			}

			@Override
			public void tableConnected() {
				List<Class<?>> receiverClasses = new ArrayList<Class<?>>();
				receiverClasses.add(ControllerApp.class);
				if(manager != null) manager.onlineItemsList.clear();
				contentSystem = ContentSystem.getContentSystemForSynergyNetApp(ClientApp.this);
				manager = new NetworkedContentManager(contentSystem, SynergyNet.getTableCommsClientService(), receiverClasses);
				manager.registerContentItems(items);				
				WorkspaceManager.getInstance().setNetworkedContentManager(manager);
				SynergyNet.registerMessageProcessor(clientProcessor);
				try {
					SynergyNet.getTableCommsClientService().sendMessage(new AnnounceTableMessage(ControllerApp.class));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void tableDisconnected() {
				// TODO Auto-generated method stub
				
			}
		});
		SynergyNet.setAutoReconnect(true);
		SynergyNet.connect(this);
		SynergyNet.getReceiverClasses().add(ControllerApp.class);

	}
	
	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(contentSystem != null) contentSystem.update(tpf);
		if(manager != null) manager.update(tpf);
		TablePortalManager.getInstance().update();
	}

	public class ClientMessageProcessor implements MessageProcessor{
		@Override
		public void process(Object obj) {
			if(obj instanceof TableDiscoveryPortalMessage){
				manager.postAliveMessage(((TableMessage)obj).getSender());
			}else if(obj instanceof ConnectTablePortalMessage){
				manager.handleConnectionRequest(((TableMessage)obj).getSender(), ((ConnectTablePortalMessage)obj).isConnect());
			}else if(obj instanceof PostItemsPortalMessage){
				PostItemsPortalMessage msg = (PostItemsPortalMessage) obj;
				manager.processReceivedItems(msg);
			}else if(obj instanceof UnicastSyncDataPortalMessage){
				manager.syncContent(((UnicastSyncDataPortalMessage)obj).getItems());
			}else if(obj instanceof RequestItemsPortalMessage){
				RequestItemsPortalMessage msg = (RequestItemsPortalMessage) obj;
				manager.postItemsToTable(msg.getItemNames(), msg.getSender(), msg.getTargetTableId(), msg.deleteItems());
			}else if(obj instanceof RequestSyncItemsPortalMessage){
				manager.updateSyncData();
			}else if(obj instanceof TableDiscoveryMessage){
				manager.postAliveMessage(((TableDiscoveryMessage)obj).getSender());
			}else if(obj instanceof ClearMessage || obj instanceof UnicastClearMessage){
				reset();
			}else if(obj instanceof UnicastMysteryPathMessage){
				reset();
				mysteryPath = ((UnicastMysteryPathMessage)obj).getMysteryPath();
			}else if(obj instanceof LockTableMessage){
				manager.processTableMode(((LockTableMessage)obj).isLocked());
			}
		}
		
		private void reset(){
			contentSystem.removeAllContentItems();
			if(manager != null){	
				List<ContentItem> itemsToRemove = new ArrayList<ContentItem>(manager.getOnlineItems().values());
				manager.unregisterContentItems(new ArrayList<ContentItem>(items));
				itemsToRemove.clear();
			}
			items.clear();
			if(stateRecorder != null) stateRecorder.reset();
			mysteryPath = null;
		}
		
	}
}
