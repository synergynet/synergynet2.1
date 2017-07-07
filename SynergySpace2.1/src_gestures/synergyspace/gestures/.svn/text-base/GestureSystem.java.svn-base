package synergyspace.gestures;

import java.util.ArrayList;
import java.util.List;

public class GestureSystem {
	private static GestureSystem instance;

	public static GestureSystem getInstance() {
		synchronized(GestureSystem.class) {
			if(instance == null) instance = new GestureSystem();
			return instance;
		}
	}

	protected List<GestureListener> listeners = new ArrayList<GestureListener>();
	protected iGestureProcessor processor = new iGestureProcessor();
	
	public iGestureProcessor getProcessor() {
		return processor;
	}

	public void addListener(GestureListener listener) {
		if(!listeners.contains(listener)) listeners.add(listener);
	}
	
	public void removeListener(GestureListener listener) {
		listeners.remove(listener);
	}
	
	public void sendGestureEvent(GestureEvent event) {
		for(GestureListener l : listeners) {
			l.gestureEvent(event);
		}
	}
}
