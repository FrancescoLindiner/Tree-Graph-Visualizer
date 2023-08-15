package code;

import java.util.ArrayDeque;
import java.util.ArrayList;
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
                
                ArrayList<Node> figli = node.getFigli();
                for (Node node2 : figli) {
                    stack.push(node2);
                }
                
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}