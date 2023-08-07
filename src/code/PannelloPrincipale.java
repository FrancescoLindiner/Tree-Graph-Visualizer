package code;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PannelloPrincipale implements Initializable {

    @FXML
    private Pane pane;

    static int indici=-1;
    private Circle selectedPallino; // Variabile per memorizzare il pallino selezionato
    Tree tree = new Tree();

    public void onStart() {
        Circle pallino = new Circle(20, Color.BLUE);
        pallino.setCenterX(450);
        pallino.setCenterY(50);
        pane.getChildren().add(pallino);
 
        Node root = new Node(0);
        tree.addNode(root);

        pallino.setOnMouseClicked(event -> {
            System.out.println("Hai cliccato il la radice " + root.getIndiceNodo());
            pallino.setFill(Color.GREEN);
            selectedPallino = pallino;
        });
    }

    @FXML
    void buttonBFS(ActionEvent event) {

    }

    @FXML
    void buttonDFS(ActionEvent event) {

    }

    @FXML
    void buttonInsertLeftNode(ActionEvent event) {
        Circle node = new Circle(20, Color.BLUE);
        node.setCenterX(selectedPallino.getCenterX()-70);
        node.setCenterY(selectedPallino.getCenterY()+40);
        Node figlio = new Node(indici++);
        tree.addNode(figlio);
        pane.getChildren().add(node);
        node.setOnMouseClicked(e -> {
            System.out.println("Hai cliccato il la radice " + figlio.getIndiceNodo());
            node.setFill(Color.GREEN);
            selectedPallino.setFill(Color.BLUE);
            selectedPallino = node;
        });

    }

    @FXML
    void buttonInsertRightNode(ActionEvent event) {
        Circle node = new Circle(20, Color.BLUE);
        node.setCenterX(selectedPallino.getCenterX()+70);
        node.setCenterY(selectedPallino.getCenterY()+40);
        Node figlio = new Node(indici++);
        tree.addNode(figlio);
        pane.getChildren().add(node);
        node.setOnMouseClicked(e -> {
            System.out.println("Hai cliccato il la radice " + figlio.getIndiceNodo());
            node.setFill(Color.GREEN);
            selectedPallino.setFill(Color.BLUE);
            selectedPallino = node;
        });
    }

    @FXML
    void buttonReset(ActionEvent event) {

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        onStart();
    }

}
