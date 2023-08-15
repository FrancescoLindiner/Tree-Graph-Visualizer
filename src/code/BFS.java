package code;

import java.util.ArrayList;
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
                ArrayList<Node> figli = node.getFigli();
                for (Node node2 : figli) {
                    coda.add(node2);
                }
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}
