package code;

public class Edge {
    
    private int weight;
    private Vertex v1, v2;

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
}