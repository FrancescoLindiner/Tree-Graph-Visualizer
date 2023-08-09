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
            int indiceCasuale = rand.nextInt(tree.size());
            randomNode = tree.get(indiceCasuale);
        } while ((randomNode.getIndiceNodo() == getRoot().getIndiceNodo()) &&
                (randomNode.getPuntatoreFiglioDx() == 0 || randomNode.getPuntatoreFiglioSx() == 0));

        return randomNode;
    }
}
