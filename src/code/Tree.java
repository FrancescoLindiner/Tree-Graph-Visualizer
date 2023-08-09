package code;

import java.util.ArrayList;
import java.util.Random;

public class Tree {

    private ArrayList<Node> tree;

    public Tree() {
        tree = new ArrayList<>();
    }

    public void addNode(Node node) {
        tree.add(node);
    }

    public void deleteTree() {
        tree.clear();
    }

    public Node getRoot() {
        return tree.get(0);
    }

    public Node getNodeSx(Node node) {
        int indexToSearch = node.getPuntatoreFiglioSx();
        for (Node n : tree) {
            if (n.getIndiceNodo() == indexToSearch) {
                return n;
            }
        }
        return null;
    }

    public Node getNodeDx(Node node) {
        int indexToSearch = node.getPuntatoreFiglioDx();
        for (Node n : tree) {
            if (n.getIndiceNodo() == indexToSearch) {
                return n;
            }
        }
        return null;
    }

    public Node selectRandomNode() {
        Random rand = new Random();
        Node randomNode = null;
        do {
            int indiceCasuale = rand.nextInt(tree.size()); // Genera un indice casuale nell'intervallo valido
            randomNode = tree.get(indiceCasuale); // Ottiene il nodo corrispondente all'indice casuale
        } while (randomNode.equals(getRoot()) && // Continua finché il nodo è la radice
                (randomNode.getPuntatoreFiglioDx() == 0 || randomNode.getPuntatoreFiglioSx() == 0)); // oppure non ha
                                                                                                     // figli

        return randomNode;
    }

    public boolean verificaNodo(Node selectedNode) {
        Node father = getFather(selectedNode);

        if (father.getPuntatoreFiglioDx()==selectedNode.getIndiceNodo()) {
            if (father.getPuntatoreFiglioSx()==0) {
                return true;
            }
            Node leftChildern = tree.get(father.getPuntatoreFiglioSx());
            if (leftChildern.getPuntatoreFiglioDx()==0) {
                return true;
            }
        } else {
            if (father.getPuntatoreFiglioDx()==0) {
                return true;
            }
            Node rightChildern = tree.get(father.getPuntatoreFiglioDx());
            if (rightChildern.getPuntatoreFiglioSx()==0) {
                return true;
            }
        }
        return false;
    }

    public Node getFather(Node selectedNode) {
        for (Node node : tree) {
            if (node.getPuntatoreFiglioDx() == selectedNode.getIndiceNodo()
                    || node.getPuntatoreFiglioSx() == selectedNode.getIndiceNodo()) {
                        return node;
            }
        }
        return null;
    }
}