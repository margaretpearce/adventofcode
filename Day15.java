import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.chocosolver.solver.*;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;


/***
 * Day 15 attempt using ChocoSolver...slow!
 * See day15.gms for a better solution solved using Couenne via neos-server.org
 * @author margaretpearce
 */
public class Day15 {
	
	private ArrayList<String> ingredient;
	private ArrayList<Integer> capacity;
	private ArrayList<Integer> durability;
	private ArrayList<Integer> flavor;
	private ArrayList<Integer> texture;
	private ArrayList<Integer> calories;
	private final int TOTAL_NUM_TEASPOONS = 100;
	
	public Day15() {
		this.ingredient = new ArrayList<String>();
		this.capacity = new ArrayList<Integer>();
		this.durability = new ArrayList<Integer>();
		this.flavor = new ArrayList<Integer>();
		this.texture = new ArrayList<Integer>();
		this.calories = new ArrayList<Integer>();
		this.readInputFile();
	}

	public void readInputFile() {
		BufferedReader buffReader = null;
		try{
			buffReader = new BufferedReader (new FileReader("day15.txt"));
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
		String[] lineItems = line.split(" ");
		this.ingredient.add(lineItems[0].replaceAll(":", ""));
		this.capacity.add(Integer.parseInt(lineItems[2].replaceAll(",", "")));
		this.durability.add(Integer.parseInt(lineItems[4].replaceAll(",", "")));
		this.flavor.add(Integer.parseInt(lineItems[6].replaceAll(",", "")));
		this.texture.add(Integer.parseInt(lineItems[8].replaceAll(",", "")));
		this.calories.add(Integer.parseInt(lineItems[10]));
	}
	
	public void solveILP() {
		// 1. Create a Solver
        Solver solver = new Solver("Ingredients Solver");
        
        // 2. Create variable for the number of each ingredient
        IntVar[] numEachIngredient = new IntVar[this.ingredient.size()];
        IntVar[] capacityByIngredient = new IntVar[this.ingredient.size()];
        IntVar[] durabilityByIngredient = new IntVar[this.ingredient.size()];
        IntVar[] flavorByIngredient = new IntVar[this.ingredient.size()];
        IntVar[] textureByIngredient = new IntVar[this.ingredient.size()];
        
        for (int i = 0; i < this.ingredient.size(); i++) {
        	// The number of teaspoons for each ingredient to use should be between 0 and 100 (integers)
        	IntVar numForI = VariableFactory.bounded(this.ingredient.get(i), 0, this.TOTAL_NUM_TEASPOONS, solver);
        	IntVar capacityForI = VariableFactory.bounded(this.ingredient.get(i) + "_capacity", 
        			VariableFactory.MIN_INT_BOUND, VariableFactory.MAX_INT_BOUND, solver);
        	IntVar durabilityForI = VariableFactory.bounded(this.ingredient.get(i) + "_durability", 
        			VariableFactory.MIN_INT_BOUND, VariableFactory.MAX_INT_BOUND, solver);
        	IntVar flavorForI = VariableFactory.bounded(this.ingredient.get(i) + "_flavor", 
        			VariableFactory.MIN_INT_BOUND, VariableFactory.MAX_INT_BOUND, solver);
        	IntVar textureForI = VariableFactory.bounded(this.ingredient.get(i) + "_texture", 
        			VariableFactory.MIN_INT_BOUND, VariableFactory.MAX_INT_BOUND, solver);
        	
        	numEachIngredient[i] = numForI;
        	capacityByIngredient[i] = capacityForI;
        	durabilityByIngredient[i] = durabilityForI;
        	flavorByIngredient[i] = flavorForI;
        	textureByIngredient[i] = textureForI;
        }
        
        IntVar totalTeaspoons = VariableFactory.bounded("total_teaspoons", 0, this.TOTAL_NUM_TEASPOONS, solver);
        
        // Create variables for the total score of each property
        IntVar totalCapacity = VariableFactory.bounded("totalcapacity", 0, VariableFactory.MAX_INT_BOUND, solver);
        IntVar totalDurability = VariableFactory.bounded("totalscore", 0, VariableFactory.MAX_INT_BOUND, solver);
        IntVar totalFlavor = VariableFactory.bounded("totalscore", 0, VariableFactory.MAX_INT_BOUND, solver);
        IntVar totalTexture = VariableFactory.bounded("totalscore", 0, VariableFactory.MAX_INT_BOUND, solver);
        
        // Create a variable for the total score
        IntVar totalScore = VariableFactory.bounded("totalscore", 0, VariableFactory.MAX_INT_BOUND, solver);
        
        // Add constraints
        // Total number of teaspoons used
        solver.post(IntConstraintFactory.sum(numEachIngredient, totalTeaspoons));
        solver.post(IntConstraintFactory.arithm(totalTeaspoons, "<=", 100));
        
        // Assign total for each property
        solver.post(IntConstraintFactory.sum(capacityByIngredient, totalCapacity));
        solver.post(IntConstraintFactory.sum(durabilityByIngredient, totalDurability));
        solver.post(IntConstraintFactory.sum(flavorByIngredient, totalFlavor));
        solver.post(IntConstraintFactory.sum(textureByIngredient, totalTexture));
        
        // Calculate total score
        IntVar productFirstTwo = VariableFactory.bounded("prod_firsthalf", 0, VariableFactory.MAX_INT_BOUND, solver);
        IntVar productSecondTwo = VariableFactory.bounded("prod_secondhalf", 0, VariableFactory.MAX_INT_BOUND, solver);
        solver.post(IntConstraintFactory.times(totalCapacity, totalDurability, productFirstTwo));
        solver.post(IntConstraintFactory.times(totalFlavor, totalTexture, productSecondTwo));
        solver.post(IntConstraintFactory.times(productFirstTwo, productSecondTwo, totalScore));
        
        // Set objective and find optimal solution
        solver.findOptimalSolution(ResolutionPolicy.MAXIMIZE, totalScore);
        
        // Print solver stats
        Chatterbox.printStatistics(solver);
	}
	
	public static void main(String[] args) {
		Day15 puzzle = new Day15();
		puzzle.solveILP();
	}

}
