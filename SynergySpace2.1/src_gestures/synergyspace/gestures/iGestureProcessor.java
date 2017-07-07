/*
 * Copyright (c) 2007 University of Durham
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet-MT' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergyspace.gestures;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.sigtec.ink.input.Location;
import org.sigtec.ink.input.TimestampedLocation;
import org.sigtec.input.InputDeviceEvent;
import org.ximtec.igesture.Recogniser;
import org.ximtec.igesture.algorithm.AlgorithmException;
import org.ximtec.igesture.configuration.Configuration;
import org.ximtec.igesture.core.DigitalDescriptor;
import org.ximtec.igesture.core.GestureSet;
import org.ximtec.igesture.event.EventManager;
import org.ximtec.igesture.io.MouseReaderEvent;
import org.ximtec.igesture.util.XMLTool;

import com.jme.system.DisplaySystem;

import synergyspace.mtinput.IMultiTouchEventListener;
import synergyspace.mtinput.events.MultiTouchCursorEvent;
import synergyspace.mtinput.events.MultiTouchObjectEvent;

public class iGestureProcessor implements IMultiTouchEventListener {

	private Recogniser recogniser;
	private Map<Long,CursorInputDevice> activeCursors = new HashMap<Long,CursorInputDevice>();
	public static final int MODE_GLOBAL = 1;
	public static final int MODE_ON_NODE = 2;
	protected static int mode = MODE_GLOBAL;

	public static void setMode(int newMode) {
		mode = newMode;
	}

	public iGestureProcessor() {
		init();
	}

	private void init() {
		Configuration configuration = XMLTool.importConfiguration(new File(iGestureProcessor.class.getResource("rubineconfiguration.xml").getFile()));
		GestureSet gestureSet = XMLTool.importGestureSet(new File(iGestureProcessor.class.getResource("demogestures.xml").getFile())).get(0);

		EventManager eventManager = new EventManager();
		SynergySpaceGestureEventHandler drawEventHandler = new SynergySpaceGestureEventHandler();

		gestureSet.getGestureClass("Rectangle").addDescriptor(DigitalDescriptor.class, new RectangleDescriptor());
		gestureSet.getGestureClass("LeftRight").addDescriptor(DigitalDescriptor.class, new LineDescriptor());
		gestureSet.getGestureClass("Triangle").addDescriptor(DigitalDescriptor.class, new TriangleDescriptor());
		gestureSet.getGestureClass("Arrow").addDescriptor(DigitalDescriptor.class, new ArrowDescriptor());

		eventManager.registerEventHandler("Rectangle", drawEventHandler);
		eventManager.registerEventHandler("LeftRight", drawEventHandler);
		eventManager.registerEventHandler("Triangle", drawEventHandler);
		eventManager.registerEventHandler("Arrow", drawEventHandler);
		eventManager.registerEventHandler("Delete", drawEventHandler);
		eventManager.registerEventHandler("Red", drawEventHandler);
		eventManager.registerEventHandler("Black", drawEventHandler);
		eventManager.registerEventHandler("Yellow", drawEventHandler);
		eventManager.registerEventHandler("Thin", drawEventHandler);
		eventManager.registerEventHandler("Fat", drawEventHandler);

		configuration.addGestureSet(gestureSet);
		configuration.setEventManager(eventManager);

		try {
			recogniser = new Recogniser(configuration);
		}
		catch (AlgorithmException e) {
			System.out.println(e);
		}
	}


//	private void processEventOnNode(MultiTouchEvent event) {
//		if(event instanceof NewCursorOnNodeEvent) {
//			NewCursorOnNodeEvent evt = (NewCursorOnNodeEvent) event;
//			if(evt.getNode().getCapabilities().acceptsGestures) {
//				CursorInputDevice cid = new CursorInputDevice(evt.getID(), recogniser);
//				activeCursors.put(evt.getID(), cid);
//			}
//		}else if(event instanceof UpdatedCursorOnNodeEvent) {
//			UpdatedCursorOnNodeEvent evt = (UpdatedCursorOnNodeEvent) event;
//			if(evt.getNode().getCapabilities().acceptsGestures) {
//				CursorInputDevice cid = activeCursors.get(evt.getID());
//				if(cid != null) {
//					Location location = new Location("screen", 1, ScreenProjectionData.tableToScreenPoint(evt.getTableX(), evt.getTableY()));
//					TimestampedLocation tsl = new TimestampedLocation(location, System.currentTimeMillis());
//					cid.fireInputDeviceEvent2(new MouseReaderEvent(tsl));
//				}
//			}
//		}else if(event instanceof RemovedCursorOnNodeEvent) {
//			RemovedCursorOnNodeEvent evt = (RemovedCursorOnNodeEvent) event;
//			if(evt.getNode().getCapabilities().acceptsGestures) {
//				CursorInputDevice cid = activeCursors.get(evt.getID());
//				cid.fireMouseButtonEvent(new InputDeviceEvent() {
//					public long getTimestamp() {
//						return System.currentTimeMillis();
//					}
//				});
//				activeCursors.remove(evt.getID());	
//			}
//		}
//	}


	public void cursorChanged(MultiTouchCursorEvent event) {
//		switch(mode) {
//		case MODE_GLOBAL : {
//			processEventGlobalMode(event);
//			break;
//		}
//
//		case MODE_ON_NODE : {
//			processEventOnNode(event);
//			break;
//		}
//		}
		
		CursorInputDevice cid = activeCursors.get(event.getCursorID());
		if(cid != null) {
			float screenX = DisplaySystem.getDisplaySystem().getRenderer().getWidth() * event.getPosition().x;
			float screenY = DisplaySystem.getDisplaySystem().getRenderer().getHeight() * event.getPosition().y;
			Location location = new Location("screen", 1, screenX, screenY);
			TimestampedLocation tsl = new TimestampedLocation(location, System.currentTimeMillis());
			cid.fireInputDeviceEvent2(new MouseReaderEvent(tsl));
		}
	}


	public void cursorPressed(MultiTouchCursorEvent event) {
		CursorInputDevice cid = new CursorInputDevice(event.getCursorID(), recogniser);
		activeCursors.put(event.getCursorID(), cid);
		
	}

	public void cursorReleased(MultiTouchCursorEvent event) {
		CursorInputDevice cid = activeCursors.get(event.getCursorID());
		cid.fireMouseButtonEvent(new InputDeviceEvent() {
			public long getTimestamp() {
				return System.currentTimeMillis();
			}
		});
		activeCursors.remove(event.getCursorID());
		
	}
	
	public void cursorClicked(MultiTouchCursorEvent event) {}
	public void objectAdded(MultiTouchObjectEvent event) {}
	public void objectChanged(MultiTouchObjectEvent event) {}
	public void objectRemoved(MultiTouchObjectEvent event) {}
}
