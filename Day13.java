import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Day13 {
	private ArrayList<String> names;
	private int[][] happinessUnits;
	private ArrayList<int[]> permutations;
	
	public Day13(boolean includeMyself) {
		this.names = new ArrayList<String>();
		
		// Check if Part B is being ran
		if (includeMyself) {
			this.names.add("Me");
		}
		
		this.readInputFile();
		this.permutations = new ArrayList<int[]>();
		this.calculatePermutations();
	}
	
	public void readInputFile() {
		ArrayList<String> input = new ArrayList<String>();
		
		BufferedReader buffReader = null;
		try{
			buffReader = new BufferedReader (new FileReader("day13.txt"));
			String line = buffReader.readLine();
			while(line != null){
				input.add(line);
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
		
		// Get names
		this.getNames(input);
		
		// After getting all the names, get happiness units
		this.happinessUnits = new int[this.names.size()][this.names.size()];
		
		// Initialize happiness units to 0
		for (int i = 0; i < this.names.size(); i++) {
			for (int j = 0; j < this.names.size(); j++) {
				this.happinessUnits[i][j] = 0;
			}
		}
		
		// Populate array of happiness units
		for (int i = 0; i < input.size(); i++) {
			this.getHappinessUnits(input.get(i));
		}
	}
	
	public void getNames(ArrayList<String> input) {
		// Get first name in the list
		String firstName = input.get(0).split(" ")[0];
		this.names.add(firstName);
		
		for (int i = 0; i < input.size(); i++) {
			String[] splitInput = input.get(i).trim().split(" ");
			
			if (splitInput[0].equals(firstName)) {
				// Get the last word (other guest name)
				String name = splitInput[splitInput.length-1].trim();
				name = name.replaceAll("\\.",	"");
				
				// Add the name to the list
				if (!this.names.contains(name)) {
					this.names.add(name);
				}
			}
			else {
				// We've seen all names at this point, so exit the loop
				break;
			}
		}
	}
	
	public void getHappinessUnits(String line) {
		String[] lineItems = line.trim().split(" ");
		String sourceName = lineItems[0];
		String destName = lineItems[lineItems.length - 1].trim().replaceAll("\\.", "");
		int happinessUnits = Integer.parseInt(lineItems[3]);
		boolean happinessUnitsPositive = lineItems[2].contains("gain");
		
		// Handle loss of happiness units
		if (!happinessUnitsPositive) {
			happinessUnits *= -1;
		}
		
		int sourceIndex = this.names.indexOf(sourceName);
		int destIndex = this.names.indexOf(destName);
		
		this.happinessUnits[sourceIndex][destIndex] = happinessUnits;
	}
	
	public void calculatePermutations() {
		// Create an array of name indices
		int[] values = new int[this.names.size()];
		
		for (int i = 0; i < this.names.size(); i++) {
			values[i] = i;
		}
		
		// Get all permutations of the array
		this.permutations.clear();
		this.heapsAlgorithm(values, this.names.size());
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
	
	public void findOptimalArrangement() {
		int minHappiness = Integer.MAX_VALUE;
		int maxHappiness = Integer.MIN_VALUE;
		
		int indexOfMinHappiness = -1;
		int indexOfMaxHappiness = -1;
		
		int numGuests = this.names.size();
		
		for (int i = 0; i < this.permutations.size(); i++) {
			// Get the current permutation
			int[] currentSeatingArrangement = this.permutations.get(i);
			int currentHappiness = 0;
			
			for (int j = 0; j < currentSeatingArrangement.length; j++) {
				// Get happiness for person seated to the left of guest j
				int currentIndex = currentSeatingArrangement[j];
				int positionLeft = (((j-1) % numGuests) + numGuests) % numGuests; // Force % to be positive
				int leftIndex = currentSeatingArrangement[positionLeft];
				currentHappiness += this.happinessUnits[currentIndex][leftIndex];
				
				// Get happiness for person seated to the right of guest j
				int positionRight = (((j+1) % numGuests) + numGuests) % numGuests; // Force % to be positive
				int rightIndex = currentSeatingArrangement[positionRight];
				currentHappiness += this.happinessUnits[currentIndex][rightIndex];
			}
			
			// Check for shortest distance
			if (currentHappiness < minHappiness) {
				minHappiness = currentHappiness;
				indexOfMinHappiness = i;
			}
			
			// Check for longest distance
			if (currentHappiness > maxHappiness) {
				maxHappiness = currentHappiness;
				indexOfMaxHappiness = i;
			}
		}
		
		// Print shortest tour
		System.out.println(minHappiness);
		int[] minHappinessArrangement = this.permutations.get(indexOfMinHappiness);
		
		for (int i = 0; i < minHappinessArrangement.length; i++) {
			System.out.print(this.names.get(minHappinessArrangement[i]) + " ");
		}
		
		// Print longest tour
		System.out.println("\n" + maxHappiness);
		int[] maxHappinessArrangement = this.permutations.get(indexOfMaxHappiness);
		
		for (int i = 0; i < maxHappinessArrangement.length; i++) {
			System.out.print(this.names.get(maxHappinessArrangement[i]) + " ");
		}
	}
	
	public static void main(String[] args) {
		boolean includeMyself = args[0].contains("1");
		Day13 puzzle = new Day13(includeMyself);
		puzzle.findOptimalArrangement();
	}
}
