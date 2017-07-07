/*
 * Copyright (c) 2008 University of Durham, England
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

package synergynet.table.apps.pong;


import java.awt.Color;
import java.awt.Font;

import com.jme.system.DisplaySystem;
import com.sun.java.util.collections.Random;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.ImageTextLabel;
import synergynet.contentsystem.items.RoundImageLabel;
import synergynet.contentsystem.items.TextLabel;
import synergynet.contentsystem.items.listener.ItemListener;
import synergynet.table.SynergyNetAppUtils;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.menucontrol.HoldBottomLeftExit;
import synergynet.table.apps.DefaultSynergyNetApp;

public class PongApp extends DefaultSynergyNetApp {

	//When bouncing off objects is implemented properly this game will be playable

	private boolean start = true;

	private static final int SPEED = 250;
	private static final int TIMEOUT = 2000;
	private static final int LIMIT = 5;

	int blueScore = 0;
	int redScore = 0;


	private RoundImageLabel ball;
	private ImageTextLabel red, blue;
	private TextLabel endText, titleText;

	public PongApp(ApplicationInfo info) {
		super(info);
	}

	private ContentSystem contentSystem;

	@Override
	public void addContent() {
		SynergyNetAppUtils.addTableOverlay(this);
		setMenuController(new HoldBottomLeftExit());
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
	}

	@Override
	public void onActivate() {
		contentSystem.removeAllContentItems();
		reset();
		addInitialParts();
	}

	private void addInitialParts() {

		titleText = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		titleText.setBackgroundColour(Color.black);
		titleText.setBorderColour(Color.black);
		titleText.setTextColour(Color.white);
		titleText.setText("" + blueScore + " - " + redScore);
		titleText.setFont(new Font("Arial", Font.PLAIN,30));
		titleText.setLocalLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight() - titleText.getHeight());
		titleText.setRotateTranslateScalable(false);

		red = (ImageTextLabel)this.contentSystem.createContentItem(ImageTextLabel.class);
		red.setBorderSize(0);
		red.setBackgroundColour(Color.red);
		red.setRotateTranslateScalable(false);
		red.setAutoFitSize(false);
		red.setHeight(DisplaySystem.getDisplaySystem().getRenderer().getHeight());
		red.setWidth(40);
		red.setSteadfastLimit(1);
		red.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth(),
				DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2);
		addPaddle(Color.red, (DisplaySystem.getDisplaySystem().getRenderer().getWidth()/4)*3,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2);


		blue = (ImageTextLabel)this.contentSystem.createContentItem(ImageTextLabel.class);
		blue.setBorderSize(0);
		blue.setBackgroundColour(Color.blue);
		blue.setRotateTranslateScalable(false);
		blue.setAutoFitSize(false);
		blue.setHeight(DisplaySystem.getDisplaySystem().getRenderer().getHeight());
		blue.setWidth(40);
		blue.setSteadfastLimit(1);
		blue.setLocation(0,	DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2);
		addPaddle(Color.blue, DisplaySystem.getDisplaySystem().getRenderer().getWidth()/4,
					DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2);

		ball = (RoundImageLabel)this.contentSystem.createContentItem(RoundImageLabel.class);
		ball.setBorderSize(0);
		ball.setBackgroundColour(Color.white);
		ball.setRadius(10);
		ball.setSteadfastLimit(1);
		ball.setLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2);
		ball.makeFlickable(0f);
		ball.setRotateTranslateScalable(false);
		ball.setScaleLimit(1f, 1f);
		ball.addItemListener(new ItemListener(){

			@Override
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
				if(start){
					if(new Random().nextBoolean()){
						ball.flick(-SPEED, 0, 0);
					}else{
						ball.flick(SPEED, 0, 0);
					}
					start = false;
				}
			}

			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}

			@Override
			public void cursorLongHeld(ContentItem item, long id, float x, float y, float pressure) {

			}

			@Override
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorRightClicked(ContentItem item, long id, float x, float y, float pressure) {}
		});

	}

	private void addPaddle(Color colour, float x, float y){

		final ImageTextLabel paddle = (ImageTextLabel)this.contentSystem.createContentItem(ImageTextLabel.class);
		paddle.setSteadfastLimit(1);
		paddle.setBorderSize(0);
		paddle.setBackgroundColour(colour);
		paddle.makeBounceOffable(orthoNode);
		paddle.setLocation(x, y);
		paddle.makeFlickable(3f);
		paddle.setScaleLimit(0.75f, 1.5f);
		paddle.setAutoFitSize(false);
		paddle.setHeight(100);
		paddle.setWidth(20);
	}

	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(contentSystem != null) contentSystem.update(tpf);
		if(ball != null && blue != null && red != null){
			if(ball.hasCollision(red)){
				blueScore++;
				titleText.setText("" + blueScore + " - " + redScore);
				ball.setLocalLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2);
				if (blueScore < LIMIT){
					ball.flick(SPEED, 0, 0);
				}else{
					win("blue");
				}
			}else if(ball.hasCollision(blue)){
				redScore++;
				titleText.setText("" + blueScore + " - " + redScore);
				ball.setLocalLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, DisplaySystem.getDisplaySystem().getRenderer().getHeight()/2);
				if (redScore < LIMIT){
					ball.flick(-SPEED, 0, 0);
				}else{
					win("red");
				}
			}
		}
		if (start && !zeroAll && titleText != null){
			titleText.setText("" + 0 + " - " + 0);
			zeroAll = true;
		}

	}

	boolean zeroAll = true;

	private void win(String colour) {
		ball.flick(0, 0, 0);
		endText = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		endText.setBackgroundColour(Color.black);
		if (colour.equals("blue")){
			endText.setTextColour(Color.blue);
			endText.setText("Blue Wins!");
		}else{
			endText.setTextColour(Color.red);
			endText.setText("Red Wins!");
		}
		endText.setFont(new Font("Arial", Font.BOLD,50));
		endText.setLocalLocation(DisplaySystem.getDisplaySystem().getRenderer().getWidth()/2, DisplaySystem.getDisplaySystem().getRenderer().getHeight()/4);
		endText.setRotateTranslateScalable(false);
		new Thread(){public void run(){
			try {
				sleep(TIMEOUT);
			} catch (Exception e) {
				e.printStackTrace();
			}
			reset();
			endText.setVisible(false, true);
		}}.start();

	}

	private void reset(){
		zeroAll = false;
		blueScore = 0;
		redScore = 0;
		start = true;
	}

}
