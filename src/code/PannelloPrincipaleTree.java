package code;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PannelloPrincipaleTree implements Initializable {

    @FXML
    private Pane pane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextArea log;

    @FXML
    private Slider slider;

    static int index = 0;
    private Circle selectedCircle;
    private Node selectedNode;
    Tree tree = new Tree();
    Parent parent;
    Stage stage;
    Scene scene;
    private double mouseX, mouseY; // Store the initial mouse click position

    DFS dfs = new DFS();
    BFS bfs = new BFS();

    public void onStart() {
        Circle circle = new Circle(20, Color.BLUE);
        circle.setCenterX(350);
        circle.setCenterY(40);

        Node root = new Node(0, circle);
        tree.addNode(root);

        Text numberText = new Text("0");
        numberText.setFill(Color.WHITE);
        numberText.setX(circle.getCenterX() - 5);
        numberText.setY(circle.getCenterY() + 5);
        root.setNumberText(numberText);
        pane.getChildren().addAll(circle, numberText);

        circle.setOnMouseClicked(event -> {
            log.appendText("Root " + root.getIndexNode() + "\n");
            circle.setFill(Color.GREEN);
            selectedCircle = circle;
            selectedNode = root;
        });

        circle.setOnMousePressed(e -> {
            mouseX = e.getSceneX() - circle.getCenterX();
            mouseY = e.getSceneY() - circle.getCenterY();
        });

        circle.setOnMouseDragged(e -> {
            double newCircleX = e.getSceneX() - mouseX;
            double newCircleY = e.getSceneY() - mouseY;

            circle.setCenterX(newCircleX);
            circle.setCenterY(newCircleY);

            numberText.setX(newCircleX - 5);
            numberText.setY(newCircleY + 5);

            for (int i = 0; i < selectedNode.getSizeVicini(); i++) {
                selectedNode.getLine(i).setStartX(selectedCircle.getCenterX());
                selectedNode.getLine(i).setStartY(selectedCircle.getCenterY());
                selectedNode.getLine(i).setEndX(selectedNode.getVicino(i).getCenterX());
                selectedNode.getLine(i).setEndY(selectedNode.getVicino(i).getCenterY());
            }
        });
    }

    @FXML
    void buttonInstruction(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("How to use the app");
        alert.setHeaderText(null);

        String bullet = "\u2022";

        String contentText = bullet
                + "To insert a node select a node and click 'Insert node'\n"
                + bullet + "You can move a node by double-clicking it and moving it with the mouse";

        alert.setContentText(contentText);

        String customStyle = "-fx-font-size: 16px;";
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(customStyle);

        alert.showAndWait();
    }

    @FXML
    void buttonIndietro(ActionEvent event) throws IOException {
        buttonReset(event);
        parent = FXMLLoader.load(getClass()
                .getResource("../resources/PannelloPrincipale.fxml"));
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        scene = new Scene(parent, 900, 700);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Interfaccia principale");
        stage.show();
    }

    @FXML
    void buttonDFS(ActionEvent event) {
        log.appendText("---------------------\nStarting DFS...\n");
        dfs.executeDFS(tree.getRoot(), tree, slider.valueProperty().doubleValue(), log);
    }

    @FXML
    void buttonBFS(ActionEvent event) {
        log.appendText("---------------------\nStarting BFS...\n");
        bfs.executeBFS(tree.getRoot(), tree, slider.valueProperty().doubleValue(), log);
    }

    @FXML
    void buttonDeleteNode(ActionEvent event) {
        removeNodeAndDescendants(selectedNode);
    }

    private void removeNodeAndDescendants(Node node) {
        ArrayList<Node> children = node.getChildren();

        pane.getChildren().remove(node.getCircle());
        pane.getChildren().remove(node.getLine(0));
        pane.getChildren().remove(node.getNumberText());
        tree.deleteNode(node);

        for (Node child : children) {
            removeNodeAndDescendants(child);
        }

        Node padre = tree.getFather(selectedNode);
        if (padre != null) {
            padre.removeChildren(selectedNode);
        }
        pane.getChildren().remove(selectedNode.getLine(0));
        pane.getChildren().remove(selectedNode.getCircle());
        pane.getChildren().remove(node.getNumberText());
        tree.deleteNode(selectedNode);
    }

    @FXML
    Node buttonInsertNode(ActionEvent event) {
        if (selectedNode==null) {
            selectedNode = tree.getRoot();
            selectedCircle = selectedNode.getCircle();
        }
        Circle node = new Circle(20, Color.BLUE);
        Node children = new Node(++index, node);

        int numChildren = selectedNode.get_nNode();

        tree.addNode(children);
        selectedNode.setFigli(children);
        selectedNode.set_nNodes();

        changePosition(numChildren);

        Text numberText = new Text(Integer.toString(children.getIndexNode()));
        numberText.setFill(Color.WHITE);
        numberText.setX(node.getCenterX() - 5);
        numberText.setY(node.getCenterY() + 5);
        children.setNumberText(numberText);
        pane.getChildren().addAll(node, numberText);

        log.appendText("Node " + children.getIndexNode() + " added\n");
        node.setOnMouseClicked(e -> {
            selectedNode = children;
            log.appendText("Index " + children.getIndexNode() + "\n");

            node.setFill(Color.GREEN);
            selectedCircle.setFill(Color.BLUE);
            selectedCircle = node;
        });

        Line connectionLine = new Line(
                selectedCircle.getCenterX(), selectedCircle.getCenterY(),
                node.getCenterX(), node.getCenterY());

        selectedNode.setLine(connectionLine);
        children.setLine(connectionLine);
        selectedNode.setVicino(children);
        children.setVicino(selectedNode);

        changePositionLine(children);

        pane.getChildren().add(connectionLine);
        connectionLine.toBack();

        node.setOnMousePressed(e -> {
            mouseX = e.getSceneX() - node.getCenterX();
            mouseY = e.getSceneY() - node.getCenterY();
        });

        node.setOnMouseDragged(e -> {
            double newCircleX = e.getSceneX() - mouseX;
            double newCircleY = e.getSceneY() - mouseY;

            node.setCenterX(newCircleX);
            node.setCenterY(newCircleY);

            numberText.setX(newCircleX - 5);
            numberText.setY(newCircleY + 5);

            for (int i = 0; i < selectedNode.getSizeVicini(); i++) {
                selectedNode.getLine(i).setStartX(selectedCircle.getCenterX());
                selectedNode.getLine(i).setStartY(selectedCircle.getCenterY());
                selectedNode.getLine(i).setEndX(selectedNode.getVicino(i).getCenterX());
                selectedNode.getLine(i).setEndY(selectedNode.getVicino(i).getCenterY());
            }
        });
        return children;
    }

    private void changePositionLine(Node figlio) {

        for (int i = 0; i < selectedNode.getSizeVicini(); i++) { // to move the children
            selectedNode.getLine(i).setStartX(selectedCircle.getCenterX());
            selectedNode.getLine(i).setStartY(selectedCircle.getCenterY());
            selectedNode.getLine(i).setEndX(selectedNode.getVicino(i).getCenterX());
            selectedNode.getLine(i).setEndY(selectedNode.getVicino(i).getCenterY());
        }
        ArrayList<Node> figli = selectedNode.getChildren();
        for (Node node : figli) { // per spostare i number text
            node.getNumberText().setX(node.getCircle().getCenterX() - 5);
            node.getNumberText().setY(node.getCircle().getCenterY() + 5);
        }

        for (Node node : figli) { // to move the children's children
            for (int i = 0; i < node.getSizeVicini(); i++) {
                node.getLine(i).setStartX(node.getCircle().getCenterX());
                node.getLine(i).setStartY(node.getCircle().getCenterY());
                node.getLine(i).setEndX(node.getVicino(i).getCenterX());
                node.getLine(i).setEndY(node.getVicino(i).getCenterY());

            }
        }
    }

    private void changePosition(int numFigli) {
        int x;
        ArrayList<Node> figli = selectedNode.getChildren();
        if (numFigli == 0) {
            x = 0;
        } else {
            x = 50 * numFigli;
        }

        for (Node node : figli) {
            node.getCircle().setCenterX(selectedNode.getCircle().getCenterX() - x);
            node.getCircle().setCenterY(selectedNode.getCircle().getCenterY() + 70);
            x -= 100;

        }
    }

    @FXML
    void buttonReset(ActionEvent event) {
        tree.deleteTree();
        pane.getChildren().clear();
        log.clear();

        // reset the variable at the initial state
        index = 0;
        selectedCircle = null;
        selectedNode = null;
        tree = new Tree();

        // start the scene
        onStart();
    }

    @FXML
    void buttonRandom(ActionEvent event) {

        buttonReset(event);
        Random random = new Random();

        log.appendText("Generating a tree...\n");

        int randomDim = random.nextInt(4) + 3;
        selectedNode = tree.getRoot();
        selectedCircle = selectedNode.getCircle();

        for (int i = 0; i < randomDim; i++) {
            int randomChildren = random.nextInt(3) + 1;

            for (int j = 1; j <= randomChildren; j++) {
                Node n = buttonInsertNode(event);
                if (!tree.checkCoordianteNode(n)) {
                    // si deve cancellare
                    deleteNode(n);
                }
            }
            Node node = tree.selectRandomNode();
            selectedNode = node;
            selectedCircle = selectedNode.getCircle();
        }
    }

    private void deleteNode(Node n) {
        pane.getChildren().removeAll(n.getCircle(), n.getNumberText());
        for (int i = 0; i < n.getSizeVicini(); i++) {
            pane.getChildren().remove(n.getLine(i));
        }

        tree.deleteNode(n);

        if (n == selectedNode) {
            selectedNode = null;
            selectedCircle = null;
        }
        for (Node node : tree.getNodes()) {
            System.out.println(node.getIndexNode());
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        onStart();
    }
}