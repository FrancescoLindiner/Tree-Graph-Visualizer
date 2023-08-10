package code;

import java.util.ArrayList;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Vertex {
    
    private int indiceVertice;
    private ArrayList<Vertex> vicini;
    private ArrayList<Line> lines;
    private Circle circle;

    public Vertex(int indiceVertice, Circle circle) {
        this.indiceVertice = indiceVertice;
        vicini = new ArrayList<>();
        lines = new ArrayList<>();
        this.circle = circle;
    }

    public void setLine(Line line) {
        lines.add(line);
    }

    public Line getLine(int index) {
        return this.lines.get(index);
    }

    public int getIndiceVertice() {
        return this.indiceVertice;
    }

    public void setVicino(Vertex vertex) {
        vicini.add(vertex);
    }

    public int getSizeVicini() {
        return this.vicini.size();
    }

    public Circle getVicino(int index) {
        return vicini.get(index).circle;
    }
}
