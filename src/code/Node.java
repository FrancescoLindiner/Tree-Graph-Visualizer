package code;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class Node {

    private int indiceNodo;
    private int nNodes; // number of children
    private Circle circle;
    private ArrayList<Line> lines; // lines incident at the node
    private ArrayList<Node> vicini; //neightbots of the node, includes the father and the children
    private ArrayList<Node> figli; // children of the node
    private Text numberText; // text to insert the index at the center of the circle

    public Node(int indiceNodo, Circle circle) {
        this.indiceNodo = indiceNodo;
        this.circle = circle;
        nNodes = 0;
        lines = new ArrayList<>();
        vicini = new ArrayList<>();
        figli = new ArrayList<>();
    }

    public Circle getCircle() {
        return this.circle;
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
        circle.setFill(Color.RED);
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

    public void set_nNodes() { // to increment the number of children
        this.nNodes++;
    }

    public int get_nNode() {
        return this.nNodes;
    }

    public int getIndiceNodo() {
        return this.indiceNodo;
    }

    @Override
    public String toString() {
        return "Indice nodo " + indiceNodo;
    }
}