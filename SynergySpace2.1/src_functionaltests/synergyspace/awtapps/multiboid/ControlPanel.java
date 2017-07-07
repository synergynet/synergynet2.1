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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class ControlPanel {
	public Dimension screenSize, panelSize;
	private static Color disabled = new Color(192, 192, 192);
	private static Color panel = new Color(96, 96, 96);
	private static Color selected = new Color(96, 96, 128);
	private int left;
	private HashMap<String, Rectangle2D> panels;
	private HashMap<String, Point> labels;
	private BoidSystem parent;
	private long lastClick = 0;
	private long clickInterval = 500;
	private long feedInterval = 20;
	private String currType = "bird";
	
	public ControlPanel(Dimension screenSize, Dimension panelSize, BoidSystem parent)
	{
		this.parent = parent;
		this.screenSize = screenSize;
		this.panelSize = panelSize;
		this.left = screenSize.width - panelSize.width;
		this.panels = new HashMap<String, Rectangle2D>();
		this.labels = new HashMap<String, Point>();
		panels.put("box", new Rectangle2D.Double(left, 0, panelSize.width - 1, panelSize.height - 1));
		int y = 50;
		int bh = 24;
		labels.put("Boids", new Point(10, y));
		panels.put("boidadd", new Rectangle2D.Double(left + 5, y, 30, 30));
		panels.put("boidminus", new Rectangle2D.Double(left + 40, y, 30, 30));
		y+= 75;
		labels.put("Options", new Point(10, y));
		panels.put("walls", new Rectangle2D.Double(left + 5, y, 65, bh));
		y += 50;
		panels.put("pies", new Rectangle2D.Double(left + 5, y, 65, bh));
		y += 50;
		panels.put("colours", new Rectangle2D.Double(left + 5, y, 65, bh));
		y += 80;
		labels.put("Behaviour", new Point(10, y));
		panels.put("bird", new Rectangle2D.Double(left + 10, y, 60, bh));
		y+= 50;
		panels.put("insect", new Rectangle2D.Double(left + 10, y, 60, bh));
		y+= 50;
		panels.put("fish", new Rectangle2D.Double(left + 10, y, 60, bh));
		y+= 50;
		panels.put("drone", new Rectangle2D.Double(left + 10, y, 60, bh));
	}
	
	public void render(Graphics2D g2d)
	{
		g2d.setColor(Color.gray);
		g2d.fill(panels.get("box"));
		g2d.setColor(Color.white);
		g2d.draw(panels.get("box"));
		drawBoidPanel(g2d);
		drawOptions(g2d);
		drawTypePanel(g2d);
		g2d.setColor(Color.white);
		for(String s : labels.keySet())
		{
			Point p = labels.get(s);
			g2d.drawString(s, p.x + left, p.y - 12);
		}
	}
	
	public boolean contains(Point p)
	{
		return panels.get("box").contains(p) || p.x >= panels.get("box").getMaxX();
	}
	
	public void click(Point p)
	{
		long current = System.currentTimeMillis();
		if (current - lastClick < feedInterval) return;
		boolean clicked = false;
		if (panels.get("boidadd").contains(p)) 
		{
			clicked = true;
			parent.addBoid();
		}
		if (panels.get("boidminus").contains(p))
		{
			clicked = true;
			parent.removeBoid();
		}
		if (clicked)
		{
			lastClick = current;
			return;
		}
		if (current - lastClick < clickInterval) return;
		if (panels.get("walls").contains(p))
		{
			clicked = true;
			Boid.setWalls(!MultiBoid.useWalls);
		}		
		if (panels.get("pies").contains(p))
		{
			clicked = true;
			MultiBoid.drawVisionLines = !MultiBoid.drawVisionLines;
		}		
		if (panels.get("colours").contains(p))
		{
			clicked = true;
			MultiBoid.dynamicColours = !MultiBoid.dynamicColours;
		}		
		if (panels.get("bird").contains(p))
		{
			clicked = true;
			Boid.setBirds();
			currType = "bird";
		}
		if (panels.get("insect").contains(p))
		{
			clicked = true;
			Boid.setInsects();
			currType = "insect";
		}
		if (panels.get("fish").contains(p))
		{
			clicked = true;
			Boid.setFish();
			currType = "fish";
		}
		if (panels.get("drone").contains(p))
		{
			clicked = true;
			Boid.setApart();
			currType = "drone";
		}
		if (clicked)
			lastClick = current;
	}
	
	private void drawOptions(Graphics2D g2d)
	{
		drawBox(g2d, panels.get("walls"), "Walls", MultiBoid.useWalls);
		drawBox(g2d, panels.get("pies"), "Sight", MultiBoid.drawVisionLines);
		drawBox(g2d, panels.get("colours"), "Colours", MultiBoid.dynamicColours);
	}
	
	private void drawBoidPanel(Graphics2D g2d)
	{
		g2d.setColor(Color.white);
		g2d.draw(panels.get("boidadd"));
		g2d.draw(panels.get("boidminus"));
		g2d.setColor(Color.white);
		if (MultiBoid.boidCount >= MultiBoid.maxBoids)
			g2d.setColor(disabled);
		g2d.drawString("+", (int)panels.get("boidadd").getX() + 10, (int)panels.get("boidadd").getY() + 20);
		g2d.setColor(Color.white);
		if (MultiBoid.boidCount <= MultiBoid.minBoids)
			g2d.setColor(disabled);
		g2d.drawString("-", (int)panels.get("boidminus").getX() + 11, (int)panels.get("boidminus").getY() + 20);
	}
	
	private void drawTypePanel(Graphics2D g2d)
	{
		g2d.setColor(Color.white);
		drawBox(g2d, panels.get("bird"), "Bird", currType == "bird");
		drawBox(g2d, panels.get("insect"), "Insect", currType == "insect");
		drawBox(g2d, panels.get("fish"), "Fish", currType == "fish");
		drawBox(g2d, panels.get("drone"), "Drone", currType == "drone");
	}
	
	private void drawBox(Graphics2D g2d, Rectangle2D box, String label, boolean on)
	{
		if (on)
			g2d.setColor(selected);
		else 
			g2d.setColor(panel);
		g2d.fill(box);
		g2d.setColor(Color.white);
		g2d.draw(box);
		if (on)
			g2d.setColor(Color.white);
		else 
			g2d.setColor(disabled);
		g2d.drawString(label, (int)box.getX() + 5, (int)box.getCenterY() + 6);
	}
}
