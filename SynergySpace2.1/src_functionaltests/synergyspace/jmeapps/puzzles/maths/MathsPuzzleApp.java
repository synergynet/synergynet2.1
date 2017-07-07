/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergySpace' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergyspace.jmeapps.puzzles.maths;

import java.applet.Applet;
import java.awt.Font;
import java.util.ArrayList;



import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;

import synergyspace.jme.gfx.twod.MTText;

import synergyspace.jme.abstractapps.AbstractMultiTouchTwoDeeApp;
import synergyspace.jme.config.AppConfig;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.jme.cursorsystem.elements.MultiTouchButton;
import synergyspace.jme.cursorsystem.elements.MultiTouchButtonAdapter;
import synergyspace.jme.cursorsystem.elements.twod.OrthoBringToTop;
import synergyspace.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;
import synergyspace.jme.cursorsystem.flicksystem.FlickSystem;
import synergyspace.jme.gfx.twod.ImageQuadFactory;
import synergyspace.jmeapps.puzzles.framework.CompositeMatcher;
import synergyspace.jmeapps.puzzles.framework.Puzzle;
import synergyspace.jmeapps.puzzles.framework.network.NetworkHandler;
import synergyspace.mtinput.events.MultiTouchCursorEvent;

public class MathsPuzzleApp extends AbstractMultiTouchTwoDeeApp {

	private final static float friction = 15f;

	private Puzzle puzzle;
	private int questionCount = 0;
	private int fontSize = 50;
	private boolean first = true;

	private ArrayList<QuestionAndAnswer> QandA = new ArrayList<QuestionAndAnswer>();
	private ArrayList<MTText> questionTexts = new ArrayList<MTText>();

	private final static String NAME = "Simple Maths";

	public static void main(String[] args) {
		AppConfig.debugToolsFlag = AppConfig.INPUT_DEBUGTOOLS_ON;
		MathsPuzzleApp app = new MathsPuzzleApp();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		NetworkHandler.setup(NAME, false);
	}

	public void update(float f) {
		super.update(f);
		if (!puzzle.keepWaiting()){
			if (first){
				putInPlace();
				puzzle.addBorders();
			}
		}
	}

	public void putInPlace(){
		GridLayout gL = new GridLayout(orthoRoot);
		puzzle.setupLayout(gL);

		//You can have 12 questions
		String[] questionValues = {"33/11", "9-7", "3*4", "6/3", "5/2", "7-9", "7*7", "8/3", "5+5", "20-7", "1*3", "1/1"};

		FlickSystem flicking = FlickSystem.getInstance();
		flicking.enableScreenBoundary(orthoRoot);
		addItemForUpdating(flicking);

		for (int i = 0; i < questionValues.length; i++){
			generatesQA(questionValues[i], flicking);
		}

		gL.addPartsToRoot();

		Quad button = ImageQuadFactory.createQuadWithImageResource("button", 252,
				MathsPuzzleApp.class.getResource("check.jpg"));
		button.setLocalTranslation(0f, -DisplaySystem.getDisplaySystem().getHeight()/2+50, 0f);
		button.setModelBound(new OrthogonalBoundingBox());
		button.updateModelBound();
		MultiTouchButton b = new MultiTouchButton(button);
		b.addButtonListener(new MultiTouchButtonAdapter() {
			public void buttonClicked(MultiTouchButton button, ScreenCursor c, MultiTouchCursorEvent event) {
				boolean anyCorrect = false;
				for (QuestionAndAnswer qa : QandA){
					if (qa.checkSolved()){
						qa.clear();
						anyCorrect = true;
					}
				}
				if (anyCorrect){
					Applet.newAudioClip(MathsPuzzleApp.class.getResource("correct.wav")).play();
				}
			}
		});
		orthoRoot.attachChild(button);

		float progressHeight = DisplaySystem.getDisplaySystem().getHeight()/2-95;
		float progressWidth = DisplaySystem.getDisplaySystem().getWidth()/2-75;
		puzzle.setupProgressMonitor(150, new Vector3f(progressWidth,progressHeight,0), orthoRoot);
		puzzle.addProgressSpatialsToNode();

		orthoRoot.updateGeometricState(0f, false);
		first = false;
	}



	@Override
	protected void setupContent() {

		puzzle = new Puzzle(NAME, orthoRoot);
		addItemForUpdating(puzzle);

		fpsNode.detachAllChildren();

		orthoRoot.updateGeometricState(0f, false);

	}

	private int generatesQA(String value, FlickSystem flicking) {

		String shownValue = value.replace("+", " + ");
		shownValue = shownValue.replace("-", " - ");
		shownValue = shownValue.replace("/", " ÷ ");
		shownValue = shownValue.replace("*", " x ");
		shownValue += "=";

		final MTText quadQ = new MTText("question" + questionCount, Puzzle.inverse,
				new Font("sansserif", Font.PLAIN, fontSize), shownValue);

		quadQ.setModelBound(new OrthogonalBoundingBox());
		quadQ.updateModelBound();
		quadQ.setZOrder(-2);

		questionTexts.add(quadQ);

		String result = "";
		if(value.contains("+")){
			int valueA = Integer.parseInt(value.split("\\+")[0]);
			int valueB = Integer.parseInt(value.split("\\+")[1]);
			float sum = valueA + valueB;
			result = "" + sum;
		}else if(value.contains("-")){
			int valueA = Integer.parseInt(value.split("\\-")[0]);
			int valueB = Integer.parseInt(value.split("\\-")[1]);
			float sum = valueA - valueB;
			result = "" + sum;
		}else if(value.contains("/")){
			float valueA = Float.parseFloat(value.split("\\/")[0]);
			float valueB = Float.parseFloat(value.split("\\/")[1]);
			float sum = valueA / valueB;
			result = "" + sum;
		}else if(value.contains("*")){
			int valueA = Integer.parseInt(value.split("\\*")[0]);
			int valueB = Integer.parseInt(value.split("\\*")[1]);
			float sum = valueA * valueB;
			result = "" + sum;
		}

		result = result.replace(".0", "");
		if (result.length() > 5){
			result = result.substring(0, 4);
		}

		final MTText quadA = new MTText("Answer" + questionCount, ColorRGBA.green,
				new Font("sansserif", Font.BOLD, fontSize), result);

		Quaternion rot = new Quaternion();
		rot.fromAngles(0f, 0f, FastMath.rand.nextFloat() * FastMath.PI);
		quadA.setLocalRotation(rot);
		quadA.setModelBound(new OrthogonalBoundingBox());
		quadA.updateModelBound();
		quadQ.setZOrder(-1);
		OrthoControlPointRotateTranslateScale ocprts = new OrthoControlPointRotateTranslateScale(quadA);
		ocprts.setScaleLimits(0.8f, 2.0f);
		ocprts.setPickMeOnly(true);
		new OrthoBringToTop(quadA);
		flicking.makeFlickable(quadA, ocprts, friction);

		QuestionAndAnswer QnA = new QuestionAndAnswer(quadA);
		CompositeMatcher composite = new CompositeMatcher(quadA);
		composite.addMatcher(new LocRotScaleMatcher(quadA, quadA.getWidth(), quadA.getHeight()));
		ValueMatcher vm = new ValueMatcher(quadA);
		vm.setValue(result);
		composite.addMatcher(vm);
		QnA.addMatcher(composite);

		QandA.add(QnA);

        puzzle.addPiece(new AnswerPiece(quadQ, result, quadQ.getWidth(), quadQ.getHeight()));
        puzzle.addTemplateProblem(QnA);

		questionCount++;;

		return 0;

	}

	@Override
	protected void setupLighting() {
	}

	@Override
	protected void setupSystem() {
	}

}
