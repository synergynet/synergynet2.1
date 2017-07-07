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

package synergynet.contentsystem.jme.items;

import java.awt.Color;
import java.awt.Font;
import java.nio.FloatBuffer;

import com.jme.bounding.LineBoundingBox;
import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.BlendState;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.BufferUtils;

import synergynet.contentsystem.ContentSystemUtils;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.LineItem;
import synergynet.contentsystem.items.implementation.interfaces.ILineImplementation;
import synergynet.contentsystem.items.utils.Location;
import synergynet.contentsystem.jme.UpdateableJMEContentItemImplementation;
import synergynet.contentsystem.jme.items.JMEOrthoContainer;
import synergynet.contentsystem.jme.items.utils.ArrowGeom;
import synergyspace.jme.cursorsystem.MultiTouchCursorSystem;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.jme.cursorsystem.elements.twod.OrthoBringToTop;
import synergyspace.jme.cursorsystem.elements.twod.OrthoCursorEventDispatcher;
import synergyspace.jme.cursorsystem.elements.twod.OrthoCursorEventDispatcher.CommonCursorEventListener;
import synergyspace.jme.gfx.twod.textbox.ChangeableTextLabel;
import synergyspace.jme.gfx.twod.utils.GraphicsImageQuad;
import synergyspace.mtinput.events.MultiTouchCursorEvent;



public class JMELineItem extends JMEOrthoContainer implements ILineImplementation, CommonCursorEventListener, UpdateableJMEContentItemImplementation{

	private LineItem item;
	
	private Node node;
	private static final long serialVersionUID = 1L;

	protected Line line;
	
	protected Vector3f sourcePoint;
	protected Vector3f targetPoint;
	protected Spatial sourceSpatial;
	protected Spatial targetSpatial;
	protected ArrowGeom arrowToSource, arrowToTarget;
	protected ColorRGBA lineColour = ColorRGBA.white;
	protected ColorRGBA textColour = ColorRGBA.white;
	protected StringBuffer text = new StringBuffer();
	protected GraphicsImageQuad textQuad;
	protected ChangeableTextLabel textLabel;
	
	protected boolean arrowsEnabled = true;
	protected boolean textEnabled = true;

	protected int lineMode = LineItem.CONNECTED_LINE;
	protected int arrowMode = LineItem.BIDIRECTIONAL_ARROWS;
	
	private float arrowWidth = 14;
	private float arrowHeight = 14;
	
	protected short defaultAnimationPattern =  (short)0xAAAA;
	protected float defaultAnimationDelay = 1;
	protected float animationDelay = defaultAnimationDelay;
	
	public JMELineItem(ContentItem contentItem){
		this(contentItem, null, null);
	}
	
	public JMELineItem(final ContentItem contentItem, Vector3f sourcePoint, Vector3f targetPoint) {
		super(contentItem);
		this.item = (LineItem)contentItem;
		this.node = ((Node)this.getImplementationObject());
		this.line = new Line(node.getName()+"_line", new Vector3f[]{sourcePoint, targetPoint}, null, new ColorRGBA[]{lineColour,  lineColour}, null);
		this.line.setModelBound(new LineBoundingBox());
		this.line.updateModelBound();
		new OrthoCursorEventDispatcher(line, node).addMultiTouchListener(new CommonCursorEventListener(){

			@Override
			public void cursorChanged(
					OrthoCursorEventDispatcher commonCursorEventDispatcher,
					ScreenCursor c, MultiTouchCursorEvent event) {
				item.fireCursorChanged(c.getID(), event.getPosition().x, event.getPosition().y, event.getPressure());
			}

			@Override
			public void cursorClicked(
					OrthoCursorEventDispatcher commonCursorEventDispatcher,
					ScreenCursor c, MultiTouchCursorEvent event) {
				Vector2f screenPos = MultiTouchCursorSystem.tableToScreen(event.getPosition());
				item.fireCursorClicked(c.getID(), screenPos.x, screenPos.y, event.getPressure());
			}

			@Override
			public void cursorPressed(
					OrthoCursorEventDispatcher commonCursorEventDispatcher,
					ScreenCursor c, MultiTouchCursorEvent event) {
				Vector2f screenPos = MultiTouchCursorSystem.tableToScreen(event.getPosition());
				item.fireCursorPressed(c.getID(), screenPos.x, screenPos.y, event.getPressure());
			}

			@Override
			public void cursorReleased(
					OrthoCursorEventDispatcher commonCursorEventDispatcher,
					ScreenCursor c, MultiTouchCursorEvent event) {
				Vector2f screenPos = MultiTouchCursorSystem.tableToScreen(event.getPosition());
				item.fireCursorReleased(c.getID(), screenPos.x, screenPos.y, event.getPressure());
			}

			@Override
			public void cursorRightClicked(
					OrthoCursorEventDispatcher commonCursorEventDispatcher,
					ScreenCursor c, MultiTouchCursorEvent event) {
				
			}});

		this.sourcePoint = sourcePoint;
		this.targetPoint = targetPoint;
		line.setLineWidth(item.getWidth());
		line.setAntialiased(true);

		configureLineMode(item.getLineMode());

		arrowsEnabled = item.isArrowEnabled();
		arrowMode = item.getArrowMode();

		node.attachChild(line);
	    
		BlendState blendedAlphaState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        blendedAlphaState.setBlendEnabled(true);
        blendedAlphaState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        blendedAlphaState.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        blendedAlphaState.setTestEnabled(false);
        blendedAlphaState.setEnabled(true);
        node.setRenderState(blendedAlphaState);
        node.updateModelBound();
        node.updateRenderState();
	}

	protected void initSpatial(){
		this.spatial.setModelBound(new LineBoundingBox());
		this.spatial.updateGeometricState(0f, true);
		this.spatial.updateModelBound();
	}
	
	private void reconstruct()	{
		if(sourcePoint != null && targetPoint != null && lineColour != null){

			FloatBuffer vectorBuff = BufferUtils.createVector3Buffer(2);
			FloatBuffer colorBuff = BufferUtils.createFloatBuffer(new ColorRGBA[]{lineColour, lineColour});
			BufferUtils.setInBuffer(sourcePoint, vectorBuff, 0);
			BufferUtils.setInBuffer(targetPoint, vectorBuff, 1);
			line.setLineWidth(item.getWidth());
			configureLineMode(item.getLineMode());
			line.reconstruct(vectorBuff, null, colorBuff, null);
			if(textEnabled)	
				configureLineAnnotation();
			if(arrowsEnabled)	
				configureArrows();
			node.setZOrder(0,true);
			node.updateModelBound();
			node.updateRenderState();
		}
	}
	
	private void configureArrows(){
		if(arrowMode == LineItem.ARROW_TO_SOURCE || arrowMode == LineItem.BIDIRECTIONAL_ARROWS){	
			if(arrowToSource == null){
				arrowToSource = new ArrowGeom(node.getName() + "_Arrow_To_Source",arrowWidth,arrowHeight, lineColour);
				arrowToSource.setModelBound(new OrthogonalBoundingBox());
				arrowToSource.updateModelBound();
				new OrthoBringToTop(arrowToSource, node);
				node.attachChildAt(arrowToSource, 0);
				arrowToSource.setZOrder(line.getZOrder()+1);
			}
			arrowToSource.setLocalTranslation(sourcePoint);
			positionAndRotate(arrowToSource, sourceSpatial, sourcePoint, targetPoint);
		}
		else{
			if(arrowToSource != null){
				arrowToSource.removeFromParent();
				arrowToSource = null;
			}
		}
		if(arrowMode == LineItem.ARROW_TO_TARGET || arrowMode == LineItem.BIDIRECTIONAL_ARROWS){
			if(arrowToTarget == null){
				arrowToTarget = new ArrowGeom(node.getName() + "_Arrow_To_Target",arrowWidth, arrowHeight, lineColour);
				arrowToTarget.setModelBound(new OrthogonalBoundingBox());
				arrowToTarget.updateModelBound();
//				new OrthoControlPointRotateTranslateScale(arrowToTarget, node);
				new OrthoBringToTop(arrowToTarget, node);
				node.attachChildAt(arrowToTarget,0);
				arrowToTarget.setZOrder(line.getZOrder()+1);
			}
			arrowToTarget.setLocalTranslation(targetPoint);
			positionAndRotate(arrowToTarget, targetSpatial, targetPoint, sourcePoint);
		}
		else{
			if(arrowToTarget != null){
				arrowToTarget.removeFromParent();
				arrowToTarget = null;
			}
		}
	}
	
	private void positionAndRotate(ArrowGeom arrow, Spatial connectedSpatial, Vector3f point1, Vector3f point2){
		Vector3f direction = point1.subtract(point2).normalize();
		float angle = direction.angleBetween(new Vector3f(0,1,0).normalize());
		if(direction.x > 0)
			angle = -angle;
		Quaternion q = new Quaternion();
		q.fromAngleAxis(angle, new Vector3f(0,0,1).normalize());
		arrow.setLocalRotation(q);
		
		if(connectedSpatial != null && connectedSpatial.getWorldBound() != null){

			OrthogonalBoundingBox obb = (OrthogonalBoundingBox) connectedSpatial.getWorldBound();
			
			Matrix3f m = connectedSpatial.getWorldRotation().toRotationMatrix();
			
			Vector3f p1 = m.mult(new Vector3f(obb.extent.x, obb.extent.y, 0)).add(connectedSpatial.getWorldTranslation());
			Vector3f p2 = m.mult(new Vector3f(obb.extent.x, -obb.extent.y, 0)).add(connectedSpatial.getWorldTranslation());
			Vector3f p3 = m.mult(new Vector3f(-obb.extent.x, -obb.extent.y, 0)).add(connectedSpatial.getWorldTranslation());
			Vector3f p4 = m.mult(new Vector3f(-obb.extent.x, obb.extent.y, 0)).add(connectedSpatial.getWorldTranslation());
			
			Vector3f intersectionPoint = find2DLineRectangleIntersection(point1, point2, p1, p2, p3, p4);
			
			if(intersectionPoint != null) arrow.setLocalTranslation(intersectionPoint);
			
		}
	}
	
	private Vector3f find2DLineRectangleIntersection(Vector3f point1, Vector3f point2, Vector3f p1,Vector3f p2, Vector3f p3,Vector3f p4){
		float shortest_distance = java.lang.Float.MAX_VALUE;
		Vector3f intersection_point = null;
		
		Vector3f int_p01 = this.findIntersection(p1, p2, point1, point2);
		if(int_p01 != null && point2.distance(int_p01) < shortest_distance){
			shortest_distance = point2.distance(int_p01);
			intersection_point = int_p01;
		}
		Vector3f int_p02 = this.findIntersection(p2, p3, point1, point2);
		if(int_p02 != null && point2.distance(int_p02) < shortest_distance){
			shortest_distance = point2.distance(int_p02);
			intersection_point = int_p02;
		}
		Vector3f int_p03 = this.findIntersection(p3, p4, point1, point2);
		if(int_p03 != null && point2.distance(int_p03) < shortest_distance){
			shortest_distance = point2.distance(int_p03);
			intersection_point = int_p03;
		}
		Vector3f int_p04 = this.findIntersection(p4, p1, point1, point2);
		if(int_p04 != null && point2.distance(int_p04) < shortest_distance){
			shortest_distance = point2.distance(int_p04);
			intersection_point = int_p04;
		}
		return intersection_point;
	}
	
	private Vector3f findIntersection(Vector3f p1,Vector3f p2, Vector3f p3,Vector3f p4) {  
		  float deg,len1,len2;  
		  float segmentLen1,segmentLen2;  
		  float ua,div;  

		  Vector3f d1 = p2.subtract(p1);
		  Vector3f d2 = p4.subtract(p3);
		  Vector3f d3 = p1.subtract(p3);

		  len1= d1.length(); 
		  len2= d2.length();  
		  
		  deg = d1.dot(d2)/ (len1 * len2);  
		  if(FastMath.abs(deg) == 1) return null; 
		  
		  div=d2.y*d1.x-d2.x*d1.y;  
		  ua=(d2.x*d3.y-d2.y*d3.x)/div;  
		  Vector3f pt= new Vector3f(p1.x+ua*d1.x, p1.y+ua*d1.y, 0);  
		  
		  d1 = pt.subtract(p1);
		  d2 = pt.subtract(p2);
		  segmentLen1= d1.length() + d2.length();  
		  
		  d1 = pt.subtract(p3);
		  d2 = pt.subtract(p4);
		  segmentLen2= d1.length() + d2.length();

		  if(FastMath.abs(len1-segmentLen1)>0.01 || FastMath.abs(len2-segmentLen2)>0.01)  
		    return null;  
		  return pt;  
	}
	
	private void configureLineAnnotation(){
		if(text != null){
			if(textQuad == null){	
				textQuad = new GraphicsImageQuad(node.getName()+"_text_quad",1,1);
				node.attachChild(textQuad);
				textQuad.setZOrder(line.getZOrder()+1);
			}
			int textHeight = ContentSystemUtils.getFontHeight(item.getTextFont());
			int textWidth =  ContentSystemUtils.getStringWidth(item.getTextFont(), text.toString());
			int textDescent = ContentSystemUtils.getFontDescent(item.getTextFont());
			textQuad.updateGeometry(textWidth, textHeight);
			if(textWidth < 2) textWidth = 2;
			if(textHeight < 2) textHeight = 2;
			textQuad.recreateImageForSize(textWidth, textHeight);
			textQuad.getImageGraphics().setColor(item.getTextColour());
			textQuad.getImageGraphics().setFont(item.getTextFont());
			textQuad.getImageGraphics().drawString(text.toString(), textQuad.getImageWidth()/2 - textWidth/2, textQuad.getImageHeight()/2 + textHeight / 2 - textDescent);
			textQuad.updateGraphics();

			Vector3f temp1 = targetPoint.subtract(sourcePoint);
			Vector3f lineCenter = new Vector3f(sourcePoint.x + (temp1.x/2), sourcePoint.y + (temp1.y/2) + 20, 0);
			textQuad.setLocalTranslation(lineCenter);
		}
	}
	
	private void configureLineMode(int lineMode){
		if(lineMode == LineItem.CONNECTED_LINE){
			line.setMode(Line.Mode.Connected);
			line.setStipplePattern((short)0xFFFF);
			line.setStippleFactor(0);
		}
		else if(lineMode == LineItem.SEGMENT_LINE){
			line.setMode(Line.Mode.Segments);
			line.setStipplePattern((short)0xAAAA);
			line.setStippleFactor(10);
		}
		
		else if(lineMode == LineItem.DOTTED_LINE){
			line.setMode(Line.Mode.Segments);
			line.setStipplePattern((short)0xAAAA);
			line.setStippleFactor(2);
		} else if(lineMode == LineItem.ANIMATION){
			line.setMode(Line.Mode.Segments);
			line.setStipplePattern(defaultAnimationPattern);
			line.setStippleFactor(10);
		}
		
	}
	
	public Line getLine(){
		return line;
	}

	@Override
	public void setAnnotationEnabled(boolean isEnabled) {
		this.textEnabled = item.isAnnotationEnabled();
		this.reconstruct();
	}

	@Override
	public void setArrowMode(int arrowMode) {
		this.arrowMode = arrowMode;
		this.reconstruct();
	}

	@Override
	public void setArrowsEnabled(boolean isEnabled) {
		this.arrowsEnabled = isEnabled;
		this.reconstruct();
	}

	@Override
	public void setLineColour(Color lineColour) {
		this.lineColour = new ColorRGBA(lineColour.getRed(), lineColour.getGreen(), lineColour.getBlue(), lineColour.getAlpha());
		this.reconstruct();
	}

	@Override
	public void setLineMode(int lineMode) {
		this.lineMode = lineMode;
		this.reconstruct();
	}

	@Override
	public void setSourceItem(ContentItem sourceItem) {
		if(sourceItem != null){
			sourceSpatial = (Spatial)sourceItem.getImplementationObject();
			this.reconstruct();
		}
	}

	@Override
	public void setSourceLocation(Location sourceLocation) {
		if(sourceLocation != null){	
			sourcePoint = new Vector3f(sourceLocation.x, sourceLocation.y, 0);
			this.reconstruct();
		}
	}

	@Override
	public void setTargetItem(ContentItem targetItem) {
		if(targetItem != null){
			targetSpatial = (Spatial)targetItem.getImplementationObject();
			this.reconstruct();
		}
	}

	@Override
	public void setTargetLocation(Location targetLocation) {
		if(targetLocation != null){	
			targetPoint = new Vector3f(targetLocation.x, targetLocation.y, 0);
			this.reconstruct();
		}
	}

	@Override
	public void setText(String str) {
		text.setLength(0);
		text.append(str);
		this.reconstruct();
	}

	@Override
	public void setTextColour(Color textColour) {
		this.textColour = new ColorRGBA(textColour.getRed(), textColour.getGreen(), textColour.getBlue(), textColour.getAlpha());
		this.reconstruct();
	}

	@Override
	public void setTextFont(Font textFont) {
		this.reconstruct();
	}

	@Override
	public void setWidth(float lineWidth) {
		this.reconstruct();
	}

	@Override
	public Spatial getSpatial() {
		// TODO Auto-generated method stub
		return line;
	}
	
	public void update(float interpolation) {
		super.update(interpolation);
		if(item.getLineMode() == LineItem.ANIMATION && animationDelay <0){	
			line.setStipplePattern(defaultAnimationPattern);
			if(defaultAnimationPattern == (short)0xAAAA) defaultAnimationPattern = (short)0x5555;
			else defaultAnimationPattern = (short)0xAAAA;
			animationDelay = defaultAnimationDelay;
		}
		animationDelay-=interpolation*2;
	}
}
