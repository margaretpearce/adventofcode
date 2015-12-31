public class Day11 {

	public String getNextPassword(String initialPassword) {
		String nextPassword = initialPassword;
		boolean passwordMeetsRequirements = false;
		
		while (!passwordMeetsRequirements) {
			// Shift letters
			nextPassword = this.shiftLetters(nextPassword);
			
			// Must have increasing straight of 3+ letters
			boolean passesIncreasingStraight = this.testForIncreasingStraight(nextPassword);
			
			// Must not contain i, o, l
			boolean passesIllegalLetters = this.testNoIllegalLetters(nextPassword);
			
			// Must contain at least two different, non-overlapping pairs
			boolean passesTwoPairs = this.testHasNonOverlappingPairs(nextPassword);
			
			passwordMeetsRequirements = passesIncreasingStraight && passesIllegalLetters && passesTwoPairs; 
		}
		
		System.out.println(nextPassword);
		return nextPassword;
	}
	
	public String shiftLetters(String input) {
		String letters = "abcdefghijklmnopqrstuvwxyz";
		
		// Increase rightmost and move left
		for (int i = input.length() - 1; i >= 0; i--) {
			char currentChar = input.charAt(i);
			int indexOfCurrentChar = letters.indexOf(currentChar);
			int nextCharIndex = (indexOfCurrentChar + 1) % letters.length();
			
			input = input.substring(0, i) + letters.charAt(nextCharIndex) + input.substring(i+1);
			
			if (nextCharIndex > indexOfCurrentChar) {
				// Found a letter that doesn't wrap around
				break;
			}
		}
		
		return input;
	}
	
	public boolean testForIncreasingStraight(String password) {
		String letters = "abcdefghijklmnopqrstuvwxyz";
		
		for (int i = 0; i < letters.length() - 2; i++) {
			String straightToCheck = letters.substring(i,i+3);
			if (password.contains(straightToCheck)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean testNoIllegalLetters(String password) {
		if (password.contains("o") || password.contains("i") || password.contains("l")) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean testHasNonOverlappingPairs(String password) {
		int firstIndex = -1;
		int secondIndex = -1;
		
		String[] pairs = new String[]{"aa", "bb", "cc", "dd", "ee", "ff", "gg", "hh", "jj", "kk",
			"mm", "nn", "pp", "qq", "rr", "ss", "tt", "uu", "vv", "ww", "xx", "yy", "zz"};
		
		for (int i = 0; i < pairs.length; i++) {
			// Try to find one match
			if (firstIndex == -1) {
				firstIndex = password.indexOf(pairs[i]);
			}
			else if (firstIndex != -1 && secondIndex == -1) {
				// Try to look for a second match
				secondIndex = password.indexOf(pairs[i]);
			}
			
			// Check if two matches have been found
			if (firstIndex != -1 && secondIndex != -1 && (Math.abs(secondIndex - firstIndex) > 1)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		Day11 puzzle = new Day11();
		String next = puzzle.getNextPassword(args[0]);
		puzzle.getNextPassword(next);
	}
}
