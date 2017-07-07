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
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;

import synergyspace.awt.AWTAppRenderer;
import synergyspace.mtinput.Blob;

public class Network extends AWTAppRenderer {
	public static Color lineCol = Color.gray;
	public static Color pathCol = Color.white;
	public static Color startCol = Color.green;
	public static Color endCol = Color.orange;
	public static Color offCol = Color.gray;
	public static Color onCol = Color.blue;
	public static int maxEdge;
	private int frameCount = 0;
	private long lastTime = 0;
	private int fps = 0;
	private ArrayList<Node> nodes, initNodes;
	private Node start, end;
		
	public Network()
	{
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		width = size.width;
		height = size.height;
		maxEdge = height / Graffs.edgeLimit;
		initNodes = new ArrayList<Node>(2);
		start = new Node(size.width / 2, 50, 0, startCol);
		end = new Node(size.width / 2, size.height - 50, 1, endCol);
		initNodes.add(start);
		initNodes.add(end);
	}
	
	public static double distance(int x1, int y1, int x2, int y2)
	{
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
		
	public void render(Graphics2D g2d) {
		try
		{
			g2d.setColor(Color.darkGray);
			g2d.fillRect(0, 0, width, height);
			calculateFPS();
			start.reset();
			end.reset();
			nodes = new ArrayList<Node>(initNodes);
			//nodes.add(new Node(100, 500, 2));
			//nodes.add(new Node(800, 200, 3));
			//nodes.add(new Node(800, 400, 4));
			
			//Make nodes
			synchronized(blobs) {
				for(Blob b : blobs.values()) {
					int y = height - b.y;
					if (b.y == 0 && b.x == 0) continue;
					Node n = new Node(b.x, y, -1, offCol);
					if (!nodes.contains(n))
					{
						int id = nodes.get(nodes.size() - 1).id + 1;
						n.id = id;
						nodes.add(n);
					}
				}
			}
			
			//Make edges
			for(int a = 0; a < nodes.size(); a++)
			{
				Node outer = nodes.get(a);
				for(int b = a + 1; b < nodes.size(); b++)
				{
					if (a == b) continue;
					Node inner = nodes.get(b);
					int dist = (int)distance(outer.x, outer.y, inner.x, inner.y);
					if (dist > maxEdge) continue;
					//System.out.println("Adding edge from " + outer.x + "x" + outer.y + " to " + inner.x + "x" + inner.y + ": " + dist);
					//System.out.println("Adding edge from " + outer.id + " to " + inner.id);
					outer.addEdge(inner, dist);
				}
			}
			
			//Get connections
			getConnections(start);
			for(Node n : nodes)
				if (n.connected && !n.equals(start) && !n.equals(end)) n.setCol(onCol);
			
			//Draw edges
			g2d.setColor(lineCol);
			for(int a = 0; a < nodes.size(); a++)
			{
				Node outer = nodes.get(a);
				for(int b = 0; b < outer.edges.size(); b++)
				{
					Edge e = outer.edges.get(b);
					//if (outer.compareTo(e.to) <= 0) continue;
					g2d.drawLine(e.from.x, e.from.y, e.to.x, e.to.y);
				}
			}
			
			//Draw path
			ArrayList<Node> path = getPath(start, end);
			if (path != null)
			{
				g2d.setColor(pathCol);
				for(int n = 0; n < path.size() - 1; n++)
					g2d.drawLine(path.get(n).x, path.get(n).y, path.get(n + 1).x, path.get(n + 1).y);
			}
			
			
			//Draw nodes
			for(Node n : nodes)
				n.render(g2d);
			
			g2d.setColor(Color.lightGray);
			g2d.drawString("FPS: " + fps, 4, 13);
		}
		catch(Exception e)
		{
			System.out.println(e);
			e.printStackTrace();
		}
		//System.exit(-1);
	}
	
	private void getConnections(Node from)
	{
		ArrayList<Node> stack = new ArrayList<Node>();
		stack.add(from);
		while(stack.size() > 0)
		{
			Node curr = stack.remove(0);
			curr.connected = true;
			for(Edge e : curr.edges)
			{
				//if (!(stack.contains(e.from) || e.from.connected)) stack.add(e.from);
				if (!(stack.contains(e.to) || e.to.connected)) stack.add(e.to);
			}
		}
	}
	
	private ArrayList<Node> getPath(Node from, Node to)
	{
		for(Node n : nodes)
		{
			n.H = (int)distance(n.x, n.y, to.x, to.y);
		}
		ArrayList<Node> closed = new ArrayList<Node>();
		ArrayList<Node> open = new ArrayList<Node>();
		open.add(from);
		while(open.size() > 0)
		{
			Collections.sort(open); //Lowest f() scores at the end of the list
			Node current = open.remove(open.size() - 1);
			if (current.equals(to))
			{
				ArrayList<Node> path = new ArrayList<Node>();
				path.add(current);
				while(current.parent != null)
				{
					current = current.parent;
					path.add(0, current);
				}
				return path;
			}
			closed.add(current);
			for(Edge e : current.edges)
			{
				Node next = e.to;
				if (next.equals(current)) next = e.from;
				if (closed.contains(next)) continue;
				int G = current.G + e.length;
				boolean better = false;
				if (!open.contains(next))
				{
					open.add(next);
					better = true;
				}
				else if (G < next.G && next.G > 0)
				{
					better = true;
				}
				if (better)
				{
					next.G = G;
					next.parent = current;
				}
			}
		}
		return null;
	}
	
	private void calculateFPS() {
		long currentTime = System.currentTimeMillis();
		if(currentTime - lastTime > 1000) {
			fps = frameCount;
			frameCount = 0;
			lastTime = currentTime;
		}
		frameCount++;
	}
}
