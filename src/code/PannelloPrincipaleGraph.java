package code;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PannelloPrincipaleGraph implements Initializable {

    Parent parent;
    Stage stage;
    Scene scene;
    static int indici = 0;
    Graph graph = new Graph();
    private Circle selectedPallino; // Variabile per memorizzare il pallino selezionato
    private Vertex selectedVertex;
    private double mouseX, mouseY; // Store the initial mouse click position
    private boolean isDragging = false;

    @FXML
    private Button button;

    @FXML
    private TextArea log;

    @FXML
    private Pane pane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Slider slider;

    @FXML
    void buttonIndietro(ActionEvent event) throws IOException {
        // buttonReset(event); DA CHIAMARE QUANDO SI VA INDIETRO
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
    void buttonBellmanFord(ActionEvent event) {

    }

    @FXML
    void buttonDijkstra(ActionEvent event) {

    }

    @FXML
    void buttonInsertNode(ActionEvent event) {
        Circle vertexCircle = new Circle(20, Color.RED);
        Vertex vertex = new Vertex(indici++, vertexCircle);

        vertexCircle.setCenterX(selectedPallino.getCenterX() - 55);
        vertexCircle.setCenterY(selectedPallino.getCenterY() + 60);

        graph.addVertex(vertex);
        selectedVertex.setVicino(vertex);
        vertex.setVicino(selectedVertex);

        Text numberText = new Text(Integer.toString(vertex.getIndiceVertice()));
        numberText.setFill(Color.WHITE);
        numberText.setX(vertexCircle.getCenterX() - 5);
        numberText.setY(vertexCircle.getCenterY() + 5);
        pane.getChildren().addAll(vertexCircle, numberText);

        log.appendText("Add vertex " + vertex.getIndiceVertice() + "\n");

        vertexCircle.setOnMouseClicked(e -> {
            selectedVertex = vertex;
            log.appendText("Vertex " + vertex.getIndiceVertice() + "\n");

            vertexCircle.setFill(Color.GREEN);
            selectedPallino.setFill(Color.BLUE);
            selectedPallino = vertexCircle;
        });

        // Disegna la linea che connette i nodi
        Line connectionLine = new Line(selectedPallino.getCenterX(), selectedPallino.getCenterY(),
                vertexCircle.getCenterX(), vertexCircle.getCenterY());
        pane.getChildren().add(connectionLine);

        vertex.setLine(connectionLine);
        selectedVertex.setLine(connectionLine);

        vertexCircle.setOnMousePressed(e -> {
            mouseX = e.getSceneX() - vertexCircle.getCenterX();
            mouseY = e.getSceneY() - vertexCircle.getCenterY();
        });

        vertexCircle.setOnMouseDragged(e -> {
            double newCircleX = e.getSceneX() - mouseX;
            double newCircleY = e.getSceneY() - mouseY;

            vertexCircle.setCenterX(newCircleX);
            vertexCircle.setCenterY(newCircleY);

            for (int i = 0; i < selectedVertex.getSizeVicini(); i++) {
                selectedVertex.getLine(i).setStartX(selectedPallino.getCenterX());
                selectedVertex.getLine(i).setStartY(selectedPallino.getCenterY());
                selectedVertex.getLine(i).setEndX(selectedVertex.getVicino(i).getCenterX());
                selectedVertex.getLine(i).setEndY(selectedVertex.getVicino(i).getCenterY());
            }
            isDragging = true;

        });

        vertexCircle.setOnMouseReleased(e -> {
            if (isDragging) {
                isDragging = false;
                // Perform any necessary updates after dragging ends
                // For example, updating vertex positions in your graph data structure
            }
        });
    }

    @FXML
    void buttonKruskal(ActionEvent event) {

    }

    @FXML
    void buttonPrim(ActionEvent event) {

    }

    @FXML
    void buttonRandom(ActionEvent event) {

    }

    @FXML
    void buttonReset(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onStart();
    }

    private void onStart() {
        Circle pallino = new Circle(20, Color.RED);
        pallino.setCenterX(350);
        pallino.setCenterY(250);

        Vertex vertex = new Vertex(indici++, pallino);
        graph.addVertex(vertex);

        Text numberText = new Text("0");
        numberText.setFill(Color.WHITE);
        numberText.setX(pallino.getCenterX() - 5);
        numberText.setY(pallino.getCenterY() + 5);
        pane.getChildren().addAll(pallino, numberText);

        pallino.setOnMouseClicked(event -> {
            log.appendText("Vertex " + vertex.getIndiceVertice() + "\n");
            pallino.setFill(Color.GREEN);
            selectedPallino = pallino;
            selectedVertex = vertex;
        });
    }
}