package movieApp;

import java.util.HashMap;
import java.util.Map;

public class User {
	
	private Integer id;
	private double averageRating;
	private double variance;


	// the first integer is the movieId and the second is the rating that the user gave
	HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
	
	public User( int id ) {
		this.id = id;
	}
	public void addRating( Integer movieId, Integer rating) {
		map.put(movieId, rating);
	}
	
	/*
	 * TODO call this method once and only once at the appropriate time
	 */
	
	public void makeAverageRating() {
		double av = 0;
		for (Integer rating : map.values()) {
			av += rating;
		}
		averageRating = av / map.size();
	}
	
	public double getAverageRating() {
		return this.averageRating;
	}
	
	public int getUserId() {
		return this.id;
	}
	
	public double getNumRatings() {
		// TODO Auto-generated method stub
		return (double) map.size();
	}
	public void makeVariance() {
		
		double sum = 0;
		
		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			double rating = (entry.getValue() - averageRating) * (entry.getValue() - averageRating);
			sum += rating;
		}
		
		variance = Math.sqrt(sum / map.size());
		
	}
	public double getVariance() {
		// TODO Auto-generated method stub
		return this.variance;
	}

}
