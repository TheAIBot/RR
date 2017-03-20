package Tests;

import org.junit.Before;
import org.junit.Test;

public class KonkurrenceRandomMapTests {
	
	@Before
	public void limitTestTime()
	{
		TestTools.timePerMap = 5;
	}
	
	@Test
	public void Random1_005()
	{
		TestTools.testMap("konkurrence_random\\1-005-Random.in", new String[] {
				"0L",
				"0D"
			});
	}
	
	@Test
	public void Random1_200()
	{
		TestTools.testMap("konkurrence_random\\1-200-Random.in", new String[] {
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
			});
	}
	
	@Test
	public void Random2_05()
	{
		TestTools.testMap("konkurrence_random\\2-05-Random.in", new String[] {
				"1R",
				"0U"
			});
	}
	
	@Test
	public void Random2_80()
	{
		TestTools.testMap("konkurrence_random\\2-80-Random.in", new String[] {
				"1U",
				"1L",
				"1U",
				"1L",
				"1U",
				"1R",
				"0U",
				"0L",
				"0U",
				"0L",
				"0U",
				"0R",
				"0D"
			});
	}
	
	@Test
	public void Random3_07()
	{
		TestTools.testMap("konkurrence_random\\3-07-Random.in", new String[] {
				"1L",
				"0L",
				"0U"
			});
	}
	
	@Test
	public void Random3_10()
	{
		TestTools.testMap("konkurrence_random\\3-10-Random.in", new String[] {
				"2U",
				"0L"
			});
	}
	
	@Test
	public void Random4_012()
	{
		TestTools.testMap("konkurrence_random\\4-012-Random.in", new String[] {
				"1D",
				"0D",
				"0R",
				"0U"
			});
	}
	
	@Test
	public void Random4_025()
	{
		TestTools.testMap("konkurrence_random\\4-025-Random.in", new String[] {
				"1R",
				"0U"
			});
	}
	
	@Test
	public void Random4_100()
	{
		TestTools.testMap("konkurrence_random\\4-100-Random.in", new String[] {
				"0L",
				"2R",
				"2D",
				"0R"
			});
	}
	
	@Test
	public void Random8_009()
	{
		TestTools.testMap("konkurrence_random\\8-009-Random.in", new String[] {
				"1R",
				"0U"
			});
	}
	
	@Test
	public void Random8_020()
	{
		TestTools.testMap("konkurrence_random\\8-020-Random.in", new String[] {
				"1R",
				"0U"
			});
	}
	
	@Test
	public void Random8_045()
	{
		TestTools.testMap("konkurrence_random\\8-045-Random.in", new String[] {
				"6D",
				"0D",
				"0R",
				"0D",
				"0L"
			});
	}
	
	@Test
	public void Random8_100()
	{
		TestTools.testMap("konkurrence_random\\8-100-Random.in", new String[] {
				"4D",
				"4L",
				"4U",
				"0D",
				"0R",
				"0D",
				"0R",
				"0U",
				"0R"
			});
	}
	
	@Test
	public void Randomchallenge()
	{
		TestTools.testMap("konkurrence_random\\challenge.in", new String[] {
				"2U",
				"2L",
				"2D",
				"2R",
				"2U",
				"0L"
			});
	}
}
