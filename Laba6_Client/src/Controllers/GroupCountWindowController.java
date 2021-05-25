package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class GroupCountWindowController {
    private static Stage stage;
    private static String info;
    public static void setStage(Stage stage,String message) {
        GroupCountWindowController.stage = stage;
        GroupCountWindowController.info = message;
    }
    @FXML
    private Button exitButton;

    @FXML
    private TextArea infoTextArea;

    @FXML
    private Button getIMaxButton;

    @FXML
    void getMax(ActionEvent event) {
        infoTextArea.appendText(info);
    }

    @FXML
    void exit(ActionEvent event) {
        stage.close();
    }

}
