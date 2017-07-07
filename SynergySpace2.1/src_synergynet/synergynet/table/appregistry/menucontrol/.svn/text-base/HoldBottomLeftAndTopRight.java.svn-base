/*
 * Copyright (c) 2009 University of Durham, England
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

package synergynet.table.appregistry.menucontrol;

import java.util.HashMap;
import java.util.Map;

import synergynet.table.SynergyNetDesktop;
import synergynet.table.apps.SynergyNetApp;
import synergyspace.jme.sysutils.BorderUtility;
import synergyspace.mtinput.IMultiTouchEventListener;
import synergyspace.mtinput.events.MultiTouchCursorEvent;
import synergyspace.mtinput.events.MultiTouchObjectEvent;

public class HoldBottomLeftAndTopRight extends MenuController implements IMultiTouchEventListener{


	protected int cornerDistance = 20;
	protected Map<Long,Long> bottomLeftCursorTiming = new HashMap<Long,Long>();
	protected Map<Long,Long> topRightCursorTiming = new HashMap<Long,Long>();
	protected long interval = 1000;
	private boolean enabled;

	@Override
	public void enableForApplication(SynergyNetApp app) {
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent().registerMultiTouchEventListener(this);
		setEnabled(true);	
	}

	@Override
	public void applicationFinishing() {
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent().unregisterMultiTouchEventListener(this);
		synchronized(bottomLeftCursorTiming) {
			bottomLeftCursorTiming.clear();
		}
		synchronized(topRightCursorTiming) {
			topRightCursorTiming.clear();
		}
		setEnabled(false);
	}



	public boolean isEnabled() {
		return enabled;
	}

	private void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}



	public void cursorPressed(MultiTouchCursorEvent event) {
		if(isInBottomLeft(event)) {
			synchronized(bottomLeftCursorTiming) {
				bottomLeftCursorTiming.put(event.getCursorID(), System.currentTimeMillis());
			}
		}else if(isInTopRight(event)) {
			synchronized(topRightCursorTiming) {
				topRightCursorTiming.put(event.getCursorID(), System.currentTimeMillis());
			}
		}
	}

	public void cursorReleased(MultiTouchCursorEvent event) {

		synchronized(bottomLeftCursorTiming) {
			bottomLeftCursorTiming.remove(event.getCursorID());
		}

		synchronized(topRightCursorTiming) {
			topRightCursorTiming.put(event.getCursorID(), System.currentTimeMillis());
		}


	}


	private boolean isInTopRight(MultiTouchCursorEvent event) {
		int x = SynergyNetDesktop.getInstance().tableToScreenX(event.getPosition().x);
		int y = SynergyNetDesktop.getInstance().tableToScreenY(event.getPosition().y);
		
		return
		x < BorderUtility.getTopRightCorner().x + cornerDistance && x > BorderUtility.getTopRightCorner().x - cornerDistance &&
		y < BorderUtility.getTopRightCorner().y + cornerDistance && y > BorderUtility.getTopRightCorner().y - cornerDistance;
	}

	private boolean isInBottomLeft(MultiTouchCursorEvent event) {
		int x = SynergyNetDesktop.getInstance().tableToScreenX(event.getPosition().x);
		int y = SynergyNetDesktop.getInstance().tableToScreenY(event.getPosition().y);
		
		return
		x < BorderUtility.getBottomLeftCorner().x + cornerDistance && x > BorderUtility.getBottomLeftCorner().x - cornerDistance &&
		y < BorderUtility.getBottomLeftCorner().y + cornerDistance && y > BorderUtility.getBottomLeftCorner().y - cornerDistance;
	}

	public void update(float interpolation) {
		long endTime = System.currentTimeMillis();

		Map<Long,Long> btm = new HashMap<Long,Long>();
		for(Long id : bottomLeftCursorTiming.keySet()) {
			long startTime = bottomLeftCursorTiming.get(id);
			if(endTime - startTime > interval ) {
				btm.put(id, startTime);
			}
		}

		Map<Long,Long> top = new HashMap<Long,Long>();
		for(Long id : topRightCursorTiming.keySet()) {
			long startTime = topRightCursorTiming.get(id);
			if(endTime - startTime > interval ) {
				top.put(id, startTime);
			}
		}		

		if(btm.size() > 0 && top.size() > 0) {
			synchronized(bottomLeftCursorTiming) {
				bottomLeftCursorTiming.clear();
			}
			synchronized(topRightCursorTiming) {
				topRightCursorTiming.clear();
			}
			if(enabled) {
				try {
					SynergyNetDesktop.getInstance().showMainMenu();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {}
	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {}
	@Override
	public void objectAdded(MultiTouchObjectEvent event) {}
	@Override
	public void objectChanged(MultiTouchObjectEvent event) {}
	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {}


}
