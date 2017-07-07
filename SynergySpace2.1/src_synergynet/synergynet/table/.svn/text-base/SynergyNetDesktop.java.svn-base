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

package synergynet.table;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import synergynet.Resources;
import synergynet.services.ServiceManager;
import synergynet.table.appregistry.ApplicationControlError;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.ApplicationRegistry;
import synergynet.table.appregistry.ApplicationRegistryXMLReader;
import synergynet.table.appregistry.ApplicationTaskManager;
import synergynet.table.appregistry.DesktopTypeXMLReader;
import synergynet.table.apps.splasher.SplasherApp;
import synergynet.table.config.display.DisplayConfigPrefsItem;
import synergynet.table.config.logging.LoggingConfigPrefsItem;
import synergynet.table.config.logging.LoggingConfigPrefsItem.LoggingLevel;
import synergynet.table.config.network.NetworkConfigPrefsItem;
import synergynet.table.config.table.TableConfigPrefsItem;
import synergynet.table.config.table.TableConfigPrefsItem.TableType;

import synergyspace.jme.cursorsystem.MultiTouchCursorSystem;
import synergyspace.jme.mtinputbridge.MultiTouchInputFilterManager;
import synergyspace.jme.pickingsystem.IJMEMultiTouchPicker;
import synergyspace.jme.pickingsystem.JMEMultiTouchPickSystem;
import synergyspace.jme.simulators.directsimulator.JMEDirectSimulator;
import synergyspace.jme.simulators.tuiosimulator.JMETUIOSimulator;
import synergyspace.jme.sysutils.InputUtility;
import synergyspace.mtinput.IMultiTouchInputFilter;
import synergyspace.mtinput.IMultiTouchInputSource;
import synergyspace.mtinput.MultiTouchInputComponent;
import synergyspace.mtinput.exceptions.MultiTouchInputException;
import synergyspace.mtinput.luminja.LuminMultiTouchInput;
import synergyspace.mtinput.luminja.MIMProcessController;
import synergyspace.mtinput.simulator.AbstractMultiTouchSimulator;
import synergyspace.mtinput.tuio.TUIOMultiTouchInput;

import com.jme.app.BaseGame;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.input.joystick.JoystickInput;
import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.Timer;
import com.jmex.audio.AudioSystem;
import com.jmex.game.state.GameStateManager;

public class SynergyNetDesktop extends BaseGame {
	
	private static final String APPNAME = "SynergyNetDesktop";
	private static final String VERSION_STRING = "2.1";
	
	protected static String configFile = "config/demotableconfiguration.xml";
	private static final Logger log = Logger.getLogger(SynergyNetDesktop.class.getName());	
	private Timer timer;	
	private float tpf;

	protected ApplicationTaskManager taskManager = ApplicationTaskManager.getInstance();
	protected ApplicationRegistry registry = ApplicationRegistry.getInstance();

	public ApplicationRegistry getRegistry() {
		return registry;
	}

	protected IMultiTouchInputSource multiTouchInputSource;
	protected MultiTouchCursorSystem cursorSystem = new MultiTouchCursorSystem();


	protected IJMEMultiTouchPicker pickSystem = JMEMultiTouchPickSystem.getInstance();
//	protected DesktopActionListener desktopActionListener = new DesktopActionListener();
//	public DesktopActionListener getDesktopActionListener() {
//		return desktopActionListener;
//	}

	private MultiTouchInputComponent inputComponent;

	private static SynergyNetDesktop instance;

	public static SynergyNetDesktop getInstance() {
		if(instance == null) instance = new SynergyNetDesktop();
		return instance;
	}

	private SynergyNetDesktop() {
		super();
	}

	public MultiTouchInputComponent getMultiTouchInputComponent() {
		return inputComponent;
	}

	protected final void update(float interpolation) {
		timer.update();
		tpf = timer.getTimePerFrame();		

		InputUtility.checkKeys(log, DisplaySystem.getDisplaySystem().getRenderer().getCamera());
		
		try {
			if(multiTouchInputSource != null) {
				try {
					multiTouchInputSource.update(tpf);
				} catch (MultiTouchInputException e) {
					log.severe("Multitouch input source failed. " + e.getMessage());
					log.severe("  Stack trace follows:");
					StringBuffer sb = new StringBuffer();
					for(StackTraceElement elem : e.getStackTrace()) {
						sb.append("   " + elem.toString() + "\n");
					}
					log.severe(sb.toString());
				}
			}
			
			if(inputComponent != null) {
				inputComponent.update(tpf);
			}
			
			//if(desktopActionListener != null)
			//	desktopActionListener.update(tpf);
		} catch (IllegalArgumentException e) {
			log.severe(e.toString());
		}
		
		if(AudioSystem.isCreated()) {
			AudioSystem.getSystem().update();
		}
		
		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).execute();
		GameStateManager.getInstance().update(tpf);
		ServiceManager.getInstance().update();

	}

	protected final void render(float interpolation) {	
		display.getRenderer().clearBuffers();
		GameStateManager.getInstance().render(tpf);
	}

	protected final void initSystem() {
		NetworkConfigPrefsItem netPrefs = new NetworkConfigPrefsItem();
		if(netPrefs.getProxyEnabled()) {
		String proxySetString = netPrefs.getProxyEnabled()? "true" : "false";
		System.getProperties().put("http.proxySet", proxySetString);
		System.getProperties().put("http.proxyHost", netPrefs.getProxyHost());
		System.getProperties().put("http.proxyPort", netPrefs.getProxyPort());
		}

		
		try {			
			display = DisplaySystem.getDisplaySystem();
			DisplayConfigPrefsItem prefsItem = new DisplayConfigPrefsItem();
			display.setMinSamples(prefsItem.getMinimumAntiAliasSamples());
			display.createWindow(
					prefsItem.getWidth(), 
					prefsItem.getHeight(), 
					prefsItem.getBitDepth(), 
					prefsItem.getFrequency(), 
					prefsItem.getFullScreen());
			

			display.getRenderer().setBackgroundColor(ColorRGBA.black);
		} catch (JmeException e) {
			log.log(Level.SEVERE, "Could not create displaySystem", e);
			System.exit(1);
		}

		configureAndStartTableInput();
		cursorSystem.setPickSystem(pickSystem);

		InputUtility.setupKeys();		

		timer = Timer.getTimer();		
	}

	protected final void initGame() {		
		instance = this;
		display.setTitle("SynergyNet Desktop");

		GameStateManager.create();
		try {
			DesktopTypeXMLReader.loadFromConfiguration(Resources.getResourceAsStream(configFile));
			ApplicationRegistryXMLReader.loadFromConfiguration(Resources.getResourceAsStream(configFile), registry);
			ServiceManager.getInstance().loadFromConfiguration(Resources.getResourceAsStream(configFile));

			ApplicationInfo defaultApp = registry.getDefaultApp();

			if(DesktopTypeXMLReader.splashScreenEnabled) {
				ApplicationInfo splashScreen = registry.getInfoForClassName("synergynet.table.apps.splasher.SplasherApp");
				SplasherApp splasherApp = (SplasherApp)registry.getAppInstance(splashScreen);
				splasherApp.setNextApp(defaultApp);				
				URL url = Resources.getResource(DesktopTypeXMLReader.splashScreenResource);
				splasherApp.setSplashImageURL(url, 1024);
				try {
					taskManager.setActiveApplication(splashScreen);
				} catch (ApplicationControlError e) {
					e.printStackTrace();
				}
				splasherApp.splash();
			}else{
				try {
					taskManager.setActiveApplication(defaultApp);
				} catch (ApplicationControlError e) {
					e.printStackTrace();
				}
			}

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}		
	}
	
	public int getDisplayWidth() {
		return display.getWidth();
	}
	
	public int getDisplayHeight() {
		return display.getHeight();
	}
	
	public int tableToScreenX(float x) {
		return (int)(getDisplayWidth() * x);
	}
	
	public int tableToScreenY(float y) {
		return (int)(getDisplayHeight() * y);
	}

	public void showMainMenu() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			taskManager.setActiveApplication(registry.getDefaultApp());
		} catch (ApplicationControlError e) {
			e.printStackTrace();
		}
	}

	protected void reinit() {
	}	

	protected void cleanup() {
		log.info("Cleaning up resources.");
		log.info("SynergyNet Services shutting down...");
		ServiceManager.getInstance().shutdown();
		log.info("Shutting down GameStateManager");
		GameStateManager.getInstance().cleanup();

		KeyInput.destroyIfInitalized();
		MouseInput.destroyIfInitalized();
		JoystickInput.destroyIfInitalized();
		if(new TableConfigPrefsItem().isAutoStartMTServer()){
			try {
				MIMProcessController.getInstance().kill();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.exit(0);
	}

	public static void exit() {
		instance.finish();
		log.info("Done.");		
	}

	private void configureAndStartTableInput() {
		TableConfigPrefsItem tablePrefs = new TableConfigPrefsItem();
		TableType tabletype = tablePrefs.getTableType();
		
		switch(tabletype) {
		
		case JMEDIRECT: {
			try {
				JMEDirectSimulator simulator = new JMEDirectSimulator(display.getWidth(), display.getHeight());
				multiTouchInputSource = simulator;
				MouseInput.get().setCursorVisible(true);
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			break;
		}
		
		case LUMIN: {
			try {
				LuminMultiTouchInput mti = new LuminMultiTouchInput();
				multiTouchInputSource = mti;
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			break;
		}
		
		case TUIO: {
			try {
				multiTouchInputSource = new TUIOMultiTouchInput();				
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			break;
		}
		
		case TUIOSIM: {
			try {
				multiTouchInputSource = new TUIOMultiTouchInput();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			AbstractMultiTouchSimulator simulator = new JMETUIOSimulator(display.getWidth(), display.getHeight());
			simulator.start();	
			break;
		}		
		}

		
		inputComponent = new MultiTouchInputComponent(multiTouchInputSource);
		inputComponent.registerMultiTouchEventListener(cursorSystem);
		
		try {
			loadFilters(inputComponent);
		} catch (BackingStoreException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	private void loadFilters(MultiTouchInputComponent inputComponent2) throws BackingStoreException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Preferences filterPrefs = Preferences.userNodeForPackage(IMultiTouchInputFilter.class);
		
		MultiTouchInputFilterManager filterManager = MultiTouchInputFilterManager.getInstance();
		filterManager.setPickingSystem(JMEMultiTouchPickSystem.getInstance());
		filterManager.setLoggingClass(this.getClass());

		Map<Integer,String> ordered = new HashMap<Integer, String>();

		// for each key-value pair, populate into map ready for ordering
		for(String key : filterPrefs.keys()) {
			int value = filterPrefs.getInt(key, -1);
			if(value > -1) {
				ordered.put(value, key);
			}
		}
		
		List<Integer> mapKeys = new ArrayList<Integer>(ordered.keySet());
		Collections.sort(mapKeys);
		
		// load in order, ignoring those switched off (index < 0)
		for(Integer i : mapKeys) {
			if(i > -1) {
				inputComponent.addMultiTouchInputFilter((IMultiTouchInputFilter)Class.forName(ordered.get(i)).newInstance());
			}
		}
	}

	public IJMEMultiTouchPicker getPickSystem() {
		return pickSystem;
	}

	public MultiTouchCursorSystem getCursorSystem() {
		return cursorSystem;
	}
	
	public static File getSynergyNetLocalWorkingDirectory() {
		String userHome = System.getProperty("user.home", ".");
		File workingDirectory = new File(".");
		String sysName = System.getProperty("os.name").toLowerCase();
		String dirName = APPNAME+VERSION_STRING;
		
		if(sysName.contains("windows")) {
			final String applicationData = System.getenv("APPDATA");
            if (applicationData != null)
                workingDirectory = new File(applicationData, "." + dirName + File.pathSeparatorChar);
            else
                workingDirectory = new File(userHome, '.' + dirName + File.pathSeparatorChar);
		}else if(sysName.contains("mac")) {
			workingDirectory = new File(userHome, "Library/Application Support/" + dirName);
		}else if(sysName.contains("solaris")) {
			workingDirectory = new File(userHome, '.' + dirName + File.pathSeparatorChar);
		}else if(sysName.contains("linux")) {
			// ?
		}else{
			workingDirectory = new File(".");
		}
		
		return workingDirectory;
	}

	public static void main(String[] args) {

		//set logging level
		LoggingConfigPrefsItem logPrefs = new LoggingConfigPrefsItem();
		LoggingLevel loggingLevel = logPrefs.getLoggingLevel();		
		Logger.getLogger("").setLevel(Level.parse(loggingLevel.name()));
	
		//Logger.getLogger(Node.class.getName()).setLevel(Level.OFF);

		try {
			log.info("Current working directory: " + new File(".").getCanonicalPath());
			log.info("Java version: " + System.getProperty("java.version"));
			log.info("Java vendor: " + System.getProperty("java.vendor"));
			log.info("Operating System: " + System.getProperty("os.name") + " ");
			log.info(System.getProperty("os.version" ) + " on " + System.getProperty("os.arch"));
			log.info("User: " + System.getProperty("user.name") + " in " + System.getProperty("user.dir"));
			log.info("Library path: " + System.getProperty("java.library.path"));
			log.info("Application data path: " + getSynergyNetLocalWorkingDirectory());

			Map<String, String> variables = System.getenv();  

			for (Map.Entry<String, String> entry : variables.entrySet())  
			{  
				String name = entry.getKey();  
				String value = entry.getValue();  
				log.info(name + "=" + value);  
			} 
		} catch (IOException e) {
			log.warning(e.toString());
		}

		if(args.length > 0) {
			configFile = args[0];
		}

		SynergyNetDesktop app = new SynergyNetDesktop();
		app.setConfigShowMode(ConfigShowMode.NeverShow);
		app.start();
	}

	

}
