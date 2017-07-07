/* Copyright (c) 2008 University of Durham, England
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

package synergynet.contentsystem.items.implementation.interfaces;

import java.util.List;

import com.jme.scene.Node;

import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.listener.BringToTopListener;
import synergynet.contentsystem.items.listener.ItemListener;
import synergynet.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener;
import synergynet.contentsystem.items.listener.OrthoFlickListener;
import synergynet.contentsystem.items.listener.OrthoSnapListener;
import synergyspace.jme.cursorsystem.fixutils.FixLocationStatus;
import synergynet.contentsystem.items.listener.ScreenCursorListener;

public interface IOrthoContentItemImplementation extends IContentItemImplementation{
	public void setOrder(int zOrder);

	public void setRotateTranslateScalable(boolean isEnabled);
	public void setRotateTranslateScalable(boolean isEnabled, boolean attachToParent, ContentItem targetItem);

	public void allowMoreThanTwoToRotateAndScale(boolean b);
	public void allowSingleTouchFreeMove(boolean b);
	public void setScaleLimit(float min, float max);
	public void setZRotateLimit(float min, float max);

	public void setBringToTopable(boolean isEnabled);
	public void setAsTopObject();
	public void setAsBottomObject();

	public void setAsTopObjectAndBroadCastEvent();

	public void setSingleTouchRotateTranslate(boolean isEnabled) throws Exception;

	public void setSnapable(boolean isEnabled);
	public void setFixLocations(List<FixLocationStatus> fixLocations);
	public void setTolerance(float tolerance);
	public void setRightClickDistance(float distance);
	public void allowSnapToOccupiedLocation(boolean allowSnapToOccupiedLocation);

	public void centerItem();
	public void placeRandom();
	public void rotateRandom();

	public void addItemListener(ItemListener itemListener);
	public void removeItemListerner(ItemListener itemListener);
	public void addOrthoControlPointRotateTranslateScaleListener(OrthoControlPointRotateTranslateScaleListener l);
	public void removeOrthoControlPointRotateTranslateScaleListeners(OrthoControlPointRotateTranslateScaleListener l);
	public void addBringToTopListener(BringToTopListener l);
	public void removeBringToTopListeners(BringToTopListener l);
	public void addSnapListener(OrthoSnapListener l);
	public void removeSnapListeners(OrthoSnapListener l);
	public void addFlickListener(OrthoFlickListener l);
	public void removeFlickListeners(OrthoFlickListener l);

	public void makeFlickable(float deceleration);
	public void makeUnflickable();
	public boolean isFlickable();
	public void flick(float velocityX, float velocityY, float deceleration);

	public void enableBounce();
	public void disableBounce();
	public void makeSticky();
	public void makeUnsticky();
	public void makeReflective();
	public void makeUnreflective();
	public void makeBounceOffable(Node orthoNode);
	public void makeUnbounceOffable();

	public void makeSpinnable(float deceleration);
	public void makeUnspinnable();
	public boolean isSpinnable();
	public void spin(float rotVelocity, float deceleration);

	public void makeScaleMotionable(float deceleration);
	public void makeUnScaleMotionable();
	public boolean isScaleMotionable();
	public void scaleMotion(float scaleVelocity, float deceleration);
	public void setScaleMotionaLimits(float minScale, float maxScale);

	public void addScreenCursorListener(ScreenCursorListener l);
	public void removeScreenCursorListeners();
	public void turnOffEventDispatcher();

	public void setSteadfastValues();

}
