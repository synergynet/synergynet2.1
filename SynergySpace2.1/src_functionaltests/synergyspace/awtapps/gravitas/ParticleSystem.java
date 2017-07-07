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
import java.awt.Point;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

public class ParticleSystem {
	public ArrayList<Particle> Particles;
	private long LastTime;
	private int Width, Height;
	
	public ParticleSystem(int width, int height)
	{
		this.Width = width;
		this.Height = height;
		Particles = new ArrayList<Particle>(Gravitas.particles);
		LastTime = 0;
	}
	
	public void AddParticle(Color col, Point.Double location, Point.Double velocity, double life)
	{
		if (Gravitas.particles == 0 || Gravitas.useTrails) return;
		while(Particles.size() >= Gravitas.particles)
			Particles.remove(0);
		Particles.add(new Particle(location, velocity, col, life));
	}
	
	public void Update(ArrayList<Well> wells)
	{
		long time = System.currentTimeMillis();
		double seconds = ((double)(time - LastTime)) / 1000.0;
		LastTime = time;
		Iterator<Particle> it = Particles.iterator();
		while(it.hasNext())
		{
			Particle p = it.next();
			if (Gravitas.particleMass) p.Move(seconds, wells);
			else p.Move(seconds);
			if (p.Dead() || p.Location.x < 0 || p.Location.x > Width || p.Location.y < 0 || p.Location.y > Height)
				it.remove();
		}
	}
	
	public void setParticles(int count)
	{
		if (count < 0) count = 0;
		if (count > Gravitas.maxParticles) count = Gravitas.maxParticles;
		Gravitas.particles = count;
	}
	
	public int Count()
	{
		return Particles.size();
	}
	
	public void render(Graphics2D g2d)
	{
		for(Particle p : Particles)
		{
			g2d.setColor(p.Col);
			g2d.fillRect((int)p.Location.x, (int)p.Location.y, 1, 1);
		}
	}
}
