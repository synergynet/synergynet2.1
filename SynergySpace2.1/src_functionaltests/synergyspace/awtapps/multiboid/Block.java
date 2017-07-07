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

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Block {
	public int LifeSpan = 3000; //ms
	public Point.Double Location;
	public int Radius;
	public int Age; //ms
	public Ellipse2D.Double Shape;
	
	public Block(Point.Double location, double pressure)
	{
		//this.Radius = GetRadiusFromPressure(pressure);
		this.Radius = 10;
		this.Location = location;
		this.Age = 0;
		this.LifeSpan = (int)(30000 * Math.cbrt(pressure));
		this.Shape = new Ellipse2D.Double(Location.x - Radius, Location.y - Radius, Radius * 2, Radius * 2);
	}
	
	public static int GetRadiusFromPressure(double pressure)
	{
		if (pressure < MultiBoid.MinPressure) pressure = MultiBoid.MinPressure;
		if (pressure > MultiBoid.MaxPressure) pressure = MultiBoid.MaxPressure;
		return (int)(BoidSystem.Size.width * pressure * MultiBoid.blockScale);
	}
	
	public void Update(long time) //ms
	{
		Age += time;
	}
	
	public void Restart()
	{
		Age = 0;
	}
	
	public boolean IsFinished()
	{
		return Age >= LifeSpan;
	}
	
	public boolean Touching(Point.Double p)
	{
		return this.Shape.contains(p);
	}
	
	public void Render(Graphics2D g2d)
	{
		if(LifeSpan > 0)
			g2d.setColor(new Color(64, 64, 255 - (Age * 192 / LifeSpan)));
		else
			g2d.setColor(new Color(64, 64, 255 - (Age * 192 / (LifeSpan+1))));
		g2d.fill(Shape);
	}
}
