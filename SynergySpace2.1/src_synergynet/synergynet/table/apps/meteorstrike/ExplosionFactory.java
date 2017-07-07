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

package synergynet.table.apps.meteorstrike;

import com.jme.bounding.BoundingSphere;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Controller;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleMesh;

public class ExplosionFactory {
	
	public static ParticleMesh createRingBlast() {
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture(
				TextureManager.loadTexture(
						ExplosionFactory.class.getResource(
								"flaresmall.jpg"),
								Texture.MinificationFilter.Trilinear,
								Texture.MagnificationFilter.Bilinear));
		ts.setEnabled(true);
		
	    BlendState as1 = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
	    as1.setBlendEnabled(true);
	    as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
	    as1.setDestinationFunction(BlendState.DestinationFunction.One);
	    as1.setTestEnabled(true);
	    as1.setTestFunction(BlendState.TestFunction.GreaterThan);
	    as1.setEnabled(true);
		
        ParticleMesh particleGeom = ParticleFactory.buildParticles("", 300);        

        particleGeom.warmUp(120);        
        particleGeom.clearInfluences();        
        particleGeom.setEmissionDirection(new Vector3f(0.0f, 1.0f, 0.0f));
        particleGeom.setMaximumAngle(90f * FastMath.DEG_TO_RAD);
        particleGeom.setMinimumAngle(91f * FastMath.DEG_TO_RAD);
        particleGeom.setInitialVelocity(0.006f);
        particleGeom.getParticleController().setSpeed(1.0f);
        particleGeom.setMinimumLifeTime(5000.0f);
        particleGeom.setMaximumLifeTime(15000.0f);
        particleGeom.setStartSize(2.5f);
        particleGeom.setEndSize(.5f);
        particleGeom.setStartColor(new ColorRGBA(0.0625f, 0.0625f, 1.0f, 1.0f));
        particleGeom.setEndColor(new ColorRGBA(0.0625f, 0.0625f, 1.0f, 0.0f));
        particleGeom.getParticleController().setControlFlow(false);
        particleGeom.getParticleController().setRepeatType(Controller.RT_CLAMP);
        
		ZBufferState zstate = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		zstate.setEnabled(false);
		particleGeom.setRenderState(zstate);
		
		particleGeom.setModelBound(new BoundingSphere());
		particleGeom.updateModelBound();

		particleGeom.setRenderState(ts);
		particleGeom.setRenderState(as1);
		
		particleGeom.updateRenderState();
		particleGeom.updateGeometricState(-1, true);
        
        return particleGeom;
		
	}
	
	public static ParticleMesh createMainBlast() {
		
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture(
				TextureManager.loadTexture(
						ExplosionFactory.class.getResource(
								"flaresmall.jpg"),
								Texture.MinificationFilter.Trilinear,
								Texture.MagnificationFilter.Bilinear));
		ts.setEnabled(true);
		
	    BlendState as1 = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
	    as1.setBlendEnabled(true);
	    as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
	    as1.setDestinationFunction(BlendState.DestinationFunction.One);
	    as1.setTestEnabled(true);
	    as1.setTestFunction(BlendState.TestFunction.GreaterThan);
	    as1.setEnabled(true);
		
        ParticleMesh particleGeom = ParticleFactory.buildParticles("", 300);        
        particleGeom.warmUp(120);        
        particleGeom.clearInfluences();        
        particleGeom.setEmissionDirection(new Vector3f(0.0f, 1.0f, 0.0f));
        particleGeom.setMaximumAngle(90f * FastMath.DEG_TO_RAD);
        particleGeom.setMinimumAngle(0);
        particleGeom.setInitialVelocity(0.006f);
        particleGeom.getParticleController().setSpeed(0.5f);
        particleGeom.setMinimumLifeTime(5000.0f);
        particleGeom.setMaximumLifeTime(15000.0f);
        particleGeom.setStartSize(2.5f);
        particleGeom.setEndSize(.5f);
        particleGeom.setStartColor(new ColorRGBA(1.0f, 0.312f, 0.121f, 1.0f));
        particleGeom.setEndColor(new ColorRGBA(1.0f, 0.24313726f,0.03137255f, 0.0f));
        particleGeom.getParticleController().setControlFlow(true);
        particleGeom.getParticleController().setRepeatType(Controller.RT_CLAMP);
        
		ZBufferState zstate = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		zstate.setEnabled(false);
		particleGeom.setRenderState(zstate);
		
		particleGeom.setModelBound(new BoundingSphere());
		particleGeom.updateModelBound();

		particleGeom.setRenderState(ts);
		particleGeom.setRenderState(as1);
		
		particleGeom.updateRenderState();
		particleGeom.updateGeometricState(-1, true);
        
        return particleGeom;
	}

}
