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
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;

public class Boid {
	//Misc
	public static final double Rad0 = Math.toRadians(0);
	public static final double Rad90 = Math.toRadians(90);
	public static final double Rad180 = Math.toRadians(180);
	public static final double Rad270 = Math.toRadians(270);
	public static final double Rad360 = Math.toRadians(360);
	public static final double RadSmall = Math.toRadians(1);
	public static final double RadMed = Math.toRadians(20);
	private static final int Border = 20;
	private static Point.Double ScreenCentre = null;
	
	private BoidSystem parent = null;
		
	//Behaviour
	private static final double VisionAngle = 50.0;
	private static final double SenseRange = 50.0;
	private static final double FullSenseRange = 70.0;
	private static final double TooClose = 10.0;
	private static double WWalls = MultiBoid.useWalls ? 1 : 0;
	private static double WVelocity = 7;
	private static double WCentre = 2;
	private static final double WRepulse = 10;
	private static double WScreen = WWalls;
	private static final int LeftTurn = -1;
	private static final int RightTurn = 1;
	public static final int fearDist = 200;
	private int StraightTurn = 1;
	private int fearTime = 0; //ms
	private Point.Double fearPoint;
	
	//Drawing
	private static final Color Col = Color.white;
	private static Color[] dirCols;
	private static final Color SightCol = new Color(72, 72, 72);
	
	//Fields
	private Point.Double Location, LeftEye, RightEye;
	private double Heading = 0;
	private int ID;
	private boolean RightHanded;
	private int Turn = 0;
	
	public Boid(BoidSystem parent)
	{
		this(new Point2D.Double(BoidSystem.Random.nextInt(parent.GetWidth() - 200) + 100, BoidSystem.Random.nextInt(BoidSystem.GetHeight() - 200) + 100), parent);
	}
	
	public Boid(Point.Double location, BoidSystem parent)
	{
		this.parent = parent;
		if (ScreenCentre == null)
			ScreenCentre = new Point2D.Double(parent.GetWidth() / 2, BoidSystem.GetHeight() / 2);
		ID = BoidSystem.GetNewID();
		Location = location;
		Heading = Math.toRadians(Math.random() * 360);
		/*if (ID == 0)
			Heading = Rad90;
		else 
			Heading = Rad270;*/
		//Heading = Rad0;
		SetHeading();
		SetHandedness();
	}
	
	public static void initCols()
	{
		if (dirCols != null) return;
		dirCols = new Color[360];
		for(double a = 0; a < 90; a++)
		{
			int shade = (int)(a * 192.0 / 90.0);
			dirCols[(int)a] = new Color(64, 64 + shade, 255);
			dirCols[(int)a + 90] = new Color(64, 255, 255 - shade);
			dirCols[(int)a + 180] = new Color(64, 255, 64 + shade);
			dirCols[(int)a + 270] = new Color(64, 255 - shade, 255);
		}
	}
	
	public static void setWalls(boolean walls)
	{
		MultiBoid.useWalls = walls;
		WWalls = MultiBoid.useWalls ? 1 : 0;
		WScreen = WWalls;
	}
	
	public static void setBirds()
	{
		WCentre = 2;
		WVelocity = 7;
	}
	
	public static void setInsects()
	{
		WCentre = 2;
		WVelocity = 0;
	}
	
	public static void setFish()
	{
		WCentre = 0;
		WVelocity = 7;
	}
	
	public static void setApart()
	{
		WCentre = 0;
		WVelocity = 0;
	}
	
	public void setFear(double pressure, double dist, Point.Double source)
	{
		//pressure ~300 - 400
		//dist < 200
		if (dist < 5) dist = 5;
		fearTime += (int)(MultiBoid.fearScale * BoidSystem.Size.width * pressure / dist);
		fearPoint = source;
	}
	
	public void SetHandedness()
	{
		RightHanded = BoidSystem.Random.nextBoolean();	
		if (!RightHanded) StraightTurn = LeftTurn;
	}
	
	public int avoidWall()
	{
		if (WWalls == 0) return 0;
		//Need to deal with corners?
		//Top left corner
		if (Distance(new Point2D.Double(0, 0))	< FullSenseRange)
		{
			//System.out.println("Near the top left corner");
			if (Heading <= (Rad270 + RadMed) && Heading >= (Rad180 - RadMed))
				return TurnTowardsCentre();
		}
		//Top right corner
		if (Distance(new Point2D.Double(parent.GetWidth(), 0)) < FullSenseRange)
		{
//			System.out.println("Near the top right corner");
			if (Heading >= (Rad270 - RadMed) && Heading <= (Rad360 + RadMed))
				return TurnTowardsCentre();
		}
		//Bottom left corner
		if (Distance(new Point2D.Double(0, BoidSystem.GetHeight())) < FullSenseRange)
		{
			//System.out.println("Near the bottom left corner, heading " + Math.toDegrees(Heading));
			if (Heading <= (Rad180 + RadMed) && Heading >= (Rad90 - RadMed))
				return TurnTowardsCentre();
		}
		//Bottom right corner
		if (Distance(new Point2D.Double(parent.GetWidth(), BoidSystem.GetHeight())) < FullSenseRange)
		{
			//System.out.println("Near the bottom right corner");
			if (Heading <= (Rad90 + RadMed) && Heading >= (Rad0 - RadMed))
				return TurnTowardsCentre();
		}
		//Left Wall
		if (LeftEye.x < 0 || RightEye.x < 0)
		{
			//System.out.println("Near left wall");
			if (Heading < Rad180) //angle left
				return LeftTurn;
			else if (Heading > Rad180)
				return RightTurn;
			return StraightTurn;
		}
		//Right Wall
		if (LeftEye.x > parent.GetWidth() || RightEye.x > parent.GetWidth())
		{
			//System.out.println("Near right wall");
			if (Heading > Rad270) //angle left
				return LeftTurn;
			else if (Heading > Rad0)
				return RightTurn;
			return StraightTurn;
		}
		//Top Wall: 180-360
		if (LeftEye.y < 0 || RightEye.y < 0)
		{
			//System.out.println("Near top wall");
			if (Heading < Rad270) //angle left
				return LeftTurn;
			else if (Heading > Rad270)
				return RightTurn;
			return StraightTurn;
		}
		//Bottom Wall: 0-180
		if (LeftEye.y > BoidSystem.GetHeight() || RightEye.y > BoidSystem.GetHeight())
		{
			//System.out.println("Near bottom wall");
			if (Heading < Rad90) //angle left
				return LeftTurn;
			else if (Heading > Rad90)
				return RightTurn;
			return StraightTurn;
		}
		return 0;
	}
	
	public int TurnTowardsCentre()
	{
		//Top left
		if (ScreenCentre.x > Location.x && ScreenCentre.y > Location.y)
		{
			//Angle is 180 - 270
			if (Rad270 - Heading < Heading - Rad180) //closer to vertical
				return RightTurn;
			if (Rad270 - Heading > Heading - Rad180)
				return LeftTurn;
			return StraightTurn;
		}
		//Top right
		if (ScreenCentre.x < Location.x && ScreenCentre.y > Location.y)
		{
			//System.out.println("Top right");
			//Angle is 270 - 360
			if (Rad360 - Heading < Heading - Rad270) //closer to horizontal
				return RightTurn;
			if (Rad360 - Heading > Heading - Rad270)
				return LeftTurn;
			return StraightTurn;
		}
		//Bottom right
		if (ScreenCentre.x < Location.x && ScreenCentre.y < Location.y)
		{
			//Angle is 0 - 90
			if (Rad90 - Heading < Heading - Rad0) //closer to vertical
				return RightTurn;
			if (Rad90 - Heading > Heading - Rad0)
				return LeftTurn;
			return StraightTurn;
		}
		//Bottom left
		if (ScreenCentre.x > Location.x && ScreenCentre.y < Location.y)
		{
			//Angle is 90 - 180
			if (Rad180 - Heading < Heading - Rad90) //closer to horizontal
				return RightTurn;
			if (Rad180 - Heading > Heading - Rad90)
				return LeftTurn;
			return StraightTurn;
		}
		return 0;
	}
	
	public int matchVelocity(ArrayList<Boid> flock)
	{
		if (WVelocity == 0) return 0;
		Point.Double vels = new Point2D.Double(0, 0);
		int count = 0;
		for(Boid b : flock)
		{
			if (b.equals(this) || Distance(b.Location) > FullSenseRange) continue;
			count ++;
			Point.Double vel = b.GetVelocity();
			vels.x += vel.x;
			vels.y += vel.y;
		}
		if (count == 0) return 0;
		vels.x /= count;
		vels.y /= count;
		Point.Double thisVel = GetVelocity();
		double heading = Angle(thisVel.x, thisVel.y);
		double angle = Angle(vels.x, vels.y);
		if (heading < 0) heading += Rad360;
		if (angle < 0) angle += Rad360;
		
		if (Math.abs(heading - (angle - Rad360)) < Math.abs(heading - angle)) 
			angle -= Rad360;
		else if (Math.abs(heading - (angle + Rad360)) < Math.abs(heading - angle))
			angle += Rad360;
		if (Math.abs(heading - angle) < RadSmall) return 0;
		if (angle < heading) return LeftTurn;
		else if (angle > heading) return RightTurn;
		return 0;
	}
	
	private int avoidBlocks(ArrayList<Block> blocks)
	{
		for(Block b : blocks)
		{
			if (!CanSee(b)) continue;
			double dl = Distance(LeftEye, b.Location);
			double dr = Distance(RightEye, b.Location);
			if (dl < dr) return RightTurn;
			if (dl > dr) return LeftTurn;
			return StraightTurn;
		}
		return 0;
	}
	
	public int findCentre(ArrayList<Boid> flock)
	{
		if (WCentre == 0) return 0;
		HashMap<Point.Double, Double> points = new HashMap<Point.Double, Double>(flock.size());
		//points.put(Location, 1.0);
		for(Boid b : flock)
		{
			if (b.equals(this)) continue;
			if (Distance(b.Location) > FullSenseRange) continue;
			double dist = Distance(b.Location);
			if (dist < 2) dist = 2;
			points.put(b.Location, dist);
		}
		if (points.size() == 0) return 0;
		Point.Double centre = new Point2D.Double();
		double dist = 0;
		for(Point.Double p : points.keySet())
		{
			centre.x += p.x * points.get(p);
			centre.y += p.y * points.get(p);
			dist += points.get(p);
		}
		centre.x /= dist;
		centre.y /= dist;
		Point.Double thisVel = GetVelocity();
		double heading = Angle(thisVel.x, thisVel.y);
		double angle = Angle(centre.x - Location.x, centre.y - Location.y);
		
		if (heading < 0) heading += Rad360;
		if (angle < 0) angle += Rad360;
		
		if (Math.abs(heading - (angle - Rad360)) < Math.abs(heading - angle)) 
			angle -= Rad360;
		else if (Math.abs(heading - (angle + Rad360)) < Math.abs(heading - angle))
			angle += Rad360;
		if (Math.abs(heading - angle) < RadSmall) return 0;
		if (angle < heading) return LeftTurn;
		else if (angle > heading) return RightTurn;
		return 0;
	}
	
	public int repulse(ArrayList<Boid> flock)
	{
		for(Boid b : flock)
		{
			if (b.equals(this)) continue;
			if (Distance(b.Location) > TooClose) continue;
			int rand = BoidSystem.Random.nextInt(8);
			if (rand == 0) return LeftTurn;
			if (rand == 1) return RightTurn;
			if (rand == 2) return 0;
			return StraightTurn;
		}
		return 0;
	}
	
	public int screenCentre()
	{
		if (WCentre == 0) return 0;
		Point.Double thisVel = GetVelocity();
		double heading = Angle(thisVel.x, thisVel.y);
		double angle = Angle(ScreenCentre.x - Location.x, ScreenCentre.y - Location.y);
		
		if (heading < 0) heading += Rad360;
		if (angle < 0) angle += Rad360;
		
		if (Math.abs(heading - (angle - Rad360)) < Math.abs(heading - angle)) 
			angle -= Rad360;
		else if (Math.abs(heading - (angle + Rad360)) < Math.abs(heading - angle))
			angle += Rad360;
		if (Math.abs(heading - angle) < RadSmall) return 0;
		if (angle < heading) return LeftTurn;
		else if (angle > heading) return RightTurn;
		return 0;
	}
	
	private int flee()
	{
		if (fearTime == 0 || fearPoint == null) return 0;
		Point.Double thisVel = GetVelocity();
		double heading = Angle(thisVel.x, thisVel.y);
		double angle = Angle(fearPoint.x - Location.x, fearPoint.y - Location.y);
		
		if (heading < 0) heading += Rad360;
		if (angle < 0) angle += Rad360;
		
		if (Math.abs(heading - (angle - Rad360)) < Math.abs(heading - angle)) 
			angle -= Rad360;
		else if (Math.abs(heading - (angle + Rad360)) < Math.abs(heading - angle))
			angle += Rad360;
		if (Math.abs(heading - angle) < RadSmall) return 0;
		if (angle < heading) return RightTurn;
		else if (angle > heading) return LeftTurn;
		return 0;
	}
	
	public void SetForces(ArrayList<Boid> flock, ArrayList<Block> blocks)
	{
		Turn = 0;
		Turn = avoidBlocks(blocks);
		if (Turn != 0) return;
		Turn = avoidWall();
		if (Turn != 0) return;
		Turn = flee();
		if (Turn != 0) return;
		double turn = (matchVelocity(flock) * WVelocity) + (findCentre(flock) * WCentre) + (repulse(flock) * WRepulse) + (screenCentre() * WScreen);
		if (turn < 0) Turn = -1;
		else if (turn > 0) Turn = 1;
	}
	
	public Point.Double GetVelocity()
	{
		return new Point2D.Double(MultiBoid.velocity * Math.cos(Heading), MultiBoid.velocity * Math.sin(Heading));
	}
	
	private void SetHeading()
	{
		if (Heading > Rad360) Heading -= Rad360;
		if (Heading < Rad0) Heading += Rad360;
		//A = left side of vision
		LeftEye = new Point2D.Double((int)Location.x + (int)(Math.cos(Heading - Math.toRadians(VisionAngle / 2)) * SenseRange), (int)Location.y + (int)(Math.sin(Heading - Math.toRadians(VisionAngle / 2)) * SenseRange));
		//B = right side of vision
		RightEye = new Point2D.Double((int)Location.x + (int)(Math.cos(Heading + Math.toRadians(VisionAngle / 2)) * SenseRange), (int)Location.y + (int)(Math.sin(Heading + Math.toRadians(VisionAngle / 2)) * SenseRange));
	}
	
	public void Move(int time) //time in ms
	{
		if (fearTime > 0)
		{
			fearTime -= time;
			if (fearTime < 0)
				fearTime = 0;
		}
		double seconds = (double)time / 1000.0;
		Heading += Turn * MultiBoid.bankRate * seconds;
		SetHeading();
		double dist = MultiBoid.velocity * seconds;
		if (fearTime > 0)
			dist *= MultiBoid.fearFactor;
		Location.x += dist * Math.cos(Heading);
		Location.y += dist * Math.sin(Heading);
		
		if (Location.x < 0) 
		{
			Location.x = parent.GetWidth() - Border;
			if (fearTime > 0) fearPoint.x = parent.GetWidth();
		}
		if (Location.x > parent.GetWidth())
		{
			Location.x = Border;
			if (fearTime > 0) fearPoint.x = 0;
		}
		if (Location.y < 0)
		{
			Location.y = BoidSystem.GetHeight() - Border;
			if (fearTime > 0) fearPoint.y = BoidSystem.GetHeight();
		}
		if (Location.y > BoidSystem.GetHeight())
		{
			Location.y = Border;
			if (fearTime > 0) fearPoint.y = 0;
		}
	}
	
	private double Distance(Point.Double a, Point.Double b)
	{
		return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
	}
	
	public double Distance(Point.Double loc)
	{
		return Math.sqrt(DistanceSqr(loc));
	}
	
	private double DistanceSqr(Point.Double loc)
	{
		return Math.pow(loc.x - Location.x, 2) + Math.pow(loc.y - Location.y, 2);
	}
	
	private boolean CanSee(Point.Double loc)
	{
		if (Distance(loc) > SenseRange) return false;
		double target = Angle(loc.x - Location.x, loc.y - Location.y);
		double Diff = Math.abs(Math.toDegrees(target) - Math.toDegrees(Heading));
		return Diff < VisionAngle;
	}
	
	private boolean CanSee(Block b)
	{
		return CanSee(b.Location) || b.Shape.contains(LeftEye) || b.Shape.contains(RightEye) || b.Shape.contains(Location);
	}
	
	//Thanks to tomvollerthun
	//Code from http://forums.sun.com/thread.jspa?threadID=498241&messageID=2352912
	public double Angle(double adj, double opp) {
		double angle;
	 
		// Get the basic angle.
		if (Math.abs(adj) < 0.0001)
			angle = Math.PI / 2;
		else
			angle = Math.abs(Math.atan(opp / adj));
	 
		// See if we are in quadrant 2 or 3.
		if (adj < 0)
			// angle > PI/2 or angle < -PI/2.
			angle = Math.PI - angle;
	 
		// See if we are in quadrant 3 or 4.
		if (opp < 0)
			angle = -angle;
	 
		return angle;
	}

	public void RenderA(Graphics2D g2d)
	{
		if (!MultiBoid.drawVisionLines) return;
		//Point A = new Point((int)Location.x + (int)(Math.cos(Heading - Math.toRadians(VisionAngle / 2)) * SenseRange), (int)Location.y + (int)(Math.sin(Heading - Math.toRadians(VisionAngle / 2)) * SenseRange));
		//Point B = new Point((int)Location.x + (int)(Math.cos(Heading + Math.toRadians(VisionAngle / 2)) * SenseRange), (int)Location.y + (int)(Math.sin(Heading + Math.toRadians(VisionAngle / 2)) * SenseRange));
		if (OutOfBounds(LeftEye) || OutOfBounds(RightEye)) return;
		g2d.setColor(SightCol);
		g2d.drawLine((int)Location.x, (int)Location.y, (int)(LeftEye.x), (int)(LeftEye.y));
		g2d.drawLine((int)Location.x, (int)Location.y, (int)(RightEye.x), (int)(RightEye.y));
		g2d.drawLine((int)(LeftEye.x), (int)(LeftEye.y), (int)(RightEye.x), (int)(RightEye.y));
	}
	
	private boolean OutOfBounds(Point.Double p)
	{
		return p.x < 0 || p.x >= parent.GetWidth() || p.y < 0 || p.y >= BoidSystem.GetHeight();
	}
	
	public void RenderB(Graphics2D g2d)
	{
		if (fearTime > 0)
			g2d.setColor(Color.red);
		else
			g2d.setColor(MultiBoid.dynamicColours ? dirCols[(int)Math.toDegrees(Heading)] : Col);
		g2d.fill(new Ellipse2D.Double(Location.x - 2, Location.y - 2, 4, 4));
	}
	
	public boolean equals(Object other)
	{
		if (other instanceof Boid) return equals((Boid)other);
		return false;
	}
	
	public boolean equals(Boid other)
	{
		return ID == other.ID;
	}
}
