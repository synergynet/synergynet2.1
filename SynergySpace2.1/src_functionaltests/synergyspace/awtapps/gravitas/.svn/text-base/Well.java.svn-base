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

import java.awt.*;
import java.util.*;

public abstract class Well implements Comparable<Well> {
	protected static final double FixedWellDensity = 0.01;
	protected static final double FadingWellDensity = 0.08;
	protected static final double DrifterDensity = 2;
	protected static final double Repulsion = -0.9;
	protected static final int CloseRange = 6;
	protected static final int TooClose = 4;
	
	//Physics
	protected double Density, Mass, Area;
	//Drawing
	protected Point.Double Location;
	protected double Radius, HalfRad, Diameter;
	protected Color Col, ParticleCol;
	//Misc
	public int ID;
	
	public Well(Point.Double location, double mass, double density, Color col)
	{
		this.Col = col;
		this.Density = density;
		this.Location = location;
		this.Mass = mass;
		this.Area = GetArea();
		this.Radius = GetRadius();
		this.HalfRad = Radius / 2;
		this.Diameter = Radius * 2;
		this.ID = GravitySystem.GetNewID();
		this.ParticleCol = Color.white;
	}
	
	public static int GetMassFromPressure(double pressure)
	{
		if (pressure < Gravitas.minPressure) pressure = Gravitas.minPressure;
		if (pressure > Gravitas.maxPressure) pressure = Gravitas.maxPressure;
		double DeltaM = Gravitas.maxMass - Gravitas.minMass;
		double DeltaP = Gravitas.maxPressure - Gravitas.minPressure;
		return (int)(Gravitas.maxMass - (DeltaM * ((Gravitas.maxPressure - pressure) / DeltaP)));
	}
	
	public abstract void Render(Graphics2D g2d, int pass);
	public abstract void Update(int time, ArrayList<Well> world);
	
	protected double GetArea()
	{
		return Mass / Density; 
	}
	
	protected double GetRadius()
	{
		return Math.sqrt(Area / Math.PI);
	}
	
	public boolean Touching(Point P)
	{
		return Math.sqrt(Math.pow(P.x - Location.x, 2) + Math.pow(P.y - Location.y, 2)) < Math.min(50, Radius);
	}
	
	public int compareTo(Well w)
	{
		if (w == null) throw new NullPointerException();
		int IDiff = ID - w.ID;
		if (this instanceof Drifter)
		{
			if (w instanceof Drifter) return IDiff;
			return 1;
		}
		return IDiff;
	}
	
	public boolean equals(Object other)
	{
		if (other == null) return false;
		if (!(other instanceof Well)) return false;
		return equals(equals((Well)other));
	}
	
	public boolean equals(Well w)
	{
		return ID == w.ID;
	}
}
