import java.util.Random;
import java.util.Vector;
import java.util.Collections; 

public class Population {
    public Vector<Individual> populationIndividuals;
    public int generationNumber;
    public Random ran;
    private float mutationRate;
    public int populationSize;

    //Population size must be even size
    Population(int numIndividuals, int mazeSize){
        populationSize = numIndividuals;
        ran = new Random();
        mutationRate = 1;
        generationNumber = 1;
        populationIndividuals = new Vector<Individual>();

        // Creates the number of individuals requested with a initial set of movements
        // The size of the total tiles of the maze
        for (int i = 0; i < numIndividuals; i++){
            populationIndividuals.add(new Individual(ran, mazeSize));
        }
    }

    public void Breed(Vector<Individual> topIndividuals, int maxLength){
        //In case the objective is found, cut the length of the set of movements 
        //So that their maximum length is the number of movements it took the 
        //Individual who found the objective.
        Vector<Individual> inds = new Vector<Individual>();
        for (int i = 0; i < (populationSize/2); i++){
            Individual newInd = new Individual(topIndividuals.get(i), maxLength);
            populationIndividuals.add(i, newInd);
            inds.add(newInd);
        }


        Collections.shuffle(inds);

        for (int i = 0; i < (populationSize/2); i+=2){
            // Where is the array of the set of movements going to be split
            int partition = ran.nextInt(maxLength);
            //Breeds the individuals
            Individual parent1 = inds.get(i);
            Individual parent2 = inds.get(i+1);
            //Gets sublists from both parents given the partition index and creates
            //2 new individuals with them.
            populationIndividuals.add(populationSize/2 + i,new Individual(parent1.getMovements().subList(0, partition), parent2.getMovements().subList(partition, maxLength), ran, mutationRate));
            populationIndividuals.add(populationSize/2 + i,new Individual(parent2.getMovements().subList(0, partition), parent1.getMovements().subList(partition, maxLength), ran, mutationRate));
        }

        generationNumber += 1;
    }
}
