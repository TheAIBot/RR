package Tests;

import static org.junit.Assert.*;

import Solver.Board;
import Solver.BotPathDirection;
import Solver.Loader;
import Solver.RobotPath;

public class TestTools
{
	public static int timePerMap = 5000;

	public static void testMap(String fileName, String[] expectedRoute)
	{
		Board board = Loader.loadBoard("tests\\" + fileName);
		BotPathDirection[] directions = RobotPath.getRobotPath(board, timePerMap);

		assertNotNull("Directions is null", directions);

		for (BotPathDirection botPathDirection : directions)
		{
			System.out.println(botPathDirection.toString());
		}

		assertTrue(directions.length > 0);
		assertFalse("Found shorter route", expectedRoute.length > directions.length);

		for (int i = 0; i < expectedRoute.length; i++)
		{
			assertEquals(directions[i].toString(), expectedRoute[i]);
		}
	}
}
