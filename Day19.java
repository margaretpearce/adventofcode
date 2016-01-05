import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;


public class Day19 {

	private ArrayList<String> replaceFrom;
	private ArrayList<String> replaceTo;
	private ArrayList<String> molecules;
	
	private ArrayList<String[]> transformations = new ArrayList<String[]>();
	private HashMap<Integer, ArrayList<String>> stepwiseReplacements;
	private String medicineMolecule;
	
	public Day19() {
		this.replaceFrom = new ArrayList<String>();
		this.replaceTo = new ArrayList<String>();
		this.molecules = new ArrayList<String>();
		this.stepwiseReplacements = new HashMap<Integer, ArrayList<String>>();
		this.transformations = new ArrayList<String[]>();
		this.readInputFile();
	}
	
	public void readInputFile() {
		BufferedReader buffReader = null;
		try{
			buffReader = new BufferedReader (new FileReader("day19.txt"));
			String line = buffReader.readLine();
			
			while(line != null) {
				if (line.contains("=>")) {
					String[] lineItems = line.split("=>");
					this.replaceFrom.add(lineItems[0].trim());
					this.replaceTo.add(lineItems[1].trim());
					
					String[] transformation = new String[]{ lineItems[0].trim(), 
							lineItems[1].trim()};
					this.transformations.add(transformation);
				} else if (line != "") {
					this.medicineMolecule = line;
				}
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
	}
	
	public void findMoleculesAfterReplacements() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.medicineMolecule);
		
		// Go through the molecule one character at a time
		for (int i = 0; i < this.medicineMolecule.length(); i++) {
			
			// Try all replacements
			for (int j = 0; j < this.replaceFrom.size(); j++) {
				String replace = this.replaceFrom.get(j);
				String replacedWith = this.replaceTo.get(j);
				int remainingLength = (sb.subSequence(i, sb.length())).length();
				
				if (remainingLength >= replace.length() && 
						sb.subSequence(i, i+replace.length()).equals(replace)) {
					// Do the replacement and add it to the queue
					sb.delete(i, i+replace.length());
					
					// Add the replacement string to the front
					sb.insert(i, replacedWith);
					
					// Add to the list of molecules if it hasn't already been found
					if (!this.molecules.contains(sb.toString())) {
						this.molecules.add(sb.toString());
					}
					
					// Undo the replacement by resetting the stringbuilder
					sb.delete(0, sb.length());
					sb.append(this.medicineMolecule);
				}
			}
		}
	}
	
	public void printNumberOfMoleculeReplacements() {
		System.out.println(this.molecules.size());
	}
	
	/* Adapted from Reddit solution.
	 * See: https://www.reddit.com/r/adventofcode/comments/3xglof/day_19_part_2java_part_1_works_part_2_results_in/
	 * Key factor: need to use Collections.shuffle() to try transformations in random orders.
	 * Otherwise, transformations on input is prone to resulting in an infinite loop.
	 */
	public void printPartB() {
		// Randomly reorganize the transformations list
		Collections.shuffle(this.transformations);
		int countSteps = 0;
		int reshuffleCount = 0;
		String medicineCopy = new String(this.medicineMolecule);
		
		while (!medicineCopy.equals("e")) {
			String lastMolecule = medicineCopy;
			
			for (String[] transformation : this.transformations) {
				// Try doing the replacement in reverse
				if (medicineCopy.contains(transformation[1])) {
					medicineCopy = replace(medicineCopy, transformation[1], 
							transformation[0], medicineCopy.indexOf(transformation[1]));
					countSteps++;
				}
			}
			
			// Check to see if no changes have occurred after the previous transformations
			if (lastMolecule.equals(medicineCopy) && (!medicineCopy.equals("e"))) {
				// Reshuffle the transformations and try again
				Collections.shuffle(this.transformations);
				medicineCopy = new String(this.medicineMolecule);
				countSteps = 0;
				reshuffleCount++;
			}
		}

		System.out.println("Part B: " + countSteps + " transformations after " + reshuffleCount + " reshuffles.");
	}
	
	public static String replace(String where, String what, String with, int pos) {
		return where.substring(0, pos) + with + where.substring(pos + what.length());
	}
	
	/* Breadth-first search attempt
	 * For my input, ran indefinitely
	 * See: https://www.reddit.com/r/adventofcode/comments/3xglof/day_19_part_2java_part_1_works_part_2_results_in/
	 */
	public void printNumberOfStepsToFavoriteMolecule() {
		boolean moleculeCreated = false;
		int stepNumber = 0;
		
		// Add the medicine molecule string as step 0
		ArrayList<String> stepZeroList = new ArrayList<String>();
		stepZeroList.add(this.medicineMolecule);
		this.stepwiseReplacements.put(stepNumber, stepZeroList);
		
		while (!moleculeCreated) {
			// Increment step number
			stepNumber++;
			System.out.println(stepNumber);
			
			// Get strings from the previous step
			ArrayList<String> previousStepStrings = this.stepwiseReplacements.get(stepNumber-1);
			
			// Create an empty list to store strings for this step
			ArrayList<String> currentStepStrings = new ArrayList<String>();
			
			// Find all possible replacements
			moleculeCreated = this.findReplacementsForStep(previousStepStrings, currentStepStrings);
			
			// Save results
			this.stepwiseReplacements.put(stepNumber, currentStepStrings);
		}
		
		// Print the step number where the favorite molecule was found
		System.out.println(stepNumber);
	}
	
	public boolean findReplacementsForStep(ArrayList<String> previousStepStrings, 
			ArrayList<String> currentStepStrings) {
		
		// Try all replacements in reverse for previous step strings one at a time
		for (int i = 0; i < previousStepStrings.size(); i++) {
			StringBuilder sb = new StringBuilder();
			sb.append(previousStepStrings.get(i));
			
			// Go through the current molecule one character at a time
			for (int j = 0; j < sb.length(); j++) {
				
				// Try all replacements
				for (int k = 0; k < this.replaceTo.size(); k++) {
					/* Replace in reverse - this ensures the search is performed on smaller
					 * and smaller strings, which is more efficient than starting from "e"
					 * and working up to the lengthy medicine molecule string.
					*/
					String replace = this.replaceTo.get(k);
					String replacedWith = this.replaceFrom.get(k);
					int remainingLength = (sb.subSequence(j, sb.length())).length();
					
					if (remainingLength >= replace.length() && 
							sb.subSequence(j, j+replace.length()).equals(replace)) {
						// Do the replacement
						sb.delete(j, j+replace.length());
						
						// Add the replacement string to the front
						sb.insert(j, replacedWith);
						
						// Add to the list of molecules for this step
						if (!currentStepStrings.contains(sb.toString())) {
							currentStepStrings.add(sb.toString());
						}
						
						// Check if this replacement resulted in the goal string
						if (sb.toString().equals("e")) {
							return true;
						}
						
						// Undo the replacement by resetting the stringbuilder
						sb.delete(0, sb.length());
						sb.append(previousStepStrings.get(i));
					}
				}
			}
		}
		
		// If no replacements resulted in the goal string, continue to another step
		return false;
	}
	
	/* Depth-first search attempt
	 * For my input, ran indefinitely
	 * See: https://www.reddit.com/r/adventofcode/comments/3xglof/day_19_part_2java_part_1_works_part_2_results_in/
	 */
	public int searchReplacementsForGoal() {
		LinkedList<String> stringsToTry = new LinkedList<String>();
		LinkedList<Integer> stepNumber = new LinkedList<Integer>();
		stringsToTry.addFirst(this.medicineMolecule);
		stepNumber.addFirst(0);
				
		while (!stringsToTry.isEmpty()) {
			// Get the first string in the stack
			StringBuilder sb = new StringBuilder(stringsToTry.removeFirst());
			int currentStepNumber = stepNumber.removeFirst() + 1;
			
			// Check if this is the goal string
			if (sb.toString().equals("e")) {
				return currentStepNumber;
			} else {
				// Otherwise, try all replacements for this string - DFS, not BFS
				for (int i = 0; i < this.replaceTo.size(); i++) {
					
					// Go through the current molecule one character at a time
					for (int j = 0; j < sb.length(); j++) {
						String replace = this.replaceTo.get(i);
						String replacedWith = this.replaceFrom.get(i);
						int remainingLength = (sb.subSequence(j, sb.length())).length();
							
							if (remainingLength >= replace.length() && 
									sb.subSequence(j, j+replace.length()).equals(replace)) {
								// Do the replacement
								sb.delete(j, j+replace.length());
								
								// Add the replacement string to the front
								sb.insert(j, replacedWith);
								
								// Add to the stack
								if (!stringsToTry.contains(sb.toString())) {
									stringsToTry.push(sb.toString());
									stepNumber.push(currentStepNumber);
								}
								
								// Undo the replacement by resetting the stringbuilder
								sb.delete(j, j+replacedWith.length());
								sb.insert(j, replace);
							}
						}
				}
			}
		}
		
		return -1;
	}
	
	public static void main(String[] args) {
		Day19 puzzle = new Day19();
		puzzle.findMoleculesAfterReplacements();
		puzzle.printNumberOfMoleculeReplacements();
		
		// Part B (with some help from Reddit)
		puzzle.printPartB();
	}

}
