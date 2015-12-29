/***
 * SLOW Java solution
 * See Day10.py for a speedier Python implementation
 * @author margaretpearce
 */
public class Day10 {

	public void processInput(String input, int timesToApply) {
		String currentIteration = input;
		
		for (int i = 0; i < timesToApply; i++) {
			String workingCopy = "";
			
			// Start at the first char
			int charIndex = 0;
			char currentChar = currentIteration.charAt(charIndex);
			int charCount = 1;
			charIndex++;
			
			// Loop through each character in the current input string
			while (charIndex < currentIteration.length()) {
				// Compare char at this index to the last one
				if (currentIteration.charAt(charIndex) == currentChar) {
					// If the chars are the same, increment the counter
					charCount++;
				} else {
					// If the chars don't match, print <count><lastchar>
					workingCopy += charCount;
					workingCopy += currentChar;
					
					// Reset the char to compare and the counter
					charCount = 1;
					currentChar = currentIteration.charAt(charIndex);
				}
				
				charIndex++;
			}
			
			// After exiting the loop, print the last char/ count
			workingCopy += charCount;
			workingCopy += currentChar;
			
			// Set the next iteration to start with the result from this iteration
			currentIteration = workingCopy;
		}
		
		// Print the final string
		System.out.println(currentIteration);
		System.out.println("Length: " + currentIteration.length());
	}
	
	public static void main(String[] args) {
		Day10 puzzle = new Day10();
		String originalInput = args[0];
		int timesToApply = Integer.parseInt(args[1]);
		
		puzzle.processInput(originalInput, timesToApply);
	}

}
