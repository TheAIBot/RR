package Solver;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteBuildingInformation {
	public final List<BotPathDirection> route = new ArrayList<BotPathDirection>();
	public Point[] botPositions;
	public final boolean[] isBotMoveable;
	public final Board board;
	public boolean canFindRoute = true;
	public final Map<Integer, DepthPointer>[] botsDepthMap;
	public boolean hasTopStepBeenRemoved = false;
	
	@SuppressWarnings("unchecked")
	public RouteBuildingInformation(Board board)
	{
		this.board = board;
		this.botPositions = new Point[board.robots.length];
		this.isBotMoveable = new boolean[botPositions.length];
		for (int i = 0; i < botPositions.length; i++) {
			this.botPositions[i] = new Point(board.robots[i]);
			this.isBotMoveable[i] = true;
		}
		this.botsDepthMap = (Map<Integer, DepthPointer>[])new Map[board.robots.length]; // shitty java needs compiler supressing
		for (int i = 0; i < board.robots.length; i++) {
			botsDepthMap[i] = board.robotDepthMaps[i];
		}
	}
}
