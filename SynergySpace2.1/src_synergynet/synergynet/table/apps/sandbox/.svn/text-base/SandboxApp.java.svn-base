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

package synergynet.table.apps.sandbox;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.ImageTextLabel;
import synergynet.contentsystem.items.Keyboard;
import synergynet.contentsystem.items.ObjShape;
import synergynet.contentsystem.items.RoundImageLabel;
import synergynet.contentsystem.items.TextLabel;
import synergynet.contentsystem.items.TextLabel.Alignment;
import synergynet.contentsystem.items.innernotecontroller.InnerNoteController;
import synergynet.contentsystem.items.listener.ItemEventAdapter;
import synergynet.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleAdapter;
import synergynet.table.SynergyNetAppUtils;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.menucontrol.HoldBottomLeftExit;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergynet.table.apps.conceptmap.GraphConfig;
import synergyspace.jme.gfx.twod.keyboard.Key;
import synergyspace.jme.gfx.twod.keyboard.MTKeyListener;

public class SandboxApp extends DefaultSynergyNetApp {
	
	public SandboxApp(ApplicationInfo info) {
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
		
    	addKeyboard();
		
		addPics();
		
		setBorderAdaptationScaleMin(0.1f);
	}

	@SuppressWarnings("unchecked")
	private List<Key> getKeyDefs() {
		try {
			ObjectInputStream ois = new ObjectInputStream(InnerNoteController.class.getResourceAsStream("keyboard.def"));
			return (List<Key>) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean buttonOn = false;
	private TextLabel answerText;
	private ImageTextLabel button;
	private Keyboard keyboard;
	
	private void addKeyboard(){
		
		button = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
		button.setAsTopObject();
		button.setBringToTopable(false);
		
		answerText = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		
		answerText.setAsTopObject();
		answerText.setBringToTopable(false);
		answerText.setText(" ");
		answerText.setAlignment(Alignment.LEFT);
		answerText.setFont(new Font("Arial", Font.PLAIN,30));
		answerText.setBackgroundColour(Color.black);
		answerText.setTextColour(Color.white);
		answerText.setBorderColour(Color.gray);
		answerText.setBorderSize(2);		
		answerText.setRotateTranslateScalable(false);	

		keyboard = (Keyboard)contentSystem.createContentItem(Keyboard.class);
		keyboard.setScale(0.5f);
		keyboard.setKeyboardImageResource(GraphConfig.nodeKeyboardImageResource);
		keyboard.setKeyDefinitions(this.getKeyDefs());
		keyboard.centerItem();
		keyboard.addKeyListener(new MTKeyListener(){

			boolean caps = false;
			
			@Override
			public void keyReleasedEvent(KeyEvent evt) {
				
				if (buttonOn){
					
					if(evt.getKeyChar() == KeyEvent.VK_CAPS_LOCK){
						caps = !caps;
						if (caps){
							button.setBorderColour(Color.white);
							answerText.setBorderColour(Color.white);
						}else{
							button.setBorderColour(Color.gray);
							answerText.setBorderColour(Color.gray);
						}
					}	
					
					String text = answerText.getText();
					if(text == null) text ="";
					if(evt.getKeyChar() == KeyEvent.VK_BACK_SPACE){
						if(text.length() >0 && !text.equals(" ")){
							text = text.substring(0,text.length()-1);
						}
					}else if(evt.getKeyCode() == 521){
						text = text + "+";
					}else if(evt.getKeyChar() == KeyEvent.VK_STOP){
						text = text + ".";
					}else if(evt.getKeyChar() != KeyEvent.VK_CAPS_LOCK){
						text = text+evt.getKeyChar();
					}	
					
					if(evt.getKeyChar() == KeyEvent.VK_SHIFT){

					}else{
						answerText.setText(text);
						
					}					
				}
			}

			@Override
			public void keyPressedEvent(KeyEvent evt) {

			}});

		keyboard.addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleAdapter(){

		});
		
		keyboard.makeFlickable(3f);
		keyboard.makeSpinnable(0.5f);
		keyboard.makeScaleMotionable(3f);
		keyboard.setScaleLimit(0.5f, 3f);
		keyboard.makeSticky();
		keyboard.setAsTopObject();
		
		button.setSteadfastLimit(1);;
		button.setAutoFit(false);
		button.setBorderColour(Color.gray);
		button.setBorderSize(2);		
		button.setBackgroundColour(Color.green);
		button.setRotateTranslateScalable(false);
		button.setWidth(answerText.getHeight());
		button.setHeight(answerText.getHeight());
		button.addItemListener(new ItemEventAdapter() {			
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
				buttonOn = !buttonOn;
				if(buttonOn){
					button.setBackgroundColour(Color.red);
					keyboard.makeUnflickable();
					keyboard.makeUnspinnable();
					keyboard.makeUnScaleMotionable();
					keyboard.setRotateTranslateScalable(false, true);
				}else{
					button.setBackgroundColour(Color.green);
					keyboard.setRotateTranslateScalable(true, true);
					keyboard.makeFlickable(3f);
					keyboard.makeSpinnable(0.5f);
					keyboard.makeScaleMotionable(3f);
					keyboard.setScaleLimit(0.5f, 3f);
					keyboard.makeSticky();
				}
			}
		});
		button.makeBounceOffable(orthoNode);
		
		answerText.setSteadfastLimit(1);;
		answerText.makeBounceOffable(orthoNode);
		answerText.setAutoFit(false);			
		answerText.setHeight(button.getHeight());
		answerText.setWidth(DisplaySystem.getDisplaySystem().getRenderer().getWidth()-button.getWidth());
		answerText.setLocation(answerText.getWidth()/2, answerText.getHeight()/2);
		button.setLocation(answerText.getWidth() + button.getWidth()/2, button.getHeight()/2);
		
		addShape();
		
	}
	
	private void addShape(){
		ObjShape shape = (ObjShape)this.contentSystem.createContentItem(ObjShape.class);
		shape.setShapeGeometry(SandboxApp.class.getResource("octagonshape.obj"));		
		shape.setSolidColour(ColorRGBA.blue);
		shape.makeFlickable(3f);
		shape.makeSpinnable(2f);
		shape.makeScaleMotionable(4f);
		shape.makeBounceOffable(orthoNode);
		shape.centerItem();
			
	}
	
	private void addPics() {
		ImageTextLabel imageTTextLabel1 = (ImageTextLabel)this.contentSystem.createContentItem(ImageTextLabel.class);
		imageTTextLabel1.setImageInfo(SandboxApp.class.getResource("app_sandbox.png"));
		imageTTextLabel1.placeRandom();
		imageTTextLabel1.makeFlickable(3f);
		imageTTextLabel1.makeSpinnable(2f);
		imageTTextLabel1.makeScaleMotionable(4f);
		imageTTextLabel1.setScaleLimit(0.5f, 2f);	
		imageTTextLabel1.makeBounceOffable(orthoNode);
		imageTTextLabel1.setAsTopObject();
		
		ImageTextLabel imageTTextLabel2 = (ImageTextLabel)this.contentSystem.createContentItem(ImageTextLabel.class);
		imageTTextLabel2.setImageInfo(SandboxApp.class.getResource("app_sandbox.png"));
		imageTTextLabel2.placeRandom();
		imageTTextLabel2.makeFlickable(3f);
		imageTTextLabel2.makeSpinnable(2f);
		imageTTextLabel2.makeScaleMotionable(4f);
		imageTTextLabel2.setScaleLimit(0.5f, 2f);	
		imageTTextLabel2.makeBounceOffable(orthoNode);
		imageTTextLabel2.setAsTopObject();

		RoundImageLabel roundImageLabel1 = (RoundImageLabel)this.contentSystem.createContentItem(RoundImageLabel.class);
		roundImageLabel1.setImageInfo(SandboxApp.class.getResource("app_sandbox.png"));
		roundImageLabel1.setRadius(40);
		roundImageLabel1.placeRandom();
		roundImageLabel1.makeFlickable(3f);
		roundImageLabel1.makeSpinnable(0.2f);
		roundImageLabel1.setScale(2f);
		roundImageLabel1.setScaleLimit(2f, 2f);
		roundImageLabel1.makeBounceOffable(orthoNode);
		roundImageLabel1.setAsTopObject();
		
		RoundImageLabel roundImageLabel2 = (RoundImageLabel)this.contentSystem.createContentItem(RoundImageLabel.class);
		roundImageLabel2.setImageInfo(SandboxApp.class.getResource("app_sandbox.png"));
		roundImageLabel2.setRadius(40);
		roundImageLabel2.placeRandom();
		roundImageLabel2.makeFlickable(3f);
		roundImageLabel2.makeSpinnable(0.2f);
		roundImageLabel2.setScale(2f);
		roundImageLabel2.setScaleLimit(2f, 2f);
		roundImageLabel2.makeBounceOffable(orthoNode);
		roundImageLabel2.setAsTopObject();
		
		keyboard.setAsBottomObject();
		keyboard.setBringToTopable(false);
		
	}

	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(contentSystem != null) contentSystem.update(tpf);
	}

}
