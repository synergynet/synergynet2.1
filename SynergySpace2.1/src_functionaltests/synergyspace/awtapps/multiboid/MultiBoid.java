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

package synergyspace.awtapps.multiboid;

import synergyspace.awt.AWTMultiTouch;

public class MultiBoid {	
	
	//Useful fields to change
	public static final double velocity = 150; //Boids move at a constant velocity (pixels/second)
	public static final double bankRate = Math.toRadians(360); //How fast a boid can turn (radians per second)
	public static final boolean drawWalls = true; //Draw a wall around the screen when boids are avoiding walls
	public static final double fearFactor = 3;
	
	//Pressure Related Stuff
	public static final double pressureHit = 0.03; //The pressure required to scatter the boids
	public static final int fearScale = 1000; //How much the pressure is scaled up when scattering flocks
	public static final double MaxPressure = 0.02; //The highest acceptable pressure value
	public static final double MinPressure = 0.005; //The lowest acceptable pressure value
	public static final double blockScale = 1; //How much the size of blocks is scaled up by

//	public static final double pressureHit = 0.02; //The pressure required to create/destroy a fixed well
//	public static final double maxPressure = 0.0457;
//	public static final double minPressure = 0.005;
	//Control panel options
	public static int boidCount = 150; //The number of boids to create
	public static final int maxBoids = 500;
	public static final int minBoids = 1;
	public static boolean useWalls = false; //true: boids try to avoid the walls; false: boids wrap around screen edges
	public static boolean drawVisionLines = false; //Draw the lines showing LOS for boids
	public static boolean dynamicColours = true; //Draw in plain white, or use dynamic colours
	
	public static void main(String[] args) {
		AWTMultiTouch app = new AWTMultiTouch(new BoidSystem(boidCount));
		app.setFPS(50);
	}
}
