package code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class Vertex {

    private int indiceVertice;
    private ArrayList<Vertex> vicini;
    private ArrayList<Line> lines;
    private Circle circle;
    private Map<Line, Text> weights = new HashMap<>();
    private ArrayList<Edge> edges;

    public Vertex(int indiceVertice, Circle circle) {
        this.indiceVertice = indiceVertice;
        vicini = new ArrayList<>();
        lines = new ArrayList<>();
        this.circle = circle;
        this.edges = new ArrayList<>();
    }

    public void setEdge(Edge edge) {
        edges.add(edge);
    }

    public Text getWeight(Line line) {
        return weights.get(line);
    }

    public void setLine(Line line, Text weight) {
        lines.add(line);
        weights.put(line, weight);
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

    public ArrayList<Vertex> getVicino(Vertex v) {
        return vicini;
    }

    public Circle getCircle() {
        return this.circle;
    }

    public Text getNumberText() {
        // Restituisci l'etichetta numerica associata a questo vertice (se presente)
        for (Map.Entry<Line, Text> entry : weights.entrySet()) {
            if (entry.getValue() != null) {
                return entry.getValue();
            }
        }
        return null; // Restituisci null se l'etichetta numerica non è presente
    }

    public ArrayList<Edge> getEdges() {
        return this.edges;
    }
}