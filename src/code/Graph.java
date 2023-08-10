package code;

import java.util.ArrayList;

public class Graph {

    private ArrayList<Vertex> graph;

    public Graph() {
        graph = new ArrayList<>();
    }

    public void addVertex(Vertex vertex) {
        graph.add(vertex);
    }

    public void deleteTree() {
        graph.clear();
    }
}
