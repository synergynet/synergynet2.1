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

package synergyspace.jmeapps.multitouchpicking;

import java.util.List;
import java.util.logging.Logger;

import synergyspace.jme.pickingsystem.IJMEMultiTouchPicker;
import synergyspace.jme.pickingsystem.JMEMultiTouchPickSystem;
import synergyspace.jme.pickingsystem.PickSystemException;
import synergyspace.jme.pickingsystem.data.OrthogonalPickResultData;
import synergyspace.jme.pickingsystem.data.PickRequest;
import synergyspace.jme.pickingsystem.data.PickResultData;
import synergyspace.jme.pickingsystem.data.ThreeDPickResultData;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.input.MouseInput;
import com.jme.input.MouseInputListener;
import com.jme.light.PointLight;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Teapot;

public class TestJMEMultiTouchPicking extends SimpleGame implements MouseInputListener {

	protected static Logger log = Logger.getLogger(TestJMEMultiTouchPicking.class.getName());

	protected IJMEMultiTouchPicker pickSystem = JMEMultiTouchPickSystem.getInstance();	

	@Override
	protected void simpleInitGame() {
		lightState.detachAll();

		PointLight pointlight = new PointLight();
		pointlight.setLocation(new Vector3f(15f, 5f, 30f));
		pointlight.setAmbient(ColorRGBA.blue);
		pointlight.setAttenuate(true);
		pointlight.setEnabled(true);
		this.lightState.attach(pointlight);

		pointlight = new PointLight();
		pointlight.setAttenuate(true);
		pointlight.setLocation(new Vector3f(-15f, 5f, 10f));
		pointlight.setAmbient(ColorRGBA.red);
		pointlight.setEnabled(true);
		this.lightState.attach(pointlight);

//		this.input = new InputHandler();
		MouseInput.get().setCursorVisible(true);
		MouseInput.get().addListener(this);		
		addTeapot();

		try {
			pickSystem.setPickingRootNode(rootNode);
		} catch (PickSystemException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}


	private void addTeapot() {
		Teapot tp1 = new Teapot("tp1");
		tp1.setLocalTranslation(new Vector3f(1f, 2f, -2f));
		tp1.setModelBound(new BoundingSphere());
		tp1.updateModelBound();
		rootNode.attachChild(tp1);

		Teapot tp2 = new Teapot("tp2");
		tp2.setLocalTranslation(new Vector3f(1f, 2f, -7f));
		tp2.setModelBound(new BoundingSphere());
		tp2.updateModelBound();
		rootNode.attachChild(tp2);
	}

	public static void main(String[] args) {
		TestJMEMultiTouchPicking app = new TestJMEMultiTouchPicking();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	public void onButton(int button, boolean pressed, int x, int y) {
		if(pressed) {
			log.info("Requesting pick at " + x + ", " + y);
			PickRequest req = new PickRequest(0, new Vector2f(x, y));
			List<PickResultData> pickResults;
			try {
				pickResults = pickSystem.doPick(req);
				log.info("*********");
				for(PickResultData r : pickResults) {
					if(r instanceof ThreeDPickResultData) {
						ThreeDPickResultData tdprd = (ThreeDPickResultData) r;
						log.info("* Picked " + tdprd.getPickedSpatialName() + "@" + tdprd.getCursorScreenPositionAtPick());
						Box b = new Box("loc", tdprd.getPointOfSelection(), 0.1f, 0.1f, 0.1f);
						b.setDefaultColor(ColorRGBA.red);
						b.setModelBound(new BoundingBox());
						rootNode.attachChild(b);
						rootNode.updateGeometricState(0f, false);
						rootNode.updateModelBound();
					}else if(r instanceof OrthogonalPickResultData){
						log.info("* Picked orthogonal element " + r);
					}
				}
				log.info("**********");
			} catch (PickSystemException e) {
				e.printStackTrace();
				System.exit(1);
			}			
		}
	}

	public void onMove(int delta, int delta2, int newX, int newY) {}
	public void onWheel(int wheelDelta, int x, int y) {}
}
