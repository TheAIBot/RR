package Solver;

import java.awt.Point;

public class Hasher {
	public static int hashPoint(Point point)
	{
		return hashPoint(point.x, point.y);
	}
	
	public static int hashPoint(int x, int y)
	{
		return x + y * 10000;
	}
}
