/* Copyright (c) 2008 University of Durham, England
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

package synergynet.contentsystem.jme.items;

import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.RoundFrame;
import synergynet.contentsystem.items.RoundWindow;
import synergynet.contentsystem.items.implementation.interfaces.IRoundWindowImplementation;
import synergynet.contentsystem.items.utils.Background;
import synergynet.contentsystem.items.utils.Border;

public class JMERoundWindow extends JMEOrthoContainer implements IRoundWindowImplementation {

	protected RoundFrame backgroundFrame;
	
	public JMERoundWindow(ContentItem contentItem) {
		super(contentItem);		
	}
	
	public void init(){
		super.init();
		backgroundFrame = (RoundFrame)contentItem.getContentSystem().createContentItem(RoundFrame.class);
		RoundWindow window = (RoundWindow)contentItem;	
		backgroundFrame.setRadius(window.getRadius());
		backgroundFrame.setBackGround(window.getBackGround());
		backgroundFrame.setBorder(window.getBorder());	
		backgroundFrame.setOrder(-99999999);
		window.addSubItem(backgroundFrame);
	}

	
	
	@Override
	public void setBackGround(Background backGround) {	
		backgroundFrame.setBackGround(backGround);
	}

	@Override
	public void setBorder(Border border) {
		backgroundFrame.setBorder(border);
	}
	
	public void lowerIndex(){
		backgroundFrame.setOrder(-999999999);
	}

	@Override
	public void setRadius(int radius) {
		backgroundFrame.setRadius(radius);
		
	}
	
	public RoundFrame getBackgroundFrame(){
		return backgroundFrame;
	}
	
}