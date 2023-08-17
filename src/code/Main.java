package code;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    Stage window;
    Parent parent;
    Scene scene;
    
    @Override
    public void start(Stage stage) throws IOException {
        parent = FXMLLoader.load(getClass().getResource("../resources/PannelloPrincipale.fxml"));
        scene = new Scene(parent);
        stage.setScene(scene);

        Image icon = new Image(getClass().getResourceAsStream("../resources/img/icona.png"));
        stage.getIcons().add(icon);

        stage.centerOnScreen();
        stage.setTitle("Pannello principale");
        stage.setResizable(false);
        stage.show();  
    }

    public static void main(String[] args) {
        launch(args);
    }
}
