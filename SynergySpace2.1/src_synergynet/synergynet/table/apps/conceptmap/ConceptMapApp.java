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

package synergynet.table.apps.conceptmap;

//import java.io.File;

import synergynet.contentsystem.ContentSystem;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.menucontrol.HoldBottomLeftExit;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergynet.table.apps.conceptmap.graphcomponents.nodes.GraphNode;
import synergynet.table.apps.conceptmap.graphcomponents.nodes.ImageTextNode;
//import synergynet.table.apps.conceptmap.graphcomponents.nodes.PDFNode;
//import synergynet.table.apps.conceptmap.graphcomponents.nodes.PPTNode;
//import synergynet.table.apps.conceptmap.graphcomponents.nodes.TextNode;
import synergynet.table.apps.conceptmap.utility.GraphManager;
//import synergynet.table.apps.docviewer.DocViewerApp;

public class ConceptMapApp extends DefaultSynergyNetApp{

	protected ContentSystem content;
	protected GraphManager gmanager;
	
	private static ConceptMapApp instance;
	
	public ConceptMapApp(ApplicationInfo info) {
		super(info);
	}


	@Override
	public void addContent() {
		setDefaultSteadfastLimit(1);
		
		instance = this;
		content = ContentSystem.getContentSystemForSynergyNetApp(this);
		setMenuController(new HoldBottomLeftExit());

		gmanager = new GraphManager(content);
		setMenuController(new HoldBottomLeftExit());
		
		ImageTextNode trees = new ImageTextNode(content, gmanager);
		trees.setImageResource(ConceptMapApp.class.getResource("images/sampleconceptmap/tree.jpg"));
		trees.setText("Trees");
		trees.getImageTextLabel().setBorderSize(20);
		trees.getImageTextLabel().setBorderColour(GraphConfig.nodeBorderColor);
		trees.getImageTextLabel().setBackgroundColour(GraphConfig.nodeBackgroundColor);
		trees.getImageTextLabel().setFont(GraphConfig.nodeTextFont);
		trees.getImageTextLabel().setTextColour(GraphConfig.nodeForegroundColor);
		trees.setLinkButtonLocation(GraphNode.TOP_RIGHT_CORNER);
		trees.setEditPointLocation(GraphNode.TOP_LEFT_CORNER);
		trees.setCloseButtonLocation(GraphNode.TOP_LEFT_CORNER);
		//trees.setScale(0.8f);
		trees.setLocation(100,100);
		
		ImageTextNode wood = new ImageTextNode(content, gmanager);
		wood.setImageResource(ConceptMapApp.class.getResource("images/sampleconceptmap/wood.jpg"));
		wood.setText("Wood");
		wood.getImageTextLabel().setBorderSize(20);
		wood.getImageTextLabel().setBorderColour(GraphConfig.nodeBorderColor);
		wood.getImageTextLabel().setBackgroundColour(GraphConfig.nodeBackgroundColor);
		wood.getImageTextLabel().setFont(GraphConfig.nodeTextFont);
		wood.getImageTextLabel().setTextColour(GraphConfig.nodeForegroundColor);
		wood.setLinkButtonLocation(GraphNode.TOP_RIGHT_CORNER);
		wood.setEditPointLocation(GraphNode.TOP_LEFT_CORNER);
		wood.setCloseButtonLocation(GraphNode.TOP_LEFT_CORNER);
		//wood.setScale(0.8f);
		wood.setLocation(100,100);
		
		ImageTextNode house = new ImageTextNode(content, gmanager);
		house.setImageResource(ConceptMapApp.class.getResource("images/sampleconceptmap/house.gif"));
		house.setText("House");
		house.getImageTextLabel().setBorderSize(20);
		house.getImageTextLabel().setBorderColour(GraphConfig.nodeBorderColor);
		house.getImageTextLabel().setBackgroundColour(GraphConfig.nodeBackgroundColor);
		house.getImageTextLabel().setFont(GraphConfig.nodeTextFont);
		house.getImageTextLabel().setTextColour(GraphConfig.nodeForegroundColor);
		house.setLinkButtonLocation(GraphNode.TOP_RIGHT_CORNER);
		house.setEditPointLocation(GraphNode.TOP_LEFT_CORNER);
		house.setCloseButtonLocation(GraphNode.TOP_LEFT_CORNER);
		//house.setScale(0.8f);
		house.setLocation(100,100);
		
		ImageTextNode oxygen = new ImageTextNode(content, gmanager);
		oxygen.setImageResource(ConceptMapApp.class.getResource("images/sampleconceptmap/oxygen.jpg"));
		oxygen.setText("Oxygen");
		oxygen.getImageTextLabel().setBorderSize(20);
		oxygen.getImageTextLabel().setBorderColour(GraphConfig.nodeBorderColor);
		oxygen.getImageTextLabel().setBackgroundColour(GraphConfig.nodeBackgroundColor);
		oxygen.getImageTextLabel().setFont(GraphConfig.nodeTextFont);
		oxygen.getImageTextLabel().setTextColour(GraphConfig.nodeForegroundColor);
		oxygen.setLinkButtonLocation(GraphNode.TOP_RIGHT_CORNER);
		oxygen.setEditPointLocation(GraphNode.TOP_LEFT_CORNER);
		oxygen.setCloseButtonLocation(GraphNode.TOP_LEFT_CORNER);
		//oxygen.setScale(0.8f);
		oxygen.setLocation(100,100);
		
		ImageTextNode animals = new ImageTextNode(content, gmanager);
		animals.setImageResource(ConceptMapApp.class.getResource("images/sampleconceptmap/animals.jpg"));
		animals.setText("Animals");
		animals.getImageTextLabel().setBorderSize(20);
		animals.getImageTextLabel().setBorderColour(GraphConfig.nodeBorderColor);
		animals.getImageTextLabel().setBackgroundColour(GraphConfig.nodeBackgroundColor);
		animals.getImageTextLabel().setFont(GraphConfig.nodeTextFont);
		animals.getImageTextLabel().setTextColour(GraphConfig.nodeForegroundColor);
		animals.setLinkButtonLocation(GraphNode.TOP_RIGHT_CORNER);
		animals.setEditPointLocation(GraphNode.TOP_LEFT_CORNER);
		animals.setCloseButtonLocation(GraphNode.TOP_LEFT_CORNER);
		//animals.setScale(0.8f);
		animals.setLocation(100,100);

	}
	
	public ContentSystem getContentSystem(){
		return content;
	}
	
	public GraphManager getGraphManager(){
		return gmanager;
	}
	
	public static ConceptMapApp getInstance(){
		return instance;
	}
	
	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(content != null) content.update(tpf);
	}

}
