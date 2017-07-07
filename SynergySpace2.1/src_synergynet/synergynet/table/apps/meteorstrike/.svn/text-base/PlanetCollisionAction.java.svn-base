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

import java.util.ArrayList;
import java.util.List;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;
import com.jmex.effects.particles.ParticleSystem;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.contact.ContactInfo;

public class PlanetCollisionAction extends InputAction {
	
	protected Node rootNode;
	protected Spatial planet;
	List<ParticleSystem> mainExplosions = new ArrayList<ParticleSystem>();
	List<ParticleSystem> ringExplosions = new ArrayList<ParticleSystem>();
	private MeteorStrikeApp parent;

	public PlanetCollisionAction(MeteorStrikeApp parent, Spatial planet, Node rootNode) {
		this.parent = parent;
		this.planet = planet;
		this.rootNode = rootNode;
	}
	
	public void performAction( InputActionEvent evt ) {
		final ContactInfo contactInfo = ( (ContactInfo) evt.getTriggerData() );
		DynamicPhysicsNode sphere;
		if ( contactInfo.getNode2() instanceof DynamicPhysicsNode ) {
			sphere = (DynamicPhysicsNode) contactInfo.getNode2();
		}else if ( contactInfo.getNode1() instanceof DynamicPhysicsNode ) {
			sphere = (DynamicPhysicsNode) contactInfo.getNode1();
		}else {
			return;
		}

		float mass = sphere.getMass();
		
		Vector3f contactPos = new Vector3f();
		contactInfo.getContactPosition(contactPos);
		
		Vector3f linVel = sphere.getLinearVelocity(null).normalize();

		ParticleSystem mainExplosion = getAvailableMainExplosion();		
		mainExplosion.setLocalTranslation(contactPos);		
		mainExplosion.setEmissionDirection(linVel);
		mainExplosion.forceRespawn();
		mainExplosion.setCullHint(CullHint.Dynamic);
		mainExplosion.updateGeometricState(0f, true);
		mainExplosion.updateWorldVectors();
		mainExplosion.updateRenderState();
		
		mainExplosion.setStartSize(.5f * mass);
		mainExplosion.setEndSize(.1f * mass);
		
		
		
		ParticleSystem ringExplosion = getAvailableRingExplosion();		
		ringExplosion.setLocalTranslation(contactPos);
		ringExplosion.setEmissionDirection(linVel);
		ringExplosion.forceRespawn();
		ringExplosion.setCullHint(CullHint.Dynamic);
		ringExplosion.updateGeometricState(0f, true);
		ringExplosion.updateWorldVectors();
		ringExplosion.updateRenderState();
		
		ringExplosion.setInitialVelocity(0.001f * mass);
		ringExplosion.setStartSize(.5f * mass);
		ringExplosion.setEndSize(.1f * mass);

		
		rootNode.updateGeometricState(0f, true);
		rootNode.updateRenderState();
		
		sphere.setActive(false);
		sphere.clearDynamics();
		sphere.rest();		
		parent.removeMeteor(sphere);
	}
	
	private ParticleSystem makeNewMainExplosionAvailable() {
		ParticleSystem explosionMain = ExplosionFactory.createMainBlast();
		explosionMain.setCullHint(CullHint.Always);
		rootNode.attachChild(explosionMain);
		mainExplosions.add(explosionMain);
		
		return explosionMain;
	}
	
	private ParticleSystem makeNewRingExplosionAvailable() {
		ParticleSystem explosionRing = ExplosionFactory.createRingBlast();
		explosionRing.setCullHint(CullHint.Always);
		rootNode.attachChild(explosionRing);
		ringExplosions.add(explosionRing);
		return explosionRing;
	}

	private ParticleSystem getAvailableMainExplosion() {
		for(ParticleSystem pg : mainExplosions) {
			if(!pg.getController(0).isActive()) {
				return pg;
			}
		}
		ParticleSystem explosion = makeNewMainExplosionAvailable();		
		return explosion;
	}
	
	private ParticleSystem getAvailableRingExplosion() {
		for(ParticleSystem pg : ringExplosions) {
			if(!pg.getController(0).isActive()) {
				return pg;
			}
		}
		ParticleSystem explosion = makeNewRingExplosionAvailable();		
		return explosion;
	}
}