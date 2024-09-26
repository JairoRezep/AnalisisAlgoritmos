
import java.awt.Dimension;
import java.lang.Math;
import java.util.PriorityQueue;
import java.util.Vector;

public class AStarEngine extends AbstractSearchEngine{

    private boolean diagNE, diagNO, diagSE, diagSO;
    private PriorityQueue<NodoAStar> nodosAbiertos;
    private Vector<NodoAStar> nodosCerrados;
    NodoAStar ultimoNodo;
    boolean isSearching;
    
    public AStarEngine(int width, int height){
        super(width, height);
        iterateSearch(startLoc);
    }


    public void iterateSearch(Dimension startLoc){
        long startTime = System.currentTimeMillis();
        nodosCerrados = new Vector<>();
        nodosAbiertos = new PriorityQueue<>();

        isSearching = true;

        NodoAStar nodoActual = new NodoAStar(startLoc);
        nodosAbiertos.add(nodoActual);
        while (!nodosAbiertos.isEmpty() && isSearching) {
            nodoActual = nodosAbiertos.poll();
            nodosCerrados.add(nodoActual);

            Dimension[] dimensionesAdyacentes = getPossibleMoves(nodoActual.getDimensionActual());
            revisarMovimientos(dimensionesAdyacentes, nodoActual, 10);

            Dimension[] dimensionesDiagonales = getPossibleDiagonalDimensions(nodoActual.getDimensionActual());
            revisarMovimientos(dimensionesDiagonales, nodoActual, 14);
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Tiempo de ejecución: " + executionTime + " ms");
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
            diagNO = diagSO = false;
        }
        if (maze.getValue(x + 1, y) == 0 || maze.getValue(x + 1, y) == Maze.GOAL_LOC_VALUE) {
            tempMoves[num++] = new Dimension(x + 1, y);
        }
        else{
            diagNE = diagSE = false;
        }
        if (maze.getValue(x, y - 1) == 0 || maze.getValue(x, y - 1) == Maze.GOAL_LOC_VALUE) {
            tempMoves[num++] = new Dimension(x, y - 1);
        }
        else{
            diagNE = diagNO = false;
        }
        if (maze.getValue(x, y + 1) == 0 || maze.getValue(x, y + 1) == Maze.GOAL_LOC_VALUE) {
            tempMoves[num++] = new Dimension(x, y + 1);
        }
        else{
            diagSE = diagSO = false;
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
        if (maze.getValue(x + 1, y + 1) == 0 && !(maze.getValue(x + 1, y + 1) == Maze.GOAL_LOC_VALUE) && diagSE ) {
            tempMoves[numDiag++] = new Dimension(x + 1, y + 1);
        }
        if (maze.getValue(x - 1, y + 1) == 0 && !(maze.getValue(x - 1, y + 1) == Maze.GOAL_LOC_VALUE) && diagSO) {
            tempMoves[numDiag++] = new Dimension(x - 1, y + 1);
        }
        if (maze.getValue(x + 1, y - 1) == 0 && !(maze.getValue(x + 1, y - 1) == Maze.GOAL_LOC_VALUE) && diagNE) {
            tempMoves[numDiag++] = new Dimension(x + 1, y - 1);
        }
        if (maze.getValue(x - 1, y - 1) == 0 && !(maze.getValue(x - 1, y - 1) == Maze.GOAL_LOC_VALUE) && diagNO) {
            tempMoves[numDiag++] = new Dimension(x - 1, y - 1);
        }

        return tempMoves;
    }

    protected int calcularEstimacionHeuristica(Dimension loc){
        int difX = (goalLoc.width - loc.width) * 10;
        int difY = (goalLoc.height - loc.height) * 10;

        return Math.abs(difX) + Math.abs(difY);
    }

    protected void revisarMovimientos(Dimension[] dimensions, NodoAStar nodoActual,int dist){
        for (int i = 0; i < 4; i++){
            if (dimensions[i] == null || estaEnNodosCerrados(dimensions[i])){
                continue;
            }

            NodoAStar nuevoNodo = new NodoAStar(dimensions[i], nodoActual, nodoActual.getDistanciaRecorrida() + dist, calcularEstimacionHeuristica(dimensions[i]));

            if (equals(dimensions[i], goalLoc)){
                System.out.println("Found the goal at " + dimensions[i].width +", " + dimensions[i].height);

                nodosCerrados.add(nuevoNodo);
                nodosAbiertos.poll();
                ultimoNodo = nuevoNodo;
                isSearching = false;
                return;
            }
            if (!equals(dimensions[i], nodoActual.getDimensionActual())){
                actualizarNodoAbierto(nuevoNodo);
            }
            
        }

        
    }

    protected boolean estaEnNodosCerrados(Dimension dimension){

        for (int i = 0; i < nodosCerrados.size(); i++){
            if(equals(dimension, nodosCerrados.get(i).getDimensionActual())){
                return true;
            }
        }
        return false;
    }

    protected void actualizarNodoAbierto (NodoAStar nuevoNodo){

        for (NodoAStar nodo: nodosAbiertos){
            if(equals(nuevoNodo.getDimensionActual(), nodo.getDimensionActual())){
                if ((nuevoNodo.getDistanciaRecorrida()) < nodo.getDistanciaRecorrida()){
                    nodosAbiertos.remove(nodo);
                    nodo.setNodoAnterior(nuevoNodo.getNodoAnterior(), nuevoNodo.getDistanciaRecorrida());
                    nodosAbiertos.add(nodo);
                }
                return;
            }
        }
        nodosAbiertos.add(nuevoNodo);
        return;
    }

    @Override
    public Dimension[] getPath(){

        Vector<NodoAStar> bestPathInverted = new Vector<>();
        NodoAStar nodo = ultimoNodo;
        while (nodo.getDistanciaRecorrida() != 0) {
            bestPathInverted.add(nodo);
            nodo = nodo.getNodoAnterior();
        }

        Dimension[] bestPath = new Dimension[bestPathInverted.size()];
        nodo = ultimoNodo;
        for (int i = 0; i <= bestPathInverted.size() - 1; i++){
            bestPath[i] = nodo.getDimensionActual();
            nodo = nodo.getNodoAnterior();
        }
        
        for (Dimension nodito:bestPath){
        }

        return bestPath;

    }


}