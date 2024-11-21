import java.util.Vector;
import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.util.*;
import java.util.function.Supplier;

public class Fitness {
    //Creates a transparten red color
    MazeGenerator generatedMaze;
    public boolean[][][] walls;
    public int[] objectiveCell;
    public int[] initialCell;
    Population population;
    private java.util.HashMap<Vector<Integer>, Integer> cellRecord;

    Fitness(int populationSize,int mazeSize, MazeGenerator maze, int[] initial, int[] objective){
        population = new Population(populationSize, mazeSize);
        generatedMaze = maze;
        objectiveCell = objective;
        initialCell = initial;
        walls = generatedMaze.walls;
        cellRecord = new HashMap<Vector<Integer>, Integer>();

        boolean foundPath = false;
        while (!foundPath) {
            generatedMaze.updateGeneration(population.generationNumber);
            makeMovements();
            foundPath = population.checkFitness(objective);
            if (foundPath){
                break;
            }
            population.Breed();
        }
        System.out.println("Found path");

    }

    

    private void makeMovements(){
        setInitialCell();
        int loopNumber = 0;
        for (int i = 0; i < population.populationIndividuals.getFirst().getMovements().size(); i++){
            loopNumber += 1;
            
            slowBasedOnMovementSet();

            for (Individual individual : population.populationIndividuals){
                if (individual.objectiveFound){
                    continue;
                }
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

                    // If next cell is the objective, mark individual as someone who got
                    // to the objective and how many movements it took him.
                    if (Arrays.equals(nextCell, objectiveCell)){
                        individual.objectiveFoundAt(i + 1);
                    }
                    //Checks if the cell was already visited
                    //if it was set the indiviual's value of tried to backtrack to 1
                    else if (individual.backTracked(nextCell)){
                        individual.backTracked += 1;
                    }
                    
                    individual.previousCell = currentCell;
                    individual.currentCell = nextCell;
                    

                }
                // If it crashed with a wall and it is not on a dead end, 
                //add it to the counter and don't update
                // the current cell
                else{
                    individual.numberOfWallCrashes += 1;
                }
            }

            if (loopNumber % 3 == 0){
                // Update UI
                updateMazeUI();
            }
        }
        //Check if the cell where they ended is a dead end and updates the record of the cell
        UpdateCellRecord();
        AsignCellRecord();
        checkDeadEnd();
    }

    // Set the current cell of all the individuals of the population to
    // the initial cell
    private void setInitialCell(){
        for (Individual individual : population.populationIndividuals){
            individual.currentCell = initialCell;
        }
    }

    private void checkDeadEnd(){

        for (Individual individual : population.populationIndividuals){
            int[] cell = individual.currentCell;
            if (Arrays.equals(cell, initialCell)){
                continue;
            }
            if ((cell[0] > 19 || cell[0] < 0 || cell[1] > 19 || cell[1] < 0)){
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
    }

    //Updates the MazeUI
    private void updateMazeUI(){
        Hashtable<java.util.List<Integer>, Integer> redCells = new Hashtable<>();

        for (Individual individual : population.populationIndividuals){
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
        // Paint each cell that has no individuals currently on it of light gray
        Set<java.util.List<Integer>> redKeySet = redCells.keySet();
        for (int x = 0; x < 50; x ++){
            for (int y = 0; y < 50; y++){
                java.util.List<Integer> currentCell = Arrays.asList(x, y);
                if (x == objectiveCell[0] && y == objectiveCell[1]){
                    if (redKeySet.contains(currentCell)){
                        generatedMaze.paintCell(x, y, Color.YELLOW);
                    }
                    continue;
                }
                else if (redKeySet.contains(currentCell)){
                    int alphaValue = 55 + (15 * redCells.get(currentCell));
                    if (alphaValue > 255){
                        alphaValue = 255;
                    }
                    generatedMaze.paintCell(x, y,new Color(255, 0, 0, alphaValue));
                }
                
                else{
                    generatedMaze.paintCell(x, y, Color.LIGHT_GRAY);
                }
            }
        }

        generatedMaze.repaint();;
    }

    //Slows down the Update of the UI based on the amount of movements that are left to perform
    private void slowBasedOnMovementSet(){
        int movementSetSize = population.populationIndividuals.getFirst().getMovements().size();

        try{
            Thread.sleep(2000 * 1/movementSetSize);
        }
        catch(InterruptedException e){
            System.out.println(e);
        }
    }

    private void UpdateCellRecord(){
        for (Individual individual : population.populationIndividuals){
            int[] cell = individual.currentCell;
            Vector<Integer> cellAsVector = new Vector<>(Arrays.asList(cell[0], cell[1]));
            if (cellRecord.containsKey(cellAsVector)){
                cellRecord.put(cellAsVector, cellRecord.get(cellAsVector) + 1);
            }
            else{
                cellRecord.put(cellAsVector, 1);
            }
        }
    }

    private void AsignCellRecord(){
        for (Individual individual : population.populationIndividuals){
            Vector<Integer> cell = new Vector<>(Arrays.asList(individual.currentCell[0], individual.currentCell[1]));
            individual.cellRepetitionPenalization = cellRecord.get(cell);
        }
    }
}
