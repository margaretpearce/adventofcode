import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {
	
	private String input;
	private int numHouses;
	private int numHousesRoboSanta;
	private HashMap<Integer, ArrayList<Integer>> housesVisited;
	private HashMap<Integer, ArrayList<Integer>> housesVisitedRobotSanta;
	
	public Day3(String input) {
		if (this.inputIsValid(input)) {
			this.input = input;
		}
		
		// Initialize map of visited houses as (x,y) to contain only (0,0)
		this.housesVisited = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<Integer> initialY = new ArrayList<Integer>();
		initialY.add(0);
		this.housesVisited.put(0, initialY);
		
		this.housesVisitedRobotSanta = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<Integer> initialYRobotSanta = new ArrayList<Integer>();
		initialYRobotSanta.add(0);
		this.housesVisitedRobotSanta.put(0, initialYRobotSanta);
		
		// Initialize the number of visited houses. Set to 1 to include (0,0).
		this.numHouses = 1;
		this.numHousesRoboSanta = 1;
	}
	
	/*
	 * Input should only contain "^", ">", "v", and "<"
	 */
	public boolean inputIsValid(String input) {
		Pattern pattern = Pattern.compile("^[(\\^|>|v|<)]+$");
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}
	
	public void printNumberOfHousesVisited() {
		if (this.input != null) {
			this.processInput();
		}
		
		System.out.println(this.numHouses);
		System.out.println(this.numHousesRoboSanta);
	}
	
	public void processInput() {
		// Start at (0,0)
		int x = 0;
		int y = 0;
		
		int xSanta = 0;
		int ySanta = 0;
		int xRobot = 0;
		int yRobot = 0;
		
		// Process one character at a time to go up or down a floor
		for (int i = 0; i < this.input.length(); i++) {
			char direction = this.input.charAt(i);
			
			// Get new coordinates
			if (direction == '^') {
				y += 1;
				
				if (i % 2 == 1) {
					ySanta += 1;
				} else {
					yRobot += 1;
				}
			}
			else if (direction == '>') {
				x += 1;
				
				if (i % 2 == 1) {
					xSanta += 1;
				} else {
					xRobot += 1;
				}
			}
			else if (direction == 'v') {
				y -= 1;
				
				if (i % 2 == 1) {
					ySanta -= 1;
				} else {
					yRobot -= 1;
				}
			}
			else if (direction == '<') {
				x -= 1;
				
				if (i % 2 == 1) {
					xSanta -= 1;
				} else {
					xRobot -= 1;
				}
			}
			
			// Update the visited locations and number of houses visited
			if (this.addCoordinateAndCheckIfNew(x, y)) {
				this.numHouses++;
			}
			
			// Do the same thing for the Robot-Santa team
			if (i % 2 == 1) {
				if (this.addCoordinateAndCheckIfNewRobotSanta(xSanta, ySanta)) {
					this.numHousesRoboSanta++;
				}
			}
			else {
				if (this.addCoordinateAndCheckIfNewRobotSanta(xRobot, yRobot)) {
					this.numHousesRoboSanta++;
				}
			}
		}
	}
	
	public boolean addCoordinateAndCheckIfNew(int x, int y) {
		boolean newHouse = true;
		
		if (this.housesVisited.containsKey(x)) {
			// Get list of y coordinates for current x
			ArrayList<Integer> yCoordinates = this.housesVisited.get(x);
			
			// Check if the current y is already in the list
			if (yCoordinates.contains(y)) {
				newHouse = false;
			}
			
			// Add the y coordinate to the list for x
			yCoordinates.add(y);
			this.housesVisited.remove(x);
			this.housesVisited.put(x, yCoordinates);
		}
		else {
			// Add x and y coordinates
			ArrayList<Integer> yCoordinates = new ArrayList<Integer>();
			yCoordinates.add(y);
			this.housesVisited.put(x, yCoordinates);
		}
		
		return newHouse;
	}
	
	public boolean addCoordinateAndCheckIfNewRobotSanta(int x, int y) {
		boolean newHouse = true;
		
		if (this.housesVisitedRobotSanta.containsKey(x)) {
			// Get list of y coordinates for current x
			ArrayList<Integer> yCoordinates = this.housesVisitedRobotSanta.get(x);
			
			// Check if the current y is already in the list
			if (yCoordinates.contains(y)) {
				newHouse = false;
			}
			
			// Add the y coordinate to the list for x
			yCoordinates.add(y);
			this.housesVisitedRobotSanta.remove(x);
			this.housesVisitedRobotSanta.put(x, yCoordinates);
		}
		else {
			// Add x and y coordinates
			ArrayList<Integer> yCoordinates = new ArrayList<Integer>();
			yCoordinates.add(y);
			this.housesVisitedRobotSanta.put(x, yCoordinates);
		}
		
		return newHouse;
	}
	
	public static void main(String[] args) {
		// Get puzzle input from command line
		if (args.length == 1) {
			String input = args[0];
			
			// Create puzzle instance
			Day3 puzzle = new Day3(input);
			
			// Print the number of houses visited at least once
			puzzle.printNumberOfHousesVisited();
		}
	}
}
