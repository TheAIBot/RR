package Solver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import Solver.SolverRobotPathParts.PotentialRoute;
import Solver.SolverRobotPathParts.RobotDirectionChangePoint;
import Solver.SolverRobotPathParts.RobotFiller;
import Solver.SolverRobotPathParts.RobotPathPart;

public final class RobotPath {
	
	//optimizations:
	//don't make Fillers when the direction the 0 robot will come from is blocked
	//
	//need to add:
	//ability to find a path to a filler
	//find a good way to figure out which robots to use in a filler. maybe reserve robots on the way up the path
	
	public static BotPathDirection[] getRobotPath(Board board, long timeConstraint)
	{
		if (board.robots.length == 1) {
			return getRobotPathToTarget(board, board.robots[0].x, board.robots[0].y, board.goal.x, board.goal.y, 0);
		} else {
			final int[] robotToGoal = new int[] {0};
			final int[] fillerRobots = new int[board.robots.length - 1];
			for (int i = 1; i <= fillerRobots.length; i++) {
				fillerRobots[i - 1] = i;
			}
			return getRoute(board, board.robots[0].x, board.robots[0].y, board.goal.x, board.goal.y, timeConstraint, robotToGoal, fillerRobots);
		}
	}
	
	public static BotPathDirection[] getRobotPathToTarget(Board board, int startX, int startY, int endX, int endY, int robotNumber)
	{
		final Map<Integer, DepthPointer> depthGraph = getDepthGraph(board, startX, startY, endX, endY, true);
		if (!depthGraph.containsKey(Hasher.hashPoint(endX, endY))) {
			return null;
		}
		DepthPointer highestPoint = depthGraph.get(Hasher.hashPoint(endX, endY));
		return getRobotPathToTargetFromDepthPoint(highestPoint, robotNumber);
	}
	
	public static BotPathDirection[] getRobotPathToTargetFromDepthPoint(DepthPointer highestPoint, int robotNumber)
	{
		final BotPathDirection[] directions = new BotPathDirection[highestPoint.depth];
		int index = highestPoint.depth - 1;
		while (highestPoint.directionToLowest != null) {
			directions[index] = new BotPathDirection(robotNumber, highestPoint.directionToLowest);
			highestPoint = highestPoint.lowestDepth;
			index--;
		}
		return directions;
	}
	
	
 	public static Map<Integer, DepthPointer> getDepthGraph(Board board, int startX, int startY, int endX, int endY, boolean notMakeWholeGraph)
	{
		final Map<Integer, DepthPointer> depthGraph = new HashMap<Integer, DepthPointer>();
		final Queue<MovePoint> toGoThrough = new LinkedList<MovePoint>();
		final Map<Integer, Boolean> checkedPoints = new HashMap<Integer, Boolean>();
		
		depthGraph.put(Hasher.hashPoint(startX, startY), new DepthPointer(0, null, null, startX, startY));
		toGoThrough.add(board.staticMovePoints[startX][startY]);
		
		while (toGoThrough.size() > 0) {
			final MovePoint toCheck = toGoThrough.poll();
			final DepthPointer lowestDepth = depthGraph.get(Hasher.hashPoint(toCheck.x, toCheck.y));
			if (toCheck.up != null) {
				final int hash = Hasher.hashPoint(toCheck.up.x, toCheck.up.y);
				if (!checkedPoints.containsKey(hash)) {
					depthGraph.put(hash, new DepthPointer(lowestDepth.depth + 1, lowestDepth, Directions.Up, toCheck.up.x, toCheck.up.y));
					if (notMakeWholeGraph &&
						toCheck.up.x == endX &&
						toCheck.up.y == endY) {
						return depthGraph;
					}
					toGoThrough.add(toCheck.up);
					checkedPoints.put(hash, null);
				}
			}
			if (toCheck.down != null) {
				final int hash = Hasher.hashPoint(toCheck.down.x, toCheck.down.y);
				if (!checkedPoints.containsKey(hash)) {
					depthGraph.put(hash, new DepthPointer(lowestDepth.depth + 1, lowestDepth, Directions.Down, toCheck.down.x, toCheck.down.y));
					if (notMakeWholeGraph &&
						toCheck.down.x == endX &&
						toCheck.down.y == endY) {
						return depthGraph;
					}
					toGoThrough.add(toCheck.down);
					checkedPoints.put(hash, null);
				}
			}
			if (toCheck.left != null) {
				final int hash = Hasher.hashPoint(toCheck.left.x, toCheck.left.y);
				if (!checkedPoints.containsKey(hash)) {
					depthGraph.put(hash, new DepthPointer(lowestDepth.depth + 1, lowestDepth, Directions.Left, toCheck.left.x, toCheck.left.y));
					if (notMakeWholeGraph &&
						toCheck.left.x == endX &&
						toCheck.left.y == endY) {
						return depthGraph;
					}
					toGoThrough.add(toCheck.left);
					checkedPoints.put(hash, null);
				}
			}
			if (toCheck.right != null) {
				final int hash = Hasher.hashPoint(toCheck.right.x, toCheck.right.y);
				if (!checkedPoints.containsKey(hash)) {
					depthGraph.put(hash, new DepthPointer(lowestDepth.depth + 1, lowestDepth, Directions.Right, toCheck.right.x, toCheck.right.y));
					if (notMakeWholeGraph &&
						toCheck.right.x == endX &&
						toCheck.right.y == endY) {
						return depthGraph;
					}
					toGoThrough.add(toCheck.right);
					checkedPoints.put(hash, null);
				}
			}
		}
		return depthGraph;
	}

	
	
	private static BotPathDirection[] getRoute(Board board, int startX, int startY, int endX, int endY, long timeConstraint, int[] robotsThisPathIsFor, int[] robotsToBuildPathWith)
	{
		BotPathDirection[] bestRoute = null;
		
		final long startTime = System.nanoTime();
		
		final Map<Integer, Boolean> checkedPoints = new HashMap<Integer, Boolean>();
		final PriorityQueue<PotentialRoute> routesToCheckout = new PriorityQueue<PotentialRoute>();
		checkedPoints.put(Hasher.hashPoint(startX, startY), true);

		PotentialRoute upRoute = getPotentialRouteFromGoal(board, endX, endY, Directions.Up, robotsThisPathIsFor, robotsToBuildPathWith);
		if (upRoute != null) {
			routesToCheckout.add(upRoute);
		}
		
		PotentialRoute downRoute = getPotentialRouteFromGoal(board, endX, endY, Directions.Down, robotsThisPathIsFor, robotsToBuildPathWith);
		if (downRoute != null) {
			routesToCheckout.add(downRoute);
		}
		
		PotentialRoute leftRoute = getPotentialRouteFromGoal(board, endX, endY, Directions.Left, robotsThisPathIsFor, robotsToBuildPathWith);
		if (leftRoute != null) {
			routesToCheckout.add(leftRoute);
		}
		
		PotentialRoute rightRoute = getPotentialRouteFromGoal(board, endX, endY, Directions.Right, robotsThisPathIsFor, robotsToBuildPathWith);
		if (rightRoute != null) {
			routesToCheckout.add(rightRoute);
		}
		
		//bestRoute = routesToCheckout.peek();
		
		while (System.nanoTime() - startTime + (timeConstraint * 0.2 * 1000000000L) < timeConstraint * 1000000000L) {
		//while (true) {
			final PotentialRoute shortestRoute = routesToCheckout.poll();
			if (shortestRoute == null) {
				break;
			}
			if (!shortestRoute.isValidPotentialRoute) {
				continue;
			}
			if (shortestRoute.buildPath(board)) {
				final RouteBuildingInformation routeInfo = shortestRoute.getPath(board);
				if (routeInfo.route != null && routeInfo.canFindRoute) {
					//Tools.printPath(routeInfo.route);
					if (bestRoute == null ||
							routeInfo.route.size() < bestRoute.length) {
							bestRoute = routeInfo.route.toArray(new BotPathDirection[routeInfo.route.size()]);
						}
				}
			} else if (!shortestRoute.isValidPotentialRoute) {
				continue;
			}
			if (shortestRoute.isValidPotentialRoute) {
				routesToCheckout.add(shortestRoute);
			}
		}
		
		
		return bestRoute;
	}
	
	private static boolean isValidRouteStartDirection(Board board, int x, int y, Directions direction)
	{
		if (!board.isValidPosition(x + direction.translationX, y + direction.translationY)) {
			return false;
		}
		MovePoint movePoint = board.staticMovePoints[x + direction.translationX][y + direction.translationY];
		//if it's a wall
		if (movePoint == null) {
			return false;
		}
		return true;
	}
	
	private static PotentialRoute getPotentialRouteFromGoal(Board board, int endX, int endY, Directions stopPointDirection, int[] robotsThisPathIsFor, int[] robotsToBuildPathWith)
	{
		if (isValidRouteStartDirection(board, endX, endY, stopPointDirection.getOppositeDirection())) {
			RobotPathPart part = getListOfPossiblePathsOrderedByBotsRequired(board, 
					  endX, 
					  endY, 
					  !board.isStopPoint(endX, endY, stopPointDirection), 
					  stopPointDirection, 
					  robotsThisPathIsFor, 
					  robotsToBuildPathWith);
			if (part != null) {
				//part.printPart(board);
				return new PotentialRoute(part, endX,  endY);
			}
		}
		return null;
	}
		
	public static RobotPathPart getListOfPossiblePathsOrderedByBotsRequired(Board board, int x, int y, boolean hasNoStopPoint, Directions stopPointDirection, int[] robotsThisPathIsFor, int[] robotsToBuildPathWith)
	{
		final RobotPathPart robotPathPart = new RobotPathPart(x, y, robotsThisPathIsFor, robotsToBuildPathWith);
		if (!hasNoStopPoint) {
			stopPointDirection = stopPointDirection.getOppositeDirection();
		}
		x += stopPointDirection.translationX;
		y += stopPointDirection.translationY;
		
		if (board.isValidPosition(x, y) && hasNoStopPoint) {
			addRobotDirectionChangePoint(board, board.staticMovePoints[x][y], robotPathPart, stopPointDirection, stopPointDirection.getOppositeDirection(), x, y, hasNoStopPoint);
		}
		
		while (board.isValidPosition(x, y)) {
			final MovePoint movePoint = board.staticMovePoints[x][y];
			switch (stopPointDirection) {
				case Up:
				case Down:
					addRobotDirectionChangePoint(board, movePoint, robotPathPart, stopPointDirection, Directions.Left, x, y, hasNoStopPoint);
					addRobotDirectionChangePoint(board, movePoint, robotPathPart, stopPointDirection, Directions.Right, x, y, hasNoStopPoint);
					break;
				case Left:
				case Right:
					addRobotDirectionChangePoint(board, movePoint, robotPathPart, stopPointDirection, Directions.Up, x, y, hasNoStopPoint);
					addRobotDirectionChangePoint(board, movePoint, robotPathPart, stopPointDirection, Directions.Down, x, y, hasNoStopPoint);
					break;
			}
			if (hasNoStopPoint) {
				break;
			}
			x += stopPointDirection.translationX;
			y += stopPointDirection.translationY;
		}
		if (robotPathPart.robotDirectionChangePoints.size() == 0) {
			return null;
		} else {
			return robotPathPart;
		}
	}
	
	private static void addRobotDirectionChangePoint(Board board, 
													 MovePoint movePoint,
													 RobotPathPart robotPathPart,
													 Directions direction, 
													 Directions fillerDirection,
													 int x, 
													 int y, 
													 boolean robotOnEnd)
	{
		PriorityQueue<RobotFiller> fillers = new PriorityQueue<RobotFiller>();
		switch (fillerDirection) {
			case Up:
				getFillersUp(board, movePoint, fillers, x, y, robotOnEnd, direction.getOppositeDirection(), robotPathPart);
				break;
			case Down:
				getFillersDown(board, movePoint, fillers, x, y, robotOnEnd, direction.getOppositeDirection(), robotPathPart);
				break;
			case Left:
				getFillersLeft(board, movePoint, fillers, x, y, robotOnEnd, direction.getOppositeDirection(), robotPathPart);
				break;
			case Right:
				getFillersRight(board, movePoint, fillers, x, y, robotOnEnd, direction.getOppositeDirection(), robotPathPart);
				break;
		}
		if (fillers.size() > 0) {
			robotPathPart.addRobotDirectionChangePoint(new RobotDirectionChangePoint(fillers, x, y, robotPathPart));
		}
	}
	
	private static void getFillersUp(Board board, MovePoint startPoint, PriorityQueue<RobotFiller> fillers, int x, int y, boolean robotOnEnd, Directions cameFromDirection, RobotPathPart parentPart)
	{
		int extra = 0;
		Directions changedDirection = Directions.Down;
		if (robotOnEnd) {
			extra = 1;
			changedDirection = cameFromDirection;
		}
		if (startPoint.up != null) {
			//is there any point to making it move up to moveX <= movePoint.left.x?
			//-1 so the search starts at a valid search position to the left
				for (int moveY = y - 1; moveY >= startPoint.up.y; moveY--) {
					MovePoint possibleRobotAnchorPoint = board.staticMovePoints[x][moveY];
					if (possibleRobotAnchorPoint.left == null ||
						possibleRobotAnchorPoint.right == null) {
						fillers.add(new RobotFiller(startPoint, possibleRobotAnchorPoint, y - moveY + extra, changedDirection, robotOnEnd, parentPart));
					} else if (moveY == startPoint.up.y) {
						fillers.add(new RobotFiller(startPoint, possibleRobotAnchorPoint, y - moveY + extra, changedDirection, robotOnEnd, parentPart));
					}
				}
			//need to add filler here that goes from end to start
		} else if (robotOnEnd) {
			fillers.add(new RobotFiller(startPoint, startPoint, 1, changedDirection, robotOnEnd, parentPart));
		}
	}
	
	private static void getFillersDown(Board board, MovePoint startPoint, PriorityQueue<RobotFiller> fillers, int x, int y, boolean robotOnEnd, Directions cameFromDirection, RobotPathPart parentPart)
	{
		int extra = 0;
		Directions changedDirection = Directions.Up;
		if (robotOnEnd) {
			extra = 1;
			changedDirection = cameFromDirection;
		}
		if (startPoint.down != null) {
			//is there any point to making it move up to moveX <= movePoint.left.x?
			//+1 so the search starts at a valid search position to the left
				for (int moveY = y + 1; moveY <= startPoint.down.y; moveY++) {
					MovePoint possibleRobotAnchorPoint = board.staticMovePoints[x][moveY];
					if (possibleRobotAnchorPoint.left == null ||
						possibleRobotAnchorPoint.right == null) {
						fillers.add(new RobotFiller(startPoint, possibleRobotAnchorPoint, moveY - y + extra, changedDirection, robotOnEnd, parentPart));
					} else if (moveY == startPoint.down.y) {
						fillers.add(new RobotFiller(startPoint, possibleRobotAnchorPoint, moveY - y + extra, changedDirection, robotOnEnd, parentPart));
					}
				}
		} else if (robotOnEnd) {
			fillers.add(new RobotFiller(startPoint, startPoint, 1, changedDirection, robotOnEnd, parentPart));
		}
	}
	
	private static void getFillersLeft(Board board, MovePoint startPoint, PriorityQueue<RobotFiller> fillers, int x, int y, boolean robotOnEnd, Directions cameFromDirection, RobotPathPart parentPart)
	{
		int extra = 0;
		Directions changedDirection = Directions.Right;
		if (robotOnEnd) {
			extra = 1;
			changedDirection = cameFromDirection;
		}
		if (startPoint.left != null) {
			//is there any point to making it move up to moveX <= movePoint.left.x?
			//-1 so the search starts at a valid search position to the left
				for (int moveX = x - 1; moveX >= startPoint.left.x; moveX--) {
					MovePoint possibleRobotAnchorPoint = board.staticMovePoints[moveX][y];
					if (possibleRobotAnchorPoint.up == null ||
						possibleRobotAnchorPoint.down == null) {
						fillers.add(new RobotFiller(startPoint, possibleRobotAnchorPoint, x - moveX + extra, changedDirection, robotOnEnd, parentPart));
					} else if (moveX == startPoint.left.x) {
						fillers.add(new RobotFiller(startPoint, possibleRobotAnchorPoint, x - moveX + extra, changedDirection, robotOnEnd, parentPart));
					}
				}
		} else if (robotOnEnd) {
			fillers.add(new RobotFiller(startPoint, startPoint, 1, changedDirection, robotOnEnd, parentPart));
		}
	}
	
	private static void getFillersRight(Board board, MovePoint startPoint, PriorityQueue<RobotFiller> fillers, int x, int y, boolean robotOnEnd, Directions cameFromDirection, RobotPathPart parentPart)
	{
		int extra = 0;
		Directions changedDirection = Directions.Left;
		if (robotOnEnd) {
			extra = 1;
			changedDirection = cameFromDirection;
		}
		if (startPoint.right != null) {
			//is there any point to making it move up to moveX <= movePoint.left.x?
			//+1 so the search starts at a valid search position to the left
				for (int moveX = x + 1; moveX <= startPoint.right.x; moveX++) {
					MovePoint possibleRobotAnchorPoint = board.staticMovePoints[moveX][y];
					if (possibleRobotAnchorPoint.up == null ||
						possibleRobotAnchorPoint.down == null) {
						fillers.add(new RobotFiller(startPoint, possibleRobotAnchorPoint, moveX - x + extra, changedDirection, robotOnEnd, parentPart));
					} else if (moveX == startPoint.right.x) {
						fillers.add(new RobotFiller(startPoint, possibleRobotAnchorPoint, moveX - x + extra, changedDirection, robotOnEnd, parentPart));
					}
				}
		} else if (robotOnEnd) {
			fillers.add(new RobotFiller(startPoint, startPoint, 1, changedDirection, robotOnEnd, parentPart));
		}
	}

	public static DepthPointer getInterceptingDepthPointer(Board board, Map<Integer, DepthPointer> DepthMap, int x, int y, Directions interceptDirection)
	{
		final MovePoint currentPosition = board.staticMovePoints[x][y];
		DepthPointer interceptingDepthPointer = null;
		switch (interceptDirection) {
			case Up:
				if (currentPosition.up != null) {
					for (DepthPointer depthPointer : DepthMap.values()) {
						if (depthPointer.x == x &&
							depthPointer.directionToLowest == Directions.Down &&
							depthPointer.y >= y && 
							depthPointer.y >= currentPosition.up.y) {
							interceptingDepthPointer = depthPointer;
							break;
						}
					}
				}
				break;
			case Down:
				if (currentPosition.down != null) {
					for (DepthPointer depthPointer : DepthMap.values()) {
						if (depthPointer.x == x &&
							depthPointer.directionToLowest == Directions.Up &&
							depthPointer.y <= y && 
							depthPointer.y <= currentPosition.down.y) {
							interceptingDepthPointer = depthPointer;
							break;
						}
					}
				}
				break;
			case Left:
				if (currentPosition.left != null) {
					for (DepthPointer depthPointer : DepthMap.values()) {
						if (depthPointer.y == y &&
							depthPointer.directionToLowest == Directions.Right &&
							depthPointer.x >= x && 
							depthPointer.x >= currentPosition.left.x) {
							interceptingDepthPointer = depthPointer;
							break;
						}
					}
				}
				break;
			case Right:
				if (currentPosition.right != null) {
					for (DepthPointer depthPointer : DepthMap.values()) {
						if (depthPointer.y == y &&
							depthPointer.directionToLowest == Directions.Left &&
							depthPointer.x <= x && 
							depthPointer.x <= currentPosition.right.x) {
							interceptingDepthPointer = depthPointer;
							break;
						}
					}
				}
				break;
		}
		return interceptingDepthPointer;
	}
}
