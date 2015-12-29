import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Day9 {
	private ArrayList<String> cities;
	private int[][] distance;
	private ArrayList<int[]> permutations;
	
	public Day9() {
		this.cities = new ArrayList<String>();
		this.readInputFile();
		this.permutations = new ArrayList<int[]>();
		this.calculatePermutations();
	}
	
	public void readInputFile() {
		ArrayList<String> input = new ArrayList<String>();
		
		BufferedReader buffReader = null;
		try{
			buffReader = new BufferedReader (new FileReader("day9.txt"));
			String line = buffReader.readLine();
			while(line != null){
				input.add(line);
				this.getCities(line);
				line = buffReader.readLine();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				buffReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// After getting all the cities, get distances
		this.distance = new int[this.cities.size()][this.cities.size()];
		
		// Initialize distances to 0
		for (int i = 0; i < this.cities.size(); i++) {
			for (int j = 0; j < this.cities.size(); j++) {
				this.distance[i][j] = 0;
			}
		}
		
		// Populate distances
		for (int i = 0; i < input.size(); i++) {
			this.getDistance(input.get(i));
		}
	}
	
	public void getCities(String line) {
		String[] lineItems = line.split(" ");
		String sourceCity = lineItems[0];
		String destCity = lineItems[2];
		
		if (!this.cities.contains(sourceCity)) {
			this.cities.add(sourceCity);
		}
		
		if (!this.cities.contains(destCity)) {
			this.cities.add(destCity);
		}
	}
	
	public void getDistance(String line) {
		String[] lineItems = line.split(" ");
		String sourceCity = lineItems[0];
		String destCity = lineItems[2];
		int distance = Integer.parseInt(lineItems[4]);
		
		int sourceIndex = this.cities.indexOf(sourceCity);
		int destIndex = this.cities.indexOf(destCity);
		
		this.distance[sourceIndex][destIndex] = distance;
		this.distance[destIndex][sourceIndex] = distance;
	}
	
	public void calculatePermutations() {
		// Create an array of city indices
		int[] values = new int[this.cities.size()];
		
		for (int i = 0; i < this.cities.size(); i++) {
			values[i] = i;
		}
		
		// Get all permutations of the array
		this.permutations.clear();
		this.heapsAlgorithm(values, this.cities.size());
	}
	
	public void heapsAlgorithm(int[] list, int n) {
		if (n == 1) {
			int[] arrayCopy = new int[list.length];
			for (int i = 0; i < list.length; i++) {
				arrayCopy[i] = list[i];
			}
			permutations.add(arrayCopy);
		}
		else {
			for (int i = 0; i < n; i++) {
				heapsAlgorithm(list, n-1);
				
				if (n % 2 == 0) {
					swap(list, i, n-1);
				}
				else {
					swap(list, 0, n-1);
				}
			}
			
		}
	}
	
	private static void swap(int[] v, int i, int j) {
		int temp = v[i];
		v[i] = v[j];
		v[j] = temp;
	}
	
	public void findShortestTour() {
		int shortestTourDistance = Integer.MAX_VALUE;
		int longestTourDistance = Integer.MIN_VALUE;
		
		int indexOfShortestTourPlan = -1;
		int indexOfLongestTourPlan = -1;
		
		for (int i = 0; i < this.permutations.size(); i++) {
			// Get the current permutation
			int[] currentTourPlan = this.permutations.get(i);
			int currentDistance = 0;
			
			for (int j = 0; j < currentTourPlan.length - 1; j++) {
				int startIndex = currentTourPlan[j];
				int nextIndex = currentTourPlan[j+1];
				currentDistance += this.distance[startIndex][nextIndex];
			}
			
			// Check for shortest distance
			if (currentDistance < shortestTourDistance) {
				shortestTourDistance = currentDistance;
				indexOfShortestTourPlan = i;
			}
			
			// Check for longest distance
			if (currentDistance > longestTourDistance) {
				longestTourDistance = currentDistance;
				indexOfLongestTourPlan = i;
			}
		}
		
		// Print shortest tour
		System.out.println(shortestTourDistance);
		int[] shortestTour = this.permutations.get(indexOfShortestTourPlan);
		
		for (int i = 0; i < shortestTour.length; i++) {
			System.out.print(this.cities.get(shortestTour[i]) + " ");
		}
		
		// Print longest tour
		System.out.println("\n" + longestTourDistance);
		int[] longestTour = this.permutations.get(indexOfLongestTourPlan);
		
		for (int i = 0; i < longestTour.length; i++) {
			System.out.print(this.cities.get(longestTour[i]) + " ");
		}
	}
	
	public static void main(String[] args) {
		Day9 puzzle = new Day9();
		puzzle.findShortestTour();
	}

}
