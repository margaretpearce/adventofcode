public class Day5 {
	
	private String[] input;
	private int numberNiceStrings;
	private int numberNiceStringsPartB;
	
	public Day5(String[] input) {
		this.input = input;
		this.numberNiceStrings = 0;
		this.numberNiceStringsPartB = 0;
	}
	
	public void getNumberOfNiceStrings() {
		for (int i = 0; i < this.input.length; i++) {
			if (stringIsNice(input[i])) {
				this.numberNiceStrings++;
			}
			if (stringIsNicePartB(input[i])) {
				this.numberNiceStringsPartB++;
			}
		}
		
		System.out.println(this.numberNiceStrings);
		System.out.println(this.numberNiceStringsPartB);
	}
	
	public boolean stringIsNice(String input) {
		// Must contain at least 3 vowels
		boolean passesVowelTest = this.testVowels(input);
		
		// Must contain at least one letter that appears twice in a row
		boolean passesDuplicateTest = this.testDuplicateLetter(input);
		
		// Must not contain "ab", "cd", "pq", or "xy"
		boolean passesDoesNotContainTest = this.testDoesNotContain(input);
		
		return passesVowelTest && passesDuplicateTest && passesDoesNotContainTest;
	}
	
	public boolean testVowels(String input) {
		int numVowels = 0;
		
		for (int i = 0; i < input.length(); i++) {
			char currentChar = input.toLowerCase().charAt(i);
			
			// Check if this character is a vowel
			if (currentChar == 'a' || currentChar == 'e' || currentChar == 'i' || 
					currentChar == 'o' || currentChar == 'u') {
				numVowels++;
			}
			
			// Return true if at least three vowels have been found
			if (numVowels == 3) {
				return true;
			}
		}
	
		return false;
	}
	
	public boolean testDuplicateLetter(String input) {
		if (input.contains("aa") || input.contains("bb") || input.contains("cc") || 
				input.contains("dd") || input.contains("ee") || input.contains("ff") || 
				input.contains("gg") || input.contains("hh") || input.contains("ii") || 
				input.contains("jj") || input.contains("kk") || input.contains("ll") || 
				input.contains("mm") || input.contains("nn") || input.contains("oo") || 
				input.contains("pp") || input.contains("qq") || input.contains("rr") || 
				input.contains("ss") || input.contains("tt") || input.contains("uu") || 
				input.contains("vv") || input.contains("ww") || input.contains("xx") || 
				input.contains("yy") || input.contains("zz")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean testDoesNotContain(String input) {
		if (input.contains("ab") || input.contains("cd") || input.contains("pq") || 
				input.contains("xy")) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean stringIsNicePartB(String input) {
		// Must have pair of 2 letters appearing twice without overlapping
		boolean passesPairOfDuplicates = this.testHasPairOfDuplicatesNonOverlapping(input);
		
		// Must have repeat letter with one letter in between
		boolean passesRepeatLetter = this.testHasRepeatLetter(input);
		
		return passesPairOfDuplicates && passesRepeatLetter;
	}
	
	public boolean testHasPairOfDuplicatesNonOverlapping(String input) {
		for (int i = 0; i < input.length() - 2; i++) {
			// Get the pair of letters starting at this index
			String pair = input.substring(i, i+2);
			
			// Check if the remaining substring contains this pair
			String remaining = input.substring(i+2);
			if (remaining.contains(pair)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean testHasRepeatLetter(String input) {
		for (int i = 0; i < input.length() - 2; i++) {
			char currentChar = input.charAt(i);
			
			// Check if the character 2 positions ahead matches
			char twoAhead = input.charAt(i+2);
			
			if (currentChar == twoAhead) {
				return true;
			}
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		// Create puzzle instance
		Day5 puzzle = new Day5(args);
		
		puzzle.getNumberOfNiceStrings();
	}
}
