package synergynet.table.apps.threeDButtonsExperiment.calculator;

import com.jme.math.FastMath;

import synergynet.Resources;
import synergynet.contentsystem.items.TextLabel;
import synergynet.table.apps.threeDButtonsExperiment.calculator.button.ButtonNode;
import synergynet.table.apps.threeDButtonsExperiment.logger.DateTextWritter;

public class ThreeDCalculator extends Calculator {

	private static final long serialVersionUID = 2429175967783608868L;
	
	public ThreeDCalculator(String name, TextLabel targetNumberLabel, DateTextWritter logWritter) {
		super(name, targetNumberLabel, logWritter);
	}

	@Override
	protected void setButtonProperty() {
		this.buttonNode0.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode1.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode2.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode3.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode4.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode5.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode6.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode7.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode8.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNode9.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNodeDelete.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNodePlus.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNodeSubstract.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNodeResult.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		this.buttonNodeDot.setFeedbackMode(ButtonNode.FEEDBACK_MODE_3D);
		
	}
	
	protected void setCalculatorProperty(){
		buttonZOffset=2f;
		angle=FastMath.PI/12f;
		skinURL= Resources.getResource("data/threedmanipulation/calculator/body.png");
	}

}