package synergynet.table.apps.researchpuzzle.synergycomponent.annotatewrapper;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;


import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.image.Texture.WrapMode;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jmex.awt.swingui.ImageGraphics;

import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.QuadContentItem;
import synergynet.contentsystem.items.listener.ItemListener;

public class HighlightWrapper {
	
	protected Texture texture;
	protected ImageGraphics graphics;
	protected BufferedImage drawImage;
	protected Graphics2D drawGfx;
	protected Map<Long, Point> lastPoint = new HashMap<Long, Point>();
	protected boolean isEnabled = false;
	protected Color color = Color.red;
	protected int strokeWidth = 5;
	
	public HighlightWrapper(QuadContentItem quadItem, final QuadContentItem rootItem){
		
		Quad quad = ((Quad)quadItem.getImplementationObject());
   		BlendState alpha = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
   		alpha.setEnabled( true );
  		alpha.setBlendEnabled( true );
   		alpha.setSourceFunction( BlendState.SourceFunction.SourceAlpha);
   		alpha.setDestinationFunction( BlendState.DestinationFunction.OneMinusSourceAlpha);
   		alpha.setTestEnabled( true );
  		alpha.setTestFunction( BlendState.TestFunction.GreaterThan);
  		quad.setRenderState( alpha );
    
  		recreateImageForQuad(quad, ((Quad)rootItem.getImplementationObject()));
  		
		drawImage = new BufferedImage(quadItem.getWidth() , quadItem.getHeight(), BufferedImage.TYPE_INT_ARGB);
		drawGfx = (Graphics2D)drawImage.getGraphics();
		drawGfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawGfx.setColor(color);
		drawGfx.setStroke(new BasicStroke(strokeWidth));

		quadItem.addItemListener(new ItemListener(){

			@Override
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {
				if(!isEnabled) return;
				Point p = lastPoint.get(id);		
				if(p == null) {
					p = new Point((int)x, (int)y);
					lastPoint.put(id, p);
				}
				drawGfx.drawLine(p.x, p.y, (int)x, (int)y);
				lastPoint.put(id, new Point((int)x, (int)y));
				HighlightWrapper.this.drawingFinished();
			}

			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}

			@Override
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {
				if(!isEnabled) return;
				lastPoint.put(id, new Point((int)x,(int)y));
				drawGfx.drawLine((int)x, (int)y, (int)x, (int)y);
				HighlightWrapper.this.drawingFinished();
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {
				if(!isEnabled) return;
				lastPoint.remove(id);
			}

			@Override
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
  		});
	}
	
	public void recreateImageForQuad(Quad quad, Quad rootQuad) {
		for (RenderState.StateType type : RenderState.StateType.values()) {
            RenderState state = quad.getRenderState( type );
            if(state instanceof TextureState){
            	TextureState ts = (TextureState) state;
            	ts.setEnabled(true);
            	ts.setCorrectionType(TextureState.CorrectionType.Perspective);		
            	texture = new Texture2D();
            	texture.setMinificationFilter(Texture.MinificationFilter.NearestNeighborNoMipMaps);
            	texture.setMagnificationFilter(Texture.MagnificationFilter.Bilinear);
            	texture.setWrap(WrapMode.Repeat);
            	graphics = ImageGraphics.createInstance((int)quad.getWidth(), (int)quad.getHeight(), 0);
            	enableAntiAlias( graphics );
            	texture.setImage( graphics.getImage() );
            	texture.setScale( new Vector3f( 1f, -1f, 1f ) );
            	ts.setTexture( texture, ts.getNumberOfSetTextures());
            	ts.apply();
            	quad.setTextureCoords(rootQuad.getTextureCoords(0), ts.getNumberOfSetTextures()-1);
            	/*
            	if(ts.getNumberOfSetTextures()>1){
            		for(int i=1; i< ts.getNumberOfSetTextures()-1; i++){
            			quad.copyTextureCoordinates(0, i, 1f);
            		}
        		}
    			*/
        		quad.setRenderState(ts);
        		quad.updateRenderState();
        		graphics.setColor(Color.white);
        		graphics.fillRect(0, 0, (int)quad.getWidth(), (int)quad.getHeight());
    			graphics.update(texture, false);
            }
		}
	}
	
	private void enableAntiAlias( Graphics2D graphics ) {
		RenderingHints hints = graphics.getRenderingHints();
		if ( hints == null ) {
			hints = new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		}
		else {
			hints.put( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		}
		graphics.setRenderingHints( hints );
	}
	
	protected void drawingFinished() {
		if(graphics != null && drawImage != null){
			graphics.drawImage(drawImage, 0,0, drawImage.getWidth(), drawImage.getHeight(), null);
		}
		graphics.update(texture, false);
	}
	
	public boolean isAnnotateEnabled(){
		return this.isEnabled;
	}
	
	public void setAnnotateEnabled(boolean isEnabled){
		this.isEnabled = isEnabled;
	}
	
	public void setColor(Color color){
		this.color = color;
		if(drawGfx != null) drawGfx.setColor(color);
	}
	
	public void setStrokeWidth(int strokeWidth){
		this.strokeWidth = strokeWidth;
		if(drawGfx != null) drawGfx.setStroke(new BasicStroke(strokeWidth));
	}
}
