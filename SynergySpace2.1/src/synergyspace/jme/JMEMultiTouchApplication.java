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

package synergyspace.jme;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import synergyspace.jme.config.AppConfig;
import synergyspace.jme.cursorsystem.MultiTouchCursorSystem;
import synergyspace.jme.gfx.TableOverlayNode;
import synergyspace.jme.gfx.twod.FPSNode;
import synergyspace.jme.pickingsystem.IJMEMultiTouchPicker;
import synergyspace.jme.pickingsystem.JMEMultiTouchPickSystem;
import synergyspace.jme.pickingsystem.PickSystemException;
import synergyspace.jme.simulators.directsimulator.JMEDirectSimulator;
import synergyspace.jme.simulators.tuiosimulator.JMETUIOSimulator;
import synergyspace.jme.sysutils.CameraUtility;
import synergyspace.jme.sysutils.DisplayUtility;
import synergyspace.jme.sysutils.InputUtility;
import synergyspace.jme.sysutils.RenderPassUtility;
import synergyspace.mtinput.IMultiTouchInputSource;
import synergyspace.mtinput.MultiTouchInputComponent;
import synergyspace.mtinput.filters.LoggingFilter;
import synergyspace.mtinput.filters.SingleInputFilter;
import synergyspace.mtinput.luminja.LuminMultiTouchInput;
import synergyspace.mtinput.simulator.AbstractMultiTouchSimulator;
import synergyspace.mtinput.tuio.TUIOMultiTouchInput;

import com.jme.app.BaseGame;
import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.input.joystick.JoystickInput;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.state.LightState;
import com.jme.scene.state.WireframeState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jme.util.Timer;
import com.jme.util.geom.Debugger;

/**
 * Abstract base class providing all necessary infrastructure for a multi-touch application.
 * Use {@link synergyspace.jme.abstractapps.AbstractMultiTouchThreeDApp} for predominantly 3D applications
 * and {@link synergyspace.jme.abstractapps.AbstractMultiTouchTwoDeeApp} for 2D applications.  When creating an
 * instance of a multi-touch application, use {@link synergyspace.jme.config.AppConfig} to control various
 * parameters. 
 * 
 * @author dcs0ah1
 *
 */


//TODO: determining good practice for logging, and implement system-wide

public abstract class JMEMultiTouchApplication extends BaseGame {
	private static final Logger log = Logger.getLogger(JMEMultiTouchApplication.class.getName());

	protected Camera cam;
	protected Node rootNode;
	protected FPSNode fpsNode;
	protected TableOverlayNode tableOverlay;
	protected Timer timer;
	protected float tpf;
	protected InputHandler input;	
	protected WireframeState wireState;
	protected LightState lightState;
	protected BasicPassManager passManager = new BasicPassManager();

	protected MultiTouchCursorSystem cursorSystem = new MultiTouchCursorSystem();
	protected IMultiTouchInputSource mtInput;
	protected IJMEMultiTouchPicker pickSystem = JMEMultiTouchPickSystem.getInstance();

	protected URL defaultDialogImage = JMEMultiTouchApplication.class.getResource("synergyspace-splash.png");
	protected int dialogBehaviour;

	protected List<Updateable> elementsRequiringUpdates = new ArrayList<Updateable>();

	protected MultiTouchInputComponent inputComponent;

	public JMEMultiTouchApplication() {
		super();
	}

	protected void update(float interpolation) {		
		timer.update();
		tpf = timer.getTimePerFrame();
		input.update(tpf);


		InputUtility.checkKeys(log, cam);
		try {
			if(inputComponent != null)
				inputComponent.update(tpf);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).execute();

		if(AppConfig.showFPS) {
			fpsNode.update(timer);
		}

		processInputDirectives();

		if(!InputUtility.pause) {
			rootNode.updateGeometricState(tpf, true);
			passManager.updatePasses(tpf);
		}

		for(Updateable item : elementsRequiringUpdates) {
			item.update(tpf);
		}
	}

	private void processInputDirectives() {
		if(InputUtility.shouldFinish) {
			finish();
		}

		if(InputUtility.useLighting && !lightState.isEnabled()) {
			lightState.setEnabled(true);
			rootNode.updateRenderState();
		}else if(!InputUtility.useLighting && lightState.isEnabled()) {
			lightState.setEnabled(false);
			rootNode.updateRenderState();			
		}

		if(InputUtility.wireframeMode && !wireState.isEnabled()) {
			wireState.setEnabled(true);
			rootNode.updateRenderState();
		}else if(!InputUtility.wireframeMode && wireState.isEnabled()) {
			wireState.setEnabled(false);
			rootNode.updateRenderState();			
		}

	}

	@Override
	protected void render(float interpolation) {
		Renderer r = display.getRenderer();
		r.clearBuffers();		

		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER).execute();
		passManager.renderPasses(r);

		doDebug(r);
	}



	@Override
	protected void initGame() {
		rootNode = new Node("rootNode");		

		lightState = getAndAttachLightState();

		wireState = display.getRenderer().createWireframeState();
		wireState.setEnabled(false);
		rootNode.setRenderState(wireState);

		tableOverlay = new TableOverlayNode(0f);
		tableOverlay.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		tableOverlay.setLightCombineMode(Spatial.LightCombineMode.Off);
		tableOverlay.updateRenderState();

		try {
			pickSystem.setPickingRootNode(rootNode);
		} catch (PickSystemException e1) {
			e1.printStackTrace();
			System.exit(1);
		}

		inputComponent.registerMultiTouchEventListener(tableOverlay);

		rootNode.attachChild(tableOverlay);

		ZBufferState buf = display.getRenderer().createZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		rootNode.setRenderState(buf);				

		RenderPassUtility.addRenderPassesFromAppConfig(cam, rootNode, passManager);

		if(AppConfig.showFPS) {			
			fpsNode = new FPSNode("FPS");
			fpsNode.setCullHint(CullHint.Never);
			RenderPass fpsPass = new RenderPass();
			fpsPass.add(fpsNode);	
			passManager.add(fpsPass);
		}		
		// expect subclass to call super
	}

	protected abstract LightState getAndAttachLightState();

	@Override
	protected void initSystem() {
		log.info(getVersion());
		try {
			display = DisplaySystem.getDisplaySystem();          
	        display.setMinDepthBits(AppConfig.depthBits);
	        display.setMinStencilBits(AppConfig.stencilBits);
	        display.setMinAlphaBits(AppConfig.alphaBits);
	        display.setMinSamples(AppConfig.samples);
	        display.createWindow(1024, 768, 24, 60, false );
	        display.getRenderer().setBackgroundColor( ColorRGBA.black );
	        display.getRenderer().setBackgroundColor(AppConfig.backgroundColour);
	        display.setTitle(AppConfig.appTitle);
			DisplayUtility.displayDriverInfo(log);            
		} catch ( JmeException e ) {
			log.log(Level.SEVERE, "Could not create displaySystem", e);
			System.exit( 1 );
		}

		cam = CameraUtility.getCamera();
		cam.update();
		display.getRenderer().setCamera(cam);

		cursorSystem.setPickSystem(pickSystem);

		configureAndStartTableInput();


		InputUtility.setupKeys();
		InputUtility.setupMouseInput();		

		timer = Timer.getTimer();
		input = InputUtility.getInputHandler(cam);


	}	

	private void configureAndStartTableInput() {

		switch(AppConfig.tableType) {
		case AppConfig.TABLE_TYPE_REMOTE_TUIO: {
			try {
				mtInput = new TUIOMultiTouchInput();
			} catch (SecurityException e) {
				e.printStackTrace();
			}

			break;
		}

		case AppConfig.TABLE_TYPE_JME_DIRECT_SIMULATOR: {
			try {
				JMEDirectSimulator simulator = new JMEDirectSimulator(display.getWidth(), display.getHeight());
				//				mtInput = new JMEMultiTouchInputService(simulator);
				mtInput = simulator;
			} catch (SecurityException e) {
				e.printStackTrace();
			}

			break;
		}

		case AppConfig.TABLE_TYPE_JME_TUIO_SIMULATOR: {
			try {
				mtInput = new TUIOMultiTouchInput();
			} catch (SecurityException e) {
				e.printStackTrace();
			}

			AbstractMultiTouchSimulator simulator = new JMETUIOSimulator(display.getWidth(), display.getHeight());
			simulator.start();	
			break;
		}

		case AppConfig.TABLE_TYPE_LUMIN_JAVA: {
			try {
				mtInput = new LuminMultiTouchInput();
			} catch (SecurityException e) {
				e.printStackTrace();
			}

			break;
		}
		}

		inputComponent = new MultiTouchInputComponent(mtInput);		
		inputComponent.registerMultiTouchEventListener(cursorSystem);

		if(AppConfig.singleInputOnly == AppConfig.SINGLE_INPUT_ONLY_ON) {
			inputComponent.addMultiTouchInputFilter(new SingleInputFilter());
		}
		
		if(AppConfig.recordTableInput == AppConfig.RECORD_TABLE_INPUT_ON) {
			try {
				inputComponent.addMultiTouchInputFilter(new LoggingFilter());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void doDebug(Renderer r) {
		if(InputUtility.showBounds) Debugger.drawBounds(rootNode, r, true);

		if(InputUtility.showNormals) {
			Debugger.drawNormals(rootNode, r);
			Debugger.drawTangents(rootNode, r);
		}

		if(InputUtility.showDepth){
			r.renderQueue();			
			Debugger.drawBuffer(Texture.RenderToTextureType.Depth16, Debugger.NORTHEAST, r);
		}
	}

	protected void cleanup() {
		log.info( "Cleaning up resources." );
		TextureManager.doTextureCleanup();
		if (display != null && display.getRenderer() != null)
			display.getRenderer().cleanup();
		KeyInput.destroyIfInitalized();
		MouseInput.destroyIfInitalized();
		JoystickInput.destroyIfInitalized();
	}

	@Override
	protected void reinit() {
	}


	protected void quit() {
		super.quit();
		System.exit(0);
	}

	protected void addItemForUpdating(Updateable item) {
		this.elementsRequiringUpdates.add(item);
	}

}
