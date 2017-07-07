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

package synergyspace.jmeapps.keyboard;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import javax.swing.ImageIcon;

import com.jme.bounding.BoundingBox;

import synergyspace.jme.abstractapps.AbstractMultiTouchThreeDApp;
import synergyspace.jme.cursorsystem.elements.threed.ControlPointRotateTranslateScale;
import synergyspace.jme.cursorsystem.elements.threed.MultiTouchTransformableDrawableElement;
import synergyspace.jme.gfx.twod.keyboard.Key;
import synergyspace.jme.gfx.twod.keyboard.MTKeyListener;
import synergyspace.jme.gfx.twod.keyboard.MTKeyboard;
import synergyspace.jme.gfx.twod.textbox.ChangeableTextLabel;

public class KeyboardDemo extends AbstractMultiTouchThreeDApp {

	public static void main(String[] args) {
		KeyboardDemo app = new KeyboardDemo();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	@Override
	protected void setupContent() {
		final ChangeableTextLabel textLabel = new ChangeableTextLabel("Fred", 4.1f, 2.2f);
		textLabel.setModelBound(new BoundingBox());
		textLabel.setText("");
		textLabel.setLocalTranslation(3f, -4f, 10f);
		new ControlPointRotateTranslateScale(textLabel);
		this.rootNode.attachChild(textLabel);
		
		
		ImageIcon ii;
		ii = new ImageIcon(KeyboardDemo.class.getResource("keyboard.png"));
		Image img = ii.getImage();
		int imgWidth = ii.getIconWidth();
		int imgHeight = ii.getIconHeight();
		System.out.println(imgHeight);
		float pixelsPerUnit = 100;
		List<Key> keys = getKeyDefs();
		MTKeyboard kb = new MTKeyboard("keyboard", img, keys, (int)(imgWidth/pixelsPerUnit), (int)(imgHeight/pixelsPerUnit), imgWidth, imgHeight);
		kb.addKeyListener(new MTKeyListener() {
			public void keyPressedEvent(KeyEvent evt) {}

			public void keyReleasedEvent(KeyEvent evt) {
				System.out.println(evt);
				textLabel.appendText(" " + evt.getKeyChar());
			}
			
		});
		kb.setModelBound(new BoundingBox());
		kb.setLocalTranslation(-3f, 2f, 10f);
		new MultiTouchTransformableDrawableElement(kb, 200f, new Rectangle(12, 108, 1002, 228));
		rootNode.attachChild(kb);
	}

	@SuppressWarnings("unchecked")
	private List<Key> getKeyDefs() {
		try {
			ObjectInputStream ois = new ObjectInputStream(KeyboardDemo.class.getResourceAsStream("keyboard.def"));
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

	@Override
	protected void setupLighting() {

	}

	@Override
	protected void setupSystem() {

	}

}
