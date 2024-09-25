import java.awt.Dimension;

public class NodoAStar(){
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

    public setNodoAnterior(NodoAStar nod, int dist){
        nodoAnterior = nod;
        distanciaRecorrida = dist;
        sumaDistancias = distanciaRecorrida + estimacionHeuristica;
    }
}