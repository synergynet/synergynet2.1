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

package synergyspace.jmeapps.puzzles.framework;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;



import synergynet.table.appregistry.ApplicationControlError;
import synergynet.table.appregistry.ApplicationRegistry;
import synergynet.table.appregistry.ApplicationTaskManager;
import synergyspace.jme.Updateable;
import synergyspace.jme.cursorsystem.cursordata.ScreenCursor;
import synergyspace.jme.cursorsystem.elements.MultiTouchButton;
import synergyspace.jme.cursorsystem.elements.MultiTouchButtonAdapter;
//import synergyspace.jmeapps.puzzles.framework.network.NetworkHandler;
import synergyspace.jmeapps.puzzles.framework.progress.Monitor;
import synergyspace.mtinput.events.MultiTouchCursorEvent;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

import synergyspace.jme.gfx.twod.MTText;
import synergyspace.jme.gfx.twod.PolygonMesh;

public class Puzzle implements Updateable {

	private static List<TemplateProblem> templateProblems = new ArrayList<TemplateProblem>();
	private static List<Piece> pieces = new ArrayList<Piece>();
	private static Monitor progress = null;
	private static Layout layout = null;
	private static boolean setup = true;
	private static ColorRGBA theColour = ColorRGBA.red;
	private static ColorRGBA currentColour = theColour;
	private static List<ColorRGBA> colours = new ArrayList<ColorRGBA>();
	private static List<MultiTouchButton> colourButtons = new ArrayList<MultiTouchButton>();
	private static List<PolygonMesh> colourBoxes = new ArrayList<PolygonMesh>();
	private static boolean bp = false;
	private static int playerCount = 1;
	private static Node rootNode;

	public static ColorRGBA inverse = ColorRGBA.white;

	private PolygonMesh left;
	private PolygonMesh right;
	private PolygonMesh top;
	private PolygonMesh bottom;

	private MTText logo;
	private MTText selectColour;
	private MTText playerStatus;

	private static boolean exitButton = false;
	private boolean checkExit = false;

	public Puzzle(String name, Node rootNode){

		Puzzle.rootNode = rootNode;

		colours.add(ColorRGBA.red);
		setupBorders();

		setupStartScreen(name);

//		NetworkHandler.startDiscovery();

	}

	private void setupStartScreen(String name) {
		Font logoFont = new Font("sansserif", Font.PLAIN, 50);

		logo = new MTText("logo", inverse, logoFont, name);
		logo.setLocalTranslation(new Vector3f(-logo.getWidth()/2, DisplaySystem.getDisplaySystem().getHeight()/2-100, 0));
		logo.setModelBound(new OrthogonalBoundingBox());
		rootNode.attachChild(logo);

		Font font = new Font("sansserif", Font.PLAIN, 30);

		playerStatus = new MTText("playerStatus", inverse, font, playerCount + " table ready to play", 400);
		playerStatus.setLocalTranslation(
				new Vector3f(0, 50, 0));
		rootNode.attachChild(playerStatus);

		selectColour = new MTText("selectColour", inverse, font, "Select table colour:");
		selectColour.setLocalTranslation(
				new Vector3f(-200, -DisplaySystem.getDisplaySystem().getHeight()/2 + 140, 0));
		rootNode.attachChild(selectColour);


		PolygonMesh invert = new PolygonMesh("invert", 108, 56);
		invert.addImage(Puzzle.class.getResource("invert.png"));
		invert.setLocalTranslation(-DisplaySystem.getDisplaySystem().getWidth()/2+80,
				-DisplaySystem.getDisplaySystem().getHeight()/2+50, 0f);
		invert.setModelBound(new OrthogonalBoundingBox());
		invert.updateModelBound();
		MultiTouchButton i = new MultiTouchButton(invert);
		i.addButtonListener(new MultiTouchButtonAdapter() {
			public void buttonClicked(MultiTouchButton invert, ScreenCursor c, MultiTouchCursorEvent event) {
				if (inverse.equals(ColorRGBA.white)){
					DisplaySystem.getDisplaySystem().getRenderer().setBackgroundColor(ColorRGBA.white);
					inverse = ColorRGBA.black;
				}else{
					DisplaySystem.getDisplaySystem().getRenderer().setBackgroundColor(ColorRGBA.black);
					inverse = ColorRGBA.white;
				}
				playerStatus.changeColour(inverse);
				selectColour.changeColour(inverse);
				logo.changeColour(inverse);
			}
		});
		rootNode.attachChild(invert);

		PolygonMesh button = new PolygonMesh("button", 300, 56);
		button.addImage(Puzzle.class.getResource("start.png"));
		button.setLocalTranslation(0f, -DisplaySystem.getDisplaySystem().getHeight()/2+250, 0f);
		button.setModelBound(new OrthogonalBoundingBox());
		button.updateModelBound();
		MultiTouchButton b = new MultiTouchButton(button);
		b.addButtonListener(new MultiTouchButtonAdapter() {
			public void buttonClicked(MultiTouchButton button, ScreenCursor c, MultiTouchCursorEvent event) {
				bp = true;
			}
		});
		rootNode.attachChild(button);

		setupColours(500, -DisplaySystem.getDisplaySystem().getHeight()/2 + 100);

	}

	private void setupColours(float width, float y) {

		ArrayList<ColorRGBA> colourRange = new ArrayList<ColorRGBA>();
		colourRange.add(ColorRGBA.red);
		colourRange.add(ColorRGBA.blue);
		colourRange.add(ColorRGBA.yellow);
		colourRange.add(ColorRGBA.green);
		colourRange.add(ColorRGBA.pink);
		colourRange.add(ColorRGBA.orange);
		colourRange.add(ColorRGBA.magenta);
		colourRange.add(ColorRGBA.cyan);
		colourRange.add(ColorRGBA.gray);

		float gap = width/colourRange.size();
		float size = gap - gap/colourRange.size();

		float x = -width/2 + gap/2;

		for (ColorRGBA thisColour: colourRange){
			rootNode.attachChild(makeColourBox(x, y, size, thisColour));
			x += gap;
		}
	}

	private Spatial makeColourBox(float x, float y, float size, final ColorRGBA colour) {

		PolygonMesh s = new PolygonMesh("colour" + colourBoxes.size(), (int)size, (int)size);
		s.setLocalTranslation(x,y,0);
		s.addSquareFilled(colour, 50, false);
		s.setModelBound(new OrthogonalBoundingBox());
		s.updateModelBound();
		colourBoxes.add(s);

		MultiTouchButton b = new MultiTouchButton(s);
		b.addButtonListener(new MultiTouchButtonAdapter() {
			private ColorRGBA thisColour = colour;
			public void buttonPressed(MultiTouchButton button, ScreenCursor c, MultiTouchCursorEvent event) {
				theColour = thisColour;
			}
		});

		colourButtons.add(b);

		return s;

	}

	public boolean keepWaiting(){
		return setup;
	}

	public void update(float timePerFrame){
		if (checkExit){
			if (exitButton){
//				NetworkHandler.closeCommunication();
				try {
					ApplicationTaskManager.getInstance().setActiveApplication(ApplicationRegistry.getInstance().getDefaultApp());
				} catch (ApplicationControlError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else if (setup){
//			if (NetworkHandler.addresses.size() != NetworkHandler.discoveryListener.getAddresses().size()){
//				NetworkHandler.addresses = NetworkHandler.discoveryListener.getAddresses();
//				playerCount = NetworkHandler.addresses.size();
//				if(NetworkHandler.addresses.size() == 1 || NetworkHandler.addresses.size() == 0){
					playerStatus.setText("1 table ready to play");
//				}else{
//					playerStatus.setText(playerCount + " tables ready to play");
//				}
//			}

			if (theColour != currentColour){
				colours.set(0, theColour);
				setBorderColour();
				currentColour = theColour;
			}

			boolean start = false;

			if (bp){
				start = true;
			}
//			if (NetworkHandler.startGame()){
//				start = true;
//			}
			if (start){

//				for (int i = 1; i < NetworkHandler.addresses.size(); i++){
//					colours.add(ColorRGBA.white);
//				}

				toDoOnStart();

				setup = false;
				rootNode.detachAllChildren();

//				NetworkHandler.closeDiscovery();

//				if (NetworkHandler.addresses.size() > 1){
//					NetworkHandler.startListening();
//					NetworkHandler.startAnnouncing();
//					String message = "colour:-" + colours.get(0).r + "-" + colours.get(0).g + "-" +
//	        			colours.get(0).b + "-" + colours.get(0).a + "-" + NetworkHandler.addresses.get(0)[0];
//					NetworkHandler.sendMessage(message);
//				}
			}


		}else{
//			if (NetworkHandler.addresses.size() > 1){
//				String message = NetworkHandler.getMessage();
//				if (!message.equals("")){
//					if (message.contains("colour")){
//						String[] messageSubStrings = message.split("-");
//						colours.set(getNoFromAdd(messageSubStrings[5]), getColourFromString(message));
//					}else{
//						progress.increase(getNoFromAdd(message));
//					}
//				}
//			}
			TemplateProblem toRemove = null;
			for(TemplateProblem tp : templateProblems) {
				if(tp.getSolved()){
					toRemove = tp;
				}
			}
			if(toRemove != null){
				progress.increase(0);
				templateProblems.remove(toRemove);
				toRemove = null;
//				if (NetworkHandler.addresses.size() > 1){
//					String message = NetworkHandler.addresses.get(0)[0];
//					NetworkHandler.sendMessage(message);
//				}

			}
			if (templateProblems.size() == 0){
				checkExit = true;
				Font logoFont = new Font("sansserif", Font.PLAIN, 30);
				rootNode.detachAllChildren();
				MTText exit = new MTText("exit", ColorRGBA.green, logoFont, "Puzzle Complete");



				PolygonMesh exitButtonMesh = new PolygonMesh("exitbutton", 300, 56);
				exitButtonMesh.addImage(Puzzle.class.getResource("exit.png"));
				exitButtonMesh.setLocalTranslation(0f, -DisplaySystem.getDisplaySystem().getHeight()/2+250, 0f);
				exitButtonMesh.setModelBound(new OrthogonalBoundingBox());
				exitButtonMesh.updateModelBound();
				MultiTouchButton d = new MultiTouchButton(exitButtonMesh);
				d.addButtonListener(new MultiTouchButtonAdapter() {
					public void buttonClicked(MultiTouchButton exitButtonMesh, ScreenCursor c, MultiTouchCursorEvent event) {
						exitButton = true;
					}
				});
				exitButtonMesh.updateRenderState();
				rootNode.attachChild(exit);
				rootNode.attachChild(exitButtonMesh);
				addBorders();
			}

		}
	}

	private void toDoOnStart() {}

	@SuppressWarnings("unused")
	private ColorRGBA getColourFromString(String message) {
		String[] messageSubStrings = message.split("-");
		float[] messagePart = {Float.parseFloat(messageSubStrings[1]), Float.parseFloat(messageSubStrings[2]),
				Float.parseFloat(messageSubStrings[3]), Float.parseFloat(messageSubStrings[4])};
		return new ColorRGBA(messagePart[0], messagePart[1], messagePart[2], messagePart[3]);
	}

	@SuppressWarnings("unused")
	private int getNoFromAdd(String message) {
		message = message.replace(" ", "");
//		for (int i = 1; i < NetworkHandler.addresses.size(); i++){
//			if (message.equals(NetworkHandler.addresses.get(i)[0])){
//				return i;
//			}
//		}
		return 0;
	}

	private void setBorderColour() {
		PolygonMesh.setBorder(colours.get(0), 0);
		left.clear();
		left.addSquareFilled(colours.get(0), 0, false);
		right.clear();
		right.addSquareFilled(colours.get(0), 0, false);
		top.clear();
		top.addSquareFilled(colours.get(0), 0, false);
		bottom.clear();
		bottom.addSquareFilled(colours.get(0), 0, false);

	}

	public void addTemplateProblem(TemplateProblem tp){
		templateProblems.add(tp);
		if (layout != null){
			layout.setupProblem(tp.getSpatial());
		}
	}

	public void removeTemplateProblem(TemplateProblem tp){
		templateProblems.remove(tp);
	}

	public void setupLayout(Layout layout){
		Puzzle.layout = layout;
	}

	public void addPiece(Piece p){
		pieces.add(p);
		TemplateProblem.pieces = pieces;
		if (layout != null){
			layout.setupPiece(p.getSpatial());
		}
	}

	public void removePiece(Piece p){
		pieces.remove(p);
	}

	public void setupProgressMonitor(int radius, Vector3f translation, Node rootNode){
		progress = new Monitor(pieces.size(), radius, colours, translation, rootNode);
	}

	public void addProgressSpatialsToNode(){
		if (progress != null){
			progress.addSpatialsToNode();
		}
	}

	public void setupBorders(){

		float borderSize = 10;

		left = new PolygonMesh("leftBox", (int)borderSize, DisplaySystem.getDisplaySystem().getHeight());
		left.setLocalTranslation(new Vector3f(-DisplaySystem.getDisplaySystem().getWidth()/2, 0, 0));
		left.setModelBound(new OrthogonalBoundingBox());
		left.updateModelBound();
		left.updateRenderState();
		left.setZOrder(-3);

		right = new PolygonMesh("rightBox", (int)borderSize, DisplaySystem.getDisplaySystem().getHeight());
		right.setLocalTranslation(new Vector3f(DisplaySystem.getDisplaySystem().getWidth()/2, 0, 0));
		right.setModelBound(new OrthogonalBoundingBox());
		right.updateModelBound();
		right.updateRenderState();
		right.setZOrder(-3);

		top = new PolygonMesh("topBox", DisplaySystem.getDisplaySystem().getWidth(), (int)borderSize);
		top.setLocalTranslation(new Vector3f(0, DisplaySystem.getDisplaySystem().getHeight()/2, 0));
		top.setModelBound(new OrthogonalBoundingBox());
		top.updateModelBound();
		top.updateRenderState();
		top.setZOrder(-3);


		bottom = new PolygonMesh("bottomBox", DisplaySystem.getDisplaySystem().getWidth(), (int)borderSize);
		bottom.setLocalTranslation(new Vector3f(0, -DisplaySystem.getDisplaySystem().getHeight()/2, 0));
		bottom.setModelBound(new OrthogonalBoundingBox());
		bottom.updateModelBound();
		bottom.updateRenderState();
		bottom.setZOrder(-3);


		addBorders();
		setBorderColour();
	}

	public void addBorders(){
		rootNode.attachChild(bottom);
		rootNode.attachChild(top);
		rootNode.attachChild(left);
		rootNode.attachChild(right);
	}

}
