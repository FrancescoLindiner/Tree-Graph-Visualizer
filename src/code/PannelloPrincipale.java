package code;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PannelloPrincipale {

    @FXML
    private Button button;

    Parent parent;
    Stage stage;
    Scene scene;

    @FXML
    void buttonGraph(ActionEvent event) throws IOException {
        parent = FXMLLoader.load(getClass()
                .getResource("../resources/PannelloPrincipaleGraph.fxml"));
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        scene = new Scene(parent, 900, 700);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Graph");
        stage.show();
    }

    @FXML
    void buttonTree(ActionEvent event) throws IOException {
        parent = FXMLLoader.load(getClass()
                .getResource("../resources/PannelloPrincipaleTree.fxml"));
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        scene = new Scene(parent, 900, 700);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Tree");
        stage.show();
    }
}