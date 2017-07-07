package synergynet.table.apps.mysteriestableportal.messages;

import synergynet.services.net.localpresence.TableIdentity;
import synergynet.services.net.tablecomms.messages.application.UnicastApplicationMessage;

public class UnicastMysteryPathMessage extends UnicastApplicationMessage{
	
	private static final long serialVersionUID = 1L;

	private String mysteryPath;
	
	public UnicastMysteryPathMessage(TableIdentity tableId, Class<?> targetClass, String mysteryPath){
		super(targetClass);
		this.setRecipient(tableId);
		this.mysteryPath = mysteryPath;
	}
	
	public void setMysteryPath(String mysteryPath){
		this.mysteryPath = mysteryPath;
	}
	
	public String getMysteryPath(){
		return mysteryPath;
	}
}
