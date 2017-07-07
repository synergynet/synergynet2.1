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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class ControlPanel {
	public Dimension screenSize, panelSize;
	private static Color disabled = new Color(192, 192, 192);
	private int left;
	private HashMap<String, Rectangle2D> panels;
	private HashMap<String, Point> labels;
	private GravitySystem parent;
	private long lastClick = 0;
	private long clickInterval = 200;
	
	public ControlPanel(Dimension screenSize, Dimension panelSize, GravitySystem parent)
	{
		this.parent = parent;
		this.screenSize = screenSize;
		this.panelSize = panelSize;
		this.left = screenSize.width - panelSize.width;
		this.panels = new HashMap<String, Rectangle2D>();
		this.labels = new HashMap<String, Point>();
		int y = 50;
		panels.put("box", new Rectangle2D.Double(left, 0, panelSize.width - 1, panelSize.height - 1));
		int sliderW = 40;
		int sliderH = 200;
		if (panelSize.height < 800)
			sliderH /= 2;
		labels.put("Drifters", new Point(10, y));
		panels.put("drifteradd", new Rectangle2D.Double(left + 5, y, 30, 30));
		panels.put("drifterminus", new Rectangle2D.Double(left + 40, y, 30, 30));
		y+= 85;
		labels.put("Particles", new Point(10, y));
		y+= 15;
		panels.put("particlemax", new Rectangle2D.Double(left + 20, y, sliderW, sliderW));
		y += sliderW;
		panels.put("particlevar", new Rectangle2D.Double(left + 20, y, sliderW, sliderH));
		y += sliderH;
		panels.put("particlemin", new Rectangle2D.Double(left + 20, y, sliderW, sliderW));
		y += 100;
		labels.put("Trails", new Point(20, y));
		panels.put("trails", new Rectangle2D.Double(left + 10, y, 60, 30));
		y += 60;
		panels.put("trailmax", new Rectangle2D.Double(left + 20, y, sliderW, sliderW));
		y += sliderW;
		panels.put("trailvar", new Rectangle2D.Double(left + 20, y, sliderW, sliderH));
		y += sliderH;
		panels.put("trailmin", new Rectangle2D.Double(left + 20, y, sliderW, sliderW));

	}
	
	public void render(Graphics2D g2d)
	{
		g2d.setColor(Color.gray);
		g2d.fill(panels.get("box"));
		g2d.setColor(Color.white);
		g2d.draw(panels.get("box"));
		drawDrifterPanel(g2d);
		drawParticlePanel(g2d);
		drawTrailsPanel(g2d);
		g2d.setColor(Color.white);
		for(String s : labels.keySet())
		{
			Point p = labels.get(s);
			g2d.drawString(s, p.x + left, p.y - 12);
		}
	}
	
	public boolean contains(Point p)
	{
		return panels.get("box").contains(p);
	}
	
	public void click(Point p)
	{
		if (!Gravitas.useTrails)
		{
			if (panels.get("particlemax").contains(p)) GravitySystem.PS.setParticles(Gravitas.maxParticles);
			if (panels.get("particlemin").contains(p)) GravitySystem.PS.setParticles(0);
			if (panels.get("particlevar").contains(p)) GravitySystem.PS.setParticles(getParticleCount(p.y));
		}
		else
		{
			if (panels.get("trailmax").contains(p)) Gravitas.trailLength = Gravitas.maxTrailLength;
			if (panels.get("trailmin").contains(p)) Gravitas.trailLength = Gravitas.minTrailLength;
			if (panels.get("trailvar").contains(p)) Gravitas.trailLength = getTrailLength(p.y);
		}
		long current = System.currentTimeMillis();
		if (current - lastClick < clickInterval) return;
		boolean clicked = false;
		if (panels.get("drifteradd").contains(p)) 
		{
			clicked = true;
			parent.AddDrifter();
		}
		if (panels.get("drifterminus").contains(p))
		{
			clicked = true;
			parent.RemoveDrifter();
		}
		if (panels.get("trails").contains(p))
		{
			clicked = true;
			Gravitas.useTrails = !Gravitas.useTrails;
		}
		if (clicked)
			lastClick = current;
	}
	
	private void drawTrailsPanel(Graphics2D g2d)
	{
		g2d.setColor(Color.white);
		g2d.draw(panels.get("trails"));
		if (!Gravitas.useTrails) g2d.setColor(disabled);
		g2d.drawString(Gravitas.useTrails ? "On" : "Off", (int)panels.get("trails").getX() + 15, (int)panels.get("trails").getY() + 20);
		
		g2d.setColor(Color.white);
		g2d.drawString("# " + Gravitas.trailLength, left + 15, (int)panels.get("trailmax").getY() - 10);
		double ratio = 0;
		if (Gravitas.trailLength > 0) ratio = (double)(Gravitas.trailLength - Gravitas.minTrailLength) / (double)(Gravitas.maxTrailLength - Gravitas.minTrailLength);
		g2d.setColor(blend(Color.red, Color.blue, ratio));
		if (!Gravitas.useTrails) g2d.setColor(disabled);
		g2d.fill(panels.get("trailmin"));
		if (Gravitas.trailLength == Gravitas.maxTrailLength)
		{
			g2d.fill(panels.get("trailmax"));
		}
		if (Gravitas.trailLength > 0)
		{
			Rectangle2D middle = panels.get("trailvar");
			int height = (int)(middle.getHeight() * ratio);
			g2d.fill(new Rectangle2D.Double(middle.getX(), middle.getY() + middle.getHeight() - height, middle.getWidth(),height));
		}
		g2d.setColor(Color.white);
		g2d.draw(panels.get("trailmax"));
		g2d.draw(panels.get("trailvar"));
		g2d.draw(panels.get("trailmin"));
	}
	
	private void drawDrifterPanel(Graphics2D g2d)
	{
		g2d.setColor(Color.white);
		g2d.draw(panels.get("drifteradd"));
		g2d.draw(panels.get("drifterminus"));
		if (parent.drifterCount < Gravitas.maxDrifters)
		g2d.drawString("+", (int)panels.get("drifteradd").getX() + 10, (int)panels.get("drifteradd").getY() + 20);
		if (parent.drifterCount > 1)
			g2d.drawString("-", (int)panels.get("drifterminus").getX() + 11, (int)panels.get("drifterminus").getY() + 20);
	}
	
	private int getParticleCount(int y)
	{
		Rectangle2D b = panels.get("particlevar");
		y -= b.getY();
		return (int)(Gravitas.maxParticles * (1 - y / b.getHeight()));
		//return Gravitas.MaxParticles * 
	}
	
	private int getTrailLength(int y)
	{
		Rectangle2D b = panels.get("trailvar");
		y -= b.getY();
		int h = (int)b.getHeight();
		y = h - y;
		return Gravitas.minTrailLength + ((Gravitas.maxTrailLength - Gravitas.minTrailLength) * y) / h;
	}
	
	private Color blend(Color from, Color to, double ratio)
	{
		return new Color((int)(from.getRed() + ((to.getRed() - from.getRed()) * ratio)), (int)(from.getGreen() + ((to.getGreen() - from.getGreen()) * ratio)),	(int)(from.getBlue() + ((to.getBlue() - from.getBlue()) * ratio)));
	}
	
	private void drawParticlePanel(Graphics2D g2d)
	{
		g2d.setColor(Color.white);
		g2d.drawString("# " + Gravitas.particles, left + 15, (int)panels.get("particlemax").getY() - 10);
		double ratio = 0;
		if (Gravitas.particles > 0) ratio = (double)Gravitas.particles / (double)Gravitas.maxParticles;
		g2d.setColor(blend(Color.red, Color.blue, ratio));
		if (Gravitas.useTrails) g2d.setColor(disabled);
		g2d.fill(panels.get("particlemin"));
		if (Gravitas.particles == Gravitas.maxParticles)
		{
			g2d.fill(panels.get("particlemax"));
		}
		if (Gravitas.particles > 0)
		{
			Rectangle2D middle = panels.get("particlevar");
			int height = (int)(middle.getHeight() * ratio);
			g2d.fill(new Rectangle2D.Double(middle.getX(), middle.getY() + middle.getHeight() - height, middle.getWidth(),height));
		}
		g2d.setColor(Color.white);
		g2d.draw(panels.get("particlemax"));
		g2d.draw(panels.get("particlevar"));
		g2d.draw(panels.get("particlemin"));
	}
}
