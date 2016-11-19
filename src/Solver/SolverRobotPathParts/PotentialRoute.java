package Solver.SolverRobotPathParts;

import PathExceptions.ChangePointInvalidRouteException;
import Solver.Board;
import Solver.BotPathDirection;
import Solver.DepthPointer;
import Solver.RobotPath;
import Solver.RouteBuildingInformation;

public class PotentialRoute implements Comparable<PotentialRoute> {
	private final RobotPathPart pathPart;
	public int requiredRobots;
	public final int x;
	public final int y;
	public boolean isValidPotentialRoute = true;
	
	public PotentialRoute(RobotPathPart pathParts, int x, int y)
	{
		this.pathPart = pathParts;
		requiredRobots = pathParts.requiredRobots;
		this.x = x;
		this.y = y;
	}
	
	public boolean buildPath(Board board)
	{
		boolean returnValue = pathPart.buildPath(board);
		if (!pathPart.isValidPathPart) {
			isValidPotentialRoute = false;
			return false;
		}
		requiredRobots = pathPart.requiredRobots;
		return returnValue;
	}
	
	public RouteBuildingInformation getPath(Board board)
	{
		RouteBuildingInformation routeInfo = new RouteBuildingInformation(board);
		for (int i = 0; i < pathPart.robotsThisPathIsFor.length; i++) {
			final int robot = pathPart.robotsThisPathIsFor[i];
			routeInfo.isBotMoveable[robot] = false;
		}
		printFillers(board);
		pathPart.addPath(routeInfo);
		isValidPotentialRoute = pathPart.isValidPathPart;

		if (routeInfo.canFindRoute &&
				   !routeInfo.botPositions[0].equals(board.goal)) {
			BotPathDirection[] routePart = RobotPath.getRobotPathToTarget(board, routeInfo.botPositions[0].x, routeInfo.botPositions[0].y, board.goal.x, board.goal.y, 0);
			if (routePart == null) {
				routeInfo.canFindRoute = false;
				return routeInfo;
			}
			for (int j = 0; j < routePart.length; j++) {
				routeInfo.route.add(routePart[j]);
			}
		}
		return routeInfo;
	}
	
	public void printFillers(Board board)
	{
		char[][] map = board.copyBoard();
		pathPart.printBoardLayered(map);
		System.out.println(Board.makeBoard(map));
		System.out.println("------------");
	}

	@Override
	public int compareTo(PotentialRoute o) {
		return requiredRobots - o.requiredRobots;
	}
}
