import java.awt.Dimension;


public class NodoAStar{

    private Dimension dimensionActual;
    private NodoAStar nodoAnterior;
    private int distanciaRecorrida, estimacionHeuristica, sumaDistancias;

    public NodoAStar(){
        distanciaRecorrida = -1;
    }

    public NodoAStar(Dimension dim, NodoAStar prev, int dis, int est){
        dimensionActual = dim;
        nodoAnterior = prev;
        distanciaRecorrida = dis;
        estimacionHeuristica = est;
        sumaDistancias = distanciaRecorrida + estimacionHeuristica;
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