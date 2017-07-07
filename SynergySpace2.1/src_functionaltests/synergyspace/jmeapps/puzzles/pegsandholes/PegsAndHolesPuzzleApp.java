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

package synergyspace.jmeapps.puzzles.pegsandholes;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import synergyspace.jme.gfx.twod.PolygonMesh;


import com.jme.math.Vector3f;

import com.jme.system.DisplaySystem;

import synergyspace.jme.abstractapps.AbstractMultiTouchTwoDeeApp;
import synergyspace.jme.config.AppConfig;
import synergyspace.jme.cursorsystem.elements.twod.OrthoBringToTop;
import synergyspace.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;
import synergyspace.jmeapps.puzzles.framework.CompositeMatcher;
import synergyspace.jmeapps.puzzles.framework.Piece;
import synergyspace.jmeapps.puzzles.framework.Puzzle;
import synergyspace.jmeapps.puzzles.framework.network.NetworkHandler;

public class PegsAndHolesPuzzleApp extends AbstractMultiTouchTwoDeeApp {

	private final static String NAME = "Pegs and Holes";

	private ShapeGenerator shapeMaker;

	private ArrayList<PuzzlePart> objects = new ArrayList<PuzzlePart>();
	private ArrayList<PuzzlePart> holes = new ArrayList<PuzzlePart>();
	private ArrayList<OrthoControlPointRotateTranslateScale> movers =
		new ArrayList<OrthoControlPointRotateTranslateScale>();
	private ArrayList<OrthoBringToTop> topps = new ArrayList<OrthoBringToTop>();

	private PolygonMesh brick;
	private Puzzle puzzle;
	private int attemptCount = 0;
	private int maxAttempts = 20;
	private RandomLayout layout;
	public boolean first = true;

	public static void main(String[] args) {
		AppConfig.debugToolsFlag = AppConfig.INPUT_DEBUGTOOLS_ON;
		final PegsAndHolesPuzzleApp app = new PegsAndHolesPuzzleApp();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		NetworkHandler.setup(NAME, false);
	}

	public void update(float f) {
		super.update(f);
		if (!puzzle.keepWaiting()){
			if (first){
				putInPlace();
				brick.updateRenderState();
				for (PuzzlePart p: holes){
					p.updateRenderState();
				}
				for (PuzzlePart p: objects){
					p.updateRenderState();
				}
				puzzle.addBorders();
			}
		}
	}

	@Override
	protected void setupContent() {

		puzzle = new Puzzle(NAME, orthoRoot);
		addItemForUpdating(puzzle);

		boolean svgMode = false;

		String message = "Which mode do you wish to use?";
		Object[] options = { "SVG Mode", "Generic Shape Mode" };
		int conf = JOptionPane.showOptionDialog(null, message, "Confirmation", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null, options, options[1]);
		if (conf == JOptionPane.YES_OPTION) {
			svgMode = true;
		}

		if (svgMode){

			SVGShapeGenerator svgShapeMaker = new SVGShapeGenerator();

			shapeMaker = svgShapeMaker;

		}else{

			GenericShapeGenerator genericShapeMaker = new GenericShapeGenerator();

			genericShapeMaker.addRectangle();
			genericShapeMaker.addRectangle();
			genericShapeMaker.addTriangle();
			genericShapeMaker.addTriangle();

			shapeMaker = genericShapeMaker;

		}

		assignArrays();

		orthoRoot.updateGeometricState(0f, true);


	}

	public void putInPlace(){

		brick = new PolygonMesh("brick", (int)350, (int)200);
		brick.addSquareFilled(Puzzle.inverse, 50, false);
		brick.setZOrder(-3);
		orthoRoot.attachChild( brick );



		layout = new RandomLayout(orthoRoot, brick);
		puzzle.setupLayout(layout);

		setUp(objects, "object");
		setUp(holes, "hole");

		float progressHeight = DisplaySystem.getDisplaySystem().getHeight()/2-95;
		float progressWidth = DisplaySystem.getDisplaySystem().getWidth()/2-45;
		puzzle.setupProgressMonitor(150, new Vector3f(progressWidth,progressHeight,0), orthoRoot);
		puzzle.addProgressSpatialsToNode();

		orthoRoot.updateGeometricState(0f, true);

		first = false;

	}

	private void assignArrays() {
		objects = shapeMaker.getObjects();
		holes = shapeMaker.getHoles();
		movers = shapeMaker.getMovers();
		topps = shapeMaker.getBringToTopMovers();
	}

	private void setUp(ArrayList<PuzzlePart> parts, String type) {
		for (int i = 0; i < parts.size(); i++){

			if (attemptCount < maxAttempts){
				//Utilise Puzzle framework
				if (type.equals("hole")){
					Piece p = new Piece(parts.get(i));
					puzzle.addPiece(p);
				}else{
					PiecesAligned tp = new PiecesAligned(parts.get(i));
					tp.addController(movers.get(i));
					tp.addToTopController(topps.get(i));
					CompositeMatcher cm = new CompositeMatcher(parts.get(i));
					cm.addMatcher(new TranslationMatcher(parts.get(i)));
					cm.addMatcher(new VertexMatcher(parts.get(i)));
					tp.addMatcher(cm);
					puzzle.addTemplateProblem(tp);
				}
			}else{
				holes.remove(i);
				objects.remove(i);
				movers.remove(i);
				topps.remove(i);
			}
			attemptCount = 0;
		}
	}

	@Override
	protected void setupLighting() {
	}

	@Override
	protected void setupSystem() {
	}

}
