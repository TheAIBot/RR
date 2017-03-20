package Tests;

import org.junit.Before;
import org.junit.Test;

public class TestMaps
{

	@Before
	public void limitTestTime()
	{
		TestTools.timePerMap = 5;
	}

	@Test
	public void test1_2()
	{
		TestTools.testMap("test_maps\\Test1-2.in", new String[]
		{
				"1D", "0L", "0D"
		});
	}

	@Test
	public void test1_3()
	{
		TestTools.testMap("test_maps\\Test1-3.in", new String[]
		{
				"1D", "2D", "0L", "0D"
		});
	}

	@Test
	public void test2()
	{
		TestTools.testMap("test_maps\\Test2.in", new String[]
		{
				"1L", "1U", "1R", "0R", "0D", "0L", "0U", "0R"
		});
	}

	@Test
	public void test3()
	{
		TestTools.testMap("test_maps\\Test3.in", new String[]
		{
				"0L", "0D"
		});
	}

	@Test
	public void test4()
	{
		TestTools.testMap("test_maps\\Test4.in", new String[]
		{
				"0L", "0D"
		});
	}

	@Test
	public void test5()
	{
		TestTools.testMap("test_maps\\Test5.in", new String[]
		{
				"0L", "0D"
		});
	}
	
	@Test
	public void test6_3()
	{
		TestTools.testMap("test_maps\\Test6-3.in", new String[]
		{
				"1D", "1R", "0R"
		});
	}
}
