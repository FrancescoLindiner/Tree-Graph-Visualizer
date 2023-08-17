package code;

import javafx.scene.paint.Color;

public class Edge implements Comparable<Edge> {
    
    private int weight;
    private Vertex v1, v2; // v1 & v2 are the vertices of the edge
    private Color color;

    public Edge(int weight, Vertex v1, Vertex v2) {
        this.weight = weight;
        this.v1 = v1;
        this.v2 = v2;
    }

    public Vertex getV1() {
        return v1;
    }

    public Vertex getV2() {
        return v2;
    }

    public int getWeight() {
        return weight;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public int compareTo(Edge o) { // to sorting the edge for the kruskal algorithm
        return Integer.compare(this.weight, o.weight);
    }
}