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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;


public class Drifter extends Well {
	private static final double ParticleDrag = 0.5;
	private static final int MaxParticles = 4;
	public double VX, VY;
	private double Weight;
	private static Color[] trailCols = null;
	private Point.Double[] trails = null;
	
	public Drifter(int width, int height)
	{
		this(new Point2D.Double(GravitySystem.Random.nextInt(width - 200) + 100, GravitySystem.Random.nextInt(height - 200) + 100), Gravitas.drifterMass);
	}
	
	public Drifter(Point.Double location, double mass)
	{
		super(location, mass, DrifterDensity, new Color(164, 164, 212));
		this.VX = 0;
		this.VY = 0;
		this.Weight = GravitySystem.G * Mass;
		initTrails(false);
	}
	
	public void initTrails(boolean resize)
	{
		if (trailCols == null || resize)
		{
			trailCols = new Color[Gravitas.trailLength];
			for(int a = 0; a < Gravitas.trailLength; a++)
			{
				int shade = 64 + 192 * a / Gravitas.trailLength;
				trailCols[a] = new Color(shade, shade, shade);
			}
		}
		if (!resize)
		{
			trails = new Point.Double[Gravitas.trailLength];
			for(int a = 0; a < Gravitas.trailLength; a++)
				trails[a] = new Point2D.Double(Location.x, Location.y);
		}
		else if (trails.length < Gravitas.trailLength)
		{
			Point.Double[] newTrails = new Point.Double[Gravitas.trailLength];
			int a = 0;
			for(; a < trails.length; a++)
				newTrails[a] = trails[a];
			for(; a < Gravitas.trailLength; a++)
				newTrails[a] = new Point2D.Double(Location.x, Location.y);
			trails = newTrails;
		}
		else if (trails.length > Gravitas.trailLength)
		{
			Point.Double[] newTrails = new Point.Double[Gravitas.trailLength];
			int b = trails.length - 1;
			for(int a = Gravitas.trailLength - 1; a > 0; a--)
			{
				newTrails[a] = trails[b];
				b--;
			}
			trails = newTrails;
		}
	}
	
	private void updateTrails()
	{
		if (trails.length != Gravitas.trailLength)
		{
			initTrails(true);
		}
		if (!Gravitas.useTrails)
		{
			for(int a = 0; a < Gravitas.trailLength; a++)
				trails[a] = (Point2D.Double)Location.clone();
			return;
		}
		for(int a = 1; a < Gravitas.trailLength; a++)
			trails[a - 1] = trails[a];
		trails[Gravitas.trailLength - 1] = (Point2D.Double)Location.clone();
	}
	
	public void Update(int time, ArrayList<Well> world)
	{
		Point.Double forces = GetForces(world);
		double seconds = (double)time / 1000.0;
		double OVX = VX;
		double OVY = VY;
		VX -= seconds * forces.x;
		VY -= seconds * forces.y;
		if (Math.sqrt(Math.pow(VX, 2) + Math.pow(VY, 2)) > Gravitas.maxV)
		{
			VX = OVX;
			VY = OVY;
		} 
		Location.x += VX * seconds;
		Location.y += VY * seconds;
		AddParticle();
		updateTrails();
	}
	
	protected void AddParticle()
	{
		if (Math.abs(VX) + Math.abs(VY) < 70) return;
		for(int a = 0; a < GravitySystem.Random.nextInt(MaxParticles); a++)
		{
			Point2D.Double vel = new Point2D.Double(VX * ParticleDrag * ((Math.random() / 20) + 0.975), VY * ParticleDrag * ((Math.random() / 20) + 0.975));
			int Shade = GravitySystem.Random.nextInt(172) + 84;
			GravitySystem.PS.AddParticle(new Color(Shade, Shade, Shade), new Point2D.Double(Location.x, Location.y), vel, Math.random() * 3 + 1);
		}
	}
	
	private Point.Double GetForces(ArrayList<Well> world) 
	{
		Point.Double forces = new Point2D.Double(0, 0);
		for(Well w : world)
		{
			if (w.ID == ID) continue;
			double dx = Location.x - w.Location.x; 
			double dy = Location.y - w.Location.y;
			double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
			double force = 0.0;
			if (dist < CloseRange)
			{
				if (dist < TooClose) continue;
				force = GetForce(w, dx, dy) * Repulsion;
			} 	
			else
				force = GetForce(w, dx, dy);
			double factor = force / dist;
			forces.x += factor * dx;
			forces.y += factor * dy;
		}
		return forces;
	}
	
	public double GetForce(Well a, double dx, double dy)
	{
		return Weight * a.Mass / (Math.pow(dx, 2) + Math.pow(dy, 2));
	}
	
	public void Render(Graphics2D g2d, int pass)
	{
		if (Gravitas.useTrails)
		{
			g2d.setColor(Color.white);
			for(int a = 0; a < Gravitas.trailLength - 1; a++)
			{
				g2d.setColor(trailCols[a]);
				g2d.drawLine((int)trails[a].x, (int)trails[a].y, (int)trails[a + 1].x, (int)trails[a+1].y);
			}
			g2d.setColor(trailCols[Gravitas.trailLength - 1]);
			g2d.drawLine((int)trails[Gravitas.trailLength - 1].x, (int)trails[Gravitas.trailLength - 1].y, (int)Location.x, (int)Location.y);
		}
		else
		{
			g2d.setColor(Col);
			g2d.fill(new Ellipse2D.Double(Location.x - Radius, Location.y - Radius, Diameter, Diameter));
		}
	}
}
