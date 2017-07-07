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

package synergynet.contentsystem.jme;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.Updateable;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.OrthoContainer;
import synergynet.contentsystem.items.OrthoContentItem;
import synergynet.contentsystem.jme.items.factory.JMEImplementationItemFactory;
import synergyspace.jme.cursorsystem.MultiTouchElementRegistry;
import synergyspace.jme.cursorsystem.elements.twod.OrthoBringToTop;
import synergyspace.jme.cursorsystem.elements.twod.ClipRegistry;
import synergyspace.jme.cursorsystem.flicksystem.FlickSystem;
import synergyspace.jme.cursorsystem.scalesystem.ScaleMotionSystem;
import synergyspace.jme.cursorsystem.spinsystem.SpinSystem;
import synergyspace.jme.sysutils.BorderUtility;

public class JMEContentSystem extends ContentSystem {

	private static final Logger log = Logger.getLogger(JMEContentSystem.class.getName());	
	
	protected Node orthoNode;
	protected Map<Spatial, ContentItem> spatialToContentItem = new ConcurrentHashMap<Spatial, ContentItem>();
	protected Map<String,UpdateableJMEContentItemImplementation> forUpdating = new HashMap<String,UpdateableJMEContentItemImplementation>();


	public JMEContentSystem(final Node orthoNode) {
		this.orthoNode = orthoNode;
		this.implementationItemFactory = new JMEImplementationItemFactory();
		BorderUtility.enableScreenBoundary(this);
		FlickSystem.activateBorder();
	}

	public JMEContentSystem(){
		this.implementationItemFactory = new JMEImplementationItemFactory();
	}

	/**
	 * Create a content item where the name will be auto-generated. The content item will be a class that
	 * extends ContentItem. You may use the default set of ContentItem classes
	 * found in synergyspace.contentsystem.items or use your own. If you use your own
	 * content item, the content item implementation must live in the same package as
	 * the content item.
	 */
	@Override
	public ContentItem createContentItem(Class<? extends ContentItem> contentItemType) {
		String itemName = this.generateUniqueName();
		return createContentItem(contentItemType, itemName);
	}

	/**
	 * Create a content item with a specific name. The content item will be a class that
	 * extends ContentItem. You may use the default set of ContentItem classes
	 * found in synergyspace.contentsystem.items or use your own. If you use your own
	 * content item, the content item implementation must live in the same package as
	 * the content item.
	 */
	@Override
	public ContentItem createContentItem(Class<? extends ContentItem> contentItemType, String itemName) {
		Object obj=null;

		try {
			Constructor<?> con = contentItemType.getConstructor(ContentSystem.class, String.class);
			obj = con.newInstance(this, itemName);

		}catch (SecurityException e) {
			log.severe(e.toString());
		} catch (NoSuchMethodException e) {
			log.severe(e.toString());
		} catch (IllegalArgumentException e) {
			log.severe(e.toString());
		} catch (InstantiationException e) {
			log.severe(e.toString());
		} catch (IllegalAccessException e) {
			log.severe(e.toString());
		} catch (InvocationTargetException e) {
			log.severe(e.toString());
		}

		if (obj==null)
			return null;
		
		log.info("ContentItem-"+contentItemType.getName()+" is created.");
		
		ContentItem contentItem = (ContentItem)obj;
		this.addContentItem(contentItem);
		
		return contentItem;
	}

	@Override
	public void removeContentItem(ContentItem contentItem) {
		this.removeContentItem(contentItem, true);
	}
	
	@Override
	public void removeContentItem(ContentItem contentItem, boolean releaseTextures){
		log.info("Start to remove ContentItem - "+contentItem.getClass().getName());
		if(contentItem instanceof OrthoContainer){
			OrthoContainer container = (OrthoContainer) contentItem;
			for(ContentItem subItem: container.getAllItemsIncludeSystemItems())
				removeContentItem(subItem, releaseTextures);
			container.getAllItemsIncludeSystemItems().clear();
			log.info("Remove all sub contentitems of ContentItem - "+contentItem.getClass().getName());
		}
		Spatial spatial = (Spatial)(contentItem.getImplementationObject());
		if(contentItem instanceof OrthoContentItem){
			OrthoContentItem item = ((OrthoContentItem)contentItem);
			item.setRotateTranslateScalable(false);
			item.setSnapable(false);
			item.turnOffEventDispatcher();
	 		OrthoBringToTop.unRegister(spatial);
	 		if(item.isFlickable()) item.makeUnflickable();
	 		if(item.isSpinnable()) item.makeUnspinnable();
	 		if(item.isScaleMotionable()) item.makeUnScaleMotionable();

		}
		MultiTouchElementRegistry.getInstance().unregisterElementsForSpatial(spatial);
		OrthoBringToTop.unRegister(spatial);
		ClipRegistry.getInstance().unregister(spatial);
		this.spatialToContentItem.remove(spatial);
		if(releaseTextures){
			RenderState rs = spatial.getRenderState(RenderState.StateType.Texture);
			if(rs != null){
				TextureManager.deleteTextureFromCard(((TextureState)rs).getTexture());
				TextureManager.releaseTexture(((TextureState)rs).getTexture());
			}
			log.info("Release texture attached to the contentItem.");
		}
		spatial.removeFromParent();
		this.contentItems.remove(contentItem.getName());
		log.info("The ContentItem - "+contentItem.getClass().getName()+" is removed");
	}
	
	public void setallSteadfastValues(){
		Iterator <ContentItem> iter = this.contentItems.values().iterator();
		while(iter.hasNext()){
			ContentItem item = iter.next();
			if(item instanceof OrthoContentItem){
				OrthoContentItem orthoItem = ((OrthoContentItem)item);
				orthoItem.setSteadfastValues();
			}
		}

	}

	public void removeAllContentItems(){
		Iterator <ContentItem> iter = this.contentItems.values().iterator();
		while(iter.hasNext()){
			ContentItem item = iter.next();
			Spatial spatial = (Spatial)(item.getImplementationObject());
			if(item instanceof OrthoContentItem){
				OrthoContentItem orthoItem = ((OrthoContentItem)item);
				orthoItem.setRotateTranslateScalable(false);
				orthoItem.setSnapable(false);
				orthoItem.turnOffEventDispatcher();
		 		OrthoBringToTop.unRegister(spatial);
		 		if(orthoItem.isFlickable()) orthoItem.makeUnflickable();
		 		if(orthoItem.isSpinnable()) orthoItem.makeUnspinnable();
		 		if(orthoItem.isScaleMotionable()) orthoItem.makeUnScaleMotionable();

			}
	 		this.spatialToContentItem.remove(spatial);
	 		RenderState rs = spatial.getRenderState(RenderState.StateType.Texture);
			if(rs != null){
				TextureManager.deleteTextureFromCard(((TextureState)rs).getTexture());
				TextureManager.releaseTexture(((TextureState)rs).getTexture());
			}
			spatial.removeFromParent();
		}
		this.contentItems.clear();
		BorderUtility.borderReset(orthoNode);		
		FlickSystem.clearbouncers();
		FlickSystem.activateBorder();
		
		log.info("All content items are removed.");
	}

	public ContentItem getContentItem(Spatial spatial){
		return this.spatialToContentItem.get(spatial);
	}

	@Override
	public boolean isTopLevelContainer(Object item) {
		Spatial spatial = (Spatial)(item);
		if (spatial.getParent().hashCode() == orthoNode.hashCode())
			return true;
		return false;
	}


	@Override
	public void update(float tpf) {
		for(UpdateableJMEContentItemImplementation item : forUpdating.values()) {
			item.update(tpf);
		}

		for (ContentItem item:this.contentItems.values()){
			item.update(tpf);
		}
		FlickSystem.getInstance().update(tpf);
		SpinSystem.getInstance().update(tpf);
		ScaleMotionSystem.getInstance().update(tpf);

		for (Updateable l:this.updateableItems){
			l.update(tpf);
		}
	}

	@Override
	public int getScreenHeight() {
		return DisplaySystem.getDisplaySystem().getHeight();
	}

	@Override
	public int getScreenWidth() {
		return DisplaySystem.getDisplaySystem().getWidth();
	}

	@Override
	public void addContentItem(ContentItem contentItem) {
		contentItem.bindImplementationOjbect();
		Spatial spatial = (Spatial)(contentItem.getImplementationObject());
		orthoNode.attachChild(spatial);
		orthoNode.updateGeometricState(0f, true);
		orthoNode.updateModelBound();
		contentItem.initImplementationObjet();
		contentItem.setContentSystem(this);
		contentItem.update();
		if(contentItems.get(contentItem.getName()) == null) this.contentItems.put(contentItem.getName(), contentItem);
		this.spatialToContentItem.put(spatial, contentItem);
		
		log.info("ContentItem - "+contentItem.getClass().getName()+" is added to the ContentSystem.");
	}

	@Override
	public void setItemName(ContentItem item, String newName) {
		if(this.getContentItem(newName) != null) throw new IllegalArgumentException("An item with the same name already exists in the content system");
		String oldName = item.getName();
		Spatial spatial = (Spatial)item.getImplementationObject();
		boolean isRTS = false, isSnapable = false, isFlickable = false, isSpinnable = false, isScaleMotionable = false;
		if(item instanceof OrthoContentItem){
			OrthoContentItem orthoItem = ((OrthoContentItem)item);
			isRTS = orthoItem.isRotateTranslateScaleEnabled();
			isSnapable = orthoItem.isSnapEnabled(); 
			isFlickable = orthoItem.isFlickable();
			isSpinnable = orthoItem.isSpinnable();
			isScaleMotionable = orthoItem.isScaleMotionable();
			
			orthoItem.setRotateTranslateScalable(false);
			orthoItem.setSnapable(false);
			orthoItem.turnOffEventDispatcher();
	 		OrthoBringToTop.unRegister(spatial);
	 		if(isFlickable) orthoItem.makeUnflickable();
	 		if(isSpinnable) orthoItem.makeUnspinnable();
	 		if(isScaleMotionable) orthoItem.makeUnScaleMotionable();
		}
		contentItems.remove(oldName);
		this.spatialToContentItem.remove(spatial);
		spatial.setName(newName);
		contentItems.put(newName, item);
		this.spatialToContentItem.put(spatial, item);
		
		if(item instanceof OrthoContentItem){
			OrthoContentItem orthoItem = ((OrthoContentItem)item);
			orthoItem.setRotateTranslateScalable(isRTS);
			orthoItem.setSnapable(isSnapable);
			if(isFlickable) orthoItem.makeFlickable(orthoItem.getFlickDeceleration());
	 		if(isSpinnable) orthoItem.makeSpinnable(orthoItem.getSpinDeceleration());
	 		if(isScaleMotionable) orthoItem.makeScaleMotionable(orthoItem.getScaleMotionDeceleration());		
		}
		
	}

	public Spatial getOrthoRootNode(){
		return this.orthoNode;
	}


}
