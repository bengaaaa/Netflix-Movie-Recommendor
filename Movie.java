package movieApp;

import java.util.HashMap;

public class Movie {
	
	private Integer movieId;
	
	HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
	
	public Movie(int id) {
		this.movieId = id;
	}
	
	public void addRating( Integer userId, Integer rating) {
		map.put(userId, rating);
	}

	public int getMovieId() {
		// TODO Auto-generated method stub
		return this.movieId;
	}
	

}
