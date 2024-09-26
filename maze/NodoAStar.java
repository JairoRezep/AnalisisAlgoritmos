import java.awt.Dimension;


public class NodoAStar implements Comparable<NodoAStar>{

    private Dimension dimensionActual;
    private NodoAStar nodoAnterior;
    private int distanciaRecorrida, estimacionHeuristica, sumaDistancias;

    public NodoAStar(){
        distanciaRecorrida = -1;
    }

    public NodoAStar(Dimension dim){
        dimensionActual = dim;
        distanciaRecorrida = 0;
    }

    public NodoAStar(Dimension dim, NodoAStar prev, int dis, int est){
        dimensionActual = dim;
        nodoAnterior = prev;
        distanciaRecorrida = dis;
        estimacionHeuristica = est;
        sumaDistancias = distanciaRecorrida + estimacionHeuristica;
    }

    public int compareTo(NodoAStar nodo){
        return Integer.compare(sumaDistancias, nodo.getSumaDistancias());
    }

    public void setNodoAnterior(NodoAStar nod, int dist){
        nodoAnterior = nod;
        distanciaRecorrida = dist;
        sumaDistancias = distanciaRecorrida + estimacionHeuristica;
    }

    public NodoAStar getNodoAnterior(){
        return nodoAnterior;
    }

    public Dimension getDimensionActual(){
        return dimensionActual;
    }
    
    public int getDistanciaRecorrida(){
        return distanciaRecorrida;
    }

    public int getSumaDistancias(){
        return sumaDistancias;
    }

    public int getEstimacionHeuristica(){
        return estimacionHeuristica;
    }
    
}