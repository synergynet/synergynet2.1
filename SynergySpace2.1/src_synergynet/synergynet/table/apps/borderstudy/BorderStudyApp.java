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

package synergynet.table.apps.borderstudy;

import java.awt.Color;
import java.awt.Font;

import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ImageTextLabel;
import synergynet.contentsystem.items.TextLabel;
import synergynet.table.SynergyNetAppUtils;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.menucontrol.HoldBottomLeftExit;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergyspace.jme.sysutils.BorderUtility;


public class BorderStudyApp extends DefaultSynergyNetApp {
	
	public BorderStudyApp(ApplicationInfo info) {
		super(info);
	}

	private ContentSystem contentSystem;

	@Override
	public void addContent() {
		SynergyNetAppUtils.addTableOverlay(this);
		setMenuController(new HoldBottomLeftExit());
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
	}
	
	@Override
	public void onActivate() {
		contentSystem.removeAllContentItems();
		BorderUtility.setBorderColour(ColorRGBA.darkGray);
		if (BorderUtility.isVirtualRectangleValid()){
			ImageTextLabel rectangle = (ImageTextLabel)this.contentSystem.createContentItem(ImageTextLabel.class);
			rectangle.setSteadfastLimit(2);;
			rectangle.centerItem();
			rectangle.setAutoFit(false);
			rectangle.setBorderSize(0);
			rectangle.setBackgroundColour(Color.gray);
			rectangle.setWidth(DisplaySystem.getDisplaySystem().getRenderer().getWidth());
			rectangle.setHeight(DisplaySystem.getDisplaySystem().getRenderer().getHeight());
//			rectangle.setScale(BorderUtility.getVirtualRectangleEnvironmentScale());
//			rectangle.setAngle(BorderUtility.getVirtualRectangleEnvironmentRotation());
//			rectangle.setLocalLocation(rectangle.getLocation().x + BorderUtility.getVirtualRectangleEnvironmentTranslationX(), 
//					rectangle.getLocation().y + BorderUtility.getVirtualRectangleEnvironmentTranslationY());
			rectangle.setRotateTranslateScalable(false);
			rectangle.setAsTopObject();
		}else{
			TextLabel text = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
			
			text.setAsTopObject();
			text.setText("This Application is not compatible with the display shape being used.");
			text.setFont(new Font("Arial", Font.PLAIN,20));
			text.setBackgroundColour(Color.black);
			text.setTextColour(Color.white);
			text.setBorderSize(0);
			text.setRotateTranslateScalable(false);	
			text.centerItem();
			
		}
	}
	
	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(contentSystem != null) contentSystem.update(tpf);
	}

}
