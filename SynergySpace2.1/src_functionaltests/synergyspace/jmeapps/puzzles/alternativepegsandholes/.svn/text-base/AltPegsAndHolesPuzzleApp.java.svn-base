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

package synergyspace.jmeapps.puzzles.alternativepegsandholes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.jme.math.Vector3f;

import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;

import synergyspace.jme.abstractapps.AbstractMultiTouchTwoDeeApp;
import synergyspace.jme.config.AppConfig;
import synergyspace.jme.cursorsystem.elements.twod.OrthoBringToTop;
import synergyspace.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;
import synergyspace.jme.cursorsystem.flicksystem.FlickSystem;
import synergyspace.jme.gfx.twod.PolygonMesh;
import synergyspace.jmeapps.puzzles.framework.CompositeMatcher;
import synergyspace.jmeapps.puzzles.framework.Piece;
import synergyspace.jmeapps.puzzles.framework.Puzzle;
import synergyspace.jmeapps.puzzles.framework.network.NetworkHandler;

public class AltPegsAndHolesPuzzleApp extends AbstractMultiTouchTwoDeeApp {

	private final static String NAME = "Pegs and Holes";

	private ShapeGenerator shapeMaker;

	private ArrayList<Piece> objects = new ArrayList<Piece>();
	private ArrayList<Piece> holes = new ArrayList<Piece>();
	private ArrayList<OrthoControlPointRotateTranslateScale> movers =
		new ArrayList<OrthoControlPointRotateTranslateScale>();
	private ArrayList<OrthoBringToTop> topps = new ArrayList<OrthoBringToTop>();
	private List<String> result = new ArrayList<String>();

	private boolean svgMode;

	private PolygonMesh brick;
	private Puzzle puzzle;
	private int attemptCount = 0;
	private int maxAttempts = 20;
	private RandomLayout layout;
	public boolean first = true;

	public static void main(String[] args) {
		AppConfig.debugToolsFlag = AppConfig.INPUT_DEBUGTOOLS_ON;
		final AltPegsAndHolesPuzzleApp app = new AltPegsAndHolesPuzzleApp();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		NetworkHandler.setup(NAME, false);
	}

	public void update(float f) {
		super.update(f);
		if (!puzzle.keepWaiting()){
			if (first){
				putInPlace();
				brick.updateRenderState();
				for (Piece p: holes){
					p.getSpatial().updateRenderState();
				}
				for (Piece p: objects){
					p.getSpatial().updateRenderState();
				}
				puzzle.addBorders();
			}
		}
	}

	@Override
	protected void setupContent() {

		fpsNode.detachAllChildren();

		puzzle = new Puzzle(NAME, orthoRoot);
		addItemForUpdating(puzzle);

		svgMode = false;

//		String message = "Which mode do you wish to use?";
//		Object[] options = { "SVG Mode", "Generic Shape Mode" };
//		int conf = JOptionPane.showOptionDialog(null, message, "Confirmation", JOptionPane.YES_NO_OPTION,
//                JOptionPane.WARNING_MESSAGE, null, options, options[1]);
//		if (conf == JOptionPane.YES_OPTION) {
//			svgMode = true;
//		}

		FlickSystem flicking = FlickSystem.getInstance();
		flicking.enableScreenBoundary(orthoRoot);
		addItemForUpdating(flicking);

		if (svgMode){

			SVGShapeGenerator svgShapeMaker = new SVGShapeGenerator(flicking);

			shapeMaker = svgShapeMaker;

		}else{

			getXML("shapes.xml", result);

		}


	}

	public void getXML(String resourceName, List<String> result){
		
		Document document = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(false);

		try{
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			document = builder.parse(AltPegsAndHolesPuzzleApp.class.getResourceAsStream(resourceName));
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(SAXException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}

		if (document != null){
			getValues(document, result);

		}
	}

	private void getValues(Document document, List<String> result) {

		NodeList list = document.getElementsByTagName("shape");
		for (int i = 0; i <list.getLength(); i++){
			result.add(list.item(i).getTextContent());
		}

	}

	public void putInPlace(){

		brick = new PolygonMesh("brick", 450, 350);
		brick.addImage(AltPegsAndHolesPuzzleApp.class.getResource("brick.png"));
		brick.setZOrder(-3);
		orthoRoot.attachChild( brick );

		FlickSystem flicking = FlickSystem.getInstance();
		flicking.enableScreenBoundary(orthoRoot);
		addItemForUpdating(flicking);

		if (svgMode){
			shapeMaker.setOutSVG();
		}else{
			ShapeGenerator genericShapeMaker = new ShapeGenerator(flicking);
			int count = 0;
			for (String s : result){
				genericShapeMaker.createPuzzlePart("shape" + count, s, ColorRGBA.randomColor());
				count++;
			}

			shapeMaker = genericShapeMaker;

		}

		assignArrays();

		orthoRoot.updateGeometricState(0f, true);




		layout = new RandomLayout(orthoRoot, brick);
		puzzle.setupLayout(layout);

		setUp(objects, "object");
		setUp(holes, "hole");

		float progressHeight = DisplaySystem.getDisplaySystem().getHeight()/2-95;
		float progressWidth = DisplaySystem.getDisplaySystem().getWidth()/2-75;
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

	private void setUp(ArrayList<Piece> parts, String type) {
		for (int i = 0; i < parts.size(); i++){
			if (attemptCount < maxAttempts){
				//Utilise Puzzle framework
				if (type.equals("hole")){
					puzzle.addPiece(parts.get(i));
				}else{
					PiecesAligned tp = new PiecesAligned(parts.get(i).getSpatial());
					tp.addController(movers.get(i));
					tp.addToTopController(topps.get(i));
					CompositeMatcher cm = new CompositeMatcher(parts.get(i).getSpatial());
					cm.addMatcher(new TranslationMatcher(parts.get(i).getSpatial()));
					cm.addMatcher(new ShapeMatcher(parts.get(i).getSpatial()));
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
