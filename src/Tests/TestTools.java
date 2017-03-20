package Tests;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.Arrays;

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

		assertTrue(directions.length > 0);
		assertFalse("Found shorter route", expectedRoute.length > directions.length);
		
		System.out.println("Expected:");
		System.out.println(Arrays.toString(expectedRoute));
		System.out.println("Found:");
		System.out.println(Arrays.toString(directions));

		for (int i = 0; i < expectedRoute.length; i++)
		{
			assertEquals(directions[i].toString(), expectedRoute[i]);
		}
	}
}
