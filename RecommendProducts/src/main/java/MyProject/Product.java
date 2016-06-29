package MyProject;

import java.util.List;
import java.util.ArrayList;

/**
 * This class is used if we want to create custom object out of the json object received from Walmart API.
 * @author shaileshchaudhary
 *
 */
public class Product {
	private String id;
	private String name;
	private float score;
	
	
	public Product(String id, String name, float score)
	{
		this.id = id;
		this.name = name;
		this.score = score;
	}
	
	public String getId()
	{
		return this.id;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public float getScore()
	{
		return this.score;
	}
	
	
}
