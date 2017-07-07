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

public class PressureWell extends FixedWell {
	//private static final int PressureScale = 2;
	public double LastPressure = 0;
	
	public PressureWell(Point.Double location, double pressure)
	{
		super(location, GetMassFromPressure(pressure), FadingWellDensity, false);
		//super(location, PressureScale * (int)Math.min(MaxPressure, Math.max(MinPressure, pressure)), FadingWellDensity, false);
		this.EffectCol = new Color(192, 64, 64);
	}
	
	public void Poke(double pressure)
	{
		LastPressure = pressure;
	}
			
	public void Update(int time) //time is in ms
	{
		if (EffectRad == 0) EffectRad = Radius;
		if (LastPressure != 0)
		{
			Mass = GetMassFromPressure(LastPressure);
		}
		else
		{
			Mass -= GetMassDelta(time);
		}
		LastPressure = 0;
		Area = GetArea();
		Radius = GetRadius();
		Diameter = Radius * 2;
		HalfRad = Radius / 2;
	}
		
	private static int GetMassDelta(int time) //time is in ms
	{
		return (int)((double)Gravitas.massLossRate * (double)time);
	}
	
	public boolean IsFinished()
	{
		return Mass < Gravitas.minMass / 2;
	}
}