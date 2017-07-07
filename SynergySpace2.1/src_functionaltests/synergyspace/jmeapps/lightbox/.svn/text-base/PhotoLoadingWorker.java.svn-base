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

import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.ImageIcon;

import synergyspace.jme.gfx.twod.ImageQuadFactory;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.util.TextureManager;

public class PhotoLoadingWorker implements Runnable {
	private Node attachPoint;

	protected List<URL> photosAwaitingLoading = new ArrayList<URL>();
	protected List<Texture> loadedTextures = new ArrayList<Texture>();

	private float width;

	private ZOrderManager manager = new ZOrderManager(-40f);
	

	public PhotoLoadingWorker(float width, Node attachPoint) {
		this.attachPoint = attachPoint;
		this.width = width;
		Thread t = new Thread(this);
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
	}

	public void run() {
		while(true) {
			checkAndLoadWaitingPhoto();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void addPhotoToLoad(URL photoURL) {
		synchronized(photosAwaitingLoading) {
			photosAwaitingLoading.add(photoURL);
		}
	}

	private void checkAndLoadWaitingPhoto() {
		URL url = null;

		synchronized(photosAwaitingLoading) {
			if(photosAwaitingLoading.size() > 0)
				url = photosAwaitingLoading.remove(0);
		}

		if(url != null) {
			ImageIcon imgIcon = new ImageIcon(url);
			
			synchronized(loadedTextures) {
				Texture t = TextureManager.loadTexture(imgIcon.getImage(), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, true);
				loadedTextures.add(t);
			}
		}
	}

	/**
	 * To be called from within jME main thread only
	 * @param interpolation
	 */
	public void update(float interpolation) {
		checkAndAddPhotos();
	}

	private void checkAndAddPhotos() {
		long start = System.currentTimeMillis();

		Texture t = null;
		// hmmm... with thread-safety in place, this takes way too long.
		//synchronized(loadedTextures) {
		try{
		if(loadedTextures.size() > 0)
			t = loadedTextures.remove(0);
		}catch(ConcurrentModificationException ex) {
			System.out.println("Concurrent modification exception in " + this.getClass().getName() + " ignored");
		}
		//}
		long end = System.currentTimeMillis();
		if(start - end > 0)
			System.out.println(end - start);
		
		if(t != null) {
			Long time = new Long(System.currentTimeMillis());
			Quad q = ImageQuadFactory.createQuadWithTexture(time.toString().substring(5, time.toString().length()-1), width, t);
			q.setModelBound(new BoundingBox());		
			q.updateModelBound();
			q.setLocalTranslation(FastMath.rand.nextFloat() * 20f, FastMath.rand.nextFloat() * 20f, 10f);
			Quaternion rot = new Quaternion();
			rot.fromAngles(0f, 0f, FastMath.rand.nextFloat() * FastMath.PI);
			q.setLocalRotation(rot);			
			attachPoint.attachChild(q);
			ControlPointRotateTranslateScaleFixedZ mt = new ControlPointRotateTranslateScaleFixedZ(q);
			mt.setPickMeOnly(true);
			ZOrderTrigger zot = new ZOrderTrigger(manager, q);
			zot.setPickMeOnly(true);
		}	
	}

}

