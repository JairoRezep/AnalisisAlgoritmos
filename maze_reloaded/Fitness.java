import java.util.Vector;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Fitness {
    //Creates a transparten red color
    private Color redColor = new Color(255, 0,0, 55);
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

    }

    // Calculates a fitness value based on Individual performance on certain parameters
    public float fitnessCheck(Individual individual){
        float formula = (1 / individual.distanceFromObjective) * 1 / (individual.numberOfWallCrashes + (individual.backTracked * 5) + individual.reachedDeadEnd * 10);
        return formula;
    }

    private void makeMovements(){
        setInitialCell();

        for (int i = 0; i < initialPopulation.populationIndividuals.getFirst().getMovements().size(); i++){
            for (Individual individual : initialPopulation.populationIndividuals){
                int movement = individual.getMovements().get(i);
                int[] currentCell = individual.currentCell;

                // Evaluates if in the next attempted movement there is a wall
                if (walls[currentCell[0]][currentCell[1]][movement]){
                    int[] nextCell;

                    // If there is not a wall define what the next cell would be
                    switch (movement) {
                        case 0:
                            nextCell = new int[]{currentCell[0] - 1, currentCell[1]};
                            break;
                        case 1:
                            nextCell = new int[]{currentCell[0], currentCell[1] + 1};
                            break;
                        case 2:
                            nextCell = new int[]{currentCell[0] + 1, currentCell[1]};
                            break;
                        case 3:
                            nextCell = new int[]{currentCell[0], currentCell[1] - 1};
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
                // Update UI
                updateMazeUI();
            }
        }
    }

    // Set the current cell of all the individuals of the population to
    // the initial cell
    private void setInitialCell(){
        for (Individual individual : initialPopulation.populationIndividuals){
            individual.currentCell = initialCell;
        }
    }

    private void isDeadEnd(Individual individual){
        int[] cell = individual.currentCell;
        int wallCounter = 0;
        for (int wall = 0; wall < 4; wall++){
            if (walls[cell[0]][cell[1]][wall]){
                wallCounter += 1;
            }
        }

        if (wallCounter >= 3){
            individual.reachedDeadEnd = 1;
        }
    }

    private void updateMazeUI(){
        Hashtable<int[], Integer> redCells = new Hashtable<int[], Integer>();

        for (Individual individual : initialPopulation.populationIndividuals){
            int[] cell = individual.currentCell;
            for (int[] k : redCells.keySet()){
                if (Arrays.equals(cell, k)){
                    redCells.;
                }
            }
        }
    }

}
