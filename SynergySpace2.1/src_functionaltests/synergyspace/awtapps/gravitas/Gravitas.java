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

package synergyspace.awtapps.gravitas;


import synergyspace.awt.AWTMultiTouch;

public class Gravitas {	
	
	//Useful fields to change
	public static final double drifterMass = 200.0; //The mass of the drifters (moving wells)
	public static final double maxV = 800; //Maximum speed a drifter can move
	public static final double maxMass = 1000; //The heaviest object you can have
	public static final double minMass = 50; //Guess...
	public static double massLossRate = 0.15; //Units of mass lost per second on pressure wells
	public static final int maxParticles = 2000;
	public static final int maxDrifters = 50; //The most drifters the system can have
	public static boolean particleMass = true; //Do particles have mass?  Are they affected by gravity?
	public static final int minTrailLength = 5; //The minimum length of a trail (length is measured in frames, not time or pixels)
	public static final int maxTrailLength = 100; //Guess
	
	//Control panel settings
	public static int particles = 1500; //The number of particles allowed.  Set to 0 to disable
	public static boolean useTrails = true; //Whether or not to use trails
	public static int trailLength = 20; //The current length of the trails
	
	//Initial wells
	public static final int fixedWells = 2;
	public static final int drifters = 10;
	
	//Pressure related stuff
//	public static final double pressureHit = 0.03; //The pressure required to scatter the boids
//	public static final double MaxPressure = 0.02; //The highest acceptable pressure value
//	public static final double MinPressure = 0.005; //The lowest acceptable pressure value
	
	public static final double pressureHit = 0.03; //The pressure required to create/destroy a fixed well
	public static final double maxPressure = 0.02;
	public static final double minPressure = 0.005;
	
	
	public static void main(String[] args) {
		//AWTMultiTouch app = new AWTMultiTouch(new GravitySystem(GravitySystem.Random.nextInt(10) + 2, 15));
		AWTMultiTouch app = new AWTMultiTouch(new GravitySystem(fixedWells, drifters));
		app.setFPS(50);
	}
}