import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Day21 {

	private String[] weaponNames = new String[5];
	private int[][] weapons = new int[5][3];
	private String[] armorNames = new String[5];
	private int[][] armor = new int[5][3];
	private String[] ringNames = new String[6];
	private int[][] rings = new int[6][3];

	private int myHitPoints = 100;
	private int myDamage = 0;
	private int myArmor = 0;

	private int bossHitPoints;
	private int bossDamage;
	private int bossArmor;

	private final int ITEM_COST = 0;
	private final int ITEM_DAMAGE = 1;
	private final int ITEM_ARMOR = 2;

	public Day21() {
		this.readShopInputFile();
		this.readBossInputFile();
	}

	public void readShopInputFile() {
		BufferedReader buffReader = null;
		try{
			buffReader = new BufferedReader (new FileReader("day21_shop.txt"));
			String line = buffReader.readLine();
			int mode = -1;
			int counter = 0;

			while(line != null) {
				if (line.length() < 1) {
					// Skip empty lines
				} else if (line.contains("Weapons:")) {
					mode = 0;
					counter = 0;
				} else if (line.contains("Armor:")) {
					mode = 1;
					counter = 0;
				} else if (line.contains("Rings:")) {
					mode = 2;
					counter = 0;
				} else {
					String[] lineItems = line.split(" +");
					if (mode == 0) {
						// Parse weapon info
						this.weaponNames[counter] = lineItems[0];					// Name
						this.weapons[counter][0] = Integer.parseInt(lineItems[1]);	// Cost
						this.weapons[counter][1] = Integer.parseInt(lineItems[2]);	// Damage
						this.weapons[counter][2] = Integer.parseInt(lineItems[3]);	// Armor
						counter++;
					} else if (mode == 1) {
						// Parse armor info
						this.armorNames[counter] = lineItems[0];					// Name
						this.armor[counter][0] = Integer.parseInt(lineItems[1]);	// Cost
						this.armor[counter][1] = Integer.parseInt(lineItems[2]);	// Damage
						this.armor[counter][2] = Integer.parseInt(lineItems[3]);	// Armor
						counter++;
					} else if (mode == 2) {
						// Parse ring info
						this.ringNames[counter] = lineItems[0];						// Name
						this.rings[counter][0] = Integer.parseInt(lineItems[2]);	// Cost
						this.rings[counter][1] = Integer.parseInt(lineItems[3]);	// Damage
						this.rings[counter][2] = Integer.parseInt(lineItems[4]);	// Armor
						counter++;
					}
				}
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

	public void readBossInputFile() {
		BufferedReader buffReader = null;
		try{
			buffReader = new BufferedReader (new FileReader("day21.txt"));
			String line = buffReader.readLine();

			while(line != null) {
				if (line.contains("Hit Points")) {
					this.bossHitPoints = Integer.parseInt(line.split(" ")[2]);
				} else if (line.contains("Damage")) {
					this.bossDamage = Integer.parseInt(line.split(" ")[1]);
				} else if (line.contains("Armor")) {
					this.bossArmor = Integer.parseInt(line.split(" ")[1]);
				}
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

	public void tryBuyingCombinations() {
		int numDamage = this.myDamage;
		int numArmor = this.myArmor;
		int numCost = 0;

		int minCost = Integer.MAX_VALUE;
		int maxCost = Integer.MIN_VALUE;

		for (int i = 0; i < this.weaponNames.length; i++) {
			for (int j = -1; j < this.armorNames.length; j++) {
				for (int k = -1; k < this.ringNames.length; k++) {
					for (int l = k; l < this.ringNames.length; l++) {
						// Buy weapon
						numCost += this.weapons[i][this.ITEM_COST];
						numDamage += this.weapons[i][this.ITEM_DAMAGE];
						
						// Optionally buy armor
						if (j >= 0) {
							numCost += this.armor[j][this.ITEM_COST];
							numArmor += this.armor[j][this.ITEM_ARMOR];
						}
						
						// Optionally buy one ring
						if (k >= 0) {
							numCost += this.rings[k][this.ITEM_COST];
							numDamage += this.rings[k][this.ITEM_DAMAGE];
							numArmor += this.rings[k][this.ITEM_ARMOR];
							
							// Optionally buy two rings
							if (l > k) {
								numCost += this.rings[l][this.ITEM_COST];
								numDamage += this.rings[l][this.ITEM_DAMAGE];
								numArmor += this.rings[l][this.ITEM_ARMOR];
							}
						}
						
						// Check if this combination wins
						if (this.bossLosesInScenario(numDamage, numArmor)) {
							if (numCost < minCost) {
								minCost = numCost;
							}
						} else {
							if (numCost > maxCost) {
								maxCost = numCost;
							}
						}
						
						// Reset values
						numCost = 0;
						numDamage = 0;
						numArmor = 0;
					}
				}
			}
		}

		System.out.println("Min cost: " + minCost);
		System.out.println("Max cost: " + maxCost);
	}

	public boolean bossLosesInScenario(int myDamage, int myArmor) {
		int myHitPoints = this.myHitPoints;
		int bossHitPoints = this.bossHitPoints;
		int turnNumber = 1;
		
		int damageDealt = 1;
		
		while (myHitPoints > 0 && bossHitPoints > 0) {
			if (turnNumber % 2 == 1) {
				// My turn
				damageDealt = myDamage - this.bossArmor;
				
				if (damageDealt < 0) {
					damageDealt = 1;
				}
				
				bossHitPoints = bossHitPoints - damageDealt;
			} else {
				// Boss's turn
				damageDealt = this.bossDamage - myArmor;
				
				if (damageDealt < 0) {
					damageDealt = 1;
				}
				
				myHitPoints = myHitPoints - damageDealt;
			}
			
			// Next turn
			turnNumber++;
		}
		
		// One player is down to 0 hit points
		return myHitPoints > 0;
	}

	public static void main(String[] args) {
		Day21 puzzle = new Day21();
		puzzle.tryBuyingCombinations();
	}

}
