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

import java.net.URL;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ImageTextLabel;
import synergynet.table.apps.conceptmap.utility.GraphManager;

public class ImageTextNode extends EditableQuadNode{

	protected ImageTextLabel imageTextLabel;
	
	public ImageTextNode(ContentSystem contentSystem, GraphManager gManager) {
		super(contentSystem, gManager);
		imageTextLabel = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
		this.setNodeContent(imageTextLabel);
	}

	public void setText(String text){
		imageTextLabel.setCRLFSeparatedString(text);
		updateNode();
	}
	
	public String getText(){
		String text = "";
		for(String line: imageTextLabel.getLines())
			text+=line+"\n";
		if(text != null && !text.equals(""))
			text = text.substring(0, text.lastIndexOf("\n"));
		return text;
	}
	
	public void setImageResource(URL imageResource){
		imageTextLabel.setImageInfo(imageResource);
	}
	
	public ImageTextLabel getImageTextLabel(){
		return imageTextLabel;
	}
}

