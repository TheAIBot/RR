package Solver.SolverRobotPathParts;

import java.util.PriorityQueue;

import PathExceptions.ChangePointInvalidRouteException;
import Solver.Board;
import Solver.DepthPointer;
import Solver.RobotPath;
import Solver.RouteBuildingInformation;

public class RobotDirectionChangePoint implements Comparable<RobotDirectionChangePoint> {
	public final PriorityQueue<RobotFiller> fillers;
	public RobotFiller bestFiller;
	//public RobotFiller mostReacentlyUsedFiller;
	public int requiredRobots;
	public final int x;
	public final int y;
	public RobotPathPart nextPathPart;
	private RobotPathPart parentPathPart;
	private DepthPointer interceptingPoint = null;
	public boolean removeWhenRouteFound = false;
	public boolean isValidDirectionChangePoint = true;
	
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
			//bestFiller.printFiller(board);
			interceptingPoint = RobotPath.getInterceptingDepthPointer(board, board.robotDepthMaps[0], x, y, bestFiller.changedDirection);
			//if true then a path for robot 0 has been found.
			if (interceptingPoint != null) {
				return true;
			} else {
				nextPathPart = RobotPath.getListOfPossiblePathsOrderedByBotsRequired(board, x, y, false, bestFiller.changedDirection.getOppositeDirection(), parentPathPart.robotsThisPathIsFor, parentPathPart.robotsToBuildPathWith);
				if (nextPathPart != null) {
					requiredRobots = nextPathPart.requiredRobots + bestFiller.requiredRobots;
				} else {
					isValidDirectionChangePoint = false;
				}
				return false;
			}
		} else {
			boolean toReturn = nextPathPart.buildPath(board);
			if (!nextPathPart.isValidPathPart) {
				isValidDirectionChangePoint = false;
				return false;
			}
			requiredRobots = nextPathPart.requiredRobots + bestFiller.requiredRobots;
			return toReturn;
		}
	}
	
	public void addPath(RouteBuildingInformation routeInfo)
	{
		if (nextPathPart != null) {
			nextPathPart.addPath(routeInfo);
			if (!nextPathPart.isValidPathPart) {
				isValidDirectionChangePoint = false;
				routeInfo.canFindRoute = false;
				return;
			}
		}
		if (routeInfo.canFindRoute) {
			while (!bestFiller.addPath(routeInfo)) {
				bestFiller = fillers.poll();
				if (bestFiller == null) {
					isValidDirectionChangePoint = false;
					routeInfo.canFindRoute = false;
					return;
				}
			}
			if (!routeInfo.hasTopStepBeenRemoved) {
				removeWhenRouteFound = true;
				routeInfo.hasTopStepBeenRemoved = true;
			}
		}
	}

	public void printAllFillers(Board board)
	{
		if (bestFiller != null) {
			bestFiller.printFiller(board);
		}
		fillers.stream().forEach(x -> x.printFiller(board));
	}
	
	public void printFillerLayer(char[][] board)
	{
		bestFiller.putFillerOnBoard(board);
		if (nextPathPart != null) {
			nextPathPart.printBoardLayered(board);
		}
	}
	
	@Override
	public int compareTo(RobotDirectionChangePoint o) {
		return requiredRobots - o.requiredRobots;
	}
}
