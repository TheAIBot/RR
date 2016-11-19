package Tests;

import org.junit.Test;

public class SimpleTests {
	
	@Test
	public void test_up()
	{
		TestTools.testMap("simple\\Test-up.in", new String[] {
				"0U"
			});
	}
	
	@Test
	public void test_down()
	{
		TestTools.testMap("simple\\Test-down.in", new String[] {
				"0D"
			});
	}
	
	@Test
	public void test_left()
	{
		TestTools.testMap("simple\\Test-left.in", new String[] {
				"0L"
			});
	}
	
	@Test
	public void test_right()
	{
		TestTools.testMap("simple\\Test-right.in", new String[] {
				"0R"
			});
	}
	
	@Test
	public void test2_up()
	{
		TestTools.testMap("simple\\Test_2-up.in", new String[] {
				"0U"
			});
	}
	
	@Test
	public void test2_down()
	{
		TestTools.testMap("simple\\Test_2-down.in", new String[] {
				"0D"
			});
	}
	
	@Test
	public void test2_left()
	{
		TestTools.testMap("simple\\Test_2-left.in", new String[] {
				"0L"
			});
	}
	
	@Test
	public void test2_right()
	{
		TestTools.testMap("simple\\Test_2-right.in", new String[] {
				"0R"
			});
	}
	
	@Test
	public void test3_up()
	{
		TestTools.testMap("simple\\Test_3-up.in", new String[] {
				"0U"
			});
	}
	
	@Test
	public void test3_down()
	{
		TestTools.testMap("simple\\Test_3-down.in", new String[] {
				"0D"
			});
	}
	
	@Test
	public void test3_left()
	{
		TestTools.testMap("simple\\Test_3-left.in", new String[] {
				"0L"
			});
	}
	
	@Test
	public void test3_right()
	{
		TestTools.testMap("simple\\Test_3-right.in", new String[] {
				"0R"
			});
	}
	
}
