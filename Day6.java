public class Day6 {

	private String[] input;
	private boolean[][] lightsOn;
	private int[][] brightness;
	
	private final int NUM_ROWS = 1000;
	private final int MODE_TOGGLE = 0;
	private final int MODE_ON = 1;
	private final int MODE_OFF = 2;
	
	public Day6(String[] input) {
		this.input = input;
		this.lightsOn = new boolean[NUM_ROWS][NUM_ROWS];
		this.brightness = new int[NUM_ROWS][NUM_ROWS];
		
		// Initialize the lights to all off
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_ROWS; j++) {
				this.lightsOn[i][j] = false;
				this.brightness[i][j] = 0;
			}
		}
	}
	
	public void printNumberLightsOn() {
		int numberOn = 0;
		int totalBrightness = 0;
		
		// Count the number of lights that are on
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_ROWS; j++) {
				if (this.lightsOn[i][j]) { 
					numberOn++;
				}
				
				totalBrightness += this.brightness[i][j];
			}
		}
		
		System.out.println(numberOn);
		System.out.println(totalBrightness);
	}
	
	public void followInstructions() {
		int mode = -1;		
		int startX = -1;
		int startY = -1;
		int stopX = -1;
		int stopY = -1;
		
		boolean readyToProcess = false;
		
		// Read instructions one phrase at a time
		for (int i = 0; i < this.input.length; i++) {
			String instruction = this.input[i].toLowerCase();
			
			// Read mode: toggle (0), turn on (1), or turn off (2)
			if (instruction.contains("toggle")) {
				mode = MODE_TOGGLE;
			} else if (instruction.contains("on")) {
				mode = MODE_ON;
			} else if (instruction.contains("off")) {
				mode = MODE_OFF;
			}
			
			// Read range strings
			if (instruction.contains(",") && startX == -1 && startY == -1) {
				startX = Integer.parseInt(instruction.split(",")[0].trim());
				startY = Integer.parseInt(instruction.split(",")[1].trim());
			}
			else if (instruction.contains(",") && startX != -1 && startY != -1) {
				stopX = Integer.parseInt(instruction.split(",")[0].trim());
				stopY = Integer.parseInt(instruction.split(",")[1].trim());
				readyToProcess = true;
			}
			
			if (readyToProcess) {
				// Handle lights
				this.processInstruction(startX, startY, stopX, stopY, mode);
			
				// Reset instructions
				startX = -1;
				startY = -1;
				stopX = -1;
				stopY = -1;
				mode = -1;
				readyToProcess = false;
			}
		}
	}
	
	public void processInstruction(int startX, int startY, int stopX, int stopY, int mode) {
		for (int i = startX; i <= stopX; i++) {
			for (int j = startY; j <= stopY; j++) {
				if (mode == MODE_TOGGLE) {
					this.lightsOn[i][j] = !this.lightsOn[i][j];
					this.brightness[i][j] += 2;
				}
				else if (mode == MODE_ON) {
					this.lightsOn[i][j] = true;
					this.brightness[i][j] += 1;
				} else if (mode == MODE_OFF) {
					this.lightsOn[i][j] = false;
					
					if (this.brightness[i][j] > 0) {
						this.brightness[i][j] -= 1;
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Day6 puzzle = new Day6(args);
		puzzle.followInstructions();
		puzzle.printNumberLightsOn();
	}
	
}
