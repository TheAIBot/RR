package Solver;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class RouteBuildingInformation {
	public final List<BotPathDirection> route = new ArrayList<BotPathDirection>();
	public final Point[] botPositions;
	public final boolean[] isBotMoveable;
	public final Board board;
	public boolean canFindRoute = true;
	
	public RouteBuildingInformation(Board board)
	{
		this.board = board;
		this.botPositions = new Point[board.robots.length];
		this.isBotMoveable = new boolean[botPositions.length];
		for (int i = 0; i < botPositions.length; i++) {
			this.botPositions[i] = new Point(board.robots[i]);
			this.isBotMoveable[i] = true;
		}
	}
}
