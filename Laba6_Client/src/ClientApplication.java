import Client.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;
import Controllers.*;


public class ClientApplication extends Application {

    public static void main(String[] args) {

        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setMinWidth(50);
        stage.setMinHeight(50);

        StartWindowController.setStage(stage);


        Parent content = FXMLLoader.load(getClass().getResource("registration.fxml"));
        Scene scene = new Scene(content);

        stage.setScene(scene);
        stage.setTitle("Life of vehicles");

        //ClientMain.init();
        stage.show();
    }
}

