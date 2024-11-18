import java.util.Random;
import java.util.Vector;
import java.util.Collections; 
import java.util.Comparator;

public class Population {
    public Vector<Individual> populationIndividuals;
    public int generationNumber;
    public Random ran;
    private float mutationRate;
    public int populationSize;
    private int maxAmountOfMovements;

    //Population size must be even size
    Population(int numIndividuals, int mazeSize){
        maxAmountOfMovements = mazeSize;
        populationSize = numIndividuals;
        ran = new Random();
        mutationRate = 10;
        generationNumber = 1;
        populationIndividuals = new Vector<Individual>();

        // Creates the number of individuals requested with a initial set of movements
        // The size of the total tiles of the maze
        for (int i = 0; i < numIndividuals; i++){
            populationIndividuals.add(new Individual(ran, maxAmountOfMovements));
        }
    }

    public void Breed(){
        // Set the top 50 individuals on a vector based on their performance
        getTopIndividuals();

        Vector<Individual> topIndividuals = new Vector<>();
        int limit = populationSize / 2;

        for (int i = 0; i < limit; i++) {
            topIndividuals.add(populationIndividuals.get(i));
        }
        
        //In case the objective is found, cut the length of the set of movements 
        //So that their maximum length is the number of movements it took the 
        //Individual who found the objective.
        for (int i = 0; i < topIndividuals.size(); i++){
            Individual newInd = new Individual(topIndividuals.get(i), maxAmountOfMovements);
            //Replaces individual to a version with a reduced set of movements
            populationIndividuals.add(i, newInd);
        }

        //Suffle the vector of top individuals
        Collections.shuffle(topIndividuals);

        //Make them breed randomly so that each pair of individuals has 2 childs
        for (int i = 0; i < topIndividuals.size(); i+=2){
            // Where is the array of the set of movements going to be split
            int partition = ran.nextInt(maxAmountOfMovements);
            //Breeds the individuals
            Individual parent1 = topIndividuals.get(i);
            Individual parent2 = topIndividuals.get(i+1);
            //Gets sublists from both parents given the partition index and creates
            //2 new individuals with them.
            populationIndividuals.add(topIndividuals.size() - 1 + i, new Individual(parent1.getMovements().subList(0, partition), parent2.getMovements().subList(partition, maxAmountOfMovements), ran, mutationRate));
            populationIndividuals.add(topIndividuals.size() + i, new Individual(parent2.getMovements().subList(0, partition), parent1.getMovements().subList(partition, maxAmountOfMovements), ran, mutationRate));
        }

        //Adds one to the generation number
        generationNumber += 1;
    }

    public void getTopIndividuals(){
        populationIndividuals.sort(new Comparator<Individual>() {
            @Override
            public int compare(Individual ind1, Individual ind2) {
                return Float.compare(ind2.fitnessValue, ind1.fitnessValue); // Descending order
            }
        });

    }

    public boolean checkFitness(int[] objective){
        boolean foundPath = false;

        for (Individual individual : populationIndividuals){
            individual.fitnessCheck(objective);

            if (individual.fitnessValue == 100){
                foundPath = true;
                return foundPath;
            }
            else if(individual.objectiveFound && individual.movementsPerformed < maxAmountOfMovements){
                maxAmountOfMovements = individual.movementsPerformed;
            }
        }

        return foundPath;
    }
}
