package Solver;


public class MovePoint {
	public MovePoint up;
	public MovePoint down;
	public MovePoint left;
	public MovePoint right;
	public final int x;
	public final int y;
	
	public MovePoint(int x, int y)
	{
		this.x = x;
		this.y = y;
	}	
	public MovePoint(int x, int y, MovePoint up, MovePoint down, MovePoint left, MovePoint right)
	{
		this.x = x;
		this.y = y;
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
	}
	
	public int distanceTo(MovePoint otherMovePoint)
	{
		return Math.abs(x - otherMovePoint.x) + Math.abs(y - otherMovePoint.y);
	}
}
