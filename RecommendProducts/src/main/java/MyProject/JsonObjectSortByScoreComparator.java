package MyProject;
import java.util.Comparator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class JsonObjectSortByScoreComparator implements Comparator<JsonObject> {

	/**
	 * This is a comparator which is used to sort Recommended JsonObject items based on their score.
	 */
	public int compare(JsonObject o1, JsonObject o2) {
		// TODO Auto-generated method stub
		return o1.get("score").getAsDouble() > o2.get("score").getAsDouble()  ? -1: (o1.get("score").getAsDouble() < o2.get("score").getAsDouble() ) ? 1:0 ;
		}

}
