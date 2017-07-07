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

package synergyspace.jmeapps.lightbox;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;

import synergyspace.jme.JMEMultiTouchApplication;
import synergyspace.jme.abstractapps.AbstractMultiTouchThreeDApp;
import synergyspace.jme.config.AppConfig;

public class LightBox extends AbstractMultiTouchThreeDApp {

	public static final String PREFKEY_API_KEY = "PREFKEY_API_KEY";
	public static final String PREFKEY_API_SECRET = "PREFKEY_API_SECRET";
	public static final String PREFKEY_API_LASTSUCCESS = "PREFKEY_API_LASTSUCCESS";

	private static Preferences prefs = Preferences.userNodeForPackage(JMEMultiTouchApplication.class);
	protected static String apiKey = "";
	protected static String apiSecret = "";
	protected static boolean apiLastUseSuccess = false;

	protected PhotoLoadingWorker photoLoader;

	public static void main(String[] args) {
		prefs = Preferences.userNodeForPackage(LightBox.class);
		apiLastUseSuccess = prefs.getBoolean(PREFKEY_API_LASTSUCCESS, false);
		apiKey = prefs.get(PREFKEY_API_KEY, "");
		apiSecret = prefs.get(PREFKEY_API_SECRET, "");

		if(!apiLastUseSuccess) {
			getAPIInfo();
		}

		AppConfig.debugToolsFlag = AppConfig.INPUT_DEBUGTOOLS_ON;
		LightBox lb = new LightBox();
		lb.setConfigShowMode(ConfigShowMode.AlwaysShow);
		lb.start();
	}

	private static void getAPIInfo() {
		int result = JOptionPane.showConfirmDialog(new JFrame(), "Do you want to use flickr? (This requires you to provide API keys)", "Use flickr?", JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.OK_OPTION) {
			apiKey = JOptionPane.showInputDialog(new JFrame(), "Please enter your flickr api KEY", "API Key", JOptionPane.QUESTION_MESSAGE);
			apiSecret = JOptionPane.showInputDialog(new JFrame(), "Please enter your flickr api SECRET", "API Secret", JOptionPane.QUESTION_MESSAGE);
			apiLastUseSuccess = true;
		}
	}		

	public LightBox() {
		super();
	}

	public void update(float interpolation) {
		super.update(interpolation);
		if(photoLoader != null) photoLoader.update(interpolation);		
	}

	@Override
	protected void setupContent() {	
		photoLoader = new PhotoLoadingWorker(10f, rootNode);

		if(apiLastUseSuccess) {
			try {
				prefs.put(PREFKEY_API_KEY, apiKey);
				prefs.put(PREFKEY_API_SECRET, apiSecret);
				Flickr f = new Flickr(apiKey, apiSecret, new REST());
				PhotosInterface pi = f.getPhotosInterface();
				SearchParameters params = new SearchParameters();
				params.setText("landscape");
				int perPage = 3;
				int page = 1;
				PhotoList pl = pi.search(params, perPage, page);
				for(int i = 0; i < pl.size(); i++) {
					Photo p = (Photo) pl.get(i);
					photoLoader.addPhotoToLoad(new URL(p.getSmallUrl()));
				}
				prefs.putBoolean(PREFKEY_API_LASTSUCCESS, true);			

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				prefs.putBoolean(PREFKEY_API_LASTSUCCESS, false);
				e.printStackTrace();
			} catch (FlickrException e) {
				prefs.putBoolean(PREFKEY_API_LASTSUCCESS, false);
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				prefs.putBoolean(PREFKEY_API_LASTSUCCESS, false);
				e.printStackTrace();
			}
		}else{

			try {
				photoLoader.addPhotoToLoad(new File("1.jpg").toURI().toURL());
				photoLoader.addPhotoToLoad(new File("pillar.jpg").toURI().toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();

			}
		}
		
		rootNode.updateGeometricState(0f, true);
		rootNode.updateRenderState();
	}

	@Override
	protected void setupLighting() {

	}

	@Override
	protected void setupSystem() {

	}

}
