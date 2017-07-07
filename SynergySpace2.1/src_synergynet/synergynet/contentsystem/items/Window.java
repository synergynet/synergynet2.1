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

package synergynet.contentsystem.items;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.implementation.interfaces.IWindowImplementation;

public class Window extends OrthoContainer {

	private static final long serialVersionUID = -278216111222663419L;
	
	protected int height = 300;
	protected int width = 400;
		
	public Window(ContentSystem contentSystem, String name) {
		super(contentSystem, name);	
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		((IWindowImplementation)this.contentItemImplementation).setHeight(height);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		((IWindowImplementation)this.contentItemImplementation).setWidth(width);
	}
	
	public Frame getBackgroundFrame(){
		return ((IWindowImplementation)this.contentItemImplementation).getBackgroundFrame();
	}

	@Override
	public void makeFlickable(float deceleration) {
		super.makeFlickable(deceleration);
		
		this.getBackgroundFrame().makeFlickable(deceleration);
	}
}