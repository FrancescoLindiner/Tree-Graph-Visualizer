package code;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Node {
    
    private int indiceNodo;
    private int puntatoreFiglioSx, puntatoreFiglioDx;
    Circle circle;

    public Node(int indiceNodo, Circle circle) {
        this.indiceNodo = indiceNodo;
        this.circle = circle;
    }
    
    public void setColor() {
        circle.setFill(Color.BROWN);
    }

    public int getIndiceNodo() {
        return this.indiceNodo;
    }

    public void setFiglioSx(int indiceFiglioSx) {
        this.puntatoreFiglioSx = indiceFiglioSx;
    }

    public void setFiglioDx(int indiceFiglioDx) {
        this.puntatoreFiglioDx = indiceFiglioDx;
    }

    public int getPuntatoreFiglioSx() {
        return this.puntatoreFiglioSx;
    }

    public int getPuntatoreFiglioDx() {
        return this.puntatoreFiglioDx;
    }

    @Override
    public String toString() {
        return "Indice nodo " + indiceNodo;
    }
}