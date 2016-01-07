import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Day24 {

	private int[] packageWeights;
	private int totalWeight;
	private int minNumberInFirstGroup;
	private long quantumEntanglement;
	private PriorityQueue<ArrayList<Integer>> candidates;

	public Day24(String[] input) {
		this.quantumEntanglement = Integer.MAX_VALUE;
		this.minNumberInFirstGroup = Integer.MAX_VALUE;
		this.packageWeights = new int[input.length];

		// Create a priority queue for solutions to explore (shorter solutions explored first)
		Comparator<ArrayList<Integer>> candidateComparator = new Comparator<ArrayList<Integer>>() {
			public int compare(ArrayList<Integer> candidateA, ArrayList<Integer> candidateB) {
				return Integer.valueOf(candidateA.size()).compareTo(Integer.valueOf(candidateB.size()));
			}
		};
		
		this.candidates = new PriorityQueue<ArrayList<Integer>>(this.packageWeights.length, candidateComparator);

		// Read input backwards
		for (int i = this.packageWeights.length-1; i >= 0; i--) {
			this.packageWeights[i] = Integer.parseInt(input[i]);
			this.totalWeight += this.packageWeights[i];
		}
	}

	public void findBestArrangement(int targetWeight) {
		// Create ArrayList of the package weights
		ArrayList<Integer> listOfWeights = new ArrayList<Integer>();
		for (int i = 0; i < this.packageWeights.length; i++) {
			listOfWeights.add(this.packageWeights[i]);
		}

		this.getSubsetSum(listOfWeights, targetWeight, new ArrayList<Integer>());
		int smallestCandidateSize = this.candidates.peek().size();
		
		// Look through all candidates
		for (int i = 0; i < this.candidates.size(); i++) {
			ArrayList<Integer> currentCandidate = this.candidates.remove();
			
			// If the remaining candidates have more packages than the smallest solution, don't bother checking them
			if (currentCandidate.size() > smallestCandidateSize) {
				break;
			}

			int numInFirst = currentCandidate.size();
			long qe = this.getQuantumEntanglementOfFirstGroup(currentCandidate);

			if (numInFirst < this.minNumberInFirstGroup) {
				this.minNumberInFirstGroup = numInFirst;
				this.quantumEntanglement = qe;
			} else if (numInFirst == this.minNumberInFirstGroup) {
				if (qe < this.quantumEntanglement) {
					this.minNumberInFirstGroup = numInFirst;
					this.quantumEntanglement = qe;
				}
			}
		}
	}

	public void getSubsetSum(ArrayList<Integer> weights, int target, ArrayList<Integer> partialSolution) {
		int currentSum = 0;

		// Get the sum of the current solution
		for (int i = 0; i < partialSolution.size(); i++) {
			currentSum += partialSolution.get(i);
		}

		if (currentSum == target) {
			// This is a subset that equals the target sum
			if (this.candidates != null && this.candidates.size() > 0) {
				int smallestCandidate = this.candidates.peek().size();
				
				// Don't bother add candidates with more elements than the current smallest solution
				if (partialSolution.size() <= smallestCandidate) {
					this.candidates.add(partialSolution);
				}
			} else {
				this.candidates.add(partialSolution);
			}
		} else if (currentSum > target) {
			// If the sum is too big, exit
			return;
		} else {
			// If the sum is too small, keep adding numbers to it and trying
			for (int i = 0; i < weights.size(); i++) {
				ArrayList<Integer> remaining = new ArrayList<Integer>();
				int n = weights.get(i);

				// Get all weights after the ith weight
				for (int j = i+1; j < weights.size(); j++) {
					remaining.add(weights.get(j));
				}

				ArrayList<Integer> partial = new ArrayList<Integer>(partialSolution);
				partial.add(n);
				this.getSubsetSum(remaining, target, partial);
			}
		} 
	}

	public long getQuantumEntanglementOfFirstGroup(ArrayList<Integer> assignments) {
		long qe = 1;

		for (int i = 0; i < assignments.size(); i++) {
			qe *= assignments.get(i);
		}

		return qe;
	}

	public void printQuantumEntanglement() {
		System.out.println(this.quantumEntanglement);
	}

	public static void main(String[] args) {
		Day24 puzzle = new Day24(args);
		puzzle.findBestArrangement(puzzle.totalWeight / 3);
		puzzle.printQuantumEntanglement();
		
		Day24 puzzlePartB = new Day24(args);
		puzzlePartB.findBestArrangement(puzzle.totalWeight / 4);
		puzzlePartB.printQuantumEntanglement();
	}

}
