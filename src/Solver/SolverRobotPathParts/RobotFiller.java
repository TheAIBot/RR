package Solver.SolverRobotPathParts;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import Solver.Board;
import Solver.BotPathDirection;
import Solver.DepthPointer;
import Solver.Directions;
import Solver.Hasher;
import Solver.MovePoint;
import Solver.RobotPath;
import Solver.RouteBuildingInformation;

public class RobotFiller implements Comparable<RobotFiller> {
	public final MovePoint start;
	public final MovePoint end;
	public final int requiredRobots;
	public final Directions changedDirection;
	public final boolean robotOnEnd;
	public final RobotPathPart containerOfThisFiller;
	
	public RobotFiller(MovePoint start, MovePoint end, int requiredRobots, Directions changedDirection, boolean robotOnEnd, RobotPathPart containerOfThisFiller)
	{
		this.start = start;
		this.end = end;
		this.requiredRobots = requiredRobots;
		this.changedDirection = changedDirection;
		this.robotOnEnd = robotOnEnd;
		this.containerOfThisFiller = containerOfThisFiller;
	}
	
	public boolean buildPath(Board board)
	{
		throw new UnsupportedOperationException("can't build path for fillers yet");
		//need to make this possible to add RobotPathParts so robots
		//can be used to build a path to this path
	}
	
	public boolean addPath(RouteBuildingInformation routeInfo)
	{
		if (requiredRobots == 0) {
			return true;
		}
		//printFiller(routeInfo.board);
		final List<BotPathDirection> thisFillersRoute = new ArrayList<BotPathDirection>();
		final boolean[] isBotMoveable = Arrays.copyOf(routeInfo.isBotMoveable, routeInfo.isBotMoveable.length);
		final Point[] botPositions = new Point[routeInfo.botPositions.length];
		for (int i = 0; i < botPositions.length; i++) {
			botPositions[i] = new Point(routeInfo.botPositions[i]);
		}
		
		final int numberOfRobotsRequired = Math.abs(end.x - start.x) + Math.abs(end.y - start.y) + ((robotOnEnd)? 1 : 0);
		final Point[] fillerPoints = new Point[numberOfRobotsRequired];
		final int[] robotsPath = new int[numberOfRobotsRequired];
		int robotFillerIndex = 0;
		
		Directions robotBuildDirection = getDirectionFromTranslation(start.x - end.x, start.y - end.y);
		
		
		int currentPosX = end.x;
		int currentPosY = end.y;
		for (int i = 0; i < fillerPoints.length; i++) {
			fillerPoints[i] = new Point(currentPosX, currentPosY);
			currentPosX += robotBuildDirection.translationX;
			currentPosY += robotBuildDirection.translationY;
		}
		int previousRobotFiller = -1;
		for (int q = 0; q < fillerPoints.length; q++) {
			Point goal =  fillerPoints[q];
			final int[] robotIndexForRobotsThatCanReachFiller = new int[containerOfThisFiller.robotsToBuildPathWith.length];
			final int[] robotIndexLength = new int[robotIndexForRobotsThatCanReachFiller.length];
			final DepthPointer[] robotIndexDepthPointer = new DepthPointer[robotIndexForRobotsThatCanReachFiller.length];
			for (int i = 0; i < robotIndexForRobotsThatCanReachFiller.length; i++) {
				robotIndexForRobotsThatCanReachFiller[i] = -1;
			}
			for (int i = 0; i < containerOfThisFiller.robotsToBuildPathWith.length; i++) {
				final int robot = containerOfThisFiller.robotsToBuildPathWith[i];
				final Point botPosition = botPositions[robot];
				if (isBotMoveable[robot]) {
					Map<Integer, DepthPointer> robotDepthMap;
					if (routeInfo.botsDepthMap[robot] != null) {
						robotDepthMap = routeInfo.botsDepthMap[robot];
						DepthPointer lowestPoint = robotDepthMap.get(Hasher.hashPoint(botPositions[robot]));
						if (lowestPoint == null ||
							lowestPoint.depth != 0) {
							robotDepthMap = null;
							robotDepthMap = RobotPath.getDepthGraph(routeInfo.board, botPosition.x, botPosition.y, goal.x, goal.y, true);
						}
					} else {
						robotDepthMap = RobotPath.getDepthGraph(routeInfo.board, botPosition.x, botPosition.y, goal.x, goal.y, true);
					}
					routeInfo.botsDepthMap[robot] = robotDepthMap;
					final int hash = Hasher.hashPoint(goal.x, goal.y);
					if (robotDepthMap.containsKey(hash)) {
						//ignoring robot collisions for now
						DepthPointer goalPoint = robotDepthMap.get(hash);
						robotIndexForRobotsThatCanReachFiller[i] = robot;
						robotIndexLength[i] = goalPoint.depth;
						robotIndexDepthPointer[i] = goalPoint;
					} else {
						if (previousRobotFiller != -1) {
							DepthPointer goalPoint = RobotPath.getInterceptingDepthPointer(routeInfo.board, 
																						   robotDepthMap, 
																						   botPositions[previousRobotFiller].x, 
																						   botPositions[previousRobotFiller].y, 
																						   robotBuildDirection.getOppositeDirection());
							if (goalPoint != null) {
								robotIndexForRobotsThatCanReachFiller[i] = robot;
								robotIndexLength[i] = goalPoint.depth;
								robotIndexDepthPointer[i] = goalPoint;
							}
						}
//						DepthPointer goalPoint = RobotPath.getInterceptingDepthPointer(routeInfo.board, robotDepthMap, goal.x, goal.y, changedDirection);
//						if (goalPoint != null) {
//							robotIndexForRobotsThatCanReachFiller[i] = robot;
//							robotIndexLength[i] = goalPoint.depth;
//						}
					}
				}
			}
			if (robotIndexForRobotsThatCanReachFiller[0] == -1) {
				return false;
			} else {
				int bestRobot = robotIndexForRobotsThatCanReachFiller[0];
				int bestRobotLength = robotIndexLength[0];
				int index = 1;
				while (index < robotIndexForRobotsThatCanReachFiller.length &&
					   robotIndexForRobotsThatCanReachFiller[index] != -1) {
					if (robotIndexLength[index] < bestRobotLength) {
						bestRobot = robotIndexForRobotsThatCanReachFiller[index];
						bestRobotLength = robotIndexLength[index];
					}
					index++;
				}
				robotsPath[robotFillerIndex] = bestRobot;
				robotFillerIndex++;
				
				isBotMoveable[bestRobot] = false;
				if (previousRobotFiller != -1) {
					isBotMoveable[previousRobotFiller] = true;
				}
				previousRobotFiller = bestRobot;
				
				Map<Integer, DepthPointer> depthMap = RobotPath.getDepthGraph(routeInfo.board, botPositions[bestRobot].x, botPositions[bestRobot].y, goal.x, goal.y, true);
				BotPathDirection[] routePart = null;
				if (depthMap.containsKey(Hasher.hashPoint(goal))) {
					routePart = RobotPath.getRobotPathToTarget(routeInfo.board, botPositions[bestRobot].x, botPositions[bestRobot].y, goal.x, goal.y, bestRobot);
				} else {
					//intercept finder point might have to be the old point
					//badly optimized. can use a boolean value when valid route is found for the bot above or even use the depth pointer
//					DepthPointer interceptPoint = RobotPath.getInterceptingDepthPointer(routeInfo.board, depthMap, goal.x, goal.y, changedDirection);
//					if (interceptPoint == null) {
//						interceptPoint = RobotPath.getInterceptingDepthPointer(routeInfo.board, 
//																			   depthMap, 
//																			   botPositions[previousRobotFiller].x, 
//																			   botPositions[previousRobotFiller].y, 
//																			   robotBuildDirection.getOppositeDirection());
//						
//					}
					DepthPointer interceptPoint = robotIndexDepthPointer[index - 1];
					routePart = RobotPath.getRobotPathToTargetFromDepthPoint(interceptPoint, bestRobot);
				}
				for (int j = 0; j < routePart.length; j++) {
					thisFillersRoute.add(routePart[j]);
				}
				
				botPositions[bestRobot] = goal;
			}
		}
		
		for (int i = 0; i < robotsPath.length; i++) {
			final int robot = robotsPath[i];
			isBotMoveable[robot] = true;
		}
		DepthPointer higestPoint = RobotPath.getInterceptingDepthPointer(routeInfo.board, routeInfo.botsDepthMap[0], start.x, start.y, changedDirection);
		if (higestPoint == null) {
			return false;
		}
		BotPathDirection[] routePart = RobotPath.getRobotPathToTargetFromDepthPoint(higestPoint, 0);
		routeInfo.route.addAll(thisFillersRoute);
		for (int j = 0; j < routePart.length; j++) {
			routeInfo.route.add(routePart[j]);
		}
		routeInfo.botPositions = botPositions;
		if (robotOnEnd) {
			routeInfo.botPositions[0] = new Point(start.x + changedDirection.translationX, start.y + changedDirection.translationY);
		} else {
			routeInfo.botPositions[0] = new Point(start.x, start.y);
		}
		routeInfo.botsDepthMap[0] = RobotPath.getDepthGraph(routeInfo.board, routeInfo.botPositions[0].x, routeInfo.botPositions[0].y, routeInfo.board.goal.x, routeInfo.board.goal.y, false);
		for (int i = 0; i < routeInfo.isBotMoveable.length; i++) {
			routeInfo.isBotMoveable[i] = isBotMoveable[i];
		}
		
		
		return true;
	}
	
	private Directions getDirectionFromTranslation(int xT, int yT)
	{
		if (xT == 0 && yT < 0) {
			return Directions.Up;
		} else if (xT == 0 && yT > 0) {
			return Directions.Down;
		} else if (xT < 0 && yT == 0) {
			return Directions.Left;
		} else {
			return Directions.Right;
		}
	}
	
	public void printFiller(Board board)
	{
		final char[][] boardCopy = board.copyBoard();
		putFillerOnBoard(boardCopy);
		System.out.println(Board.makeBoard(boardCopy));
	}
	
	public void putFillerOnBoard(char[][] board)
	{
		Directions fillerDirection = getDirectionFromTranslation(start.x - end.x, start.y - end.y);
		int currentX = end.x;
		int currentY = end.y;
		for (int i = 0; i < requiredRobots; i++) {
			board[currentX][currentY] = '@';
			currentX += fillerDirection.translationX;
			currentY += fillerDirection.translationY;
		}
	}
	
	@Override
	public int compareTo(RobotFiller o) {
		return requiredRobots - o.requiredRobots;
	}
}
