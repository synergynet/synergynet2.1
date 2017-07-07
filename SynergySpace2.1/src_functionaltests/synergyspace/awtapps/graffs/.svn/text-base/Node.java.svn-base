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

package synergyspace.awtapps.graffs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Node implements Comparable<Node> {
	public int x, y;
	private static int radius = 20;
	private static int diameter = 40;
	public static int spacing = 50;
	private Color color = Color.gray;
	public Node parent = null;
	public int G = 0;
	public int H = 0;
	public int id;
	public ArrayList<Edge> edges;
	public boolean connected = false;
	
	public Node(int x, int y, int id)
	{
		this(x, y, id, Network.offCol);
	}
		
	public Node(int x, int y, int id, Color color)
	{
		this.x = x;
		this.y = y;
		this.id = id;
		this.color = color;
		edges = new ArrayList<Edge>();
	}
	
	public void render(Graphics2D g2d)
	{
		g2d.setColor(color);
		g2d.fill(new Ellipse2D.Double(x - radius, y - radius, diameter, diameter));
	}
	
	public int getF()
	{
		return G + H;
	}
	
	public boolean equals(Node other)
	{
		if (other == null) return false;
		if (this.id == other.id)
		if (x == other.x && y == other.y) return true;
		return Network.distance(x, y, other.x, other.y) < spacing;
	}
	
	public boolean equals(Object other)
	{
		if (other == null) return false;
		if (!(other instanceof Node)) return false;
		return equals((Node)other);
	}
	
	public int compareTo(Node other)
	{
		if (equals(other)) return 0;
		return other.getF() - getF();
	}
	
	public void addEdge(Node to, int length)
	{
		Edge e = new Edge(this, to, length);
		if (!edges.contains(e)) edges.add(e);
		to.addEdgeNoBounce(this, length);
	}
	
	private void addEdgeNoBounce(Node to, int length)
	{
		Edge e = new Edge(this, to, length);
		if (!edges.contains(e)) edges.add(e);
	}
	
	public String toString()
	{
		return "" + id;
	}
	
	public void reset()
	{
		edges.clear();
		G = 0;
		H = 0;
		connected = false;
	}
	
	public void setCol(Color col)
	{
		this.color = col;
	}
}
