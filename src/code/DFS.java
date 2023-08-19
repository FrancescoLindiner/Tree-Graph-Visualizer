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
                log.appendText("Node visited " + node.getIndexNode() + "\n");

                node.setColor();
                
                ArrayList<Node> children = node.getChildren();
                int size = children.size();
                for (int i = size-1; i>=0; i--) { // the loop goes backwards to start the algorithm from the leftmost node
                    stack.push(children.get(i));
                }
                
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}