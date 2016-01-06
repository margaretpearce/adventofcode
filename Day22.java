import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Day22 {

	private int bossDamage;
	private int bossHitPoints;

	private final int EFFECT_AREA_ARMOR = 0;
	private final int EFFECT_AREA_DEAL_DAMAGE = 1;
	private final int EFFECT_AREA_NEW_MANA = 2;

	private String[] spells = new String[]{"Magic Missile", "Drain", "Shield", "Poison", "Recharge"};
	private int[] spellCosts = new int[]{53, 73, 113, 173, 229};
	private int[] spellDamage = new int[]{4, 2, 0, 0, 0};
	private int[] spellHealing = new int[]{0, 2, 0, 0, 0};
	private int[] effectArea = new int[]{-1, -1, this.EFFECT_AREA_ARMOR, 
			this.EFFECT_AREA_DEAL_DAMAGE, this.EFFECT_AREA_NEW_MANA};
	private int[] effectDuration = new int[]{0, 0, 6, 6, 5};
	
	private PriorityQueue<Day22TurnState> turnQueue;

	public Day22() {
		this.readBossInputFile();
	}

	public void readBossInputFile() {
		BufferedReader buffReader = null;
		try{
			buffReader = new BufferedReader (new FileReader("day22.txt"));
			String line = buffReader.readLine();

			while(line != null) {
				if (line.contains("Hit Points")) {
					this.bossHitPoints = Integer.parseInt(line.split(" ")[2]);
				} else if (line.contains("Damage")) {
					this.bossDamage = Integer.parseInt(line.split(" ")[1]);
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

	public void tryCombinations() {
		int myStartingMana = 500;
		int myStartingHitPoints = 50;
		int myStartingArmor = 0;
		
		// Compare two turns by prioritizing ones with lower total mana spent
		Comparator<Day22TurnState> turnComparator = new Comparator<Day22TurnState>() {
			public int compare(Day22TurnState turnA, Day22TurnState turnB) {
				return Integer.valueOf(turnA.getTotalManaSpent()).compareTo(Integer.valueOf(turnB.getTotalManaSpent()));
			}
		};
		
		// Create a queue of turns
		this.turnQueue = new PriorityQueue<Day22TurnState>(10, turnComparator);
		
		// Add a starting state
		Day22TurnState startingState = new Day22TurnState(1, myStartingMana, myStartingHitPoints, myStartingArmor, 
				this.bossHitPoints, 0, "", null, null, false, false);
		turnQueue.add(startingState);
		
		while (!turnQueue.isEmpty()) {
			// Get the current turn info
			Day22TurnState currentTurn = turnQueue.remove();
			
			// Create instance of next turn
			Day22TurnState nextTurnState = new Day22TurnState(currentTurn.getTurnNumber() + 1);
			
			// Part B only
			// At the start of each player turn, lose one point
			if (currentTurn.getTurnNumber() % 2 == 1) {
				currentTurn.setHitPoints(currentTurn.getHitPoints() - 1);
			}
			
			// Check if this turn should already be over
			int previousTurnOutcome = this.checkIfGameHasEnded(currentTurn);
			
			if (previousTurnOutcome == -1) {
				// Boss won, go to the next turn state in the queue
				continue;
			} else if (previousTurnOutcome == 1) {
				// Current player won. Queue is sorted by mana spent, so this is the lowest spent overall
				System.out.println(currentTurn.getSpells());
				System.out.println(currentTurn.getTotalManaSpent());
				break;
			}
			
			// Apply any active effects from the previous turn to the new state
			nextTurnState = this.applySpellEffects(currentTurn, nextTurnState);
			
			// Check if this turn should continue
			int turnOutcome = this.checkIfGameHasEnded(nextTurnState);
			
			if (turnOutcome == -1) {
				// Boss won, go to the next turn state in the queue
				continue;
			} else if (turnOutcome == 1) {
				// Current player won. Queue is sorted by mana spent, so this is the lowest spent overall
				System.out.println(currentTurn.getSpells());
				System.out.println(currentTurn.getTotalManaSpent());
				break;
			} else {
				// Take the turn
				this.takeTurn(currentTurn, nextTurnState);
			}
		}
	}

	public int checkIfGameHasEnded(Day22TurnState turn) {
		if (turn.getBossPoints() <= 0 && turn.getMyBossPointsSet()) {
			return 1;
		} else if (turn.getHitPoints() <= 0 && turn.getMyTurnPointsSet()) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public Day22TurnState applySpellEffects(Day22TurnState previousTurn, Day22TurnState nextTurn) {
		// Get list of effects from the previous state
		LinkedList<Integer> effects = previousTurn.getActiveEffectNumbers();
		LinkedList<Integer> remainingTurns = previousTurn.getRemainingEffectTurns();
		
		LinkedList<Integer> newEffects = new LinkedList<Integer>();
		LinkedList<Integer> newRemainingTurns = new LinkedList<Integer>();
		
		nextTurn.setMyArmorPoints(0);
		
		if (effects != null && remainingTurns != null && effects.size() > 0 && remainingTurns.size() > 0) {
			// Apply effects one at a time
			for (int i = 0; i < effects.size(); i++) {
				int activeEffectNumber = effects.get(i);
				int remainingEffectTurnNumber = remainingTurns.get(i);

				if (activeEffectNumber != -1 && remainingEffectTurnNumber > 0) {
					// See what effect is active and apply it
					if (activeEffectNumber == 2) {
						nextTurn.setMyArmorPoints(7);
					}
					
					if (activeEffectNumber == 3) {
						nextTurn.setBossPoints(previousTurn.getBossPoints() - 3);
					} else if (activeEffectNumber == 4) {
						nextTurn.setMyMana(previousTurn.getMyMana() + 101);
					}

					// Decrease timer by one
					remainingEffectTurnNumber--;

					// If timer is zero, effect ends, otherwise continue through the next turn
					if (remainingEffectTurnNumber > 0) {
						newEffects.addLast(activeEffectNumber);
						newRemainingTurns.addLast(remainingEffectTurnNumber);
					}
				}
			}
		}
		
		// Save new effect information after processing current effects
		nextTurn.setActiveEffectNumbers(newEffects);
		nextTurn.setRemainingEffectTurns(newRemainingTurns);
		
		return nextTurn;
	}
	
	public void takeTurn(Day22TurnState previousTurn, Day22TurnState nextTurn) {
		if (previousTurn.getTurnNumber() % 2 == 1) {				
			// My turn, try all legal moves
			for (int i = 0; i < this.spells.length; i++) {
				// Copy effect information
				LinkedList<Integer> activeEffectsInNextTurn = 
						new LinkedList<Integer>(nextTurn.getActiveEffectNumbers());
				LinkedList<Integer> effectTurnsRemainingInNextTurn = 
						new LinkedList<Integer>(nextTurn.getRemainingEffectTurns());
				
				// If spell i isn't in play and there is enough mana for spell i
				if ((activeEffectsInNextTurn == null || (activeEffectsInNextTurn != null && !activeEffectsInNextTurn.contains(i)))
						&& previousTurn.getMyMana() >= this.spellCosts[i]) {
					// Try spell i
					// Only set my mana if it wasn't adjusted already by a spell
					int newMana;
					
					LinkedList<Integer> previousTurnSpells = previousTurn.getActiveEffectNumbers();
					// If mana has already been set, use the set mana value
					if (previousTurnSpells != null && previousTurnSpells.contains(4)) {
						newMana = nextTurn.getMyMana() - this.spellCosts[i];
					} else {
						newMana = previousTurn.getMyMana() - this.spellCosts[i];
					}
					
					// Set boss points to the previous turn's amount unless a spell was applied
					int newBossPoints;
					if (!nextTurn.getMyBossPointsSet()) {
						newBossPoints = previousTurn.getBossPoints() - this.spellDamage[i];
					} else {
						newBossPoints = nextTurn.getBossPoints() - this.spellDamage[i];
					}
					
					int newHitPoints = previousTurn.getHitPoints() + this.spellHealing[i];
					int newManaSpent = previousTurn.getTotalManaSpent() + this.spellCosts[i];
					String newSpellCast = previousTurn.getSpells() + "-" + this.spells[i];
					
					// Handle spell effects (current spell effects + carry over from previous turns)
					if (this.effectArea[i] >= 0) {
						activeEffectsInNextTurn.addLast(i);
						effectTurnsRemainingInNextTurn.addLast(this.effectDuration[i]);
					}
					
					// Save results of this turn (armor is set only in spell effects)
					Day22TurnState nextTurnSpellOption = new Day22TurnState(nextTurn);
					nextTurnSpellOption.setMyMana(newMana);
					nextTurnSpellOption.setHitPoints(newHitPoints);
					nextTurnSpellOption.setBossPoints(newBossPoints);
					nextTurnSpellOption.setTotalManaSpent(newManaSpent);
					nextTurnSpellOption.setSpells(newSpellCast);
					nextTurnSpellOption.setActiveEffectNumbers(activeEffectsInNextTurn);
					nextTurnSpellOption.setRemainingEffectTurns(effectTurnsRemainingInNextTurn);
					
					// Add the next turn to the queue
					this.turnQueue.add(nextTurnSpellOption);
				}
			}
		} else {
			// Boss turn
			int damageToDeal = this.bossDamage - nextTurn.getMyArmorPoints();
			if (damageToDeal < 0) {
				damageToDeal = 1;
			}
			
			int newHitPoints = previousTurn.getHitPoints() - damageToDeal;
			
			// Set factors that don't change on the boss's turn. 
			// No need to set spell effect info - this is handled earlier and no new spells cast in boss turn
			nextTurn.setTotalManaSpent(previousTurn.getTotalManaSpent());
			nextTurn.setSpells(previousTurn.getSpells());
			
			// Only set my mana if it wasn't adjusted already by Recharge
			LinkedList<Integer> previousTurnSpells = previousTurn.getActiveEffectNumbers();
			if (previousTurnSpells == null || (previousTurnSpells != null && !previousTurnSpells.contains(4))) {
				nextTurn.setMyMana(previousTurn.getMyMana());
			}
			
			// Set boss points to the previous turn's amount unless a spell was applied
			if (!nextTurn.getMyBossPointsSet()) {
				nextTurn.setBossPoints(previousTurn.getBossPoints());
			}
			
			// Set factors that do change on the boss's turn (hit points)
			nextTurn.setHitPoints(newHitPoints);
			
			// Add the next turn to the queue
			this.turnQueue.add(nextTurn);
		}
	}
	
	public static void main(String[] args) {
		Day22 puzzle = new Day22();
		puzzle.tryCombinations();
	}
}
