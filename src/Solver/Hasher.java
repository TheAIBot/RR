package Solver;

import java.awt.Point;

public class Hasher {
	public static int hashPoint(Point point)
	{
		return hashPoint(point.x, point.y);
	}
	
	public static int hashPoint(int x, int y)
	{
		return x + 
			   y * 10000;
	}
	
	public static long hashPoint(long x1, long y1, long x2, long y2)
	{
		return x1 + 
			   y1 * 10000 + 
			   x2 * (10000 * 10000) + 
			   y2 * (10000 * 10000 * 10000);
	}
}
