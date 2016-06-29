package MyProject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.Assert;

public class WalmartAPITest {
	static WalmartAPI walmartAPI = null;
	
	@BeforeClass
	public static void beforeClass()
	{
		walmartAPI = new WalmartAPI();
	}
	
	@Test
	public void urlValidInputTest()
	{
		String userUrl = URLs.getsearchURL("boat shoes");
		String result = walmartAPI.getDataFromUrl(userUrl);
		JsonObject obj = new JsonParser().parse(result).getAsJsonObject();
		Assert.assertTrue("Result should be true for boat shoes", obj.get("totalResults").getAsDouble() > 0 && !obj.has("errors"));
	}
	
	@Test
	public void urlInValidInputTest()
	{
		String userUrl = URLs.getsearchURL("jhsjchdgchm");
		String result = walmartAPI.getDataFromUrl(userUrl);
		//System.out.println("Randome sytring get lenth = "+result.length());
		JsonObject obj = new JsonParser().parse(result).getAsJsonObject();
		Assert.assertTrue("Result should have lenght 0 for random string ", obj.get("totalResults").getAsDouble() == 0 && !obj.has("errors"));
	}
	
	
	@Test
	public void urlEmptyInputTest()
	{
		String userUrl = URLs.getsearchURL("");
		String result = walmartAPI.getDataFromUrl(userUrl);
		//System.out.println("Randome sytring get lenth = "+result.length());
		//JsonObject obj = new JsonParser().parse(result).getAsJsonObject();
		Assert.assertTrue("Result should have lenght 0 for empty string ", result.length() == 0);
	}
	
	
	@Test
	public void getItemsValidInputTest()
	{
		String searchString = "ipod";
		JsonArray result = walmartAPI.getItemsForKeyword(searchString);
		Assert.assertNotNull("The ouput for ipod  should not be null ",result);
	}
	
	
	@Test
	public void getItemsInvalidInputTest()
	{
		String searchString = "xyzabc";
		JsonArray result = walmartAPI.getItemsForKeyword(searchString);
		Assert.assertNull("The ouput for random string search should be null ",result);
	}
	
	@Test
	public void getItemsEmptyInvalidInputTest()
	{
		String searchString = "";
		JsonArray result = walmartAPI.getItemsForKeyword(searchString);
		Assert.assertNull("The ouput for empty string search should be null ",result);
	}
	
	
	
	@Test
	public void reviewScoreValidInputTest()
	{
		String validItemId = "10449169";
		float actualOutput = walmartAPI.reviewScore(validItemId);
		float expectedOutput = 0.96000004f;
		Assert.assertTrue(actualOutput == expectedOutput );
	}
	
	
	@Test
	public void reviewScoreInvalidInputTest()
	{
		String validItemId = "123";
		float actualOutput = walmartAPI.reviewScore(validItemId);
		float expectedOutput = 0;
		Assert.assertTrue(actualOutput == expectedOutput );
	}
	
	@Test
	public void reviewScoreEmptyInputTest()
	{
		String validItemId = "";
		float actualOutput = walmartAPI.reviewScore(validItemId);
		float expectedOutput = 0;
		Assert.assertTrue(actualOutput == expectedOutput );
	}

}
