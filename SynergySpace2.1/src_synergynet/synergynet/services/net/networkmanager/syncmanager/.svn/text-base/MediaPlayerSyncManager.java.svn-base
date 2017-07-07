package synergynet.services.net.networkmanager.syncmanager;

import java.util.Map;

import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.MediaPlayer;
import synergynet.contentsystem.jme.items.JMEMediaPlayer.PlayListener;

public class MediaPlayerSyncManager extends OrthoRotateTranslateScaleSyncManager{

	public static final short VIDEO_PLAY = 7;
	
	public MediaPlayerSyncManager() {}

	@Override
	public void addSyncListeners(ContentItem item){
		super.addSyncListeners(item);
		
		if(item instanceof MediaPlayer){
			final MediaPlayer video = ((MediaPlayer)item);
			video.addPlayerListener(new PlayListener(){

				@Override
				public void videoPlayed() {
					MediaPlayerSyncManager.this.addSyncData(video, VIDEO_PLAY, true);
				}

				@Override
				public void videoStopped() {
					MediaPlayerSyncManager.this.addSyncData(video, VIDEO_PLAY, false);
				}
			});
		}
		
	}
	
	@Override
	public void renderSyncData( ContentItem item, Map<Short, Object> itemAttrs) {
		super.renderSyncData(item, itemAttrs);
		//draw data
		if(item instanceof MediaPlayer){
			if(itemAttrs.containsKey(VIDEO_PLAY)) ((MediaPlayer)item).setPlaying(Boolean.valueOf(itemAttrs.get(VIDEO_PLAY).toString()));
		}
	}
}
