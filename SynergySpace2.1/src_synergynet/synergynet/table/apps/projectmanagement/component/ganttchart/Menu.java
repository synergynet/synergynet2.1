package synergynet.table.apps.projectmanagement.component.ganttchart;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.LightImageLabel;
import synergynet.contentsystem.items.ListContainer;
import synergynet.contentsystem.items.SimpleButton;
import synergynet.contentsystem.items.Window;
import synergynet.contentsystem.items.listener.ItemEventAdapter;
import synergynet.contentsystem.items.listener.SimpleButtonAdapter;
import synergynet.table.apps.projectmanagement.ProjectManagementApp;

public class Menu {
	
	protected ContentSystem contentSystem;
	
	protected Window menuNode;
	protected ListContainer menuContainer;
	protected LightImageLabel toggleButton;
	protected List<MenuCommandListener> menuCommandListeners = new ArrayList<MenuCommandListener>();

	public Menu(ContentSystem contentSystem){
		this.contentSystem =contentSystem;
		
		menuNode = (Window)contentSystem.createContentItem(Window.class);
		
		//set note label
		toggleButton = (LightImageLabel)contentSystem.createContentItem(LightImageLabel.class);
		toggleButton.drawImage(ProjectManagementApp.class.getResource("images/menu.png"));
		toggleButton.setImageLabelHeight(25);
		toggleButton.setLocalLocation(0, 0);
		toggleButton.setBorderSize(0);
		toggleButton.setBringToTopable(false);	
		toggleButton.addItemListener(new ItemEventAdapter(){

			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorClicked(item, id, x, y, pressure);
				if (menuContainer.isVisible())
					menuContainer.setVisible(false);
				else {
					menuContainer.setVisible(true);
					updateMenuPosition();
				}
			}
	
		});
		
		menuNode.addSubItem(toggleButton);
		
		Spatial toggleSpatial =(Spatial)(toggleButton.getImplementationObject());
		toggleSpatial.setZOrder(10014, true);
		toggleButton.setBringToTopable(false);
		
		menuContainer = (ListContainer)contentSystem.createContentItem(ListContainer.class);
		menuContainer.setVisible(false);
		
		SimpleButton selectTaskButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		selectTaskButton.setAutoFitSize(false);
		selectTaskButton.setText("Select Task");
		selectTaskButton.setHeight(15);
		selectTaskButton.setWidth(80);
		selectTaskButton.setBackgroundColour(Color.lightGray);
		selectTaskButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {
				for (MenuCommandListener l: menuCommandListeners)
					l.selectTask();
			}
		});
		
		SimpleButton editTaskButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		editTaskButton.setAutoFitSize(false);
		editTaskButton.setText("Toggle Keyboard");
		editTaskButton.setHeight(15);
		editTaskButton.setWidth(80);
		editTaskButton.setBackgroundColour(Color.lightGray);
		editTaskButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {
				for (MenuCommandListener l: menuCommandListeners)
					l.editTask();
			}
		});
		
		SimpleButton controlPadButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		controlPadButton.setAutoFitSize(false);
		controlPadButton.setText("Control Pad");
		controlPadButton.setHeight(15);
		controlPadButton.setWidth(80);
		controlPadButton.setBackgroundColour(Color.lightGray);
		controlPadButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {
				for (MenuCommandListener l: menuCommandListeners)
					l.toggleControlPanel();
			}
		});
		
		SimpleButton addSequenceButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		addSequenceButton.setAutoFitSize(false);
		addSequenceButton.setText("Add Sequence Line");
		addSequenceButton.setHeight(15);
		addSequenceButton.setWidth(80);
		addSequenceButton.setBackgroundColour(Color.lightGray);
		addSequenceButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {
				for (MenuCommandListener l: menuCommandListeners)
					l.addSequenceLine();
			}
		});
		
		SimpleButton addMileStoneButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		addMileStoneButton.setAutoFitSize(false);
		addMileStoneButton.setText("Add Milestone");
		addMileStoneButton.setHeight(15);
		addMileStoneButton.setWidth(80);
		addMileStoneButton.setBackgroundColour(Color.lightGray);
		addMileStoneButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {
				for (MenuCommandListener l: menuCommandListeners)
					l.addMilestone();
			}
		});
		
		SimpleButton deleteButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		deleteButton.setAutoFitSize(false);
		deleteButton.setText("Delete");
		deleteButton.setHeight(15);
		deleteButton.setWidth(80);
		deleteButton.setBackgroundColour(Color.lightGray);
		deleteButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {
				for (MenuCommandListener l: menuCommandListeners)
					l.delete();
			}
		});
		
		menuContainer.setAutoFitSize(true);
		menuContainer.setWidth(180);
		menuContainer.setItemWidth(150);
		menuContainer.addSubItem(selectTaskButton);
		menuContainer.addSubItem(editTaskButton);
		menuContainer.addSubItem(controlPadButton);
		menuContainer.addSubItem(addSequenceButton);
		menuContainer.addSubItem(addMileStoneButton);
		menuContainer.addSubItem(deleteButton);
		menuContainer.setBackgroundColour(Color.ORANGE);
		
		Spatial menuSpatial =(Spatial)(menuContainer.getImplementationObject());
		menuSpatial.setZOrder(10015, true);
		menuContainer.setBringToTopable(false);
		
	}
	
	public void clear(){
		if (menuNode.getParent()!=null){
			menuNode.getParent().removeSubItem(menuNode, false);
			menuNode = null;
		}
		
		
		if (this.menuContainer!=null){
			this.contentSystem.removeContentItem(menuContainer);
			menuContainer = null;
		}
		
		toggleButton=null;
		menuCommandListeners=null;

	}
	
	public void setMenuLocation(float x, float y){

		menuNode.setLocalLocation(x, y);
		updateMenuPosition();
		
	}
	
	protected void updateMenuPosition(){
		Vector3f worldLocation = new Vector3f();
		((Spatial)(menuNode.getImplementationObject())).getParent().localToWorld(new Vector3f(menuNode.getLocalLocation().x+10, menuNode.getLocalLocation().y, 0), worldLocation);
		menuContainer.setLocalLocation(worldLocation.x, worldLocation.y-80);
	}
	
	public Window getMenuNode(){
		return menuNode;
	}
	
	public void addMenuCommandListener(MenuCommandListener l){
		menuCommandListeners.add(l);
	}

	public void removeMenuCommandListener(MenuCommandListener l){
		if (menuCommandListeners.contains(l))
			menuCommandListeners.remove(l);
	}

	public interface MenuCommandListener {
		public void selectTask();
		public void editTask();
		public void toggleControlPanel();
		public void addSequenceLine();
		public void addMilestone();
		public void delete();
		
	}

}
