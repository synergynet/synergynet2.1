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

package synergynet.table.apps.mathpadapp.controllerapp;

import java.io.IOException;
import java.util.ArrayList;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import synergynet.contentsystem.ContentSystem;
import synergynet.services.ServiceManager;
import synergynet.services.exceptions.CouldNotStartServiceException;
import synergynet.services.exceptions.ServiceNotRunningException;
import synergynet.services.net.netservicediscovery.NetworkServiceDiscoveryService;
import synergynet.services.net.tablecomms.client.TableCommsApplicationListener;
import synergynet.services.net.tablecomms.client.TableCommsClientService;
import synergynet.table.SynergyNetAppUtils;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.menucontrol.HoldTopRightExit;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergynet.table.apps.mathpadapp.clientapp.MathPadClient;
import synergynet.table.apps.mathpadapp.conceptmapping.GraphManager;
import synergynet.table.apps.mathpadapp.networkmanager.handlers.controller.ControllerMessageHandler;
import synergynet.table.apps.mathpadapp.networkmanager.managers.ControllerManager;
import synergyspace.jme.sysutils.CameraUtility;

public class MathPadController extends DefaultSynergyNetApp{
	
	private TableCommsClientService comms;
	protected ControllerMessageHandler messageHandler;
	protected ControllerManager controllerManager;
	private ContentSystem contentSystem;
	private ControlBar controlBar;
	private NetworkServiceDiscoveryService nsds = null;

	public MathPadController(ApplicationInfo info) {
		super(info);
	}

	@Override
	public void addContent() {
		
		setDefaultSteadfastLimit(1);
		SynergyNetAppUtils.addTableOverlay(this);

		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		setMenuController(new HoldTopRightExit());

		controlBar = new ControlBar(contentSystem);
	}
	
	@Override
	public void onActivate() {
		connect();
	}
	
	@Override
	protected void onDeactivate() {
	}

	protected Camera getCamera() {
		if(cam == null) {
			cam = CameraUtility.getCamera();
			cam.setLocation(new Vector3f(0f, 10f, 50f));
			cam.lookAt(new Vector3f(), new Vector3f( 0, 0, -1 ));
			cam.update();
		}		
		return cam;
	}

	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(contentSystem != null) contentSystem.update(tpf);
		if(controllerManager != null) controllerManager.update(tpf);
	}

	public void connect(){

		
		new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					if(comms == null) comms = (TableCommsClientService) ServiceManager.getInstance().get(TableCommsClientService.class);
					if(comms != null){
							while(!comms.isClientConnected()){
							Thread.sleep(1000);
							if(nsds == null){
								nsds = (NetworkServiceDiscoveryService) ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);
							}else{
								nsds.shutdown();
							}
							nsds.start();
							comms.start();
						}
						if(nsds != null) nsds.shutdown();

						ArrayList<Class<?>> clientClasses = new ArrayList<Class<?>>();
						clientClasses.add(MathPadClient.class);
						//clientClasses.add(MathPadProjector.class);
						ArrayList<Class<?>> controllerClasses = new ArrayList<Class<?>>();
						controllerClasses.add(MathPadController.class);
						
						controllerManager = new ControllerManager(contentSystem, comms, clientClasses);
						controllerManager.createRemoteDesktopManager(controllerClasses, clientClasses);
						controllerManager.setGraphManager(new GraphManager(contentSystem));
						controlBar.setControllerManager(controllerManager);
						messageHandler = new ControllerMessageHandler(controllerManager);
						comms.register(MathPadController.this, messageHandler);
						comms.register("connection_listener", new TableCommsApplicationListener(){
							@Override
							public void messageReceived(Object obj) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void tableDisconnected() {
								if(comms != null)
									try {
										comms.stop();
									} catch (ServiceNotRunningException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								comms = null;
								connect();
							}
							
						});
					}
				} catch (CouldNotStartServiceException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
