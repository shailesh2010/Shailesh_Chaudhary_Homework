package MyProject;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

public class URLs {

	private static String searchURL = "http://api.walmartlabs.com/v1/search?apiKey=" + ProjectConfiguration.apiKey;
	private static String recommendURL = "http://api.walmartlabs.com/v1/nbp?apiKey="+ ProjectConfiguration.apiKey;
	private static  String reviewURL = "http://api.walmartlabs.com/v1/reviews/";
	
	/**
	 * This method returns a url for an item that is used to get reviews for that item.
	 * @param itemId ItemId
	 * @return URL for Reviews retrieval for an item
	 */
	public static String getReviewURL(String itemId)
	{
		try
		{
			itemId = getEncodedData(itemId);
		}
		catch(Exception e)
		{
			itemId="";
			System.out.println(e.getMessage());
		}
		return reviewURL + itemId + "?apiKey=" + ProjectConfiguration.apiKey ;
	}
	
	/**
	 * This method returns a url for an item that is used to get recommendations for that item.
	 * @param itemId ItemId
	 * @return URL for Recommendations for an item
	 */
	public static String getrecommendURL(String itemId)
	{
		try
		{
			itemId = getEncodedData(itemId);
		}
		catch(Exception e)
		{
			itemId="";
			System.out.println(e.getMessage());
		}
		return recommendURL+ "&itemId=" + itemId;
	}
	
	/**
	 * This method searches items for the user input
	 * @param searchString Item to be searched
	 * @return URL of the item to be searched
	 */
	public static String getsearchURL(String searchString)
	{
		try
		{
			searchString = getEncodedData(searchString);
		}
		catch(Exception e)
		{
			searchString="";
			System.out.println(e.getMessage());
		}
		return searchURL + "&query=" + searchString;
	}
	
	
	
	/**
	 * This method encodes bad user input
	 * @param data data to be encoded
	 * @return encoded data
	 * @throws UnsupportedEncodingException
	 */
	public static String getEncodedData(String data) throws UnsupportedEncodingException
	{
		String result = "";
		try
		{
			result = URLEncoder.encode(data,"UTF-8");
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		System.out.println("Encoded data is " + result);
		return result;
		
	}

}
