package synergynet.table.apps.mysteriestableportal;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.jme.system.DisplaySystem;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.OrthoContentItem;
import synergynet.contentsystem.items.SimpleButton;
import synergynet.contentsystem.items.listener.SimpleButtonAdapter;
import synergynet.contentsystem.items.listener.SubAppMenuEventListener;
import synergynet.services.net.localpresence.TableIdentity;
import synergynet.services.net.networkmanager.SynergyNet;
import synergynet.services.net.networkmanager.handlers.MessageProcessor;
import synergynet.services.net.networkmanager.handlers.NetworkedContentMessageProcessor.NetworkedContentListener;
import synergynet.services.net.tablecomms.client.TableCommsClientService;
import synergynet.services.net.tablecomms.messages.TableMessage;
import synergynet.table.SynergyNetAppUtils;
import synergynet.table.SynergyNetDesktop;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.menucontrol.HoldTopRightExit;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergynet.table.apps.mathpadapp.conceptmapping.GraphManager;
import synergynet.table.apps.mysteries.utils.SubAppMenu;
import synergynet.table.apps.mysteriestableportal.ControlMenu.ControlMenuListener;
import synergynet.table.apps.remotecontrol.networkmanager.managers.NetworkedContentManager;
import synergynet.table.apps.remotecontrol.networkmanager.managers.TablePortalManager;
import synergynet.table.apps.remotecontrol.networkmanager.messages.ConnectTablePortalMessage;
import synergynet.table.apps.remotecontrol.networkmanager.messages.PostItemsPortalMessage;
import synergynet.table.apps.remotecontrol.networkmanager.messages.RequestItemsPortalMessage;
import synergynet.table.apps.remotecontrol.networkmanager.messages.RequestSyncItemsPortalMessage;
import synergynet.table.apps.remotecontrol.networkmanager.messages.TableDiscoveryPortalMessage;
import synergynet.table.apps.remotecontrol.networkmanager.messages.UnicastSyncDataPortalMessage;
import synergynet.table.apps.remotecontrol.tableportal.TablePortal;
import synergynet.table.apps.remotecontrol.tableportal.WorkspaceManager;
import synergyspace.mtinput.filters.LoggingFilter;

public class ControllerApp extends DefaultSynergyNetApp{

	private ContentSystem contentSystem;
	private List<ContentItem> items = new ArrayList<ContentItem>();
	private NetworkedContentManager manager;
	private GraphManager graphManager;
	private boolean isMenuVisible = true;
	private static final Logger log = Logger.getLogger(TableCommsClientService.class.getName());
	private ControllerMessageProcessor controllerProcessor;
	protected String mysteryPath = ""; 
	public static float minScaleLimit = 0.5f;
	public static float maxScaleLimit = 3f;

	public ControllerApp(ApplicationInfo info) {
		super(info);
	}

	@Override
	public void addContent() {		
		
		setDefaultSteadfastLimit(1);
		setMenuController(new HoldTopRightExit());
		SynergyNetAppUtils.addTableOverlay(this);
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		contentSystem.removeAllContentItems();
		
		controllerProcessor = new ControllerMessageProcessor();
		graphManager = new GraphManager(contentSystem);
		SubAppMenu subAppMenu = new SubAppMenu(contentSystem);
		subAppMenu.addSubAppMenuEventListener(new SubAppMenuEventListener(){
			@Override
			public void menuSelected(String filePath, String appName) {
				loadContent(filePath, appName);
			}
		});
		final ControlMenu controlMenu = new ControlMenu(contentSystem, subAppMenu);
		controlMenu.addControlMenuListener(new ControlAppListener());
		
		final SimpleButton menuBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		menuBtn.setText("Hide Menu");
		menuBtn.setLocation(contentSystem.getScreenWidth()-55,20);
		menuBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,	float y, float pressure) {
				controlMenu.setVisible(!isMenuVisible);
				isMenuVisible=!isMenuVisible;
				if(menuBtn.getText().equals("Hide Menu")) 
					menuBtn.setText("Show Menu");
				else
					menuBtn.setText("Hide Menu");
			}
		});
		LoggingFilter logFilter;
		try {
			logFilter = new LoggingFilter(this.getClass());
			SynergyNetDesktop.getInstance().getMultiTouchInputComponent().addMultiTouchInputFilter(logFilter);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	protected void loadContent(String filePath, String appName) {
		removeAllItems();
		mysteryPath = filePath;
		items.addAll(contentSystem.loadContentItems(filePath));
		for(ContentItem item: items){
			((OrthoContentItem)item).setScaleLimit(minScaleLimit, maxScaleLimit);
		}
		if(manager != null) manager.registerContentItems(items);
	}

	public void removeAllItems(){
		if(manager != null){ 	
			List<ContentItem> itemsToRemote = new ArrayList<ContentItem>(manager.getOnlineItems().values());
			manager.unregisterContentItems(itemsToRemote);
			itemsToRemote.clear();
		}
		for(ContentItem item: items) contentSystem.removeContentItem(item);
		items.clear();
	}

	@Override
	public void onActivate() {
		SynergyNet.addNetworkedContentListener(new NetworkedContentListener(){

			@Override
			public void itemsReceived(List<ContentItem> item,
					TableIdentity tableId) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void tableDisconnected() {
				log.severe("Disconnected");
				if(manager != null)	manager.removeNetworkListeners();
			}

			@Override
			public void tableConnected() {
				log.info("Connected");
				List<Class<?>> receiverClasses = new ArrayList<Class<?>>();
				receiverClasses.add(ClientApp.class);
				if(manager != null){
					manager.removeNetworkListeners();
					manager.onlineItemsList.clear();
				}
				manager = new NetworkedContentManager(contentSystem, SynergyNet.getTableCommsClientService(), receiverClasses);
				manager.registerContentItems(items);				
				WorkspaceManager.getInstance().setNetworkedContentManager(manager);
				TablePortalManager.getInstance().setNetworkedContentManager(manager);
				SynergyNet.registerMessageProcessor(controllerProcessor);
			}
			
		});
		SynergyNet.setAutoReconnect(true);
		SynergyNet.connect(this);
		SynergyNet.getReceiverClasses().add(ClientApp.class);

	}
	
	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(contentSystem != null) contentSystem.update(tpf);
		if(manager != null) manager.update(tpf);
		TablePortalManager.getInstance().update();
	}
	
	class ControlAppListener implements ControlMenuListener{

		@Override
		public void sendDesktopData() {
			SendDataDialog sendDataDialog = new SendDataDialog(ControllerApp.this, mysteryPath);
			sendDataDialog.getWindow().setVisible(true);
			sendDataDialog.getWindow().centerItem();
			sendDataDialog.getWindow().setAsTopObject();
		}

		@Override
		public void clearStudentTables() {
			ClearDataDialog clearDataDialog = new ClearDataDialog(manager,contentSystem);
			clearDataDialog.getWindow().setVisible(true);
			clearDataDialog.getWindow().centerItem();
			clearDataDialog.getWindow().setAsTopObject();
		}

		@Override
		public void clearLocalTable() {
			removeAllItems();
		}

		@Override
		public void createTablePortals() {
			int locX = DisplaySystem.getDisplaySystem().getWidth()/2;
			int locY = DisplaySystem.getDisplaySystem().getHeight()/2;

			for(TableIdentity tableId: SynergyNet.getTableCommsClientService().getCurrentlyOnline()){
				if(!tableId.equals(TableIdentity.getTableIdentity())){
					if(TablePortalManager.getInstance().getTablePortalForTable(tableId)== null){
						TablePortal portal = new TablePortal(contentSystem, manager, graphManager);
						portal.getWindow().setScaleLimit(0.6f, 2f);
						portal.getWindow().setScale(0.8f);
						TablePortalManager.getInstance().registerTablePortal(portal);
						portal.connect(tableId);
						portal.getWindow().setLocation(locX, locY);
						locX+=50; locY-=50;
						if(tableId.toString().equalsIgnoreCase("red")){
							portal.getWindow().setBackgroundColour(Color.red);
						}else if(tableId.toString().equalsIgnoreCase("green")){
							portal.getWindow().setBackgroundColour(Color.green);
						}else if(tableId.toString().equalsIgnoreCase("blue")){
							portal.getWindow().setBackgroundColour(Color.blue);
						}else if(tableId.toString().equalsIgnoreCase("yellow")){
							portal.getWindow().setBackgroundColour(Color.yellow);
						}
					}else{
						TablePortal portal = TablePortalManager.getInstance().getTablePortalForTable(tableId);
						portal.getWindow().setVisible(true);
						//portal.connect(tableId);
					}
				}
			}
		}

		@Override
		public void hideTablePortals() {
			TablePortalManager.getInstance().hideAll();
		}
		
	}
	
	public NetworkedContentManager getNetworkedContentManager(){
		return manager;
	}
	
	public class ControllerMessageProcessor implements MessageProcessor{
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
				}				
				manager.fireMessageReceived(obj);
		}
	}
	
}
