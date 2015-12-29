import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Day8 {
	
	private ArrayList<String> input;
	private int numCharsLiteral;
	private int numCharsString;
	private int numCharsEncoded;
	
	public Day8() {
		this.numCharsLiteral = 0;
		this.numCharsString = 0;
		this.numCharsEncoded = 0;
		this.input = new ArrayList<String>();
		this.readInputFile();
	}
	
	public void readInputFile() {
		BufferedReader buffReader = null;
		try{
			buffReader = new BufferedReader (new FileReader("day8.txt"));
			String line = buffReader.readLine();
			while(line != null){
				// Store each instruction
				this.input.add(line.trim());
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

	public void printDifferenceInSize() {
		this.getDifferenceInSize();
		System.out.println(this.numCharsLiteral - this.numCharsString);
		System.out.println(this.numCharsEncoded - this.numCharsLiteral);
	}
	
	public void getDifferenceInSize() {
		for (int i = 0; i < this.input.size(); i++) {
			String listItemOriginal = this.input.get(i);
			this.numCharsLiteral += listItemOriginal.length();
			
			// Replace escaped values with *
			String listItem = listItemOriginal;
			listItem = listItem.replaceAll("\\\\{2}", "*");						// Replace '\\' with *
			listItem = listItem.replaceAll("\\\\\"", "*");						// Replace '\"' with *
			listItem = listItem.replaceAll("\\\\[x]{1}[0-9A-Fa-f]{2}", "*");	// Replace '\x__' with *
			listItem = listItem.replaceAll("\"", "");							// Remove quotes
			this.numCharsString += listItem.length();
			
			// Encode quotes
			String listItemEncoded = listItemOriginal;
			listItemEncoded = listItemEncoded.replaceAll("\\\\", "\\\\\\\\");	// Change '\' to '\\'
			listItemEncoded = listItemEncoded.replaceAll("\"", "\\\\\"");		// Change '"' to '\"'
			listItemEncoded = "\"" + listItemEncoded + "\"";					// Add quotes to the ends
			this.numCharsEncoded += listItemEncoded.length();
		}
	}
	
	public static void main(String[] args) {
		Day8 puzzle = new Day8();
		puzzle.printDifferenceInSize();
	}

}
