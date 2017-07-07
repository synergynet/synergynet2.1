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

package synergynet.table.apps.projectmanagement;

import synergynet.contentsystem.ContentSystem;
import synergynet.table.SynergyNetAppUtils;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.menucontrol.HoldBottomLeftExit;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergynet.table.apps.projectmanagement.component.ganttchart.GanttChart;

public class ProjectManagementApp extends DefaultSynergyNetApp {
	
	protected ContentSystem contentSystem;	
	protected GanttChart ganttChart;
	
	public ProjectManagementApp(ApplicationInfo info) {
		super(info);
	}

	@Override
	public void addContent() {
		
		setMenuController(new HoldBottomLeftExit());
		SynergyNetAppUtils.addTableOverlay(this);
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		ganttChart = new GanttChart(contentSystem, 700, 520);
		orthoNode.attachChild(ganttChart);
		
		
	}
	
	@Override
	public void stateUpdate(float tpf) {
		if(contentSystem != null) contentSystem.update(tpf);
		ganttChart.update();
	}
	
	@Override
	public void onDeactivate() {
		super.onDeactivate();
	}
	
}

