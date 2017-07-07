package synergynet.table.apps.mysteriestableportal.messages;

import synergynet.services.net.localpresence.TableIdentity;
import synergynet.services.net.tablecomms.messages.application.UnicastApplicationMessage;

public class UnicastClearMessage extends UnicastApplicationMessage{
	
	private static final long serialVersionUID = 1L;

	public UnicastClearMessage(TableIdentity tableId, Class<?> targetClass){
		super(targetClass);
		this.setRecipient(tableId);
	}
}
