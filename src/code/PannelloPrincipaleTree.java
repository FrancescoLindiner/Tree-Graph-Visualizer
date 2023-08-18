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

    static int indici = 0;
    private Circle selectedPallino;
    private Node selectedNode;
    Tree tree = new Tree();
    Parent parent;
    Stage stage;
    Scene scene;
    private double mouseX, mouseY; // Store the initial mouse click position
    static int livello = 0;

    DFS dfs = new DFS();
    BFS bfs = new BFS();

    public void onStart() {
        Circle pallino = new Circle(20, Color.BLUE);
        pallino.setCenterX(350);
        pallino.setCenterY(40);

        Node root = new Node(0, pallino);
        tree.addNode(root);

        Text numberText = new Text("0");
        numberText.setFill(Color.WHITE);
        numberText.setX(pallino.getCenterX() - 5);
        numberText.setY(pallino.getCenterY() + 5);
        root.setNumberText(numberText);
        pane.getChildren().addAll(pallino, numberText);

        pallino.setOnMouseClicked(event -> {
            log.appendText("Root " + root.getIndiceNodo() + "\n");
            pallino.setFill(Color.GREEN);
            selectedPallino = pallino;
            selectedNode = root;
        });

        pallino.setOnMousePressed(e -> {
            mouseX = e.getSceneX() - pallino.getCenterX();
            mouseY = e.getSceneY() - pallino.getCenterY();
        });

        pallino.setOnMouseDragged(e -> {
            double newCircleX = e.getSceneX() - mouseX;
            double newCircleY = e.getSceneY() - mouseY;

            pallino.setCenterX(newCircleX);
            pallino.setCenterY(newCircleY);

            numberText.setX(newCircleX - 5);
            numberText.setY(newCircleY + 5);

            for (int i = 0; i < selectedNode.getSizeVicini(); i++) {
                selectedNode.getLine(i).setStartX(selectedPallino.getCenterX());
                selectedNode.getLine(i).setStartY(selectedPallino.getCenterY());
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
        ArrayList<Node> children = node.getFigli();

        pane.getChildren().remove(node.getCircle());
        pane.getChildren().remove(node.getLine(0));
        pane.getChildren().remove(node.getNumberText());
        tree.deleteNode(node);

        for (Node child : children) {
            removeNodeAndDescendants(child);
        }

        Node padre = tree.getPadre(selectedNode);
        if (padre != null) {
            padre.removeFiglio(selectedNode);
        }
        pane.getChildren().remove(selectedNode.getLine(0));
        pane.getChildren().remove(selectedNode.getCircle());
        pane.getChildren().remove(node.getNumberText());
        tree.deleteNode(selectedNode);
    }

    @FXML
    Node buttonInsertNode(ActionEvent event) {
        Circle node = new Circle(20, Color.BLUE);
        Node figlio = new Node(++indici, node);

        int numFigli = selectedNode.get_nNode();

        tree.addNode(figlio);
        selectedNode.setFigli(figlio);
        selectedNode.set_nNodes();

        changePosition(numFigli);

        Text numberText = new Text(Integer.toString(figlio.getIndiceNodo()));
        numberText.setFill(Color.WHITE);
        numberText.setX(node.getCenterX() - 5);
        numberText.setY(node.getCenterY() + 5);
        figlio.setNumberText(numberText);
        pane.getChildren().addAll(node, numberText);

        log.appendText("Node " + figlio.getIndiceNodo() + " added\n");
        node.setOnMouseClicked(e -> {
            selectedNode = figlio;
            log.appendText("Index " + figlio.getIndiceNodo() + "\n");

            node.setFill(Color.GREEN);
            selectedPallino.setFill(Color.BLUE);
            selectedPallino = node;
        });

        Line connectionLine = new Line(
                selectedPallino.getCenterX(), selectedPallino.getCenterY(),
                node.getCenterX(), node.getCenterY());

        selectedNode.setLine(connectionLine);
        figlio.setLine(connectionLine);
        selectedNode.setVicino(figlio);
        figlio.setVicino(selectedNode);

        changePositionLine(figlio);

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
                selectedNode.getLine(i).setStartX(selectedPallino.getCenterX());
                selectedNode.getLine(i).setStartY(selectedPallino.getCenterY());
                selectedNode.getLine(i).setEndX(selectedNode.getVicino(i).getCenterX());
                selectedNode.getLine(i).setEndY(selectedNode.getVicino(i).getCenterY());
            }
        });
        return figlio;
    }

    private void changePositionLine(Node figlio) {

        for (int i = 0; i < selectedNode.getSizeVicini(); i++) { // to move the children
            selectedNode.getLine(i).setStartX(selectedPallino.getCenterX());
            selectedNode.getLine(i).setStartY(selectedPallino.getCenterY());
            selectedNode.getLine(i).setEndX(selectedNode.getVicino(i).getCenterX());
            selectedNode.getLine(i).setEndY(selectedNode.getVicino(i).getCenterY());
        }
        ArrayList<Node> figli = selectedNode.getFigli();
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
        ArrayList<Node> figli = selectedNode.getFigli();
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
        indici = 0;
        selectedPallino = null;
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
        selectedPallino = selectedNode.getCircle();

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
            selectedPallino = selectedNode.getCircle();
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
            selectedPallino = null;
        }
        for (Node node : tree.getNodes()) {
            System.out.println(node.getIndiceNodo());
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        onStart();
    }
}