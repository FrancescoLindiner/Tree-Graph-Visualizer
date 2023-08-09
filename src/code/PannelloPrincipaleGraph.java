package code;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PannelloPrincipaleGraph {

    Parent parent;
    Stage stage;
    Scene scene;

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
}