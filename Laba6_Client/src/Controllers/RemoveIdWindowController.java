package Controllers;

import Client.ClientMain;
import Exceptions.NotThisVehicleInPossessionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

public class RemoveIdWindowController {
    private static Stage stage;

    public static void setStage(Stage stage) {
        RemoveIdWindowController.stage = stage;
    }
    private static Locale locale;
    private ResourceBundle rb;
    public static void setLocale(Locale locale) {
        RemoveIdWindowController.locale = locale;
    }

    @FXML
    private Label enterIdLabel;

    @FXML
    private TextField removeIdTextField;

    @FXML
    private Button removeIdButton;

    @FXML
    void removeId(ActionEvent event) throws IOException {
        Parent content = null;
        int check =0;
        try{
            String id_check = removeIdTextField.getText();
            int id = Integer.parseInt(id_check);
            if (id<=0) throw new NumberFormatException();

            String answer = ClientMain.removeId(id);
            if (!answer.equals("Element was deleted")) throw new NotThisVehicleInPossessionException();
            content = FXMLLoader.load(getClass().getResource("../commandIsDone.fxml"));

        }catch (NumberFormatException e){
            content = FXMLLoader.load(getClass().getResource("../commandIsNotDone.fxml"));
            check++;
        } catch (NotThisVehicleInPossessionException e) {
            Button btn = new Button("Ok");
            Label lbl = new Label("You don't have such element in your possession");
            VBox root = new VBox(lbl,btn);
            root.setSpacing(50);
            root.setAlignment(Pos.TOP_CENTER);

            Scene scene = new Scene(root, 400, 400);
            Stage stage1 = new Stage();
            stage1.setScene(scene);
            btn.setOnAction(kevent -> stage1.close());
            stage1.show();
            check = 2;
        } finally {
            if (check!=2){
                Scene scene = new Scene(content);
                Stage newStage = new Stage();
                newStage.setMinHeight(50);
                newStage.setMinWidth(50);
                newStage.setScene(scene);
                stage.close();
                newStage.setTitle("Life of vehicles");
                if (check==0){
                    CommandIsDoneWindowController.setStage(newStage);
                }else{
                    CommandIsNotDoneWindowController.setStage(newStage);
                }
                newStage.show();
            }

        }

    }
    @FXML
    void initialize(){

        initLocale();
    }
    private void initLocale() {
        rb = ResourceBundle.getBundle("text",locale);

        removeIdButton.setText(setString("remove.button"));
        enterIdLabel.setText(setString("enterIdToRemove.label"));

    }
    private String setString(String text){
        String stringToSet = rb.getString(text);
        stringToSet = new String(stringToSet.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        return stringToSet;
    }

}

