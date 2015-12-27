import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2 {
	
	private String[] inputDimensions;
	private int wrappingPaper;
	private int ribbon;
	
	public Day2(String[] input) {
		this.wrappingPaper = 0;
		this.ribbon = 0;
		this.inputDimensions = input;
	}
	
	/*
	 * Find the amount of wrapping paper needed and print the result
	 */
	public void getAmountOfWrappingPaperNeeded() {
		this.processInput();
		
		// Print the total number of square feet of wrapping paper needed
		System.out.println(this.wrappingPaper);
		
		// Print the amount of ribbon needed
		System.out.println(this.ribbon);
	}
	
	/*
	 * Read the input, calculate paper needed for each line, and add it to the total.
	 */
	public void processInput() {
		// Read the input line-by=line
		for (int i = 0; i < this.inputDimensions.length; i++) {
			if (this.inputIsValid(this.inputDimensions[i])) {
				// If the input is valid, then there will be three integers separated by "x"
				String[] currentInput = this.inputDimensions[i].split("x");
				int length = Integer.parseInt(currentInput[0]);
				int width = Integer.parseInt(currentInput[1]);
				int height = Integer.parseInt(currentInput[2]);
				
				// Get area of sides
				int frontBackSideArea = length * height;
				int leftRightSideArea = width * height;
				int topBottomSideArea = length * width;
				
				// Get the smallest side area
				int smallestSide = Math.min(frontBackSideArea, Math.min(leftRightSideArea, topBottomSideArea));
				
				// Find the amount of wrapping paper needed and add it to the total
				int wrappingPaper = 2*frontBackSideArea + 2*leftRightSideArea + 2*topBottomSideArea + smallestSide;
				this.wrappingPaper += wrappingPaper;
				
				// Get the ribbon for the bow
				int bow = length * width * height;
				
				// Get the ribbon for wrapping
				int frontBackPerimeter = 2*length + 2*height;
				int leftRightPerimeter = 2*width + 2*height;
				int topBottomPerimeter = 2*length + 2*width;
				int smallestPerimeter = Math.min(frontBackPerimeter, Math.min(leftRightPerimeter, topBottomPerimeter));
				
				// Find the amount of ribbon needed and add it to the total
				this.ribbon += bow + smallestPerimeter;
			}
		}
	}
	
	/*
	 * Input should be "number x number x number" (without spaces)
	 */
	public boolean inputIsValid(String input) {
		Pattern pattern = Pattern.compile("\\d+x\\d+x\\d+");
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}
	
	public static void main(String[] args) {
		// Get puzzle input from command line
		if (args.length >= 1) {
			// Create puzzle instance
			Day2 puzzle = new Day2(args);
			
			// Get the amount of wrapping paper needed
			puzzle.getAmountOfWrappingPaperNeeded();
		}
	}
}
