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
    public float fitnessValue;
    public int[] previousCell;
    public boolean objectiveFound;
    public int movementsPerformed;

    //Creates a new Individual with a random set of movements of the specified length
    Individual(Random ran, int length){
        objectiveFound = false;
        // Creates a random movement list taking into account the length of the labyrinth
        movements = new Vector<Integer>();

        for (int i = 0; i < length; i ++){
            movements.add(ran.nextInt(4));
        }

        numberOfWallCrashes = 0;
    }

    //Creates a new Individual given their parents subset of movements and a mutation rate
    Individual(List<Integer> parent1, List<Integer> parent2, Random ran, float mutationRate){
        // Creates a list fo movements with the length of the parents sublists
        movements = new Vector<Integer>();
        objectiveFound = false;
        
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
    public float fitnessCheck(int[] objectiveCell){
        float distanceFitness = 0;
        float performanceFitness = (numberOfWallCrashes * 5 + (backTracked * 10) + reachedDeadEnd * 10);
        int distance =  calculateCurrentDistanceFromObjective(objectiveCell);
        if (distance == 0){
            distanceFitness = 1;
        }
        else if(distance == 1){
            distanceFitness = 0.75f;
        }
        else{
            distanceFitness = 1 / distance;
        }

        if (performanceFitness == 0){
            performanceFitness = 1;
        }
        else if(performanceFitness == 1){
            performanceFitness = 1.5f;
        }
        else{

        }
        float formula = distanceFitness * (100 / performanceFitness);
        return formula;
    }
}
