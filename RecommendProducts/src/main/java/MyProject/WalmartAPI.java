package MyProject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonElement;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/**
 * @author shaileshchaudhary
 * This class provides functionality to deal with Walmart API to search item, get recommendation for an item 
 * and calculate score for an item based on reviews.
 */
public class WalmartAPI {
   
	/**
	 * This method simply takes a url, calls it and then
	 * returns the received response
	 * @param userUrl The url to be called 
	 * @return Json data received from url is returned in string format 
	 */
	public String getDataFromUrl(String userUrl) {
		StringBuilder sb = new StringBuilder();
		if(userUrl.length() > 0)
		{
			HttpURLConnection conn = null;
			try
			{
				URL url = new URL(userUrl);
				conn = (HttpURLConnection) url.openConnection();

				if (conn.getResponseCode() != 200) 
				{
					System.out.println(conn.getResponseMessage());
					return sb.toString();
					//throw new Exception(conn.getResponseMessage());
				}
				
				BufferedReader rd = null;
				try
				{
					rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String line;
					while ((line = rd.readLine()) != null) {
						sb.append(line);
					}
				}
				catch(Exception e)
				{
					System.out.println(e.getMessage() );
					//System.out.println(" BufferedReader error");
				}
				finally
				{
					rd.close();	
				}
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				//System.out.println(" getDataFromUrl error");
			}
			
			finally
			{
				conn.disconnect();
			}
		}
		
		return sb.toString();
	}

	
	/**
	 * This method takes the item name to be searched and return a list of items corresponding
	 * to that name.
	 * @param searchString Item name to be searched
	 * @return List of items from the search
	 */
	public JsonArray getItemsForKeyword(String searchString) {
		JsonArray items = null;
		
		if (searchString.length() > 0) {
			try {
				String url = URLs.getsearchURL(searchString);
				JsonElement element = new JsonParser().parse(getDataFromUrl(url));
				if(element.isJsonObject())
				{
					JsonObject object = element.getAsJsonObject();
					if(object.has("items"))
					{
						items = object.getAsJsonArray("items");
					}
				}
				
				
				//If no item was found for the entered search text
				if (items == null || items.size() == 0) {
					System.out.println("Your search for " + searchString + " was not found. Please try some other keyword");
					return items;
				}
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
				//System.out.println("getItemsForKeyword error");
			}
		}
		
		return items;
	}
	
	/**
	 * This method retrieves recommendations for an item
	 * and calculate score for each of them and then sort them in descending order 
	 * based on score. I have returned list of JsonObject items. We can also have custom object . 
	 * I have shown implementation of both.
	 * @param itemId ItemId of the item for which recommendations has to be retrieved
	 * @return Sorted Recommendation items list(Items are in JsonObject format. We can also use custom object)
	 */
	public List<JsonObject> recommendedProducts(String itemId) {
		List<JsonObject> p = null;
		try {
			if (itemId != null && itemId.length() > 0) {
				String recommended = getDataFromUrl(URLs.getrecommendURL(itemId));
				JsonElement element = new JsonParser().parse(recommended);
				/**
				 * If the data retrieved is JsonArray then it is correct data.
				 * Else for error, Walmart API returns a JsonObject
				 */
				if (element.isJsonArray()) {
					JsonArray recommendedItems = element.getAsJsonArray();
					if (recommendedItems != null && recommendedItems.size() > 0) {
						List<Product> products = new ArrayList<Product>();
						p = new ArrayList<JsonObject>();
						for (int i = 0; i < recommendedItems.size(); i++) {
							JsonObject item = recommendedItems.get(i).getAsJsonObject();
							if (item.has("itemId") && item.has("name")) {
								// System.out.println("Hello " + i);
								String id = item.get("itemId").getAsString();
								String name = item.get("name").getAsString();
								float score = reviewScore(id);
								item.addProperty("score", score);
								products.add(new Product(id, name, score));
								/*
								 * We can use above or below statement according to requirement
								 * But since the question asks the application to return recommended data to user
								 * I would implement the below statement or Serialize the Product class and implement
								 * above statement.
								 */
								p.add(item);
								// Thread.sleep(3000);
							}

						}
						products.sort(new ProductSortByScoreComparator());
						//print(products);
						//System.out.println("\n \n \n \n\n \nStarting json object comparator list data");
						p.sort(new JsonObjectSortByScoreComparator());
						//jsonObjectListPrint(p);
						
					}
					else {
						System.out.println("Item Id is invalid");
					}

				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			//System.out.println("recommendedProducts method error");
		}

		 return p;
	}
	
	
	/**
	 * This method receives itemid and calculates score for that item and returns the score
	 * @param itemId Retrieve reviews for this item id
	 * @return returns score for this itemid
	 */
	public float reviewScore(String itemId)
	{
		float score= 0;
		try
		{
			/*
			 * I saw that there was reviewStatistics element in Review JsonObject which has 
			 * averageOverallRating and overallRatingRange elements. I used the latter two elements to get score for the item.
			 * We can use third party API also to calculate score based on review text, but that will make the application slow
			 * and highly dependent on response time of third party API for sentiment analysis.
			 */
			if(itemId != null && itemId.length() > 0)
			{
				//System.out.println("recommendation id = "+itemId);
				JsonElement element =  new JsonParser().parse(getDataFromUrl(URLs.getReviewURL(itemId)));
				if(element.isJsonObject())
				{
					JsonObject review = element.getAsJsonObject();
					/*
					 * Some items may not have reviewStatistics element. In those cases
					 * I have returned score as zero
					 */
					if(review.has("reviewStatistics"))
					{
						JsonObject reviewStatistics = review.get("reviewStatistics").getAsJsonObject();
						
						/*
						 * Some items may not have averageOverallRating and overallRatingRange elements. In those cases
						 * I have returned score as zero
						 */
						if(reviewStatistics.has("averageOverallRating") && reviewStatistics.has("overallRatingRange"))
						{
							float average = reviewStatistics.get("averageOverallRating").getAsFloat();
							float overall = reviewStatistics.get("overallRatingRange").getAsFloat();
							score =average/overall;
							//System.out.println(score);
						}
					}
					else
					{
						//System.out.println("score is 0");
					}
				}
				
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("reviewScore error");
		}
		
		
		return score;
	}
	
	/**
	 * This method prints a list of recommended products in  Product object format
	 * @param products Products to be printed:
	 */
	public  void print(List<Product> products)
	{
		for(Product p: products)
		{
			System.out.println("Id= "+p.getId()+ ", Name=  " + p.getName() + ", Score= "+p.getScore());
		}
	}
	
	/**
	 * This method prints a list of recommended products in  JsonObject format
	 * @param products Products to be printed:
	 */
	public void jsonObjectListPrint(List<JsonObject> products)
	{
		System.out.println("Following are the recommendations:");
		for(JsonObject p:products)
		{
			System.out.println("id = "+p.get("itemId").getAsString()+", name= "+p.get("name").getAsString()+", score= "+p.get("score")+", sale price= "+p.get("salePrice").getAsString() );
		}
	}
	
	
	
	
	
	
}
