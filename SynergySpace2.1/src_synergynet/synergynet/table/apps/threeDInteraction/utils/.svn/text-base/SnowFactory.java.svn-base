package synergynet.table.apps.threeDInteraction.utils;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial.CullHint;
import com.jmex.effects.particles.ParticleSystem;

public class SnowFactory {

	protected ParticleSystem mainExplosion;
	protected Vector3f spawnLocation;
	protected Node worldNode;
	
	public SnowFactory(Vector3f spawnLocation, Node worldNode){
		mainExplosion = SnowParticleSystem.createShow();
		this.spawnLocation = spawnLocation;
		this.worldNode = worldNode;
		forceSpawn();
	}
	
	public Vector3f getSpawnLocation() {
		return spawnLocation;
	}

	public void setSpawnLocation(Vector3f spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

	private void forceSpawn(){	
		mainExplosion.setCullHint(CullHint.Dynamic);
		mainExplosion.setLocalTranslation(this.spawnLocation);		
		mainExplosion.updateGeometricState(0f, true);
		mainExplosion.updateWorldVectors();
		mainExplosion.updateRenderState();		
		mainExplosion.setInitialVelocity(0.005f);
		mainExplosion.setStartSize(0.3f);
		mainExplosion.setEndSize(0.07f);	
	}	
	
	public void enableShow(boolean b){
		if (b){
			if (mainExplosion!=null && !worldNode.hasChild(mainExplosion))
				worldNode.attachChild(mainExplosion);
		}
		else{
			if (mainExplosion!=null && worldNode.hasChild(mainExplosion))
				worldNode.detachChild(mainExplosion);
		}
	}
}
