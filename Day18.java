import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Day18 {
	
	private int[][] currentState;
	private final int GRID_SIZE = 100;
	private final String LIGHT_ON = "#";
	private final String LIGHT_OFF = ".";
	private final int NUM_STEPS = 100;
	
	public Day18() {
		this.currentState = new int[this.GRID_SIZE][this.GRID_SIZE];
		this.readInputFile();
	}
	
	public void readInputFile() {
		BufferedReader buffReader = null;
		try{
			buffReader = new BufferedReader (new FileReader("day18.txt"));
			int rowNumber = 0;
			String line = buffReader.readLine();
			
			while(line != null) {
				this.setInitialLightsInRow(line, rowNumber);
				
				rowNumber++;
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
	
	public void setInitialLightsInRow(String lights, int rowNumber) {
		for (int i = 0; i < lights.length(); i++) {
			String light = lights.substring(i, i+1);
			
			if (light.equalsIgnoreCase(this.LIGHT_ON)) {
				this.currentState[rowNumber][i] = 1;
			} else if (light.equalsIgnoreCase(this.LIGHT_OFF)) {
				this.currentState[rowNumber][i] = 0;
			}
		}
	}
	
	public void runSteps() {
		for (int i = 0; i < this.NUM_STEPS; i++) {
			this.currentState = this.getNextState();
		}
	}
	
	public int[][] getNextState() {
		int[][] nextState = new int[this.GRID_SIZE][this.GRID_SIZE];
		
		for (int i = 0; i < this.GRID_SIZE; i++) {
			for (int j = 0; j < this.GRID_SIZE; j++) {
				int numNeighborsOn = this.countNumNeighborLightsOn(i, j);
				
				if (currentState[i][j] == 1 && (numNeighborsOn == 2 || numNeighborsOn == 3)) {
					// Light stays on if it is current on and 2 or 3 neighbors are on
					nextState[i][j] = 1;
				}
				else if (currentState[i][j] == 0 && numNeighborsOn == 3) {
					// Light that is off turns on if exactly 3 neighbors on
					nextState[i][j] = 1;
				}
				else {
					// Otherwise, turns off
					nextState[i][j] = 0;
				}
			}
		}
		
		return nextState;
	}
	
	public int countNumNeighborLightsOn(int row, int col) {
		int numNeighborsOn = 0;
		
		/* Pattern for X at row, col:
		 * 1 2 3
		 * 4 X 5
		 * 6 7 8
		 */
		if (row > 0 && col > 0) {
			// 1
			numNeighborsOn += this.currentState[row-1][col-1];
		}
		if (row > 0) {
			// 2
			numNeighborsOn += this.currentState[row-1][col];
		}
		if (row > 0 && col < this.GRID_SIZE-1) {
			// 3
			numNeighborsOn += this.currentState[row-1][col+1];
		}
		if (col > 0) {
			// 4
			numNeighborsOn += this.currentState[row][col-1];
		}
		if (col < this.GRID_SIZE-1) {
			// 5
			numNeighborsOn += this.currentState[row][col+1];
		}
		if (row < this.GRID_SIZE-1 && col > 0) {
			// 6
			numNeighborsOn += this.currentState[row+1][col-1];
		}
		if (row < this.GRID_SIZE-1) {
			// 7
			numNeighborsOn += this.currentState[row+1][col];
		}
		if (row < this.GRID_SIZE-1 && col < this.GRID_SIZE-1) {
			// 8
			numNeighborsOn += this.currentState[row+1][col+1];
		}
		
		return numNeighborsOn;
	}
	
	public void printNumLightsOn() {
		int numLightsOn = 0;
		
		for (int i = 0; i < this.GRID_SIZE; i++) {
			for (int j = 0; j < this.GRID_SIZE; j++) {
				numLightsOn += this.currentState[i][j];
			}
		}
		
		System.out.println(numLightsOn);
	}
	
	public void runStepsPartB() {
		// Initially set corner lights to all on
		this.currentState[0][0] = 1;
		this.currentState[0][this.GRID_SIZE-1] = 1;
		this.currentState[this.GRID_SIZE-1][0] = 1;
		this.currentState[this.GRID_SIZE-1][this.GRID_SIZE-1] = 1;
		
		for (int i = 0; i < this.NUM_STEPS; i++) {
			this.currentState = this.getNextState();
			
			// Force corner lights to stay lit after figuring out next step
			this.currentState[0][0] = 1;
			this.currentState[0][this.GRID_SIZE-1] = 1;
			this.currentState[this.GRID_SIZE-1][0] = 1;
			this.currentState[this.GRID_SIZE-1][this.GRID_SIZE-1] = 1;
		}
	}
	
	public static void main(String[] args) {
		Day18 puzzle = new Day18();
		puzzle.runSteps();
		puzzle.printNumLightsOn();
		
		Day18 puzzlePartB = new Day18();
		puzzlePartB.runStepsPartB();
		puzzlePartB.printNumLightsOn();
	}
}
