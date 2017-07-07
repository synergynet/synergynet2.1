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

import synergynet.table.SynergyNetDesktop;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.menucontrol.HoldBottomLeftExit;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergyspace.jme.config.AppConfig;
import synergyspace.jme.cursorsystem.CursorRegistry;
import synergyspace.jme.cursorsystem.MultiTouchCursorSystem;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.mtinput.IMultiTouchEventListener;
import synergyspace.mtinput.events.MultiTouchCursorEvent;
import synergyspace.mtinput.events.MultiTouchObjectEvent;

import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;

public class MeteorStrikeApp extends DefaultSynergyNetApp implements IMultiTouchEventListener {

	protected PhysicsSpace physicsSpace;
	protected boolean showPhysics;
	protected float physicsSpeed = 1f;
	protected List<Vector2f> newMeteorRequests = new ArrayList<Vector2f>();
	protected List<DynamicPhysicsNode> meteors = new ArrayList<DynamicPhysicsNode>();
	protected StaticPhysicsNode planetPhysics;
	protected Skybox space;
	private int meteorNum = 0;

	public MeteorStrikeApp(ApplicationInfo info) {
		super(info);
	}

	@Override
	public void addContent() {
		setMenuController(new HoldBottomLeftExit());
		setupLighting();
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent().registerMultiTouchEventListener(this);
		
		physicsSpace = PhysicsSpace.create();
		physicsSpace.setDirectionalGravity(new Vector3f(0f, -1f, 0f));

		//		space = SolarSystemFactory.createSpace();
		worldNode.attachChild(space);

		//		Spatial p = SolarSystemFactory.createSimpleEarth();
		//		Spatial p = SolarSystemFactory.createEvenSimplerEarth();
		Spatial p = SolarSystemFactory.createEarth();		
		planetPhysics = physicsSpace.createStaticNode();
		worldNode.attachChild( planetPhysics );
		planetPhysics.attachChild(p);
		planetPhysics.generatePhysicsGeometry();
		planetPhysics.getLocalTranslation().set( 0, -10, 0 );

		SyntheticButton collisionEventHandler = planetPhysics.getCollisionEventHandler();
		input.addAction( new PlanetCollisionAction(this, p, worldNode), collisionEventHandler, false );

		showPhysics = true;

		cam.setLocation(new Vector3f(-12.614077f, -1.3468808f, 43.177616f));
		cam.update();

		worldNode.updateRenderState();
		worldNode.updateGeometricState(0f, true);
	}

	protected void setupLighting() {
		LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		worldNode.setRenderState(lightState);
		lightState.setEnabled(AppConfig.useLighting);
		SolarSystemFactory.createLighting(lightState, worldNode);
	}

	@Override
	public void stateUpdate(float tpf) {
		if(space != null) space.setLocalTranslation(cam.getLocation());
		checkAddMeteors();
		updateMeteorForces(tpf);
		input.update(tpf);
		physicsSpace.update(tpf * physicsSpeed);				
	}

	private void updateMeteorForces(float tpf) {
		for(DynamicPhysicsNode n : meteors) {
			Vector3f meteorToPlanet = planetPhysics.getWorldTranslation().subtract(n.getWorldTranslation());
			float r_squared = meteorToPlanet.lengthSquared();
			Vector3f force = meteorToPlanet.normalize().mult(10000f/r_squared * tpf * n.getMass());			
			n.addForce(force);

		}		
	}

	public void removeMeteor(DynamicPhysicsNode sphere) {
		meteors.remove(sphere);
		worldNode.detachChild(sphere);		
	}

	private void checkAddMeteors() {
		synchronized (newMeteorRequests) {
			for(Vector2f v : newMeteorRequests) {
				Vector3f worldCoords = DisplaySystem.getDisplaySystem().getWorldCoordinates(v, 1.0f);
				Vector3f vFromCam = worldCoords.subtractLocal(cam.getLocation()).normalize().mult(1.2f);
				Vector3f pos = cam.getLocation().add(vFromCam);
				DynamicPhysicsNode meteorPhysics = SolarSystemFactory.createAndAttachNewMeteor(meteorNum, pos, physicsSpace, worldNode);
				meteorPhysics.setLinearVelocity(vFromCam);
				meteors.add(meteorPhysics);

				worldNode.updateGeometricState(0f, false);
				worldNode.updateRenderState();
				meteorNum++;
			}
			newMeteorRequests.clear();
		}		
	}

	public void cursorPressed(MultiTouchCursorEvent event) {
		ScreenCursor c = CursorRegistry.getInstance().getCursor(event.getCursorID());
		if(c.getPickResult() == null) {
			Vector2f v = MultiTouchCursorSystem.tableToScreen(event.getPosition());
			synchronized(newMeteorRequests) {
				newMeteorRequests.add(v);
			}	
		}
	}

	public void cursorReleased(MultiTouchCursorEvent event) {}
	public void cursorChanged(MultiTouchCursorEvent event) {}
	public void cursorClicked(MultiTouchCursorEvent event) {}
	public void objectAdded  (MultiTouchObjectEvent event) {}
	public void objectChanged(MultiTouchObjectEvent event) {}
	public void objectRemoved(MultiTouchObjectEvent event) {}






}
