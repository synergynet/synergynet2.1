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

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import synergyspace.jme.gfx.twod.keyboard.Key;

public class KeyboardBuilder {
	
	
	
	public static void main(String[] args) {
		int width = 44;
		int height = 44;
		Point nums = new Point(59, 109);
		int[] numsRowOne = {KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8, KeyEvent.VK_9, KeyEvent.VK_0, KeyEvent.VK_MINUS, KeyEvent.VK_PLUS};
		Point rowOne = new Point(81,154);
		int[] keysRowOne = {KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_T, KeyEvent.VK_Y, KeyEvent.VK_U, KeyEvent.VK_I, KeyEvent.VK_O, KeyEvent.VK_P};
		Point rowTwo = new Point(94,199);
		int[] keysRowTwo = {KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_F, KeyEvent.VK_G, KeyEvent.VK_H, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L, KeyEvent.VK_SEMICOLON, KeyEvent.VK_QUOTE};
		Point rowThree = new Point(110, 243);
		int[] keysRowThree = {KeyEvent.VK_Z, KeyEvent.VK_X, KeyEvent.VK_C, KeyEvent.VK_V, KeyEvent.VK_B, KeyEvent.VK_N, KeyEvent.VK_M, KeyEvent.VK_COMMA, KeyEvent.VK_STOP, KeyEvent.VK_SLASH};
		
		
		
		int x = nums.x;
		int y = nums.y;
		
		List<Key> keys = new ArrayList<Key>();
		
		for(int i = 0; i < numsRowOne.length; i++) {
			Rectangle r = new Rectangle(x, y, width, height);
			Key key = new Key(r, numsRowOne[i]);
			keys.add(key);
			x += width;
		}
		
		x = rowOne.x;
		y = rowOne.y;
		
		for(int i = 0; i < keysRowOne.length; i++) {
			Rectangle r = new Rectangle(x, y, width, height);
			Key key = new Key(r, keysRowOne[i]);
			keys.add(key);
			x += width;
		}
		
		x = rowTwo.x;
		y = rowTwo.y;
		
		for(int i = 0; i < keysRowTwo.length; i++) {
			Rectangle r = new Rectangle(x, y, width, height);
			Key key = new Key(r, keysRowTwo[i]);
			keys.add(key);
			x += width;
		}
		
		x = rowThree.x;
		y = rowThree.y;
		
		for(int i = 0; i < keysRowThree.length; i++) {
			Rectangle r = new Rectangle(x, y, width, height);
			Key key = new Key(r, keysRowThree[i]);
			keys.add(key);
			x += width;
		}		
		
		Rectangle r = new Rectangle(197,286,280,44);
		Key key = new Key(r, KeyEvent.VK_SPACE);
		keys.add(key);
		
		r = new Rectangle(15,198,80,44);
		key = new Key(r, KeyEvent.VK_CAPS_LOCK);
		keys.add(key);
		
		r = new Rectangle(16,242,51,44);
		key = new Key(r, KeyEvent.VK_SHIFT);
		keys.add(key);
		
		r =  new Rectangle(589,111,85,44);
		key = new Key(r, KeyEvent.VK_BACK_SPACE);
		keys.add(key);
		
		r = new Rectangle(624,153,49,89);
		key = new Key(r, KeyEvent.VK_ENTER);
		keys.add(key);
		
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("keyboard.def")));
			oos.writeObject(keys);
			oos.close();
			System.out.println("file written.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
