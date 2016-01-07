import java.math.BigInteger;
import java.util.ArrayList;

public class Day25 {

	private int targetRow;
	private int targetCol;

	private ArrayList<Integer> numbers;
	private ArrayList<Integer> numberRow;
	private ArrayList<Integer> numberCol;

	public Day25(int startingNumber, int row, int col) {
		this.targetRow = row;
		this.targetCol = col;

		this.numbers = new ArrayList<Integer>();
		this.numberRow = new ArrayList<Integer>();
		this.numberCol = new ArrayList<Integer>();
		
		this.numbers.add(startingNumber);
		this.numberRow.add(1);
		this.numberCol.add(1);
	}

	public void findTargetNumber() {
		boolean found = false;

		while (!found) {
			// Find the row/ col for the next number
			int lastRow = this.numberRow.get(this.numberRow.size()-1);
			int lastCol = this.numberCol.get(this.numberCol.size()-1);

			int newRow = -1;
			int newCol = -1;

			if (lastRow == 1) {
				newRow = lastCol + 1;	// start at new row
				newCol = 1;				// start in the first column
			} else {
				newRow = lastRow - 1;	// up
				newCol = lastCol + 1;	// right
			}

			// Compute the next number
			int lastNumber = this.numbers.get(this.numbers.size()-1);
			BigInteger intermediateVal = BigInteger.valueOf(lastNumber);
			BigInteger multiplied = intermediateVal.multiply(BigInteger.valueOf(252533));
			BigInteger newNumberBig = multiplied.mod(BigInteger.valueOf(33554393));
			int newNumber = newNumberBig.intValue();

			// Save values
			this.numberRow.add(newRow);
			this.numberCol.add(newCol);
			this.numbers.add(newNumber);

			// Check if the target has been found
			if (newRow == this.targetRow && newCol == this.targetCol) {
				found = true;
				System.out.println(newNumber);
			}
		}
	}

	public static void main(String[] args) {
		int startingNumber = Integer.parseInt(args[0]);
		int row = Integer.parseInt(args[1]);
		int col = Integer.parseInt(args[2]);
		
		Day25 puzzle = new Day25(startingNumber, row, col);
		puzzle.findTargetNumber();
	}

}
