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
            int randomIndex = rand.nextInt(tree.size());
            randomNode = tree.get(randomIndex);
        } while (randomNode.equals(getRoot()) && randomNode.get_nNode() != 0);
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
        boolean overlap = true;
        double x = n.getCircle().getCenterX();
        double y = n.getCircle().getCenterY();
        for (Node node : tree) {
            double x1 = node.getCircle().getCenterX();
            double y1 = node.getCircle().getCenterY();
            if (node != n && (x == x1 && y == y1)) {
                overlap = false;
            }
        }
        return overlap;
    }

    public void deleteNode(Node n) {
        tree.remove(n);
    }

    public Node getFather(Node selectedNode) {
        for (Node node : tree) {
            ArrayList<Node> figli = node.getChildren();
            for (Node node2 : figli) {
                if (node2 == selectedNode) {
                    return node;
                }
            }
        }
        return null;
    }
}