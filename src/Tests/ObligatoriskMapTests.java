package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Solver.Board;
import Solver.BotPathDirection;
import Solver.Loader;
import Solver.RobotPath;

public class ObligatoriskMapTests {

	@Test
	public void test1()
	{
		Board board = Loader.loadBoard("obligatorisk_synlig\\Test1.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"0L",
			"0D"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
	
	@Test
	public void test2()
	{
		Board board = Loader.loadBoard("obligatorisk_synlig\\Test2.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"0R",
			"0D",
			"0L",
			"0U",
			"0R"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
	
	@Test
	public void test3()
	{
		Board board = Loader.loadBoard("obligatorisk_synlig\\Test3.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"0U",
			"0R",
			"0D",
			"0L",
			"0U"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
	
	@Test
	public void test4()
	{
		Board board = Loader.loadBoard("obligatorisk_synlig\\Test4.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"0U",
			"0L",
			"0D",
			"0R",
			"0D",
			"0L",
			"0D",
			"0R",
			"0D"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
	
	@Test
	public void test5()
	{
		Board board = Loader.loadBoard("obligatorisk_synlig\\Test5.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"0U",
			"0L",
			"0D",
			"0R",
			"0U",
			"0R",
			"0D",
			"0R",
			"0D",
			"0R"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
}
