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

import java.awt.Point;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Particle {
	public double LifeSpan; //seconds
	public double Age; //seconds
	public Color Col;
	public Point.Double Location, Velocity;
	private static double Weight = 500 * GravitySystem.G;
	private static double MaxV = 100;
	
	public Particle(Point.Double location, Point.Double velocity, Color col, double lifespan)
	{
		this.Age = 0.0;
		this.LifeSpan = lifespan;
		this.Col = col;
		this.Location = location;
		this.Velocity = velocity;
	}
	
	public boolean Dead()
	{
		return Age >= LifeSpan;
	}
	
	public void Move(double time) //time in seconds
	{	
		Age += time;
		Location.x += Velocity.x * time;
		Location.y += Velocity.y * time;
	}
	
	public void Move(double time, ArrayList<Well> wells) //time in seconds
	{
		Point.Double forces = GetForces(wells);
		Point.Double oldV = (Point2D.Double)Velocity.clone();
		
		Velocity.x -= time * forces.x;
		Velocity.y -= time * forces.y;
		if (Math.sqrt(Math.pow(Velocity.x, 2) + Math.pow(Velocity.x, 2)) > MaxV) 
		{
			Age += time;
			Velocity = oldV; 
		}
		Age += time;
		Location.x += Velocity.x * time;
		Location.y += Velocity.y * time;
	}
	
	private Point.Double GetForces(ArrayList<Well> wells)
	{
		Point.Double forces = new Point2D.Double(0, 0);
		for(Well w : wells)
		{
			if (!(w instanceof FixedWell)) continue;
			double dx = Location.x - w.Location.x; 
			double dy = Location.y - w.Location.y;
			double distS = Math.pow(dx, 2) + Math.pow(dy, 2);
			double dist = Math.sqrt(distS);
			if (dist < w.HalfRad)
			{
				Age += LifeSpan;
				return forces;
			}
			double force = GetForce(w, distS);
			double factor = force / dist;
			forces.x += factor * dx;
			forces.y += factor * dy;
		}
		return forces;
	}
	
	public double GetForce(Well a, double distS)
	{
		return Weight * a.Mass / distS;
	}
}
