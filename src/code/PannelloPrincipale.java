package code;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class PannelloPrincipale implements Initializable {

    @FXML
    private Pane pane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextArea log;

    @FXML
    private Slider slider;

    // private double currentZoom = 1.0; // Valore di zoom iniziale

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
        pane.getChildren().addAll(pallino, numberText);

        pallino.setOnMouseClicked(event -> {
            log.appendText("Root " + root.getIndiceNodo() + "\n");
            log.appendText(
                    "Root's childern: " + root.getPuntatoreFiglioSx() + " - " + root.getPuntatoreFiglioDx() + "\n");
            pallino.setFill(Color.GREEN);
            selectedPallino = pallino;
            selectedNode = root;
        });
    }

    DFS dfs = new DFS();
    BFS bfs = new BFS();

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

    void generateLeftNode() {
        Circle node = new Circle(20, Color.BLUE);
        Node figlio = new Node(++indici, node);
        // selectedPallino = figlio.circle;
        node.setCenterX(selectedPallino.getCenterX() - 55);
        node.setCenterY(selectedPallino.getCenterY() + 60);

        tree.addNode(figlio);
        selectedNode.setFiglioSx(figlio.getIndiceNodo());
        Text numberText = new Text(Integer.toString(figlio.getIndiceNodo()));
        numberText.setFill(Color.WHITE);
        numberText.setX(node.getCenterX() - 5);
        numberText.setY(node.getCenterY() + 5);
        pane.getChildren().addAll(node, numberText);
        log.appendText("Add node " + figlio.getIndiceNodo() + "\n");
        node.setOnMouseClicked(e -> {
            selectedNode = figlio;
            log.appendText("Nodo " + figlio.getIndiceNodo() + "\n");
            log.appendText("Node's childrens " + selectedNode.getPuntatoreFiglioSx() + " - "
                    + selectedNode.getPuntatoreFiglioDx() + "\n");

            node.setFill(Color.GREEN);
            selectedPallino.setFill(Color.BLUE);
            selectedPallino = node;
        });

        // Disegna la linea che connette i nodi
        Line connectionLine = new Line(
                selectedPallino.getCenterX() - 13, selectedPallino.getCenterY() + 15,
                node.getCenterX() + 13, node.getCenterY() - 15);
        pane.getChildren().add(connectionLine);
    }

    @FXML
    void buttonInsertLeftNode(ActionEvent event) {
        if (selectedNode == null) {
            log.appendText("First select a node\nwhere to insert a node\n");
            return;
        }
        if (selectedNode.getPuntatoreFiglioSx() != 0) {
            log.appendText("The node has already\na left children\n");
            return;
        }
        generateLeftNode();
    }

    void generateRightNode() {
        Circle node = new Circle(20, Color.BLUE); // Crea il pallino colorato

        // Il problema è che si deve aggiornare selectedPallino al nodo che si sta
        // considerando
        // forse conviene fare un nuovo metodo

        node.setCenterX(selectedPallino.getCenterX() + 55);
        node.setCenterY(selectedPallino.getCenterY() + 60);
        Node figlio = new Node(++indici, node); // Crea il nodo
        tree.addNode(figlio); // Lo aggiunge all'albero
        selectedNode.setFiglioDx(figlio.getIndiceNodo());
        Text numberText = new Text(Integer.toString(figlio.getIndiceNodo())); 
        numberText.setFill(Color.WHITE);
        numberText.setX(node.getCenterX() - 5); // Imposta la posizione X del testo all'interno del cerchio
        numberText.setY(node.getCenterY() + 5);

        pane.getChildren().addAll(node, numberText);

        log.appendText("Add node " + figlio.getIndiceNodo() + "\n");
        node.setOnMouseClicked(e -> {
            selectedNode = figlio;
            log.appendText("Nodo " + figlio.getIndiceNodo() + "\n");
            log.appendText("Node's childrens " + selectedNode.getPuntatoreFiglioSx() + " - "
                    + selectedNode.getPuntatoreFiglioDx() + "\n");

            node.setFill(Color.GREEN);
            selectedPallino.setFill(Color.BLUE);
            selectedPallino = node;
        });

        // Disegna la linea che connette i nodi
        Line connectionLine = new Line(
                selectedPallino.getCenterX() + 13, selectedPallino.getCenterY() + 15,
                node.getCenterX() - 13, node.getCenterY() - 15);
        pane.getChildren().add(connectionLine);
    }

    @FXML
    void buttonInsertRightNode(ActionEvent event) {
        if (selectedNode == null) {
            log.appendText("First select a node\nwhere to insert a node\n");
            return;
        }
        if (selectedNode.getPuntatoreFiglioDx() != 0) {
            log.appendText("The node has already\na right children\n");
            return;
        }
        generateRightNode();
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

    Random random = new Random();

    /*
     * Dalla radice genero due figli
     * prendo uno dei due figli e genero un figlio
     * prendo un qualsiasi figlio che non ha già due figli e genero un figlio e così
     * via
     */
    @FXML
    void buttonRandom(ActionEvent event) {
        int randomDim = random.nextInt(6) + 5;
        selectedNode = tree.getRoot();
        selectedPallino = selectedNode.circle;
        generateLeftNode();
        generateRightNode();

        for (int i = 0; i < randomDim; i++) {
            Node node = tree.selectRandomNode();
            selectedNode = node;
            selectedPallino = selectedNode.circle;
            if (random.nextInt(2) == 0 && selectedNode.getPuntatoreFiglioSx()==0) {
                generateLeftNode();
            } else {
                if (selectedNode.getPuntatoreFiglioDx()==0) {
                    generateRightNode();
                }                
            }
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        onStart();
        /*
         * pane.setOnScroll(event -> {
         * if (event.isControlDown()) { // Controlla se il tasto Ctrl è premuto
         * double zoomFactor = 1.05; // Fattore di zoom (puoi regolare questo valore)
         * 
         * if (event.getDeltaY() < 0) {
         * // Zoom out
         * zoomFactor = 1 / zoomFactor;
         * }
         * 
         * // Effettua il zoom all'interno del pannello
         * pane.setScaleX(pane.getScaleX() * zoomFactor);
         * pane.setScaleY(pane.getScaleY() * zoomFactor);
         * 
         * // Aggiorna lo stato del zoom corrente
         * currentZoom *= zoomFactor;
         * 
         * // Impedisce il passaggio a livelli di zoom troppo piccoli o grandi
         * if (currentZoom < 0.1) {
         * currentZoom = 0.1;
         * } else if (currentZoom > 10) {
         * currentZoom = 10;
         * }
         * 
         * event.consume(); // Impedisce la propagazione dell'evento
         * }
         * });
         */
    }
}