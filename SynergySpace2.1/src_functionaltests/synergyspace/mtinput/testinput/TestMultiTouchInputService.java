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

package synergyspace.mtinput.testinput;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JFrame;

import synergyspace.mtinput.Blob;
import synergyspace.mtinput.IMultiTouchEventListener;
import synergyspace.mtinput.IMultiTouchInputSource;
import synergyspace.mtinput.events.MultiTouchCursorEvent;
import synergyspace.mtinput.events.MultiTouchObjectEvent;
import synergyspace.mtinput.luminja.LuminMultiTouchInput;

public class TestMultiTouchInputService extends JFrame implements IMultiTouchEventListener {

	private static final long serialVersionUID = -904616250396448907L;	
	private static Logger log = Logger.getLogger(TestMultiTouchInputService.class.getName());
	private Map<Long,Blob> blobs = new HashMap<Long,Blob>();

	public static void main(String[] args) {
		TestMultiTouchInputService app = new TestMultiTouchInputService();
		app.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent key) {
				System.exit(0);
			}
		});
		app.setSize(Toolkit.getDefaultToolkit().getScreenSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height/2);
		app.setUndecorated(true);
		app.setVisible(true);		
	}

	public TestMultiTouchInputService() {
//		IMultiTouchInputService mtInput = new TUIOMultiTouchInput();
		IMultiTouchInputSource mtInput = new LuminMultiTouchInput();		
		mtInput.registerMultiTouchEventListener(this);
		mtInput.setClickSensitivity(500, 0.001f);
	}	

	public void paint(final Graphics g) {
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.blue);
		synchronized(blobs) {
			for(Blob b : blobs.values()) {
				g.setColor(Color.blue);
				g.fillOval(b.x, this.getHeight() - b.y, 10 + (int)b.pressure, 10 + (int)b.pressure);
				g.setColor(Color.white);
				g.drawString(b.id + " (" + b.x + "," + b.y + ")", b. x + 20, this.getHeight() - b.y + 20);
			}
		}
	}

	public void cursorPressed(MultiTouchCursorEvent event) {
		Blob b = new Blob(event.getCursorID(), (int)event.getPosition().x, (int)event.getPosition().y);
		synchronized(blobs) {
			blobs.put(b.id, b);
		}
		log.info(event.toString());
		repaint();
	}

	public void cursorChanged(MultiTouchCursorEvent event) {
		Blob b = blobs.get(event.getCursorID());
		b.x = (int) (event.getPosition().x * this.getWidth());
		b.y = (int) (event.getPosition().y * this.getHeight());		
		log.info(event.toString());
		repaint();
	}

	public void cursorClicked(MultiTouchCursorEvent event) {
		log.info(event.toString());
		repaint();
	}

	public void cursorReleased(MultiTouchCursorEvent event) {
		synchronized(blobs) {
			blobs.remove(event.getCursorID());
		}
		log.info(event.toString());
		repaint();
	}

	public void objectAdded(MultiTouchObjectEvent event) {
		log.info(event.toString());		
	}

	public void objectChanged(MultiTouchObjectEvent event) {
		log.info(event.toString());		
	}

	public void objectRemoved(MultiTouchObjectEvent event) {
		log.info(event.toString());		
	}
}
