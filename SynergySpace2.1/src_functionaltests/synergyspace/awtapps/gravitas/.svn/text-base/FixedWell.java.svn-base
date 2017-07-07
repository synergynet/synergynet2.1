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
import java.util.*;
import java.awt.*;
import java.awt.geom.*;

public class FixedWell extends Well {
	protected Color EffectCol;
	protected double EffectRad;
	
	public FixedWell(int width, int height) 
	{
		this(new Point2D.Double(GravitySystem.Random.nextInt(width - 200) + 100, GravitySystem.Random.nextInt(height - 200) + 100), GravitySystem.Random.nextInt((int)(Gravitas.maxMass - Gravitas.minMass)) + Gravitas.minMass, FixedWellDensity, true);
	}
	
	public FixedWell(Point.Double location, double pressure)
	{
		this(location, Gravitas.minMass + GravitySystem.Random.nextInt((int)(Gravitas.maxMass - Gravitas.minMass)), FixedWellDensity, true);
	}
	
	public FixedWell(Point.Double location, double mass, double density, boolean fixed)
	{
		super(location, mass, density, fixed ? new Color(64, 64, 82) : new Color(82, 64, 64));
		this.EffectCol = new Color(64, 64, 192);
		this.EffectRad = 0;
	}
	
	public void Update(int time, ArrayList<Well> world)
	{
		if (EffectRad == 0 && GravitySystem.Random.nextInt(50) == 1) EffectRad = Radius;
	}
	
//	protected void AddParticle()
//	{
//		double rad = (Radius / 3) * (1 + Math.random()); 
//		double angle = Math.toRadians(Math.random() * 360.0);
//		Point2D.Double loc = new Point2D.Double(Location.x + (rad * Math.cos(angle)), Location.y + (rad * Math.sin(angle)));
//		double speed = -50.0;
//		Point2D.Double vel = new Point2D.Double(speed * Math.cos(angle), speed * Math.sin(angle));
//		GravitySystem.PS.AddParticle(ParticleCol, loc, vel, (Math.random() * (rad / Math.sqrt(Math.pow(speed, 2) + Math.pow(speed, 2)))));
//	}
	
	public void Render(Graphics2D g2d, int pass) //Pass 0 = main block, Anything else = effects
	{
		if (pass == 0)
		{
			g2d.setColor(Col);
			g2d.fill(new Ellipse2D.Double(Location.x - Radius, Location.y - Radius, Diameter, Diameter));
			return;
		}
		if (EffectRad > 0)
		{
			EffectRad --;
			if (EffectRad >= Radius) EffectRad = Radius;
			if (EffectRad < 3) EffectRad = 0;
			else
			{
				g2d.setColor(EffectCol);
				g2d.draw(new Ellipse2D.Double(Location.x - EffectRad, Location.y - EffectRad, EffectRad * 2, EffectRad * 2));
			}
		}
	}
}