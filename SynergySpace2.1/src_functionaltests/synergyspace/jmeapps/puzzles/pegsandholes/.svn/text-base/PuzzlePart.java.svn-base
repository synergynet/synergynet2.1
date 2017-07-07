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

package synergyspace.jmeapps.puzzles.pegsandholes;

import synergyspace.jme.gfx.twod.Grid;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.BufferUtils;


public class PuzzlePart extends Grid{

	private static final long serialVersionUID = 1L;

	protected float[][] coords;
	float worldSizeWidth;
	float worldSizeHeight;
	float tolerance = 5f;

	protected TextureState textureState;


	public PuzzlePart(String name, float[][] coords, float width, float height, ColorRGBA colour) {
		super(name, width, height, 3, 3, true);
		this.coords = coords;
		updateGeometry();
		setColour(colour);
	}

	public void setColour(ColorRGBA colour) {
		DisplaySystem display = DisplaySystem.getDisplaySystem();

	    this.setRenderQueueMode(Renderer.QUEUE_ORTHO);

	    BlendState as = display.getRenderer().createBlendState();
		as.setBlendEnabled( false );
		this.setRenderState(as);

		this.setSolidColor(colour);
		this.setLightCombineMode(LightCombineMode.Off);
	    this.updateRenderState();
	}

	private void updateGeometry() {
		int count = 0;
		int miniCount = 0;
		for (int i = 0; i < coords.length; i++){
			movePoint(miniCount, count, coords[i][0],coords[i][1]);
			miniCount++;
			if (i%4==0){
				count++;
				miniCount = 0;
			}
		}
	}

	public float[][] getCoords(){
		return coords;
	}

	public boolean allOutside(PuzzlePart puzzlePart) {
		if (puzzlePart.hasCollision(this, true)){
			return false;
		}
		return true;
	}

	public float getWidth() {
		Vector3f[] theseCoords = BufferUtils.getVector3Array(this.getVertexBuffer());

		float xMin = 0;
		float xMax = 0;
		if (theseCoords.length > 0){
			xMin = theseCoords[0].x;
			xMax = theseCoords[0].x;
			for (int i = 0; i < theseCoords.length; i++){
				if (xMin > theseCoords[i].x){
					xMin = theseCoords[i].x;
				}
				if (xMax < theseCoords[i].x){
					xMax = theseCoords[i].x;
				}
			}
		}
		return xMax - xMin;
	}

	public float getHeight() {
		Vector3f[] theseCoords = BufferUtils.getVector3Array(this.getVertexBuffer());

		float yMin = 0;
		float yMax = 0;
		if (theseCoords.length > 0){
			yMin = theseCoords[0].y;
			yMax = theseCoords[0].y;
			for (int i = 0; i < theseCoords.length; i++){
				if (yMin > theseCoords[i].y){
					yMin = theseCoords[i].y;
				}
				if (yMax < theseCoords[i].y){
					yMax = theseCoords[i].y;
				}
			}
		}
		return yMax - yMin;
	}


}
