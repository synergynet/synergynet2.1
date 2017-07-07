package synergynet.table.apps.threedmanipulationExperiment.tools;

import java.util.ArrayList;
import java.util.List;

import synergynet.contentsystem.ContentSystem;
import synergynet.table.apps.threedmanipulation.listener.ToolListener;
import synergynet.table.apps.threemanipulationExperiment.logger.LogListener;

import com.jme.math.Vector2f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Line;

public class TouchPad {

	private TouchPadScreen monitorScreen;
	protected List<ToolListener> toolListeners = new ArrayList<ToolListener>();  
	private Node worldNode;
	private Node orthoNode;
	private Line line;
	protected List<LogListener> listeners = new ArrayList<LogListener>();
	  
	public void cleanup() {
		orthoNode.detachChild(monitorScreen);
		orthoNode.updateGeometricState(0f, false);
		
		worldNode.detachChild(line);
		worldNode.updateGeometricState(0f, false);
	}
	
	public TouchPad(String name, ContentSystem contentSystem, Node worldNode, Node orthoNode, float monitorWidth, Spatial manipulatabledOjbect, Vector2f initMonitorPosition){
		
		this.worldNode = worldNode;
		this.orthoNode = orthoNode;
		
		
		/*
		line =  new Line(name+"line");
			
		Vector3f cursorWorldStart = DisplaySystem.getDisplaySystem().getWorldCoordinates(new Vector2f(initMonitorPosition.x, initMonitorPosition.y), 0.9f);
		
		if(cursorWorldStart != null && manipulatabledOjbect != null){

			FloatBuffer vectorBuff = BufferUtils.createVector3Buffer(2);
			FloatBuffer colorBuff = BufferUtils.createFloatBuffer(new ColorRGBA[]{ColorRGBA.gray, ColorRGBA.white});
			BufferUtils.setInBuffer(cursorWorldStart, vectorBuff, 0);
			BufferUtils.setInBuffer(manipulatabledOjbect.getLocalTranslation(), vectorBuff, 1);
			line.setLineWidth(1f);
			line.reconstruct(vectorBuff, null, colorBuff, null);
			line.updateRenderState();
			line.updateGeometricState(0f, false);
		
		}
		
		this.worldNode.attachChild(line);
		worldNode.updateGeometricState(0f, false);
		*/
		 
		monitorScreen = new TouchPadScreen(name+"monitorScreen", contentSystem, monitorWidth, manipulatabledOjbect, line);
		orthoNode.attachChild(monitorScreen);
		monitorScreen.setLocalTranslation(initMonitorPosition.x, initMonitorPosition.y, 0);
		
		orthoNode.updateGeometricState(0f, false);
		orthoNode.updateRenderState();
		
		monitorScreen.addLogListener(new LogListener(){

			@Override
			public void taskCompleted(int touchNumber) {
				for (LogListener l: listeners)
					l.taskCompleted(touchNumber);			
			}
			
		});
			
	}
	
	public void setRotationSpeed(int rotationSpeed){
		monitorScreen.setRotationSpeed(rotationSpeed);
	}
	
	public void resetTouchNumber(){
		monitorScreen.resetTouchNumber();
	}
	
	public void updateLine(){
		
		/*
		Vector3f cursorWorldStart = DisplaySystem.getDisplaySystem().getWorldCoordinates(new Vector2f(monitorScreen.getLocalTranslation().x, monitorScreen.getLocalTranslation().y), 0.9f);
	
		if(cursorWorldStart != null && manipulatabledOjbect != null){

			FloatBuffer vectorBuff = BufferUtils.createVector3Buffer(2);
			FloatBuffer colorBuff = BufferUtils.createFloatBuffer(new ColorRGBA[]{ColorRGBA.gray, ColorRGBA.white});
			BufferUtils.setInBuffer(cursorWorldStart, vectorBuff, 0);
			BufferUtils.setInBuffer(manipulatabledOjbect.getLocalTranslation(), vectorBuff, 1);
			line.setLineWidth(1f);
			line.reconstruct(vectorBuff, null, colorBuff, null);
			line.updateRenderState();
			line.updateGeometricState(0f, false);
		
		}
		*/
	}
	
	public void setLocation(Vector2f location){
		monitorScreen.setLocalTranslation(location.x, location.y, 0);
		orthoNode.updateGeometricState(0f, false);
	}
	
	public void addLogListener(LogListener l){
		listeners.add(l);
	}
	
	public Spatial getTouchPad(){
		return monitorScreen;
	}
	
}
