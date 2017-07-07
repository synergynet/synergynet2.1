package synergynet.table.apps.tablepositionsetup;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.TextLabel;
import synergynet.contentsystem.items.listener.ItemListener;
import synergyspace.jme.Updateable;

public class Stepper implements Updateable{

	private TextLabel distanceText, upper, downer, prefixText, suffixText = null;
	private boolean up, down = false;
	private float result, scale = 0;
	private int locX, locY = 0;
	private String measurement;
	private float upperLimit, lowerLimit = 0;
	private boolean upperLimitSet, lowerLimitSet = false;

	public Stepper(ContentSystem contentSystem, int x, int y, float startValue, float stepSize, String prefix, String suffix, String measure){
		this.locX = x;
		this.locY = y;
		this.measurement = measure;
		this.scale = stepSize;

		result = startValue;

		prefixText = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		prefixText.setBackgroundColour(Color.black);
		prefixText.setBorderSize(0);
		prefixText.setTextColour(Color.white);
		prefixText.setFont(new Font("Arial", Font.PLAIN,20));
		prefixText.setRotateTranslateScalable(false);
		prefixText.setBringToTopable(false);
		prefixText.setText(prefix);
		prefixText.setLocalLocation(locX, locY);

		distanceText = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		distanceText.setBackgroundColour(Color.black);
		distanceText.setBorderSize(0);
		distanceText.setTextColour(Color.white);
		distanceText.setFont(new Font("Arial", Font.PLAIN,20));
		distanceText.setRotateTranslateScalable(false);
		distanceText.setBringToTopable(false);
		distanceText.setText(" " + new DecimalFormat("0.##").format((float)result) + measurement + " ");
		distanceText.setLocalLocation(locX + prefixText.getWidth()/2 + distanceText.getWidth()/2, locY);

		suffixText = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		suffixText.setBackgroundColour(Color.black);
		suffixText.setBorderSize(0);
		suffixText.setTextColour(Color.white);
		suffixText.setFont(new Font("Arial", Font.PLAIN,20));
		suffixText.setRotateTranslateScalable(false);
		suffixText.setBringToTopable(false);
		suffixText.setText(suffix);
		suffixText.setLocalLocation(locX + prefixText.getWidth()/2 + distanceText.getWidth(), locY);

		upper = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		upper.setBackgroundColour(Color.black);
		upper.setBorderSize(2);
		upper.setBorderColour(Color.green);
		upper.setTextColour(Color.green);
		upper.setFont(new Font("Arial", Font.BOLD,20));
		upper.setRotateTranslateScalable(false);
		upper.setBringToTopable(false);
		upper.setText("+");
		int buttonWidth = upper.getWidth();
		upper.setAutoFitSize(false);
		upper.setWidth(buttonWidth);
		upper.setHeight(buttonWidth);
		upper.setLocalLocation(locX + upper.getWidth(), locY - distanceText.getHeight()/2 - upper.getHeight()/2);
		upper.addItemListener(new ItemListener(){

			@Override
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}

			@Override
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {
				up = true;
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {
				up = false;
			}

			@Override
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
		});


		downer = (TextLabel)contentSystem.createContentItem(TextLabel.class);
		downer.setBackgroundColour(Color.black);
		downer.setBorderSize(2);
		downer.setBorderColour(Color.red);
		downer.setTextColour(Color.red);
		downer.setFont(new Font("Arial", Font.BOLD,20));
		downer.setRotateTranslateScalable(false);
		downer.setBringToTopable(false);
		downer.setText("-");
		downer.setAutoFitSize(false);
		downer.setWidth(buttonWidth);
		downer.setHeight(buttonWidth);
		downer.setLocalLocation(locX - downer.getWidth(), locY - distanceText.getHeight()/2 - downer.getHeight()/2);
		downer.addItemListener(new ItemListener(){

			@Override
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}

			@Override
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {
				down = true;
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {
				down = false;
			}

			@Override
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
		});

	}

	public float getValue(){
		return result;
	}

	public void setValue(float value){
		result = value;		updateStepper();;
	}

	public void setUpperLimit(float limit){
		upperLimitSet = true;
		upperLimit = limit;
	}

	public void unSetUpperLimit(){
		upperLimitSet = false;
	}

	public void setLowerLimit(float limit){
		lowerLimitSet = true;
		lowerLimit = limit;
	}

	public void unSetLowerLimit(){
		lowerLimitSet = false;
	}

	public void destroyDial(){
		distanceText.setVisible(false, true);
		upper.setVisible(false, true);
		downer.setVisible(false, true);
		prefixText.setVisible(false, true);
		suffixText.setVisible(false, true);
	}

	private void updateStepper(){
		distanceText.setText(" " + new DecimalFormat("0.##").format((float)result) + measurement + " ");
		distanceText.setLocalLocation(locX + prefixText.getWidth()/2 + distanceText.getWidth()/2, locY);
		suffixText.setLocalLocation(locX + prefixText.getWidth()/2 + distanceText.getWidth(), locY);
	}

	@Override
	public void update(float timePerFrame) {
		if (up){
			if (upperLimitSet && result + (scale*timePerFrame) > upperLimit){
				result = upperLimit;
				updateStepper();
			}else if(lowerLimitSet && result + (scale*timePerFrame) < lowerLimit){
				result = lowerLimit;
				updateStepper();
			}else{
				result += (scale*timePerFrame);
				updateStepper();
			}
		}

		if (down){
			if (upperLimitSet && result - (scale*timePerFrame) > upperLimit){
				result = upperLimit;
				updateStepper();
			}else if(lowerLimitSet && result - (scale*timePerFrame) < lowerLimit){
				result = lowerLimit;
				updateStepper();
			}else{
				result -= (scale*timePerFrame);
				updateStepper();
			}
		}

	}

}
