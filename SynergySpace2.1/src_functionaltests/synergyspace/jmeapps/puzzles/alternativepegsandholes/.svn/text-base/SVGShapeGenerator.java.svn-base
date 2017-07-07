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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import synergyspace.jme.cursorsystem.flicksystem.FlickSystem;

import com.jme.renderer.ColorRGBA;

public class SVGShapeGenerator extends ShapeGenerator{

	public SVGShapeGenerator(FlickSystem flicking){
		super(flicking);

		String svgPath = System.getProperty("user.dir") + "/src/puzzle/pegsandholes/pic.svg";

		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			svgPath = selectedFile.getPath();
		}

		getShape(svgPath);

	}



	public void getShape(String fileName){
		File file = new File(fileName);
		Document document = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(false);

		try{
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			document = builder.parse(file);
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(SAXException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}

		if (document != null){
			getPolygon(document);

		}
	}

	private void getPolygon(Document document) {
		NodeList list = document.getElementsByTagName("polygon");

		for (int i = 0; i <list.getLength(); i++){

			String coords = list.item(i).getAttributes().getNamedItem("points").getTextContent();

			String[] pairs = coords.split(" ");
			ArrayList<String> newPairs = new ArrayList<String>();

			for (int j = 0; j < pairs.length; j++){
				if (!(pairs[j].equals("C")) && !(pairs[j].equals("M")) && !(pairs[j].equals("z"))){
					newPairs.add(pairs[j]);
				}
			}

			float[][] newCoords = new float[newPairs.size()][2];

			for (int j = 0; j < newPairs.size(); j++){
				newCoords[j][0] =  Float.valueOf(newPairs.get(j).split(",")[0]);
				newCoords[j][1] =  Float.valueOf(newPairs.get(j).split(",")[1]);
			}

			createPuzzlePart("Polygon" + i, newCoords, ColorRGBA.randomColor());
		}
	}

	public void createPuzzlePart(String name, float[][] coords, ColorRGBA colour){
		super.createPuzzlePart(name, coords, colour);
	}
}
