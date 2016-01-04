import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Day16 {

	private ArrayList<String> giftProperties;
	private int maxIndex;
	private int maxValue;
	
	private int maxIndexPartB;
	private int maxValuePartB;
	
	public Day16() {
		this.giftProperties = new ArrayList<String>();
		this.maxIndex = -1;
		this.maxValue = Integer.MIN_VALUE;
		this.maxIndexPartB = -1;
		this.maxValuePartB = Integer.MIN_VALUE;
	}
	
	public void findAunt() {
		this.readGiftProperties();
		this.readInputFile();
		
		System.out.println("Part A: The gift is from Sue #" + (this.maxIndex+1));
		System.out.println("Part B: The gift is from Sue #" + (this.maxIndexPartB+1));
	}
	
	public void readGiftProperties() {
		BufferedReader buffReader = null;
		try{
			buffReader = new BufferedReader (new FileReader("day16_gift.txt"));
			String line = buffReader.readLine();
			while(line != null){
				this.giftProperties.add(line);
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
	
	public void readInputFile() {
		BufferedReader buffReader = null;
		try{
			buffReader = new BufferedReader (new FileReader("day16.txt"));
			int auntNumber = 0;
			String line = buffReader.readLine();
			
			while(line != null) {
				this.processLine(line, auntNumber);
				this.processLineWithRanges(line, auntNumber);
				
				auntNumber++;
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
	
	public void processLine(String line, int auntNumber) {
		int numMatches = 0;
		
		for (int i = 0; i < this.giftProperties.size(); i++) {
			if (line.contains(this.giftProperties.get(i))) {
				numMatches++;
			}
		}
				
		if (numMatches > this.maxValue) {
			this.maxValue = numMatches;
			this.maxIndex = auntNumber;
		}
	}
	
	public void processLineWithRanges(String line, int auntNumber) {
		int numMatches = 0;
		
		for (int i = 0; i < this.giftProperties.size(); i++) {
			// Split the gift property into property: value
			String propertyAndValue[] = this.giftProperties.get(i).split(":");
			String propertyName = propertyAndValue[0].trim();
			int propertyValue = Integer.parseInt(propertyAndValue[1].trim());
			
			if (line.contains(propertyName)) {
				// Get the property value for this line
				int indexOfPropertyName = line.indexOf(propertyName) + propertyName.length() + 2;
				int stopIndex = line.indexOf(" ", indexOfPropertyName);
				
				// If no ending space is found, then this property is the end of the line
				if (stopIndex == -1) {
					stopIndex = line.length();
				}
				
				String linePropertyValueString = line.substring(indexOfPropertyName, stopIndex).replaceAll(",", "");
				
				int linePropertyValue = Integer.parseInt(linePropertyValueString);
				
				if ((propertyName.equalsIgnoreCase("cats") || propertyName.equalsIgnoreCase("trees")) && linePropertyValue > propertyValue) {
					numMatches++;
				} else if ((propertyName.equalsIgnoreCase("pomeranians") || propertyName.equalsIgnoreCase("goldfish")) 
						&& linePropertyValue < propertyValue) {
					numMatches++;
				} else if (linePropertyValue == propertyValue){
					numMatches++;
				}
			}
		}
				
		if (numMatches > this.maxValuePartB) {
			this.maxValuePartB = numMatches;
			this.maxIndexPartB = auntNumber;
		}
	}
	
	public static void main(String[] args) {
		Day16 puzzle = new Day16();
		puzzle.findAunt();
	}

}
