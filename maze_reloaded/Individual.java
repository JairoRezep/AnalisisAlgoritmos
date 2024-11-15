import java.util.Random;
import java.util.Vector;
import java.util.List;

public class Individual {
    private Vector<Integer> movements;
    public int distanceFromObjective;
    public boolean backTracked;

    //Creates a new Individual with a random set of movements of the specified length
    Individual(Random ran, int length){
        // Creates a random movement list taking into account the length of the labyrinth
        movements = new Vector<Integer>();

        for (int i = 0; i < length; i ++){
            movements.add(ran.nextInt(41));
        }
    }

    //Creates a new Individual given their parents subset of movements and a mutation rate
    Individual(List<Integer> parent1, List<Integer> parent2, Random ran, float mutationRate){
        // Creates a list fo movements with the length of the parents sublists
        movements = new Vector<Integer>();
        
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
    }

    //Creates a new Individual with a subset of movements of the original
    Individual(Individual ind, int maxLength){
        Vector<Integer> oldMovements = ind.getMovements();
        movements = new Vector<Integer>();
        for (int i = 0; i < maxLength; i++){
            movements.add(oldMovements.get(i));
        }
    }

    public Vector<Integer> getMovements() {
        return movements;
    }
}
