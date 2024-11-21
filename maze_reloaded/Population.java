import java.util.Random;
import java.util.Vector;
import java.util.Collections; 
import java.util.Comparator;
import java.util.HashMap;

public class Population {
    public Vector<Individual> populationIndividuals;
    public int generationNumber;
    public Random ran;
    private float mutationRate;
    public int populationSize;
    private int maxAmountOfMovements;
    private float crossoverRate = 80;
    // Amount of individuals with the best fitness number that will pass to the next generation
    private int eliteCount = 20;

    //Population size must be even size
    Population(int numIndividuals, int mazeSize){
        maxAmountOfMovements = mazeSize;
        populationSize = numIndividuals;
        ran = new Random();
        mutationRate = 100;
        generationNumber = 1;
        populationIndividuals = new Vector<Individual>();

        // Creates the number of individuals requested with a initial set of movements
        // The size of the total tiles of the maze
        for (int i = 0; i < numIndividuals; i++){
            populationIndividuals.add(new Individual(ran, maxAmountOfMovements));
        }
    }

    public void Breed(){

        Vector<Individual> selectedIndividuals = rouletteWheelSelector();
        Vector<Individual> newGeneration = new Vector<Individual>();
        // Set the top individuals on a vector based on their performance
        getTopIndividuals();

        //Add the top individuals to the new generation
        for (int i = 0; i < eliteCount; i++) {
            newGeneration.add(populationIndividuals.get(i));
        }

        //Make them breed randomly so that each pair of individuals has 2 childs
        // with a 80% chance that breeding ocurrs. If it does not ocurrs, then pass the 
        // parents to the next generation
        for (int i = 0; i < selectedIndividuals.size(); i+=2){
            // Where is the array of the set of movements going to be split
            int partition = ran.nextInt(maxAmountOfMovements);
            float crossoverValue = ran.nextFloat(100);
            //Breeds the individuals
            Individual parent1 = selectedIndividuals.get(i);
            Individual parent2 = selectedIndividuals.get(i+1);
            // If crossover does not occurs, pass the parents to the next generation
            if (!(crossoverValue <= crossoverRate)){
                newGeneration.add(new Individual(parent1, maxAmountOfMovements));
                newGeneration.add(new Individual(parent2, maxAmountOfMovements));
                continue;
            }
            //Gets sublists from both parents given the partition index and creates
            //2 new individuals with them. Add those individuals to the next generation
            System.out.println("Max Amount of Movements " + maxAmountOfMovements);
            System.out.println("Create list amount of movements  " + parent1.getMovements().subList(0, partition).size() + parent1.getMovements().subList(partition, maxAmountOfMovements).size());
            newGeneration.add(new Individual(parent1.getMovements().subList(0, partition), parent2.getMovements().subList(partition, maxAmountOfMovements), ran, mutationRate)); 
            newGeneration.add(new Individual(parent2.getMovements().subList(0, partition), parent1.getMovements().subList(partition, maxAmountOfMovements), ran, mutationRate)); 
        }

        // Sets the new generation as the current population
        populationIndividuals = newGeneration;
        //Adds one to the generation number
        generationNumber += 1;

    }


    public void getTopIndividuals(){
        populationIndividuals.sort(new Comparator<Individual>() {
            @Override
            public int compare(Individual ind1, Individual ind2) {
                return Double.compare(ind2.fitnessValue, ind1.fitnessValue); // Descending order
            }
        });

    }

    public boolean checkFitness(int[] objective){
        boolean foundPath = false;

        for (Individual individual : populationIndividuals){
            individual.fitnessCheck(objective);

            if (individual.fitnessValue == 1000){
                foundPath = true;
                return foundPath;
            }
            else if(individual.objectiveFound && individual.movementsPerformed < maxAmountOfMovements){
                maxAmountOfMovements = individual.movementsPerformed;
                System.out.println("MAX AMOUNT OF MOVEMENTS " + maxAmountOfMovements);
            }
        }

        return foundPath;
    }
    /* 
    private Vector<Individual> rouletteWheelSelector(){

        // Creates a vector that maps the index of an individual with its fitness valule
        Vector<Double> fitness = new Vector<Double>();
        Vector<Individual> selectedIndividuals = new Vector<Individual>();
        int counter = 0;
        double totalFitness = 0;
        int selectedMax = populationSize - eliteCount;
        for (Individual individual : populationIndividuals){
            if (counter < selectedMax){
                System.out.println(individual.fitnessValue);
                fitness.add( individual.fitnessValue);
                totalFitness += individual.fitnessValue;
            }
            counter ++;

        }

        //Creates an array that maps the index of an individual with its cumulative probability
        //value
        double[] cumulativeProbability = new double[maxAmountOfMovements];
        double cumulativeProb = 0;
        counter = 0;
        for (double fitnessValue : fitness){
            cumulativeProb += fitnessValue/totalFitness * 1000;
            cumulativeProbability[counter] =  cumulativeProb;
            counter++;
        }

        // Returns the index of the individuals, listed on the populationIndividuals variable, 
        // that were selected to reproduce.
        for (int i = 0; i < selectedMax; i++){
            float ranNum = ran.nextFloat(1000f);
            counter = 0;
            for (double cumProb : cumulativeProbability){
                counter += 1;
                if (ranNum <= cumProb){
                    selectedIndividuals.add(populationIndividuals.get(counter));
                    break;
                }
            }
        }
        return selectedIndividuals;

    }
    */
    private Vector<Individual> rouletteWheelSelector() {
        Vector<Double> fitness = new Vector<>();
        Vector<Individual> selectedIndividuals = new Vector<>();
        int selectedMax = populationSize - eliteCount;
    
        // Calculate fitness and handle negative values
        double minFitness = Double.MAX_VALUE;
        double totalFitness = 0;
    
        for (Individual individual : populationIndividuals) {
            double fitnessValue = individual.fitnessValue;
            fitness.add(fitnessValue);
            if (fitnessValue < minFitness) {
                minFitness = fitnessValue;
            }
        }
    
        // Shift all fitness values to ensure non-negativity
        if (minFitness < 0) {
            for (int i = 0; i < fitness.size(); i++) {
                fitness.set(i, fitness.get(i) - minFitness + 1); // Add 1 to ensure all are > 0
            }
        }
    
        // Recalculate total fitness after shifting
        totalFitness = fitness.stream().mapToDouble(Double::doubleValue).sum();
    
        // Build cumulative probability array
        double[] cumulativeProbability = new double[fitness.size()];
        double cumulativeProb = 0;
        for (int i = 0; i < fitness.size(); i++) {
            cumulativeProb += fitness.get(i) / totalFitness;
            cumulativeProbability[i] = cumulativeProb;
        }
    
        // Perform selection
        Random random = new Random();
        for (int i = 0; i < selectedMax; i++) {
            double rand = random.nextDouble();
            for (int j = 0; j < cumulativeProbability.length; j++) {
                if (rand <= cumulativeProbability[j]) {
                    selectedIndividuals.add(populationIndividuals.get(j));
                    break;
                }
            }
        }
    
        return selectedIndividuals;
    }
    
}
