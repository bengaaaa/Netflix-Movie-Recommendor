package movieApp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class MovieRecommendor {

	HashMap<Integer, User> users = new HashMap<Integer, User>();
	
	HashMap<Integer, Movie> movies = new HashMap<Integer, Movie>();

	public static void main(String[] args) {
		new MovieRecommendor().run();
	}

	private void run() {

		getData();
		makeAverages();
		makeVariances();
	//	printAverages();
		User u = users.get(1395430);
		User v = users.get(1296163);
		User j = users.get(1395430);
		Movie m = movies.get(2);
		Movie n = movies.get(87);
//		System.out.println(findPcc(u, v));
//		double rhat = rHat(u, m, 50);
//		System.out.println(rhat);

		double rmse = rMSE(u, 50);
		System.out.println("The rmse for user 1395430 " + "is: " + rmse);
		
		double moviermse = rMSEm(m, 50);
		System.out.println("The rmse for movie 2 is: " + moviermse);
		
		double rmse2 = rMSE(v, 50);
		System.out.println("The rmse for user 1296163 " + "is: " + rmse2);
//		System.out.println(rHat( v, m, 50));
		double moviermse2 = rMSEm(n, 50);
		System.out.println("The rmse for movie 87 is: " + moviermse2);
		
//		double rmse3 = rMSE(j, 50);
//		System.out.println(rmse3);
		
	}

	private double rMSEm(Movie m, int i) {
		// TODO Auto-generated method stub
		double sum = 0;
		for (Map.Entry<Integer, Integer> entry : m.map.entrySet()) {
			int userId = entry.getKey();
			int rating = entry.getValue();
			sum += Math.pow(rHat(users.get(userId), m, i) - rating, 2);
//			System.out.println(rHat(users.get(userId), m, i) - rating);
		}
		return Math.sqrt(sum / m.map.size());
		
	}

	private double rMSE(User u, int k) {
		// TODO Auto-generated method stub
		double sum = 0;
		for (Map.Entry<Integer, Integer> entry : u.map.entrySet()) {
			int movieId = entry.getKey();
			int rating = entry.getValue();
			sum += Math.pow(rHat(u, movies.get(movieId), k) - rating, 2);
//			System.out.println(rHat(u, movies.get(movieId), k) - rating);
		}
		
		return Math.sqrt(sum / u.map.size());
	}

	private double rHat(User u, Movie m, int k) {
		// k is the number of neighbors we wnat to search for
		ArrayList<UserPcc> neighbors = new ArrayList<UserPcc>();
		
		int movieId = m.getMovieId();
		
		for (Map.Entry<Integer, Integer> entry : m.map.entrySet()) {
			int userId = entry.getKey();
			User v = users.get(userId);
			UserPcc up = new UserPcc(userId, findPcc(u, v));
			neighbors.add(up);
		}
		
		// sort the list!
//		Collections.sort(neighbors, new UComparator());
		neighbors.sort( new UComparator());
		
		// code to test if the list is sorted
//		for (int i = 0; i < k && i < neighbors.size(); i++) {
//			int userjId = neighbors.get(i).userId;
//			double pcc = neighbors.get(i).userPcc;
//			System.out.println("User Id: " + userjId + "User pcc:" + pcc);
//		}
		
		

		double numerator = 0;
		double denominator = 0;
		
		for (int i = 0; i < k && i < neighbors.size(); i++) {
			int userjId = neighbors.get(i).userId;
			User userj = users.get(userjId);
			double pcc = neighbors.get(i).userPcc;
			numerator += pcc * (userj.map.get(movieId) - userj.getAverageRating());
			denominator += Math.abs(pcc);
			if (denominator == 0) {
				return 0;
			}
			
		}
		double rhat = numerator / denominator + u.getAverageRating();

		
		return rhat;
	}

	private void makeVariances() {
		// TODO Auto-generated method stub
		users.forEach((k, v) -> {
			v.makeVariance();
		});
	}

	private double findPcc(User u, User v) {
		
		// this function creates a hashmap which shows the correlation
		// that the inputed user has to each other user
		// the key is the user id which points to the pcc
		
		double var1 = u.getVariance();
		double var2 = v.getVariance();
		double E = bigE(u, v);
		if (var1 ==0 || var2 == 0) {
			return 0;
		}
		return (E / (var1 * var2));
		
		
	}

	private double bigE(User u, User v) {
		
		int numCommon = 0;
		double sum = 0;
		for (Map.Entry<Integer, Integer> entry : u.map.entrySet()) {
			int key = entry.getKey();
			if (v.map.containsKey(key)) {
				numCommon+=1;
				sum += ((entry.getValue() - u.getAverageRating()) * (v.map.get(key) - v.getAverageRating()));	
			}
		}
		
		if (numCommon == 0) {
			return 0;
		}
		
		return sum / numCommon;
		
	}

	@SuppressWarnings("unused")
	private void printAverages() {
		users.forEach((k, v) -> {
			System.out.println(v.getAverageRating());
		});
	}

	private void makeAverages() {
		
		users.forEach((k, v) -> {
			v.makeAverageRating();
		});
		
	}

	public void getData() {
		try {
			File folder = new File("moviedata");

			Filter filter = new Filter();

			File[] files = folder.listFiles(filter);

			for (int i = 0; i < files.length; i++) {
				String fileName = files[i].getName();
//				System.out.println(fileName);
				String movieName = fileName.substring(3, 10);
				int movieId = Integer.parseInt(movieName);
//				System.out.println(movieId);
				if (!movies.containsKey(movieId)) {
					Movie newMovie = new Movie(movieId);
				}

				Scanner scanner = new Scanner(files[i]);
				while (scanner.hasNextLine()) {
					String data = scanner.nextLine();
					if (data.contains(",")){
						String[] split = data.split(",", 0);
						int userNum = Integer.parseInt(split[0]);
						int rating = Integer.parseInt(split[1]);

						//						System.out.println(userNum);
						//						System.out.println(rating);
						//						scanner.close();

						if (users.containsKey(userNum) ) {
							users.get(userNum).addRating(movieId, rating);

						} 
						else {
							User newUser = new User(userNum);
							newUser.addRating(movieId, rating);
							users.put(userNum, newUser);
						}
						if (movies.containsKey(movieId)) {
							movies.get(movieId).addRating(userNum, rating);
						} else {
							Movie newMovie = new Movie(movieId);
							newMovie.addRating(userNum, rating);
							movies.put(movieId, newMovie);
						}
						
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}

	}
}