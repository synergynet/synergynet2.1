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

import synergyspace.mtinput.Blob;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Collection;

public class GamePanel{

	private static final long serialVersionUID = 1L;
	public Blob[] currentBlobs = new Blob[0];
	private PingPong game;
	private double prevLAngle = 0;
	private double prevRAngle = 0;
	private Point2D.Float prevLPoint;
	private Point2D.Float prevRPoint;

	public GamePanel(PingPong g){
		game=g;
	}

	public double getLAngle(){
		double result = 0;
		boolean first = true;
		int count = 0;
		double xMin = 0, yMin = 0, xMax = 0, yMax = 0;
		for (int i = 0; i < currentBlobs.length; i++){
			if (currentBlobs[i].x < Toolkit.getDefaultToolkit().getScreenSize().width/2){
				count++;
			}
		}
		if (count == 0){
			result = prevLAngle;
		} else if (count == 1){
			result = 0;
		}else{
			for (int i = 0; i < currentBlobs.length; i++){
				if (currentBlobs[i].x < Toolkit.getDefaultToolkit().getScreenSize().width/2){
					if (currentBlobs[i].x < xMin && !first){
						xMin = currentBlobs[i].x;
						yMin = currentBlobs[i].y;
					} else if (currentBlobs[i].x > xMax && !first){
						xMax = currentBlobs[i].x;
						yMax = currentBlobs[i].y;
					} else if (first){
						xMin = currentBlobs[i].x;
						yMin = currentBlobs[i].y;
						xMax = currentBlobs[i].x;
						yMax = currentBlobs[i].y;
						first = false;
					}
				}
			}
			yMin = Toolkit.getDefaultToolkit().getScreenSize().height - yMin;
			yMax = Toolkit.getDefaultToolkit().getScreenSize().height - yMax;
			if(xMin == xMax){
				result = 90;
			} else if(yMin == yMax){
				result = 0;
			} else if(yMin > yMax){
				result = -java.lang.Math.atan2((yMin - yMax),(xMax - xMin));
			} else{
				result = -java.lang.Math.atan2((yMin - yMax), (xMax - xMin));
			}
		}
		prevLAngle = result;
		return result;
	}

	public double getRAngle(){
		double result = 0;
		boolean first = true;
		int count = 0;
		double xMin = 0, yMin = 0, xMax = 0, yMax = 0;
		for (int i = 0; i < currentBlobs.length; i++){
			if (currentBlobs[i].x >= Toolkit.getDefaultToolkit().getScreenSize().width/2){
				count++;
			}
		}
		if (count == 0){
			result = prevRAngle;
		} else if (count == 1){
			result = 0;
		}else{
			for (int i = 0; i < currentBlobs.length; i++){
				if (currentBlobs[i].x >= Toolkit.getDefaultToolkit().getScreenSize().width/2){
					if (currentBlobs[i].x < xMin && !first){
						xMin = currentBlobs[i].x;
						yMin = currentBlobs[i].y;
					} else if (currentBlobs[i].x > xMax && !first){
						xMax = currentBlobs[i].x;
						yMax = currentBlobs[i].y;
					} else if (first){
						xMin = currentBlobs[i].x;
						yMin = currentBlobs[i].y;
						xMax = currentBlobs[i].y;
						yMax = currentBlobs[i].x;
						first = false;
					}
				}
			}
			yMin = Toolkit.getDefaultToolkit().getScreenSize().height - yMin;
			yMax = Toolkit.getDefaultToolkit().getScreenSize().height - yMax;
			if(xMin == xMax){
				result = 90;
			} else if(yMin == yMax){
				result = 0;
			} else if(yMin > yMax){
				result = -java.lang.Math.atan2((yMin - yMax),(xMax - xMin));
			} else{
				result = -java.lang.Math.atan2((yMin - yMax), (xMax - xMin));
			}
		}

		prevRAngle = result;
		return result;
	}

	public Point2D.Float getTouchPositionLeft(){
		float avgX = 0;
		float avgY = 0;
		int count = 0;
		for (int i = 0; i < currentBlobs.length; i++){
			if (currentBlobs[i].x <= Toolkit.getDefaultToolkit().getScreenSize().width/2){
				avgX = avgX + currentBlobs[i].x;
				avgY = avgY + currentBlobs[i].y;
				count++;
			}
		}
		Point2D.Float result = new Point2D.Float();
		if (count == 0 || (avgX == 0 && avgY == 0)){
			result = prevLPoint;
		} else{
			result.setLocation(avgX/count, avgY/count);
		}
		prevLPoint = result;
		return result;
	}

	public Point2D.Float getTouchPositionRight(){
		float avgX = 0;
		float avgY = 0;
		int count = 0;
		for (int i = 0; i < currentBlobs.length; i++){
			if (currentBlobs[i].x > Toolkit.getDefaultToolkit().getScreenSize().width/2){
				avgX = avgX + currentBlobs[i].x;
				avgY = avgY + currentBlobs[i].y;
				count++;
			}
		}
		Point2D.Float result = new Point2D.Float();
		if (count == 0 || (avgX == 0 && avgY == 0)){
			result = prevRPoint;
		} else{
			result.setLocation(avgX/count, avgY/count);
		}
		prevRPoint = result;
		return result;
	}

	public void paint(Graphics g){
		Color c=game.getBackgroundColor();
		if(c!=null){
			g.setColor(c);
			g.fillRect(0,0,Toolkit.getDefaultToolkit().getScreenSize().width,
					Toolkit.getDefaultToolkit().getScreenSize().height);
		}
		game.paint(g);
	}

	public void setBlob(Collection<Blob> values) {

		currentBlobs = new Blob[values.size()];
		for (int i = 0; i < values.size(); i++){

			currentBlobs[i] = (Blob)values.toArray()[i];
		}

	}

}

