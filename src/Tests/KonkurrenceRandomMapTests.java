package Tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import Solver.Board;
import Solver.BotPathDirection;
import Solver.Loader;
import Solver.RobotPath;

public class KonkurrenceRandomMapTests {
	
	@Test
	public void Random1_005()
	{
		Board board = Loader.loadBoard("konkurrence_random\\1-005-Random.in");
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
	public void Random1_200()
	{
		Board board = Loader.loadBoard("konkurrence_random\\1-200-Random.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"0R",
			"0D",
			"0R",
			"0D",
			"0L",
			"0D",
			"0R",
			"0U",
			"0L",
			"0U",
			"0L",
			"0D",
			"0R",
			"0U"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
	
	@Test
	public void Random2_05()
	{
		Board board = Loader.loadBoard("konkurrence_random\\2-05-Random.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"1R",
			"0U"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
	
	@Test
	public void Random2_80()
	{
		Board board = Loader.loadBoard("konkurrence_random\\2-80-Random.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"1R",
			"0U"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
	
	@Test
	public void Random3_07()
	{
		Board board = Loader.loadBoard("konkurrence_random\\3-07-Random.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"1L",
			"0L",
			"0U"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
	
	@Test
	public void Random3_10()
	{
		Board board = Loader.loadBoard("konkurrence_random\\3-10-Random.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"2U",
			"0L"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
	
	@Test
	public void Random4_012()
	{
		Board board = Loader.loadBoard("konkurrence_random\\4-012-Random.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"1D",
			"0D",
			"0R",
			"0U"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
	
	@Test
	public void Random4_025()
	{
		Board board = Loader.loadBoard("konkurrence_random\\4-025-Random.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"1R",
			"0U"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
	
	@Test
	public void Random4_100()
	{
		Board board = Loader.loadBoard("konkurrence_random\\4-100-Random.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		for (BotPathDirection botPathDirection : directions) {
			System.out.println(botPathDirection.toString());
		}
		String[] expected = new String[] {
			"3R",
			"3U",
			"3R",
			"3U",
			"3L",
			"3D",
			"3L",
			"3D",
			"3R",
			"0U",
			"0L",
			"0U",
			"0L",
			"0U",
			"0R",
			"0D",
			"0L",
			"0D",
			"0R",
			"0U"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
	
	@Test
	public void Random8_009()
	{
		Board board = Loader.loadBoard("konkurrence_random\\8-009-Random.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"1R",
			"0U"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
	
	@Test
	public void Random8_020()
	{
		Board board = Loader.loadBoard("konkurrence_random\\8-020-Random.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"1R",
			"0U"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
	
	@Test
	public void Random8_045()
	{
		Board board = Loader.loadBoard("konkurrence_random\\8-045-Random.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"1R",
			"0U"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
	
	@Test
	public void Random8_100()
	{
		Board board = Loader.loadBoard("konkurrence_random\\8-100-Random.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"4D",
			"4L",
			"4U",
			"0D",
			"0R",
			"0D",
			"0R",
			"0U",
			"0R"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
	
	@Test
	public void Randomchallenge()
	{
		Board board = Loader.loadBoard("konkurrence_random\\challenge.in");
		BotPathDirection[] directions = RobotPath.getRobotPath(board, 5);
		String[] expected = new String[] {
			"2U",
			"2L",
			"2D",
			"2R",
			"2U",
			"0L"
		};
		for (int i = 0; i < expected.length; i++) {
			assertEquals(directions[i].toString(), expected[i]);
		}
	}
}
