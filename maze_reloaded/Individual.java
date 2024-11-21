import java.util.Random;
import java.util.Vector;
import java.util.List;
import java.util.*;

public class Individual {
    private Vector<Integer> movements;
    public int backTracked;
    public int numberOfWallCrashes;
    public int reachedDeadEnd;
    public int[] currentCell;
    public double fitnessValue;
    public int[] previousCell;
    public boolean objectiveFound;
    public int movementsPerformed;
    public int cellRepetitionPenalization;

    //Creates a new Individual with a random set of movements of the specified length
    Individual(Random ran, int length){
        objectiveFound = false;
        backTracked = 0;
        // Creates a random movement list taking into account the length of the labyrinth
        movements = new Vector<Integer>();

        for (int i = 0; i < length; i ++){
            movements.add(ran.nextInt(4));
        }

        numberOfWallCrashes = 0;
        movementsPerformed = movements.size();
    }

    //Creates a new Individual given their parents subset of movements and a mutation rate
    Individual(List<Integer> parent1, List<Integer> parent2, Random ran, float mutationRate){
        // Creates a list fo movements with the length of the parents sublists
        movements = new Vector<Integer>();
        objectiveFound = false;
        backTracked = 0;
        
        // Combines the list taking into account mutation rate
        for (int i = 0 ; i < parent1.size(); i ++){
            int mutation = ran.nextInt(1001);
            if (mutation <= mutationRate){
                int movement = ran.nextInt(4);
                while (movement == parent1.get(i)) {
                    movement = ran.nextInt(4);
                }
                movements.add(parent1.get(i));
                continue;
            }
            movements.add(parent1.get(i));
        }

        int counter = 0;
        for(int j = parent1.size(); j < parent1.size() + parent2.size(); j++){
            int mutation = ran.nextInt(1001);
            if (mutation <= mutationRate){
                int movement = ran.nextInt(4);
                while (movement == parent2.get(counter)) {
                    movement = ran.nextInt(4);
                }
                movements.add(parent2.get(counter));
                counter++;
                continue;
            }
            movements.add(parent2.get(counter));
            counter++;
        }

        numberOfWallCrashes = 0;
        movementsPerformed = movements.size();
    }

    //Modifies the Individual with a subset of movements of the original
    Individual(Individual ind, int maxLength){
        Vector<Integer> oldMovements = ind.getMovements();
        objectiveFound = false;

        movements = new Vector<Integer>();
        for (int i = 0; i < maxLength; i++){
            movements.add(oldMovements.get(i));
        }

        numberOfWallCrashes = 0;
        backTracked = 0;
        movementsPerformed = movements.size();
    }

    public Vector<Integer> getMovements() {

        return movements;
    }

    public boolean backTracked(int[] nextCell){

        if (Arrays.equals(nextCell, previousCell)){
            return true;
        }
        return false;

    }

    public int calculateCurrentDistanceFromObjective(int[] objective){
        return Math.abs(currentCell[0] - objective[0]) + Math.abs(currentCell[1] - objective[1]);
    }

    public void objectiveFoundAt(int movementNum) {
        movementsPerformed = movementNum;
        objectiveFound = true;
    }

    // Calculates a fitness value based on Individual performance on certain parameters
    public double fitnessCheck(int[] objectiveCell){
        //float performanceFitness = (numberOfWallCrashes  + (backTracked * 10) + reachedDeadEnd * 40) + 1;
        double distance =  calculateCurrentDistanceFromObjective(objectiveCell) + 1;
        //double distanceFitness = (1000 / distance);
        //double formula = distanceFitness * (10 / performanceFitness);
        //ouble formula =  10000 / Math.abs(100 * distance + 10 * backTracked  + 2f * numberOfWallCrashes + 40 * (reachedDeadEnd));
        double formula =  -10 * distance - 5 * numberOfWallCrashes - 400 * reachedDeadEnd - 8 * backTracked - (cellRepetitionPenalization * distance) * 100;
        fitnessValue = formula;
        System.out.println("DF " + distance  + " WC " + numberOfWallCrashes + " DE "+ reachedDeadEnd+ " FITNESS " + formula);
        return formula;
    }
}
