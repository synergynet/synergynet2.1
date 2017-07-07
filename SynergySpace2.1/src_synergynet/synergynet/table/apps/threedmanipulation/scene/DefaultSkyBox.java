package synergynet.table.apps.threedmanipulation.scene;

import synergynet.Resources;

import com.jme.image.Texture;
import com.jme.scene.Skybox;
import com.jme.util.TextureManager;

public class DefaultSkyBox extends Skybox {

	private static final long serialVersionUID = 4131281165000957490L;
	
	public DefaultSkyBox(String name, float size){
		super(name, size, size, size);
		
		Texture north = TextureManager.loadTexture(
		        Resources.getResource(
		        "data/threedmanipulation/north.jpg"),
		        Texture.MinificationFilter.BilinearNearestMipMap,
		        Texture.MagnificationFilter.Bilinear);
		    Texture south = TextureManager.loadTexture(
		    	Resources.getResource(
		        "data/threedmanipulation/south.jpg"),
		        Texture.MinificationFilter.BilinearNearestMipMap,
		        Texture.MagnificationFilter.Bilinear);
		    Texture east = TextureManager.loadTexture(
		    	Resources.getResource(
		        "data/threedmanipulation/east.jpg"),
		        Texture.MinificationFilter.BilinearNearestMipMap,
		        Texture.MagnificationFilter.Bilinear);
		    Texture west = TextureManager.loadTexture(
		    	Resources.getResource(
		        "data/threedmanipulation/west.jpg"),
		        Texture.MinificationFilter.BilinearNearestMipMap,
		        Texture.MagnificationFilter.Bilinear);
		    Texture up = TextureManager.loadTexture(
		    	Resources.getResource(
		        "data/threedmanipulation/top.jpg"),
		        Texture.MinificationFilter.BilinearNearestMipMap,
		        Texture.MagnificationFilter.Bilinear);
		    Texture down = TextureManager.loadTexture(
		    	Resources.getResource(
		        "data/threedmanipulation/bottom.jpg"),
		        Texture.MinificationFilter.BilinearNearestMipMap,
		        Texture.MagnificationFilter.Bilinear);

		    this.setTexture(Skybox.Face.North, north);
		    this.setTexture(Skybox.Face.West, west);
		    this.setTexture(Skybox.Face.South, south);
		    this.setTexture(Skybox.Face.East, east);
		    this.setTexture(Skybox.Face.Up, up);
		    this.setTexture(Skybox.Face.Down, down);
		    this.preloadTextures();
	}
	
}
