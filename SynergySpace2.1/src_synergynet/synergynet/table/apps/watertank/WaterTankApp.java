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

package synergynet.table.apps.watertank;

import java.awt.Color;

import com.jme.bounding.OrthogonalBoundingBox;

import synergynet.contentsystem.ContentSystem;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.menucontrol.HoldTopRightExit;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergynet.table.gfx.GFXUtils;
import synergyspace.jme.cursorsystem.elements.twod.CursorEventDispatcher;

public class WaterTankApp extends DefaultSynergyNetApp {
	
	public WaterTankApp(ApplicationInfo info) {
		super(info);
	}

	protected WaterTank wt;

	@Override
	public void addContent() {		
		setMenuController(new HoldTopRightExit());
		wt = new WaterTank("watertank", 1024f, 768f, 1024, 768, ContentSystem.getContentSystemForSynergyNetApp(this));
		wt.setModelBound(new OrthogonalBoundingBox());
		wt.updateModelBound();
		GFXUtils.centerOrthogonalSpatial(wt);
		orthoNode.attachChild(wt);
		
		new CursorEventDispatcher(wt, 1);
	}
	
	public void setActive(boolean active) {		
		wt.clear(Color.black);
		super.setActive(active);
	}
	
	@Override
	protected void stateUpdate(float tpf) {
		wt.draw();
	}
}
