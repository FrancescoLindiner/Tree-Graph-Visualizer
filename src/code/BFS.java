package code;

import java.util.ArrayList;
import java.util.LinkedList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

public class BFS {

    LinkedList<Node> queue = new LinkedList<>();

    public void executeBFS(Node root, Tree tree, double speed, TextArea log) {
        queue.add(root);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(speed), event -> {
            if (queue.isEmpty()) {
                timeline.stop();
                return;
            }

            Node node = queue.removeFirst();

            if (queue != null) {
                log.appendText("Node visited " + node.getIndexNode() + "\n");

                node.setColor();
                ArrayList<Node> children = node.getChildren();
                for (Node node2 : children) {
                    queue.add(node2);
                }
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}
