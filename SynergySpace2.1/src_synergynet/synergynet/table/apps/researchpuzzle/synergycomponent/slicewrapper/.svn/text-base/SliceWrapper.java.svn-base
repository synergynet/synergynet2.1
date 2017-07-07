package synergynet.table.apps.researchpuzzle.synergycomponent.slicewrapper;

import java.util.HashMap;
import java.util.Map;

//import com.jme.image.Image;
//import com.jme.image.Texture;
//import com.jme.image.Texture2D;
//import com.jme.image.Texture.WrapMode;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.TexCoords;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
//import com.jme.util.CloneImportExport;

import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.Frame;
import synergynet.contentsystem.items.QuadContentItem;
import synergynet.contentsystem.items.listener.ScreenCursorListener;
import synergynet.table.apps.researchpuzzle.synergycomponent.SynergyComponent;

public class SliceWrapper {
	
	protected QuadContentItem rootItem;
	protected Vector2f startPoint = new Vector2f();
	protected Map<Long, SelectedArea> areas = new HashMap<Long, SelectedArea>();
	protected boolean isEnabled = false;
	protected SelectionWrapper selectionWrapper;
	
	private int minSliceWidth = 30, minSliceHeight = 30, maxSliceWidth = 500, maxSliceHeight = 500;
	
	public SliceWrapper(final QuadContentItem quadItem){
		this(quadItem, quadItem); 
	}	
	
	public SliceWrapper(final QuadContentItem quadItem, final QuadContentItem rootItem){
		this.rootItem = rootItem;
		selectionWrapper = new SelectionWrapper(quadItem, rootItem, 2);
		quadItem.addScreenCursorListener(new ScreenCursorListener(){

			@Override
			public void screenCursorChanged(ContentItem item, long id, float x,	float y, float pressure) {
				if(!isEnabled) return;
				
				if(areas.containsKey(id)){
					SelectedArea area = areas.get(id);
					area.getPoints().add(new Vector2f(x,y));
				}
			}

			@Override
			public void screenCursorClicked(ContentItem item, long id, float x,	float y, float pressure) {}

			@Override
			public void screenCursorPressed(ContentItem item, long id, float x,	float y, float pressure) {
				if(!isEnabled) return;
				SelectedArea area = new SelectedArea();
				area.getPoints().add(new Vector2f(x,y));
				areas.put(id, area);
			}

			@Override
			public void screenCursorReleased(ContentItem item, long id,	float x, float y, float pressure) {
				if(!isEnabled){
					areas.clear();
					return;
				}
				if(areas.containsKey(id)){
					SelectedArea area = areas.get(id);
					area.getPoints().add(new Vector2f(x,y));
					
					Vector3f v1 = area.getMinPoint();
					Vector3f v2 = area.getMaxPoint();
					
					Vector3f v1local = new Vector3f();
					Vector3f v2local = new Vector3f();

					((Quad)quadItem.getImplementationObject()).worldToLocal(v1, v1local);
					((Quad)quadItem.getImplementationObject()).worldToLocal(v2, v2local);
					v1local.addLocal(new Vector3f(quadItem.getWidth()/2, quadItem.getHeight()/2,0));
					v2local.addLocal(new Vector3f(quadItem.getWidth()/2, quadItem.getHeight()/2,0));
					v1local.setY(quadItem.getHeight()-v1local.getY());
					v2local.setY(quadItem.getHeight()-v2local.getY());
					Vector3f v21 = v2.subtract(v1);
					float distance = v2.distance(v1);
					float angle = v21.normalize().angleBetween(new Vector3f(FastMath.cos(quadItem.getParent().getAngle()),FastMath.sin(quadItem.getParent().getAngle()),0).normalizeLocal());
					
					float nw = FastMath.abs(distance * FastMath.cos(angle));
					float nh = FastMath.abs(distance * FastMath.sin(angle));
					
					if(nw>minSliceWidth && nw<maxSliceWidth && nh>minSliceHeight && nh<maxSliceHeight){
						Frame slice = (Frame) quadItem.getContentSystem().createContentItem(Frame.class);
						slice.setWidth((int)nw);
						slice.setHeight((int)nh);
						slice.setLocation((int)(v1.getX()+ ((v2.getX()-v1.getX())/2)), (int)(v2.getY()+ ((v1.getY()-v2.getY())/2)));
						slice.setOrder(9999);
						slice.setAngle(quadItem.getParent().getAngle());
						copyTexture(quadItem, slice, v1local, v2local);
						new SynergyComponent(slice, rootItem).getContainer().setOrder(quadItem.getParent().getOrder()+1);
					}
				}
				areas.clear();
			}
			
			private void copyTexture(QuadContentItem parentFrame, QuadContentItem sliceFrame, Vector3f start, Vector3f end){
				Quad parentQuad = ((Quad)parentFrame.getImplementationObject());
				Quad sliceQuad = ((Quad)sliceFrame.getImplementationObject());
				
				sliceQuad.setCullHint(parentQuad.getLocalCullHint());
				sliceQuad.setLightCombineMode(parentQuad.getLocalLightCombineMode());
		        sliceQuad.setRenderQueueMode(parentQuad.getLocalRenderQueueMode());
		        sliceQuad.setTextureCombineMode(parentQuad.getLocalTextureCombineMode());
		        
		        for (RenderState.StateType type : RenderState.StateType.values()) {
		            RenderState state = parentQuad.getRenderState( type );
		            
		            if (state != null) {
		            	if(state instanceof TextureState){
		            		TextureState ts = (TextureState) state;
		            		TextureState sliceTs = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	            			sliceTs.setTexture(ts.getTexture(0),0);
	            			float txtX1 = (int)Math.min(start.x, end.x) + (rootItem.getWidth()*parentQuad.getTextureCoords(0).coords.get(0));
	            			float txtY1 = (int)Math.min(start.y, end.y)+ (rootItem.getHeight()* (1-parentQuad.getTextureCoords(0).coords.get(1)));
	            			float txtX2 = (int)Math.max(start.x, end.x)+ (rootItem.getWidth()*parentQuad.getTextureCoords(0).coords.get(0));
	            			float txtY2 = (int)Math.max(start.y, end.y)+ (rootItem.getHeight()*(1-parentQuad.getTextureCoords(0).coords.get(1)));
            				sliceQuad.setTextureCoords(TexCoords.makeNew(partTexture(rootItem.getWidth(), rootItem.getHeight(), txtX1, txtY1, txtX2, txtY2)),0);
            				/*
            				if(ts.getNumberOfSetTextures()>1){
		            			for(int i=1; i< ts.getNumberOfSetTextures(); i++){
		            				Texture sliceTexture = new Texture2D();
		            				sliceTexture.setMinificationFilter(Texture.MinificationFilter.NearestNeighborNoMipMaps);
		            				sliceTexture.setMagnificationFilter(Texture.MagnificationFilter.Bilinear);
		            				sliceTexture.setWrap(WrapMode.Repeat);
		            				CloneImportExport ie = new CloneImportExport();
		            				ie.saveClone(ts.getTexture(i).getImage());
		            				Image clonedImage = (Image) ie.loadClone();
		            				sliceTexture.setImage(clonedImage);
		            				sliceTexture.setScale(new Vector3f(1, -1, 1));
		            				
		            				sliceTs.setTexture(sliceTexture,i);
		            				sliceTs.apply();
		            				sliceQuad.setTextureCoords(TexCoords.makeNew(partTexture(rootItem.getWidth(), rootItem.getHeight(), txtX1, txtY1, txtX2, txtY2)),i);
			            			sliceQuad.copyTextureCoordinates(0, i, 1.0f);
		            			}
		            		}
		            		*/
            				/*
            				sliceQuad.setTextureCoords(((Quad)rootItem.getImplementationObject()).getTextureCoords(0), sliceTs.getNumberOfSetTextures()-1);
                        	if(sliceTs.getNumberOfSetTextures()>1){
                        		for(int i=1; i< sliceTs.getNumberOfSetTextures(); i++){
                        			sliceQuad.copyTextureCoordinates(0, i, 1f);
                        		}
                    		}
                    		*/
		            		
		            		sliceQuad.setRenderState(sliceTs);
		            		sliceQuad.updateRenderState();
		           			sliceQuad.updateGeometricState(0, true);
		            		
		            	}else{
		            		sliceQuad.setRenderState(state );
		            	}
		            }
		        }
			}
			
			private Vector2f[] partTexture (int width, int height, float inix, float iniy, float finx, float finy){
				
				Vector2f[] coords = new Vector2f[4];
				coords[0] = new Vector2f();
				coords[1] = new Vector2f();
				coords[2] = new Vector2f();
				coords[3] = new Vector2f();
				
				coords[0].x = getUForPixel(inix, width);
		        coords[0].y = getVForPixel(iniy, height);

		        coords[1].x = getUForPixel(inix, width);
		        coords[1].y = getVForPixel(finy, height);

		        coords[2].x = getUForPixel(finx, width);
		        coords[2].y = getVForPixel(finy, height);

		        coords[3].x = getUForPixel(finx, width);
		        coords[3].y = getVForPixel(iniy, height);
	        	
		        return coords;
			}

			private float getUForPixel(float xPixel, float width) {
			    return (float) xPixel / width;
			}
			 
			private float getVForPixel(float yPixel, float height) {
			    return 1f - (float) yPixel / height;
			} 
		});
	}
	
	public boolean isSliceEnabled(){
		return this.isEnabled;
	}
	
	public void setSliceEnabled(boolean isEnabled){
		this.isEnabled = isEnabled;
		selectionWrapper.setAnnotateEnabled(isEnabled);
	}
	
	public void setMinSliceDimension(int minWidth, int minHeight){
		this.minSliceWidth = minWidth;
		this.minSliceHeight = minHeight;
	}
	
	public void setMaxSliceDimension(int maxWidth, int maxHeight){
		this.maxSliceWidth = maxWidth;
		this.maxSliceHeight = maxHeight;
	}
}
