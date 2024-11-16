import java.util.Vector;
import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.util.*;

public class Fitness {
    //Creates a transparten red color
    MazeGenerator generatedMaze;
    public boolean[][][] walls;
    public int[] objectiveCell;
    public int[] initialCell;
    Population initialPopulation;

    Fitness(int populationSize,int mazeSize, MazeGenerator maze, int[] initial, int[] objective){
        initialPopulation = new Population(populationSize, mazeSize);
        generatedMaze = maze;
        objectiveCell = objective;
        initialCell = initial;
        walls = generatedMaze.walls;
        makeMovements();

    }

    // Calculates a fitness value based on Individual performance on certain parameters
    public float fitnessCheck(Individual individual){

        float formula = (1 / individual.calculateCurrentDistanceFromObjective(objectiveCell)) * 100 / (individual.numberOfWallCrashes + (individual.backTracked * 5) + individual.reachedDeadEnd * 100);
        return formula;
    }

    private void makeMovements(){
        setInitialCell();

        for (int i = 0; i < initialPopulation.populationIndividuals.getFirst().getMovements().size(); i++){
            
            try{
                Thread.sleep(10);
            }
            catch(InterruptedException e){
                System.out.println(e);
            }
            
            for (Individual individual : initialPopulation.populationIndividuals){
                int movement = individual.getMovements().get(i);
                int[] currentCell = individual.currentCell;

                // Evaluates if in the next attempted movement there is a wall
                if (!walls[currentCell[1]][currentCell[0]][movement]){
                    int[] nextCell;
                    
                    // If there is not a wall define what the next cell would be
                    switch (movement) {
                        case 0:
                            nextCell = new int[]{currentCell[0], currentCell[1] - 1};
                            break;
                        case 1:
                            nextCell = new int[]{currentCell[0] + 1, currentCell[1]};
                            break;
                        case 2:
                            nextCell = new int[]{currentCell[0], currentCell[1] + 1};
                            break;
                        case 3:
                            nextCell = new int[]{currentCell[0] - 1, currentCell[1]};
                            break;
                        default:
                            nextCell = new int[]{-1,-1};
                            break;
                    }

                    

                    //Checks if the cell was already visited
                    //if it was set the indiviual's value of tried to backtrack to 1
                    if (individual.checkIfVisited(nextCell)){
                        individual.backTracked = 1;
                    }
                    // If not, add the cell to the list of visited cells
                    // move to the next cell
                    else{
                        individual.visitedCells.add(nextCell);
                        individual.currentCell = nextCell;
                    }

                    //Check if this cell is a dead end
                    isDeadEnd(individual);

                }
                // If it crashed with a wall, add it to the counter and don't update
                // the current cell
                else{
                    individual.numberOfWallCrashes += 1;
                }
                
            }
            System.out.println("Finished loop no " + i );
            // Update UI
            updateMazeUI();
        }
    }

    // Set the current cell of all the individuals of the population to
    // the initial cell
    private void setInitialCell(){
        for (Individual individual : initialPopulation.populationIndividuals){
            individual.currentCell = initialCell;
            individual.visitedCells.add(initialCell);
        }
    }

    private void isDeadEnd(Individual individual){
        int[] cell = individual.currentCell;

        if ((cell[0] > 49 || cell[0] < 0 || cell[1] > 49 || cell[1] < 0) || (cell[0] == initialCell[0] && cell[1] == initialCell[1])){
            return;
        }
        int wallCounter = 0;
        for (int wall = 0; wall < 4; wall++){
            if (walls[cell[1]][cell[0]][wall]){
                wallCounter += 1;
            }
        }

        if (wallCounter >= 3){
            individual.reachedDeadEnd = 1;
        }
    }

    private void updateMazeUI(){
        Hashtable<java.util.List<Integer>, Integer> redCells = new Hashtable<>();

        for (Individual individual : initialPopulation.populationIndividuals){
            int[] cell = individual.currentCell;
            boolean notFound = true;
            for (java.util.List<Integer> k : redCells.keySet()){
                if (k.equals(Arrays.asList(cell[0], cell[1]))){
                    redCells.put(k, redCells.get(k) + 1);
                    notFound = false;
                    break;
                }
            }
            if (notFound){
                redCells.put(Arrays.asList(cell[0], cell[1]), 1);
            }
            
        }

        //Paint each cell that has an individual currently on it, with the transparency
        // depending on the ammount of Individuals that are currently on that cell
        for (java.util.List<Integer> k : redCells.keySet()){
            System.out.println("No of cells in " + k.get(0) + " " + k.get(1) + " is " + redCells.get(k));
            int alphaValue = 55 + (15 * redCells.get(k));
            if (alphaValue > 255){
                alphaValue = 255;
            }
                

            generatedMaze.paintCell(k.get(0), k.get(1),new Color(255, 0, 0, alphaValue));
        }

        // Paint each cell that has no individuals currently on it of light gray
        
        for (int x = 0; x < 50; x ++){
            for (int y = 0; y < 50; y++){
                java.util.List<Integer> currentCell = Arrays.asList(x, y);
                if (redCells.keySet().contains(currentCell)){
                    continue;
                }
                else{
                    generatedMaze.paintCell(x, y, Color.LIGHT_GRAY);
                }
            }
        }

        generatedMaze.repaint();
        
    }

}
