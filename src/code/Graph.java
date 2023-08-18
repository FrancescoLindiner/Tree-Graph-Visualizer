package code;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.shape.Line;

public class Graph {

    private ArrayList<Vertex> graph;

    public Graph() {
        graph = new ArrayList<>();
    }

    public void addVertex(Vertex vertex) {
        graph.add(vertex);
    }

    public void deleteVertex(Vertex vertex) {
        graph.remove(vertex);
    }

    public void deleteGraph() {
        graph.clear();
    }

    public Vertex getVertex(int i) {
        return graph.get(i);
    }

    public Vertex getRandomVertex() {
        Random rand = new Random();
        Vertex v = null;
        v = graph.get(rand.nextInt(graph.size()));
        return v;
    }

    public ArrayList<Vertex> getGraph() {
        return graph;
    }

    public int getSize() {
        return graph.size();
    }
}
