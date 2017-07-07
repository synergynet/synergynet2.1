package synergynet.table.apps.remotecontrol.networkmanager.messages;

import synergynet.services.net.localpresence.TableIdentity;
import synergynet.services.net.tablecomms.messages.application.UnicastApplicationMessage;

public class LockTableMessage extends UnicastApplicationMessage{

	private static final long serialVersionUID = 3611717853521792797L;
	
	protected boolean isLocked = false;
	
	public LockTableMessage(){
		super();
	}
	
	public LockTableMessage(Class<?> targetClass, TableIdentity targetTableId){
		super(targetClass);
		this.setRecipient(targetTableId);
	}
	
	public void enableLock(boolean isLocked){
		this.isLocked = isLocked;
	}
	
	public boolean isLocked(){
		return this.isLocked;
	}
}
