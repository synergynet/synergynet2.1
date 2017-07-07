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

package synergyspace.jme.gfx.twod;

import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.scene.state.RenderState;
import com.jme.util.Timer;

public class FPSNode extends Node {

	private static final long serialVersionUID = 7521550035241588792L;
	protected StringBuffer updateBuffer = new StringBuffer( 30 );
	protected StringBuffer tempBuffer = new StringBuffer();
	protected Text fps;

	public FPSNode(String name) {
		super(name);
		fps = Text.createDefaultTextLabel( "FPS label" );
		fps.setCullHint(CullHint.Never);
		fps.setTextureCombineMode(TextureCombineMode.Replace);


		this.setRenderState(fps.getRenderState(RenderState.StateType.Blend));
		this.setRenderState(fps.getRenderState(RenderState.StateType.Texture));
		this.attachChild( fps );
		this.setCullHint(CullHint.Never);
		this.updateGeometricState( 0.0f, true );
		this.updateRenderState();		
	}

	public void update(Timer timer) {
		updateBuffer.setLength(0);
		updateBuffer.append("FPS: ").append((int)timer.getFrameRate()).append(" - ");
		//TODO: fix FPS display
//		updateBuffer.append(DisplaySystem.getDisplaySystem().getRenderer().getStatistics(tempBuffer));	
//		fps.print(updateBuffer);
		
	}
}
