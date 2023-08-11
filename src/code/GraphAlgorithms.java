package code;

public class GraphAlgorithms {

    public int[] executeBellmanFord(Graph graph, Vertex selectedVertex) {
        int[] distance = new int[graph.getSize()];

        for (int i = 0; i < distance.length; i++) {
            distance[i] = Integer.MAX_VALUE;
        }

        distance[selectedVertex.getIndiceVertice()] = 0; // Inizializza la distanza del nodo sorgente a 0

        for (int i = 0; i < graph.getSize() - 1; i++) {
            for (int a = 0; a < graph.getSize(); a++) { // Itera su tutti i nodi del grafo
                Vertex currentVertex = graph.getVertex(a);
                for (Edge e : currentVertex.getEdges()) {
                    Vertex v1 = currentVertex;
                    Vertex v2 = e.getV2();
                    if (v2.equals(currentVertex)) {
                        v2 = e.getV1();
                    }

                    int weight = e.getWeight();

                    if (distance[v1.getIndiceVertice()] != Integer.MAX_VALUE &&
                            distance[v1.getIndiceVertice()] + weight < distance[v2.getIndiceVertice()]) {
                        distance[v2.getIndiceVertice()] = distance[v1.getIndiceVertice()] + weight;
                    }
                }
            }
        }
        
        return distance;
    }
}