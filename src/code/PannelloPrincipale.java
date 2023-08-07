package code;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class PannelloPrincipale implements Initializable {

    @FXML
    private Pane pane;

    static int indici=0;
    private Circle selectedPallino; // Variabile per memorizzare il pallino selezionato
    private Node selectedNode; // Variabile per memorizzare il nodo selezionato
    Tree tree = new Tree();

    public void onStart() {
        Circle pallino = new Circle(20, Color.BLUE);
        pallino.setCenterX(450);
        pallino.setCenterY(50);
        pane.getChildren().add(pallino);
 
        Node root = new Node(0, pallino);
        tree.addNode(root);

        pallino.setOnMouseClicked(event -> {
            System.out.println("Hai cliccato il la radice " + root.getIndiceNodo());
            System.out.println("La radice ha figli con indice: " + root.getPuntatoreFiglioSx() + " e " + root.getPuntatoreFiglioDx());
            pallino.setFill(Color.GREEN);
            selectedPallino = pallino;
            selectedNode = root;
        });
    }

    DFS dfs = new DFS();

    @FXML
    void buttonDFS(ActionEvent event) {
        dfs.executeDFS(tree.getRoot(), tree);
        System.out.println(tree.getRoot().toString());
    }

    @FXML
    void buttonBFS(ActionEvent event) {

    }

    @FXML
    void buttonInsertLeftNode(ActionEvent event) { 
        Circle node = new Circle(20, Color.BLUE);
        node.setCenterX(selectedPallino.getCenterX()-70);
        node.setCenterY(selectedPallino.getCenterY()+40);
        Node figlio = new Node(++indici, node);
        tree.addNode(figlio);
        selectedNode.setFiglioSx(figlio.getIndiceNodo());
        pane.getChildren().add(node);
        node.setOnMouseClicked(e -> {
            selectedNode = figlio;
            System.out.println("Hai cliccato il nodo da sinsitra" + figlio.getIndiceNodo());
            System.out.println("Ha figli " + selectedNode.getPuntatoreFiglioSx());
            System.out.println("Ha figli " + selectedNode.getPuntatoreFiglioDx());

            node.setFill(Color.GREEN);
            selectedPallino.setFill(Color.BLUE);
            selectedPallino = node;
        });

        System.out.println(selectedNode.getIndiceNodo());

        // Disegna la linea che connette i nodi
        Line connectionLine = new Line(
            selectedPallino.getCenterX(), selectedPallino.getCenterY(),
            node.getCenterX(), node.getCenterY()
        );
        pane.getChildren().add(connectionLine);
    }

    @FXML
    void buttonInsertRightNode(ActionEvent event) {
        Circle node = new Circle(20, Color.BLUE); // Crea il pallino colorato
        node.setCenterX(selectedPallino.getCenterX()+70);
        node.setCenterY(selectedPallino.getCenterY()+40);
        Node figlio = new Node(++indici, node); // Crea il nodo
        tree.addNode(figlio); // Lo aggiunge all'albero
        selectedNode.setFiglioDx(figlio.getIndiceNodo());
        pane.getChildren().add(node); // Lo disegna nello schermo
        node.setOnMouseClicked(e -> {
            selectedNode = figlio;
            System.out.println("Hai cliccato il nodo da destra" + figlio.getIndiceNodo());
            System.out.println("Ha figli " + selectedNode.getPuntatoreFiglioSx());
            System.out.println("Ha figli " + selectedNode.getPuntatoreFiglioDx());
            node.setFill(Color.GREEN);
            selectedPallino.setFill(Color.BLUE);
            selectedPallino = node;
        });

        System.out.println(selectedNode.getIndiceNodo());

        // Disegna la linea che connette i nodi
        Line connectionLine = new Line(
            selectedPallino.getCenterX(), selectedPallino.getCenterY(),
            node.getCenterX(), node.getCenterY()
        );
        pane.getChildren().add(connectionLine);
    }

    @FXML
    void buttonReset(ActionEvent event) {
        tree.deleteTree();
        pane.getChildren().clear();

        // Reimposta le variabili allo stato iniziale
        indici = 0;
        selectedPallino = null;
        selectedNode = null;
        tree = new Tree();

        // Avvia nuovamente la scena
        onStart();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        onStart();
    }

}
