package synergynet.services.net.networkmanager.syncmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.SketchPad;
import synergynet.contentsystem.items.SketchPad.DrawListener;
import synergynet.contentsystem.jme.items.utils.DrawData;

public class SketchPadSyncManager extends OrthoRotateTranslateScaleSyncManager{

	public static final short SYNC_ITEM_DRAW_DATA = 7;
	public static final short SYNC_ITEM_CLEAR_PAD = 8;
	
	public SketchPadSyncManager() {}

	@Override
	public void addSyncListeners(ContentItem item){
		super.addSyncListeners(item);
		
		if(item instanceof SketchPad){
			final SketchPad pad = ((SketchPad)item);
			pad.addDrawListener(new DrawListener(){

				@SuppressWarnings("unchecked")
				@Override
				public void itemDrawn(DrawData drawData) {
					Object storedDrawData = SketchPadSyncManager.this.getSyncData(pad, SYNC_ITEM_DRAW_DATA);
					if(storedDrawData == null) storedDrawData = new ArrayList<DrawData>();
					((List<DrawData>)storedDrawData).add(drawData);
					SketchPadSyncManager.this.addSyncData(pad, SYNC_ITEM_DRAW_DATA, storedDrawData);
				}

				@Override
				public void padCleared() {
					SketchPadSyncManager.this.addSyncData(pad, SYNC_ITEM_CLEAR_PAD, "clear");
				}
			});
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void renderSyncData( ContentItem item, Map<Short, Object> itemAttrs) {
		super.renderSyncData(item, itemAttrs);
		//draw data
		if(item instanceof SketchPad){
			if(itemAttrs.containsKey(SYNC_ITEM_DRAW_DATA)){
				List<DrawData> drawData = (ArrayList<DrawData>)itemAttrs.get(SYNC_ITEM_DRAW_DATA);
				((SketchPad)item).draw(drawData);
			}
			if(itemAttrs.containsKey(SYNC_ITEM_CLEAR_PAD))
				((SketchPad)item).clearAll();
		}
	}
}
