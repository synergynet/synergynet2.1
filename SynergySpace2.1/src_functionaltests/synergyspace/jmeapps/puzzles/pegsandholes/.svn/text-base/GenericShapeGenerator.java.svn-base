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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.jme.math.FastMath;
import com.jme.renderer.ColorRGBA;

public class GenericShapeGenerator extends ShapeGenerator{

	int triangleCount = 0;
	int rectangleCount = 0;
	int squareCount = 0;

	public GenericShapeGenerator(){}

	public void addTriangle(){
		triangleCount++;
		getShape("triangle", triangleCount);
	}

	public void addRectangle(){
		rectangleCount++;
		getShape("rectangle", rectangleCount);
	}

	public void addSquare(){
		squareCount++;
		getShape("square", squareCount);
	}

	public void getShape(String shapeName, int count){
		float[][] coords;
		ArrayList<String[]> poss = new ArrayList<String[]>();
	    try {
	    	BufferedReader input =  new BufferedReader(new InputStreamReader(GenericShapeGenerator.class.getResourceAsStream("shapes.txt")));
	    	try {
	    		String line = null;
	    		while (( line = input.readLine()) != null){
	    			String[] words = line.split(" ");
	    			if (words[0].equals(shapeName)){
	    				poss.add(words);
	    			}
	    		}
	    	}
	    	finally {
	    		input.close();
	    	}
	  	  }catch (IOException ex){
	  		  ex.printStackTrace();
	  	  }
	  	  if (poss.size() > 0){
	  		  int value = (FastMath.rand.nextInt(poss.size()));
	  		  coords = new float[poss.get(value).length][2];
	  		  for (int i = 1; i < poss.get(value).length; i++){
	  			  poss.get(value)[i] = poss.get(value)[i].replace("(", "").replace(")", "");
	  			  coords[i][0] =  Float.valueOf(poss.get(value)[i].split(",")[0]).floatValue();
	  			  coords[i][1] =  Float.valueOf(poss.get(value)[i].split(",")[1]).floatValue();
	  		  }
	  		  createPuzzlePart(""+ shapeName + count, coords, ColorRGBA.randomColor());
	  	  }else{
	  		  System.out.println("No shapes match this name");
	  	  }
	}

	public void createPuzzlePart(String name, float[][] coords, ColorRGBA colour){
		super.createPuzzlePart(name, coords, 0, 0, colour);
	}
}
