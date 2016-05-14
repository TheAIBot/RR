package Solver.SolverRobotPathParts;

import java.util.PriorityQueue;

import PathExceptions.ChangePointInvalidRouteException;
import Solver.Board;
import Solver.RouteBuildingInformation;

public class RobotPathPart {
	public final PriorityQueue<RobotDirectionChangePoint> robotDirectionChangePoints = new PriorityQueue<RobotDirectionChangePoint>();
	public RobotDirectionChangePoint mostRecentlyusedChangePoint;
	public int requiredRobots = 0;
	public final int x;
	public final int y;
	public final int[] robotsThisPathIsFor;
	public final int[] robotsToBuildPathWith;
	public boolean isValidPathPart = true;
	
	public RobotPathPart(int x, int y, int[] robotsThisPathIsFor, int[] robotsToBuildPathWith)
	{
		this.x = x;
		this.y = y;
		this.robotsThisPathIsFor = robotsThisPathIsFor;
		this.robotsToBuildPathWith = robotsToBuildPathWith;
	}
	
	public boolean buildPath(Board board)
	{
		mostRecentlyusedChangePoint = robotDirectionChangePoints.poll();
		
		boolean returnValue = mostRecentlyusedChangePoint.buildPath(board);
		if (!mostRecentlyusedChangePoint.isValidDirectionChangePoint) {
			if (robotDirectionChangePoints.size() == 0) {
				isValidPathPart = false;
				return false;
			}
			requiredRobots = robotDirectionChangePoints.peek().requiredRobots;
			return false;
		} else {
			robotDirectionChangePoints.add(mostRecentlyusedChangePoint);
		}
		requiredRobots = robotDirectionChangePoints.peek().requiredRobots;
		return returnValue;		
	}
	
	public void addPath(RouteBuildingInformation routeInfo)
	{
		mostRecentlyusedChangePoint.addPath(routeInfo);
		if (mostRecentlyusedChangePoint.removeWhenRouteFound ||
			!mostRecentlyusedChangePoint.isValidDirectionChangePoint) {
			robotDirectionChangePoints.remove(mostRecentlyusedChangePoint); // super bad performing solution
			if (robotDirectionChangePoints.size() == 0) {
				isValidPathPart = false;
				return;
			}
			requiredRobots = robotDirectionChangePoints.peek().requiredRobots;
		}
	}
	
	public void addRobotDirectionChangePoint(RobotDirectionChangePoint changePoint)
	{
		robotDirectionChangePoints.add(changePoint);
		requiredRobots = robotDirectionChangePoints.peek().requiredRobots;
	}

	public void printPart(Board board)
	{
		robotDirectionChangePoints.stream().forEach(x -> x.printAllFillers(board));
	}
	
	public void printBoardLayered(char[][] board)
	{
		mostRecentlyusedChangePoint.printFillerLayer(board);
	}
}
