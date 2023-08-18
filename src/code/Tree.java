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

    public boolean checkX(int x) {
        for (Node node : tree) {
            if (node.getCircle().getCenterX() == x) {
                return false;
            }
        }
        return true;
    }

    public boolean checkCoordianteNode(Node n) {
        // devo scorrere tutto l'albero e vedere se le coordinate di n sono contenute
        // nelle coordinatae di qualche altro nodo
        boolean overlap = true;
        double x = n.getCircle().getCenterX();
        double y = n.getCircle().getCenterY();
        for (Node node : tree) {
            double x1 = node.getCircle().getCenterX();
            double y1 = node.getCircle().getCenterY();
            // double distance = Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
            if (node != n && (x == x1 && y == y1)) {

                overlap = false;

            }
        }
        return overlap;
    }

    public void deleteNode(Node n) {
        tree.remove(n);
    }

    public Node getPadre(Node selectedNode) {
        for (Node node : tree) {
            ArrayList<Node> figli = node.getFigli();
            for (Node node2 : figli) {
                if (node2 == selectedNode) {
                    return node;
                }
            }
        }
        return null;
    }
}