import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day1 {
	
	private String input;
	
	public Day1(String input) {
		if (this.inputIsValid(input)) {
			this.input = input;
		}
	}
	
	/*
	 * Input should only contain "(" and ")"
	 */
	public boolean inputIsValid(String input) {
		Pattern pattern = Pattern.compile("^[(|)]+$");
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}
	
	public void printFloorResult() {
		if (this.input != null) {
			// Start at the ground floor (floor 0)
			int floorNumber = 0;
			
			// Process one character at a time to go up or down a floor
			for (int i = 0; i < this.input.length(); i++) {
				char floorDirection = this.input.charAt(i);
				
				if (floorDirection == ')') {
					floorNumber--;
				}
				else if (floorDirection == '(') {
					floorNumber++;
				}
			}
			
			// Print the final floor number
			System.out.println(floorNumber);
		}
		else {
			// If the input is invalid or not given, assume we stay on the ground floor
			System.out.println("0");
		}
	}
	
	public void findFirstBasementPosition() {
		if (this.input != null) {
			// Start at the ground floor (floor 0)
			int floorNumber = 0;
			
			// Process one character at a time to go up or down a floor
			for (int i = 0; i < this.input.length(); i++) {
				char floorDirection = this.input.charAt(i);
				
				if (floorDirection == ')') {
					floorNumber--;
				}
				else if (floorDirection == '(') {
					floorNumber++;
				}
				
				// If we reach the basement, print the current character position (starting at 1)
				if (floorNumber == -1) {
					System.out.println(i+1);
					break;
				}
			}
		}
		else {
			// If the input is invalid or not given, return -1 to indicate we never enter the basement
			System.out.println("-1");
		}
	}

	public static void main(String[] args) {
		// Get puzzle input from command line
		if (args.length == 1) {
			String input = args[0];
			
			// Create puzzle instance
			Day1 puzzle = new Day1(input);
			
			// Find final floor
			puzzle.printFloorResult();
			
			// Find basement position
			puzzle.findFirstBasementPosition();
		}
	}
}
