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

    public void onStart() {
        Circle pallino = new Circle(20, Color.BLUE);
        pallino.setCenterX(450);
        pallino.setCenterY(50);

        pane.getChildren().add(pallino);

        pallino.setOnMouseClicked(event -> {
            System.out.println("Hai cliccato il pallino!");
            
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

    }

    @FXML
    void buttonInsertRightNode(ActionEvent event) {

    }

    @FXML
    void buttonReset(ActionEvent event) {

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        onStart();
    }

}
