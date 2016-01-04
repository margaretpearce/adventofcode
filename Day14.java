import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Day14 {

	private int numSecondsToRace;
	private ArrayList<String> reindeer;
	private ArrayList<Integer> speed;
	private ArrayList<Integer> timeLimit;
	private ArrayList<Integer> restTime;
	private ArrayList<Integer> distanceTraveled;
	private int[] points;
	
	public Day14(int seconds) {
		this.numSecondsToRace = seconds;
		this.reindeer = new ArrayList<String>();
		this.speed = new ArrayList<Integer>();
		this.timeLimit = new ArrayList<Integer>();
		this.restTime = new ArrayList<Integer>();
		this.distanceTraveled = new ArrayList<Integer>();
		this.readInputFile();
		
		this.points = new int[this.reindeer.size()];
	}
	
	public void readInputFile() {
		BufferedReader buffReader = null;
		try{
			buffReader = new BufferedReader (new FileReader("day14.txt"));
			String line = buffReader.readLine();
			while(line != null){
				this.processLine(line);
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
	
	public void processLine(String line) {
		// Get reindeer names, speed, time limit, and rest time
		String[] lineItems = line.split(" ");
		this.reindeer.add(lineItems[0]);
		this.speed.add(Integer.parseInt(lineItems[3]));
		this.timeLimit.add(Integer.parseInt(lineItems[6]));
		this.restTime.add(Integer.parseInt(lineItems[13]));
	}
	
	public void getDistancesAfterTime() {
		for (int i = 0; i < this.reindeer.size(); i++) {
			int distance = 0;
			int timeLeft = this.numSecondsToRace;
			
			while (timeLeft > 0) {
				// Fly for max distance
				if (timeLeft > this.timeLimit.get(i)) {
					distance += this.speed.get(i) * this.timeLimit.get(i);
					timeLeft -= this.timeLimit.get(i);
					
					// Rest
					timeLeft -= this.restTime.get(i);
				} else {
					// Fly for the remaining amount of time left
					distance += this.speed.get(i) * timeLeft;
					timeLeft = 0;
				}
			}
			
			// Save the total distance travelled
			this.distanceTraveled.add(distance);
		}
	}
	
	public void getPointsThroughoutRace() {
		boolean[][] reindeerMoving = new boolean[this.reindeer.size()][this.numSecondsToRace];
		int[][] distanceBySecond = new int[this.reindeer.size()][this.numSecondsToRace];
		
		// Figure out when each reindeer should be moving
		for (int i = 0; i < this.reindeer.size(); i++) {
			int currentTime = 0;
			
			while (currentTime < this.numSecondsToRace) {
				// Reindeer i should start by moving
				int timeToMoveFor = currentTime + this.timeLimit.get(i);
				
				for (int j = currentTime; j < timeToMoveFor; j++) {
					if (j < this.numSecondsToRace) {
						reindeerMoving[i][j] = true;
						currentTime++;
					}
				}
				
				// After moving, go to resting
				int timeToRestFor = currentTime + this.restTime.get(i);
				
				for (int j = currentTime; j < timeToRestFor; j++) {
					if (j < this.numSecondsToRace) {
						reindeerMoving[i][j] = false;
						currentTime++;
					}
				}
			}
		}
		
		// Look at each second and figure out distances, then award points
		for (int t = 0; t < this.numSecondsToRace; t++) {
			int reindeerInLead = -1;
			int maxDistance = Integer.MIN_VALUE;
			
			for (int r = 0; r < this.reindeer.size(); r++) {
				// Get the previous distance travelled
				int previousDistance = 0;
					
				if (t > 0) {
					previousDistance = distanceBySecond[r][t-1];
				}
				
				// Figure out the new distance by checking whether or not the reindeer is flying
				if (reindeerMoving[r][t]) {	
					distanceBySecond[r][t] = previousDistance + this.speed.get(r);
				}
				else {
					distanceBySecond[r][t] = previousDistance;
				}
				
				// Check to see if this reindeer is in the lead
				if (distanceBySecond[r][t] > maxDistance) {
					maxDistance = distanceBySecond[r][t];
					reindeerInLead = r;
				}
			}
			
			// Award a point to the lead reindeer at time t
			this.points[reindeerInLead]++;
		}
	}
	
	public void printWinningReindeer() {
		int maxDistance = Integer.MIN_VALUE;
		int winningReindeer = -1;
		
		for (int i = 0; i < this.reindeer.size(); i++) {
			if (this.distanceTraveled.get(i) > maxDistance) {
				maxDistance = this.distanceTraveled.get(i);
				winningReindeer = i;
			}
		}
		
		System.out.println("Distance - " + this.reindeer.get(winningReindeer) + ": " + maxDistance);
	}
	
	public void printWinningReindeerByPoints() {
		int maxPoints = Integer.MIN_VALUE;
		int winningReindeer = -1;
		
		for (int i = 0; i < this.reindeer.size(); i++) {
			if (this.points[i] > maxPoints) {
				maxPoints = this.points[i];
				winningReindeer = i;
			}
		}
		
		System.out.println("Points - " + this.reindeer.get(winningReindeer) + ": " + maxPoints);
	}
	
	public static void main(String[] args) {
		int numSecondsToRace = Integer.parseInt(args[0]);
		Day14 puzzle = new Day14(numSecondsToRace);
		puzzle.getDistancesAfterTime();
		puzzle.printWinningReindeer();
		
		puzzle.getPointsThroughoutRace();
		puzzle.printWinningReindeerByPoints();
	}

}
