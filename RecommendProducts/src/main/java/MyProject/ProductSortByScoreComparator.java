package MyProject;
import java.util.Comparator;

public class ProductSortByScoreComparator implements Comparator<Product> {

	/**
	 * This is a comparator which is used to sort Recommended Product items based on their score.
	 */
	public int compare(Product o1, Product o2) {
		// TODO Auto-generated method stub
		return o1.getScore() > o2.getScore()  ? -1: (o1.getScore() < o2.getScore() ) ? 1:0 ;
	}

}
