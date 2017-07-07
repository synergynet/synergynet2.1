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

package synergynet.table.apps.contentitemgallery;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ControlBar;
import synergynet.contentsystem.items.DropDownList;
import synergynet.contentsystem.items.Frame;
import synergynet.contentsystem.items.Keyboard;
import synergynet.contentsystem.items.ListContainer;
import synergynet.contentsystem.items.MediaPlayer;
import synergynet.contentsystem.items.MultiLineTextLabel;
import synergynet.contentsystem.items.PDFViewer;
import synergynet.contentsystem.items.PPTViewer;
import synergynet.contentsystem.items.QuadContentItem;
import synergynet.contentsystem.items.RoundFrame;
import synergynet.contentsystem.items.RoundImageLabel;
import synergynet.contentsystem.items.RoundListContainer;
import synergynet.contentsystem.items.RoundTextLabel;
import synergynet.contentsystem.items.SimpleButton;
import synergynet.contentsystem.items.innernotecontroller.InnerNoteController;
import synergynet.contentsystem.items.listener.SimpleButtonAdapter;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.menucontrol.HoldBottomLeftExit;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergynet.table.apps.docviewer.DocViewerApp;
import synergyspace.jme.sysutils.CameraUtility;

public class ContentItemGalleryApp extends DefaultSynergyNetApp {
	
	private ContentSystem contentSystem;
	InnerNoteController innerNoteController;

	public ContentItemGalleryApp(ApplicationInfo info) {
		super(info);		
	}

	@Override
	public void addContent() {
		
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		setMenuController(new HoldBottomLeftExit());
		
		//InnerNoteController is used to flip the content item and allow user to add note on back side of the item
		innerNoteController = new InnerNoteController();
		
		addSimpleButton();
		addListContainer();
		addMediaPlayer();
		addPPTViewer();
		addPDFViewer();
		addRoundListContainer();
		addRoundImageLabel();
		addWindow();
		addFrame();
		addRoundFrame();
		addMultiLineTextLabel();
		addRoundTextLabel();
		addDropDownList();
		addControlBar();
		Keyboard keyBoard = (Keyboard) contentSystem.createContentItem(Keyboard.class);
		keyBoard.setLocalLocation(200, 200);
	}
	
	private void addControlBar(){
		ControlBar controlBar = (ControlBar)contentSystem.createContentItem(ControlBar.class);
		controlBar.setLocalLocation(507, 700);
		controlBar.setControlBarLength(400);
	}
	
	private void addWindow(){
		ListContainer window = (ListContainer)contentSystem.createContentItem(ListContainer.class);
		window.setBackgroundColour(Color.BLUE);
		window.setBorderColour(Color.white);
		window.setHeight(40);
		window.setWidth(60);
		window.placeRandom();
	}
	
	private void addMultiLineTextLabel(){
		MultiLineTextLabel mlt = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
		mlt.setAutoFitSize(true);
		mlt.placeRandom();
		mlt.setLines("Platform independent multi-touch software. This software relies on an externally provided vision system (touchlib works well). Written predominantly for a jMonkeyEngine environment, but also supporting other Java-based frameworks. ", 30);
		mlt.setFont(new Font("Arial", Font.PLAIN,15));
	}
	
	private void addDropDownList(){
		DropDownList dropDownList = (DropDownList)this.contentSystem.createContentItem(DropDownList.class);
		dropDownList.placeRandom();
		dropDownList.addListItem("Option 1", "Option 1");
		dropDownList.addListItem("Option 2", "Option 2");
	}
	
	private void addRoundFrame(){
		RoundFrame roundFrame = (RoundFrame) this.contentSystem.createContentItem(RoundFrame.class);
		roundFrame.placeRandom();
		roundFrame.setBackgroundColour(Color.yellow);
		roundFrame.setBorderColour(Color.white);
		roundFrame.setRadius(40);
	}
	
	private void addFrame(){
		Frame frame = (Frame)contentSystem.createContentItem(Frame.class);
		frame.placeRandom();
		frame.setBackgroundColour(Color.red);
		frame.setBorderColour(Color.white);
		frame.setBorderSize(5);
		frame.setHeight(40);
		frame.setWidth(60);
	}
	
	private void addPPTViewer(){
		PPTViewer ppt = (PPTViewer)contentSystem.createContentItem(PPTViewer.class);
		ppt.setPPTFile(DocViewerApp.class.getResource("synergynet-july09.ppt"));
		ppt.placeRandom();
		ppt.setScale(0.5f);
	}
	
	private void addPDFViewer(){
		PDFViewer pdf = (PDFViewer)this.contentSystem.createContentItem(PDFViewer.class);
		pdf.setPdfFile(new File("assets/resources/pdfs/test.pdf"));
		pdf.setWidth(350);
		pdf.placeRandom();
		pdf.setScale(0.5f);
		pdf.makeFlickable(2f);
	}
	
	private void addMediaPlayer(){
		MediaPlayer video = (MediaPlayer)contentSystem.createContentItem(MediaPlayer.class);
		video.setMediaURL(ContentItemGalleryApp.class.getResource("smallvid.mp4"));
		video.setPixelsPerUnit(1);
		video.placeRandom();
		video.setScale(0.5f);
		
		innerNoteController.addNoteController(video, (QuadContentItem) video.getPlayerFrame());
	}
	
	private void addSimpleButton(){
		final SimpleButton button1 = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		button1.setText("I'm a Button");
		button1.setBackgroundColour(Color.lightGray);
		button1.placeRandom();
		button1.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {			
				if (button1.getText().equals("I'm a Button")){
					button1.setText("Click me!");
				}
				else{
					button1.setText("I'm a Button");
				}
			}			
		});	
	}
	
	private void addListContainer(){
		ListContainer menu = (ListContainer)contentSystem.createContentItem(ListContainer.class);
		menu.setBackgroundColour(Color.BLUE);
		menu.setWidth(200);
		menu.setItemHeight(30);
		menu.placeRandom();
		
		final SimpleButton button1 = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		button1.setAutoFitSize(false);
		button1.setText("Start Game");
		button1.setBackgroundColour(Color.lightGray);
		button1.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {			
				if (button1.getText().equals("Start Game")){
					button1.setText("Stop Game");
				}
				else{
					button1.setText("Start Game");
				}
			}			
		});	
		
		final SimpleButton button2 = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		button2.setAutoFitSize(false);
		button2.setText("Start App");
		button2.setBackgroundColour(Color.lightGray);
		button2.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {			
				if (button2.getText().equals("Start App")){
					button2.setText("Stop App");
				}
				else{
					button2.setText("Start App");
				}
			}			
		});	
		
		menu.addSubItem(button1);
		menu.addSubItem(button2);
		
	}
	
	private void addRoundListContainer(){
		
		final RoundListContainer menu = (RoundListContainer)this.contentSystem.createContentItem(RoundListContainer.class);
		menu.placeRandom();
		
		
		final RoundTextLabel loadICTContent = (RoundTextLabel)this.contentSystem.createContentItem(RoundTextLabel.class);		
		loadICTContent.setAutoFitSize(false);
		loadICTContent.setRadius(40f);
		loadICTContent.setBackgroundColour(Color.black);
		loadICTContent.setBorderColour(Color.gray);
		loadICTContent.setTextColour(new Color(0.3f, 0.3f, 1f));
		loadICTContent.setLines("ICT", 40);
		loadICTContent.setFont(new Font("Arial", Font.PLAIN,20));
		
		final RoundTextLabel loadImageVersionContent = (RoundTextLabel)this.contentSystem.createContentItem(RoundTextLabel.class);
		loadImageVersionContent.setAutoFitSize(false);
		loadImageVersionContent.setRadius(40f);
		loadImageVersionContent.setBackgroundColour(Color.black);
		loadImageVersionContent.setBorderColour(Color.gray);
		loadImageVersionContent.setTextColour(Color.gray);
		loadImageVersionContent.setLines("Image", 40);
		loadImageVersionContent.setFont(new Font("Arial", Font.PLAIN,20));
		
		final RoundTextLabel resetButton = (RoundTextLabel)this.contentSystem.createContentItem(RoundTextLabel.class);
		resetButton.setAutoFitSize(false);
		resetButton.setRadius(40f);
		resetButton.setBackgroundColour(Color.black);
		resetButton.setBorderColour(Color.gray);
		resetButton.setTextColour(Color.gray);
		resetButton.setLines("Reset", 40);
		resetButton.setFont(new Font("Arial", Font.PLAIN,20));
		
		menu.addSubItem(loadICTContent);
		menu.addSubItem(loadImageVersionContent);
		menu.addSubItem(resetButton);
		menu.run();
			
	}

	private void addRoundImageLabel(){
		
		RoundImageLabel roundImageLabel1 = (RoundImageLabel)this.contentSystem.createContentItem(RoundImageLabel.class);
		roundImageLabel1.setImageInfo(ContentItemGalleryApp.class.getResource("images/telmug.jpg"));
		roundImageLabel1.setRadius(40);
		
		RoundImageLabel roundImageLabel2 = (RoundImageLabel)this.contentSystem.createContentItem(RoundImageLabel.class);
		roundImageLabel2.setImageInfo(ContentItemGalleryApp.class.getResource("images/puzzlepieces.jpg"));
		roundImageLabel2.setRadius(40);
		
		RoundImageLabel roundImageLabel3 = (RoundImageLabel)this.contentSystem.createContentItem(RoundImageLabel.class);
		roundImageLabel3.setImageInfo(ContentItemGalleryApp.class.getResource("images/butterfly.jpg"));	
		roundImageLabel3.setRadius(40);
		
		RoundListContainer roundListContainer = (RoundListContainer)this.contentSystem.createContentItem(RoundListContainer.class);
		roundListContainer.placeRandom();
		roundListContainer.addSubItem(roundImageLabel1);
		roundListContainer.addSubItem(roundImageLabel2);
		roundListContainer.addSubItem(roundImageLabel3);
		roundListContainer.run();
		
		roundListContainer.makeFlickable(3f);
	}
	
	private void addRoundTextLabel(){
		RoundTextLabel roundTextLabel = (RoundTextLabel)this.contentSystem.createContentItem(RoundTextLabel.class);
		roundTextLabel.placeRandom();
		roundTextLabel.setBackgroundColour(Color.black);
		roundTextLabel.setLines("Platform independent multi-touch software. This software relies on an externally provided vision system (touchlib works well). Written predominantly for a jMonkeyEngine environment, but also supporting other Java-based frameworks.", 30);
		roundTextLabel.setFont(new Font("Arial", Font.PLAIN,12));

	}
	
	protected Camera getCamera() {
		if(cam == null) {
			cam = CameraUtility.getCamera();
			cam.setLocation(new Vector3f(0f, 10f, 50f));
			cam.lookAt(new Vector3f(), new Vector3f( 0, 0, -1 ));
			cam.update();
		}		
		return cam;
	}

	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(contentSystem != null) contentSystem.update(tpf);
		
		//if(rtl2.hasCollision(list))
		//	System.out.println("Collision");
	}

}
