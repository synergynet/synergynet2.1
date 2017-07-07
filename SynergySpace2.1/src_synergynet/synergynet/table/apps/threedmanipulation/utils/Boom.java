package synergynet.table.apps.threedmanipulation.utils;

import synergynet.table.apps.meteorstrike.ExplosionFactory;

import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Controller;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleMesh;
import com.jmex.effects.particles.ParticleSystem;

public class Boom {

	public static ParticleSystem createBoom(){		
		ParticleSystem mainExplosion = createMainBlast();
		mainExplosion.setCullHint(CullHint.Always);
		mainExplosion.updateGeometricState(0f, true);
		mainExplosion.updateWorldVectors();
		mainExplosion.updateRenderState();		
		mainExplosion.setInitialVelocity(0.01f);
		mainExplosion.setStartSize(0.05f);
		mainExplosion.setEndSize(0.001f);
		return mainExplosion;		
	}
	
	private static ParticleMesh createMainBlast() {
		
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
		
        ParticleMesh particleGeom = ParticleFactory.buildParticles("aaa", 100);        
 
        particleGeom.setEmissionDirection(new Vector3f(0.0f, 1f, 1f));
        particleGeom.setMaximumAngle(FastMath.DEG_TO_RAD * 50);
        particleGeom.setMinimumAngle(FastMath.DEG_TO_RAD * 90);
        particleGeom.setMinimumLifeTime(500.0f);
        particleGeom.setMaximumLifeTime(1000.0f);
        particleGeom.warmUp(100);
        particleGeom.setStartSize(0.005f);
        particleGeom.setEndSize(0.0001f);
        particleGeom.getParticleController().setControlFlow(false);
        particleGeom.getParticleController().setRepeatType(Controller.RT_CLAMP);
        particleGeom.getParticleController().setSpeed(1f);
        particleGeom.setInitialVelocity(1f);
        particleGeom.setStartColor(ColorRGBA.blue.clone());
        particleGeom.setEndColor(ColorRGBA.white.clone());
     
		ZBufferState zstate = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		zstate.setEnabled(false);
		particleGeom.setRenderState(zstate);
		
		//particleGeom.setModelBound(new BoundingSphere());
		//particleGeom.updateModelBound();

		particleGeom.setRenderState(ts);
		particleGeom.setRenderState(as1);
		
		particleGeom.updateRenderState();
		particleGeom.updateGeometricState(-1, true);
        
        return particleGeom;
	}
}
