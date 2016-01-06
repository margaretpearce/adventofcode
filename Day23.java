import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Day23 {

	private String[] registerNames;
	private int[] registerValues;
	private ArrayList<String> instructions;
	
	public Day23(String[] registerNames) {
		this.registerNames = registerNames;
		this.registerValues = new int[]{0, 0};
		this.instructions = new ArrayList<String>();
		this.readInputFile();
	}
	
	public void readInputFile() {
		BufferedReader buffReader = null;
		try{
			buffReader = new BufferedReader (new FileReader("day23.txt"));
			String line = buffReader.readLine();
			while(line != null) {
				this.instructions.add(line);
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
	
	public void findRegisterValues() {
		final String INSTRUCTION_HALF = "hlf";
		final String INSTRUCTION_TRIPLE = "tpl";
		final String INSTRUCTION_INCREMENT = "inc";
		final String INSTRUCTION_JUMP = "jmp";
		final String INSTRUCTION_EVENJUMP = "jie";
		final String INSTRUCTION_ONEJUMP = "jio";
		
		int currentInstruction = 0;
		
		// Process one instruction at a time
		while (currentInstruction < this.instructions.size()) {
			String[] instruction = this.instructions.get(currentInstruction).split(" ");
			
			// Get the instruction type
			if (instruction[0].equals(INSTRUCTION_HALF)) {
				// Half the register value, then continue with next instruction
				for (int j = 0; j < this.registerNames.length; j++) {
					if (this.registerNames[j].equals(instruction[1])) {
						this.registerValues[j] = this.registerValues[j] / 2;
						break;
					}
				}
				currentInstruction++;
			} 
			else if (instruction[0].equals(INSTRUCTION_TRIPLE)) {
				// Triple the register value, then continue with the next instruction
				for (int j = 0; j < this.registerNames.length; j++) {
					if (this.registerNames[j].equals(instruction[1])) {
						this.registerValues[j] = this.registerValues[j] * 3;
						break;
					}
				}
				currentInstruction++;
			} 
			else if (instruction[0].equals(INSTRUCTION_INCREMENT)) {
				// Increment the register value by 1, then continue with the next instruction
				for (int j = 0; j < this.registerNames.length; j++) {
					if (this.registerNames[j].equals(instruction[1])) {
						this.registerValues[j] = this.registerValues[j] + 1;
						break;
					}
				}
				currentInstruction++;
			} 
			else if (instruction[0].equals(INSTRUCTION_JUMP)) {
				// Continue with instruction offset away
				int offsetAmount = Integer.parseInt(instruction[1]);
				currentInstruction += offsetAmount;
			} 
			else if (instruction[0].equals(INSTRUCTION_EVENJUMP)) {
				int offsetAmount = Integer.parseInt(instruction[2]);
				String instructionRegister = instruction[1].replace(",", "");
				
				// Offset only if the register given is even
				for (int j = 0; j < this.registerNames.length; j++) {
					if (this.registerNames[j].equals(instructionRegister)) {
						// Check if the register is even
						if (this.registerValues[j] % 2 == 0) {
							currentInstruction += offsetAmount;
						} else {
							// If not even, ignore this instruction and continue on
							currentInstruction++;
						}
						
						break;
					}
				}
			} 
			else if (instruction[0].equals(INSTRUCTION_ONEJUMP)) {
				int offsetAmount = Integer.parseInt(instruction[2]);
				String instructionRegister = instruction[1].replace(",", "");
				
				// Offset only if the register given is equal to 1
				for (int j = 0; j < this.registerNames.length; j++) {
					if (this.registerNames[j].equals(instructionRegister)) {
						// Check if the register is 1
						if (this.registerValues[j] == 1) {
							currentInstruction += offsetAmount;
						} else {
							// If not even, ignore this instruction and continue on
							currentInstruction++;
						}
						
						break;
					}
				}
			}
		}
	}
	
	public void printRegisterValues() {
		for (int i = 0; i < this.registerNames.length; i++) {
			System.out.println("Register " + this.registerNames[i] + ": " + this.registerValues[i]);
		}
	}
	
	public void partBSetup() {
		// Set register a to "1", reset all others to 0
		for (int i = 0; i < this.registerNames.length; i++) {
			if (this.registerNames[i].equals("a")) {
				this.registerValues[i] = 1;
			} else {
				this.registerValues[i] = 0;
			}
		}
	}
	
	public static void main(String[] args) {
		Day23 puzzle = new Day23(args);
		puzzle.findRegisterValues();
		puzzle.printRegisterValues();
		
		puzzle.partBSetup();
		puzzle.findRegisterValues();
		puzzle.printRegisterValues();
	}

}
