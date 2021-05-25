package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CommandIsDoneWindowController {
    private static Stage stage;

    public static void setStage(Stage stage) {
        CommandIsDoneWindowController.stage = stage;
    }



    @FXML
    private Button exitButton;



    @FXML
    void closeWindow(ActionEvent event) {
        stage.close();
    }

}
