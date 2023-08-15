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

    public ArrayList<Vertex> executeDijkstra(Graph graph, Vertex selectedVertex, Vertex dijkstraVertex, TextArea log) {
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

        log.appendText("--------------------\nDijkstra\nPath:\n");
        ArrayList<Vertex> shortestPath = paths.get(dijkstraVertex.getIndiceVertice());
        for (Vertex vertex : shortestPath) {
            log.appendText(vertex.getIndiceVertice() + " ");
        }
        log.appendText("\nDistance: " + distance[dijkstraVertex.getIndiceVertice()] + "\n");

        return shortestPath;

    }

    public Map<String, List<?>> executePrim(Vertex selectedVertex, Graph graph) {
        ArrayList<Vertex> listaDiVertici = new ArrayList<>();
        ArrayList<Edge> listaDiArchi = new ArrayList<>();

        listaDiVertici.add(selectedVertex);

        while (listaDiVertici.size() < graph.getSize()) {
            Edge minEdge = null;
            int minWeight = Integer.MAX_VALUE;

            for (Vertex vertex : listaDiVertici) {
                ArrayList<Edge> viciniEdge = vertex.getEdges();
                for (Edge edge : viciniEdge) {
                    if ((listaDiVertici.contains(edge.getV1()) && !listaDiVertici.contains(edge.getV2()))
                            || (!listaDiVertici.contains(edge.getV1()) && listaDiVertici.contains(edge.getV2()))) {
                        if (!listaDiArchi.contains(edge) && edge.getWeight() < minWeight) {
                            minEdge = edge;
                            minWeight = edge.getWeight();
                        }
                    }
                }
            }

            if (minEdge != null) {
                listaDiArchi.add(minEdge);
                if (!listaDiVertici.contains(minEdge.getV1())) {
                    listaDiVertici.add(minEdge.getV1());
                } else {
                    listaDiVertici.add(minEdge.getV2());
                }
            }
        }

        Map<String, List<?>> result = new HashMap<>();
        result.put("archi", listaDiArchi);
        result.put("vertici", listaDiVertici);

        return result;
    }

    public ArrayList<Edge> executeKruskal(Graph graph) {
        ArrayList<Edge> archiOrdinati = prendiArchiOrdinati(graph);
        ArrayList<ArrayList<Vertex>> vertexs = new ArrayList<>(); // struttura per contenere gli alberi
        ArrayList<Edge> mstEdges = new ArrayList<>(); // Archi dell'MST
        Collections.sort(archiOrdinati);

        for (Edge edge : archiOrdinati) {
            ArrayList<Vertex> albero1 = null;
            ArrayList<Vertex> albero2 = null;

            // Trova gli alberi contenenti i vertici dell'arco
            for (ArrayList<Vertex> tree : vertexs) {
                if (tree.contains(edge.getV1())) {
                    albero1 = tree;
                }
                if (tree.contains(edge.getV2())) {
                    albero2 = tree;
                }
            }

            if (albero1 != albero2) {
                if (albero1 != null && albero2 != null) {
                    albero1.addAll(albero2);
                    vertexs.remove(albero2);
                } else if (albero1 != null) {
                    albero1.add(edge.getV2());
                } else if (albero2 != null) {
                    albero2.add(edge.getV1());
                } else {
                    ArrayList<Vertex> nuovoAlbero = new ArrayList<>();
                    nuovoAlbero.add(edge.getV1());
                    nuovoAlbero.add(edge.getV2());
                    vertexs.add(nuovoAlbero);
                }
                mstEdges.add(edge); // Aggiungi l'arco all'MST
            } else if (albero1 == null && albero2 == null) {
                ArrayList<Vertex> nuovoAlbero = new ArrayList<>();
                nuovoAlbero.add(edge.getV1());
                nuovoAlbero.add(edge.getV2());
                vertexs.add(nuovoAlbero);
                mstEdges.add(edge); // Aggiungi l'arco all'MST

            }
        }
        for (ArrayList<Vertex> v : vertexs) {
            for (Vertex vertex : v) {
                System.out.print(vertex.getIndiceVertice() + " ");
            }
            System.out.println("");
        }
        System.out.println(mstEdges.size());
        for (Edge edge : mstEdges) {
            System.out.print(edge.getWeight() + " ");
        }
        System.out.println(" ");
        return mstEdges;
    }

    private ArrayList<Edge> prendiArchiOrdinati(Graph graph) {
        ArrayList<Edge> edges = new ArrayList<>();
        HashSet<Edge> uniqueEdges = new HashSet<>(); // Utilizziamo un HashSet per mantenere archi unici
        for (Vertex v : graph.getGraph()) {
            ArrayList<Edge> archi = v.getEdges();
            for (Edge edge : archi) {
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