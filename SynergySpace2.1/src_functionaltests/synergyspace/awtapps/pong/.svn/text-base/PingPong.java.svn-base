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

package synergyspace.awtapps.pong;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.*;
import java.util.Random;
import java.awt.geom.Area;

public class PingPong{
	Rectangle batOneLocation = new Rectangle();
	Rectangle batTwoLocation = new Rectangle();
	int speed = 5;
	int rcount = 0, lcount = 0, displayTime = 0;
	public static final int BATWIDTH=100;
	public static final int BATHEIGHT=10;
	private int batOneX=Toolkit.getDefaultToolkit().getScreenSize().width/4;
	private int batOneY=Toolkit.getDefaultToolkit().getScreenSize().height/2;
	private int batTwoX=Toolkit.getDefaultToolkit().getScreenSize().width/4*3;
	private int batTwoY=Toolkit.getDefaultToolkit().getScreenSize().height/2;
	Random generator = new Random();
	private int ballX=(generator.nextInt(2)+1)* Toolkit.getDefaultToolkit().getScreenSize().width/3, ballY=10;
	private int ballSpeedX=0;
	private int ballSpeedY=speed;
	private static final int BALLRADIUS=10;
	private GamePanel gp;

	public Color getBackgroundColor(){
		return Color.black;
	}

	public void paint(Graphics g2D) {
		Graphics2D g = (Graphics2D) g2D;
		ballX=ballX+ballSpeedX;
		ballY=ballY+ballSpeedY;
		Point2D.Float mpOne=gp.getTouchPositionLeft();
		Point2D.Float mpTwo=gp.getTouchPositionRight();
		if(mpOne!=null)
		{
			batOneX=(int)mpOne.x;
			batOneY=(int)(Toolkit.getDefaultToolkit().getScreenSize().height - mpOne.y);
		}
		if(mpTwo!=null)
		{
			batTwoX=(int)mpTwo.x;
			batTwoY=(int)(Toolkit.getDefaultToolkit().getScreenSize().height - mpTwo.y);
		}
		g.setColor(Color.white);
		g.fillRect(Toolkit.getDefaultToolkit().getScreenSize().width/2,
				Toolkit.getDefaultToolkit().getScreenSize().height/12
				,2,((Toolkit.getDefaultToolkit().getScreenSize().height)/6)*5);

		g.setColor(Color.red);
		g.rotate(gp.getLAngle(), (double)batOneX, (double)batOneY);
		g.fillRect(batOneX-BATWIDTH/2,batOneY-BATHEIGHT/2,BATWIDTH,BATHEIGHT);
		batOneLocation = new Rectangle(batOneX-BATWIDTH/2,batOneY-BATHEIGHT/2,BATWIDTH,BATHEIGHT);
		g.rotate(-gp.getLAngle(), (double)batOneX, (double)batOneY);


		g.setColor(Color.blue);
		g.rotate(gp.getRAngle(), (double)batTwoX, (double)batTwoY);
		g.fillRect(batTwoX-BATWIDTH/2,batTwoY-BATHEIGHT/2,BATWIDTH,BATHEIGHT);
		batTwoLocation = new Rectangle(batTwoX-BATWIDTH/2,batTwoY-BATHEIGHT/2,BATWIDTH,BATHEIGHT);
		g.rotate(-gp.getRAngle(), (double)batTwoX, (double)batTwoY);


		g.setColor(Color.white);
		g.fillOval(ballX-BALLRADIUS,ballY-BALLRADIUS,BALLRADIUS*2,BALLRADIUS*2);
		g.setFont(new Font("", 5, 50));
		g.drawString(""+lcount, Toolkit.getDefaultToolkit().getScreenSize().width/4, 50);
		g.drawString(""+rcount, Toolkit.getDefaultToolkit().getScreenSize().width/4*3, 50);
		if (lcount > 4){
			g.setColor(Color.red);
			g.drawString("Red Wins!", Toolkit.getDefaultToolkit().getScreenSize().width/2-100, 50);
			displayTime++;
		}
		if (rcount > 4){
			g.setColor(Color.blue);
			g.drawString("Blue Wins!", Toolkit.getDefaultToolkit().getScreenSize().width/2-100, 50);
			displayTime++;
		}
		if ((lcount > 4 || rcount > 4) && displayTime > 100 ){
			lcount = 0;
			rcount = 0;
			displayTime = 0;
		} else {

		}
		checkCollisions();
	}

	public void checkCollisions(){
		if(ballX-BALLRADIUS<0){
			ballSpeedX=-ballSpeedX;
		}
		if(ballY-BALLRADIUS<0){
			ballSpeedY=-ballSpeedY;
		}
		if(ballX+BALLRADIUS>Toolkit.getDefaultToolkit().getScreenSize().width){
			ballSpeedX=-ballSpeedX;
		}
		if(ballY+BALLRADIUS>Toolkit.getDefaultToolkit().getScreenSize().height){
			if (ballX <= Toolkit.getDefaultToolkit().getScreenSize().width/2){
				if (lcount < 5 && rcount < 5){
					rcount++;
				}
				ballX=(Toolkit.getDefaultToolkit().getScreenSize().width/3)*2;
			}else{
				if (lcount < 5 && rcount < 5){
					lcount++;
				}
				ballX=(Toolkit.getDefaultToolkit().getScreenSize().width/3);
			}
			ballY=10;
			ballSpeedX=0;
			ballSpeedY=speed;
		}
		Point ball= new Point(ballX, ballY);

		if (new Area(batOneLocation).contains(ball) )
		{
			if (gp.getLAngle() == 0 || gp.getLAngle() == 90 || gp.getLAngle() == -90){
				ballSpeedX=(ballX-batOneX)/5;
				ballSpeedY=-(int)Math.sqrt((speed*speed)-ballSpeedX^2);
			} else if (gp.getLAngle() < 0){
				ballSpeedX=(int)(((ballX-batOneX)/5) - ((-gp.getLAngle()/180)*(ballX-batOneX)/5));
				ballSpeedY=-(int)(Math.sqrt((speed*speed)-ballSpeedX^2) -
						((-gp.getLAngle()/75)*Math.sqrt((speed*speed)-ballSpeedX^2)));
			} else {
				ballSpeedX=(int)(((ballX-batOneX)/5) + ((gp.getLAngle()/180)*(ballX-batOneX)/5));
				ballSpeedY=-(int)(Math.sqrt((speed*speed)-ballSpeedX^2) -
						((gp.getLAngle()/75)*Math.sqrt((speed*speed)-ballSpeedX^2)));
			}
		}

		if (new Area(batTwoLocation).contains(ball) )
		{
			if (gp.getRAngle() == 0 || gp.getRAngle() == 90 || gp.getRAngle() == -90){
				ballSpeedX=(ballX-batTwoX)/5;
				ballSpeedY=-(int)Math.sqrt((speed*speed)-ballSpeedX^2);
			} else if (gp.getRAngle() > 0){
				ballSpeedX=(int)(((ballX-batTwoX)/5) - ((-gp.getRAngle()/180)*(ballX-batTwoX)/5));
				ballSpeedY=-(int)(Math.sqrt((speed*speed)-ballSpeedX^2) -
						((-gp.getRAngle()/75)*Math.sqrt((speed*speed)-ballSpeedX^2)));
			} else {
				ballSpeedX=(int)(((ballX-batTwoX)/5) + ((gp.getRAngle()/180)*(ballX-batTwoX)/5));
				ballSpeedY=-(int)(Math.sqrt((speed*speed)-ballSpeedX^2) -
						((gp.getRAngle()/75)*Math.sqrt((speed*speed)-ballSpeedX^2)));
			}
		}
	}

	public void setup(GamePanel gp) {
		this.gp = gp;

	}
}

