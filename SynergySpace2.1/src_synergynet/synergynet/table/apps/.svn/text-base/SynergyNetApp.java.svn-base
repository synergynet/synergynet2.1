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

package synergynet.table.apps;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.logging.Logger;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.TextLabel;
import synergynet.table.SynergyNetDesktop;
import synergynet.table.animationsystem.AnimationSystem;
import synergynet.table.appregistry.ApplicationControlError;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.ApplicationRegistry;
import synergynet.table.appregistry.ApplicationTaskManager;
import synergynet.table.appregistry.menucontrol.ApplicationDefined;
import synergynet.table.appregistry.menucontrol.HoldBottomLeftExit;
import synergynet.table.appregistry.menucontrol.MenuController;
import synergynet.table.config.developer.DeveloperConfigPrefsItem;
import synergynet.table.config.display.DisplayConfigPrefsItem;
import synergyspace.jme.pickingsystem.PickSystemException;
import synergyspace.jme.sysutils.BorderUtility;
import synergyspace.jme.sysutils.InputUtility;
import synergyspace.jme.sysutils.StereoRenderPass;
import synergyspace.jme.sysutils.StereoRenderPass.StereoMode;

import com.acarter.scenemonitor.SceneMonitor;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.math.Vector2f;
import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.Node;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.state.WireframeState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.Debugger;
import com.jmex.game.state.GameState;

public abstract class SynergyNetApp extends GameState {
	
	private static final Logger log = Logger.getLogger(SynergyNetApp.class.getName());	
	
	protected InputHandler input;	
	private Node rootNode;
	protected Node orthoNode;
	protected Node worldNode;
	private int activationCount = 0;
	protected ApplicationInfo info;
	protected File applicationDataDirectory;
	private WireframeState wireState;
	protected BasicPassManager pManager;
	private RenderPass worldRenderPass;
	private RenderPass orthoRenderPass;
	protected boolean exitState = false;
	private boolean updateSceneMonitor = false;
	private boolean limitOverride = false;
	private boolean scaleMinLimit, scaleMaxLimit, rotMinLimit, rotMaxLimit, translateMinLimit, translateMaxLimit,
		translateXMinLimit, translateXMaxLimit,translateYMinLimit, translateYMaxLimit = false;
	private float scaleMin, scaleMax, rotMin, rotMax, translateMin, translateMax,
		translateXMin, translateXMax,translateYMin, translateYMax = 0f;

	public SynergyNetApp(ApplicationInfo info) {
		super();
		this.info = info;
		this.applicationDataDirectory = new File(SynergyNetDesktop.getSynergyNetLocalWorkingDirectory(), info.getApplicationName() + "_" + info.getApplicationVersion());
		log.info("Create application Data Directory at: "+this.applicationDataDirectory.getAbsolutePath());
		setBorderAdaptationScaleMin(0.25f);
		if(getDefaultSteadfastLimit()!=3)setDefaultSteadfastLimit(3);
	}

	public void init() {		
		initInput();
		pManager = new BasicPassManager();
		setWorldRenderPass(new RenderPass());
		setOrthoRenderPass(new RenderPass());
		
		setRootNode(new Node(name + ".rootnode"));
		wireState = DisplaySystem.getDisplaySystem().getRenderer().createWireframeState();
		wireState.setEnabled(false);
		getRootNode().setRenderState(wireState);
		
		
		getRootNode().setModelBound(new BoundingBox());
		getRootNode().updateModelBound();
		orthoNode = createOrthoNode();
		worldNode = createWorldNode();

		getRootNode().attachChild(orthoNode);
		getRootNode().attachChild(worldNode);
		getCamera();
		addContent();
		orthoNode.updateModelBound();
		getRootNode().updateGeometricState(0, true);
		getRootNode().updateRenderState();
		
		getWorldRenderPass().add(getRootNode());
		pManager.add(getWorldRenderPass());
		getOrthoRenderPass().add(getOrthoNode());
		pManager.add(getOrthoRenderPass());

		String threeDeeMode = new DisplayConfigPrefsItem().getThreeDee();
		
		if (!threeDeeMode.equals("NONE")){			
			StereoRenderPass sp = new StereoRenderPass(rootNode);
			sp.setStereoMode(StereoMode.valueOf(threeDeeMode));
			pManager.clearAll();
	        pManager.add(sp);
		}
		
	}

	public Node getOrthoNode() {
		return orthoNode;
	}

	public Node getWorldNode() {
		return worldNode;
	}

	private Node createWorldNode() {
		Node n = new Node(info.getApplicationName() + "_worldNode");
		n.setModelBound(new BoundingBox());
		n.updateModelBound();
		ZBufferState buf = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		n.setRenderState(buf);
		log.info("WorldNode is created");
		return n;
	}

	private Node createOrthoNode() {
		DisplaySystem display = DisplaySystem.getDisplaySystem();
		Node n = new Node(info.getApplicationName() + "_orthoNode");
		n.setModelBound(new OrthogonalBoundingBox());
		n.updateModelBound();
		n.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		n.setLightCombineMode(LightCombineMode.Off);
		ZBufferState buf = display.getRenderer().createZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.NotEqualTo);		
		n.setRenderState(buf);		
		n.updateRenderState();
		n.updateGeometricState(0f, true);
		log.info("OrthoNode is created");
		return n;
	}



	public void onActivate() {
		setActivationCount(getActivationCount() + 1);
		DisplaySystem.getDisplaySystem().setTitle(name);
		DisplaySystem.getDisplaySystem().getRenderer().setCamera(getCamera());
		getCamera().update();
		
		DeveloperConfigPrefsItem devPrefs = new DeveloperConfigPrefsItem();		
		if(devPrefs.getShowSceneMonitor()) {
			SceneMonitor.getMonitor().registerNode(rootNode, "Root Node");
	        SceneMonitor.getMonitor().showViewer(true);
	        updateSceneMonitor  = true;
		}
		
		log.info("SynergyNet Applicaiton Instance '"+name+"' is activated ");
	}
	
	public int getDefaultSteadfastLimit(){
		return BorderUtility.getDefaultSteadfastLimit();
	}
	
	public void setDefaultSteadfastLimit(int i){
		BorderUtility.setDefaultSteadfastLimit(i);
	}
	
	public void setDefaultSteadfastLimit(int i, boolean set){
		BorderUtility.setDefaultSteadfastLimit(i);
		if (set){
			ContentSystem qc = ContentSystem.getContentSystemForSynergyNetApp(this);
			qc.setallSteadfastValues();
		}		
	}


	protected void onDeactivate() {		
		DeveloperConfigPrefsItem devPrefs = new DeveloperConfigPrefsItem();
		if(getDefaultSteadfastLimit()!=3)setDefaultSteadfastLimit(3);
		if(devPrefs.getShowSceneMonitor()) {
			SceneMonitor.getMonitor().unregisterNode(rootNode);
	        SceneMonitor.getMonitor().showViewer(false);
	        updateSceneMonitor = false;
		}
		
		log.info("SynergyNet Applicaiton Instance '"+name+"' is deactivated ");
	}

	protected void requestFocus() {
		try {
			SynergyNetDesktop.getInstance().getPickSystem().setPickingRootNode(worldNode);
			SynergyNetDesktop.getInstance().getPickSystem().setOrthogonalPickingRoot(orthoNode);
		} catch (PickSystemException e) {
			
			//how to handle exception
			e.printStackTrace();
		}		
	}


	protected void stateRender(float tpf) {
	}
	
	private void checkBorderCompatibility(){
		if(!limitOverride){
			if (scaleMinLimit) if (BorderUtility.getVirtualRectangleEnvironmentScale() < scaleMin) incompatible();
			if (scaleMaxLimit) if (BorderUtility.getVirtualRectangleEnvironmentScale() > scaleMax) incompatible();
			if (rotMinLimit) if (BorderUtility.getVirtualRectangleEnvironmentRotation() < rotMin) incompatible();
			if (rotMaxLimit) if (BorderUtility.getVirtualRectangleEnvironmentRotation() > rotMax) incompatible();	
			if (translateXMinLimit) if (BorderUtility.getVirtualRectangleEnvironmentTranslationX() < translateXMin) incompatible();
			if (translateXMaxLimit) if (BorderUtility.getVirtualRectangleEnvironmentTranslationX() > translateXMax) incompatible();
			if (translateYMinLimit) if (BorderUtility.getVirtualRectangleEnvironmentTranslationY() < translateYMin) incompatible();
			if (translateYMaxLimit) if (BorderUtility.getVirtualRectangleEnvironmentTranslationY() > translateYMax) incompatible();	
			if (translateMinLimit){
				float distance = new Vector2f().distance(new Vector2f(BorderUtility.getVirtualRectangleEnvironmentTranslationX(), BorderUtility.getVirtualRectangleEnvironmentTranslationY()));
				if (distance < translateMin) incompatible();
			}
			if (translateMaxLimit){
				float distance = new Vector2f().distance(new Vector2f(BorderUtility.getVirtualRectangleEnvironmentTranslationX(), BorderUtility.getVirtualRectangleEnvironmentTranslationY()));
				if (distance > translateMax) incompatible();
			}
		}
	}

	public void setActive(boolean active) {
		if (active) onActivate();
		else onDeactivate();
		super.setActive(active);
		requestFocus();
		if(active){
			checkBorderCompatibility();
			setDefaultSteadfastLimit(0);
			ContentSystem qc = ContentSystem.getContentSystemForSynergyNetApp(this);
			qc.setallSteadfastValues();
		}
	}

	public final void update(float tpf) {		
		stateUpdate(tpf);
		pManager.updatePasses(tpf);
		if(InputUtility.wireframeMode && !wireState.isEnabled()) {
			wireState.setEnabled(true);
			getRootNode().updateRenderState();
		}else if(!InputUtility.wireframeMode && wireState.isEnabled()) {
			wireState.setEnabled(false);
			getRootNode().updateRenderState();			
		}        
		
		AnimationSystem.getInstance().update(tpf);
		getRootNode().updateGeometricState(tpf, true);
		if(updateSceneMonitor) {
			SceneMonitor.getMonitor().updateViewer(tpf);
		}
		if (exitState){
			exitState = false;
			try {
				ApplicationTaskManager.getInstance().setActiveApplication(ApplicationRegistry.getInstance().getDefaultApp());
			} catch (ApplicationControlError e) {
				e.printStackTrace();
			}
		}
		menuController.update(tpf);
	}
	
	public void exitApp(){
		exitState = true;
	}

	public final void render(float tpf) {
		stateRender(tpf);
		renderPasses(tpf);
		renderDebug(tpf);
	}

	private void renderPasses(float tpf) {
		try{
			pManager.renderPasses(DisplaySystem.getDisplaySystem().getRenderer());
		}catch(IndexOutOfBoundsException e){
			log.severe("Exception occurs when rendering passes: \n"+e.toString());
		}
	}

	public void renderDebug(float tpf) {
		if(InputUtility.showBounds) Debugger.drawBounds(getRootNode(), DisplaySystem.getDisplaySystem().getRenderer(), true);
        if(InputUtility.showNormals) Debugger.drawNormals(getRootNode(), DisplaySystem.getDisplaySystem().getRenderer());		
        if(InputUtility.showDepth) {
            DisplaySystem.getDisplaySystem().getRenderer().renderQueue();
            Debugger.drawBuffer(Texture.RenderToTextureType.Depth, Debugger.NORTHEAST, DisplaySystem.getDisplaySystem().getRenderer());
        }		
	}

	@Override
	public void cleanup() {

	}

	public abstract void addContent();	

	protected abstract void initInput();
	protected abstract Camera getCamera();	
	protected abstract void stateUpdate(float tpf);

	public void setActivationCount(int activationCount) {
		this.activationCount = activationCount;
	}

	public int getActivationCount() {
		return activationCount;
	}

	public ApplicationInfo getInfo() {
		return info;
	}

	public File getApplicationDataDirectory() {
		if(!applicationDataDirectory.exists()) applicationDataDirectory.mkdirs();	
		return applicationDataDirectory;
	}

	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}

	public Node getRootNode() {
		return rootNode;
	}

	public void setOrthoRenderPass(RenderPass orthoRenderPass) {
		this.orthoRenderPass = orthoRenderPass;
	}

	public RenderPass getOrthoRenderPass() {
		return orthoRenderPass;
	}

	public void setWorldRenderPass(RenderPass worldRenderPass) {
		this.worldRenderPass = worldRenderPass;
	}

	public RenderPass getWorldRenderPass() {
		return worldRenderPass;
	}
	
	protected MenuController menuController = new ApplicationDefined();
	public MenuController getMenuController() {
		return menuController;
	}
	
	public void setMenuController(MenuController mc) {
		this.menuController = mc;
	}
	
	public void setBorderAdaptationDefaultLimitOverride(boolean limitOverride){
		this.limitOverride = limitOverride;	
	}
	
	
	public void setBorderAdaptationScaleMin(float scaleMin){
		this.scaleMin = scaleMin;
		scaleMinLimit = true;
	}
	
	public void setBorderAdaptationScaleMax(float scaleMax){
		this.scaleMax = scaleMax;
		scaleMaxLimit = true;
	}
	

	public void setBorderAdaptationRotationMin(float rotMin){
		this.rotMin = rotMin;
		rotMinLimit = true;
	}
	
	public void setBorderAdaptationRotationMax(float rotMax){
		this.rotMax = rotMax;
		rotMaxLimit = true;
	}
	
	public void setBorderAdaptationTranslationXMin(float translateXMin){
		this.translateXMin = translateXMin;
		translateXMinLimit = true;
	}
	
	public void setBorderAdaptationTranslationXMax(float translateXMax){
		this.translateXMax = translateXMax;
		translateXMaxLimit = true;
	}
	
	public void setBorderAdaptationTranslationYMin(float translateYMin){
		this.translateYMin = translateYMin;
		translateYMinLimit = true;
	}
	
	public void setBorderAdaptationTranslationYMax(float translateYMax){
		this.translateYMax = translateYMax;
		translateYMaxLimit = true;
	}
	
	public void setBorderAdaptationTranslationMin(float translateMin){
		this.translateMin = translateMin;
		translateMinLimit = true;
	}
	
	public void setBorderAdaptationTranslationMax(float translateMax){
		this.translateMax = translateMax;
		translateMaxLimit = true;
	}
	
	private void incompatible(){	
		ContentSystem qc = ContentSystem.getContentSystemForSynergyNetApp(this);
		qc.removeAllContentItems();
		
		TextLabel text = (TextLabel)qc.createContentItem(TextLabel.class);		
		text.setAsTopObject();
		text.setText("This Application is not compatible with the display shape being used.");
		text.setFont(new Font("Arial", Font.PLAIN,20));
		text.setBackgroundColour(Color.black);
		text.setTextColour(Color.white);
		text.setBorderSize(0);
		text.setRotateTranslateScalable(false);	
		text.centerItem();
		
		setMenuController(new HoldBottomLeftExit());
		menuController.enableForApplication(this);
	}
	

}