package Tests;

import static org.junit.Assert.assertEquals;

import Solver.Board;
import Solver.BotPathDirection;
import Solver.Loader;
import Solver.RobotPath;

public class TestTools {

	public static void testMap(String fileName, String[] expectedRoute)
	{
		Board board = Loader.loadBoard(fileName);
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		//for (BotPathDirection botPathDirection : directions) {
		//	System.out.println(botPathDirection.toString());
		//}
		for (int i = 0; i < expectedRoute.length; i++) {
			assertEquals(directions[i].toString(), expectedRoute[i]);
		}
	}
}
