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
		TestTools.testMap("obligatorisk_synlig\\Test1.in", new String[] {
				"0L",
				"0D"
			});
	}
	
	@Test
	public void test2()
	{
		TestTools.testMap("obligatorisk_synlig\\Test2.in", new String[] {
				"0R",
				"0D",
				"0L",
				"0U",
				"0R"
			});
	}
	
	@Test
	public void test3()
	{
		TestTools.testMap("obligatorisk_synlig\\Test3.in", new String[] {
				"0U",
				"0R",
				"0D",
				"0L",
				"0U"
			});
	}
	
	@Test
	public void test4()
	{
		TestTools.testMap("obligatorisk_synlig\\Test4.in", new String[] {
				"0U",
				"0L",
				"0D",
				"0R",
				"0D",
				"0L",
				"0D",
				"0R",
				"0D"
			});
	}
	
	@Test
	public void test5()
	{
		TestTools.testMap("obligatorisk_synlig\\Test5.in", new String[] {
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
			});
	}
}
