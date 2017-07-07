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

package synergyspace.mtinput.luminja;

//import java.awt.Desktop;
//import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import synergynet.table.SynergyNetDesktop;

public class MIMProcessController {
	
	private static MIMProcessController mim;
	private Runtime rt; 
	private String killCommand = "taskkill /f /im MultitouchServer.exe";
	private static final Logger log = Logger.getLogger(SynergyNetDesktop.class.getName());	
	public static final int START_DELAY = 5000;
	public static final int KILL_DELAY = 2000;
	private static boolean batGenerated = false;
	private static String batAddress =  "";
	
	private MIMProcessController(){
		rt = Runtime.getRuntime(); 
	}
	
	public static MIMProcessController getInstance(){
		if(mim == null) mim = new MIMProcessController();
		return mim;
	}
	
	public void start(String serverPath) throws IOException, InterruptedException{
//	    Desktop dt = Desktop.getDesktop();
//	    File serverFile = new File(serverPath);
//	 	dt.open(serverFile);
		if (!batGenerated){	
			batAddress = "bin\\synergyspace\\mtinput\\luminja\\MultitouchServer.bat";
			generateBat(serverPath);
			batGenerated = true;
		}
		rt.exec("cmd.exe /c start /min " + batAddress);		
	 	Thread.sleep(START_DELAY);
	 	 
	 	log.info("MultiTouch server started from "+serverPath);
	}
	
		
	private void generateBat(String serverPath) {
	    try{
	        FileWriter fstream = new FileWriter(batAddress);
	        BufferedWriter out = new BufferedWriter(fstream);
	        out.write("cd \"" + serverPath.replace("MultitouchServer.exe", "") + "\" \n");
	        out.write("MultitouchServer.exe");
	        out.close();
	        fstream.close();
	        }catch (Exception e){e.printStackTrace();}
		
	}

	public void kill() throws IOException, InterruptedException{
		rt.exec(killCommand);
	 	Thread.sleep(KILL_DELAY);
		log.info("MultiTouch server is killed");
	}
	/*
	public static void main(String args[]){
		try {
			MIMProcessController.getInstance().start();
			Thread.sleep(10000);
			MIMProcessController.getInstance().kill();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
}
