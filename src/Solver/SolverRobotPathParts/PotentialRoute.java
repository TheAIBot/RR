package Solver.SolverRobotPathParts;

import java.awt.Point;
import java.util.List;

import Solver.Board;
import Solver.BotPathDirection;
import Solver.DepthPointer;
import Solver.Hasher;
import Solver.RobotPath;
import Solver.RouteBuildingInformation;

public class PotentialRoute implements Comparable<PotentialRoute> {
	private final RobotPathPart pathPart;
	public int requiredRobots;
	public final int x;
	public final int y;
	
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
		requiredRobots = pathPart.requiredRobots;
		return returnValue;
	}
	
	public List<BotPathDirection> getPath(Board board)
	{
		RouteBuildingInformation routeInfo = new RouteBuildingInformation(board);
		for (int i = 0; i < pathPart.robotsThisPathIsFor.length; i++) {
			final int robot = pathPart.robotsThisPathIsFor[i];
			routeInfo.isBotMoveable[robot] = false;
		}
		pathPart.addPath(routeInfo);
		if (routeInfo.canFindRoute) {
			return routeInfo.route;
		} else {
			return null;
		}
	}

	@Override
	public int compareTo(PotentialRoute o) {
		return requiredRobots - o.requiredRobots;
	}
}
