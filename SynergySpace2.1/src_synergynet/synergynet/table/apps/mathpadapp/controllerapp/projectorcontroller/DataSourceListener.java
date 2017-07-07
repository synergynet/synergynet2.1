package synergynet.table.apps.mathpadapp.controllerapp.projectorcontroller;

import java.util.HashMap;

import synergynet.services.net.localpresence.TableIdentity;
import synergynet.table.apps.mathpadapp.mathtool.MathToolInitSettings;
import synergynet.table.apps.mathpadapp.networkmanager.utils.UserIdentity;

public interface DataSourceListener {
	
	public void sourceDataUpdated(TableIdentity tableId,	HashMap<UserIdentity, MathToolInitSettings> items);
	public void syncDataReceived(TableIdentity tableId, HashMap<UserIdentity, HashMap<Short, Object>> syncData);
	
}
