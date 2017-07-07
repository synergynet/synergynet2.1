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
import java.awt.geom.Point2D;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Collections;

import synergyspace.awt.AWTAppRenderer;
import synergyspace.mtinput.Blob;

public class GravitySystem extends AWTAppRenderer {
	public static ParticleSystem PS;
	public static Random Random = new Random();
	public static final double G = 9.80665;
	public static final double Damping = -0.5;
	public static final double Drag = 0.99;
	public static final long SmackInterval = 500;
	private static int WellID = 0;
	
	private int frameCount = 0;
	private long lastTime = 0;
	private long lastParticleTime = 0;
	private long prevTime = 0;
	private long lastSmack = 0;
	private long lastSmackID = -1;
	private int fps = 0;
	private int ParticleCount = 0;
	private ArrayList<Well> World;
	private ArrayList<ShieldWall> Walls;
	private ControlPanel controlPanel;
	private int clientWidth = 0;
	public int drifterCount = 0;
		
	public GravitySystem(int NumFixed, int NumMovers)
	{
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		controlPanel = new ControlPanel(screensize, new Dimension(80, screensize.height), this);
		width = screensize.width;
		height = screensize.height;
		PS = new ParticleSystem(getWidth(), height);
		World = new ArrayList<Well>(NumFixed + NumMovers + 20);
		Walls = new ArrayList<ShieldWall>(NumMovers * 2);
		for(int a = 0; a < NumFixed; a++)
			World.add(new FixedWell(getWidth(), height));
		for(int a = 0; a < NumMovers; a++)
			AddDrifter();
	}
	
	public void AddDrifter()
	{
		if (drifterCount < Gravitas.maxDrifters)
		{
			World.add(new Drifter(getWidth(), height));
			Collections.sort(World);
			drifterCount ++;
		}
	}
	
	public void RemoveDrifter()
	{
		if (drifterCount > 1)
		{
			Iterator<Well> it = World.iterator();
			while(it.hasNext())
			{
				if (it.next() instanceof Drifter)
				{
					drifterCount --;
					it.remove();
					return;
				}
			}
		}
	}
	
	public static int GetNewID()
	{
		return WellID++;
	}
	
	public boolean CanSmack()
	{
		return System.currentTimeMillis() - lastSmack > SmackInterval;
	}
	
//	public void ReleaseFaders()
//	{
//		for(Well w : World)
//			if (w instanceof PressureWell)
//				((PressureWell)w).Release();
//	}
	
	public void render(Graphics2D g2d) {
		int Delta = timeDelta();
		calculateFPS();
		g2d.setColor(Color.darkGray);
		g2d.fillRect(0, 0, getWidth(), height);
		
		synchronized(blobs) {
			for(Blob b : blobs.values()) {
				if (b.y == 0 && b.x == 0) continue;
				System.out.println(b.pressure);
				int y = height - b.y;
				if (controlPanel.contains(new Point(b.x, y)))
				{
					controlPanel.click(new Point(b.x, y));
					continue;
				}
				double pressure = b.pressure;
				boolean found = false;
				if (pressure >= Gravitas.pressureHit)
				{
					if (!CanSmack()) continue;
					lastSmackID = (int)b.id;
					lastSmack = System.currentTimeMillis();
					Iterator<Well> wellIt = World.iterator();
					while(wellIt.hasNext())
					{
						Well w = wellIt.next();
						if (w instanceof FixedWell && !(w instanceof PressureWell))
						{
							if (w.Touching(new Point(b.x, y)))
							{
								found = true;
								wellIt.remove();
								break;
							}
						}
					}
					if (!found)
					{
						World.add(new FixedWell(new Point2D.Double(b.x, y), pressure));
						Collections.sort(World);
					}
				}
				else
				{
					if (lastSmackID == (int)b.id) continue;
					for(Well w : World)
						if (w instanceof PressureWell)
						{
							PressureWell f = (PressureWell)w;
							if (f.Touching(new Point(b.x, y)))
							{
								f.Poke(pressure);
								found = true;
							}
						}
					if (!found)
					{
						World.add(new PressureWell(new Point2D.Double(b.x, y), pressure));
						Collections.sort(World);
					}
				}
			}
		}
		
		PS.Update(World);
		Iterator<Well> it = World.iterator();
		while(it.hasNext())
		{
			Well w = it.next();
			if (w instanceof PressureWell)
			{
				PressureWell f = (PressureWell)w;
				f.Update(Delta);
				if (f.IsFinished())
				{
					it.remove();
					continue;
				}
			}
			else if (w instanceof Drifter)
			{
				w.Update(Delta, World);
				BounceDrifter((Drifter)w);
			}
			else
				w.Update(Delta, World);
			w.Render(g2d, 0);
		}
		
		Iterator<ShieldWall> wallIt = Walls.iterator();
		while(wallIt.hasNext())
		{
			ShieldWall s = wallIt.next();
			s.Update(Delta / 1000.0);
			if (s.IsFinished())
				wallIt.remove();
			else
				s.Render(g2d);
		}
		for(Well w : World)
			w.Render(g2d, 1);
		
		PS.render(g2d);
		g2d.setColor(Color.lightGray);
		
		controlPanel.render(g2d);
		
		g2d.drawString("FPS: " + fps, 4, 12);
		g2d.drawString("Wells: " + World.size(), 4, 22);
		g2d.drawString("Particles: " + ParticleCount, 4, 32);
	}
	
	public void AddWall(int x, int y)
	{
		Walls.add(new ShieldWall(x, y, x == 0 || x == getWidth()));
	}
	
	private void BounceDrifter(Drifter d)
	{
		d.VX *= Drag;
		d.VY *= Drag;
		if (d.Location.x < 0)
		{
			d.Location.x = 0;
			d.VX *= Damping;
			AddWall(0, (int)d.Location.y);
		}
		if (d.Location.x >= getWidth())
		{
			d.Location.x = getWidth();
			d.VX *= Damping;
			AddWall(getWidth(), (int)d.Location.y);
		}
		if (d.Location.y < 0)
		{
			d.Location.y = 0;
			d.VY *= Damping;
			AddWall((int)d.Location.x, 0);
		}
		if (d.Location.y > height)
		{
			d.Location.y = height;
			d.VY *= Damping;
			AddWall((int)d.Location.x, height);
		}
	}
	
	private int timeDelta()
	{
		long currentTime = System.currentTimeMillis();
		if (prevTime == 0)
			prevTime = currentTime - 1;
		int result = (int)(currentTime - prevTime);
		prevTime = currentTime;
		return result;
	}

	private void calculateFPS() {
		long currentTime = System.currentTimeMillis();
		if (CanSmack()) lastSmackID = -1;
		if (currentTime - lastParticleTime > 250)
		{
			lastParticleTime = currentTime;
			ParticleCount = PS.Count();
		}
		if(currentTime - lastTime > 1000) {
			fps = frameCount;
			frameCount = 0;
			lastTime = currentTime;
		}
		frameCount++;
	}
	
	public int getWidth()
	{
		if (clientWidth == 0) clientWidth = width - controlPanel.panelSize.width;
		return clientWidth;
	}
}
