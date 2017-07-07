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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import synergyspace.awt.AWTAppRenderer;
import synergyspace.mtinput.Blob;

public class BoidSystem extends AWTAppRenderer {
	public static Random Random = new Random();
	
	private static int CurrID = 0;
	public static Dimension Size;
	
	public static final long SmackInterval = 500;
	private long lastSmack = 0;
	private long lastSmackID = -1;
	
	private int frameCount = 0;
	private long lastTime = 0;
	private long prevTime = 0;
	private int fps = 0;
	private ArrayList<Boid> Flock;
	private ArrayList<Block> Blocks;
	private ControlPanel controlPanel;
	
	private double pMax = 0;
	private double pMin = 0;
	private double pCount = 0;
	private double pTotal = 0;
	private int clientwidth = 0;
		
	public BoidSystem(int NumBoids)
	{
		Size = Toolkit.getDefaultToolkit().getScreenSize();
		controlPanel = new ControlPanel(Size, new Dimension(80, Size.height), this);
		width = Size.width;
		height = Size.height;
		Boid.initCols();
		Blocks = new ArrayList<Block>(20);
		Flock = new ArrayList<Boid>(NumBoids);
		for(int a = 0; a < NumBoids; a++)
			Flock.add(new Boid(this));
//		Flock.add(new Boid(new java.awt.geom.Point2D.Double(600, 500)));
//		Flock.add(new Boid(new Point2D.Double(BoidSystem.GetWidth() - 50, BoidSystem.GetHeight() - 50)));
	}
	
	public void addBoid()
	{
		if (MultiBoid.boidCount < MultiBoid.maxBoids)
		{
			MultiBoid.boidCount ++;
			Flock.add(new Boid(this));
		}
	}
	
	public void removeBoid()
	{
		if (MultiBoid.boidCount > MultiBoid.minBoids)
		{
			MultiBoid.boidCount --;
			Flock.remove(0);
		}
	}
	
	public boolean CanSmack()
	{
		return System.currentTimeMillis() - lastSmack > SmackInterval;
	}
	
	public int GetWidth()
	{
		if (clientwidth == 0) clientwidth = Size.width - controlPanel.panelSize.width;
		return clientwidth;
	}
	
	public static int GetHeight()
	{
		return Size.height;
	}
		
	public static int GetNewID()
	{
		return CurrID++;
	}
	
	private void countPressure(double p)
	{
		if (pCount == 0 || p < pMin) pMin = p;
		if (p > pMax) pMax = p;
		pTotal += p;
		pCount ++;
		
	}
		
	public void render(Graphics2D g2d) {
		int Delta = timeDelta();
		calculateFPS();
		g2d.setColor(Color.darkGray);
		g2d.fillRect(0, 0, width, height);
		Iterator<Block> it = Blocks.iterator();
		while(it.hasNext())
		{
			Block b = it.next();
			if (b.IsFinished())
				it.remove();
			else
				b.Update(Delta);
		}
		
		synchronized(blobs) {
			for(Blob b : blobs.values()) {
				int y = height - b.y;
				if (b.y == 0 && b.x == 0) continue;
				//Check if control panel receives this blob
				if (controlPanel.contains(new Point(b.x, y)))
				{
					controlPanel.click(new Point(b.x, y));
					continue;
				}
				//Pressure in the range 0-1
				double pressure = b.pressure;
				countPressure(pressure);
				boolean found = false;
				if (pressure >= MultiBoid.pressureHit)
				{
					if (!CanSmack()) continue;
					lastSmackID = (int)b.id;
					lastSmack = System.currentTimeMillis();
					Point2D.Double p = new Point2D.Double(b.x, y);
					for(Boid boid : Flock)
					{
						
						double dist = boid.Distance(p);
						if (dist < Boid.fearDist)
							boid.setFear(pressure, dist, p);
					}
				}
				else
				{
					if (lastSmackID == (int)b.id) continue;
					for(Block bl : Blocks)
						if (bl.Touching(new Point2D.Double(b.x, y)))
						{
							bl.Restart();
							found = true;
						}
					if (!found)
					{
						Blocks.add(new Block(new Point2D.Double(b.x, y), pressure));
					}
				}
			}
		}
		g2d.setColor(Color.blue);
		for(Block b : Blocks)
			b.Render(g2d);
		for (Boid b : Flock)
			b.SetForces(Flock, Blocks);
		for (Boid b : Flock)
		{
			b.Move(Delta);
			b.RenderA(g2d);
		}
		for (Boid b : Flock)
			b.RenderB(g2d);
		if (MultiBoid.drawWalls && MultiBoid.useWalls)
		{
			g2d.setColor(Color.gray);
			g2d.draw(new Rectangle2D.Double(0, 0, width - 1, height - 1));
		}
		
		controlPanel.render(g2d);
		
		g2d.setColor(Color.lightGray);
		g2d.drawString("FPS: " + fps, 4, 13);
		g2d.drawString("Boids: " + MultiBoid.boidCount, 4, 27);
		if (pCount > 0)
			g2d.drawString("Pressure  Min:" + pMin + ", max: " + pMax + ", ave: " + (pTotal / pCount), 4, 40);
	}
	
	private void PokeBoids()
	{
		for(Boid b : Flock)
			b.SetHandedness();
	}
	
	private int timeDelta()
	{
		long currentTime = System.currentTimeMillis();
		if (CanSmack()) lastSmackID = -1;
		if (prevTime == 0)
			prevTime = currentTime - 1;
		int result = (int)(currentTime - prevTime);
		prevTime = currentTime;
		return result;
	}

	private void calculateFPS() {
		long currentTime = System.currentTimeMillis();
		if(currentTime - lastTime > 1000) {
			fps = frameCount;
			frameCount = 0;
			lastTime = currentTime;
			PokeBoids();
		}
		frameCount++;
	}
}
