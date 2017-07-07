package synergynet.services.net.networkmanager.constructionmanagers;

import java.util.HashMap;

import synergynet.contentsystem.items.ContentItem;

public interface ConstructionManager{

	
	public HashMap<String, Object> buildConstructionInfo(ContentItem item);
	public abstract void processConstructionInfo(ContentItem item, HashMap<String, Object> info);
}
