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

package synergynet.table.apps.mathpadapp.controllerapp.assignmentbuilder.latexloader;

import uk.ac.ed.ph.snuggletex.SnuggleEngine;
import uk.ac.ed.ph.snuggletex.SnuggleInput;
import uk.ac.ed.ph.snuggletex.SnuggleSession;
import uk.ac.ed.ph.snuggletex.WebPageOutputOptions;
import uk.ac.ed.ph.snuggletex.jeuclid.JEuclidUtilities;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;


public class LatexLoader {
    
	protected SnuggleEngine engine = new SnuggleEngine();
    protected SnuggleSession session = engine.createSession();
    
	public LatexLoader(){}
	
	public Image convertLatexToImage(String strInput) throws IOException{

        SnuggleInput input = new SnuggleInput(strInput);
        session.parseInput(input);
        
        // Create temporary folder to store image in
		OutputStream out = null;
		File tempFolder = null;
		File imageFile = null;
		try {
			tempFolder = new File("temp");
			if(!tempFolder.exists()) tempFolder.mkdir();
			imageFile = new File(tempFolder, "temp.bmp");
			out = new FileOutputStream(imageFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        
		WebPageOutputOptions options = JEuclidUtilities.createWebPageOptions(false, new ImageOptions(out));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        session.writeWebPage(options, outputStream);
        
        // read the image
        Image image = ImageIO.read(imageFile);

        // delete temporary folder
        this.deleteDir(tempFolder);
        
        return image;
 	}
	
	private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                	System.out.println("failed");
                    return false;
                }
            }
        }
        return dir.delete();
    } 

	
    public static void main(String[] args){
    	LatexLoader loader = new LatexLoader();
    	try {
			loader.convertLatexToImage("$$ 1+2 $$");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
