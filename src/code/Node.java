package code;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class Node {

    private int indiceNodo;
    private int nNodes;
    private int puntatoreFiglioSx, puntatoreFiglioDx;
    Circle circle;
    private ArrayList<Line> lines;
    private ArrayList<Node> vicini;
    private ArrayList<Node> figli;
    private Text numberText;

    public Node(int indiceNodo, Circle circle) {
        this.indiceNodo = indiceNodo;
        this.circle = circle;
        nNodes = 0;
        lines = new ArrayList<>();
        vicini = new ArrayList<>();
        figli = new ArrayList<>();
    }

    public void setFigli(Node figlio) {
        figli.add(figlio);
    }

    public ArrayList<Node> getFigli() {
        return this.figli;
    }

    public int getNumFigli() {
        return this.figli.size();
    }

    public void setNumberText(Text numberText) {
        this.numberText = numberText;
    }

    public Text getNumberText() {
        return numberText;
    }

    public void setColor() {
        circle.setFill(Color.BROWN);
    }

    public void setVicino(Node node) {
        vicini.add(node);
    }

    public ArrayList<Line> getLines() {
        return this.lines;
    }

    public void setLine(Line line) {
        lines.add(line);
    }

    public int getSizeVicini() {
        return lines.size();
    }

    public Line getLine(int index) {
        return this.lines.get(index);
    }

    public Circle getVicino(int index) {
        return vicini.get(index).circle;
    }

    public void set_nNodes() {
        this.nNodes++;
    }

    public int get_nNode() {
        return this.nNodes;
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