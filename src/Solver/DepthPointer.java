package Solver;

public final class DepthPointer {
	public final int depth;
	public final DepthPointer lowestDepth;
	public final Directions directionToLowest;
	public final int x;
	public final int y;
	public final int[] requiredRobots;
	
	public DepthPointer(int depth, DepthPointer lowestDepth, Directions directionToLowest, int x, int y)
	{
		this(depth, lowestDepth, directionToLowest, x, y, null);
	}
	
	public DepthPointer(int depth, DepthPointer lowestDepth, Directions directionToLowest, int x, int y, int[] requiredRobots)
	{
		this.depth = depth;
		this.lowestDepth = lowestDepth;
		this.directionToLowest = directionToLowest;
		this.x = x;
		this.y = y;
		this.requiredRobots = requiredRobots;
	}
}
