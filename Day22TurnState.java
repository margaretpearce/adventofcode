import java.util.LinkedList;


public class Day22TurnState {
	private boolean myTurnPointsSet = false;
	private boolean bossTurnPointsSet = false;
	private int turnNumber;
	private int myMana;
	private int hitPoints;
	private int myArmorPoints;
	private int bossPoints;
	private int totalManaSpent;
	private String spells;
	private LinkedList<Integer> activeEffectNumbers;
	private LinkedList<Integer> remainingEffectTurns;
	
	public Day22TurnState(int turn) {
		this.turnNumber = turn;
		this.myTurnPointsSet = false;
		this.bossTurnPointsSet = false;
	}
	
	public Day22TurnState(Day22TurnState stateToCopy) {
		this.turnNumber = stateToCopy.turnNumber;
		this.myMana = stateToCopy.myMana;
		this.hitPoints = stateToCopy.hitPoints;
		this.myArmorPoints = stateToCopy.myArmorPoints;
		this.bossPoints = stateToCopy.bossPoints;
		this.totalManaSpent = stateToCopy.totalManaSpent;
		this.spells = stateToCopy.spells;
		this.activeEffectNumbers = new LinkedList<Integer>(stateToCopy.activeEffectNumbers);
		this.remainingEffectTurns = new LinkedList<Integer>(stateToCopy.remainingEffectTurns);
		this.myTurnPointsSet = true;
		this.bossTurnPointsSet = true;
	}
	
	public Day22TurnState(int turn, int mana, int hitPoints, int armorPoints, int bossPoints, 
			int totalMana, String spells, LinkedList<Integer> activeEffects, LinkedList<Integer> remainingEffects,
			boolean myTurnPointsSet, boolean bossTurnPointsSet) {
		this.turnNumber = turn;
		this.myMana = mana;
		this.hitPoints = hitPoints;
		this.myArmorPoints = armorPoints;
		this.bossPoints = bossPoints;
		this.totalManaSpent = totalMana;
		this.spells = spells;
		this.activeEffectNumbers = activeEffects;
		this.remainingEffectTurns = remainingEffects;
		this.myTurnPointsSet = myTurnPointsSet;
		this.bossTurnPointsSet = bossTurnPointsSet;
	}

	public boolean getMyTurnPointsSet() {
		return myTurnPointsSet;
	}
	
	public boolean getMyBossPointsSet() {
		return bossTurnPointsSet;
	}
	
	public int getTurnNumber() {
		return turnNumber;
	}

	public void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
	}

	public int getMyMana() {
		return myMana;
	}

	public void setMyMana(int myMana) {
		this.myMana = myMana;
	}

	public int getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(int hitPoints) {
		this.myTurnPointsSet = true;
		this.hitPoints = hitPoints;
	}

	public int getMyArmorPoints() {
		return myArmorPoints;
	}

	public void setMyArmorPoints(int myArmorPoints) {
		this.myArmorPoints = myArmorPoints;
	}

	public int getBossPoints() {
		return bossPoints;
	}

	public void setBossPoints(int bossPoints) {
		this.bossTurnPointsSet = true;
		this.bossPoints = bossPoints;
	}

	public int getTotalManaSpent() {
		return totalManaSpent;
	}

	public void setTotalManaSpent(int totalManaSpent) {
		this.totalManaSpent = totalManaSpent;
	}

	public String getSpells() {
		return spells;
	}

	public void setSpells(String spells) {
		this.spells = spells;
	}

	public LinkedList<Integer> getActiveEffectNumbers() {
		return activeEffectNumbers;
	}

	public void setActiveEffectNumbers(LinkedList<Integer> activeEffectNumbers) {
		this.activeEffectNumbers = activeEffectNumbers;
	}

	public LinkedList<Integer> getRemainingEffectTurns() {
		return remainingEffectTurns;
	}

	public void setRemainingEffectTurns(LinkedList<Integer> remainingEffectTurns) {
		this.remainingEffectTurns = remainingEffectTurns;
	}
}
