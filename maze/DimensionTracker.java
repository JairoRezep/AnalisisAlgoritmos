import java.awt.Dimension;

public class DimensionTracker {
    private DimensionTracker previousDimension;
    private Dimension currentDimension;
    private int  depth;

    

    public DimensionTracker(){ 
    }

    public DimensionTracker(Dimension dim){
        currentDimension = dim;
        depth = 1;
    }

    public DimensionTracker(Dimension dim, DimensionTracker prevDim){
        currentDimension = dim;
        previousDimension = prevDim;
        depth = previousDimension.getDepth() + 1;
    }

    public int getDepth(){
        return depth;
    }

    public DimensionTracker getPrev(){
        return previousDimension;
    }

    public Dimension getCurrentDimension(){
        return currentDimension;
    }
}
