package synergynet.table.apps.lightbox;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.concurrent.Callable;

import com.jme.util.GameTaskQueueManager;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.Keyboard;
import synergynet.contentsystem.items.LineItem;
import synergynet.contentsystem.items.SimpleButton;
import synergynet.contentsystem.items.TextLabel;
import synergynet.contentsystem.items.Window;
import synergynet.contentsystem.items.innernotecontroller.InnerNoteController;
import synergynet.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleAdapter;
import synergynet.contentsystem.items.listener.SimpleButtonAdapter;
import synergynet.table.apps.conceptmap.GraphConfig;
import synergynet.table.apps.mathpadapp.MathPadUtils;
import synergynet.table.apps.mathpadapp.util.MTFrame;
import synergyspace.jme.gfx.twod.keyboard.Key;
import synergyspace.jme.gfx.twod.keyboard.MTKeyListener;

public class FlickrSearchTool extends MTFrame {

	private Keyboard keyboard;
	private boolean isKeyboradOn;
	private Lightbox lightBox;
	private TextLabel searchText;

	public FlickrSearchTool(ContentSystem contentSystem, Lightbox lb) {
		super(contentSystem);
		this.lightBox = lb;
		setTitle("Flickr search");
		
		searchText = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		searchText.setBorderSize(1);
		searchText.setBorderColour(Color.black);
		searchText.setAutoFitSize(false);
		searchText.setWidth(170);
		searchText.setHeight(30);
		searchText.setFont(new Font("Arial", Font.PLAIN, 12));
		searchText.setBackgroundColour(Color.white);
		searchText.setLocalLocation(10, 50);
		
		final TextLabel searchTextLabel = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		searchTextLabel.setBorderSize(0);
		searchTextLabel.setBackgroundColour(this.getWindow().getBackgroundColour());
		searchTextLabel.setText("Username");
		searchTextLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		searchTextLabel.setLocalLocation(-110, 50);
		
		SimpleButton editTextBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		editTextBtn.setBorderSize(1);
		editTextBtn.setBorderColour(Color.black);
		editTextBtn.setAutoFitSize(false);
		editTextBtn.setWidth(30);
		editTextBtn.setHeight(30);
		editTextBtn.drawImage(MathPadUtils.class.getResource("images/buttons/text_off.jpg"));
		editTextBtn.setLocalLocation(115, 50);
		editTextBtn.addButtonListener(new SimpleButtonAdapter(){
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				if (!isKeyboradOn){
					showKeyborad(searchText, window);
					isKeyboradOn = true;
				}
				else{
					hideKeyboard();
					isKeyboradOn= false;
				}
			}
		});
		
		final SimpleButton okBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		okBtn.setBorderSize(1);
		okBtn.setBorderColour(Color.black);
		okBtn.setBackgroundColour(Color.LIGHT_GRAY);
		okBtn.setTextColour(Color.black);
		okBtn.setAutoFitSize(false);
		okBtn.setWidth(50);
		okBtn.setHeight(30);
		okBtn.setText("Search");
		okBtn.setLocalLocation(0, -50);
		okBtn.addButtonListener(new SimpleButtonAdapter(){
			@Override
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				okBtn.setBackgroundColour(Color.LIGHT_GRAY);
				okBtn.setTextColour(Color.black);			
				search();
				
			}

			@Override
			public void buttonPressed(SimpleButton b, long id, float x, float y,
					float pressure) {
				okBtn.setBackgroundColour(Color.DARK_GRAY);
				okBtn.setTextColour(Color.white);
			}
		});
		
		this.getWindow().addSubItem(searchText);
		this.getWindow().addSubItem(searchTextLabel);
		this.getWindow().addSubItem(editTextBtn);
		this.getWindow().addSubItem(okBtn);

	}
	
	protected LineItem line;
	
	private void showKeyborad(final TextLabel edittedTextLabel, Window sourceItem){
		if (keyboard!= null) return;
		keyboard = (Keyboard)contentSystem.createContentItem(Keyboard.class);		
		keyboard.setScale(0.5f);
		keyboard.setKeyboardImageResource(GraphConfig.nodeKeyboardImageResource);
		keyboard.setKeyDefinitions(this.getKeyDefs());
		keyboard.setAsTopObject();
		keyboard.setBringToTopable(false);
		
		line = (LineItem)contentSystem.createContentItem(LineItem.class);
		line.setSourceItem(sourceItem);
		line.setSourceLocation(sourceItem.getLocalLocation());
		line.setTargetItem(keyboard);
		line.setTargetLocation(keyboard.getLocalLocation());
		line.setArrowMode(LineItem.NO_ARROWS);
		line.setLineMode(LineItem.SEGMENT_LINE);

		keyboard.addKeyListener(new MTKeyListener(){

			@Override
			public void keyReleasedEvent(KeyEvent evt) {
				String text = edittedTextLabel.getText();
				if(text == null) text ="";
				if(evt.getKeyChar() == KeyEvent.VK_ENTER){
					edittedTextLabel.setText(text + evt.getKeyChar());
				}
				else if(evt.getKeyChar() == KeyEvent.VK_BACK_SPACE){
					if(text.length() >0){
						text = text.substring(0,text.length()-1);
						edittedTextLabel.setText(text);
					}
				}
				else if(evt.getKeyChar() != KeyEvent.VK_CAPS_LOCK){
					text = text+evt.getKeyChar();
					edittedTextLabel.setText(text);
				}else if(evt.getKeyChar() == KeyEvent.VK_ENTER) {
					search();
				}
			}

			@Override
			public void keyPressedEvent(KeyEvent evt) {
				
			}});
		
		keyboard.addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleAdapter(){

			@Override
			public void itemTranslated(ContentItem item, float newLocationX,
					float newLocationY, float oldLocationX, float oldLocationY) {
				updateLine();
			}
			
		});
		
		this.getWindow().addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleAdapter(){

			@Override
			public void itemTranslated(ContentItem item, float newLocationX,
					float newLocationY, float oldLocationX, float oldLocationY) {
				updateLine();
			}
			
		});
	}
	
	protected void search() {
		GameTaskQueueManager.getManager().update(new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				lightBox.loadFlickrImages(searchText.getText());
				return null;
			}
			
		});
		hideKeyboard();
	}

	private void hideKeyboard(){
		if (this.keyboard!=null){
			this.contentSystem.removeContentItem(keyboard);
			keyboard = null;
		}
		
		if (this.line!=null){
			this.contentSystem.removeContentItem(line);
			line = null;
		}
	}
	
	private void updateLine(){
		if (line!=null && keyboard!=null){
			line.setSourceLocation(window.getLocalLocation());
			line.setTargetLocation(keyboard.getLocalLocation());
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Key> getKeyDefs() {
		try {
			ObjectInputStream ois = new ObjectInputStream(InnerNoteController.class.getResourceAsStream("keyboard.def"));
			return (List<Key>) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
