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

import java.util.HashSet;

import org.sigtec.input.AbstractInputDevice;
import org.sigtec.input.BufferedInputDeviceEventListener;
import org.sigtec.input.InputDeviceEvent;
import org.sigtec.input.InputDeviceEventListener;
import org.ximtec.igesture.Recogniser;
import org.ximtec.igesture.core.ResultSet;
import org.ximtec.igesture.io.ButtonDeviceEventListener;
import org.ximtec.igesture.io.InputDeviceClient;
import org.ximtec.igesture.io.MouseReaderEvent;
import org.ximtec.igesture.io.MouseReaderEventListener;

import synergyspace.jme.cursorsystem.CursorRegistry;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;

public class CursorInputDevice extends AbstractInputDevice implements ButtonDeviceEventListener {
	private HashSet<ButtonDeviceEventListener> buttonUpEvents = new HashSet<ButtonDeviceEventListener>();
	private Recogniser recogniser;
	private long id;
	private InputDeviceClient client;

	public CursorInputDevice(long cid, Recogniser recog) {
		this.id = cid;
		this.recogniser = recog;

		InputDeviceEventListener listener = new BufferedInputDeviceEventListener(new MouseReaderEventListener(), 10000);
		client = new InputDeviceClient(this, listener);
		this.addButtonDeviceEventListener(this);
	}

	public void fireInputDeviceEvent2(MouseReaderEvent mouseReaderEvent) {
		super.fireInputDeviceEvent(mouseReaderEvent);		
	}

	public void fireMouseButtonEvent(InputDeviceEvent event) {		
		for (final ButtonDeviceEventListener listener : buttonUpEvents) {
			listener.handleButtonPressedEvent(event);
		}
	}

	public void addButtonDeviceEventListener(ButtonDeviceEventListener listener) {
		buttonUpEvents.add(listener);

	}

	public void removeButtonDeviceEventListener(ButtonDeviceEventListener listener) {
		buttonUpEvents.remove(listener);
	}

	public void handleButtonPressedEvent(InputDeviceEvent event) {
		ResultSet result = recogniser.recognise(client.createNote(0, event.getTimestamp(), 140));
		client.clearBuffer();
		ScreenCursor sc = CursorRegistry.getInstance().getCursor(id);
		if(sc != null) {
			if (result.isEmpty()) {
				GestureSystem.getInstance().sendGestureEvent(new InvalidGestureEvent(id, "", sc.getCurrentCursorScreenPosition().x, sc.getCurrentCursorScreenPosition().y));
			} else {
				GestureSystem.getInstance().sendGestureEvent(new GestureEvent(id, result.getResult().getGestureClassName(), sc.getCurrentCursorScreenPosition().x, sc.getCurrentCursorScreenPosition().y));
			}
		}
	}

	public long getID() {
		return id;
	}
}
