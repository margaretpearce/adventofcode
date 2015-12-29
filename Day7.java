import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Day7 {
	private LinkedList<String> inputInstructions;
	private ArrayList<String> wires;
	private ArrayList<Integer> wireValues;

	private final int MODE_SETVALUE = 0;
	private final int MODE_AND = 1;
	private final int MODE_OR = 2;
	private final int MODE_LSHIFT = 3;
	private final int MODE_RSHIFT = 4;
	private final int MODE_NOT = 5;

	public Day7() {
		this.inputInstructions = new LinkedList<String>();
		this.wires = new ArrayList<String>();
		this.wireValues = new ArrayList<Integer>(); 
	}

	public void printWireValues() {
		this.readInputFile();
		this.parseInputFile();

		for (int i = 0; i < this.wires.size(); i++) {
			String wireName = this.wires.get(i);
			Integer wireValue = this.wireValues.get(i);
			System.out.println(wireName + ": " + wireValue);
		}
	}
	
	public void runPartB() {		
		// Get the value for a
		int indexOfA = this.wires.indexOf("a");
		int valOfA = this.wireValues.get(indexOfA);
		
		// Override value of B to A
		this.wires.clear();
		this.wireValues.clear();
		this.setWireValue("b", valOfA);
		
		// Reset the other wires
		this.printWireValues();
	}

	public void readInputFile() {
		BufferedReader buffReader = null;
		try{
			buffReader = new BufferedReader (new FileReader("day7.txt"));
			String line = buffReader.readLine();
			while(line != null){
				// Store each instruction
				this.inputInstructions.add(line);
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

	public void parseInputFile() {
		while (!this.inputInstructions.isEmpty()) {
			String instruction = this.inputInstructions.removeFirst();
			String[] instructionComponents = instruction.split(" ");

			String sourceWireA = "";
			String sourceWireB = "";
			int operator = -1;
			String destinationWire = "";
			String operatorValue = "";

			String numericPattern = "\\d+";

			// Figure out which operator we are using
			if (instruction.contains("AND")) {
				operator = this.MODE_AND;
				if (instructionComponents[0].matches(numericPattern)) {
					operatorValue = instructionComponents[0];
				} 
				else {
					sourceWireA = instructionComponents[0];
				}
				sourceWireB = instructionComponents[2];
				destinationWire = instructionComponents[4];
			} 
			else if (instruction.contains("OR")) {
				operator = this.MODE_OR;
				sourceWireA = instructionComponents[0];
				sourceWireB = instructionComponents[2];
				destinationWire = instructionComponents[4];
			} 
			else if (instruction.contains("LSHIFT")) {
				operator = this.MODE_LSHIFT;
				sourceWireA = instructionComponents[0];
				operatorValue = instructionComponents[2];
				destinationWire = instructionComponents[4];
			} 
			else if (instruction.contains("RSHIFT")) {
				operator = this.MODE_RSHIFT;
				sourceWireA = instructionComponents[0];
				operatorValue = instructionComponents[2];
				destinationWire = instructionComponents[4];
			} 
			else if (instruction.contains("NOT")) {
				operator = this.MODE_NOT;
				sourceWireB = instructionComponents[1];
				destinationWire = instructionComponents[3];
			} 
			else {
				operator = this.MODE_SETVALUE;
				if (instructionComponents[0].matches(numericPattern)) {
					operatorValue = instructionComponents[0];
				} 
				else {
					sourceWireA = instructionComponents[0];
				}
				destinationWire = instructionComponents[2];
			}

			// Try to process this line
			boolean wasProcessed = this.processInstruction(sourceWireA, sourceWireB, operator, 
					destinationWire, operatorValue);

			// If the line can't be processed, add it to the end of the queue
			if (!wasProcessed) {
				this.inputInstructions.addLast(instruction);
			}
		}
	}

	public boolean processInstruction(String sourceWireA, String sourceWireB, int operator, 
			String destinationWire, String operatorValue) {
		// Check if the input wires are ready first
		if (sourceWireA != "" && !this.wireIsReady(sourceWireA)) {
			return false;
		}
		else if (sourceWireB != "" && !this.wireIsReady(sourceWireB)) {
			return false;
		}

		// Get operator value, if applicable
		Integer operatorNumber = -1;

		if (operatorValue != "") {
			operatorNumber = Integer.parseInt(operatorValue);
		}

		// Process instruction based on the operator
		Integer valueToSet = 0;

		if (operator == this.MODE_SETVALUE) {
			// Check if a number should be set or the value of another wire should be set
			if (operatorNumber == -1) {
				if (sourceWireA != "") {
					// Set to the value of wireA
					Integer wireA = this.getWireValue(sourceWireA);
					valueToSet = wireA;
				} else if (sourceWireB != "") {
					// Set to the value of wireA
					Integer wireB = this.getWireValue(sourceWireB);
					valueToSet = wireB;
				}
			} else {
				valueToSet = operatorNumber;
			}
		}
		else if (operator == this.MODE_AND) {
			Integer wireB = this.getWireValue(sourceWireB);

			if (sourceWireA != "") {
				Integer wireA = this.getWireValue(sourceWireA);
				valueToSet = (Integer) (wireA & wireB);
			} else {
				// If wireA is empty, then a "1" was passed in instead of a wire
				valueToSet = 1 & wireB;
			}
		} 
		else if (operator == this.MODE_OR) {
			Integer wireA = this.getWireValue(sourceWireA);
			Integer wireB = this.getWireValue(sourceWireB);
			valueToSet = (Integer) (wireA | wireB);
		} 
		else if (operator == this.MODE_LSHIFT) {
			Integer wireA = this.getWireValue(sourceWireA);
			valueToSet = (Integer) (wireA << operatorNumber);
		} 
		else if (operator == this.MODE_RSHIFT) {
			Integer wireA = this.getWireValue(sourceWireA);
			valueToSet = (Integer) (wireA >> operatorNumber);
		} 
		else if (operator == this.MODE_NOT) {
			/* Rather than use bitwise complement (~), which gives signed values, use the fact that
			 * each wire can carry a 16 bit signal (max value 65535) and simply subtract the current
			 * value from the max value.
			 */
			Integer wireB = this.getWireValue(sourceWireB);
			int bitwisecompl = ~wireB;
			bitwisecompl &= 0XFFFF;
			valueToSet = bitwisecompl;
		}

		// Apply the instruction using only 16 bit result
		valueToSet = valueToSet & 0xffff;
		this.setWireValue(destinationWire, valueToSet);
		return true;
	}

	public boolean wireIsReady(String wireName) {
		return this.wires.contains(wireName);
	}

	public void setWireValue(String wireName, Integer value) {
		// Only set a wire value once
		if (!this.wires.contains(wireName)) {
			this.wires.add(wireName);
			this.wireValues.add(value);
		}
	}

	public Integer getWireValue(String wireName) {
		int wireIndex = this.wires.indexOf(wireName);
		return this.wireValues.get(wireIndex);
	}

	public static void main(String[] args) {
		// Create puzzle instance
		Day7 puzzle = new Day7();

		// Part A
		puzzle.printWireValues();
		
		// Part B
		System.out.println("~~~~~~~~~~~~~~~~~~~PART B~~~~~~~~~~~~~~~~~~~~~~~");
		puzzle.runPartB();
	}

}
