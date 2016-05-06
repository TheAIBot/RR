package Solver;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
	//consider moving RobotDirectionChangePoint into RobotPathPart
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
		
		//while (System.nanoTime() - startTime + (timeConstraint * 0.2) < timeConstraint) {
		while (true) {
			final PotentialRoute shortestRoute = routesToCheckout.poll();
			if (shortestRoute.buildPath(board)) {
				final List<BotPathDirection> possibleBestRoute = shortestRoute.getPath(board);
				if (possibleBestRoute != null &&
					(bestRoute == null ||
					 possibleBestRoute.size() < bestRoute.length)) {
					bestRoute = possibleBestRoute.toArray(new BotPathDirection[possibleBestRoute.size()]);
				}
			}
			routesToCheckout.add(shortestRoute);
			//shortestRoute.buildPath(board);
		}
		
		
		//return bestRoute;
	}
	
	private static PotentialRoute getPotentialRouteFromGoal(Board board, int endX, int endY, Directions stopPointDirection, int[] robotsThisPathIsFor, int[] robotsToBuildPathWith)
	{
		RobotPathPart part = getListOfPossiblePathsOrderedByBotsRequired(board, 
				  endX, 
				  endY, 
				  !board.isStopPoint(endX, endY, stopPointDirection), 
				  stopPointDirection.getOppositeDirection(), 
				  robotsThisPathIsFor, 
				  robotsToBuildPathWith);
		if (part != null) {
			return new PotentialRoute(part, endX,  endY);
		} else {
			return null;
		}

	}
		
	public static RobotPathPart getListOfPossiblePathsOrderedByBotsRequired(Board board, int x, int y, boolean robotOnEnd, Directions direction, int[] robotsThisPathIsFor, int[] robotsToBuildPathWith)
	{
		final RobotPathPart robotPathPart = new RobotPathPart(x, y, robotsThisPathIsFor, robotsToBuildPathWith);
		x += direction.translationX;
		y += direction.translationY;
		while (board.isValidPosition(x, y)) {
			final MovePoint movePoint = board.staticMovePoints[x][y];
			boolean isFillerFollowingPathAllowed = true;
			switch (direction) {
				case Up:
					isFillerFollowingPathAllowed = robotOnEnd && !(movePoint.up == null && (movePoint.left == null || movePoint.right == null));
					break;
				case Down:
					isFillerFollowingPathAllowed = robotOnEnd && !(movePoint.down == null && (movePoint.left == null || movePoint.right == null));
					break;
				case Left:
					isFillerFollowingPathAllowed = robotOnEnd && !(movePoint.left == null && (movePoint.up == null || movePoint.down == null));
					break;
				case Right:
					isFillerFollowingPathAllowed = robotOnEnd && !(movePoint.right == null && (movePoint.up == null || movePoint.down == null));
					break;
			}
			switch (direction) {
			case Up:
			case Down:
				addRobotDirectionChangePoint(board, movePoint, robotPathPart, direction, Directions.Left, x, y, robotOnEnd, isFillerFollowingPathAllowed);
				addRobotDirectionChangePoint(board, movePoint, robotPathPart, direction, Directions.Right, x, y, robotOnEnd, isFillerFollowingPathAllowed);
				break;
			case Left:
			case Right:
				addRobotDirectionChangePoint(board, movePoint, robotPathPart, direction, Directions.Up, x, y, robotOnEnd, isFillerFollowingPathAllowed);
				addRobotDirectionChangePoint(board, movePoint, robotPathPart, direction, Directions.Down, x, y, robotOnEnd, isFillerFollowingPathAllowed);
				break;
			}
			if (robotOnEnd) {
				break;
			}
			x += direction.translationX;
			y += direction.translationY;
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
													 boolean robotOnEnd, 
													 boolean isFillerFollowingPathAllowed)
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
		if (isFillerFollowingPathAllowed) {
			RobotFiller filler = getFillerFollowingPath(movePoint, direction, robotPathPart, robotOnEnd);
			if (filler != null) {
				fillers.add(filler);	
			}
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
				for (int moveY = y + 1; moveY < startPoint.down.y; moveY++) {
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
				for (int moveX = x + 1; moveX < startPoint.right.x; moveX++) {
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

	private static RobotFiller getFillerFollowingPath(MovePoint start, Directions direction, RobotPathPart parentPart, boolean robotOnEnd)
	{
		int extra = 0;
		if (robotOnEnd) {
			extra = 1;
		}
		switch (direction) {
			case Up:
				if (start.up != null) {
					return new RobotFiller(start, start.up, start.y - start.up.y + extra, direction.getOppositeDirection(), true, parentPart);
				}
			case Down:
				if (start.down != null) {
					return new RobotFiller(start, start.down, start.down.y - start.y + extra, direction.getOppositeDirection(), true, parentPart);
				}
			case Left:
				if (start.left != null) {
					return new RobotFiller(start, start.left, start.x - start.left.x + extra, direction.getOppositeDirection(), true, parentPart);
				}
			case Right:
				if (start.right != null) {
					return new RobotFiller(start, start.right, start.right.x - start.x + extra, direction.getOppositeDirection(), true, parentPart);
				}
		}
		return null;
	}

	public static DepthPointer getInterceptingDepthPointer(Board board, Map<Integer, DepthPointer> DepthMap, int x, int y, Directions interceptDirection)
	{
		final MovePoint currentPosition = board.staticMovePoints[x][y];
		DepthPointer interceptingDepthPointer = null;
		switch (interceptDirection) {
			case Up:
				if (currentPosition.up != null) {
					for (DepthPointer depthPointer : DepthMap.values()) {
						if (depthPointer.y == y &&
							depthPointer.directionToLowest == Directions.Down &&
							depthPointer.x >= x && 
							depthPointer.x <= currentPosition.up.x) {
							interceptingDepthPointer = depthPointer;
							break;
						}
					}
				}
				break;
			case Down:
				if (currentPosition.down != null) {
					for (DepthPointer depthPointer : DepthMap.values()) {
						if (depthPointer.y == y &&
							depthPointer.directionToLowest == Directions.Up &&
							depthPointer.x <= x && 
							depthPointer.x >= currentPosition.down.x) {
							interceptingDepthPointer = depthPointer;
							break;
						}
					}
				}
				break;
			case Left:
				if (currentPosition.left != null) {
					for (DepthPointer depthPointer : DepthMap.values()) {
						if (depthPointer.x == x &&
							depthPointer.directionToLowest == Directions.Right &&
							depthPointer.y <= y && 
							depthPointer.y >= currentPosition.left.y) {
							interceptingDepthPointer = depthPointer;
							break;
						}
					}
				}
				break;
			case Right:
				if (currentPosition.right != null) {
					for (DepthPointer depthPointer : DepthMap.values()) {
						if (depthPointer.x == x &&
							depthPointer.directionToLowest == Directions.Left &&
							depthPointer.y >= y && 
							depthPointer.y <= currentPosition.right.y) {
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
