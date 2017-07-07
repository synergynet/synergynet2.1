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

package synergynet.table.apps.lightbox;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ImageTextLabel;
import synergynet.services.ServiceManager;
import synergynet.services.exceptions.CouldNotStartServiceException;
import synergynet.services.net.networkcontent.NetworkContentClientService;
import synergynet.table.SynergyNetAppUtils;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.menucontrol.HoldBottomLeftExit;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergynet.table.config.apikeys.APIKeysConfigPrefsItem;
import synergyspace.jme.cursorsystem.flicksystem.FlickSystem;
import synergyspace.jme.sysutils.CameraUtility;

public class Lightbox extends DefaultSynergyNetApp {


	private ImageLoader loader;
	private ContentSystem csys;

	public Lightbox(ApplicationInfo info) {
		super(info);
	}

	@Override
	public void addContent() {
		setMenuController(new HoldBottomLeftExit());
		csys = ContentSystem.getContentSystemForSynergyNetApp(this);
		new FlickrSearchTool(csys, this);
	}
	
	protected void loadFlickrImages(String searchTerms) {
		
		try {
			ServiceManager.getInstance().get(NetworkContentClientService.class);
		} catch (CouldNotStartServiceException e1) {
			e1.printStackTrace();
		}
		
		SynergyNetAppUtils.addTableOverlay(this);
		
		try {
			loader = new ImageLoader();
		} catch (CouldNotStartServiceException e1) {
			e1.printStackTrace();
		}
		APIKeysConfigPrefsItem prefs = new APIKeysConfigPrefsItem();
		Flickr f;
		try {
			f = new Flickr(prefs.getFlickrAPIKey(), prefs.getFlickrAPISecret(), new REST());
			PhotosInterface pi = f.getPhotosInterface();
			SearchParameters params = new SearchParameters();
			params.setText(searchTerms);
			int perPage = 3;
			int page = 1;
			PhotoList pl;
			pl = pi.search(params, perPage, page);
			for(int i = 0; i < pl.size(); i++) {
				Photo p = (Photo) pl.get(i);
				loader.addPhotoToLoad(new URL(p.getSmallUrl()));
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FlickrException e) {
			e.printStackTrace();
		}
		
	}

	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(loader != null) {
			URL photo = loader.getNextPhotoToShow();
			if(photo != null) {
				addPhoto(photo);
			}
		}
		FlickSystem.getInstance().update(tpf);
	}

	private void addPhoto(URL photo) {		
		ImageTextLabel itl = (ImageTextLabel) csys.createContentItem(ImageTextLabel.class);		
		itl.setImageInfo(photo);
		itl.setWidth(250);
		itl.placeRandom();
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
	
}
