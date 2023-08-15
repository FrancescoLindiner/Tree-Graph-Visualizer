package code;

import java.util.ArrayList;
import java.util.Random;

public class Tree {

    private ArrayList<Node> tree;

    public ArrayList<Node> getNodes() {
        return tree;
    }

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
            int indiceCasuale = rand.nextInt(tree.size());
            randomNode = tree.get(indiceCasuale);
        } while (randomNode.equals(getRoot()) && randomNode.get_nNode() != 0);
        System.out.println(randomNode.getIndiceNodo() + "\n\n");
        return randomNode;
    }

    public boolean verificaNodo(Node selectedNode) {
        Node father = getFather(selectedNode);

        if (father.getPuntatoreFiglioDx() == selectedNode.getIndiceNodo()) {
            Node leftChildern = tree.get(father.getPuntatoreFiglioSx());
            if (leftChildern.getPuntatoreFiglioDx() == 0) {
                return true;
            }
        } else {
            Node rightChildern = tree.get(father.getPuntatoreFiglioDx());
            if (rightChildern.getPuntatoreFiglioSx() == 0) {
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

    public boolean checkX(int x) {
        for (Node node : tree) {
            if (node.circle.getCenterX() == x) {
                return false;
            }
        }
        return true;
    }

    public boolean checkCoordianteNode(Node n) {
        // devo scorrere tutto l'albero e vedere se le coordinate di n sono contenute
        // nelle coordinatae di qualche altro nodo
        boolean overlap = true;
        double x = n.circle.getCenterX();
        double y = n.circle.getCenterY();
        for (Node node : tree) {
            double x1 = node.circle.getCenterX();
            double y1 = node.circle.getCenterY();
            // double distance = Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
            if (node != n && (x == x1 && y == y1)) {
                if ((x + 10 > x1 - 10) || (x - 10 < x1 + 10) || (x1 + 10 > x - 10) || (x1 - 10 < x + 10)) {
                    overlap = false;
                }
            }
        }
        return overlap;
    }

    public void deleteNode(Node n) {
        tree.remove(n);
    }
}