
public class Day20 {

	private int numPresents;
	
	public Day20(String input) {
		this.numPresents = Integer.parseInt(input);
	}
	
	public void findHouseNumberWithPresents() {
		/* Create an array to store the number of presents delivered to each house
		* Worst case: the house with sufficient presents will be visited by elf [numPresents/10], 
		* who will deliver (numPresents/10)*10 = |numPresents| presents to the house.
		*/
		int maxHouses = this.numPresents / 10;
		int[] houses = new int[maxHouses];
		
		// Figure out how many presents are delivered to each house
		for (int i = 1; i < maxHouses; i++) {
			// Increment the number of presents for each house j that elf i visits (i, 2i, 3i, ..)
			for (int j = i; j < maxHouses; j+= i) {
				houses[j] += i*10;
			}
		}
		
		// Look for the first house with enough presents
		for (int i = 1; i < maxHouses; i++) {
			if (houses[i] >= this.numPresents) {
				System.out.println(i);
				break;
			}
		}
	}
	
	public void findHouseWithPresentsPartB() {
		int maxHouses = this.numPresents / 11;
		int[] houses = new int[maxHouses];
		
		// Figure out how many presents are delivered to each house
		for (int i = 1; i < maxHouses; i++) {
			int numHousesVisited = 0;
			
			// Increment the number of presents for each house j that elf i visits (i, 2i, 3i, ..)
			for (int j = i; j < maxHouses; j+= i) {
				houses[j] += i*11;
				numHousesVisited++;
				
				// Stop delivering presents after 50 houses
				if (numHousesVisited == 50) {
					break;
				}
			}
		}
		
		// Look for the first house with enough presents
		for (int i = 1; i < maxHouses; i++) {
			if (houses[i] >= this.numPresents) {
				System.out.println(i);
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		Day20 puzzle = new Day20(args[0]);
		puzzle.findHouseNumberWithPresents();
		puzzle.findHouseWithPresentsPartB();
	}

}
