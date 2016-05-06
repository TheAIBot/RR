package Solver.SolverRobotPathParts;

import java.util.List;
import java.util.PriorityQueue;

import PathExceptions.ChangePointInvalidRouteException;
import Solver.Board;
import Solver.BotPathDirection;
import Solver.RouteBuildingInformation;

public class RobotPathPart {
	public final PriorityQueue<RobotDirectionChangePoint> robotDirectionChangePoints = new PriorityQueue<RobotDirectionChangePoint>();
	public int requiredRobots = 0;
	public final int x;
	public final int y;
	public final int[] robotsThisPathIsFor;
	public final int[] robotsToBuildPathWith;
	
	public RobotPathPart(int x, int y, int[] robotsThisPathIsFor, int[] robotsToBuildPathWith)
	{
		this.x = x;
		this.y = y;
		this.robotsThisPathIsFor = robotsThisPathIsFor;
		this.robotsToBuildPathWith = robotsToBuildPathWith;
	}
	
	public boolean buildPath(Board board)
	{
		RobotDirectionChangePoint robotNewDirection = robotDirectionChangePoints.poll();
		
		boolean returnValue = robotNewDirection.buildPath(board);
		
		
		robotDirectionChangePoints.add(robotNewDirection);
		requiredRobots = robotDirectionChangePoints.peek().requiredRobots;
		return returnValue;
	}
	
	public void addPath(RouteBuildingInformation routeInfo)
	{
		try {
			robotDirectionChangePoints.peek().addPath(routeInfo);
		} catch (ChangePointInvalidRouteException e) {
			routeInfo.canFindRoute = false;
		}
	}
	
	public void addRobotDirectionChangePoint(RobotDirectionChangePoint changePoint)
	{
		robotDirectionChangePoints.add(changePoint);
		requiredRobots = robotDirectionChangePoints.peek().requiredRobots;
	}
}
