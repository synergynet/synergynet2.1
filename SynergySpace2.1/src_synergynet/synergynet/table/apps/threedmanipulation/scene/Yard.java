package synergynet.table.apps.threedmanipulation.scene;

import java.net.URL;

import synergynet.Resources;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.WrapMode;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class Yard extends Node{

	private static final long serialVersionUID = -7253106690571705792L;
	
	protected String name;
	protected float length, width,height;
	protected URL floorTexture, wallTexture;
	protected Vector3f floorTextureScale, wallTextureScale;
	
	public Yard(String name, float length, float width, float height,
			URL floorTexture, Vector3f floorTextureScale, URL wallTexture, Vector3f wallTextureScale) {
		super();
		this.name = name;
		this.length = length;
		this.width = width;
		this.height = height;
		this.floorTexture = floorTexture;
		this.wallTexture = wallTexture;
		this.floorTextureScale = floorTextureScale;
		this.wallTextureScale = wallTextureScale;
		
		buildFloor();		
		buildWall(0, width, length, height);
		
		buildLogo();
	}
	
	private void buildFloor(){
		Vector3f min = new Vector3f(-width/2, -1, -length/2);
		Vector3f max = new Vector3f(width/2, 1, length/2);
		final Box floor = new Box(name+"floor", min, max);
		floor.setLocalTranslation(new Vector3f(0f, 0f, 0f));
		floor.setModelBound(new BoundingBox());
		floor.updateModelBound();
		
		TextureState ts;
		Texture texture;	
		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);		
		texture  = TextureManager.loadTexture(floorTexture,  Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setApply(ApplyMode.Replace);
		texture.setScale(this.floorTextureScale);
		ts.setTexture( texture );	
		ts.apply();
		
		floor.setRenderState( ts );	
		floor.updateRenderState();
		
		this.attachChild(floor);
				
	}
	
	private void buildWall(float floorCenter, float floorWidth, float floorLength, float wallHeight){	
		Vector3f min = new Vector3f(-floorWidth/2, -wallHeight/2, -1);
		Vector3f max = new Vector3f(floorWidth/2, wallHeight/2, 1);
		final Box north = new Box(name+"north wall", min, max);
		north.setLocalTranslation(new Vector3f(0f, wallHeight/2, floorCenter-floorLength/2));
		north.setModelBound(new BoundingBox());
		north.updateModelBound();
		this.attachChild(north);	
		
		final Box south = new Box(name+"south wall", min, max);
		south.setLocalTranslation(new Vector3f(0f, wallHeight/2, floorCenter+floorLength/2));
		south.setModelBound(new BoundingBox());
		south.updateModelBound();
		this.attachChild(south);
		
		min = new Vector3f(-1, -wallHeight/2, -floorLength/2);
		max = new Vector3f(1, wallHeight/2, floorLength/2);
		
		final Box east = new Box(name+"east wall", min, max);
		east.setLocalTranslation(new Vector3f(floorWidth/2, wallHeight/2, floorCenter));
		east.setModelBound(new BoundingBox());
		east.updateModelBound();
		this.attachChild(east);
		
		final Box west = new Box(name+"west wall", min, max);
		west.setLocalTranslation(new Vector3f(-floorWidth/2, wallHeight/2, floorCenter ));
		west.setModelBound(new BoundingBox());
		west.updateModelBound();
		this.attachChild(west);
		
		TextureState ts;
		Texture texture;	
		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);		
		texture  = TextureManager.loadTexture(wallTexture, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setApply(ApplyMode.Replace);
		texture.setScale(wallTextureScale);
		ts.setTexture( texture );	
		ts.apply();
		
		north.setRenderState( ts );	
		north.updateRenderState();
		
		south.setRenderState( ts );	
		south.updateRenderState();
		
		east.setRenderState( ts );	
		east.updateRenderState();
		
		west.setRenderState( ts );	
		west.updateRenderState();
		
	}
	
	private void buildLogo(){
		Vector3f min = new Vector3f(-10, -3, -1);
		Vector3f max = new Vector3f(10, 3, 1);
			
		final Box logo = new Box(name+ "logo", min, max);
		logo.setLocalTranslation(new Vector3f(0f, height/2+1, -length/2+1));
		logo.setModelBound(new BoundingBox());
		logo.updateModelBound();
		this.attachChild(logo);	
		
		TextureState ts;
		Texture texture;	
		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);	
		texture  = TextureManager.loadTexture(Resources.getResource(
        "data/threedmanipulation/logo.png"),  Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setScale( new Vector3f(1, 1f, 1 ) );
		ts.setTexture( texture );	
		ts.apply();
		
		logo.setRenderState( ts );	
		logo.updateRenderState();
	}
	

}
