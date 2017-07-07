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

package synergynet.table.apps.mathpadapp.mathtool;

import java.awt.Color;

import synergynet.contentsystem.items.MathPad;
import synergynet.table.apps.mathpadapp.mathtool.MathTool.WritingState;
import synergynet.table.apps.mathpadapp.mathtool.MathToolControlPanel.ControlPanelListener;

public class ControlPanelListenerImpl implements ControlPanelListener{
		
		private MathTool mathTool;
		
		public ControlPanelListenerImpl(MathTool mathTool){
			this.mathTool = mathTool;
		}
	
		@Override
		public void padCleared() {
			for(MathPad mathPad: mathTool.getAllPads())	
				mathPad.clearAll();
		}

		@Override
		public void writingStateChanged(WritingState writingState) {
			mathTool.setWritingState(writingState);
		}

		@Override
		public void textColorChanged(Color textColor) {
			mathTool.backTextColor = textColor;
			if(mathTool.currentWritingState == WritingState.FREE_DRAW){
				for(MathPad mathPad: mathTool.getAllPads())	
					mathPad.setTextColor(textColor);
			}
		}

		@Override
		public void lineWidthChanged(float lineWidth) {
			for(MathPad mathPad: mathTool.getAllPads())	
				mathPad.setLineWidth(lineWidth);			}

		@Override
		public void padCreated(MathPad newPad) {}

		@Override
		public void padChanged(int nextPadIndex) {}

		@Override
		public void answerPadDisplayed() {
			System.out.println("Displayed................");
			mathTool.answerPad.setVisible(true);

		}

		@Override
		public void padRemoved(final int padIndex) {
			mathTool.removePad(padIndex);
		}
}
