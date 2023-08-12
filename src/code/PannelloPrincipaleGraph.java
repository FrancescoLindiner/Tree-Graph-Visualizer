package code;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PannelloPrincipaleGraph implements Initializable {

    Parent parent;
    Stage stage;
    Scene scene;
    static int indici = 0;
    Graph graph = new Graph();
    private Circle selectedPallino, selectedPallino2; // Variabile per memorizzare il pallino selezionato
    private Vertex selectedVertex;
    private double mouseX, mouseY; // Store the initial mouse click position
    private boolean isDragging = false;
    private Vertex secondVertex;
    GraphAlgorithms graphAlgorithms = new GraphAlgorithms();

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
    private TextField inputField;

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
    void buttonBellmanFord(ActionEvent event) {
        log.appendText(
                "-------------------------\nBellman-Ford\nFrom vertex " + selectedVertex.getIndiceVertice() + "\n");
        int[] distance = graphAlgorithms.executeBellmanFord(graph, selectedVertex);
        int j = selectedVertex.getIndiceVertice();
        for (int i = 0; i < distance.length; i++) {
            log.appendText("To " + j + ": " + distance[j] + "\n");
            j++;
            if (j > distance.length - 1) {
                j = 0;
            }
        }
    }

    @FXML
    void buttonDijkstra(ActionEvent event) {

    }

    public boolean ctrlPressed = false;

    @FXML
    void buttonInsertNode(ActionEvent event) {
        Circle vertexCircle = new Circle(20, Color.BLUE);
        Vertex vertex = new Vertex(indici++, vertexCircle);
        Edge edge;
        if (!inputField.getText().equals("")) {
            edge = new Edge(Integer.parseInt(inputField.getText()), selectedVertex, vertex);
        } else {
            edge = new Edge(0, selectedVertex, vertex);
        }
        vertex.setEdge(edge);
        selectedVertex.setEdge(edge);

        vertexCircle.setCenterX(selectedPallino.getCenterX() - 55);
        vertexCircle.setCenterY(selectedPallino.getCenterY() + 60);

        graph.addVertex(vertex);
        selectedVertex.setVicino(vertex);
        vertex.setVicino(selectedVertex);

        Text numberText = new Text(Integer.toString(vertex.getIndiceVertice()));
        numberText.setFill(Color.WHITE);
        numberText.setX(vertexCircle.getCenterX() - 5);
        numberText.setY(vertexCircle.getCenterY() + 5);
        numberText.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        pane.getChildren().addAll(vertexCircle, numberText);

        log.appendText("Add vertex " + vertex.getIndiceVertice() + "\n");

        vertexCircle.setOnMouseClicked(e -> {
            if (e.isControlDown()) {
                selectedPallino2 = vertexCircle;
                secondVertex = vertex;
                addConnection(secondVertex, selectedVertex);
                return;
            }
            selectedVertex = vertex;
            log.appendText("Vertex " + vertex.getIndiceVertice() + "\n");

            vertexCircle.setFill(Color.GREEN);
            selectedPallino.setFill(Color.BLUE);
            selectedPallino = vertexCircle;
        });

        // Disegna la linea che connette i nodi
        Line connectionLine = new Line(selectedPallino.getCenterX(), selectedPallino.getCenterY(),
                vertexCircle.getCenterX(), vertexCircle.getCenterY());

        // Calculate the position for the weight text node
        double weightTextX = (selectedPallino.getCenterX() + vertexCircle.getCenterX()) / 2;
        double weightTextY = (selectedPallino.getCenterY() + vertexCircle.getCenterY()) / 2 - 10;

        Text weightText = new Text(weightTextX, weightTextY, inputField.getText());

        pane.getChildren().addAll(connectionLine, weightText);
        connectionLine.toBack();

        vertex.setLine(connectionLine, weightText);
        selectedVertex.setLine(connectionLine, weightText);

        System.out.println("Vertex " + vertex.getIndiceVertice() + "\nVertex " + selectedVertex.getIndiceVertice()
                + "\nWeight " + inputField.getText());

        vertexCircle.setOnMousePressed(e -> {
            mouseX = e.getSceneX() - vertexCircle.getCenterX();
            mouseY = e.getSceneY() - vertexCircle.getCenterY();
        });

        vertexCircle.setOnMouseDragged(e -> {
            double newCircleX = e.getSceneX() - mouseX;
            double newCircleY = e.getSceneY() - mouseY;

            vertexCircle.setCenterX(newCircleX);
            vertexCircle.setCenterY(newCircleY);

            numberText.setX(newCircleX - 5);
            numberText.setY(newCircleY + 5);

            for (int i = 0; i < selectedVertex.getSizeVicini(); i++) {
                selectedVertex.getLine(i).setStartX(selectedPallino.getCenterX());
                selectedVertex.getLine(i).setStartY(selectedPallino.getCenterY());
                selectedVertex.getLine(i).setEndX(selectedVertex.getVicino(i).getCenterX());
                selectedVertex.getLine(i).setEndY(selectedVertex.getVicino(i).getCenterY());
                selectedVertex.getWeight(selectedVertex.getLine(i))
                        .setX((selectedVertex.getLine(i).getStartX() + selectedVertex.getLine(i).getEndX()) / 2);
                selectedVertex.getWeight(selectedVertex.getLine(i))
                        .setY((selectedVertex.getLine(i).getStartY() + selectedVertex.getLine(i).getEndY()) / 2 - 10);

            }
            isDragging = true;

        });

        vertexCircle.setOnMouseReleased(e -> {
            if (isDragging) {
                isDragging = false;
            }
        });
    }

    @FXML
    void buttonKruskal(ActionEvent event) {

    }

    @FXML
    void buttonPrim(ActionEvent event) {

    }

    void buttonInsertRandomNode(ActionEvent event, double d, double f, int weight) {
        Circle vertexCircle = new Circle(20, Color.BLUE);
        Vertex vertex = new Vertex(indici++, vertexCircle);
        Edge edge;
        if (inputField.getText().equals("")) {
            edge = new Edge(weight, selectedVertex, vertex);
        } else {
            edge = new Edge(weight, selectedVertex, vertex);
        }
        vertex.setEdge(edge);
        selectedVertex.setEdge(edge);

        vertexCircle.setCenterX(d);
        vertexCircle.setCenterY(f);

        graph.addVertex(vertex);
        selectedVertex.setVicino(vertex);
        vertex.setVicino(selectedVertex);

        Text numberText = new Text(Integer.toString(vertex.getIndiceVertice()));
        numberText.setFill(Color.WHITE);
        numberText.setX(vertexCircle.getCenterX() - 5);
        numberText.setY(vertexCircle.getCenterY() + 5);
        numberText.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        pane.getChildren().addAll(vertexCircle, numberText);

        log.appendText("Add vertex " + vertex.getIndiceVertice() + "\n");

        vertexCircle.setOnMouseClicked(e -> {
            if (e.isControlDown()) {
                selectedPallino2 = vertexCircle;
                secondVertex = vertex;
                addConnection(secondVertex, selectedVertex);
                return;
            }
            selectedVertex = vertex;
            log.appendText("Vertex " + vertex.getIndiceVertice() + "\n");

            vertexCircle.setFill(Color.GREEN);
            selectedPallino.setFill(Color.BLUE);
            selectedPallino = vertexCircle;
        });

        // Disegna la linea che connette i nodi
        Line connectionLine = new Line(selectedPallino.getCenterX(), selectedPallino.getCenterY(),
                vertexCircle.getCenterX(), vertexCircle.getCenterY());

        // Calculate the position for the weight text node
        double weightTextX = (selectedPallino.getCenterX() + vertexCircle.getCenterX()) / 2;
        double weightTextY = (selectedPallino.getCenterY() + vertexCircle.getCenterY()) / 2 - 10; // Adjust the Y
                                                                                                  // position

        Text weightText;
        if (inputField.getText().equals("")) {
            weightText = new Text(weightTextX, weightTextY, Integer.toString(weight));
        } else {
            weightText = new Text(weightTextX, weightTextY, inputField.getText());
        }
        pane.getChildren().addAll(connectionLine, weightText);
        connectionLine.toBack();

        vertex.setLine(connectionLine, weightText);
        selectedVertex.setLine(connectionLine, weightText);

        vertexCircle.setOnMousePressed(e -> {
            mouseX = e.getSceneX() - vertexCircle.getCenterX();
            mouseY = e.getSceneY() - vertexCircle.getCenterY();
        });

        vertexCircle.setOnMouseDragged(e -> {
            double newCircleX = e.getSceneX() - mouseX;
            double newCircleY = e.getSceneY() - mouseY;

            vertexCircle.setCenterX(newCircleX);
            vertexCircle.setCenterY(newCircleY);

            numberText.setX(newCircleX - 5);
            numberText.setY(newCircleY + 5);

            for (int i = 0; i < selectedVertex.getSizeVicini(); i++) {
                selectedVertex.getLine(i).setStartX(selectedPallino.getCenterX());
                selectedVertex.getLine(i).setStartY(selectedPallino.getCenterY());
                selectedVertex.getLine(i).setEndX(selectedVertex.getVicino(i).getCenterX());
                selectedVertex.getLine(i).setEndY(selectedVertex.getVicino(i).getCenterY());
                selectedVertex.getWeight(selectedVertex.getLine(i))
                        .setX((selectedVertex.getLine(i).getStartX() + selectedVertex.getLine(i).getEndX()) / 2);
                selectedVertex.getWeight(selectedVertex.getLine(i))
                        .setY((selectedVertex.getLine(i).getStartY() + selectedVertex.getLine(i).getEndY()) / 2 - 10);

            }
            isDragging = true;

        });

        vertexCircle.setOnMouseReleased(e -> {
            if (isDragging) {
                isDragging = false;
            }
        });
    }

    @FXML
    void buttonRandom(ActionEvent event) {
        Random rand = new Random();
        buttonReset(event);
        log.appendText("Generating a graph...\n");

        Timeline timeline = new Timeline();

        double frameDurationMillis = 100; // Adjust frame duration in milliseconds as needed

        for (int i = 0; i < 10; i++) {
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * frameDurationMillis), e -> {
                selectedVertex = graph.getRandomVertex();
                selectedPallino = selectedVertex.getCircle();
                buttonInsertRandomNode(event, rand.nextInt(639) + 20,
                        rand.nextInt(537) + 20, rand.nextInt(16) + 5);
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.setOnFinished(e -> {
            Vertex randomVertex1 = null;
            Vertex randomVertex2 = null;
            do {
                randomVertex1 = graph.getRandomVertex();
                randomVertex2 = graph.getRandomVertex();
            } while (checkVertex(randomVertex1, randomVertex2));

            addConnection(graph.getRandomVertex(), graph.getRandomVertex());
            log.appendText("Graph generated\n");
        });
        timeline.setCycleCount(1); // Run the timeline indefinitely
        timeline.play();
    }

    private boolean checkVertex(Vertex randomVertex1, Vertex randomVertex2) {
        ArrayList<Edge> vicini = randomVertex1.getEdges();
        if (vicini.contains(randomVertex2.getEdges())) {
            return true;
        }
        return false;
    }

    Random rand = new Random();

    private void addConnection(Vertex randomVertex, Vertex randomVertex2) {
        int weight = rand.nextInt(20) + 5;
        Edge edge = new Edge(weight, randomVertex, randomVertex2);

        Line connectionLine = new Line(randomVertex.getCircle().getCenterX(), randomVertex.getCircle().getCenterY(),
                randomVertex2.getCircle().getCenterX(), randomVertex2.getCircle().getCenterY());

        double weightTextX = (randomVertex.getCircle().getCenterX() + randomVertex2.getCircle().getCenterX()) / 2;
        double weightTextY = (randomVertex.getCircle().getCenterY() + randomVertex2.getCircle().getCenterY()) / 2 - 10;

        Text numberText;
        if (inputField.getText().equals("")) {
            numberText = new Text(weightTextX, weightTextY, Integer.toString(weight));
        } else {
            numberText = new Text(weightTextX, weightTextY, inputField.getText());
        }

        randomVertex.setEdge(edge);
        randomVertex.setVicino(randomVertex2);
        randomVertex.setLine(connectionLine, numberText);

        randomVertex2.setVicino(randomVertex);
        randomVertex2.setEdge(edge);
        randomVertex2.setLine(connectionLine, numberText);

        pane.getChildren().addAll(connectionLine, numberText);
        connectionLine.toBack();
    }

    @FXML
    void buttonReset(ActionEvent event) {
        graph.deleteGraph();
        pane.getChildren().clear();
        log.clear();

        // Reimposta le variabili allo stato iniziale
        indici = 0;
        selectedPallino = null;
        selectedVertex = null;
        graph = new Graph();

        // Avvia nuovamente la scena
        onStart();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onStart();
    }

    private void onStart() {
        Circle vertexCircle = new Circle(20, Color.BLUE);
        vertexCircle.setCenterX(350);
        vertexCircle.setCenterY(250);

        Vertex vertex = new Vertex(indici++, vertexCircle);
        graph.addVertex(vertex);

        Text numberText = new Text("0");
        numberText.setFill(Color.WHITE);
        numberText.setX(vertexCircle.getCenterX() - 5);
        numberText.setY(vertexCircle.getCenterY() + 5);
        numberText.setFont(Font.font("Arial", FontWeight.BOLD, 16)); // Adjust the font size as needed

        pane.getChildren().addAll(vertexCircle, numberText);

        vertexCircle.setOnMouseClicked(e -> {
            if (e.isControlDown()) {
                selectedPallino2 = vertexCircle;
                secondVertex = vertex;
                addConnection(secondVertex, selectedVertex);
                return;
            }
            selectedVertex = vertex;
            log.appendText("Vertex " + vertex.getIndiceVertice() + "\n");

            selectedPallino = vertexCircle;
            vertexCircle.setFill(Color.GREEN);
            selectedPallino.setFill(Color.BLUE);
        });

        vertexCircle.setOnMousePressed(e -> {
            mouseX = e.getSceneX() - vertexCircle.getCenterX();
            mouseY = e.getSceneY() - vertexCircle.getCenterY();
        });

        vertexCircle.setOnMouseDragged(e -> {
            double newCircleX = e.getSceneX() - mouseX;
            double newCircleY = e.getSceneY() - mouseY;

            vertexCircle.setCenterX(newCircleX);
            vertexCircle.setCenterY(newCircleY);

            numberText.setX(newCircleX - 5); // Update x coordinate of the index text
            numberText.setY(newCircleY + 5);

            for (int i = 0; i < selectedVertex.getSizeVicini(); i++) {
                selectedVertex.getLine(i).setStartX(selectedPallino.getCenterX());
                selectedVertex.getLine(i).setStartY(selectedPallino.getCenterY());
                selectedVertex.getLine(i).setEndX(selectedVertex.getVicino(i).getCenterX());
                selectedVertex.getLine(i).setEndY(selectedVertex.getVicino(i).getCenterY());
                selectedVertex.getWeight(selectedVertex.getLine(i))
                        .setX((selectedVertex.getLine(i).getStartX() + selectedVertex.getLine(i).getEndX()) / 2);
                selectedVertex.getWeight(selectedVertex.getLine(i))
                        .setY((selectedVertex.getLine(i).getStartY() + selectedVertex.getLine(i).getEndY()) / 2 - 10);
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
}