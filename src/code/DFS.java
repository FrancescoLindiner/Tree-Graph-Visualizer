package code;

import java.util.ArrayDeque;
import java.util.Deque;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

public class DFS {

    public void executeDFS(Node root, Tree tree, double speed, TextArea log) {
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(root);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(speed), event -> {
            if (stack.isEmpty()) {
                timeline.stop();
                return;
            }

            Node node = stack.pop();
            if (node != null) {
                log.appendText("Node visited " + node.getIndiceNodo() + "\n");

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