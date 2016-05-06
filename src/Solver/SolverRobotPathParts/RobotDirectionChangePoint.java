package Solver.SolverRobotPathParts;

import java.util.List;
import java.util.PriorityQueue;

import Solver.Board;
import Solver.BotPathDirection;
import Solver.DepthPointer;
import Solver.RobotPath;
import Solver.RouteBuildingInformation;

public class RobotDirectionChangePoint implements Comparable<RobotDirectionChangePoint> {
	public final PriorityQueue<RobotFiller> fillers;
	public RobotFiller bestFiller;
	public int requiredRobots;
	public final int x;
	public final int y;
	public RobotPathPart nextPathPart;
	private RobotPathPart parentPathPart;
	private DepthPointer interceptingPoint = null;
	
	public RobotDirectionChangePoint(PriorityQueue<RobotFiller> fillers, int x, int y, RobotPathPart parentPathPart) {
		this.fillers = fillers;
		this.bestFiller = fillers.poll();
		this.requiredRobots = bestFiller.requiredRobots;
		this.x = x;
		this.y = y;
		this.parentPathPart = parentPathPart;
	}
	
	public boolean buildPath(Board board)
	{
		if (nextPathPart == null) {
			interceptingPoint = RobotPath.getInterceptingDepthPointer(board, board.robotDepthMaps[0], x, y, bestFiller.changedDirection);
			//if true then a path for robot 0 has been found.
			if (interceptingPoint != null) {
				return true;
			} else {
				nextPathPart = RobotPath.getListOfPossiblePathsOrderedByBotsRequired(board, x, y, false, bestFiller.changedDirection, parentPathPart.robotsThisPathIsFor, parentPathPart.robotsToBuildPathWith);
				if (nextPathPart != null) {
					requiredRobots = nextPathPart.requiredRobots + bestFiller.requiredRobots;
				} else {
					requiredRobots = 100000;
				}
				return false;
			}
		} else {
			return nextPathPart.buildPath(board);
		}
	}
	
	public void addPath(RouteBuildingInformation routeInfo)
	{
		if (nextPathPart != null) {
			nextPathPart.addPath(routeInfo);
		}
		if (routeInfo.canFindRoute) {
			while (!bestFiller.addPath(routeInfo)) {
				bestFiller = fillers.poll();
				if (bestFiller == null) {
					throw new NullPointerException("No path was found");
				}
			}
		}
	}

	@Override
	public int compareTo(RobotDirectionChangePoint o) {
		return requiredRobots - o.requiredRobots;
	}
}
