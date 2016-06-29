package MyProject;

import java.util.List;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Program {
	public void startApplication() {
		Scanner scanner = new Scanner(System.in);
		JsonArray items = null;
		String searchString = "";
		char c = 'Y';
		WalmartAPI walmartAPI = new WalmartAPI();
		
		
		do {
			System.out.println("Enter product to search");
			try
			{
				searchString = scanner.nextLine();
				if(searchString == null || searchString.length() == 0)
				{
					System.out.println("please enter some text");
				}
				else
				{
					//Get items for an user input
					items = walmartAPI.getItemsForKeyword(searchString);
					
					if (items != null && items.size() > 0) 
					{
						//Get first item from items received
						JsonElement element = items.get(0);
						JsonObject firstItem = element.getAsJsonObject();
						if (!firstItem.isJsonNull()) 
						{
							String itemId = "";
							if (firstItem.has("itemId"))
							{
								itemId = firstItem.get("itemId").getAsString();
								System.out.println("first search item is  item id= "+itemId+", name = "+firstItem.get("name"));
								//Get  recommended products(sorted on score in descending order) for the first item 
								List<JsonObject> products = walmartAPI.recommendedProducts(itemId);
								if(products != null && products.size() > 0)
								{
									//print the recommended products
									walmartAPI.jsonObjectListPrint(products);
								}
								else
								{
									System.out.println("No recommendations found");
								}
							}
						}

					}
				}

				
				//Ask users if they want to continue
				System.out.println("Would you like to continue> Press N or n to exit. Press any other key to continue");
				try 
				{
					c = scanner.nextLine().charAt(0);
					if (c == 'N' || c == 'n') 
					{
						return;
					}
				}
				catch (Exception e) 
				{
					c = 'Y';
					// System.out.println("Please select a valid character");
				}

				
			} 
			catch (Exception e) 
			{
				System.out.println(e.getMessage());
			}

		} while ((c != 'N' || c != 'n'));


	}
}