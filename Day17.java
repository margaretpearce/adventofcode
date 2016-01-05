import java.util.ArrayList;

public class Day17 {

	private ArrayList<Integer> containers;
	private int numCombinations;
	private final int NUM_LITERS = 150;
	
	private ArrayList<ArrayList<Integer>> solutions;
	
	public Day17(String[] input) {
		this.containers = new ArrayList<Integer>();
		
		for (int i = 0; i < input.length; i++) {
			this.containers.add(Integer.parseInt(input[i]));
		}
		
		this.numCombinations = 0;
		
		// Part B: Store solutions
		this.solutions = new ArrayList<ArrayList<Integer>>();
	}
	
	public void findNumberCombinations() {
		ArrayList<Integer> emptyList = new ArrayList<Integer>();
		this.tryCombinations(0, this.containers, emptyList);
		System.out.println(this.numCombinations);
		
		int minSizeSolution = this.findMinNumberContainersInASolution();
		int numSolutionsMinSize = this.countNumSolutionsOfMinSize(minSizeSolution);
		
		System.out.println("There are " + numSolutionsMinSize + " solutions of minimum size " + minSizeSolution);
	}
	
	public void tryCombinations(int currentTotal, ArrayList<Integer> remainingOptions, 
			ArrayList<Integer> usedContainers) {
		if (remainingOptions.size() > 0 && currentTotal < this.NUM_LITERS) {
			// Try using the first container size in the arraylist
			int newTotalWith = currentTotal + remainingOptions.get(0);
			
			if (newTotalWith == this.NUM_LITERS) {
				// Save this solution
				ArrayList<Integer> newSolution = new ArrayList<Integer>(usedContainers);
				newSolution.add(remainingOptions.get(0));
				this.solutions.add(newSolution);
				this.numCombinations++;
			} else if (newTotalWith < this.NUM_LITERS) {
				// Add the current container to the solution in progress
				ArrayList<Integer> inProgressSolution = new ArrayList<Integer>(usedContainers);
				inProgressSolution.add(remainingOptions.get(0));
				
				// This item isn't enough, try using this as well as more containers
				ArrayList<Integer> listWithRemovedItem = new ArrayList<Integer>(remainingOptions);
				listWithRemovedItem.remove(0);
				
				this.tryCombinations(newTotalWith, listWithRemovedItem, inProgressSolution);
			} 
		
			// Also try proceeding without using this container
			ArrayList<Integer> listWithRemovedItem = new ArrayList<Integer>(remainingOptions);
			listWithRemovedItem.remove(0);
			this.tryCombinations(currentTotal, listWithRemovedItem, usedContainers);
		}
	}
	
	public int findMinNumberContainersInASolution() {
		int minNumber = Integer.MAX_VALUE;
		
		for (int i = 0; i < this.solutions.size(); i++) {
			if (this.solutions.get(i).size() < minNumber) {
				minNumber = this.solutions.get(i).size();
			}
		}
		
		return minNumber;
	}
	
	public int countNumSolutionsOfMinSize(int minSize) {
		int numSolutions = 0;
		
		for (int i = 0; i < this.solutions.size(); i++) {
			if (this.solutions.get(i).size() == minSize) {
				numSolutions++;
			}
		}
		
		return numSolutions;
	}
	
	public static void main(String[] args) {
		Day17 puzzle = new Day17(args);
		puzzle.findNumberCombinations();
	}
}
