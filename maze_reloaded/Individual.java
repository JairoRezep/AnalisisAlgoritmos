import java.util.Random;
import java.util.Vector;
import java.util.List;

public class Individual {
    private Vector<Integer> movements;
    public int distanceFromObjective;
    public int backTracked;
    public int numberOfWallCrashes;
    public int reachedDeadEnd;
    public int[] currentCell;
    public float fitnessValue;
    public Vector<int[]> visitedCells;

    //Creates a new Individual with a random set of movements of the specified length
    Individual(Random ran, int length){
        // Creates a random movement list taking into account the length of the labyrinth
        movements = new Vector<Integer>();
        visitedCells = new Vector<int[]>();

        for (int i = 0; i < length; i ++){
            movements.add(ran.nextInt(4));
        }

        numberOfWallCrashes = 0;
    }

    //Creates a new Individual given their parents subset of movements and a mutation rate
    Individual(List<Integer> parent1, List<Integer> parent2, Random ran, float mutationRate){
        // Creates a list fo movements with the length of the parents sublists
        movements = new Vector<Integer>();
        visitedCells = new Vector<int[]>();
        
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

        for(int j = parent1.size(); j < parent1.size() + parent2.size(); j++){
            int mutation = ran.nextInt(1001);
            if (mutation <= mutationRate){
                int movement = ran.nextInt(4);
                while (movement == parent2.get(j)) {
                    movement = ran.nextInt(4);
                }
                movements.add(parent2.get(j));
                continue;
            }
            movements.add(parent1.get(j));
        }

        numberOfWallCrashes = 0;
    }

    //Modifies the Individual with a subset of movements of the original
    Individual(Individual ind, int maxLength){
        Vector<Integer> oldMovements = ind.getMovements();
        visitedCells = new Vector<int[]>();

        movements = new Vector<Integer>();
        for (int i = 0; i < maxLength; i++){
            movements.add(oldMovements.get(i));
        }

        numberOfWallCrashes = 0;
    }

    public Vector<Integer> getMovements() {

        return movements;
    }

    public boolean checkIfVisited(int[] eval){

        boolean visited = false;

        for (int i = 0; i < visitedCells.size(); i++){
            if (visitedCells.get(i)[0] == eval[0] && visitedCells.get(i)[1] == eval[1]){
                visited = true;
                return visited;
            }
        }

        return visited;

    }

    public int calculateCurrentDistanceFromObjective(int[] objective){
        return Math.abs(currentCell[0] - objective[0]) + Math.abs(currentCell[1] - objective[1]);
    }

}
