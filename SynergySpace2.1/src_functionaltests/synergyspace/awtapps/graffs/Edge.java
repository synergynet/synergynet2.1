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

public class Edge implements Comparable<Edge> {
	public Node from;
	public Node to;
	public int length;
	
	public Edge(Node from, Node to, int length)
	{
		if (from == null || to == null || from.equals(to)) throw new IllegalArgumentException("Problem creating edge from these nodes");
		if (length <= 0) throw new IllegalArgumentException("Invalid edge length");
		this.from = from;
		this.to = to;
		this.length = length;
	}
	
	public boolean equals(Object other)
	{
		if (other == null) return false;
		if (other instanceof Edge) return equals((Edge)other);
		return false;
	}
	
	public boolean equals(Edge other)
	{
		if (other == null) return false;
		if (length != other.length) return false;
		return (this.from.equals(other.from) && this.to.equals(other.to)) || (this.from.equals(other.to) && this.to.equals(other.from));
	}
	
	public int compareTo(Edge other)
	{
		return length - other.length;
	}
	
	public String toString()
	{
		return from + " -> " + to + ": " + length;
	}
}
