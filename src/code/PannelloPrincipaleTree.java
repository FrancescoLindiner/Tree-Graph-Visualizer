package code;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.animation.Timeline;
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

    Parent parent;
    Stage stage;
    Scene scene;
    private double mouseX, mouseY; // Store the initial mouse click position
    static int livello = 0;

    @FXML
    private Pane pane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextArea log;

    @FXML
    private Slider slider;

    static int indici = 0;
    private Circle selectedPallino; // Variabile per memorizzare il pallino selezionato
    private Node selectedNode; // Variabile per memorizzare il nodo selezionato
    Tree tree = new Tree();

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

        String bullet = "\u2022"; // Codice Unicode per il carattere del punto

        String contentText = bullet
                + "To insert a node select a node and click either 'Insert right node' or 'Insert left node'\n"
                + bullet + "You can move a node by double-clicking it and moving it with the mouse";

        alert.setContentText(contentText);

        String customStyle = "-fx-font-size: 16px;";
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(customStyle);

        alert.showAndWait();
    }

    DFS dfs = new DFS();
    BFS bfs = new BFS();

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

    // Metodo ricorsivo per rimuovere un nodo e i suoi discendenti
    private void removeNodeAndDescendants(Node node) {
        ArrayList<Node> children = node.getFigli();

        // Rimuovi il nodo e i suoi disegni associati
        pane.getChildren().remove(node.getCircle());
        pane.getChildren().remove(node.getLine(0));
        pane.getChildren().remove(node.getNumberText());
        tree.deleteNode(node);

        // Chiamata ricorsiva per rimuovere i discendenti
        for (Node child : children) {
            removeNodeAndDescendants(child);
        }

        // Eseguire questa parte per rimuovere il nodo selezionato e tutti i suoi
        // discendenti
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

        // Disegna la linea che connette i nodi
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

        for (int i = 0; i < selectedNode.getSizeVicini(); i++) { // per spostare le linee dei figli
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

        for (Node node : figli) { // per spostare le linee dei figli dei digli
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

    /*
     * private int getCoordinateX(int numFigli) {
     * if (numFigli == 0) {
     * x = 0;
     * return 0;
     * } else if (numFigli == 1) {
     * x += 55;
     * return x;
     * } else if (numFigli == 2) {
     * x = -55;
     * return x;
     * } else if (numFigli % 2 == 0) {
     * x = -x;
     * return x;
     * } else {
     * x = Math.abs(x);
     * x += 50;
     * return x;
     * }
     * }
     */

    void generateLeftNode() {
        Circle node = new Circle(20, Color.BLUE);
        Node figlio = new Node(++indici, node);
        // selectedPallino = figlio.circle;
        node.setCenterX(selectedPallino.getCenterX() - 55);
        node.setCenterY(selectedPallino.getCenterY() + 60);

        tree.addNode(figlio);
        selectedNode.set_nNodes();

        Text numberText = new Text(Integer.toString(figlio.getIndiceNodo()));
        numberText.setFill(Color.WHITE);
        numberText.setX(node.getCenterX() - 5);
        numberText.setY(node.getCenterY() + 5);
        pane.getChildren().addAll(node, numberText);
        log.appendText("Add node " + figlio.getIndiceNodo() + "\n");
        node.setOnMouseClicked(e -> {
            selectedNode = figlio;
            log.appendText("Nodo " + figlio.getIndiceNodo() + "\n");

            node.setFill(Color.GREEN);
            selectedPallino.setFill(Color.BLUE);
            selectedPallino = node;
        });

        // Disegna la linea che connette i nodi
        Line connectionLine = new Line(
                selectedPallino.getCenterX(), selectedPallino.getCenterY(),
                node.getCenterX(), node.getCenterY());

        selectedNode.setLine(connectionLine);
        figlio.setLine(connectionLine);
        selectedNode.setVicino(figlio);
        figlio.setVicino(selectedNode);

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
    }

    void generateRightNode() {
        Circle node = new Circle(20, Color.BLUE); // Crea il pallino colorato

        node.setCenterX(selectedPallino.getCenterX() + 55);
        node.setCenterY(selectedPallino.getCenterY() + 60);
        Node figlio = new Node(++indici, node); // Crea il nodo
        tree.addNode(figlio); // Lo aggiunge all'albero

        selectedNode.set_nNodes();
        selectedNode.setVicino(figlio);
        figlio.setVicino(selectedNode);

        Text numberText = new Text(Integer.toString(figlio.getIndiceNodo()));
        numberText.setFill(Color.WHITE);
        numberText.setX(node.getCenterX() - 5); // Imposta la posizione X del testo all'interno del cerchio
        numberText.setY(node.getCenterY() + 5);

        pane.getChildren().addAll(node, numberText);

        log.appendText("Add node " + figlio.getIndiceNodo() + "\n");
        node.setOnMouseClicked(e -> {
            selectedNode = figlio;
            log.appendText("Nodo " + figlio.getIndiceNodo() + "\n");

            node.setFill(Color.GREEN);
            selectedPallino.setFill(Color.BLUE);
            selectedPallino = node;
        });

        // Disegna la linea che connette i nodi
        Line connectionLine = new Line(
                selectedPallino.getCenterX(), selectedPallino.getCenterY(),
                node.getCenterX(), node.getCenterY());

        selectedNode.setLine(connectionLine);
        figlio.setLine(connectionLine);

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
    }

    @FXML
    void buttonReset(ActionEvent event) {
        tree.deleteTree();
        pane.getChildren().clear();
        log.clear();

        // Reimposta le variabili allo stato iniziale
        indici = 0;
        selectedPallino = null;
        selectedNode = null;
        tree = new Tree();

        // Avvia nuovamente la scena
        onStart();
    }

    private Timeline timeline;

    @FXML
    void buttonRandom(ActionEvent event) {

        buttonReset(event);
        Random random = new Random();

        log.appendText("Generating a tree...\n");
        timeline = new Timeline();

        int randomDim = random.nextInt(4) + 3;
        selectedNode = tree.getRoot();
        selectedPallino = selectedNode.getCircle();
        // ArrayList<Node> nodes = new ArrayList<>();

        /*
         * for (int i = 0; i < random.nextInt(4)+1; i++) {
         * Node n = buttonInsertNode(event);
         * nodes.add(n);
         * }
         * for (int i = 0; i < random.nextInt(4)+1; i++) {
         * Node n = nodes.remove(0);
         * buttonInsertNode(event);
         * }
         */

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

        /*
         * double frameDurationMillis = 100;
         * for (int i = 0; i < randomDim; i++) {
         * KeyFrame keyFrame = new KeyFrame(Duration.millis(i * frameDurationMillis), e
         * -> {
         * 
         * int randomChildren = random.nextInt(3) + 1;
         * System.out.println(randomChildren);
         * for (int j = 1; j <= randomChildren; j++) {
         * buttonInsertNode(event);
         * Node node = tree.selectRandomNode();
         * selectedNode = node;
         * selectedPallino = selectedNode.circle;
         * }
         * });
         * timeline.getKeyFrames().add(keyFrame);
         * 
         * }
         * timeline.setOnFinished(e -> {
         * log.appendText("Tree generated\n");
         * });
         * timeline.setCycleCount(1);
         * timeline.play();
         */
    }

    private void deleteNode(Node n) {
        // Rimuovi il nodo e le linee dal Pane
        pane.getChildren().removeAll(n.getCircle(), n.getNumberText()); // Rimuovi il cerchio e il testo dal Pane
        for (int i = 0; i < n.getSizeVicini(); i++) {
            pane.getChildren().remove(n.getLine(i)); // Rimuovi le linee di connessione dal Pane
        }

        // Rimuovi il nodo dall'albero
        tree.deleteNode(n);

        // Eventualmente, reimposta il nodo selezionato e il pallino selezionato se
        // necessario
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