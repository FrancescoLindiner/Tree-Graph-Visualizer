package code;

import java.util.LinkedList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

public class BFS {
    
    LinkedList<Node> coda = new LinkedList<>();

    public void executeBFS(Node root, Tree tree, double speed, TextArea log) {
        coda.add(root);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(speed), event -> {
            if (coda.isEmpty()) {
                timeline.stop();
                return;
            }

            Node node = coda.removeFirst();

            if (node != null) {
                log.appendText("Node visited " + node.getIndiceNodo() + "\n");

                node.setColor();
                if (node.getPuntatoreFiglioSx() != 0) {
                    Node leftChild = tree.getNodeSx(node);
                    coda.add(leftChild);
                }
                if (node.getPuntatoreFiglioDx() != 0) {
                    Node rightChild = tree.getNodeDx(node);
                    coda.add(rightChild);
                }
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}
