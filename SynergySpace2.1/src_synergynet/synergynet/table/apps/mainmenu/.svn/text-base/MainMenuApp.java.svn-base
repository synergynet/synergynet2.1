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

package synergynet.table.apps.mainmenu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.scene.Geometry;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.ImageTextLabel;
import synergynet.contentsystem.items.listener.ItemEventAdapter;
import synergynet.table.SynergyNetAppUtils;
import synergynet.table.SynergyNetDesktop;
import synergynet.table.animationsystem.AnimationSystem;
import synergynet.table.animationsystem.animelements.AnimationSequence;
import synergynet.table.animationsystem.animelements.ApplicationActivator;
import synergynet.table.animationsystem.animelements.Fader;
import synergynet.table.animationsystem.animelements.MoveInConcentricCircles;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.ApplicationRegistry;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergynet.table.config.table.TableConfigPrefsItem;
import synergynet.table.config.table.TableConfigPrefsItem.MenuType;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.jme.cursorsystem.elements.MultiTouchButton;
import synergyspace.jme.cursorsystem.elements.MultiTouchButton.MultiTouchButtonListener;
import synergyspace.jme.gfx.twod.ImageQuadFactory;
import synergyspace.jme.sysutils.BorderUtility;
import synergyspace.mtinput.events.MultiTouchCursorEvent;

public class MainMenuApp extends DefaultSynergyNetApp implements MultiTouchButtonListener {	

	protected List<Geometry> menuItems;
	protected Map<String, MultiTouchButton> buttons = new HashMap<String, MultiTouchButton>();
	private int mode= 1; 
	private MoveInConcentricCircles moveCircle;
	private float dragAngle;
	private ImageTextLabel background;

	public MainMenuApp(ApplicationInfo info) {
		super(info);
	}
	
	@Override
	public void addContent() {
		
		setBorderAdaptationDefaultLimitOverride(true);
				
		if (new TableConfigPrefsItem().getMenuType() == MenuType.MANUAL){
			mode = 1;
		}else if (new TableConfigPrefsItem().getMenuType() == MenuType.COMBO){
			mode = 2;
		}
		
		SynergyNetAppUtils.addTableOverlay(this);
		
		ContentSystem contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		BorderUtility.enableScreenBoundary(contentSystem);
		
		if (mode == 1 || mode == 2){
			background = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
			background.setSteadfastLimit(0);			
			background.centerItem();
			background.setAsBottomObject();
			background.setBringToTopable(false);
			background.setAutoFit(false);
			background.setBorderSize(0);	
			background.setOrder(-100);
			background.setBackgroundColour(Color.black);
			background.setRotateTranslateScalable(false);
			background.setWidth(DisplaySystem.getDisplaySystem().getRenderer().getWidth());
			background.setHeight(DisplaySystem.getDisplaySystem().getRenderer().getHeight());
			background.setScale(1);
			background.setAngle(0);
			
			background.addItemListener(new ItemEventAdapter() {	
				private boolean down = false;
				private Vector2f origin = new Vector2f(background.getLocation().x + BorderUtility.getVirtualRectangleEnvironmentTranslationX(), 
						background.getLocation().y + BorderUtility.getVirtualRectangleEnvironmentTranslationY());
				private Vector2f touchDown = new Vector2f();
				private float lastAngle, angle = 0f;
				
				public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {
					down = true;
					touchDown = new Vector2f(x,y);
					lastAngle = 0;
				}
				
				public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {
					if (down){
						Vector2f touchDrag = new Vector2f(x,y);
						
						angle = new Vector2f(touchDrag.x - origin.x, touchDrag.y - origin.y).angleBetween(
								new Vector2f(touchDown.x - origin.x, touchDown.y - origin.y));
						
						dragAngle +=  lastAngle - angle;
						dragAngle = dragAngle % (FastMath.DEG_TO_RAD * 360);
						lastAngle = angle;
						
					}
				}
				
				public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {
					down = false;
				}
				
			});
			
		}
		
		menuItems = new ArrayList<Geometry>();
		ApplicationRegistry registry = SynergyNetDesktop.getInstance().getRegistry();
		
		Comparator<ApplicationInfo> comparator = new Comparator<ApplicationInfo>() {
			@Override
			public int compare(ApplicationInfo a, ApplicationInfo b) {
				return a.getApplicationName().compareTo(b.getApplicationName());
			}			
		};

		List<ApplicationInfo> sortableList = new ArrayList<ApplicationInfo>();
		for(ApplicationInfo ai : registry.getAllInfo()) {
			sortableList.add(ai);
		}
		Collections.sort(sortableList, comparator);
		
		for(ApplicationInfo ai : sortableList) {
			if(ai.showInMenus()) {
				Quad q = ImageQuadFactory.createQuadWithImageResource(ai.getApplicationName(), 128, ai.getTheClass().getResource(ai.getIconResourceName()), ImageQuadFactory.ALPHA_ENABLE);
				q.setLocalScale(0.5f);
				q.setModelBound(new OrthogonalBoundingBox());
				q.updateModelBound();
				menuItems.add(q);
				final MultiTouchButton mtb2 = new MultiTouchButton(q);
				buttons.put(ai.getApplicationName(), mtb2);
				mtb2.addButtonListener(this);
				mtb2.setProperty("apptype", ai.getApplicationType());
				orthoNode.attachChild(q);
			}
		}
		
		orthoNode.updateGeometricState(0f, true);
		orthoNode.updateModelBound();
		
		moveCircle = new MoveInConcentricCircles(menuItems, 64f);
		moveCircle.setRPM(0.12f);
		moveCircle.reset();	
		
		moveCircle.setMode(mode);
		
		if (mode == 1 || mode == 2){	
			background.update();
		}
		
		AnimationSystem.getInstance().add(moveCircle);
		
		Fader fadeIn = new Fader(menuItems, Fader.MODE_FADE_IN, 2);		

		AnimationSystem.getInstance().add(fadeIn);		
	}

	public void buttonClicked(MultiTouchButton button, ScreenCursor c, MultiTouchCursorEvent event) {
		setEnableButtons(false);
		ApplicationRegistry registry = SynergyNetDesktop.getInstance().getRegistry();		
		ApplicationInfo ai = registry.getInfoForName(button.getName(), (String) button.getProperty("apptype"));		
		AnimationSequence seq = new AnimationSequence();
		seq.addAnimationElement(new Fader(menuItems, Fader.MODE_FADE_OUT, 1));
		seq.addAnimationElement(new ApplicationActivator(ai));	
		AnimationSystem.getInstance().add(seq);
	}

	private void setEnableButtons(boolean b) {
		synchronized(buttons) {
			for(MultiTouchButton btn : buttons.values()) {
				btn.setActive(b);
			}
		}
		
	}

	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(mode == 1 || mode == 2){
			if (moveCircle != null){
				moveCircle.setDragAngle(dragAngle);
			}			
		}
	}	


	@Override
	public void onActivate() {
		super.onActivate();
		setEnableButtons(true);
		Fader fadeIn = new Fader(menuItems, Fader.MODE_FADE_IN, 2);
		AnimationSystem.getInstance().add(fadeIn);		
	}



	public void buttonDragged(MultiTouchButton button, ScreenCursor c, MultiTouchCursorEvent event) {}
	public void buttonPressed(MultiTouchButton button, ScreenCursor c, MultiTouchCursorEvent event) {}
	public void buttonReleased(MultiTouchButton button, ScreenCursor c, MultiTouchCursorEvent event) {}
}
