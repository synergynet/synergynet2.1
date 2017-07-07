/*
 * Copyright (c) 2008 University of Durham, England
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
 * * Neither the name of 'SynergySpace' nor the names of its contributors 
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

package synergyspace.mtinput.luminja;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.jme.math.Vector2f;

import de.evoluce.multitouch.adapter.java.BlobJ;
import de.evoluce.multitouch.adapter.java.JavaAdapter;

import synergynet.table.config.table.TableConfigPrefsItem;
import synergyspace.mtinput.ClickDetector;
import synergyspace.mtinput.IMultiTouchEventListener;
import synergyspace.mtinput.IMultiTouchInputSource;
import synergyspace.mtinput.events.MultiTouchCursorEvent;
import synergyspace.mtinput.exceptions.MultiTouchInputException;

/**
 * Support for the Lumin multi-touch table.  This implementation currently
 * only supports cursor information - it does not support objects/fiducials.
 * @author dcs0ah1
 *
 */
public class LuminMultiTouchInput implements IMultiTouchInputSource {
	protected List<IMultiTouchEventListener> listeners = new ArrayList<IMultiTouchEventListener>();
	protected JavaAdapter ja;	
	protected BlobJ[] currentBlobs = new BlobJ[0];

	protected Map<Integer, BlobJ> currentBlobRegistry = new HashMap<Integer,BlobJ>();
	protected Map<Integer, Vector2f> lastKnownPosition = new HashMap<Integer,Vector2f>();
	protected float samePositionTolerance = 0.002f;
	protected ClickDetector clickDetector = new ClickDetector(500, 2f);
	ExecutorService executor = Executors.newCachedThreadPool();

	protected TableConfigPrefsItem tableConfig = new TableConfigPrefsItem();
	private GetBlobsTask task;

	public LuminMultiTouchInput() {
		if(tableConfig.isAutoStartMTServer()){
			try {
				MIMProcessController.getInstance().start(tableConfig.getMTServerPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ja = new JavaAdapter("localhost");
		task = new GetBlobsTask(ja);
	}




	private void process() throws MultiTouchInputException {
		try {
			Future<BlobJ[]> future = executor.submit(task);
			BlobJ[] result = future.get(5, TimeUnit.SECONDS);
			currentBlobs = result;
		} catch (Exception e) {	
			if(tableConfig.isAutoStartMTServer()){
				try {
					MIMProcessController.getInstance().kill();
					MIMProcessController.getInstance().start(tableConfig.getMTServerPath());
				} catch (IOException e1) {
					throw new MultiTouchInputException(e, e1);
				} catch (InterruptedException e1) {
					throw new MultiTouchInputException(e, e1);
				}
			}
			throw new MultiTouchInputException(e);
		}

		Vector2f pos = new Vector2f();
		Vector2f vel = new Vector2f();

		Map<Integer, BlobJ> newRegistry = new HashMap<Integer,BlobJ>();
		// notify for new blobs or changes to existing blobs
		for(BlobJ blob : currentBlobs) {
			newRegistry.put(blob.mID, blob);
			pos.x = blob.mX;
			pos.y = blob.mY;

			MultiTouchCursorEvent event = new MultiTouchCursorEvent(blob.mID, new Point2D.Float(pos.x, 1-pos.y), new Point2D.Float(vel.x, vel.y), blob.mWidth, 0f);

			if(currentBlobRegistry.containsKey(blob.mID)) {
				for(IMultiTouchEventListener listener : listeners) {					
					listener.cursorChanged(event);
				}
				lastKnownPosition.put(blob.mID, new Vector2f(blob.mX, 1-blob.mY));
			}else{
				clickDetector.newCursorPressed(blob.mID, new Point2D.Float(pos.x, 1-pos.y));
				for(IMultiTouchEventListener listener : listeners) {					
					listener.cursorPressed(event);
				}				
			}
		}


		// notify if blobs have disappeared
		for(Integer i : currentBlobRegistry.keySet()) {
			if(!newRegistry.keySet().contains(i)) {
				BlobJ blob = currentBlobRegistry.get(i);
				pos.x = blob.mX;
				pos.y = blob.mY;			
				MultiTouchCursorEvent event = new MultiTouchCursorEvent(blob.mID, new Point2D.Float(pos.x, 1-pos.y), new Point2D.Float(vel.x, vel.y), blob.mWidth, 0f);

				for(IMultiTouchEventListener l : listeners) {
					int clickCount = clickDetector.cursorReleasedGetClickCount(blob.mID, new Point2D.Float(pos.x, 1-pos.y));
					if(clickCount > 0) {
						event.setClickCount(clickCount);
						l.cursorClicked(event);
					}
					l.cursorReleased(event);
				}
				lastKnownPosition.remove(blob.mID);
			}
		}

		currentBlobRegistry = newRegistry;
	}

	public void setClickSensitivity(long time, float distance) {
		clickDetector.setSensitivity(time, distance);
	}

	public void registerMultiTouchEventListener(IMultiTouchEventListener listener) {
		if(!listeners.contains(listener)) listeners.add(listener);	
	}

	public void registerMultiTouchEventListener(IMultiTouchEventListener listener, int index) {
		if(!listeners.contains(listener)) listeners.add(index, listener);
	}

	public void unregisterMultiTouchEventListener(IMultiTouchEventListener listener) {
		listeners.remove(listener);
	}

	public void setSamePositionTolerance(float samePositionTolerance) {
		this.samePositionTolerance = samePositionTolerance;
	}

	@Override
	public void update(float tpf) throws MultiTouchInputException {
		process();
	}

}

