package Solver.SolverRobotPathParts;

import java.awt.Point;
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
		final int numberOfRobotsRequired = Math.abs(end.x - start.x) + Math.abs(end.y - start.y) + ((robotOnEnd)? 1 : 0);
		final Point[] fillerPoints = new Point[numberOfRobotsRequired];
		final int[] robotsPath = new int[numberOfRobotsRequired];
		int robotFillerIndex = 0;
		
		int xTranslation = start.x - end.x;
		int yTranslation = start.y - end.y;
		if (numberOfRobotsRequired > 1) {
			if (xTranslation < 0) {
				xTranslation = -1;
			} else if (xTranslation > 0) {
				xTranslation = 1;
			}
			if (yTranslation < 0) {
				yTranslation = -1;
			} else if (yTranslation > 0) {
				yTranslation = 1;
			}
		}
		
		
		int currentPosX = end.x;
		int currentPosY = end.y;
		for (int i = 0; i < fillerPoints.length; i++) {
			fillerPoints[i] = new Point(currentPosX, currentPosY);
			currentPosX += xTranslation;
			currentPosY += yTranslation;
		}
		Directions robotBuildDirection = getDirectionFromTranslation(xTranslation, yTranslation);
		int previousRobotFiller = -1;
		for (int q = 0; q < fillerPoints.length; q++) {
			Point goal =  fillerPoints[q];
			final int[] robotIndexForRobotsThatCanReachFiller = new int[containerOfThisFiller.robotsToBuildPathWith.length];
			final int[] robotIndexLength = new int[robotIndexForRobotsThatCanReachFiller.length];
			for (int i = 0; i < robotIndexLength.length; i++) {
				robotIndexForRobotsThatCanReachFiller[i] = -1;
			}
			for (int i = 0; i < containerOfThisFiller.robotsToBuildPathWith.length; i++) {
				final int robot = containerOfThisFiller.robotsToBuildPathWith[i];
				final Point botPosition = routeInfo.botPositions[robot];
				if (routeInfo.isBotMoveable[robot]) {
					Map<Integer, DepthPointer> robotDepthMap;
					if (routeInfo.board.robotDepthMaps[robot] != null) {
						robotDepthMap = routeInfo.board.robotDepthMaps[robot];
					} else {
						robotDepthMap = RobotPath.getDepthGraph(routeInfo.board, botPosition.x, botPosition.y, goal.x, goal.y, true);
					}
					routeInfo.board.robotDepthMaps[robot] = robotDepthMap;
					final int hash = Hasher.hashPoint(goal.x, goal.y);
					if (robotDepthMap.containsKey(hash)) {
						//ignoring robot collisions for now
						DepthPointer goalPoint = robotDepthMap.get(hash);
						robotIndexForRobotsThatCanReachFiller[i] = robot;
						robotIndexLength[i] = goalPoint.depth;
					} else {
						DepthPointer goalPoint = RobotPath.getInterceptingDepthPointer(routeInfo.board, robotDepthMap, goal.x, goal.y, robotBuildDirection);
						if (goalPoint != null) {
							robotIndexForRobotsThatCanReachFiller[i] = robot;
							robotIndexLength[i] = goalPoint.depth;
						}
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
						index++;
					}
				}
				robotsPath[robotFillerIndex] = bestRobot;
				robotFillerIndex++;
				
				routeInfo.isBotMoveable[bestRobot] = false;
				if (previousRobotFiller != -1) {
					routeInfo.isBotMoveable[bestRobot] = true;
				}
				previousRobotFiller = bestRobot;
				BotPathDirection[] routePart = RobotPath.getRobotPathToTarget(routeInfo.board, routeInfo.botPositions[bestRobot].x, routeInfo.botPositions[bestRobot].y, goal.x, goal.y, bestRobot);
				for (int j = 0; j < routePart.length; j++) {
					routeInfo.route.add(routePart[j]);
				}
				
				routeInfo.botPositions[bestRobot] = goal;
			}
		}
		for (int i = 0; i < robotsPath.length; i++) {
			final int robot = robotsPath[i];
			routeInfo.isBotMoveable[robot] = true;
		}
		
		DepthPointer higestPoint = RobotPath.getInterceptingDepthPointer(routeInfo.board, routeInfo.board.robotDepthMaps[0], start.x, start.y, changedDirection);
		BotPathDirection[] routePart = RobotPath.getRobotPathToTargetFromDepthPoint(higestPoint, 0);
		for (int j = 0; j < routePart.length; j++) {
			routeInfo.route.add(routePart[j]);
		}
		routeInfo.board.robotDepthMaps[0] = RobotPath.getDepthGraph(routeInfo.board, start.x, start.y, routeInfo.board.goal.x, routeInfo.board.goal.y, false);
		routeInfo.botPositions[0] = new Point(start.x + changedDirection.translationX, start.y + changedDirection.translationY);
		
		return true;
	}
	
	private Directions getDirectionFromTranslation(int xT, int yT)
	{
		if (xT == 0 && yT == -1) {
			return Directions.Up;
		} else if (xT == 0 && yT == 1) {
			return Directions.Down;
		} else if (xT == -1 && yT == 0) {
			return Directions.Left;
		} else {
			return Directions.Right;
		}
	}
	
	public void printFiller(Board board)
	{
		char[][] boardNew = new char[board.size][board.size];
		for (int i = 0; i < boardNew.length; i++) {
			
		}
	}
	
	@Override
	public int compareTo(RobotFiller o) {
		return requiredRobots - o.requiredRobots;
	}
}
