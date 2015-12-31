import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.*;

public class Day12 {

	private String[] input;
	private int sum;

	public Day12(String[] input) {
		this.input = input;
		this.sum = 0;
	}

	public void readInput() {
		Pattern p = Pattern.compile("-?(\\d+)");

		for (int i = 0; i < input.length; i++) {
			// Look for numbers in this string
			Matcher m = p.matcher(input[i]);

			// If a number is found, add it to the sum
			while (m.find()) {
				this.sum += Integer.parseInt(m.group());
			}
		}
	}

	public void printSum() {
		System.out.println(this.sum);
	}

	public void readInputPartB() {
		// Now handle the input as JSON for easy parsing
		for (int i = 0; i < input.length; i++) {
			Object jarray = new JSONArray(input[i]);
			
			// Remove JSON objects with value:"red"
			this.input[i] = this.parseJsonPartB(jarray);
		}
		
		// Reset sum and calculate the new value of the array
		this.sum = 0;
		this.readInput();
	}
	
	public String parseJsonPartB(Object object) {
		// Base case: we've reached a JSON value for a named entity
		if (object instanceof Integer || object instanceof String) { 
			return object.toString() + "\n";
		}
		else if (object instanceof JSONObject) {
			// If this is a JSON object, look for instances of value:"red"
			JSONObject jsonObj = (JSONObject) object;
			JSONArray objectNames = jsonObj.names();
			String tmp = "";
			
			// Loop through each object name
			for (int k = 0; k < objectNames.length(); k++) {
				String objectName = (String) objectNames.get(k);
				 
				// If the value "red" is found
				if (jsonObj.get(objectName).equals("red")) {
					// ...skip the entire object
					return "";
				} 
				else {
					// Otherwise, process the named value
					tmp += this.parseJsonPartB(jsonObj.get(objectName)) + "\n";
				}
			}
			
			// If value:"red" is never found, return the processed object
			return tmp;
		} 
		else if (object instanceof JSONArray) {
			JSONArray jarray = (JSONArray) object;
			String tmp = "";
			
			// Look for JSON objects to remove within the array
			for (int k = 0; k < jarray.length(); k++) {
				tmp += this.parseJsonPartB(jarray.get(k)) + "\n";
			}
			
			return tmp;
		}
		else {
			return "";
		}
	}
	
	public static void main(String[] args) {
		Day12 puzzle = new Day12(args);
		
		// Part A
		puzzle.readInput();
		puzzle.printSum();

		// Part B
		puzzle.readInputPartB();
		puzzle.printSum();
	}

}
