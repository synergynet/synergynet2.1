package synergynet.table.apps.researchpuzzle.synergycomponent;

import java.awt.Color;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.QuadContentItem;
import synergynet.contentsystem.items.SimpleButton;
import synergynet.contentsystem.items.Window;
import synergynet.contentsystem.items.listener.SimpleButtonAdapter;
import synergynet.table.apps.researchpuzzle.ResearchPuzzleApp;
import synergynet.table.apps.researchpuzzle.synergycomponent.annotatewrapper.HighlightWrapper;
import synergynet.table.apps.researchpuzzle.synergycomponent.slicewrapper.SliceWrapper;

public class SynergyComponent{
	
	protected ContentSystem contentSystem;
	public static enum SynergyMode{RTS, SLICE, ANNOTATE};
	protected SynergyMode defaultSynergyMode = SynergyMode.RTS; 
	protected SliceWrapper sliceWrapper;
	protected HighlightWrapper annotateWrapper;
	protected QuadContentItem quadItem;
	protected Window window;
	SimpleButton closeButton, annotateButton, cutButton;
	
	public SynergyComponent(QuadContentItem item){
		this(item, item);
	}
	
	public SynergyComponent(QuadContentItem item, QuadContentItem rootItem){
		this.contentSystem = item.getContentSystem();
		this.quadItem = item;
		window = (Window) contentSystem.createContentItem(Window.class);
		window.setLocation(quadItem.getLocation().getX(), quadItem.getLocation().getY());
		window.setWidth(quadItem.getWidth());
		window.setHeight(quadItem.getHeight());
		window.setAngle(quadItem.getAngle());
		window.setScale(quadItem.getScale());
		quadItem.setLocation(0, 0);
		quadItem.setAngle(0);
		quadItem.setScale(1);
		window.addSubItem(quadItem);
		sliceWrapper = new SliceWrapper(quadItem, rootItem);
		sliceWrapper.setMaxSliceDimension(quadItem.getWidth(), quadItem.getHeight());
		annotateWrapper = new HighlightWrapper(quadItem, rootItem);
	
		
		closeButton = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		closeButton.setAutoFitSize(false);
		closeButton.setWidth(25);
		closeButton.setHeight(25);
		closeButton.setBackgroundColour(Color.white);
		closeButton.drawImage(ResearchPuzzleApp.class.getResource("images/close.png"),0,0,25,25);
		closeButton.addButtonListener(new SimpleButtonAdapter(){
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				if(window.getParent() == null)
					contentSystem.removeContentItem(window, false);
				else
					window.getParent().removeSubItem(window, false);
			}
		});
		closeButton.setBorderSize(2);
		closeButton.setLocalLocation(item.getWidth()/2, item.getHeight()/2);
		closeButton.setOrder(item.getOrder()+1);
		window.addSubItem(closeButton);
		
		
		annotateButton = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		annotateButton.setAutoFitSize(false);
		annotateButton.setWidth(25);
		annotateButton.setHeight(25);
		annotateButton.setBackgroundColour(Color.white);
		annotateButton.drawImage(ResearchPuzzleApp.class.getResource("images/draw.png"),0,0,25,25);
		annotateButton.addButtonListener(new SimpleButtonAdapter(){
			
			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				SynergyComponent.this.setSynergyMode(SynergyMode.ANNOTATE);
			}
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				SynergyComponent.this.setSynergyMode(SynergyMode.RTS);
			}
		});
		annotateButton.setBorderSize(2);
		annotateButton.setLocalLocation(item.getWidth()/2, (item.getHeight()/2)-40);
		annotateButton.setOrder(item.getOrder()+1);
		window.addSubItem(annotateButton);
		
		
		cutButton = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		cutButton.setAutoFitSize(false);
		cutButton.setWidth(25);
		cutButton.setHeight(25);
		cutButton.setBackgroundColour(Color.white);
		cutButton.drawImage(ResearchPuzzleApp.class.getResource("images/cut.png"),0,0,25,25);
		cutButton.addButtonListener(new SimpleButtonAdapter(){
			
			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				SynergyComponent.this.setSynergyMode(SynergyMode.SLICE);
			}
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				SynergyComponent.this.setSynergyMode(SynergyMode.RTS);
			}
		});
		cutButton.setBorderSize(2);
		cutButton.setLocalLocation(item.getWidth()/2, (item.getHeight()/2)-80);
		cutButton.setOrder(item.getOrder()+1);
		window.addSubItem(cutButton);
		
		this.setSynergyMode(SynergyMode.RTS);
	}
	
	public void setSynergyMode(SynergyMode mode){
		this.defaultSynergyMode = mode;
		if(sliceWrapper != null && annotateWrapper!= null && quadItem != null){
			if(mode.equals(SynergyMode.RTS)){
				sliceWrapper.setSliceEnabled(false);
				annotateWrapper.setAnnotateEnabled(false);
				window.setRotateTranslateScalable(true);
			}else if(mode.equals(SynergyMode.SLICE)){
				sliceWrapper.setSliceEnabled(true);
				annotateWrapper.setAnnotateEnabled(false);
				window.setRotateTranslateScalable(false);
				closeButton.setRotateTranslateScalable(true);
				annotateButton.setRotateTranslateScalable(true);
				cutButton.setRotateTranslateScalable(true);

			}else if(mode.equals(SynergyMode.ANNOTATE)){
				sliceWrapper.setSliceEnabled(false);
				annotateWrapper.setAnnotateEnabled(true);
				window.setRotateTranslateScalable(false);
				closeButton.setRotateTranslateScalable(true);
				annotateButton.setRotateTranslateScalable(true);
				cutButton.setRotateTranslateScalable(true);
			}
		}
	}
	
	public SynergyMode getSynergyMode(){
		return this.defaultSynergyMode;
	}
	
	public Window getContainer(){
		return window;
	}
}
