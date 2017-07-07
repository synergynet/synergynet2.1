package synergynet.services.net.networkmanager.handlers;

import java.util.concurrent.Callable;

import com.jme.util.GameTaskQueueManager;

import synergynet.services.net.networkmanager.messages.networkflick.AnnounceTableMessage;
import synergynet.services.net.networkmanager.messages.networkflick.EnableFlickMessage;
import synergynet.services.net.networkmanager.messages.networkflick.RegisterTableMessage;
import synergynet.services.net.networkmanager.messages.networkflick.TransferableContentItem;
import synergynet.services.net.networkmanager.messages.networkflick.UnregisterTableMessage;
import synergynet.services.net.networkmanager.utils.networkflick.TransferController;

public class NetworkFlickMessageProcessor implements MessageProcessor{

	protected TransferController transferController;
	
	public NetworkFlickMessageProcessor(TransferController transferController){
		this.transferController = transferController;
	}
	@Override
	public void process(final Object obj) {

		if(transferController != null){
			if(obj instanceof EnableFlickMessage){
				transferController.enableNetworkFlick(((EnableFlickMessage)obj).isFlickEnabled());
			}
			else if(obj instanceof AnnounceTableMessage){
				AnnounceTableMessage msg = (AnnounceTableMessage)obj;
				transferController.registerRemoteTable(msg.getTableInfo());
				transferController.sendRegistrationMessage(msg.getSender());
			}
			else if(obj instanceof RegisterTableMessage){
				RegisterTableMessage msg = (RegisterTableMessage)obj;
				transferController.registerRemoteTable(msg.getTableInfo());
			}
			else if(obj instanceof UnregisterTableMessage)
				transferController.cleanUpUnregisteredTable((UnregisterTableMessage)obj);
			else if(obj instanceof TransferableContentItem){
				GameTaskQueueManager.getManager().update(new Callable<Object>(){
					public Object call() throws Exception{
					transferController.applyTransferableContentItem((TransferableContentItem)obj);
					return null;
					}
				});
			}
		}
	}

}
