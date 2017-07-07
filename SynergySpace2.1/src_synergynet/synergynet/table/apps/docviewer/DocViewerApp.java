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

package synergynet.table.apps.docviewer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ControlBar;
import synergynet.contentsystem.items.PDFViewer;
import synergynet.contentsystem.items.PPTViewer;
import synergynet.contentsystem.items.RoundFrame;
import synergynet.contentsystem.items.SketchPad;
import synergynet.contentsystem.items.SwingContainer;
import synergynet.contentsystem.items.TextLabel;
import synergynet.contentsystem.items.VideoPlayer;
import synergynet.contentsystem.items.Window;
import synergynet.contentsystem.jme.items.utils.controlbar.ControlBarMover.ControlBarMoverListener;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.groupcontrol.GroupController;
import synergynet.table.appregistry.menucontrol.HoldBottomLeftExit;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergyspace.jme.sysutils.CameraUtility;

public class DocViewerApp extends DefaultSynergyNetApp {
	
	private ContentSystem contentSystem;

	public DocViewerApp(ApplicationInfo info) {
		super(info);		
	}

	@Override
	public void addContent() {
		
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);		
		setMenuController(new HoldBottomLeftExit());
		new GroupController(this);
		
		final SwingContainer sw = (SwingContainer) contentSystem.createContentItem(SwingContainer.class);
		sw.centerItem();
		
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                // make it transparent blue
                sw.getJDesktopPane().setBackground( new Color( 0, 0, 1, 0.2f ) );

                final JButton button = new JButton( "click me" );
                sw.getJDesktopPane().add( button );
                button.setLocation( 200, 200 );
                button.setSize( button.getPreferredSize() );
                button.addActionListener( new ActionListener() {
                    public void actionPerformed( ActionEvent e ) {
                    	System.out.println("Hello");
                    }
                } );
            }
        } );

		//BackgroundController backgroundController = (BackgroundController)contentSystem.createContentItem(BackgroundController.class);
		//backgroundController.setOrder(-999999999);
	
		
		//InnerNoteController innerNoteController = new InnerNoteController();
		/*
		MediaPlayer videoTemp = (MediaPlayer)contentSystem.createContentItem(MediaPlayer.class);
		videoTemp.setMediaURL(DocViewerApp.class.getResource("smallvid.mp4"));
		videoTemp.setVisible(false);
	
		MediaPlayer video = (MediaPlayer)contentSystem.createContentItem(MediaPlayer.class);
		video.setMediaURL(DocViewerApp.class.getResource("smallvid.mp4"));
		video.setLocalLocation(0, 600);
		video.makeFlickable(0.5f);
		*/
//		MediaPlayer video1 = (MediaPlayer)contentSystem.createContentItem(MediaPlayer.class);
//		video1.setMediaURL(DocViewerApp.class.getResource("w.mp4"));
//		video1.setLocalLocation(500, 300);
//		
//		MediaPlayer video2 = (MediaPlayer)contentSystem.createContentItem(MediaPlayer.class);
//		video2.setMediaURL(DocViewerApp.class.getResource("c.mp4"));
//		video2.setLocalLocation(300, 500);
//		
//		MediaPlayer video3 = (MediaPlayer)contentSystem.createContentItem(MediaPlayer.class);
//		video3.setMediaURL(DocViewerApp.class.getResource("up.mp4"));
//		video3.setLocalLocation(500, 500);

	//	innerNoteController.addNoteController(video, (QuadContentItem) video.getPlayerFrame());
//		innerNoteController.addNoteController(video1, (QuadContentItem) video1.getPlayerFrame());
////		innerNoteController.addNoteController(video2, (QuadContentItem) video2.getPlayerFrame());
////		innerNoteController.addNoteController(video3, (QuadContentItem) video3.getPlayerFrame());
//		
		/*
		MediaPlayer video2 = (MediaPlayer)contentSystem.createContentItem(MediaPlayer.class);
		video2.setMediaURL(DocViewerApp.class.getResource("k.mp4"));
		video2.setLocalLocation(300, 500);
		
		MediaPlayer video5 = (MediaPlayer)contentSystem.createContentItem(MediaPlayer.class);
		video5.setMediaURL(DocViewerApp.class.getResource("k.mp4"));
		video5.setLocalLocation(500, 500);
		
		MediaPlayer video6 = (MediaPlayer)contentSystem.createContentItem(MediaPlayer.class);
		video6.setMediaURL(DocViewerApp.class.getResource("k.mp4"));
		video6.setLocalLocation(500, 500);
		
		MediaPlayer video7 = (MediaPlayer)contentSystem.createContentItem(MediaPlayer.class);
		video7.setMediaURL(DocViewerApp.class.getResource("k.mp4"));
		video7.setLocalLocation(500, 500);
		
		MediaPlayer video8 = (MediaPlayer)contentSystem.createContentItem(MediaPlayer.class);
		video8.setMediaURL(DocViewerApp.class.getResource("k.mp4"));
		video8.setLocalLocation(500, 500);
		*/
				
		PPTViewer ppt = (PPTViewer)contentSystem.createContentItem(PPTViewer.class);
		ppt.setPPTFile(DocViewerApp.class.getResource("synergynet-july09.ppt"));
		ppt.placeRandom();
		ppt.makeFlickable(2);
		
		PDFViewer pdf = (PDFViewer)this.contentSystem.createContentItem(PDFViewer.class);
		pdf.setPdfFile(new File("assets/resources/pdfs/test.pdf"));
		pdf.setWidth(350);
		pdf.placeRandom();
		pdf.makeFlickable(2f);
		
		
		final Window window = (Window)contentSystem.createContentItem(Window.class);
		
		ControlBar controlBar = (ControlBar)contentSystem.createContentItem(ControlBar.class);
		controlBar.setControlBarLength(400);
		controlBar.setControlBarWidth(60);
		controlBar.setBarImageResource(DocViewerApp.class.getResource("images/bar.jpg"));
		controlBar.setFinishedBarImageResource(DocViewerApp.class.getResource("images/finishedBar.jpg"));
		
		VideoPlayer videoTemp = (VideoPlayer)contentSystem.createContentItem(VideoPlayer.class);
		videoTemp.setVideoURL(DocViewerApp.class.getResource("smallvid.mp4"));
		videoTemp.setBoundaryEnabled(false);
		videoTemp.centerItem();
		videoTemp.setScale(0.5f);
		
		RoundFrame happyFrame = (RoundFrame) this.contentSystem.createContentItem(RoundFrame.class);
		happyFrame.setRadius(40);
		happyFrame.drawImage(DocViewerApp.class.getResource("images/happy.jpg"));
		happyFrame.setLocation(controlBar.getControlBarLength()/2+happyFrame.getRadius(), 0);
		

		RoundFrame unHappyFrame = (RoundFrame) this.contentSystem.createContentItem(RoundFrame.class);
		unHappyFrame.setRadius(40);
		unHappyFrame.drawImage(DocViewerApp.class.getResource("images/unhappy.jpg"));
		unHappyFrame.setLocation(-controlBar.getControlBarLength()/2-unHappyFrame.getRadius(), 0);
		
		final TextLabel label = (TextLabel) this.contentSystem.createContentItem(TextLabel.class);
		label.setBackgroundColour(Color.black);
		label.setBorderSize(0);
		label.setFont(new Font("Serif", Font.BOLD, 16));
		label.setTextColour(Color.white);
		label.setLocation(0, -80);
		
		window.addSubItem(controlBar);
		window.addSubItem(happyFrame);
		window.addSubItem(unHappyFrame);
		window.addSubItem(label);
		
		window.getBackgroundFrame().setVisible(false, false);
		window.setWidth((int)(happyFrame.getRadius()*2+ happyFrame.getBorderSize()*2));
		window.centerItem();
		
		
		controlBar.addControlBarMoverListener(new ControlBarMoverListener(){

			@Override
			public void controlBarChanged(float oldPosition, float newPosition) {
				label.setText(String.valueOf((int)(newPosition*100))+ " %");
			}

			@Override
			public void cursorPressed() {}

			@Override
			public void cursorReleased() {}
	
		});
		
		final SketchPad pad = (SketchPad) this.contentSystem.createContentItem(SketchPad.class);
		pad.setBorderSize(0);
		pad.setWidth(300);
		pad.setHeight(200);
		pad.setSketchArea(new Rectangle(0,40,300,200));
		pad.centerItem();
		pad.fillRectangle(new Rectangle(0,0,300,40), Color.red);
		pad.fillRectangle(new Rectangle(270,5,25,25), Color.black);
		pad.setClearArea(new Rectangle(270,5,25,25));
		pad.setTextColor(Color.black);
		pad.drawString("Sketch Pad", 110, 15);
		pad.makeFlickable(0.5f);
	}
	
	@Override
	protected void onDeactivate() {
		//video.stop();
		//contentSystem.remove(video);
		//super.onDeactivate();		
	}

	protected Camera getCamera() {
		if(cam == null) {
			cam = CameraUtility.getCamera();
			cam.setLocation(new Vector3f(0f, 10f, 50f));
			cam.lookAt(new Vector3f(), new Vector3f( 0, 0, -1 ));
			cam.update();
		}		
		return cam;
	}

	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(contentSystem != null) contentSystem.update(tpf);
		
		//if(rtl2.hasCollision(list))
		//	System.out.println("Collision");
	}

}
