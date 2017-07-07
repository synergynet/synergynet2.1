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

package synergynet.table.animationsystem.animelements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import synergynet.table.animationsystem.AnimationElement;

public class ParallelAnimation extends AnimationElement {
	protected List<AnimationElement> elements = new ArrayList<AnimationElement>();
	boolean finished = false;
	
	public ParallelAnimation(AnimationElement... els) {
		for(AnimationElement e : els) {
			elements.add(e);
		}
	}
	
	public void addAnimationElement(Collection<AnimationElement> c) {
		elements.addAll(c);
	}
	
	public void addAnimationElements(AnimationElement... es) {
		for(AnimationElement e : es) {
			elements.add(e);
		}
	}
	
	public void addAnimationElement(AnimationElement e) {
		elements.add(e);
	}
	
	@Override
	public void elementStart(float tpf) {
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void updateAnimationState(float tpf) {
		boolean finishTest = true;
		for(AnimationElement e : elements) {
			e.updateAnimationState(tpf);
			finishTest = finishTest && e.isFinished();
		}
		finished = finishTest;
	}

}
