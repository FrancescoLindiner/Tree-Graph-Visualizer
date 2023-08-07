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

    public void setFiglioSx(Node node, int indiceFiglioSx) {
        node.puntatoreFiglioSx = indiceFiglioSx;
    }

    public void setFiglioDx(Node node, int indiceFiglioDx) {
        node.puntatoreFiglioDx = indiceFiglioDx;
    }

    public int getPuntatoreFiglioSx() {
        return this.puntatoreFiglioSx;
    }

    public int getPuntatoreFiglioDx() {
        return this.puntatoreFiglioDx;
    }
}