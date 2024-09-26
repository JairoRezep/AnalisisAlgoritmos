
import java.awt.Dimension;
import java.lang.Math;

public class AStarEngine extends AbstractSearchEngine{

    private boolean diagNE, diagNO, diagSE, diagSO;
    
    public AStarEngine(int width, int height){
        super(width, height);
        iterateSearch(startLoc);
    }


    public void iterateSearch(Dimension startLoc){
        
    }

    @Override
    protected Dimension[] getPossibleMoves(Dimension loc) {
        Dimension tempMoves [] = new Dimension[4];
        diagNE = diagNO = diagSE = diagSO = true;
        tempMoves[0] = tempMoves[1] = tempMoves[2] = tempMoves[3] = null;
        int x = loc.width;
        int y = loc.height;
        int num = 0;
        if (maze.getValue(x - 1, y) == 0 || maze.getValue(x - 1, y) == Maze.GOAL_LOC_VALUE) {
            tempMoves[num++] = new Dimension(x - 1, y);
        }
        else{
            diagNE = diagSE = false;
        }
        if (maze.getValue(x + 1, y) == 0 || maze.getValue(x + 1, y) == Maze.GOAL_LOC_VALUE) {
            tempMoves[num++] = new Dimension(x + 1, y);
        }
        else{
            diagNO = diagSO = false;
        }
        if (maze.getValue(x, y - 1) == 0 || maze.getValue(x, y - 1) == Maze.GOAL_LOC_VALUE) {
            tempMoves[num++] = new Dimension(x, y - 1);
        }
        else{
            diagSE = diagSO = false;
        }
        if (maze.getValue(x, y + 1) == 0 || maze.getValue(x, y + 1) == Maze.GOAL_LOC_VALUE) {
            tempMoves[num++] = new Dimension(x, y + 1);
        }
        else{
            diagNE = diagNO = false;
        }
        return tempMoves;
    }

    protected Dimension[] getPossibleDiagonalDimensions(Dimension loc){
        
        Dimension tempMoves [] = new Dimension[4];
        tempMoves[0] = tempMoves[1] = tempMoves[2] = tempMoves[3] = null;

        int x = loc.width;
        int y = loc.height;

        int numDiag = 0;
        // Agregar las dimensiones diagonales, en caso de que sea posible
        if (maze.getValue(x - 1, y - 1) == 0 && !(maze.getValue(x - 1, y - 1) == Maze.GOAL_LOC_VALUE) && diagSE ) {
            tempMoves[numDiag++] = new Dimension(x - 1, y - 1);
        }
        if (maze.getValue(x + 1, y - 1) == 0 && !(maze.getValue(x + 1, y - 1) == Maze.GOAL_LOC_VALUE) && diagSO) {
            tempMoves[numDiag++] = new Dimension(x + 1, y - 1);
        }
        if (maze.getValue(x - 1, y + 1) == 0 && !(maze.getValue(x - 1, y + 1) == Maze.GOAL_LOC_VALUE) && diagNE) {
            tempMoves[numDiag++] = new Dimension(x - 1, y + 1);
        }
        if (maze.getValue(x + 1, y + 1) == 0 && !(maze.getValue(x + 1, y + 1) == Maze.GOAL_LOC_VALUE) && diagNO) {
            tempMoves[numDiag++] = new Dimension(x + 1, y + 1);
        }

        return tempMoves;
    }

    protected int calcularEstimacionHeuristica(Dimension loc){

        int difX = goalLoc.width - loc.width;
        int difY = goalLoc.height - loc.height;

        return Math.abs(difX) + Math.abs(difY);
    }
}