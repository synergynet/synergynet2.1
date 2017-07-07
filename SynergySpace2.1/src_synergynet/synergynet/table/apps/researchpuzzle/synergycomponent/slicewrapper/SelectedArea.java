package synergynet.table.apps.researchpuzzle.synergycomponent.slicewrapper;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;

public class SelectedArea {
	
	protected long id;
	protected List<Vector2f> points = new ArrayList<Vector2f>();
	
	public SelectedArea(){}
	
	public SelectedArea(long id){
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public List<Vector2f> getPoints(){
		return points;
	}
	
	public void setPoints(List<Vector2f> points){
		this.points = points;
	}

	public Vector3f getMinPoint() {
		float minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE; 
		for(Vector2f point: points){
				if(point.getX()< minX) minX = point.getX();
				if(point.getY()< minY) minY = point.getY();
		}
		return new Vector3f(minX, minY,0);
	}
	
	public Vector3f getMaxPoint() {
		float maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE; 
		for(Vector2f point: points){
				if(point.getX()> maxX) maxX = point.getX();
				if(point.getY()> maxY) maxY = point.getY();
		}
		return new Vector3f(maxX, maxY,0);
	}
}
