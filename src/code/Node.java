package code;

public class Node {
    
    private int indiceNodo;
    private int puntatoreFiglioSx, puntatoreFiglioDx;

    public Node(int indiceNodo) {
        this.indiceNodo = indiceNodo;
    }
    
    public int getIndiceNodo() {
        return this.indiceNodo;
    }
}