package code;

import java.util.ArrayDeque;
import java.util.Deque;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class DFS {

    public void executeDFS(Node root, Tree tree) {
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(root);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            if (stack.isEmpty()) {
                timeline.stop();
                return;
            }

            Node node = stack.pop();
            if (node != null) {
                System.out.println("Visitato il nodo " + node.getIndiceNodo());
                node.setColor();

                if (node.getPuntatoreFiglioDx() != 0) {
                    Node rightChild = tree.getNodeDx(node);
                    stack.push(rightChild);
                }
                if (node.getPuntatoreFiglioSx() != 0) {
                    Node leftChild = tree.getNodeSx(node);
                    stack.push(leftChild);
                }
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}