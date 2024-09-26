
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class BreadthFirstSearchEngine extends AbstractSearchEngine{
    
    public BreadthFirstSearchEngine(int width, int height){
        super(width, height);
        casillasPorVisitar = new  LinkedList<>();

        iterateSearch(startLoc, 1);
        
        
    }
    private Queue<DimensionTracker> casillasPorVisitar;
    private Vector<DimensionTracker> casillasVisitadas;

    // Este vector contendra a vectores de Dimensiones que tendran 2 valores cada 1. El primero de una dimension o casilla
    // y el segundo de la primera dimension o casilla que haga de predecesor.

    private void iterateSearch(Dimension loc, int depth) {
        long startTime = System.currentTimeMillis();
        if (isSearching == false) return;
        
        maze.setValue(loc.width, loc.height, (short)depth);
        DimensionTracker previousDimension = new DimensionTracker(loc);
        casillasPorVisitar.add(previousDimension);
        casillasVisitadas = new Vector<>();

        isSearching = true; 
        while (!casillasPorVisitar.isEmpty() && isSearching == true){
            DimensionTracker currentLoc = casillasPorVisitar.peek();
            Dimension [] moves = getPossibleMoves(casillasPorVisitar.peek().getCurrentDimension());
            for (int i = 0; i < 4 ; i++){

                if (moves[i] == null || estaEnCasillasVisitadas(moves[i])) continue; 

                
                if (equals(moves[i], goalLoc)) {
                    System.out.println("Found the goal at " + moves[i].width +
                                       ", " + moves[i].height);
                    isSearching = false;
                    maxDepth = depth;
                    casillasVisitadas.add(new DimensionTracker(goalLoc, new DimensionTracker(moves[i], currentLoc)));
                    return;
                }
                casillasPorVisitar.add(new DimensionTracker(moves[i], currentLoc));
            }
            casillasVisitadas.add(currentLoc);
            casillasPorVisitar.poll();
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Tiempo de ejecución: " + executionTime + " ms");
        
        return;

       
    }
                                
    @Override
    public Dimension[] getPath() {
        DimensionTracker currentDimensionTracker = casillasVisitadas.getLast();
        Dimension [] caminoDimensiones = new Dimension[currentDimensionTracker.getDepth() - 1];
        currentDimensionTracker = currentDimensionTracker.getPrev();

        for (int i = currentDimensionTracker.getDepth() - 1 ; i > -1; i --){
            caminoDimensiones[i] = currentDimensionTracker.getCurrentDimension();
            currentDimensionTracker = currentDimensionTracker.getPrev();
        }
        //while (equals(dimensionBuscada, ))
        return caminoDimensiones;
    }

    private boolean estaEnCasillasVisitadas(Dimension dim){
        for (int i = 0; i < casillasVisitadas.size(); i++){
            if (equals(casillasVisitadas.get(i).getCurrentDimension(), dim)){
                return true;
            }
        }
        return false;
    }

}
