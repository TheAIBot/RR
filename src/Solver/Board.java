package Solver;

import java.awt.Point;
import java.util.Map;

public class Board {
	public static final char GOAL_CHAR = 'G';
	public static final char WALL_CHAR = '#';
	public static final char EMPTY_CHAR = ' ';
	public final MovePoint[][] staticMovePoints;

	private final char[][] board;
    public final int size;
    public final Point[] robots;
    public final Point goal;
    public final Map<Integer, DepthPointer>[] robotDepthMaps;

    @SuppressWarnings("unchecked")
	public Board(char[][] board, Point[] robots, Point goal) {
    	this.board = board;
        this.size = board.length;
        this.robots = robots;
        this.goal = goal;
        this.robotDepthMaps = (Map<Integer, DepthPointer>[])new Map[robots.length]; // shitty java needs compiler supressing
        this.staticMovePoints = new MovePoint[size][size];
    	try {
            createStaticMapPoints();
		} catch (Exception e) {
			e.printStackTrace();
		}
        robotDepthMaps[0] = RobotPath.getDepthGraph(this, robots[0].x, robots[0].y, goal.x, goal.y, false);
    }
     
    public boolean isInsideBoard(int x, int y) {
        return x >= 0 && 
        	   x < size && 
        	   y >= 0 && 
        	   y < size;
    }
    
    public boolean isWall(int x, int y)
    {
    	return board[x][y] == WALL_CHAR;
    }

    private void createStaticMapPoints()
    {
    	for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				if (!isWall(x, y)) {
					staticMovePoints[x][y] = new MovePoint(x, y);
				}
			}
    	}
    	
    	for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				if (!isWall(x, y)) {
					Point endPoint = getEndPoint(x, y, Directions.Up);
					if (endPoint.x != x || 
						endPoint.y != y) {
						staticMovePoints[x][y].up = staticMovePoints[endPoint.x][endPoint.y];
					}
					endPoint = getEndPoint(x, y, Directions.Down);
					if (endPoint.x != x || 
						endPoint.y != y) {
						staticMovePoints[x][y].down = staticMovePoints[endPoint.x][endPoint.y];
					}
					endPoint = getEndPoint(x, y, Directions.Left);
					if (endPoint.x != x || 
						endPoint.y != y) {
						staticMovePoints[x][y].left = staticMovePoints[endPoint.x][endPoint.y];
					}
					endPoint = getEndPoint(x, y, Directions.Right);
					if (endPoint.x != x || 
						endPoint.y != y) {
						staticMovePoints[x][y].right = staticMovePoints[endPoint.x][endPoint.y];
					}
				}
			}
    	}
    }
    
    
    public boolean isStopPoint(int x, int y, Directions direction)
    {
    	x += direction.translationX;
    	y += direction.translationY;
    	return !isValidPosition(x, y);
    }
        
    public boolean isValidPosition(int x, int y)
    {
    	return isInsideBoard(x, y) && !isWall(x, y);
    }

    private Point getEndPoint(int x, int y, Directions direction)
    {
    	final Point position = new Point(x, y);
    	while (isValidPosition(position.x + direction.translationX, 
    						   position.y + direction.translationY)) {
			position.translate(direction.translationX, direction.translationY);
		}
    	return position;
    }
    
    public String toString() {
    	StringBuilder sb = new StringBuilder(size * size);
    	for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[0].length; y++) {
				sb.append(board[y][x]);
			}
			sb.append(System.getProperty("line.separator"));
		}
        return sb.toString();
    }
    
}
