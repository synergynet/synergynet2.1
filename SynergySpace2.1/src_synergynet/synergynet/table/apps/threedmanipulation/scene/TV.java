package synergynet.table.apps.threedmanipulation.scene;

import java.net.URL;

import org.llama.jmf.JMFVideoImage;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.VideoPlayer;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class TV extends Node{

	private static final long serialVersionUID = 6173587082857220150L;
	
	protected JMFVideoImage jMEVideoImage;
	protected VideoPlayer videoPlayer;
	protected float frameWidth;
	protected Quad videoQuad;
	protected URL tvFrameTexture;
	protected ContentSystem contentSystem;
	
	
	public TV(ContentSystem contentSystem, URL videoURL, float frameWidth, URL tvFrameTexture){
		this.frameWidth = frameWidth;
		this.tvFrameTexture = tvFrameTexture;
		this.contentSystem = contentSystem;
		
		buildVideo(contentSystem, videoURL);
		
		this.setLocalScale(0.036f);
		
		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(FastMath.PI/2f, new Vector3f(0, 1, 0));
		this.setLocalRotation(tq);		
		this.updateGeometricState(0f, false);
	}
	
	public void buildVideo(ContentSystem contentSystem, URL videoURL){
		videoPlayer = (VideoPlayer)contentSystem.createContentItem(VideoPlayer.class);
		videoPlayer.setVideoURL(videoURL);
		videoQuad = (Quad)(videoPlayer.getImplementationObject());
		videoQuad.setLocalTranslation(0, 0, frameWidth/2-0.5f);
		videoPlayer.setPlaying(true);
		//Node parentNodeofVideoPlayer = videoQuad.getParent();
		this.attachChild(videoQuad);
		//parentNodeofVideoPlayer.detachChild(videoQuad);
		
		//buildFrame();
					
	}
	
	public void buildFrame(){
		float width = videoPlayer.getVideoWidth();
		float height = videoPlayer.getVideoHeight();
		
		Vector3f min = new Vector3f(-(width+2*frameWidth)/2, -frameWidth/2, -frameWidth/2);
		Vector3f max = new Vector3f((width+2*frameWidth)/2, frameWidth/2, frameWidth/2);
		final Box top = new Box(name+"top frame", min, max);
		top.setLocalTranslation(new Vector3f(0f, height/2+frameWidth/2, 0));
		top.setModelBound(new BoundingBox());
		top.updateModelBound();
		this.attachChild(top);	
		
		final Box bottom = new Box(name+"bottom frame", min, max);
		bottom.setLocalTranslation(new Vector3f(0, -(height/2+frameWidth/2), 0));
		bottom.setModelBound(new BoundingBox());
		bottom.updateModelBound();
		this.attachChild(bottom);
		
		min = new Vector3f(-frameWidth/2, -(height+2*frameWidth)/2, -frameWidth/2);
		max = new Vector3f(frameWidth/2, (height+2*frameWidth)/2, frameWidth/2);
		
		final Box left = new Box(name+"left frame", min, max);
		left.setLocalTranslation(new Vector3f(-(width/2+frameWidth/2)/2, 0, 0));
		left.setModelBound(new BoundingBox());
		left.updateModelBound();
		this.attachChild(left);
		
		final Box right = new Box(name+"right frame", min, max);
		right.setLocalTranslation(new Vector3f((width/2+frameWidth/2)/2, 0, 0));
		right.setModelBound(new BoundingBox());
		right.updateModelBound();
		this.attachChild(right);
		
		TextureState ts;
		Texture texture;	
		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);		
		texture  = TextureManager.loadTexture(this.tvFrameTexture, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setApply(ApplyMode.Replace);
		ts.setTexture( texture );	
		ts.apply();
		
		top.setRenderState( ts );	
		top.updateRenderState();
		
		bottom.setRenderState( ts );	
		bottom.updateRenderState();
		
		left.setRenderState( ts );	
		left.updateRenderState();
		
		right.setRenderState( ts );	
		right.updateRenderState();				
	}
	
	public void update (float tpf){	
		if(contentSystem != null) contentSystem.update(tpf);				
	}
	
}
