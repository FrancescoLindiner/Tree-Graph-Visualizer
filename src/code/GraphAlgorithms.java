package code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javafx.scene.control.TextArea;

public class GraphAlgorithms {

    public int[] executeBellmanFord(Graph graph, Vertex selectedVertex) {
        int[] distance = new int[graph.getSize()];

        for (int i = 0; i < distance.length; i++) {
            distance[i] = Integer.MAX_VALUE;
        }

        distance[selectedVertex.getVertexIndex()] = 0; // Inizializza la distanza del nodo sorgente a 0

        for (int i = 0; i < graph.getSize() - 1; i++) {
            System.out.println(graph.getSize());
            for (int a = 0; a < graph.getSize(); a++) { // Itera su tutti i nodi del grafo
                Vertex currentVertex = graph.getVertex(a);
                for (Edge e : currentVertex.getEdges()) {
                    Vertex v1 = currentVertex;
                    Vertex v2 = e.getV2();
                    if (v2.equals(currentVertex)) {
                        v2 = e.getV1();
                    }

                    int weight = e.getWeight();

                    if (distance[v1.getVertexIndex()] != Integer.MAX_VALUE &&
                            distance[v1.getVertexIndex()] + weight < distance[v2.getVertexIndex()]) {
                        distance[v2.getVertexIndex()] = distance[v1.getVertexIndex()] + weight;
                    }
                }
            }
        }

        // Verifica la presenza di cicli negativi
        for (int a = 0; a < graph.getSize(); a++) {
            Vertex currentVertex = graph.getVertex(a);
            for (Edge e : currentVertex.getEdges()) {
                Vertex v1 = currentVertex;
                Vertex v2 = e.getV2();
                if (v2.equals(currentVertex)) {
                    v2 = e.getV1();
                }

                int weight = e.getWeight();

                if (distance[v1.getVertexIndex()] != Integer.MAX_VALUE &&
                        distance[v1.getVertexIndex()] + weight < distance[v2.getVertexIndex()]) {
                    return null;
                }
            }
        }

        return distance;
    }

    public ArrayList<Vertex> executeDijkstra(Graph graph, Vertex selectedVertex, Vertex dijkstraVertex, TextArea log) {
        ArrayList<ArrayList<Vertex>> paths = new ArrayList<>();
        int[] distance = new int[graph.getSize()];
        Vertex[] predecessors = new Vertex[graph.getSize()];

        for (int i = 0; i < distance.length; i++) {
            distance[i] = Integer.MAX_VALUE;
            predecessors[i] = null;
        }

        distance[selectedVertex.getVertexIndex()] = 0;

        PriorityQueue<Vertex> queue = new PriorityQueue<>(
                Comparator.comparingInt(vertex -> distance[vertex.getVertexIndex()]));
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
                if (weight < 0) {
                    return null;
                }

                if (distance[v1.getVertexIndex()] != Integer.MAX_VALUE &&
                        distance[v1.getVertexIndex()] + weight < distance[v2.getVertexIndex()]) {
                    distance[v2.getVertexIndex()] = distance[v1.getVertexIndex()] + weight;
                    predecessors[v2.getVertexIndex()] = v1;
                    queue.add(v2);
                }
            }
        }

        for (int i = 0; i < graph.getSize(); i++) {
            ArrayList<Vertex> shortestPath = new ArrayList<>();
            Vertex currentVertex = graph.getVertex(i);
            while (currentVertex != null) {
                shortestPath.add(currentVertex);
                currentVertex = predecessors[currentVertex.getVertexIndex()];
            }
            Collections.reverse(shortestPath);
            paths.add(shortestPath);
        }

        log.appendText("--------------------\nDijkstra\nPath:\n");
        ArrayList<Vertex> shortestPath = paths.get(dijkstraVertex.getVertexIndex());
        for (Vertex vertex : shortestPath) {
            log.appendText(vertex.getVertexIndex() + " ");
        }
        log.appendText("\nDistance: " + distance[dijkstraVertex.getVertexIndex()] + "\n");

        return shortestPath;

    }

    public Map<String, List<?>> executePrim(Vertex selectedVertex, Graph graph) {
        ArrayList<Vertex> vertexList = new ArrayList<>();
        ArrayList<Edge> edgeList = new ArrayList<>();

        vertexList.add(selectedVertex);

        while (vertexList.size() < graph.getSize()) {
            Edge minEdge = null;
            int minWeight = Integer.MAX_VALUE;

            for (Vertex vertex : vertexList) {
                ArrayList<Edge> viciniEdge = vertex.getEdges();
                for (Edge edge : viciniEdge) {
                    if ((vertexList.contains(edge.getV1()) && !vertexList.contains(edge.getV2()))
                            || (!vertexList.contains(edge.getV1()) && vertexList.contains(edge.getV2()))) {
                        if (!edgeList.contains(edge) && edge.getWeight() < minWeight) {
                            minEdge = edge;
                            minWeight = edge.getWeight();
                        }
                    }
                }
            }

            if (minEdge != null) {
                edgeList.add(minEdge);
                if (!vertexList.contains(minEdge.getV1())) {
                    vertexList.add(minEdge.getV1());
                } else {
                    vertexList.add(minEdge.getV2());
                }
            }
        }

        Map<String, List<?>> result = new HashMap<>();
        result.put("archi", edgeList);
        result.put("vertici", vertexList);

        return result;
    }

    public ArrayList<Edge> executeKruskal(Graph graph) {
        ArrayList<Edge> orderedEdges = getOrderedEdges(graph);
        ArrayList<ArrayList<Vertex>> vertexs = new ArrayList<>(); // to contain tree
        ArrayList<Edge> mstEdges = new ArrayList<>(); // edges of the minimum spanning tree
        Collections.sort(orderedEdges);

        for (Edge edge : orderedEdges) {
            ArrayList<Vertex> tree1 = null;
            ArrayList<Vertex> tree2 = null;

            // Trova gli alberi contenenti i vertici dell'arco
            for (ArrayList<Vertex> tree : vertexs) {
                if (tree.contains(edge.getV1())) {
                    tree1 = tree;
                }
                if (tree.contains(edge.getV2())) {
                    tree2 = tree;
                }
            }

            if (tree1 != tree2) {
                if (tree1 != null && tree2 != null) {
                    tree1.addAll(tree2);
                    vertexs.remove(tree2);
                } else if (tree1 != null) {
                    tree1.add(edge.getV2());
                } else if (tree2 != null) {
                    tree2.add(edge.getV1());
                } else {
                    ArrayList<Vertex> newTree = new ArrayList<>();
                    newTree.add(edge.getV1());
                    newTree.add(edge.getV2());
                    vertexs.add(newTree);
                }
                mstEdges.add(edge); // Aggiungi l'arco all'MST
            } else if (tree1 == null && tree2 == null) {
                ArrayList<Vertex> newTree = new ArrayList<>();
                newTree.add(edge.getV1());
                newTree.add(edge.getV2());
                vertexs.add(newTree);
                mstEdges.add(edge); // Aggiungi l'arco all'MST

            }
        }
        return mstEdges;
    }

    private ArrayList<Edge> getOrderedEdges(Graph graph) {
        ArrayList<Edge> edges = new ArrayList<>();
        HashSet<Edge> uniqueEdges = new HashSet<>(); // HashSet per mantenere archi unici
        for (Vertex v : graph.getGraph()) {
            ArrayList<Edge> edges2 = v.getEdges();
            for (Edge edge : edges2) {
                if (uniqueEdges.add(edge)) {
                    // L'arco non era presente nel set, quindi lo aggiungiamo sia al set che
                    // all'elenco
                    edges.add(edge);
                }
            }
        }
        return edges;
    }
}