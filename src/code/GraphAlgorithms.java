package code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

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

    public ArrayList<Vertex> executeDijkstra(Graph graph, Vertex selectedVertex, Vertex dijkstraVertex) {
        ArrayList<ArrayList<Vertex>> paths = new ArrayList<>();
        int[] distance = new int[graph.getSize()];
        Vertex[] predecessors = new Vertex[graph.getSize()];

        for (int i = 0; i < distance.length; i++) {
            distance[i] = Integer.MAX_VALUE;
            predecessors[i] = null;
        }

        distance[selectedVertex.getIndiceVertice()] = 0;

        PriorityQueue<Vertex> queue = new PriorityQueue<>(
                Comparator.comparingInt(vertex -> distance[vertex.getIndiceVertice()]));
        queue.add(selectedVertex);

        while (!queue.isEmpty()) {
            Vertex currentVertex = queue.poll();

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
                    predecessors[v2.getIndiceVertice()] = v1;
                    queue.add(v2);
                }
            }
        }

        for (int i = 0; i < graph.getSize(); i++) {
            ArrayList<Vertex> shortestPath = new ArrayList<>();
            Vertex currentVertex = graph.getVertex(i);
            while (currentVertex != null) {
                shortestPath.add(currentVertex);
                currentVertex = predecessors[currentVertex.getIndiceVertice()];
            }
            Collections.reverse(shortestPath);
            paths.add(shortestPath);
        }

        ArrayList<Vertex> shortestPath = paths.get(dijkstraVertex.getIndiceVertice());
        for (Vertex vertex : shortestPath) {
            System.out.print(vertex.getIndiceVertice() + " ");
        }
        System.out.println(" - Distance: " + distance[dijkstraVertex.getIndiceVertice()]);

        return shortestPath;

    }

    /*
     * public void executeDijkstra(Graph graph, Vertex selectedVertex, Vertex
     * dijkstraVertex) {
     * int[] distance = new int[graph.getSize()];
     * ArrayList<Vertex> vicini = selectedVertex.getVicino(selectedVertex);
     * ArrayList<Edge> edges = selectedVertex.getEdges();
     * for (int i = 0; i < distance.length; i++) {
     * if (i < vicini.size() && vicini.get(i).getIndiceVertice() == i) {
     * distance[i] = edges.get(i).getWeight();
     * } else {
     * distance[i] = Integer.MAX_VALUE;
     * }
     * }
     * distance[selectedVertex.getIndiceVertice()] = 0;
     * for (int i = 0; i < distance.length; i++) {
     * System.out.println(distance[i] + " ");
     * }
     * }
     */
}