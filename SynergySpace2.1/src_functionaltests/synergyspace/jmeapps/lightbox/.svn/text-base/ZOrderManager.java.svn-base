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

package synergyspace.jmeapps.lightbox;

import java.util.Stack;

import com.jme.scene.Spatial;

public class ZOrderManager {
	private float topZ;

	private Stack<Spatial> stack = new Stack<Spatial>();

	public ZOrderManager(float topZ) {
		this.topZ = topZ;
	}

	public void register(Spatial s) {
		stack.push(s);
		s.getLocalTranslation().setZ(-40f);
		s.updateWorldVectors();
	}

	public void makeTop(Spatial s) {
		stack.remove(s);
		stack.push(s);
		s.getLocalTranslation().setZ(-40f);
		s.setZOrder(-40);
		s.updateWorldVectors();
		float z = topZ - 0.1f;
		for(Spatial t : stack) {
			if(t != s) {
				t.getLocalTranslation().setZ(z);
				t.updateWorldVectors();
				t.setZOrder(-100);
				z -= 0.5f;
			}
		}
		for(Spatial t : stack) {
			System.out.println(t.getName() + " z: " + t.getLocalTranslation().z);
		}
		s.getParent().updateGeometricState(0f, true);
	}
}
