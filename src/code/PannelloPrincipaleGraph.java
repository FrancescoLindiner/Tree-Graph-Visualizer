package code;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    private Circle selectedPallino, selectedPallino2; // variable to memorize the selected circle
    private Vertex selectedVertex, dijkstraVertex;
    private double mouseX, mouseY; // Store the initial mouse click position
    private Vertex secondVertex; // second vertex when you want to connect two vertices
    GraphAlgorithms graphAlgorithms = new GraphAlgorithms();
    Random rand = new Random();

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
    void buttonInstruction(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("How to use the app");
        alert.setHeaderText(null);

        String bullet = "\u2022"; // Codice Unicode per il carattere del punto

        String contentText = bullet + " To insert a node, select a node and click 'Insert a node'\n"
                + bullet + " To move a node, double click on the node and move it\n"
                + bullet
                + " To connect two nodes, select a first node, then hold the 'Ctrl' key and select the second node\n"
                + bullet
                + " Dijkstra: To calculate the minimum path to a destination node, select a node then hold the 'Shift' key and select the node";

        alert.setContentText(contentText);

        String customStyle = "-fx-font-size: 16px;";
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(customStyle);

        alert.showAndWait();
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
        ArrayList<Vertex> path = graphAlgorithms.executeDijkstra(graph, selectedVertex, dijkstraVertex, log);
        if (path == null) {
            log.appendText("The graph contains edges with negative costs");
            return;
        }
        Timeline timeline = new Timeline();

        double frameDurationMillis = 500;

        for (int i = 0; i < path.size(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(index * frameDurationMillis), e -> {
                path.get(index).getCircle().setFill(Color.RED);
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.setCycleCount(1);
        timeline.play();
    }

    @FXML
    void buttonInsertNode(ActionEvent event) {
        Circle vertexCircle = new Circle(20, Color.BLUE); // create the circle
        Vertex vertex = new Vertex(indici++, vertexCircle); // create the vertex
        Edge edge;
        int weightRandom = -1;
        if (inputField.getText().equals("")) { // if input field is empty weigth is random
            weightRandom = rand.nextInt(41) - 20;
            edge = new Edge(weightRandom, selectedVertex, vertex);
        } else {
            edge = new Edge(Integer.parseInt(inputField.getText()), selectedVertex, vertex);
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

        log.appendText("Added vertex " + vertex.getIndiceVertice() + "\n");

        vertexCircle.setOnMouseClicked(e -> {
            if (e.isControlDown()) { // to add a connection
                selectedPallino2 = vertexCircle;
                secondVertex = vertex;
                addConnection(secondVertex, selectedVertex);
                return;
            }
            if (e.isShiftDown()) { // to select the destination node for dijkstra
                dijkstraVertex = vertex;
                return;
                // First is selectedVertex
                // Second is dijkstraVertex
            }
            selectedVertex = vertex;
            log.appendText("Vertex " + vertex.getIndiceVertice() + "\n");

            vertexCircle.setFill(Color.GREEN);
            selectedPallino.setFill(Color.BLUE);
            selectedPallino = vertexCircle;
        });

        // draw the line to connect the nodes
        Line connectionLine = new Line(selectedPallino.getCenterX(), selectedPallino.getCenterY(),
                vertexCircle.getCenterX(), vertexCircle.getCenterY());

        // Calculate the position for the weight text node
        double weightTextX = (selectedPallino.getCenterX() + vertexCircle.getCenterX()) / 2;
        double weightTextY = (selectedPallino.getCenterY() + vertexCircle.getCenterY()) / 2 - 10;

        Text weightText;
        if (inputField.getText().equals("")) {
            weightText = new Text(weightTextX, weightTextY, Integer.toString(weightRandom));
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
        });
    }

    @FXML
    void buttonKruskal(ActionEvent event) {
        ArrayList<Edge> mst = graphAlgorithms.executeKruskal(graph);
        for (Edge edge : mst) {
            edge.getV1().getCircle().setFill(Color.RED);
            edge.getV2().getCircle().setFill(Color.RED);
            ArrayList<Line> lines1 = edge.getV1().getLine();
            ArrayList<Line> lines2 = edge.getV2().getLine();
            Line line = lines1.get(0);
            for (Line line1 : lines1) {
                for (Line line2 : lines2) {
                    if (line1 == line2) {
                        line = line1;
                    }
                }
            }
            line.setStroke(Color.GREEN);
            for (Line l : lines1) {
                if (l.getStroke() != Color.GREEN) {
                    l.setStroke(Color.LIGHTGRAY);
                }
            }
            for (Line l : lines2) {
                if (l.getStroke() != Color.GREEN) {
                    l.setStroke(Color.LIGHTGRAY);
                }
            }
        }
    }

    @FXML
    void buttonPrim(ActionEvent event) {
        Timeline timeline = new Timeline();
        double frameDurationMillis = 700;

        Map<String, List<?>> result = graphAlgorithms.executePrim(selectedVertex, graph);
        List<Vertex> vertexList = (List<Vertex>) result.get("vertici");

        for (int i = 0; i < vertexList.size(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(index * frameDurationMillis), e -> {
                Vertex vertex = vertexList.get(index);
                vertex.getCircle().setFill(Color.RED);
                log.appendText("Vertex " + vertex.getIndiceVertice() + "\n");

                PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
                pauseTransition.setOnFinished(ev -> {
                    vertex.getLine(0).setStroke(Color.RED);
                });
                pauseTransition.play();
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.setCycleCount(1);
        timeline.play();
    }

    void buttonInsertRandomNode(ActionEvent event, double d, double f, int weight) {
        Circle vertexCircle = new Circle(20, Color.BLUE);
        Vertex vertex = new Vertex(indici++, vertexCircle);
        Edge edge;
        if (inputField.getText().equals("")) {
            edge = new Edge(weight, selectedVertex, vertex);
        } else {
            edge = new Edge(Integer.parseInt(inputField.getText()), selectedVertex, vertex);
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

        log.appendText("Added vertex " + vertex.getIndiceVertice() + "\n");

        vertexCircle.setOnMouseClicked(e -> {
            if (e.isControlDown()) {
                selectedPallino2 = vertexCircle;
                secondVertex = vertex;
                addConnection(secondVertex, selectedVertex);
                return;
            }
            if (e.isShiftDown()) {
                dijkstraVertex = vertex;
                return;
                // First is selectedVertex
                // Second is dijkstraVertex
            }
            selectedVertex = vertex;
            log.appendText("Vertex " + vertex.getIndiceVertice() + "\n");

            vertexCircle.setFill(Color.GREEN);
            selectedPallino.setFill(Color.BLUE);
            selectedPallino = vertexCircle;
        });

        Line connectionLine = new Line(selectedPallino.getCenterX(), selectedPallino.getCenterY(),
                vertexCircle.getCenterX(), vertexCircle.getCenterY());

        double weightTextX = (selectedPallino.getCenterX() + vertexCircle.getCenterX()) / 2;
        double weightTextY = (selectedPallino.getCenterY() + vertexCircle.getCenterY()) / 2 - 10;

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
        });
    }

    @FXML
    void buttonRandom(ActionEvent event) {
        Random rand = new Random();
        buttonReset(event);
        log.appendText("Generating a graph...\n");

        Timeline timeline = new Timeline();

        double frameDurationMillis = 100;

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

            addConnection(randomVertex1, randomVertex2);
            log.appendText("Graph generated\n");
        });
        timeline.setCycleCount(1);
        timeline.play();
    }

    private boolean checkVertex(Vertex randomVertex1, Vertex randomVertex2) {
        ArrayList<Edge> vicini1 = randomVertex1.getEdges();
        ArrayList<Edge> vicini2 = randomVertex2.getEdges();

        for (Edge edge : vicini1) {
            for (Edge edge2 : vicini2) {
                if (edge == edge2) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addConnection(Vertex randomVertex, Vertex randomVertex2) {
        int weight = rand.nextInt(20) + 5;

        Edge edge;
        if (inputField.getText().equals("")) {
            edge = new Edge(weight, randomVertex, randomVertex2);
        } else {
            edge = new Edge(Integer.parseInt(inputField.getText()), randomVertex, randomVertex2);
        }
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

        indici = 0;
        selectedPallino = null;
        selectedVertex = null;
        graph = new Graph();

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
        numberText.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        pane.getChildren().addAll(vertexCircle, numberText);

        vertexCircle.setOnMouseClicked(e -> {
            if (e.isControlDown()) {
                selectedPallino2 = vertexCircle;
                secondVertex = vertex;
                addConnection(secondVertex, selectedVertex);
                return;
            }
            if (e.isShiftDown()) {
                dijkstraVertex = vertex;
                return;
                // First is selectedVertex
                // Second is dijkstraVertex
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
        });
    }
}