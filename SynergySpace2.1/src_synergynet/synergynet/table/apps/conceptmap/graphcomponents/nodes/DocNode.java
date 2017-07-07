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

package synergynet.table.apps.conceptmap.graphcomponents.nodes;

import com.jme.system.DisplaySystem;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.QuadContentItem;
import synergynet.contentsystem.items.listener.ScreenCursorListener;
import synergynet.table.apps.conceptmap.utility.GraphManager;

public abstract class DocNode extends QuadNode{

	public DocNode(ContentSystem contentSystem, GraphManager gManager) {
		super(contentSystem, gManager);
	}

	protected void setNodeContent(ContentItem nodeItem){
		this.contentItem = (QuadContentItem)nodeItem;
		super.setNodeContent(contentItem);
		if(contentItem != null){
			contentItem.addScreenCursorListener(new ScreenCursorListener(){

				@Override
				public void screenCursorChanged(ContentItem item, long id,
						float x, float y, float pressure) {

				}

				@Override
				public void screenCursorClicked(ContentItem item, long id,
						float x, float y, float pressure) {
					if(contentItem.getLocalLocation().x !=0 || contentItem.getLocalLocation().y != 0){
						DocNode.this.updateNode();
						contentItem.setLocalLocation(0,0,0);
						DocNode.this.setLocation(DisplaySystem.getDisplaySystem().getWidth()/2, DisplaySystem.getDisplaySystem().getHeight()/2);
					}
				}

				@Override
				public void screenCursorPressed(ContentItem item, long id,
						float x, float y, float pressure) {

				}

				@Override
				public void screenCursorReleased(ContentItem item, long id,
						float x, float y, float pressure) {
					if(contentItem.getLocalLocation().x !=0 || contentItem.getLocalLocation().y != 0){
						DocNode.this.updateNode();
						contentItem.setLocalLocation(0,0,0);
						DocNode.this.setLocation(DisplaySystem.getDisplaySystem().getWidth()/2, DisplaySystem.getDisplaySystem().getHeight()/2);
					}
				}
			});
		}		
	}
}
