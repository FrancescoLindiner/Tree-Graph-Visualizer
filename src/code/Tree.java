package code;

import java.util.ArrayList;

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
}
